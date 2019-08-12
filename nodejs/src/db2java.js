import Paths from './utils/Paths';
import gulp from 'gulp'
import rename from 'gulp-rename';
import template from 'gulp-template';

/**
 * 数据库表信息
 */
export class Table {
  /**
   * 表信息，通过 SHOW TABLE STATUS FROM #{databaseName} 获取到的参数
   * @param Name {string} 表名
   * @param Engine {string} 存储引擎
   * @param Collation {string} 字符集
   * @param Comment {string} 说明
   */
  constructor({Name, Engine, Collation, Comment}) {
    /**
     * 表名
     * @type {string}
     */
    this.name = Name;
    /**
     * 存储引擎
     * @return {string}
     */
    this.engine = Engine;
    /**
     * 默认字符集
     * @return {string}
     */
    this.collation = Collation;
    /**
     * 说明
     * @return {string}
     */
    this.comment = Comment.replace(/\s/g, ' ').replace(/"/g, '\'');
    /**
     * 基于表名生成各种命名
     * @type {Names}
     */
    this.names = new Names(this.name);
    this.setAdapters();
  }

  /**
   * 设置列属性
   * @param columns {Array<Object>} 列集合
   * @result {Table}
   */
  setColumns(columns = []) {
    /**
     * 列属性
     * @return {Column[]}
     */
    this.columns = columns.map(obj => new Column(obj));
    return this;
  }

  /**
   * 设置特定字段或枚举字段适配策略
   * @param adapters {Array<BaseAdapter>} 自定义枚举字段适配策略
   * @result {Table}
   */
  setAdapters(adapters = [new BaseAdapter()]) {
    /**
     * 自定义枚举字段适配策略
     * @return {Array<BaseAdapter>} 自定义枚举字段适配策略
     */
    this.adapters = adapters;
    return this;
  }

  /**
   * 设置输出包目录
   * @param module {string} 模块名
   * @param pkg {string} 包名
   * @result {Table}
   */
  setOutput(module, pkg) {
    this.output = `../${module}/src/test/java/${pkg.replace(/\./g, '/')}/code/${this.names.javaname}`;
    return this;
  }

  /**
   * 写入 Controller
   *
   * (\S+)?IAuthController          => com.support.mvc.web.IController
   * @AuthenticationPrincipal.*\s+   =>
   * ,\s+user.getId\(\)              =>
   *
   * @param templateDirName {string} 模板目录
   * @param pkg {string} 输出包名
   * @return {Table}
   */
  writeController(templateDirName, pkg) {
    const filename = `web/${this.names.JavaName.concat('Controller.java')}`;
    console.log(Paths.resolve(this.output, filename).absolute());
    gulp.src(`templates/${templateDirName}/Controller.java`)
      .pipe(template(Object.assign(this.names.toObject(), {
        pkg,
        comment: this.comment,
        date: new Date().formatDate(),
        id: (this.columns.find(({name}) => name === 'id') || {dataType: {}}).dataType.value
      })))
      .pipe(rename(filename))
      .pipe(gulp.dest(this.output));
    return this;
  }

  /**
   * 写入实体
   * @param templateDirName {string} 模板目录
   * @param pkg {string} 输出包名
   * @return {Table}
   */
  writeEntity(templateDirName, pkg) {
    const filename = `entity/${this.names.TabName.concat('.java')}`;
    console.log(Paths.resolve(this.output, filename).absolute());
    gulp.src(`templates/${templateDirName}/Entity.java`)
      .pipe(template(Object.assign(this.names.toObject(), {
        pkg,
        comment: this.comment,
        date: new Date().formatDate(),
        orders: this.columns.map(({name}) => `"${name}"`).join(', '),
        IUser: this.columns.some(({name}) => ['createUserId', 'modifyUserId'].includes(name)) ? 'IUser,' : '',
        ITimestamp: this.columns.some(({name}) => name === 'modifyTime') ? 'ITimestamp, // 所有需要更新时间戳的实体类' : '',
        fields: this.columns.map(column => column.field(this.adapters)).filter(Boolean).join('\n'),
        props: this.columns.map(column => column.prop(this.adapters)).filter(Boolean).join(',\n'),
        orderBy: this.columns.map(column => column.orderBy(this)).filter(Boolean).join(',\n'),
        update: this.columns.map(column => column.update()).filter(Boolean).join('\n'),
        where: this.columns.map(column => column.where()).filter(Boolean).join('\n')
      })))
      .pipe(rename(filename))
      .pipe(gulp.dest(this.output));
    return this;
  }

  /**
   * 写入 Service
   *
   * ISimpleService
   * IService => ISimpleService
   * , userId => null
   * , \w+ \w+ userId =>
   *
   * IService:String id
   * (\S+)?IService => com.support.mvc.service.str.IService
   *
   *
   * ISimpleService:String id
   * (\S+)?IService => com.support.mvc.service.str.ISimpleService
   * , userId => null
   * , \w+ \w+ userId =>
   *
   * @param templateDirName {string} 模板目录
   * @param pkg {string} 输出包名
   * @return {Table}
   */
  writeService(templateDirName, pkg) {
    const filename = `service/${this.names.JavaName.concat('Service.java')}`;
    console.log(Paths.resolve(this.output, filename).absolute());
    gulp.src(`templates/${templateDirName}/Service.java`)
      .pipe(template(Object.assign(this.names.toObject(), {
        pkg,
        comment: this.comment,
        date: new Date().formatDate(),
        id: (this.columns.find(({name}) => name === 'id') || {dataType: {}}).dataType.value
      })))
      .pipe(rename(filename))
      .pipe(gulp.dest(this.output));
    return this;
  }

  /**
   * 写入 Repository
   *
   * .and(q.createUserId.eq(userId)) =>
   * .createUserId(userId)           =>
   * .set(q.modifyUserId, userId)    =>
   *
   * modifyTime 字段不存在
   *.and(q.modifyTime.eq(obj.getModifyTime())) =>
   *
   * @param templateDirName {string} 模板目录
   * @param pkg {string} 输出包名
   * @return {Table}
   */
  writeRepository(templateDirName, pkg) {
    const filename = `dao/jpa/${this.names.JavaName.concat('Repository.java')}`;
    console.log(Paths.resolve(this.output, filename).absolute());
    gulp.src(`templates/${templateDirName}/Repository.java`)
      .pipe(template(Object.assign(this.names.toObject(), {
        pkg,
        comment: this.comment,
        date: new Date().formatDate(),
        id: (this.columns.find(({name}) => name === 'id') || {dataType: {}}).dataType.value
      })))
      .pipe(rename(filename))
      .pipe(gulp.dest(this.output));
    return this;
  }
}

const TINYINT_MIN_VALUE = -128;
const TINYINT_MAX_VALUE = 127;
const SHORT_MIN_VALUE = -32768;
const SHORT_MAX_VALUE = 32767;
const INTEGER_MIN_VALUE = -2147483648;
const INTEGER_MAX_VALUE = 2147483647;
const LONG_MIN_VALUE = -9223372036854775808;
const LONG_MAX_VALUE = 9223372036854775807;

/**
 * 数据库字段信息
 */
export class Column {
  /**
   * 通过 SHOW FULL COLUMNS FROM #{tableName} 获取到的参数
   * @param Field {string} 字段名
   * @param Type {string} 数据库类型
   * @param Collation {string} 字符集
   * @param Null {string} 是否允许 null ，【YES:允许 null 值，NO:不允许 null 值】
   * @param Default {string} 默认值
   * @param Comment {string} 字段说明
   */
  constructor({Field, Type, Collation, Null, Default, Comment}) {
    /**
     * 实体属性名，驼峰命名法
     * @type {string}
     */
    this.name = Field.replace(/^is_/, '') // 数据库中 is 开头的字段生成实体需要去掉 is_ ，然后在属性头上注解数据库字段名
      .replace(/_([a-zA-Z])/g, (m, $1) => $1.toUpperCase());
    /**
     * 数据库字段名，带下划线
     * @type {string}
     */
    this.db_name = Field;
    const [_matchFull, type, _matchLength, length, fixed] = Type.match(/^(\w+)(\((\d+),?(\d+)?\))?.*/);
    /**
     * 数据类型
     * @type {string}
     */
    this.type = type.toUpperCase();
    /**
     * 长度
     * @type {number}
     */
    this.length = length && parseInt(length);
    /**
     * 小数位长度
     * @type {number}
     */
    this.fixed = fixed && parseInt(fixed);
    /**
     * 是否允许负数
     * @type {boolean}
     */
    this.unsigned = Type.toUpperCase().includes('UNSIGNED');
    /**
     * 字段字符集
     * @type {string}
     */
    this.collation = Collation;
    this.defaultValue = Default;
    /**
     * 是否必填，数据库中 not null 且没有指定默认值时，判断为必填项，用于校验注解
     * @type {boolean} true:是，false:否
     */
    this.notNull = Null === 'NO' && !Default;
    /**
     * 字段说明
     * @type {string}
     */
    this.comment = Comment.replace(/\s/g, ' ').replace(/"/g, '\'');
    /**
     * 数据库与java数据类型映射
     * @type {Type}
     */
    this.dataType = DataType[this.type];
  }

  /**
   * 生成实体类属性字段定义代码
   * @param adapters {Array<BaseAdapter>} 自定义枚举字段适配策略
   */
  field(adapters = [new BaseAdapter()]) {
    const adapter = adapters.map(o => o.fields).reduce((s, v) => Object.assign(s, v), {});
    return `    /**\n     * ${this.comment}\n     */\n`.concat((adapter[this.name] || adapter['default'])(this));
  }

  /**
   * 生成实体属性与数据库映射枚举
   * @param adapters {Array<BaseAdapter>} 自定义枚举字段适配策略
   */
  prop(adapters = [new BaseAdapter()]) {
    const adapter = adapters.map(o => o.props).reduce((s, v) => Object.assign(s, v), {});
    // const defaultProp = () => `        ${this.name}(${this.dataType.value.toUpperCase()}.build(${this.notNull ? 'true, ' : ''}"${this.comment}"))`;
    // 先从 adapter 中获取 prop 适配策略，未定义策略时使用默认策略
    return (adapter[this.name] || adapter['default'])(this);
  }

  /**
   * 生成 order by 代码
   * @param table {Table}
   * @return {string}
   */
  orderBy(table) {
    return `        ${this.name}(${table.names.tabName}.${this.name})`
  }

  /**
   * 生成 update 代码
   * @param excludes {Array<String>} 指定不在更新范围的字段
   * @return {string}
   */
  update(excludes = ['id', 'uid', 'deleted', 'createTime', 'createUserId', 'modifyTime']) {
    return excludes.includes(this.name) ? '' : `//                .then({name}, update -> update.set(q.{name}, {name}))`.formatObject({name: this.name})
  }

  /**
   * 生成 where 条件代码
   * @return {string}
   */
  where() {
    switch (this.dataType.name) {
      case 'CHAR':
        return '//                .and({name}, () -> {name}.endsWith("%") || {name}.startsWith("%") ? q.{name}.like({name}) : q.{name}.eq({name}))'.formatObject({name: this.name});
      case 'VARCHAR':
        return this.length < 100
          ? '//                .and({name}, () -> {name}.endsWith("%") || {name}.startsWith("%") ? q.{name}.like({name}) : q.{name}.eq({name}))'.formatObject({name: this.name})
          : '//                .and({name}, () -> {name}.endsWith("%") || {name}.startsWith("%") ? q.{name}.like({name}) : q.{name}.startsWith({name}))'.formatObject({name: this.name});
      case 'TEXT':
      case 'MEDIUMTEXT':
      case 'LONGTEXT':
        return '//                .and({name}, () -> {name}.endsWith("%") || {name}.startsWith("%") ? q.{name}.like({name}) : q.{name}.contains({name}))'.formatObject({name: this.name});
      case 'DECIMAL':
      case 'DOUBLE':
      case 'FLOAT':
        return '//                .and({name}Range, () -> q.{name}.between({name}Range.getMin(), {name}Range.getMax()))'.formatObject({name: this.name});
      case 'DATE':
      case 'DATETIME':
      case 'TIMESTAMP':
        return '//                .and({name}Range, () -> q.{name}.between({name}Range.rebuild().getBegin(), {name}Range.getEnd()))'.formatObject({name: this.name});
    }
    return '//                .and({name}, () -> q.{name}.eq({name}))'.formatObject({name: this.name});
  }
}

/**
 * 固定字段处理策略
 * ^.*\("\\\\t(.*)\\\\n.*
 *     $1
 */
export class BaseAdapter {
  constructor() {
    this.fields = {
      // 默认字段生成策略
      default: ({name, db_name, dataType, notNull, unsigned, length, fixed}) => {
        const list = [];
        if (notNull) list.push(`    @NotNull`);
        if (db_name.startsWith('is_')) list.push(`    @Column(name = "${db_name}")`); // 数据库自字段 is_ 开头的特殊处理
        switch (dataType.name) {
          case 'TINYINT':
            if (unsigned) {
              list.push('    @Min(0)');
              if (length) list.push(`    @Max(${Math.min(TINYINT_MAX_VALUE, parseInt('9'.repeat(length)))})`);
            } else if (length) { // 有符号表示有负数，所以要除 2
              list.push(`    @Min(${Math.floor(Math.max(TINYINT_MIN_VALUE, parseInt('-'.concat('9'.repeat(length))) / 2))})`);
              list.push(`    @Max(${Math.floor(Math.min(TINYINT_MAX_VALUE, parseInt('9'.repeat(length)) / 2))})`);
            }
            break;
          case 'SMALLINT':
            if (unsigned) {
              list.push('    @Min(0)');
              if (length) list.push(`    @Max(${Math.min(SHORT_MAX_VALUE, parseInt('9'.repeat(length)))})`);
            } else if (length) { // 有符号表示有负数，所以要除 2
              list.push(`    @Min(${Math.floor(Math.max(SHORT_MIN_VALUE, parseInt('-'.concat('9'.repeat(length))) / 2))})`);
              list.push(`    @Max(${Math.floor(Math.min(SHORT_MAX_VALUE, parseInt('9'.repeat(length)) / 2))})`);
            }
            break;
          case 'MEDIUMINT':
          case 'INT':
            if (unsigned) {
              list.push('    @Min(0)');
              if (length) list.push(`    @Max(${Math.min(INTEGER_MAX_VALUE, parseInt('9'.repeat(length)))})`);
            } else if (length) { // 有符号表示有负数，所以要除 2
              list.push(`    @Min(${Math.floor(Math.max(INTEGER_MIN_VALUE, parseInt('-'.concat('9'.repeat(length))) / 2))})`);
              list.push(`    @Max(${Math.floor(Math.min(INTEGER_MAX_VALUE, parseInt('9'.repeat(length)) / 2))})`);
            }
            break;
          case 'BIGINT':
            if (unsigned) {
              list.push('    @Min(0)');
              if (length) list.push(`    @DecimalMax("${Math.min(LONG_MAX_VALUE, parseInt('9'.repeat(length)))}")`);
            } else if (length) { // 有符号表示有负数，所以要除 2
              list.push(`    @DecimalMin("${Math.floor(Math.max(LONG_MIN_VALUE, parseInt('-'.concat('9'.repeat(length))) / 2))}")`);
              list.push(`    @DecimalMax("${Math.floor(Math.min(LONG_MAX_VALUE, parseInt('9'.repeat(length)) / 2))}")`);
            }
            break;
          case 'DECIMAL':
          case 'DOUBLE':
          case 'FLOAT':
            list.push(`    @Digits(integer = ${length}, fraction = ${fixed || 0})`);
            break;
          case 'CHAR':
            list.push(`    @Size(min = ${length}, max = ${length})`);
            break;
          case 'VARCHAR':
            list.push(`    @Size(max = ${length})`);
            break;
          case 'TEXT':
            list.push(`    @Size(max = ${Math.min(65535, length || 65535)})`);
            break;
          case 'MEDIUMTEXT':
            list.push(`    @Size(max = ${Math.min(65535 * 2, length || 65535 * 2)})`);
            break;
          case 'LONGTEXT':
            list.push(`    @Size(max = ${Math.min(65535 * 4, length || 65535 * 4)})`);
            break;
          case 'DATE':
            list.push('    @JSONField(format = "yyyy-MM-dd")');
            break;
          case 'DATETIME':
          case 'TIMESTAMP':
            list.push('    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")');
            break;
        }
        list.push(`    private ${dataType.value} ${name};`);
        return list.join('\n');
      },
      id: ({name, dataType, length}) => {
        if ([DataType.BIGINT.name, DataType.INT.name].includes(dataType.name)) {
          return `    @Id\n    @GeneratedValue(strategy = GenerationType.IDENTITY)\n    @NotNull(groups = {IUpdate.class, IMarkDelete.class})\n    @Positive\n    private Long ${name};`
        } else {
          return `    @Id\n    @NotBlank(groups = {IUpdate.class, IMarkDelete.class})\n    @Size(max = ${length})\n    private String ${name};`
        }
      },
      uid: ({name}) => `    @Column(updatable = false)\n    @NotNull(groups = {IUpdate.class, IMarkDelete.class})\n    @Size(min = 32, max = 32)\n    private String ${name};`,
      deleted: ({name}) => `    @Column(insertable = false, updatable = false)\n    @Null(groups = {ISave.class})\n    private Radio ${name};`,
      createTime: ({name}) => `    @Column(insertable = false, updatable = false)\n    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")\n    @Null(groups = {ISave.class})\n    private Timestamp ${name};`,
      modifyTime: (column) => this.fields.createTime(column),
      createUserId: ({name}) => `    @Column(updatable = false)\n    @NotNull(groups = {ISave.class})\n    @Positive\n    private Long ${name};`,
      modifyUserId: ({name}) => `    @NotNull(groups = {ISave.class, IUpdate.class})\n    @Positive\n    private Long ${name};`,
      createUserName: ({name, length}) => `    @Column(updatable = false)\n    @NotNull(groups = {ISave.class})\n    @Size(max = ${length})\n    private String ${name};`,
      modifyUserName: ({name, length}) => `    @NotNull(groups = {ISave.class, IUpdate.class})\n    @Size(max = ${length})\n    private String ${name};`
    };
    this.props = {
      default: ({name, dataType, notNull, comment}) => `        ${name}(${dataType.value.toUpperCase()}.build(${notNull ? 'true, ' : ''}"${comment}"))`,
      id: ({name, dataType, comment}) => `        ${name}(${[DataType.BIGINT.name, DataType.INT.name].includes(dataType.name) ? 'LONG' : 'STRING'}.build(true, "${comment}"))`,
      uid: ({name, comment}) => `        ${name}(STRING.build(true, "${comment}"))`,
      deleted: ({name, comment}) => `        ${name}(ENUM.build("是否逻辑删除").setOptions(Radio.comments()))`,
      createTime: ({name, comment}) => `        ${name}(TIMESTAMP.build("${comment}"))`,
      modifyTime: (column) => this.props.createTime(column),
      createUserId: ({name, comment}) => `        ${name}(LONG.build("${comment}"))`,
      modifyUserId: (column) => this.props.createUserId(column),
      createUserName: ({name, comment}) => `        ${name}(STRING.build("${comment}"))`,
      modifyUserName: (column) => this.props.createUserName(column)
    };
  }
}

class RdAdapter {
  constructor() {
    this.fields = {
      yyyy: ({name}) => `    @NotNull\n    @Min(2013)\n    @Max(2099)\n    private Integer ${name};`,
      mm: ({name}) => `    @NotNull\n    @Min(1)\n    @Max(12)\n    private Integer ${name};`,
      regionCode: ({name}) => `    private RegionCode ${name};`,
      bsBookCode: ({name}) => `    private BookCode ${name};`,
      pfsBookCode: (column) => this.fields.bsBookCode(column)
    };
    this.props = {
      yyyy: ({name, comment}) => `${name}(INTEGER.build(true, "${comment}"))`,
      mm: (column) => this.props.yyyy(column),
      regionCode: ({name, comment}) => `${name}(ENUM.build("${comment}").setOptions(RegionCode.comments()))`,
      bsBookCode: ({name, comment}) => `${name} (ENUM.build("${comment}").setOptions(BookCode.comments()))`,
      pfsBookCode: (column) => this.props.bsBookCode(column)
    };
  }
}

/**
 * 数据类型定义
 */
export class Type {
  /**
   * 实体: mysql 与 java 数据类型映射
   * @param name {string} 数据库数据类型
   * @param value {string} Java数据类型
   */
  constructor(name = '', value = '') {
    /**
     * 数据库数据类型
     * @type {string}
     */
    this.name = name;
    /**
     * Java数据类型
     * @type {string}
     */
    this.value = value;
  }
}

/**
 * mysql 与 java 数据类型映射配置
 * @type {{DECIMAL: Type, CHAR: Type, BIGINT: Type, TEXT: Type, JSON: Type, MEDIUMINT: Type, INT: Type, DATE: Type, DATETIME: Type, SMALLINT: Type, TIMESTAMP: Type, VARCHAR: Type, TINYINT: Type}}
 */
export const DataType = {
  TINYINT: new Type('TINYINT', 'Short'),
  SMALLINT: new Type('SMALLINT', 'Short'),
  MEDIUMINT: new Type('MEDIUMINT', 'Integer'),
  INT: new Type('INT', 'Integer'),
  BIGINT: new Type('BIGINT', 'Long'),
  DECIMAL: new Type('DECIMAL', 'Double'),
  DOUBLE: new Type('DOUBLE', 'Double'),
  FLOAT: new Type('FLOAT', 'Float'),
  CHAR: new Type('CHAR', 'String'),
  VARCHAR: new Type('VARCHAR', 'String'),
  TEXT: new Type('TEXT', 'String'),
  MEDIUMTEXT: new Type('MEDIUMTEXT', 'String'),
  LONGTEXT: new Type('LONGTEXT', 'String'),
  DATE: new Type('DATE', 'Timestamp'),
  TIMESTAMP: new Type('TIMESTAMP', 'Timestamp'),
  DATETIME: new Type('DATETIME', 'Timestamp'),
  JSON: new Type('JSON', 'String')
};

/**
 * 基于表名生成各种命名
 */
export class Names {
  /**
   * 构造表名各种规则
   * @param tableName {string}
   */
  constructor(tableName) {
    /**
     * tab_demo_list => tab_demo_list
     * @type {string}
     */
    this.tab_name = tableName;
    /**
     * tab_demo_list => tabDemoList
     * @type {string}
     */
    this.TabName = tableName.split('_').map(v => v.replace(/^\S/, m => m.toUpperCase())).join('');
    /**
     * tab_demo_list => tabDemoList
     * @type {string}
     */
    this.tabName = this.TabName.replace(/^\S/, m => m.toLowerCase());
    /**
     * tab_demo_list => demo-list
     * @type {string}
     */
    this.java_name = tableName.replace(/^[a-zA-Z]+_/, '').replace(/_/g, '-');
    /**
     * tab_demo_list => DemoList
     * @type {string}
     */
    this.JavaName = this.java_name.split('-').map(v => v.replace(/^\S/, m => m.toUpperCase())).join('');
    /**
     * tab_demo_list => demoList
     * @type {string}
     */
    this.javaName = this.JavaName.replace(/^\S/, m => m.toLowerCase());
    /**
     * tab_demo_list => demolist
     * @type {string}
     */
    this.javaname = this.JavaName.toLowerCase();
  }

  /**
   * 转换为对象
   * @return {Object}
   */
  toObject() {
    return JSON.parse(JSON.stringify(this));
  }

  toString() {
    return JSON.stringify(this);
  }
}
