package com.support.mvc.entity.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.utils.IJson;
import com.utils.util.FWrite;
import com.utils.util.Maps;
import com.utils.util.Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestMethod;

import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.utils.enums.Charsets.UTF_8;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 版本信息
 *
 * @author 谢长春 2019-1-9
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Accessors(chain = true)
@JSONType(orders = {"id", "url", "method", "markdown", "notes", "demo", "props"})
public class Version implements IJson {
    /**
     * 版本号
     */
    private int id;
    /**
     * 请求 URL
     */
    private String url;
    /**
     * 请求方式
     */
    private RequestMethod method;
    /**
     * 接口使用标准，markdown 地址
     */
    private String markdown;
    /**
     * 接口详细说明
     * 接口版本变更说明
     */
    private List<String> notes;
    /**
     * 请求参考案例
     */
    @Builder.Default
    private Map<String, Object> demo = new LinkedHashMap<>();
    /**
     * 实体类属性说明
     */
    private List<Prop> props;

//    /**
//     * 返回 markdown 文件 URL
//     *
//     * @return String
//     */
//    public String getMd() {
//        return App.MARKDOWN.append(markdown);
//    }

    public String formatUrl(final Object... args) {
        return Util.format(
                url.replace("{version}", Objects.toString(id))
                        .replaceAll("\\?json=.*$", ""),
                args
        );
    }

    /**
     * 设置接口调用案例
     *
     * @param url String 请求url
     * @return {@link Version}
     */
    public Version setDemo(final String url) {
        this.demo = Maps.ofSO()
                .put("url", url.replace("{version}", Objects.toString(id)))
                .put("method", method)
                .build();
        return this;
    }

    /**
     * 设置接口调用案例
     *
     * @param url String 请求url
     * @param obj Object 请求参数
     * @return {@link Version}
     */
    public Version setDemo(final String url, final Object obj) {
        if (GET == method) {
            this.demo = Maps.ofSO()
                    .put("url", url.replace("{version}", Objects.toString(id))
                            .concat("?json=").concat(UTF_8.encode(JSON.toJSONString(obj))))
                    .put("method", method)
                    .put(Objects.nonNull(obj), "URLEncode之前的参数", obj)
                    .build();
        } else {
            this.demo = Maps.ofSO()
                    .put("url", url.replace("{version}", Objects.toString(id)))
                    .put("method", method)
                    .put(Objects.nonNull(obj), "body", Maps.bySO("json", obj))
                    .build();
        }
        return this;
    }

    /**
     * 设置接口调用案例
     *
     * @param consumer {@link Consumer<Version>}
     * @return {@link Version}
     */
    public Version demo(final Consumer<Version> consumer) {
        consumer.accept(this);
        return this;
    }

    /**
     * 将 Markdown 写入到文件
     *
     * @param dir {@link String} 指定写入目录
     * @return {@link Version}
     */
    public Version write(final String dir) {
        FWrite.of(dir, markdown).write(
                "版本：{version}  \n请求地址：{url}  \n请求方式：{method}  \n接口描述：  \n> {notes} \n \n字段说明：  \n\n字段|类型|必填|说明\n----|----|:----:|----\n{props}\n\n```js\nconst arrs={console};\nconsole.table(arrs);\n``` \n请求案例： \n{demo_url}  \n参数：  \n```json\n{body}\n```"
                        .replace("{version}", Objects.toString(id))
                        .replace("{url}", url)
                        .replace("{method}", method.name())
                        .replace("{notes}", String.join("  \n", notes))
                        .replace("{props}", Optional.ofNullable(props)
                                .orElse(Collections.emptyList()).stream()
                                .map(prop -> String.format("%s|%s|%s|%s", prop.getName(), prop.getType(), prop.isRequired() ? "true" : "-", prop.getComment()
//                                        ,
//                                        Optional.ofNullable(extend.getOptions()).map(JSON::toJSONString).orElse(""),
//                                        Optional.ofNullable(extend.getProps()).map(JSON::toJSONString).orElse("")
                                ))
                                .collect(Collectors.joining("\n")))
                        .replace("{console}", Optional.ofNullable(props).map(v -> JSON.toJSONString(v, SerializerFeature.PrettyFormat, SerializerFeature.SortField).replaceAll("\t", "  ")).orElse(""))
                        .replace("{demo_url}", demo.getOrDefault("url", "").toString())
                        .replace("{body}", Optional.ofNullable(demo.get("body")).map(body -> JSON.toJSONString(body, SerializerFeature.PrettyFormat, SerializerFeature.SortField).replaceAll("\t", "  ")).orElse(""))

        );
        return this;
    }

    public Version execute(final Consumer<Version> consumer) {
        consumer.accept(this);
        return this;
    }

    @Override
    public String toString() {
        return json();
    }

    public static void main(String[] args) {
        System.out.println(
                Version.builder()
                        .id(1)
                        .markdown("version.md")
                        .url("/open/auth/login/{version}")
                        .method(POST)
                        .notes(Arrays.asList(
                                "接口详细说明",
                                "1.版本变更说明"
                        ))
                        .build()
                        .setDemo("http://beta.mvc.com/open/auth/login/{version}",
                                Maps.ofSS()
                                        .put("username", "admin")
                                        .put("password", "111111")
                                        .build()
                        )
                        .jsonFormat()
        );
        System.out.println(
                Version.builder()
                        .id(1)
                        .markdown("version.md")
                        .url("/open/{version}/{pageIndex}/{pageSize}")
                        .method(GET)
                        .notes(Arrays.asList(
                                "接口详细说明",
                                "1.版本变更说明"
                        ))
                        .build()
                        .demo(v -> v.setDemo(
                                "http://beta.mvc.com".concat(v.formatUrl(1, 20)),
                                Maps.ofSS()
                                        .put("username", "admin")
                                        .put("password", "111111")
                                        .build()
                        ))
                        .write(Paths.get("CATALINA_HOME_IS_UNDEFINED").toAbsolutePath().toString())
                        .jsonFormat()
        );
    }
}
