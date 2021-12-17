package priv.ivrdsl.impl;

import priv.ivrdsl.util.SqlQueryUtils;

/**
 * SQL 查询条件接口。覆写接口中的抽象方法 {@code queryCase} 来设定查询条件。
 *
 * @author Guanidine Beryllium
 */
@FunctionalInterface
public interface QueryCaseImpl {
    /**
     * 查询条件
     *
     * @return 返回 SQL 查询条件 where {@literal <}case{@literal >}
     * @see SqlQueryUtils#query
     */
    String queryCase();
}
