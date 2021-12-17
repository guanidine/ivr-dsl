package priv.ivrdsl.utils;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import javazoom.jl.player.Player;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

/**
 * 百度语音API接口。
 *
 * @author Guanidine Beryllium
 */
@Slf4j
public class SpeechSynthesisUtils {
    /** 百度语音识别的 Java 客户端 */
    private static volatile AipSpeech client;

    /**
     * 单例模式实例化。读取 API 配置时使用反射访问，因此可以在打包成 jar 包后在其他项目中正确运行。
     *
     * @return 返回实例化后的 {@code AipSpeech} 类
     */
    private static AipSpeech getInstance() throws IOException {
        if (client == null) {
            synchronized (AipSpeech.class) {
                if (client == null) {
                    Properties props = new Properties();
                    InputStream is = SpeechSynthesisUtils.class
                            .getClassLoader()
                            .getResourceAsStream("apikey.properties");
                    props.load(is);
                    client = new AipSpeech(
                            props.getProperty("app_id"),
                            props.getProperty("api_key"),
                            props.getProperty("secret_key")
                    );
                }
            }
        }
        return client;
    }

    /**
     * 语音合成并以 mp3 格式输出。
     *
     * @param text       文字内容
     * @param outputPath 合成语音生成路径
     * @throws IOException 文件打开失败
     * @see AipSpeech
     */
    private static void speechSynthesizer(String text, String outputPath) throws IOException {
        int maxLength = 1024;
        if (text.getBytes().length >= maxLength) {
            return;
        }
        client = getInstance();

        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        TtsResponse res = client.synthesis(text, "zh", 1, null);
        byte[] data = res.getData();
        org.json.JSONObject res1 = res.getResult();
        if (data != null) {
            try {
                Util.writeBytesToFileSystem(data, outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if (res1 != null) {
            log.info(" result : " + res1);
        }
        // TODO: 日志？
    }

    /**
     * 将文字合成为 mp3 格式的音频并播放。
     *
     * @param text 待转换文字内容
     */
    public static void mp3AudioPlay(String text) {
        String outputPath = "SpeechSynthesizer.mp3";
        try {
            File file = new File(outputPath);
            speechSynthesizer(text, outputPath);
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            Player jlPlayer = new Player(bufferedInputStream);
            jlPlayer.play();
        } catch (Exception e) {
            System.out.println("Problem playing mp3 file " + outputPath);
            System.out.println(e.getMessage());
        }
    }

}

