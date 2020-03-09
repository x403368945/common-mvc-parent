import gulp from 'gulp'
import watch from 'gulp-watch'
import batch from 'gulp-batch'
import fs from 'fs'
import path from 'path'
import readline from 'readline'
import _ from 'lodash';
import './src/utils/string-prototype'
import './src/utils/date-prototype'
import xlsx from 'xlsx';
import browser from 'browser-sync';
import {query} from './src/utils/db-execute';
import Table from './src/code/core/Table';
import {devConfig} from './src/api/config'
import UserTest from './test/api/User.test';
import DemoListTest from './test/api/DemoList.test';
import AuthorityTest from './test/api/Authority.test';
import RoleTest from './test/api/Role.test';

const web = browser.create();

const options = require('minimist')(process.argv.slice(2), {});

process.env.NODE_ENV = 'env';
process.env.DEVELOPMENT = true;
process.env.BROWSER = false;

gulp.task('default', async () => {
const vo = undefined;
// const obj = {...vo};
console.log([vo || 'aaa'])
});

gulp.task('listener', function () {
  watch('src/**/*.html', batch(function () {
    web.reload();
    gulp.start('build');
  }));
  // gulp.watch([
  //     'src/**/*.js'
  // ], function() {
  //     console.log('js');
  //     return gulp.src('src/**/*.js')
  //         .pipe(plugins.changed('dest'))
  //         .pipe(plugins.debug({title: 'scripts监听修改文件:'}))
  //         .pipe(gulp.dest('dest'))
  //         .pipe(reload({stream: true}));
  // });
  // gulp.watch('src/**/*.css', function() {
  //     console.log('css');
  //     return gulp.src('src/**/*.css')
  //         .pipe(plugins.changed('dest'))
  //         .pipe(plugins.debug({title: 'css监听修改文件:'}))
  //         .pipe(gulp.dest('dest'))
  //         .pipe(web.stream());
  // });
  return Promise.resolve();
});
gulp.task('server', gulp.series('listener', () => {
  web.init({
    // proxy: 'http://localhost/index.html'
    server: {
      baseDir: 'src/',
      index: 'index.html'
    }
  });
}));

gulp.task('test', async () => {
  devConfig();
  // await OpenDemoTest.of().testAll();
  await DemoListTest.of().testAll(); //
  await UserTest.of().testAll(); // 测试用户相关的接口
  await AuthorityTest.of().testAll();
  await RoleTest.of().testAll();
});
gulp.task('test:one', async () => {
  devConfig();
  (await UserTest.of().loginUserBasic());
});

gulp.task('mysql:read:write', async () => {
  // # 查看所有表定义参数，where name = '指定表名'
  // SHOW TABLE STATUS FROM demo_main_db;
  // # 查看 DDL
  // SHOW CREATE TABLE tab_demo_list;
  // # 表全量信息
  // SHOW FULL COLUMNS FROM tab_demo_list;
  // # 表部分信息
  // DESCRIBE tab_demo_list;
  // # 查看表数据行数
  // SELECT TABLE_NAME, TABLE_ROWS FROM INFORMATION_SCHEMA.PARTITIONS WHERE TABLE_SCHEMA = 'demo_main_db'
  const mysql = require('mysql');
  const connection = mysql.createConnection({
    host: 'localhost',
    port: '3306',
    user: 'root',
    password: '111111',
    database: 'demo_main_db'
  });
  connection.connect();
  connection.query('SELECT * FROM tab_user;',
    (err, result) => {
      if (err) {
        console.error('[SELECT ERROR] - ', err.message);
        return;
      }
      const wb = xlsx.utils.book_new();
      xlsx.utils.book_append_sheet(wb, xlsx.utils.json_to_sheet(result));
      xlsx.writeFile(wb, './temp/tab_name.xlsx');
      console.table(result);
    });
  connection.end();
});
gulp.task('mysql:markdown', async () => {
  const mysql = require('mysql');
  const db = {
    host: 'localhost',
    port: '3306',
    user: 'root',
    password: '111111',
    database: 'demo_main_db'
  };
  const connection = mysql.createConnection(db);
  connection.connect();
  const tables = await query(connection, `SHOW TABLE STATUS FROM ${db.database} WHERE engine IS NOT NULL;`)
    .then(async result => {
      // console.table(result);
      return result.map(({Name, Engine, Collation, Comment}) =>
        Object.assign({
          name: Name,
          engine: Engine,
          collation: Collation,
          comment: Comment
        }));
    });
  console.table(tables);
  for (let i = 0, len = tables.length; i < len; i++) {
    const {name, engine, collation, comment} = tables[i];
    const writer = fs.createWriteStream(path.resolve(`temp/md/${name}.md`));
    writer.write(`# ${comment}\n`);
    writer.write(`select * from ${name};\n\n`);
    { // markdown 字段表格部分
      writer.write('|字段|类型|允许空|默认值|描述|\n');
      writer.write('|----|----|----|----|----|\n');
      const columns = await query(connection, `SHOW FULL COLUMNS FROM  ${name}`);
      columns.forEach(({Field, Type, Collation, Null, Default, Comment}) =>
        writer.write(`|${Field}|${Type}|${Null}|${Default || '-'}|${Comment || '-'}|\n`));
      // console.log(columns);
    }
    writer.write('\n\n\n');
    // { // DDL 部分
    //     writer.write('```mysql\n');
    //     const [{'Create Table': ddl}] = await query(connection, `SHOW CREATE TABLE ${name}`);
    //     // console.log([tableName, ddl]);
    //     writer.write(ddl);
    //     writer.write('\n```');
    // }
    writer.end();
  }
  connection.end();
});

