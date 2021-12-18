package priv.ivrdsl.util;

import lombok.extern.slf4j.Slf4j;
import priv.ivrdsl.model.GlobalVariableBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static priv.ivrdsl.util.SpeechSynthesisUtils.mp3AudioPlay;

/**
 * 语音合成输出。
 *
 * @author Guanidine Beryllium
 */
@Slf4j
public class VoiceOutputUtils {

    public static Thread waitingThr = new Thread();
    /** 目标转换字符串，最长支持 1024 个字符 */
    String text = "";

    final Pattern pattern = Pattern.compile("\\*|#|,,|^,|,$");

    public VoiceOutputUtils() {
    }

    public void addText(String text) {
        if (text != null) {
            this.text += text;
        }
    }

    /**
     * 对传入的字符串最后再做一次处理。
     * <p>
     * 将事件对象名转换成语音输出的事件名，同时将可能含有的符号“#”和“*”转换成中文，去除多余的逗号与空格。
     *
     * @return 处理后的字符串
     */
    private String processText() {
        String[] words = text.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String w : words) {
            stringBuilder.append(GlobalVariableBean.event2VoiceTextMap.getOrDefault(w, w));
        }
        String result = stringBuilder.toString();
        while (pattern.matcher(result).find()) {
            result = result.replaceAll("\\*", "星号键")
                    .replaceAll("#", "井号键")
                    .replaceAll("^,", "")
                    .replaceAll(",$", "")
                    .replaceAll(",,", ",");
        }
        return result;
    }

    /**
     * 字符串转音频并以 pcm 格式播放。
     * <p>
     * 由于使用的是单例模式，每次语音播放结束后都要手动将 {@code text} 字符串重新赋为空串。
     *
     * @see <a href="https://cloud.baidu.com/doc/SPEECH/index.html">百度语音技术</a>
     */
    public void speak() {
        String result = processText();
        if (result.length() == 0) {
            log.info(" Output : <empty string>");
        } else {
            log.info(" Output : {}", result);
            mp3AudioPlay(result);
        }
        text = "";
    }
}
