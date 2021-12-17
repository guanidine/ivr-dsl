package priv.ivrdsl.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理方法类。
 *
 * @author Guanidine Beryllium
 */
public class StringProcessUtils {

    /**
     * 字符串分割。将字符串按 {@code " "} 分割，同时将引号内的字串视为一个完整的字符串，忽略其中的空格。
     *
     * @param text 待处理字符串
     * @return 处理后的字符串数组
     */
    public static String[] splitString(String text) {
        String regex = "\"([^\"]*)\"|(\\S+)";
        ArrayList<String> commandArray = new ArrayList<>();
        Matcher m = Pattern.compile(regex).matcher(text);
        while (m.find()) {
            if (m.group(1) != null) {
                commandArray.add(m.group(1));
            } else {
                commandArray.add(m.group(2));
            }
        }
        return commandArray.toArray(new String[0]);
    }

    /**
     * 去除字符串最后一位。
     *
     * @param text 待处理字符串
     * @return 处理后的字符串。原串为 {@code null} 或 {@code ""} 则返回 {@code null}
     */
    public static String removeLastChar(String text) {
        if (text == null || text.length() < 1) {
            return null;
        } else if (text.length() == 1) {
            return "";
        }
        return text.substring(0, text.length() - 1);
    }

    /**
     * 取出字符串最后一位。
     *
     * @param text 待处理字符串
     * @return 处理后字符串。原串为 {@code null} 或 {@code ""} 则返回 {@code null}
     */
    public static String getLastChar(String text) {
        if (text == null || text.length() < 1) {
            return null;
        }
        return text.substring(text.length() - 1);
    }
}
