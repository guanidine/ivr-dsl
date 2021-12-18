package priv.ivrdsl.unit;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import priv.ivrdsl.model.JdbcBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static priv.ivrdsl.controller.ApiSetter.setApi;
import static priv.ivrdsl.controller.JdbcSetter.setJdbc;
import static priv.ivrdsl.controller.XmlOperator.find;

@Test(groups = "ut", priority = 5)
public class TestControllerUT {
    String jdbcConfig = "src/test/resources/jdbc.xml";
    String apiConfig = "src/test/resources/apikey.properties";

    @DataProvider
    public Object[][] jdbcDataProvider() {
        return new Object[][]{
                {new JdbcBean("table1", "driver1", "url1", "user1", "passed1"),
                        new JdbcBean("table1", "driver1", "url1", "user1", "passed1")},
                {new JdbcBean("table1", "driver2", "url2", "user2", "passed2"),
                        new JdbcBean("table1", "driver2", "url2", "user2", "passed2")},
                {new JdbcBean("table2", "driver2", "url2", "user2", "passed2"),
                        new JdbcBean("table2", "driver2", "url2", "user2", "passed2")},
                {new JdbcBean("table3", "driver3", "url3", "user3", "passed3"),
                        new JdbcBean("table3", "driver3", "url3", "user3", "passed3")},
                {new JdbcBean("table1", "driver3", "url3", "user3", "passed3"),
                        new JdbcBean("table1", "driver3", "url3", "user3", "passed3")},
                {new JdbcBean("tbDataPlan", "org.postgresql.Driver", "jdbc:postgresql://117.78.10.141:8000/postgres", "user86", "user86@bupt"),
                        new JdbcBean("tbDataPlan", "org.postgresql.Driver", "jdbc:postgresql://117.78.10.141:8000/postgres", "user86", "user86@bupt")}
        };
    }

    @DataProvider
    public Object[][] apiDataProvider() {
        return new Object[][]{
                {new ApiBean(null, null, "not-null"),
                        new ApiBean("", "", "not-null")},
                {new ApiBean("appId1", "apiKey", "secretKey"),
                        new ApiBean("appId1", "apiKey", "secretKey")},
                {new ApiBean(null, null, null),
                        new ApiBean("appId1", "apiKey", "secretKey")},
                {new ApiBean("25286979", "zQ6BhKR7zPchzhhRTikw0lwL", "RbOGe7GINgzOjvXg6fz3iUQrailYdlxe"),
                        new ApiBean("25286979", "zQ6BhKR7zPchzhhRTikw0lwL", "RbOGe7GINgzOjvXg6fz3iUQrailYdlxe")}
        };
    }

    @BeforeClass
    public void prepare() {
        System.out.println("-------------------Test Controllers");
        File f = new File(jdbcConfig);
        if (f.exists()) {
            f.delete();
        }
        f = new File(apiConfig);
        if (f.exists()) {
            f.delete();
        }
    }

    @Test(dataProvider = "jdbcDataProvider")
    public void testJdbcSetter(final JdbcBean origin, final JdbcBean result) throws IOException {
        setJdbc(origin.getName(), origin.getDriver(), origin.getUrl(), origin.getUser(), origin.getPasswd(), jdbcConfig);
        JdbcBean ans = find(origin.getName(), jdbcConfig);
        Assert.assertNotNull(ans);
        Assert.assertEquals(ans.toString(), result.toString());
    }

    @Test(dataProvider = "apiDataProvider")
    public void testApiSetter(final ApiBean origin, final ApiBean result) throws IOException {
        setApi(origin.appId, origin.apiKey, origin.secretKey, apiConfig);
        Properties props = new Properties();
        props.load(new FileInputStream(apiConfig));
        ApiBean ans = new ApiBean(props.getProperty("app_id"), props.getProperty("api_key"), props.getProperty("secret_key"));
        Assert.assertNotNull(ans);
        Assert.assertEquals(ans.toString(), result.toString());
    }

    @ToString
    @AllArgsConstructor
    static class ApiBean {
        String appId;
        String apiKey;
        String secretKey;
    }
}
