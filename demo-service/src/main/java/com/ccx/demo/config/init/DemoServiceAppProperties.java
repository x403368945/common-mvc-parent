package com.ccx.demo.config.init;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.utils.util.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 实体：服务模块配置信息
 *
 * @author 谢长春 2019/1/22
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@PropertySource(value = "classpath:demo-service.properties", encoding = "UTF-8")
public class DemoServiceAppProperties {
    @Value("${demo.service.service.name}")
    private String name;
    @Value("${demo.service.service.pkg}")
    private String pkg;
    @Value("${demo.service.service.comment}")
    private String comment;

    @Override
    public String toString() {
        return Maps.ofSS()
                .put("service.name【服务名称】", name)
                .put("service.pkg【服务包名】", pkg)
                .put("service.comment【服务说明】", comment)
                .json(SerializerFeature.PrettyFormat);
    }
}