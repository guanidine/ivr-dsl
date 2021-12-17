package priv.ivrdsl.managers;

import org.dom4j.DocumentException;
import priv.ivrdsl.impls.QueryCaseImpl;
import priv.ivrdsl.impls.ResultSetCaller;
import priv.ivrdsl.utils.SqlQueryUtils;

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
     * @throws DocumentException 读取 xml 配置文件失败
     * @see QueryCaseImpl#queryCase()
     */
    public static String data2String(String table) throws DocumentException {
        StringBuilder sb = new StringBuilder();
        ResultSetCaller rowMapper = rs -> {
            try {
                int columnCount = rs.getMetaData().getColumnCount();
                if (rs.next()) {
                    sb.append("尊敬的客户").append(rs.getString(1)).append("您好，您的");
                    for (int i = 2; i <= columnCount; i++) {
                        sb.append(rs.getMetaData().getColumnName(i)).append("为").append(rs.getString(i)).append(", ");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        SqlQueryUtils.query(table, rowMapper);
        return sb.toString();
    }
}
