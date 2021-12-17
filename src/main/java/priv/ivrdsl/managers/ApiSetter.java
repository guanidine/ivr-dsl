package priv.ivrdsl.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 设置百度语音合成的 APPID/AK/SK。
 *
 * @author Guanidine Beryllium
 */
public class ApiSetter {
    /**
     * 设置 APPID/AK/SK。
     *
     * @throws IOException properties 配置文件创建或打开失败
     */
    public static void setApi(String appId, String apiKey, String secretKey, String configFile) throws IOException {
        File file = new File(configFile);
        Properties props = new Properties();
        if (file.exists()) {
            props.load(new FileInputStream(file));
            props.setProperty("app_id", appId == null ? props.getProperty("app_id") : appId);
            props.setProperty("api_key", apiKey == null ? props.getProperty("api_key") : apiKey);
            props.setProperty("secret_key", secretKey == null ? props.getProperty("secret_key") : secretKey);
            props.store(new FileOutputStream(file), "baidu api");
        } else {
            if (file.createNewFile()) {
                props.setProperty("app_id", appId == null ? "" : appId);
                props.setProperty("api_key", apiKey == null ? "" : apiKey);
                props.setProperty("secret_key", secretKey == null ? "" : secretKey);
                props.store(new FileOutputStream(file), "baidu api");
            }
        }
    }
}
