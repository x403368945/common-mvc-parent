/**
 * 金额添加千位符
 * @param value {Number|String} 金额
 * @returns {String}
 */
export default function formatAmount(value) {
  return (!value)
    ? value
    : parseFloat(value)
      .toFixed(2)
      .trim()
      .replace(/([+-])?(\d+)(\.\d+)/, (match, $1, $2, $3) => {
        const len = $2.length % 3;
        return (len === 0)
          ? `${$1 || ''}${$2.replace(/(\d{3})/g, '$1,').slice(0, -1)}${$3}`
          : `${$1 || ''}${$2.slice(0, len)}${$2.slice(len).replace(/(\d{3})/g, ',$1')}${$3}`
      });
}
