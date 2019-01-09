package web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mvc.entity.base.Param;
import com.mvc.entity.base.Result;
import com.utils.util.Dates;
import com.utils.util.FCopy;
import com.utils.util.FWrite;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mvc.enums.Code.SUCCESS;
import static com.utils.enums.Charsets.UTF_8;
import static com.utils.util.Dates.Pattern.yyyyMMddHHmmss;
import static com.utils.util.Dates.Pattern.yyyy_MM_dd_HH_mm_ss;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

/**
 *
 * @author 谢长春 on 2017/11/20.
 */
interface ITest {
    /**
     * 获取日志存储路径
     */
    default java.nio.file.Path getPath() {
        return Paths.get("logs").toAbsolutePath().resolve(this.getClass().getSimpleName()); // 单元测试结果，写入文件路径
    }

    default String getMethodName() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        Matcher m = Pattern.compile("(?<=[$])(.+)(?=[$])").matcher(methodName);
        return m.find() ? m.group() : methodName;
    }

    default String format(String pattern, Object... values) {
        for (Object value : values) {
            pattern = pattern.replaceFirst("\\{([a-zA-Z0-9]+)?}", value.toString());
        }
        return pattern;
    }

    default void asserts(String methodName, ResultActions resultActions) {
        Objects.requireNonNull(methodName, "参数【methodName】是必须的");
        Objects.requireNonNull(resultActions, "参数【resultActions】是必须的");
        Tester.builder()
                .clazz(this.getClass())
                .methodName(methodName)
                .build()
                .asserts(resultActions);
    }

    default String responseJsonFormat(String jsonText) {
        return jsonText;
    }

    default void writeFile(String methodName, String jsonText) throws IOException {
        try {
            FCopy.ofDefault()
                    .from(FWrite
                            .of(getPath().resolve(methodName).resolve(yyyyMMddHHmmss.now() + ".json").toFile())
                            .write(responseJsonFormat(jsonText))
                            .getFile()
                    )
                    .to(getPath().resolve(methodName + ".json").toFile())
                    .copyTo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @author 谢长春 on 2017/11/21.
     */
    @Builder
    @Data
    @Accessors(chain = true, fluent = true)
    class Tester {
        @Builder.Default
        private String username = "admin";
        @Builder.Default
        private String password = "admin";
        private Class clazz;
        private String methodName;
        private MockMvc mockMvc;
        private String url;
        private int version;
        private String response;

        private String getUrl() {
            return url.replace("{version}", Objects.toString(version));
        }

        /**
         * 获取日志存储路径
         */
        public java.nio.file.Path getPath() {
            return Paths.get("logs").toAbsolutePath().resolve(clazz.getSimpleName()); // 单元测试结果，写入文件路径
        }

        public String getMethodName() {
            if (Objects.isNull(this.methodName)) {
                this.methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
                final Matcher m = Pattern.compile("(?<=[$])(.+)(?=[$])").matcher(this.methodName);
                if (m.find()) {
                    this.methodName = m.group();
                }
            }
            return this.methodName;
        }

        public void asserts(ResultActions resultActions) {
            try {
                resultActions
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(res -> {
                            System.out.println(JSON.toJSONString(res.getResponse()));
                            this.response = res.getResponse().getContentAsString();
                            writeFile(this.response);
                            AssertionErrors.assertEquals("code", SUCCESS, Result.valueOfJson(this.response).getCode());
                        })
                ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void writeFile(final String jsonText) {
            try {
                FCopy.ofDefault()
                        .from(FWrite
                                .of(getPath().resolve(getMethodName()).resolve(yyyyMMddHHmmss.now() + ".json").toFile())
                                .write(JSON.toJSONString(
                                        JSON.parseObject(jsonText, Feature.OrderedField), // 保证反序列化顺序
                                        SerializerFeature.PrettyFormat
                                ))
                                .getFile()
                        )
                        .to(getPath().resolve(getMethodName() + ".json").toFile())
                        .copyTo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String format(String pattern, Object... values) {
            for (Object value : values) {
                pattern = pattern.replaceFirst("\\{(\\w+)?}", value.toString());
            }
            return pattern;
        }

        public Tester post(Param param) {
            try {
                System.err.println("url:\n" + getUrl());
                System.err.println("参数:\n" + param.toString());
                asserts(
                        mockMvc.perform(
                                MockMvcRequestBuilders
                                        .post(getUrl())
                                        .content(param.toString())
                                        .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester put(Param param) {
            try {
                System.err.println("url:\n" + getUrl());
                System.err.println("参数:\n" + param.toString());
                asserts(
                        mockMvc.perform(
                                MockMvcRequestBuilders
                                        .put(getUrl())
                                        .content(param.toString())
                                        .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester patch(Param param) {
            try {
                System.err.println("url:\n" + getUrl());
                System.err.println("参数:\n" + param.toString());
                asserts(
                        mockMvc.perform(
                                MockMvcRequestBuilders
                                        .patch(getUrl())
                                        .content(param.toString())
                                        .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester delete(Param param) {
            try {
                System.err.println("url:\n" + getUrl());
                System.err.println("参数:\n" + param.toString());
                asserts(
                        mockMvc.perform(
                                MockMvcRequestBuilders
                                        .delete(getUrl())
                                        .content(param.toString())
                                        .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester get(Param param) {
            try {
                System.err.println("url:\n" + getUrl());
                System.err.println("参数:\n" + param.toString());
                asserts(
                        mockMvc.perform(
                                MockMvcRequestBuilders
                                        .get(getUrl())
                                        .param("json", param.getJson()) // TODO get 请求带参都请注意，这里只要 json 属性
                                        .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester updateById(Object id, Param param) {
            try {
                System.err.println("url:\n" + format("{url}/{id}", getUrl(), id));
                System.err.println("参数:\n" + param.toString());
                asserts(
                        mockMvc.perform(
                                MockMvcRequestBuilders
                                        .put("{url}/{id}", getUrl(), id)
                                        .content(param.toString())
                                        .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester deleteById(Object id) {
            try {
                System.err.println("url:\n" + format("{url}/{id}", getUrl(), id));
                System.err.println("参数:");
                asserts(
                        mockMvc.perform(
                                MockMvcRequestBuilders
                                        .delete("{url}/{id}", getUrl(), id)
                                        .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester deleteByUid(Object id, String uid) {
            try {
                System.err.println("url:\n" + format("{url}/{id}/{uid}", getUrl(), id, uid));
                System.err.println("参数:");
                asserts(
                        mockMvc.perform(MockMvcRequestBuilders
                                .delete("{url}/{id}/{uid}", getUrl(), id, uid)
                                .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester markDeleteById(Object id) {
            try {
                System.err.println("url:\n" + format("{url}/{id}", getUrl(), id));
                System.err.println("参数:");
                asserts(
                        mockMvc.perform(MockMvcRequestBuilders
                                .patch("{url}/{id}", getUrl(), id)
                                .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester markDeleteByUid(Object id, String uid) {
            try {
                System.err.println("url:\n" + format("{url}/{id}/{uid}", getUrl(), id, uid));
                System.err.println("参数:");
                asserts(
                        mockMvc.perform(MockMvcRequestBuilders
                                .patch("{url}/{id}/{uid}", getUrl(), id, uid)
                                .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester markDelete(Param param) {
            try {
                System.err.println("url:\n" + getUrl());
                System.err.println("参数:\n" + param.toString());
                asserts(
                        mockMvc.perform(
                                MockMvcRequestBuilders
                                        .patch(getUrl())
                                        .content(param.toString())
                                        .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester findById(Object id) {
            try {
                System.err.println("url:\n" + format("{url}/{id}", getUrl(), id));
                System.err.println("参数:");
                asserts(
                        mockMvc.perform(
                                MockMvcRequestBuilders
                                        .get("{url}/{id}", getUrl(), id)
                                        .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester findByIdTimestamp(Object id, long timestamp) {
            try {
                System.err.println("url:\n" + format("{url}/{id}/{timestamp}", getUrl(), id, timestamp));
                System.err.println("参数:");
                asserts(
                        mockMvc.perform(MockMvcRequestBuilders
                                .get("{url}/{id}/{timestamp}", getUrl(), id, timestamp)
                                .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester findByUid(Object id, String uid) {
            try {
                System.err.println("url:\n" + format("{url}/{id}/{uid}", getUrl(), id, uid));
                System.err.println("参数:");
                asserts(
                        mockMvc.perform(MockMvcRequestBuilders
                                .get("{url}/{id}/{uid}", getUrl(), id, uid)
                                .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester findByUidTimestamp(Object id, String uid, String timestamp) {
            try {
                System.err.println("url:\n" + format("{url}/{id}/{uid}/{timestamp}", getUrl(), id, uid, Dates.of(timestamp, yyyy_MM_dd_HH_mm_ss).getTimeMillis()));
                System.err.println("参数:");
                asserts(
                        mockMvc.perform(MockMvcRequestBuilders
                                .get("{url}/{id}/{uid}/{timestamp}", getUrl(), id, uid, Dates.of(timestamp, yyyy_MM_dd_HH_mm_ss).getTimeMillis())
                                .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Tester page(int number, int size, Param param) {
            try {
                System.err.println("url:\n" + format("{url}/page/{number}/{size}", getUrl(), number, size));
                System.err.println("参数:\n?json=" + UTF_8.encode(param.toString()));
                asserts(
                        mockMvc.perform(MockMvcRequestBuilders
                                .get("{url}/page/{number}/{size}", getUrl(), number, size)
                                .param("json", param.getJson()) // TODO get 请求带参都请注意，这里只要 json 属性
                                .with(httpBasic(username, password))
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

    }

}