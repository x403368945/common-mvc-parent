import gulp from 'gulp'
import watch from 'gulp-watch'
import batch from 'gulp-batch'
import zip from 'gulp-zip'
import flatten from 'gulp-flatten'
import rename from 'gulp-rename'
import tap from 'gulp-tap';
import template from 'gulp-template';
import del from 'del'
import fs from 'fs'
import path from 'path'
import readline from 'readline'
import _ from 'lodash';
import './src/utils/string-prototype'
import './src/utils/date-prototype'
import xlsx from 'xlsx';
import browser from 'browser-sync';
import {query} from './src/utils/db-execute';
import Paths from './src/utils/entity/Paths';
import {Table} from './src/db2java';
import Result from './src/utils/entity/Result';
import {devConfig} from './src/api/config'
import Asserts from './src/utils/entity/Asserts';
import OpenDemoTest from './test/api/OpenDemo.test';
import UserTest from './test/api/User.test';
import DemoListTest from './test/api/DemoList.test';

const web = browser.create();

const options = require('minimist')(process.argv.slice(2), {});

process.env.NODE_ENV = 'env';
process.env.DEVELOPMENT = true;
process.env.BROWSER = false;

gulp.task('default', async () => {
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
  await OpenDemoTest.of().testAll();
  await UserTest.of().testAll(); // 测试用户相关的接口
  await DemoListTest.of().testAll(); //
});
gulp.task('test:one', async () => {
  devConfig();
  (await UserTest.of().loginUserBasic())
    .updateNickname(); // 测试用户相关的接口
});

gulp.task('mysql', async () => {
  // # 查看所有表定义参数，where name = '指定表名'
  // SHOW TABLE STATUS FROM demo_main_db;
  // # 查看 DDL
  // SHOW CREATE TABLE tab_demo_list;
  // # 表全量信息
  // SHOW FULL COLUMNS FROM tab_demo_list;
  // # 表部分信息
  // DESCRIBE tab_demo_list;
  const mysql = require('mysql');
  const connection = mysql.createConnection({
    database: 'demo_main_db',
    host: 'localhost',
    user: 'root',
    password: '111111',
    port: '3306'
  });
  connection.connect();
  connection.query('SELECT * FROM tab_demo_list;',
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
gulp.task('markdown', async () => {
  const mysql = require('mysql');
  const connection = mysql.createConnection({
    database: 'demo_main_db',
    host: 'localhost',
    user: 'root',
    password: '111111',
    port: '3306'
  });
  connection.connect();
  const tables = await query(connection, 'SHOW TABLE STATUS FROM demo_main_db')
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
    writer.write(`select * from ${name}\n\n`);
    { // markdown 字段表格部分
      writer.write('|字段|类型|允许空|默认值|描述|\n');
      writer.write('|----|----|----|----|----|\n');
      const columns = await query(connection, `SHOW FULL COLUMNS FROM  ${name}`);
      columns.forEach(({Field, Type, Collation, Null, Default, Comment}) =>
        writer.write(`|${Field}|${Type}|${Null}|${Default || ''}|${Collation ? Collation + ',' : ''}${Comment}|\n`));
      // console.log(columns);
    }
    writer.write('\n\n\n');
    // { // DDL 部分
    //     writer.write('```mysql\n');
    //     let [{Table: tableName, 'Create Table': ddl}] = await query(connection, `SHOW CREATE TABLE ${tables[i].name}`);
    //     // console.log([tableName, ddl]);
    //     writer.write(ddl);
    //     writer.write('\n```');
    // }
    writer.end();
  }
  connection.end();
});
gulp.task('db:java', async () => {
//     console.log(`命令需要带参数，代码会默认生成在根目录下的 src/test/java/ ：参考命令：
// gulp db:java --db demo_main_db --table tab_demo_list --template all_id_long_uid --pkg com.ccx.business
//
// 参数说明：
// --db {操作数据库名称：必填}
// --table {指定表名：必填，其实 table 是可选的，如果不指定 table 则所有表的代码都会生成代码}
// --template 选择模板代码目录：必填[
//                               "all_id_long => 全部 CRUD 代码[id:Long]",
//                               "all_id_long_uid => 全部 CRUD 代码[id:Long,uid:String]",
//                               "all_id_string => 全部 CRUD 代码[id:String]",
//                               "search_simple_id_long => 仅支持查询代码[id:Long]",
//                               "search_simple_id_string => 仅支持查询代码[id:String]",
//                               ]
// --pkg {指定输出文件包名：默认：com.ccx.demo}
//
// --user {操作用户：默认 root}
// --password {操作密码：默认 111111}
// --host {数据库主机：默认 localhost}
// --port {端口：默认 3306}
//     `);
  const {
    db: database = 'demo_main_db',
    host = 'localhost',
    user = 'root',
    password = '111111',
    port = '3306',

    // 表名
    table = [
      'tab_convert'
    ],
    // 模块名
    module = 'demo-service',
    // 包名(也会作为文件输出目录)
    pkg = 'com.ccx.demo',
    // 模板代码存放目录名
    template = 'all_id_long_uid'
  } = options;
  const mysql = require('mysql');
  console.log([{
    database,
    host,
    user,
    password,
    port,
    table,
    template,
    pkg
  }]);
  const connection = mysql.createConnection({
    database,
    host,
    user,
    password,
    port
  });
  connection.connect();
  const tables = await query(connection, `SHOW TABLE STATUS FROM ${database} `.concat(table && `where Name in ('${table.join("','")}')`));
  console.table(tables);
  for (let i = 0, len = tables.length; i < len; i++) {
    const table = new Table(tables[i]);
    const columns = await query(connection, `SHOW FULL COLUMNS FROM ${table.name}`);
    console.table(columns);
    // console.log(table)
    table.setColumns(columns)
      .setOutput(module, pkg)
      .writeController(template, pkg)
      .writeEntity(template, pkg)
      .writeService(template, pkg)
      .writeRepository(template, pkg)
    ;
  }
  connection.end();
});
gulp.task('in:out', () => {
  const writer = fs.createWriteStream(path.resolve('src/out.tmp'));
  writer.on('close', () => console.log('end'));
  readline.createInterface(
    fs.createReadStream(path.resolve('src/in.tmp')),
    writer
  )
    .on('line', line => {
      writer.write(line.concat(',\n'))
    })
    .on('close', () => writer.end())
});
gulp.task('write:line', () => {
  const writer = fs.createWriteStream(path.resolve('temp/test.log'));
  writer.on('close', () => console.log('end'));
  writer.write('测试\n');
  writer.write('测试\n');
  writer.write('测试\n');
  writer.end();
});
gulp.task('read:line', () => {
  readline.createInterface(fs.createReadStream(path.resolve('temp/test.log')))
    .on('line', text => {
      console.log(text)
    })
    .on('close', () => console.log('end'));
});

gulp.task('xlsx', () => {
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

gulp.task('exec', () => {
  const proc = require('child_process');
  proc.exec('ls', function (error, stdout, stderr) {
    console.log(stdout);
    if (error !== null) {
      console.log('exec error: ' + error);
    }
  });
});

gulp.task('stdout', () => {
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
