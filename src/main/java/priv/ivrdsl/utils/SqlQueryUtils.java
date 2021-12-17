package priv.ivrdsl.utils;

import priv.ivrdsl.beans.JdbcBean;
import priv.ivrdsl.exceptions.DatabasePropsExceptions;
import priv.ivrdsl.impls.QueryCaseImpl;
import priv.ivrdsl.impls.ResultSetCaller;

import java.sql.*;

import static priv.ivrdsl.utils.JdbcXmlUtils.find;

/**
 * SQL 查询。
 *
 * @author Guanidine Beryllium
 */
public class SqlQueryUtils {
    /**
     * 查询指定数据库中的表数据, 并通过接口回调返回查询结果。
     * <p>
     * 查询条件通过 {@link QueryCaseImpl#queryCase} 接口重载设置。
     *
     * @param table  待查表格。查询语句为表格加上了双引号，请注意表名大小写
     * @param caller - 回调的接口
     * @see QueryCaseImpl#queryCase
     * @see ResultSetCaller#callBack
     */
    public static void query(String table, ResultSetCaller caller,QueryCaseImpl queryCase) {
        JdbcBean jdbc = find(table);
        if (jdbc == null) {
            throw new DatabasePropsExceptions("ERROR: Fail to find configurations for table \"" + table + "\"");
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from \"" + table + "\"" + queryCase.queryCase();

        try {
            Class.forName(jdbc.getDriver());
            conn = DriverManager.getConnection(jdbc.getUrl(), jdbc.getUser(), jdbc.getPasswd());
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            caller.callBack(rs);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeConnection(conn, ps);
        }
    }


    /**
     * 关闭数据库连接
     *
     * @param conn 数据库连接
     * @param ps   预编译 sql 语句
     */
    private static void closeConnection(Connection conn, PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
