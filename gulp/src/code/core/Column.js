import DataType from './DataType';

/**
 * 数据库字段信息
 */
export default class Column {
  /**
   * 通过 SHOW FULL COLUMNS FROM #{tableName} 获取到的参数
   * @param index {number} 字段序号
   * @param Field {string} 字段名
   * @param Type {string} 数据库类型
   * @param Collation {string} 字符集
   * @param Null {string} 是否允许 null ，【YES:允许 null 值，NO:不允许 null 值】
   * @param Default {string} 默认值
   * @param Comment {string} 字段说明
   */
  constructor({index, Field, Type, Collation, Null, Default, Comment}) {
    /**
     * 字段排序，@ApiModelProperty position 需要该值
     * @type {number}
     */
    this.index = index + 1;
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
}
