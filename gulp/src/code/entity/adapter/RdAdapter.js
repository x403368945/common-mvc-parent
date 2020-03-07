export default class RdAdapter {
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
      regionCode: ({name, comment}) => `${name}(ENUM.build("${comment}"))`,
      bsBookCode: ({name, comment}) => `${name} (ENUM.build("${comment}"))`,
      pfsBookCode: (column) => this.props.bsBookCode(column)
    };
  }
}