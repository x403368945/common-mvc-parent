/**
 * mysql 与 java 数据类型映射配置
 * @type {{DECIMAL: Type, CHAR: Type, BIGINT: Type, TEXT: Type, JSON: Type, MEDIUMINT: Type, INT: Type, DATE: Type, DATETIME: Type, SMALLINT: Type, TIMESTAMP: Type, VARCHAR: Type, TINYINT: Type}}
 */
import Type from './Type';

const DataType = {
  TINYINT: new Type('TINYINT', 'Short'),
  SMALLINT: new Type('SMALLINT', 'Short'),
  MEDIUMINT: new Type('MEDIUMINT', 'Integer'),
  INT: new Type('INT', 'Integer'),
  BIGINT: new Type('BIGINT', 'Long'),
  DECIMAL: new Type('DECIMAL', 'BigDecimal'),
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
export default DataType;
