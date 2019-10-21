package com.ccx.demo.business.example.entity.types;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.TypeReference;
//import com.ccx.demo.business.example.entity.convert.ListLongJsonConvert;
//import org.hibernate.cfg.Configuration;
//import org.hibernate.dialect.Dialect;
//import org.hibernate.type.AbstractSingleColumnStandardBasicType;
//import org.hibernate.type.DiscriminatorType;
//import org.hibernate.type.descriptor.sql.BooleanTypeDescriptor;
//import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;
//
///**
// * <pre>
// * FIXME:
// *
// * @author 谢长春 2019/8/22
// */
//public class ArrayLongType extends AbstractSingleColumnStandardBasicType<Long[]> implements DiscriminatorType<Long[]> {
//
//    public static final ArrayLongType INSTANCE = new ArrayLongType();
//
//    public ArrayLongType() {
//        super(VarcharTypeDescriptor.INSTANCE, VarcharTypeDescriptor.INSTANCE);
//    }
//
//    @Override
//    public String getName() {
//        Configuration configuration = new Configuration().configure();
//        configuration.addAttributeConverter(new ListLongJsonConvert());
//        return "Long[]";
//    }
//
//    @Override
//    public Long[] stringToObject(String text) throws Exception {
//        return JSON.parseObject(text, new TypeReference<Long[]>() {
//        });
//    }
//
//    @Override
//    public String objectToSQLString(Long[] value, Dialect dialect) throws Exception {
//        return JSON.toJSONString(value);
//    }
//}
