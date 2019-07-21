/**
 * 分页对象
 * @author 谢长春 2019-7-28
 */
export default class Page {
    /**
     * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
     * @param self {Page}
     * @return {Page}
     */
    static self(self) {
        return self;
    }

    /**
     * 使用默认值初始化分页对象
     * @return {Page}
     */
    static ofDefault() {
        return new Page();
    }

    /**
     * 构造分页对象
     * @param number {number} 当前页码，从 1 开始
     * @param size {number} 每页返回数据行数，默认值为 20
     * @param pageCount {number} 总页数
     * @param totalCount {number} 总数据行数
     * @return {Page}
     */
    static of({number, size, pageCount = 0, totalCount = 0} = {}) {
        return new Page(number, size, pageCount, totalCount);
    }

    /**
     * 构造分页对象
     * @param number {number} 当前页码，从 1 开始
     * @param size {number} 每页返回数据行数，默认值为 20
     * @param pageCount {number} 总页数
     * @param totalCount {number} 总数据行数
     */
    constructor(number = 1, size = 20, pageCount = 0, totalCount = 0) {
        /**
         * 当前页码，从 1 开始
         * @type {number}
         */
        this.number = number;
        /**
         * 每页返回数据行数
         * @type {number}
         */
        this.size = size;
        /**
         * 总页数
         * @type {number}
         */
        this.pageCount = pageCount;
        /**
         * 总数据行数
         * @type {number}
         */
        this.totalCount = totalCount;
    }

    toString() {
        return JSON.stringify(this)
    }
}
