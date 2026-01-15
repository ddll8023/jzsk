package com.szy.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;

public class DynamicPaginationInnerInterceptor extends PaginationInnerInterceptor {
    @Override
    public DbType getDbType() {
        String ds = DynamicDataSourceContextHolder.peek();
        if (ds == null) {
            return DbType.MYSQL; // 默认
        }
        ds = ds.toLowerCase();
        if (ds.contains("mysql")) {
            return DbType.MYSQL;
        } else if (ds.contains("pgsql")) {
            return DbType.POSTGRE_SQL;
        } else if (ds.contains("sqlserver") || ds.contains("dbo") || ds.contains("zkxt")) {
            return DbType.SQL_SERVER;
        }
        return DbType.MYSQL; // 默认
    }
} 