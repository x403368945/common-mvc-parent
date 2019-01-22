package com.demo.config.init;

import com.utils.util.FPath;
import com.utils.util.FWrite;
import com.utils.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import static com.demo.config.init.AppConfig.App.*;

/**
 * 初始化 application.properties 中的应用配置参数；
 * <pre>
 * TODO
 *   先在 {@link App} 中定义好枚举，然后到 {@link AppProperties} 中初始化；
 *   {@link Path} 应用中所有依赖的文件目录
 *   {@link URL} 应用中所有依赖的请求URL
 *
 *
 * @author 谢长春 on 2018-10-2
 */
@Component
@Slf4j
public class AppConfig {
    private static AppProperties properties;

    @Autowired
    public AppConfig(AppProperties appProperties) {
        AppConfig.properties = appProperties;
    }

    /**
     * 判断当前是否属于调试模式
     *
     * @return boolean true：调试模式，false：非调试模式
     */
    public static boolean isDebug() {
        return Util.booleanValue(DEBUG.value());
    }

    /**
     * 设置 DEBUG 模式；生产环境无法开启 DEBUG 模式
     *
     * @param debug boolean true：调试模式，false：非调试模式
     * @return boolean true：调试模式，false：非调试模式
     */
    public static boolean setDebug(final boolean debug) {
        if (isProd()) {
            return false; // 生产环境无法切调试模式
        }
        properties.setDebug(debug);
        return debug;
    }

    /**
     * 判断当前是否属于单元测试模式
     *
     * @return boolean true：单元测试模式
     */
    public static boolean isJunit() {
        return Util.booleanValue(JUNIT.value());
    }

    /**
     * 设置 JUNIT 模式；生产环境无法开启 JUNIT 模式
     * true：单元测试模式，false：非单元测试模式
     *
     * @param junit boolean
     * @return boolean
     */
    public static boolean setJunit(final boolean junit) {
        if (isProd()) {
            return false; // 生产环境无法切单元测试模式
        }
        properties.setJunit(junit);
        return junit;
    }

    /**
     * 判断当前是否属于本地开发环境
     *
     * @return boolean true：本地开发环境
     */
    public static boolean isDev() {
        return "dev".equals(properties.getEnv());
    }

    /**
     * 判断当前是否属于云开发测试环境
     *
     * @return boolean true：云开发测试环境
     */
    public static boolean isBeta() {
        return "beta".equals(properties.getEnv());
    }

    /**
     * 判断当前是否属于线上生产环境
     *
     * @return boolean true：线上生产环境
     */
    public static boolean isProd() {
        return "prod".equals(properties.getEnv());
    }

    /**
     * 应用程序配置枚举
     * 来源：application.properties
     */
    public enum App {
        ENV("当前环境：[dev:本地|beta:测试|prod:生产]",
                "app.env", () -> Objects.toString(properties.getEnv())),
        ADMIN_USER_ID("管理员用户ID",
                "app.admin-user-id", () -> Objects.toString(properties.getAdminUserId())),
        ADMIN_USER("管理员账户登录账户",
                "app.admin-user", () -> properties.getAdminUser()),
        IP("当前主机 IP 地址；可使用 frp 服务器IP",
                "app.ip", () -> properties.getIp()),
        DOMAIN("应用路径，域名；可使用 frp 服务器域名",
                "app.domain", () -> properties.getDomain()),
        PATH_ROOT("应用文件根目录",
                "app.path-root", () -> properties.getPathRoot()),
        MARKDOWN("markdown 文档存放地址",
                "app.markdown", () -> properties.getMarkdown()),
        DEBUG("调试模式开关：true开启调试模式，false关闭调试模式",
                "app.debug", () -> Objects.toString(properties.isDebug())),
        JUNIT("单元测试模式开关：true开启单元测试模式，false关闭单元测试模式",
                "app.junit", () -> Objects.toString(properties.isJunit())),
        ;
        public final String key;
        public final String comment;
        private final Supplier<String> supplier;

        App(final String comment, final String key, final Supplier<String> supplier) {
            this.key = key;
            this.comment = comment;
            this.supplier = supplier;
        }

        /**
         * 获取应用配置
         *
         * @return {@link String}
         */
        public String value() {
            return Optional.ofNullable(supplier.get())
                    .orElseThrow(() -> new NullPointerException(String.format("应用配置【%s】不能空", this.name())));
        }

