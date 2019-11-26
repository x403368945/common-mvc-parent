package com.support.mvc.entity;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

/**
 * <pre>
 * 生成 32 位 uuid
 * 使用 {@link UUID#randomUUID()} 生成之后，替换 ‘-’，得到 32 位 uuid
 *
 * 使用参考：在实体类主键 ID 字段使用注解
 * \@GeneratedValue(generator = "uuid32")
 * \@GenericGenerator(name = "uuid32", strategy = "com.support.mvc.entity.UUID32Generator")
 *  private String id;
 *
 * @author 谢长春 2019/9/17
 */
public class UUID32Generator implements IdentifierGenerator, Configurable {
    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {

    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return UUID.randomUUID().toString().replace("-", "");
    }
}