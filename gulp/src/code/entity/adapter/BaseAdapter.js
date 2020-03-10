import DataType from '../../core/DataType';

const TINYINT_MIN_VALUE = -128;
const TINYINT_MAX_VALUE = 127;
const SHORT_MIN_VALUE = -32768;
const SHORT_MAX_VALUE = 32767;
const INTEGER_MIN_VALUE = -2147483648;
const INTEGER_MAX_VALUE = 2147483647;
const LONG_MIN_VALUE = -9223372036854775808;
const LONG_MAX_VALUE = 9223372036854775807;

/**
 * 固定字段处理策略
 * ^.*\("\\\\t(.*)\\\\n.*
 *     $1
 */
export default class BaseAdapter {
  constructor() {
    this.fields = {
      // 默认字段生成策略
      default: ({index, name, db_name, dataType, notNull, unsigned, length, fixed, comment}) => {
        const list = [];
        if (notNull) list.push('    @NotNull(groups = {ISave.class})');
        if (db_name.includes('_')) list.push(`    @Column(name = "${db_name}")`); // 数据库自字段 is_ 开头的特殊处理
        switch (dataType.mysql) {
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
        list.push(`    @ApiModelProperty(value = "${comment}", position = ${index})`);
        list.push(`    private ${dataType.java} ${name};`);
        return list.join('\n');
      },
      id: ({index, name, dataType, length}) => {
        if ([DataType.BIGINT.mysql, DataType.INT.mysql].includes(dataType.mysql)) {
          return `    @Id\n    @GeneratedValue(strategy = GenerationType.IDENTITY)\n    @NotNull(groups = {IUpdate.class, IMarkDelete.class})\n    @Positive\n    @ApiModelProperty(value = "数据ID", position = ${index})\n    private Long ${name};`
        } else {
          return `    @Id\n    @NotBlank(groups = {IUpdate.class, IMarkDelete.class})\n    @Size(max = ${length})\n    @ApiModelProperty(value = "数据id", position = ${index})\n    private String ${name};`
        }
      },
      uid: ({index, name}) => `    @Column(updatable = false)\n    @NotNull(groups = {ISave.class, IUpdate.class, IMarkDelete.class})\n    @Size(min = 32, max = 32)\n    @ApiModelProperty(value = "数据uid", position = ${index})\n    private String ${name};`,
      deleted: ({index, name}) => `    @Column(insertable = false, updatable = false)\n    @Null(groups = {ISave.class})\n    @ApiModelProperty(value = "是否逻辑删除，com.ccx.demo.enums.Bool", position = ${index})\n    private Bool ${name};`,
      insertTime: ({index, name}) => `    @Column(insertable = false, updatable = false)\n    @JSONField(format = "yyyy-MM-dd HH:mm:ss")\n    @Null(groups = {ISave.class})\n    @ApiModelProperty(value = "数据新增时间", example = "2020-02-02 02:02:02", position = ${index})\n    private Timestamp ${name};`,
      updateTime: ({index, name}) => `    @Column(insertable = false, updatable = false)\n    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")\n    @Null(groups = {ISave.class})\n    @ApiModelProperty(value = "数据最后一次更新时间", example = "2020-02-02 02:02:02.002", position = ${index})\n    private Timestamp ${name};`,
      insertUserId: ({index, name}) => `    @Column(updatable = false)\n    @NotNull(groups = {ISave.class})\n    @Positive\n    @ApiModelProperty(value = "新增操作人id", position = ${index})\n    private Long ${name};`,
      updateUserId: ({index, name}) => `    @NotNull(groups = {ISave.class, IUpdate.class})\n    @Positive\n    @ApiModelProperty(value = "更新操作人id", position = ${index})\n    private Long ${name};`,
      insertUserName: ({index, name, length}) => `    @Column(updatable = false)\n    @NotNull(groups = {ISave.class})\n    @Size(max = ${length})\n    @ApiModelProperty(value = "新增操作人昵称", position = ${index})\n    private String ${name};`,
      updateUserName: ({index, name, length}) => `    @NotNull(groups = {ISave.class, IUpdate.class})\n    @Size(max = ${length})\n    @ApiModelProperty(value = "更新操作人昵称", position = ${index})\n    private String ${name};`
    };
    this.props = {
      default: ({name, dataType, notNull, comment}) => `        ${name}(${dataType.java.toUpperCase()}.build(${notNull ? 'true, ' : ''}"${comment}"))`,
      id: ({name, dataType, comment}) => `        ${name}(${[DataType.BIGINT.mysql, DataType.INT.mysql].includes(dataType.mysql) ? 'LONG' : 'STRING'}.build(true, "${comment}"))`,
      uid: ({name, comment}) => `        ${name}(STRING.build(true, "${comment}"))`,
      deleted: ({name}) => `        ${name}(ENUM.build("是否逻辑删除"))`,
      insertTime: ({name, comment}) => `        ${name}(TIMESTAMP.build("${comment}"))`,
      updateTime: (column) => this.props.insertTime(column),
      insertUserId: ({name, comment}) => `        ${name}(LONG.build("${comment}"))`,
      updateUserId: (column) => this.props.insertUserId(column),
      insertUserName: ({name, comment}) => `        ${name}(STRING.build("${comment}"))`,
      updateUserName: (column) => this.props.insertUserName(column)
    };
  }
}
