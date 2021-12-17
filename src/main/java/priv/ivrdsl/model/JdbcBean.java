package priv.ivrdsl.model;

import lombok.Getter;

/**
 * JDBC配置。
 *
 * @author Guanidine Beryllium
 */
@Getter
public class JdbcBean {
    String name;
    String driver;
    String url;
    String user;
    String passwd;

    public JdbcBean(String name, String driver, String url, String user, String passwd) {
        this.name = name;
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.passwd = passwd;
    }
}
