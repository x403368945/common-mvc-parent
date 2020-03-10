/**
 * 实体：文件对象
 * @author 谢长春 2020-03-10
 */
export default class FileInfo {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {FileInfo}
   * @return {FileInfo}
   */
  static self(self) {
    return self;
  }

  /**
   * 构造函数：内部应该列出所有可能的参数，并对参数做说明
   * @param name {string} 文件名，用户上传的文件名
   * @param uname {string} 唯一文件名，磁盘上存储的uuid文件名
   * @param url {string} 文件访问路径
   */
  constructor({
                name = undefined,
                uname = undefined,
                url = undefined
              } = {}) {
    /**
     * 文件名，用户上传的文件名
     * @type {string}
     */
    this.name = name;
    /**
     * 唯一文件名，磁盘上存储的uuid文件名
     * @type {string}
     */
    this.uname = uname;
    /**
     * 文件访问路径
     * @type {string}
     */
    this.url = url;
  }

  toString() {
    return JSON.stringify(this)
  }
}
