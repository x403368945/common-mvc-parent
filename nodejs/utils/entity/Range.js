/**
 * 数字区间对象，查询该区间数据
 * @author 谢长春 2019-8-9
 */
export class NumRange {
    /**
     * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
     * @param self {NumRange}
     * @return {NumRange}
     */
    static self(self) {
        return self;
    }

    /**
     * 静态构造函数： min <= {value} <= max
     * @param min {number} 最小值，包含
     * @param max {number} 最大值，包含
     * @return {NumRange}
     */
    static of({min, max} = {}) {
        return new NumRange(min, max);
    }

    /**
     * 构造函数： min <= {value} <= max
     * @param min {number} 最小值，包含
     * @param max {number} 最大值，包含
     */
    constructor(min, max) {
        /**
         * 最小值，包含
         * @type {number}
         */
        this.min = min;
        /**
         * 最大值，包含
         * @type {number}
         */
        this.max = max;
    }

    toString() {
        return JSON.stringify(this)
    }
}

/**
 * 日期区间对象，查询该区间数据
 * @author 谢长春 2019-8-9
 */
export class DateRange {
    /**
     * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
     * @param self {DateRange}
     * @return {DateRange}
     */
    static self(self) {
        return self;
    }

    /**
     * 静态构造函数： begin <= {value} <= end
     * @param begin {string} 起始日期，包含，格式：yyyy-MM-dd | yyyy-MM-dd HH:mm:ss
     * @param end {string} 结束日期，包含，格式：yyyy-MM-dd | yyyy-MM-dd HH:mm:ss
     * @return {DateRange}
     */
    static of({begin, end} = {}) {
        return new DateRange(begin, end);
    }

    /**
     * 构造函数： begin <= {value} <= end
     * @param begin {string} 起始日期，包含，格式：yyyy-MM-dd | yyyy-MM-dd HH:mm:ss
     * @param end {string} 结束日期，包含，格式：yyyy-MM-dd | yyyy-MM-dd HH:mm:ss
     */
    constructor(begin, end) {
        /**
         * 起始日期，包含，格式：yyyy-MM-dd，yyyy-MM-dd HH:mm:ss
         * @type {string}
         */
        this.begin = begin;
        /**
         * 结束日期，包含，格式：yyyy-MM-dd，yyyy-MM-dd HH:mm:ss
         * @type {string}
         */
        this.end = end;
    }

    toString() {
        return JSON.stringify(this)
    }
}
