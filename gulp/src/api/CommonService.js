import axios from 'axios';
import FormData from 'form-data';
import Result from '../utils/entity/Result';

const enumsURL = '/1/common/enum/{clazz}'; // 获取枚举所有选项
const updateTempURL = '/1/common/upload/temp'; // 上传到临时目录，单个上传
const updateTempsURL = '/1/common/uploads/temp'; // 上传到临时目录，批量上传
const updateUserURL = '/1/common/upload/user'; // 上传到用户目录，单个上传：用户头像
const updateUsersURL = '/1/common/uploads/user'; // 上传到用户目录，批量上传：用户头像

/**
 * 后台服务请求：通用模块
 * @author 谢长春 2020-03-10
 */
export default class CommonService {
  /**
   * 获取枚举选项及说明
   * @return {Promise<Result>}
   */
  async getEnumItems(classpath) {
    return await axios
      .get(enumsURL.format(classpath))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 上传到临时目录，单个上传
   * @param file {File|ReadStream} 单个文件
   * @return {Promise<Result>}
   */
  async uploadTemp(file) {
    const form = new FormData();
    form.append('file', file);
    return await axios
      .post(updateTempURL, form, {headers: form.getHeaders()})
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 上传到临时目录，批量上传
   * @param files {Array<File>} 文件数组
   * @return {Promise<Result>}
   */
  async uploadTemps(files) {
    const form = new FormData();
    files.forEach(file => {
      form.append('files', file);
    });
    return await axios
      .post(updateTempsURL, form, {headers: form.getHeaders()})
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 上传到用户目录，单个上传：用户头像
   * @param file {File} 单个文件
   * @return {Promise<Result>}
   */
  async uploadUser(file) {
    const form = new FormData();
    form.append('file', file);
    return await axios
      .post(updateUserURL, form, {headers: form.getHeaders()})
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 上传到用户目录，批量上传：用户头像
   * @param files {Array<File>} 文件数组
   * @return {Promise<Result>}
   */
  async uploadUsers(files) {
    const form = new FormData();
    files.forEach(file => {
      form.append('files', file);
    });
    return await axios
      .post(updateUsersURL, form, {headers: form.getHeaders()})
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }
}
