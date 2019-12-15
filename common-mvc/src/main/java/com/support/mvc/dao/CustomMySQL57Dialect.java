package com.support.mvc.dao;

import org.hibernate.dialect.MySQL57Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

/**
 * 注册自定义方言函数
 * {@link org.hibernate.engine.jdbc.dialect.spi.DialectResolver}
 * https://blog.csdn.net/qq_19671173/article/details/82909338
 *
 * @author 谢长春 2019-12-13
 */
public class CustomMySQL57Dialect extends MySQL57Dialect {
    public CustomMySQL57Dialect() {
        super();
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
        registerFunction("ifnull", new StandardSQLFunction("ifnull"));
//        registerFunction("if", new StandardSQLFunction("if"));
//        registerFunction("countif", new SQLFunctionTemplate(StandardBasicTypes.LONG, "count(if(?1, ?2, ?3))"));
//        registerFunction("sumif", new StandardSQLFunction("ifnull"));
//        registerFunction("sumifnull", new StandardSQLFunction("ifnull"));
    }
}
