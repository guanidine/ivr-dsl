package priv.ivrdsl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * JDBC配置。
 *
 * @author Guanidine Beryllium
 */
@Getter
@ToString
@AllArgsConstructor
public class JdbcBean {
    /** 关系表表名 */
    String name;
    /** 数据库驱动 */
    String driver;
    /** 数据库 url */
    String url;
    /** 数据库用户名 */
    String user;
    /** 数据库密码 */
    String passwd;
}
