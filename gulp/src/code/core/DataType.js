import Type from './Type';

/**
 * mysql 与 java 数据类型映射配置
 * @type {{FLOAT: Type, LONGTEXT: Type, DECIMAL: Type, CHAR: Type, BIGINT: Type, TEXT: Type, JSON: Type, MEDIUMINT: Type, MEDIUMTEXT: Type, INT: Type, DATE: Type, DATETIME: Type, SMALLINT: Type, TIMESTAMP: Type, VARCHAR: Type, DOUBLE: Type, TINYINT: Type}}
 */
const DataType = {
  TINYINT: new Type('TINYINT', 'Byte', 'Number'),
  SMALLINT: new Type('SMALLINT', 'Short', 'Number'),
  MEDIUMINT: new Type('MEDIUMINT', 'Integer', 'Number'),
  INT: new Type('INT', 'Integer', 'Number'),
  BIGINT: new Type('BIGINT', 'Long', 'Number'),
  DECIMAL: new Type('DECIMAL', 'BigDecimal', 'Number'),
  DOUBLE: new Type('DOUBLE', 'Double', 'Number'),
  FLOAT: new Type('FLOAT', 'Float', 'Number'),
  CHAR: new Type('CHAR', 'String', 'String'),
  VARCHAR: new Type('VARCHAR', 'String', 'String'),
  TEXT: new Type('TEXT', 'String', 'String'),
  MEDIUMTEXT: new Type('MEDIUMTEXT', 'String', 'String'),
  LONGTEXT: new Type('LONGTEXT', 'String', 'String'),
  DATE: new Type('DATE', 'Timestamp', 'String'),
  TIMESTAMP: new Type('TIMESTAMP', 'Timestamp', 'String'),
  DATETIME: new Type('DATETIME', 'Timestamp', 'String'),
  JSON: new Type('JSON', 'String', 'JSONArrayObject')
};
export default DataType;
