/**
 * 封装 http 请求
 * * @author 谢长春 2019-7-28
 */
import axios from 'axios';

axios.defaults.paramsSerializer = params => {
    // url 带参如果包含对象，需要经过转换，变成字符串，否则 axios 会出错
    if (params) {
        const searchParams = new URLSearchParams();
        Object.keys(params).filter(key => params[key]).forEach(key => searchParams.append(key, JSON.stringify(params[key])));
        return searchParams.toString();
    }
    return '';
};
/**
 * 开发环境 http 请求配置
 */
export const devConfig = () => {
    axios.defaults.baseURL = 'http://localhost:8080';
    axios.interceptors.request.use(config => { // request 拦截器，拦截清秀参数，打印日志
        const {method, baseURL, url, params, data} = config;
        if (method.toUpperCase() === 'GET') {
            let urlSearchParams = '';
            if (params) {
                const searchParams = new URLSearchParams();
                Object.keys(params).filter(key => params[key]).forEach(key => searchParams.append(key, JSON.stringify(params[key])));
                urlSearchParams = `?${searchParams.toString()}`;
            }
            console.log(JSON.stringify([method.toUpperCase(), `${baseURL}${url}${urlSearchParams}`, params]));
        } else {
            console.log(JSON.stringify([method.toUpperCase(), `${baseURL}${url}`, data]));
        }
        return config;
    });
    // GET 请求参数格式化策略
    // axios.defaults.paramsSerializer = params => {
    //     console.log({params: params});
    //     return encodeURI(JSON.stringify({json: params}))
    // };
    // POST|PUT|PATCH|DELETE 请求参数格式化策略
    // axios.defaults.transformRequest = data => {
    //     console.log({data: data});
    //     return data && Qs.stringify({json: data});
    // };
    // // 响应对象装换
    // axios.defaults.transformResponse = data => {
    //     console.log({resData: data});
    //     return data
    // };
    // axios.defaults.withCredentials = true; // 允许跨域
    // axios.defaults.proxy = { // 代理后端接口
    //     host: '127.0.0.1',
    //     port: 8080
    //     // auth: {username: 'admin', password: 'admin'}
    // }
};
/**
 * 测试（扫描）接口配置
 */
export const testConfig = () => {
    axios.defaults.url = '';
    axios.defaults.proxy = {
        host: '127.0.0.1',
        port: 8080
        // auth: {username: 'admin', password: 'admin'}
    }
};
