package priv.ivrdsl.beans;

import priv.ivrdsl.VoiceMenu;
import priv.ivrdsl.utils.VoiceOutputUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 全局变量。
 * <p>
 * 每个生成的 IVR 项目都有且仅有一个全局共享的 {@code GlobalVariableBean} 类，随程序运行即得到创建，以得到对这些贯穿程序始终的变量的便捷访问。
 *
 * @author Guanidine Beryllium
 */
public class GlobalVariableBean {
    /** 当前按键路径 */
    public static String curTriggerPath = "0";
    /** 可选按键集合 */
    public static List<String> possibleOptionList = new ArrayList<>();
    /** 事件对象名与按键路径的映射表 */
    public static HashMap<String, EventBean> event2TriggerMap = new HashMap<>();
    /** 事件对象名与语音输出名称的映射表 */
    public static HashMap<String, String> event2VoiceTextMap = new HashMap<>();
    /** IVR 语音输出 */
    public static VoiceOutputUtils voiceOutput = new VoiceOutputUtils();
    /** IVR 程序开始记号 */
    public static Boolean hasStarted = false;
    /** IVR 程序终止记号 */
    public static Boolean hasFinished = false;
    /** IVR 客户信息接口的实例化对象 */
    public static VoiceMenu.UserInfoCase userInfoCase = new VoiceMenu.UserInfoCase();
}
