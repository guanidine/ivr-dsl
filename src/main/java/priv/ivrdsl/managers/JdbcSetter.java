package priv.ivrdsl.managers;

import lombok.extern.slf4j.Slf4j;
import priv.ivrdsl.beans.JdbcBean;
import priv.ivrdsl.utils.JdbcXmlUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 设置 IVR 涉及到信息查询业务时的数据源。
 *
 * @author Guanidine Beryllium
 */
@Slf4j
public class JdbcSetter {
    /**
     * 数据库配置。
     *
     * @param table      查询数据源关系表
     * @param driver     所属数据库驱动
     * @param url        数据库 jdbc 链接
     * @param user       数据库访问用户名
     * @param passwd     数据库访问密码
     * @param jdbcConfig jdbc 配置文件位置
     * @return 配置成功返回 {@code null}，否则会返回错误信息
     * @see JdbcXmlUtils
     */
    public static String setJdbc(String table, String driver, String url, String user, String passwd, String jdbcConfig) throws IOException {
        if (table != null && driver != null && url != null && user != null && passwd != null) {
            JdbcBean jdbc = new JdbcBean(table, driver, url, user, passwd);
            if (JdbcXmlUtils.find(table, jdbcConfig) != null) {
                JdbcXmlUtils.alter(jdbc, jdbcConfig);
            } else {
                JdbcXmlUtils.add(jdbc, jdbcConfig);
            }
            log.info(" Success : {} {{}, {}, {}, {}}", table, driver, url, user, passwd);
            return null;
        } else {
            return missingParamsError(table, driver, url, user, passwd);
        }
    }

    /**
     * 生成缺少参数的报错信息。
     *
     * @param table  查询数据源关系表
     * @param driver 所属数据库驱动
     * @param url    数据库 jdbc 链接
     * @param user   数据库访问用户名
     * @param passwd 数据库访问密码
     * @return 返回参数缺少信息
     */
    private static String missingParamsError(String table, String driver, String url, String user, String passwd) {
        ArrayList<String> missingParams = new ArrayList<>() {{
            if (table == null) {
                add("-table");
            }
            if (driver == null) {
                add("-driver");
            }
            if (url == null) {
                add("-url");
            }
            if (user == null) {
                add("-user");
            }
            if (passwd == null) {
                add("-passwd");
            }
        }};
        return String.join(" ", missingParams);
    }
}
