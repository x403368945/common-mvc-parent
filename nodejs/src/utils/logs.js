/**
 * Created by Conor Xie on 2018/4/1.
 */
import {parse} from 'path';
import Paths from './entity/Paths';

/**
 * 写入响应结果到文件
 * @param hasTrue 为true时执行写入操作
 * @param filename 服务js文件
 * @param name 文件名
 * @param data 日志对象
 * @returns data
 */
export const write = function (hasTrue, filename, name, data) {
  if (hasTrue) { // hasDebug() && options.node
    if (process.env.BROWSER) {
      console.log(['{filename}#{name}'.format(filename, name), data])
    } else {
      const uname = '{name}-{timestamp}.json'.format(name, new Date().format('yyyyMMddHHmmssSSS'));
      console.log(uname);
      console.log(
        Paths.resolve('logs', parse(filename).base)
          .mkdirs()
          .append(uname)
          .write(JSON.stringify(data, null, 2))
          .absolute()
      )
    }
    // console.log(resolve('logs', parse(filename).base, '{}.json'.format(name)));
  }
  return data;
};
