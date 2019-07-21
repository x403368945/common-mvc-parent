/**
 * 测试：后台服务请求：参考案例：用于调试后端服务是否可用，以及基本传参格式是否正确，没有数据入库
 * @author 谢长春 2019-7-28
 */
import OpenDemoVO from '../../src/service/OpenDemo';
import Page from '../../utils/entity/Page';

export default class OpenDemoTest {
    /**
     * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
     * @param self {OpenDemoTest}
     * @return {OpenDemoTest}
     */
    static self(self) {
        return self;
    }

    /**
     * 静态构造函数
     * @return {OpenDemoTest}
     */
    static of() {
        return new OpenDemoTest();
    }

    /**
     * @return {Promise<OpenDemoTest>}
     */
    async codes() {
        console.log(`> 查询所有状态码 ----------------------------------------------------------------------------------------------------`);
        (await OpenDemoVO.of().getService().codes()).print().assertData();
        return this;
    }

    /**
     * @return {Promise<OpenDemoTest>}
     */
    async save() {
        console.log(`> 新增 ----------------------------------------------------------------------------------------------------`);
        (await OpenDemoVO.of({name: 'Conor', phone: '18700000000'}).getService().save()).print().assertData();
        return this;
    }

    /**
     * @return {Promise<OpenDemoTest>}
     */
    async update() {
        console.log(`> 修改 ----------------------------------------------------------------------------------------------------`);
        (await OpenDemoVO.of({
            id: 100,
            name: 'Conor',
            phone: '18700000000'
        }).getService().update()).print().assertCode();
        return this;
    }

    /**
     * @return {Promise<OpenDemoTest>}
     */
    async deleteById() {
        console.log(`> 按 id 删除 ----------------------------------------------------------------------------------------------------`);
        (await OpenDemoVO.of({id: 1000}).getService().deleteById()).print().assertData();
        return this;
    }

    /**
     * @return {Promise<OpenDemoTest>}
     */
    async markDeleteById() {
        console.log(`> 按 id 逻辑删除 ----------------------------------------------------------------------------------------------------`);
        (await OpenDemoVO.of({id: 1001}).getService().markDeleteById()).print().assertCode();
        return this;
    }

    /**
     * @return {Promise<OpenDemoTest>}
     */
    async markDelete() {
        console.log(`> 按 id + uid 批量逻辑删除 ----------------------------------------------------------------------------------------------------`);
        (await OpenDemoVO.of({ids: [10000, 20000]}).getService().markDelete()).print().assertCode();
        return this;
    }

    /**
     * @return {Promise<OpenDemoTest>}
     */
    async findById() {
        console.log(`> 按 id 查询单条记录 ----------------------------------------------------------------------------------------------------`);
        (await OpenDemoVO.of({id: 100}).getService().findById()).print().assertData();
        return this;
    }

    /**
     * @return {Promise<OpenDemoTest>}
     */
    async search() {
        console.log(`> 多条件批量查询，不分页 ----------------------------------------------------------------------------------------------------`);
        (await OpenDemoVO.of({id: 100, name: 'Conor'}).getService().search()).print().assertData();
        return this;
    }

    /**
     *
     * @return {Promise<OpenDemoTest>}
     */
    async pageable() {
        console.log(`> 分页：多条件批量查询 ----------------------------------------------------------------------------------------------------`);
        (await OpenDemoVO.of({
            id: 100,
            name: 'Conor',
            page: Page.of({number: 1, size: 15})
        }).getService().pageable()).print().assertData();
        return this;
    }

    /**
     *
     * @return {Promise<OpenDemoTest>}
     */
    filename() {
        console.log(__filename);
        return this;
    }

    /**
     *
     * @return {Promise<OpenDemoTest>}
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
        const moduleName = '参考案例';
        console.info(`${moduleName}：start ${'*'.repeat(200)}`);
        await Promise.resolve(OpenDemoTest.of())
            .then(service => service.filename()).then(s => s.newline())
            // 开始
            .then(service => service.codes()).then(s => s.newline())
            .then(service => service.save()).then(s => s.newline())
            .then(service => service.update()).then(s => s.newline())
            .then(service => service.deleteById()).then(s => s.newline())
            .then(service => service.markDeleteById()).then(s => s.newline())
            .then(service => service.markDelete()).then(s => s.newline())
            .then(service => service.findById()).then(s => s.newline())
            .then(service => service.search()).then(s => s.newline())
            .then(service => service.pageable()).then(s => s.newline())
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
