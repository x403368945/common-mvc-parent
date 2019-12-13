package com.support.mvc.dao;

import org.hibernate.dialect.MySQL57Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.ObjectType;
import org.hibernate.type.StandardBasicTypes;

/**
 * 注册自定义方言函数
 *
 * @author 谢长春 2019-12-13
 */
public class CustomMySQL57Dialect extends MySQL57Dialect {
    public CustomMySQL57Dialect() {
        super();
//        registerFunction( "concat", new StandardSQLFunction( "concat", StandardBasicTypes.STRING ) );
        registerFunction( "group_concat", new StandardSQLFunction( "group_concat", StandardBasicTypes.STRING ) );
        // FIXME: 2019/12/13 未完成 *************************************************************************************
//        registerFunction("ifnull", new StandardSQLFunction("ifnull", ObjectType.INSTANCE));
//        registerFunction("ifnull", new SQLFunctionTemplate(ObjectType.INSTANCE, "ifnull"));
    }
}