async function db2java(option) {
  const {
    host, // = 'localhost',
    port, // = '3306',
    user, // = 'root',
    password, // = '111111',
    database, // = 'demo_main_db',
    table, // = ['ny_order'], 表名
    module, // = 'demo-service', 模块名
    pkg, // = 'com.ccx.demo', 包名(也会作为文件输出目录)
    template // = 'AuthUid.js' 代码模板
  } = option;

  const mysql = require('mysql');
  console.log([{database, host, user, password, port, table, template, pkg}]);
  const connection = mysql.createConnection({database, host, user, password, port});
  connection.connect();
  const tables = await query(connection, `SHOW TABLE STATUS FROM ${database} `.concat(table && `where Name in ('${table.join("','")}')`));
  console.table(tables);
  for (let i = 0, len = tables.length; i < len; i++) {
    const table = new Table(tables[i]);
    const columns = await query(connection, `SHOW FULL COLUMNS FROM ${table.name}`);
    console.table(columns);
    // console.log(table)
    table
      .setPkg(pkg)
      .setColumns(columns)
      .setOutput(module)
    ;
    await table.writeHttp(template.replace(/\.js/, ''));
    await table.writeController(template.replace(/\.js/, ''));
    await table.writeEntity(template.replace(/\.js/, ''));
    await table.writeService(template.replace(/\.js/, ''));
    await table.writeRepository(template.replace(/\.js/, ''));
  }
  connection.end();
}

gulp.task('db:java:code', async () => {
  await db2java({
    host: 'localhost',
    port: '3306',
    user: 'root',
    password: '111111',
    database: 'demo_main_db',
    table: [
      'tab_user',
      'tab_role'
    ], // 表名
    module: '../demo-main', // 模块名
    pkg: 'com.ccx.demo', // 包名(也会作为文件输出目录)
    template: 'AuthUid.js' // 代码模板
  });
});

gulp.task('in:out', async () => {
  const writer = fs.createWriteStream(path.resolve('temp/out.txt'));
  writer.on('close', () => console.log('end'));
  readline.createInterface(
    fs.createReadStream(path.resolve('temp/in.txt')),
    writer
  )
    .on('line', line => {
      writer.write(line.concat(',\n'))
    })
    .on('close', () => writer.end())
});
gulp.task('write:line', async () => {
  const writer = fs.createWriteStream(path.resolve('temp/test.log'));
  writer.on('close', () => console.log('end'));
  writer.write('测试\n');
  writer.write('测试\n');
  writer.write('测试\n');
  writer.end();
});
gulp.task('read:line', async () => {
  readline.createInterface(fs.createReadStream(path.resolve('temp/test.log')))
    .on('line', text => {
      console.log(text)
    })
    .on('close', () => console.log('end'));
});

