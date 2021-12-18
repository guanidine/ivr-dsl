package priv.ivrdsl.controller;

import priv.ivrdsl.impl.QueryCaseImpl;
import priv.ivrdsl.impl.ResultSetCaller;
import priv.ivrdsl.util.SqlQueryUtils;

import java.sql.SQLException;

/**
 * 从数据库表中获取信息。
 *
 * @author Guanidine Beryllium
 */
public class DataAccessor {
    /**
     * 根据重载过后的 {@code queryCase} 提供的条件，在指定 SQL 表中查询所需信息，并返回查询结果。
     *
     * @param table 查询目标关系表
     * @return 处理后的查询结果
     * @see QueryCaseImpl#queryCase
     */
    public static String data2String(String table, QueryCaseImpl queryCase) {
        StringBuilder sb = new StringBuilder();
        ResultSetCaller caller = rs -> {
            try {
                int columnCount = rs.getMetaData().getColumnCount();
                if (rs.next()) {
                    sb.append("尊敬的客户").append(rs.getString(1)).append("您好,您的");
                    for (int i = 2; i <= columnCount; i++) {
                        sb.append(rs.getMetaData().getColumnName(i)).append("为").append(rs.getString(i)).append(", ");
                    }
                } else {
                    sb.append("抱歉,没有查到您的信息");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        SqlQueryUtils.query(table, caller, queryCase);
        return sb.toString();
    }
}
