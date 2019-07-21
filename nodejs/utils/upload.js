/**
 * 封装文件上传
 * https://github.com/request/request
 * @author 谢长春 2019-7-28
 */
import axios from 'axios';
import request from 'request';
import Result from './entity/Result';

/**
 * 上传文件
 * @param baseUrl {String} 基础url
 * @param url {String} 请求url
 * @param data {Object} {file:fs.createReadStream(path.resolve('files/temp/test.html'))} || {files:[d.png,b.png])}
 * @param headers {Object} {authorization:'token'}
 * @return Promise.<Result>
 */
export default ({baseUrl, url, data, headers}) => {
  return new Promise((resolve, reject) => {
    request.post({
      baseUrl: (baseUrl) || axios.defaults.baseURL,
      url: url,
      formData: data,
      headers: headers
    }, (err, res, body) => {
      if (err) reject(Result.failure(err));
      else resolve(JSON.parse(body));
    });
  });
};
// // *******************************************************************************************************************
// upload({
//     baseUrl: 'http://localhost/server',
//     url: '/wapp/file/upload',
//     data: {file: data},
// }).then((res) => {
//     console.log(res);
// });
// // *******************************************************************************************************************
// upload({
//     baseUrl: 'http://localhost/server',
//     url: '/wapp/file/uploads',
//     data: {files: [fs.createReadStream(path.resolve('files/temp/test.html')), fs.createReadStream(path.resolve('files/temp/console.js'))]},
// }).then((res) => {
//     console.log(res);
// });
// // *******************************************************************************************************************
// loginToken({})
//     .then((res) => upload({
//             baseUrl: 'http://localhost/server',
//             url: '/file/upload',
//             data: {file: data},
//             headers: res.extras
//         })
//     )
//     .then((res) => {
//         console.log(res);
//     });
// // *******************************************************************************************************************
// loginToken({})
//     .then((res) => upload({
//             baseUrl: 'http://localhost/server',
//             url: '/file/upload',
//             data: {files: [fs.createReadStream(path.resolve('files/temp/test.html')), fs.createReadStream(path.resolve('files/temp/console.js'))],
//             headers: res.extras
//         })
//     )
//     .then((res) => {
//         console.log(res);
//     });
