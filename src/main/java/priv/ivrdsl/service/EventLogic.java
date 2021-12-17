package priv.ivrdsl.service;

import org.dom4j.DocumentException;
import priv.ivrdsl.model.EnumBean.Action;
import priv.ivrdsl.model.GlobalVariableBean;
import priv.ivrdsl.model.EventBean;
import priv.ivrdsl.impl.QueryCaseImpl;
import priv.ivrdsl.view.Style;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;

import static priv.ivrdsl.controller.DataAccessor.data2String;

/**
 * 处理事件运行逻辑。
 *
 * @author Guanidine Beryllium
 */
public class EventLogic {
    /**
     * 处理不同动作的运行逻辑。
     *
     * @param event 用户点击图形界面的事件
     * @throws IOException          音频文件读写失败
     * @throws InterruptedException 线程中断失败
     * @throws DocumentException    xml 配置文件读取失败
     */
    public static void runLogic(ActionEvent event, QueryCaseImpl queryCase) throws IOException, InterruptedException, DocumentException {
        detectError(event);
        EventBean currentEvent = GlobalVariableBean.event2TriggerMap.get(GlobalVariableBean.curTriggerPath);
        Style.setTextToScreen(currentEvent.getAction().getCode());
        if (currentEvent.getAction() != Action.ACTION_MENU) {
            switch (currentEvent.getAction()) {
                case ACTION_BACK -> GlobalVariableBean.curTriggerPath =
                        GlobalVariableBean.curTriggerPath.substring(0, GlobalVariableBean.curTriggerPath.length() - 2);
                case ACTION_CALL -> {
                    GlobalVariableBean.voiceOutput.addText("正在为您转接" + currentEvent.getAdditions());
                    Style.setTextToScreen("Direct Call");
                }
                case ACTION_INFO -> {
                    GlobalVariableBean.voiceOutput.addText(data2String(currentEvent.getAdditions(), queryCase));
                    Style.setTextToScreen("Getting Information");
                }
                case ACTION_HANGUP -> {
                    GlobalVariableBean.voiceOutput.addText("再见");
                    Style.setTextToScreen("End of Call");
                }
                case ACTION_MANUAL -> {
                    GlobalVariableBean.voiceOutput.addText("正在为您转接人工服务");
                    Style.setTextToScreen("Manual Service");
                }
                case ACTION_RECORD -> {
                    Style.setTextToScreen("Recording");
                    Thread.sleep(3000);
                    GlobalVariableBean.voiceOutput.addText("已为您完成录音，再见");
                }
                case ACTION_REPLAY -> GlobalVariableBean.curTriggerPath =
                        GlobalVariableBean.curTriggerPath.substring(0, GlobalVariableBean.curTriggerPath.length() - 1);
            }
        }
        finishAction(currentEvent);
    }

    /**
     * 初始化事件树。将当前事件 {@code currentEvent} 设为根节点“0”。
     */
    public static void runInitSetup() {
        EventBean currentEvent = GlobalVariableBean.event2TriggerMap.get("0");
        for (EventBean item : currentEvent.getChilds()) {
            GlobalVariableBean.possibleOptionList.add(item.getTrigger());
        }
    }

    /**
     * 预处理。判断按键是否可选。
     *
     * @param event 用户点击图形界面的事件
     */
    private static void detectError(ActionEvent event) {
        String character = event.getActionCommand();
        HashMap<String, String> charMap = new HashMap<>(12) {{
            put("0", "按键0");
            put("1", "按键1");
            put("2", "按键2");
            put("3", "按键3");
            put("4", "按键4");
            put("5", "按键5");
            put("6", "按键6");
            put("7", "按键7");
            put("8", "按键8");
            put("9", "按键9");
            put("*", "星号键");
            put("#", "井号键");
        }};
        if (!GlobalVariableBean.possibleOptionList.contains(character)) {
            GlobalVariableBean.voiceOutput.addText(charMap.get(character) + "不是一个可选的服务项，请重新按键");
        } else {
            GlobalVariableBean.curTriggerPath = GlobalVariableBean.curTriggerPath + character;
        }
    }


    /**
     * 动作结束后的处理。
     *
     * @param currentEvent 当前事件
     * @throws IOException 访问音频文件失败
     */
    private static void finishAction(EventBean currentEvent) throws IOException {
        GlobalVariableBean.hasFinished = currentEvent.isFinal();
        if (GlobalVariableBean.hasFinished) {
            GlobalVariableBean.voiceOutput.speak();
            Style.setTextToScreen("PhoneCall finished");
        } else if (currentEvent.getAction() != Action.ACTION_MENU) {
            currentEvent = GlobalVariableBean.event2TriggerMap.get(GlobalVariableBean.curTriggerPath);
        }
        GlobalVariableBean.possibleOptionList.clear();
        HashMap<String, String> charMap = new HashMap<>(12) {{
            put("0", "0");
            put("1", "1");
            put("2", "2");
            put("3", "3");
            put("4", "4");
            put("5", "5");
            put("6", "6");
            put("7", "7");
            put("8", "8");
            put("9", "9");
            put("*", "星号键");
            put("#", "井号键");
        }};
        for (EventBean child : currentEvent.getChilds()) {
            GlobalVariableBean.voiceOutput.addText(
                    ", " + child.getName() + " 请按 " + charMap.get(child.getTrigger()) + " ,");
            GlobalVariableBean.possibleOptionList.add(child.getTrigger());
        }
        GlobalVariableBean.voiceOutput.speak();
    }
}
