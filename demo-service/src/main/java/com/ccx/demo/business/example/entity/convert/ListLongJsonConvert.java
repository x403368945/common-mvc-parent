package com.ccx.demo.business.example.entity.convert;

import com.alibaba.fastjson.JSON;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.usertype.UserType;

import javax.persistence.AttributeConverter;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * 支撑 mysql 原生 JSON 数据类型与实体类属性的映射；
 *
 * @author 谢长春 2019/2/12
 */
public class ListLongJsonConvert implements AttributeConverter<List<Long>, String>, UserType {
    @Override
    public String convertToDatabaseColumn(final List<Long> o) {
        return JSON.toJSONString(o);
    }

    @Override
    public List<Long> convertToEntityAttribute(final String s) {
        return JSON.parseArray(s, Long.class);
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.BIGINT};
    }

    @Override
    public Class returnedClass() {
        return null;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return false;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return 0;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {

    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return null;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return null;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return null;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return null;
    }
}
