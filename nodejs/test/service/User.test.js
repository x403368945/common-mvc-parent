/**
 * 测试：后台服务请求：用户
 * @author 谢长春 2019-7-28
 */
import UserVO from '../../src/service/User';
import axios from 'axios';

export default class UserTest {
    /**
     * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
     * @param self {UserTest}
     * @return {UserTest}
     */
    static self(self) {
        return self;
    }

    /**
     * 静态构造函数
     * @return {UserTest}
     */
    static of() {
        return new UserTest();
    }

    /**
     * @return {Promise<UserTest>}
     */
    async login() {
        console.log(`> 登录：管理员(admin:admin) ----------------------------------------------------------------------------------------------------`);
        (await UserVO.of({username: 'admin', password: 'admin'}).getService().login()).print().assertData();
        return this;
    }

    /**
     * @return {Promise<UserTest>}
     */
    async loginAdminBasic() {
        console.log(`> 登录[basic]）：用户(admin:admin) ----------------------------------------------------------------------------------------------------`);
        axios.defaults.auth = {username: 'admin', password: 'admin'};
        return this;
    }

    /**
     * @return {Promise<UserTest>}
     */
    async loginUserBasic() {
        console.log(`> 登录[basic]：用户(user:111111) ----------------------------------------------------------------------------------------------------`);
        axios.defaults.auth = {username: 'user', password: '111111'};
        return this;
    }

    /**
     * @return {Promise<UserTest>}
     */
    async logout() {
        console.log(`> 退出 ----------------------------------------------------------------------------------------------------`);
        (await UserVO.of().getService().logout()).print().assertVersion().assertCode();
        return this;
    }

    /**
     * @return {Promise<UserTest>}
     */
    async getCurrentUser() {
        console.log(`> 获取当前登录用户信息 ----------------------------------------------------------------------------------------------------`);
        (await UserVO.of().getService().getCurrentUser()).print().assertVersion().assertData();
        return this;
    }

    /**
     *
     * @return {Promise<UserTest>}
     */
    async updateNickname() {
        console.log(`> 修改昵称 ----------------------------------------------------------------------------------------------------`);
        (await UserVO.of({nickname: '张三'}).getService().updateNickname()).print().assertVersion().assertCode();
        return this;
    }

    /**
     *
     * @return {Promise<UserTest>}
     */
    filename() {
        console.log(__filename);
        return this;
    }

    /**
     *
     * @return {Promise<UserTest>}
     */
    newline() {
        console.log('');
        return this;
    }

    /**
     * 测试全部
     * @return {Promise<void>}
     */
    async testAll() {
        const moduleName = '用户';
        console.info(`${moduleName}：start ${'*'.repeat(200)}`);
        await Promise.resolve(UserTest.of())
            .then(service => service.filename()).then(s => s.newline())
            // 开始
            .then(service => service.login()).then(s => s.newline())
            .then(service => service.loginAdminBasic())
            .then(service => service.getCurrentUser()).then(s => s.newline())
            .then(service => service.logout()).then(s => s.newline())
            .then(service => service.loginUserBasic())
            .then(service => service.getCurrentUser()).then(s => s.newline())
            .then(service => service.updateNickname()).then(s => s.newline())
            .then(service => service.logout()).then(s => s.newline())
            // 结束
            .then(() => console.info(`${moduleName}：end ${'*'.repeat(200)}\n\n\n\n\n`))
            .catch((e) => {
                console.info(`${moduleName}：异常：${e.message}`);
                console.error(e)
            })
        ;
        return null;
    }
}
