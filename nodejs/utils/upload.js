/**
 * 封装文件上传
 * https://github.com/request/request
 * Created by Jason Xie on 2018/4/1.
 */
import request from 'request';
import Result from './Result';
import Http from './Http';

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
      baseUrl: (baseUrl) || Http.baseURL(),
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
