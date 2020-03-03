package com.ccx.demo.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Timestamp;
import java.util.Date;

/**
 * <pre>
 * swagger 增强版配置
 * 官方文档： https://doc.xiaominfo.com
 * demo： https://gitee.com/xiaoym/swagger-bootstrap-ui-demo.git
 * </pre>
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
//@Import(BeanValidatorPluginsConfiguration.class)
@AutoConfigureAfter(WebMvcConfig.class)
public class SwaggerConfig {

    @Bean(value = "defaultApi")
    public Docket defaultApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("全部接口")
                .apiInfo(new ApiInfoBuilder()
                        .version("1.0")
                        .title("Swagger RESTful APIs 增强版")
                        .description("### Swagger RESTful APIs 增强版， 集成指南：doc.xiaominfo.com")
                        .termsOfServiceUrl("https://ccx.cccc6666.com/")
                        .contact(new Contact("谢长春", "ccx.cccc6666.com", "403368945@qq.com"))
                        .build()
                )
                .select()
                // 生成所有API接口
//                .apis(RequestHandlerSelectors.basePackage("com.ccx.demo.open.common.web"))
                // 只生成被Api这个注解注解过的类接口
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // 只生成被ApiOperation这个注解注解过的api接口
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(Timestamp.class, String.class)
                .directModelSubstitute(Date.class, String.class)
//                .ignoredParameterTypes()
//                .globalOperationParameters(Lists.newArrayList(parameterBuilder
//                                .name("token")
//                                .description("token 令牌")
//                                .modelRef(new ModelRef("String"))
//                                .parameterType("header")
//                                .required(true)
//                                .build()
//                        )
//                )
//                .securityContexts(Lists.newArrayList(
//                        SecurityContext.builder()
//                                .securityReferences(defaultAuth())
//                                .forPaths(PathSelectors.regex("/.*"))
//                                .build()
//                ))
//                .securitySchemes(Lists.<SecurityScheme>newArrayList(new ApiKey("BearerToken", "Authorization", "header")))
                ;
    }

    @Bean(value = "openApi")
    public Docket openApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("开放接口")
                .apiInfo(new ApiInfoBuilder()
                        .version("1.0")
                        .title("Swagger RESTful APIs 增强版")
                        .description("### Swagger RESTful APIs 增强版， 集成指南：doc.xiaominfo.com")
                        .termsOfServiceUrl("https://ccx.cccc6666.com/")
                        .contact(new Contact("谢长春", "ccx.cccc6666.com", "403368945@qq.com"))
                        .build()
                )
                .select()
                // 生成所有API接口
                .apis(RequestHandlerSelectors.basePackage("com.ccx.demo.open")) // 这里不支持通配符
                // 只生成被Api这个注解注解过的类接口
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // 只生成被ApiOperation这个注解注解过的api接口
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(Timestamp.class, String.class)
                .directModelSubstitute(Date.class, String.class)
                ;
    }

    @Bean(value = "wappApi")
    public Docket wappApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("微信小程序接口")
                .apiInfo(new ApiInfoBuilder()
                        .version("1.0")
                        .title("Swagger RESTful APIs 增强版")
                        .description("### Swagger RESTful APIs 增强版， 集成指南：doc.xiaominfo.com")
                        .termsOfServiceUrl("https://ccx.cccc6666.com/")
                        .contact(new Contact("谢长春", "ccx.cccc6666.com", "403368945@qq.com"))
                        .build()
                )
                .select()
                // 生成所有API接口
                .apis(RequestHandlerSelectors.basePackage("com.ccx.demo.wapp")) // 这里不支持通配符
                // 只生成被Api这个注解注解过的类接口
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                // 只生成被ApiOperation这个注解注解过的api接口
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(Timestamp.class, String.class)
                .directModelSubstitute(Date.class, String.class)
                ;
    }

}