        /**
         * 获取应用配置，并追加字符
         *
         * @return {@link String}
         */
        public String append(final String append) {
            return value().concat(append);
        }

    }

    /**
     * 定义所有路径枚举；所有路径依赖于 {@link App#PATH_ROOT}
     *
     * @author 谢长春 on 2018-10-2
     */
    public enum Path {
        ROOT("文件存储根目录:(d:/files | /root/files)", "          /"),
        CONFIG("配置文件路径", "                                   /config/"),
        EXCEL("Excel模板文件目录", "                               /excel/"),
        JSON("json配置文件目录", "                                 /json/"),
        HTML("html模板文件目录", "                                 /html/"),
        MD("markdown文件目录", "                                   /md/"),
        TEMP("文件上传及临时文件存储目录", "                       /temp/"),
        ;
        /**
         * 枚举属性说明
         */
        public final String comment;
        /**
         * 目录层级
         */
        public final String dir;

        Path(String comment, String dir) {
            this.comment = comment;
            this.dir = dir.trim();
        }

        /**
         * 获取文件路径操作对象
         *
         * @param names String[] 追加目录名或文件名
         * @return FPath
         */
        public FPath fpath(String... names) {
            return FPath.of(PATH_ROOT.append(dir), names);
        }

        /**
         * 获取绝对路径
         *
         * @param names String[] 追加目录名或文件名
         * @return String 文件绝对路径：d:\java
         */
        public String absolute(String... names) {
            return FPath.of(PATH_ROOT.append(dir), names).absolute();
        }

        /**
         * 获取文件对象
         *
         * @param names String[] 追加目录名或文件名
         * @return File 文件对象
         */
        public File file(String... names) {
            return FPath.of(PATH_ROOT.append(dir), names).file();
        }

        /**
         * 读取文件内容
         *
         * @param names String[] 追加目录名或文件名
         * @return File 文件对象
         */
        public String read(String... names) {
            return FPath.of(PATH_ROOT.append(dir), names).read();
        }

        /**
         * 写入文本到文件
         *
         * @param content String 写入的文件内容
         * @param names   String[] 追加目录名及文件名
         * @return File 文件对象
         */
        public String write(final String content, String... names) {
            return FWrite.of(file(names)).write(content).getAbsolute().orElse(null);
        }
    }

    /**
     * 定义所有路径枚举；所有路径依赖于 {@link App#DOMAIN}
     * url定义规则：
     * > 所有以 {} 占位的 使用 format 方法格式化参数
     * > 所有以 / 结尾的url可以追加路径
     * > 所有非 / 结尾的url不能追加路径
     *
     * @author 谢长春 on 2018-10-2
     */
    public enum URL {
        //        SERVER("后端接口访问url前缀", "                 /server/"),
        SERVER("后端接口访问url前缀", "                 /"),

        CONFIG("配置文件访问路径", "                    /files/config/"),
        TEMP("临时文件访问路径", "                      /files/temp/"),
        ;
        /**
         * 枚举属性说明
         */
        public final String comment;
        private final String url;

        URL(String comment, String url) {
            this.comment = comment;
            this.url = url.trim();
        }

        /**
         * 获取路径
         *
         * @return String
         */
        public String value() {
            return DOMAIN.append(url);
        }

        /**
         * 追加路径
         *
         * @param args 参数集合
         * @return String
         */
        public String append(String... args) {
            return DOMAIN.append(url).concat(
                    String.join("/", args)
                            // 避免出现双斜杠
                            .replaceFirst("^/", "")
                            .replace("//", "/")
            );
        }

        /**
         * 格式化路径中 {} 指定的参数
         *
         * @param args 参数集合
         * @return String
         */
        public String format(String... args) {
            String result = DOMAIN.append(url);
            if (Objects.nonNull(args)) {
                for (Object value : args) { // 替换 url 参数占位符
                    result = result.replaceFirst("\\{(\\w+)?}", value.toString());
                }
            }
            return result;
        }

        /**
         * 格式化路径中 {} 指定的参数
         *
         * @param args 参数集合
         * @return String
         */
        public String format(final Map<String, String> args) {
            String result = DOMAIN.append(url);
            if (Objects.nonNull(args)) {
                for (Map.Entry<String, String> entry : args.entrySet()) {
                    // 替换 url 参数占位符
                    result = result.replace(String.format("{%s}", entry.getKey()), entry.getValue());
                }
            }
            return result;
        }
    }

}