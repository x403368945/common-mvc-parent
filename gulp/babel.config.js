// https://juejin.im/post/5c794f4b6fb9a04a102ffe46
// https://juejin.im/post/5c8134bd5188251ba73eed76
// module.exports = {
//   plugins: ['@babel/transform-runtime'],
//   presets: ['@babel/env']
// };

const plugins = [];
const presets = [];
if (process.env.NODE_ENV) {
  // 其他环境依赖编译插件
} else { // gulpfile.babel.js 运行时依赖编译插件
  plugins.push('@babel/transform-runtime');
  presets.push('@babel/env')
}
module.exports = {plugins, presets};

