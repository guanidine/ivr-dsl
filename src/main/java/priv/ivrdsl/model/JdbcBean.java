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
    String name;
    String driver;
    String url;
    String user;
    String passwd;
}