gulp.task('xlsx:read', async () => {
  const wb = xlsx.readFile(
    './temp/数据文件.xls'
  );
  const file = 'temp/数据文件.sql';
  const writer = fs.createWriteStream(file);
  writer.on('close', () => console.log(path.resolve(file)));
  // const sheet = wb.Sheets[wb.SheetNames.shift()];
  const sheet = wb.Sheets['SQL Results'];
  sheet['!ref'] = sheet['!ref'].replace(/\w+:(\w+)/, 'B1:$1');
  const arrs = xlsx.utils.sheet_to_json(sheet)
    .map(row => {
      _.forEach(row, (v, k) => {
        if (v === '') delete row[k]
      });
      writer.write(
        `${Object.values(row).join(',')}\n`
      );
      return row;
    })
  ;
  writer.end();
});
gulp.task('xlsx:write', async () => {
  const {data: {errorInfos: array}} = JSON.parse('');
  array.sort((a, b) => a.rowNumber - b.rowNumber);
  const wb = xlsx.utils.book_new();
  xlsx.utils.book_append_sheet(wb, xlsx.utils.json_to_sheet(array), 'Sheet1');
  xlsx.writeFile(wb, './temp/write.xlsx');
});
gulp.task('xlsx:付息方式', async () => {
  const wb = xlsx.readFile('./temp/付息方式.xls');
  const file = 'temp/付息方式.sql';
  const writer = fs.createWriteStream(file);
  writer.on('close', () => console.log(path.resolve(file)));
  // const sheet = wb.Sheets[wb.SheetNames.shift()];
  const sheet = wb.Sheets.Sheet1;
  // sheet['!ref'] = sheet['!ref'].replace(/\w+:(\w+)/, 'B1:$1');
  const arrs = xlsx.utils.sheet_to_json(sheet)
    .map(row => {
      const [table, name] = row['表.字段'].split('.');
      const value = row['值'].replace(/\\''/, '');
      console.log(row);
      if (value === 'NULL') {
        writer.write(`update ${table} set ${name}='${row['付息方式（数字）']}' where ${name} is null;\n`);
      } else {
        writer.write(`update ${table} set ${name}='${row['付息方式（数字）']}' where ${name}='${value}';\n`);
      }
      return row;
    })
  ;
  writer.end();
});

gulp.task('exec', async () => {
  const proc = require('child_process');
  proc.exec('ls', function (error, stdout, stderr) {
    console.log(stdout);
    if (error !== null) {
      console.log('exec error: ' + error);
    }
  });
});

gulp.task('stdout', async () => {
  process.stdin.setEncoding('utf8');
  process.stdin.on('readable', () => {
    const chunk = process.stdin.read();
    if (chunk) {
      process.stdout.write(`data: ${chunk}`);
      process.exitCode = 1;
    }
  });
  process.stdin.on('end', () => {
    process.stdout.write('end');
  });
});

gulp.task('ssr', async () => {
  // const content = fs.readFileSync('temp/console.txt');
  // fs.writeFileSync('temp/ssr.txt',Buffer.from(content).toString('base64'));
  //
  // const decode = fs.createWriteStream(path.resolve('temp/decode.txt'));
  // decode.on('close', () => console.log('end'));
  // readline.createInterface(
  //   fs.createReadStream(path.resolve('temp/console.txt')),
  //   decode
  // )
  //   .on('line', line => {
  //     decode.write(Buffer.from(line.replace(/^ssr:\/\//, ''), 'base64').toString('ascii').concat('\n'))
  //   })
  //   .on('close', () => decode.end())

  const shell = fs.createWriteStream(path.resolve('temp/ssr.sh'));
  shell.write('#!/bin/bash\n');
  readline.createInterface(
    fs.createReadStream(path.resolve('temp/console.txt')),
    shell
  )
    .on('line', line => {
      shell.write(`echo '${line}'\n`);
      shell.write(Buffer.from(line.replace(/^ssr:\/\//, ''), 'base64').toString('ascii').concat('\n'))
    })
    .on('close', () => shell.end())
});
