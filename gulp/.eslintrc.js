// https://eslint.org/docs/user-guide/configuring

module.exports = {
  root: true,
  parserOptions: {
    parser: 'babel-eslint'
  },
  env: {
    node: true
  },
  extends: [
    // https://github.com/standard/standard/blob/master/docs/RULES-en.md
    'standard'
  ],
  // add your custom rules here
  rules: {
    // allow async-await
    'generator-star-spacing': 'off',
    // allow debugger during development
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    semi: ['off', 'always'],
    indent: 0,
    'no-lone-blocks': ['off'],
    'camelcase': ['off'], // 禁止 非驼峰命名 警告
    'no-eval': ['off'], // 禁止 eval 警告
    'no-template-curly-in-string': ['off'], // 禁止 双引号里面带模板语法 警告
    'no-extend-native': ['off'], // 禁止原型扩展警告
    'no-return-await': ['off'], // 禁止 return await 警告
    'no-unused-vars': ['off'], // 禁止 定义了未使用的方法 警告
    'object-curly-spacing': ['off'], // 禁止 { 这里前后必须要有一个空格 } 警告
    'object-property-newline': ['off'], // 禁止 定义了未使用的方法 警告
    'space-before-function-paren': ['off'], // 禁止方法前必须加空格警告
  }
};
