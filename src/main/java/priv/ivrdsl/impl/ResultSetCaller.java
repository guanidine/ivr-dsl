package priv.ivrdsl.impl;

import priv.ivrdsl.controller.DataAccessor;
import priv.ivrdsl.util.SqlQueryUtils;

import java.sql.ResultSet;

/**
 * SQL 查询结果的回调接口。结合回调操作在处理完 SQL 查询结果后再释放查询时申请的资源。
 *
 * @author Guanidine Beryllium
 * @see DataAccessor#data2String
 * @see SqlQueryUtils#query
 */
public interface ResultSetCaller {
    /**
     * 回调接口。
     *
     * @param rs - SQL 查询结果
     * @see DataAccessor#data2String
     * @see SqlQueryUtils#query
     */
    void callBack(ResultSet rs);
}
