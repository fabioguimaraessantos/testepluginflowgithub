package com.ciandt.pms.dialect;

import org.hibernate.dialect.Oracle9iDialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

public class CustomOracleDialect extends Oracle9iDialect {

    public CustomOracleDialect() {
        super();
        registerColumnType(Types.NVARCHAR, 4000, "nvarchar($l)");
        registerColumnType(Types.NVARCHAR, "nvarchar(max)");

        registerColumnType(Types.NCHAR, 2000, "nchar($l)");
        registerColumnType(Types.NCHAR, "nchar(max)");

        registerColumnType(Types.NCLOB, "nclob");

        registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.NCHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.NCLOB, StandardBasicTypes.STRING.getName());
    }
}
