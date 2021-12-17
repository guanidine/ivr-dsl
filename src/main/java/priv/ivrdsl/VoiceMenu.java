package priv.ivrdsl;

/*Generated by IVR DSL */

import org.dom4j.DocumentException;
import priv.ivrdsl.beans.EventBean;
import priv.ivrdsl.beans.GlobalVariableBean;
import priv.ivrdsl.impls.QueryCaseImpl;
import priv.ivrdsl.services.EventLogic;
import priv.ivrdsl.utils.VoiceOutputUtils;
import priv.ivrdsl.views.Init;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class VoiceMenu implements ActionListener {


    public static VoiceMenu instance;

    public static void initHashMap() {
        EventBean Action_0_pkuuh4 = new EventBean("Home", "China-Mobile", "0");
        Action_0_pkuuh4.setAction("", true);

        Action_0_pkuuh4.setAdditions("欢迎致电申国移动");

        GlobalVariableBean.event2TriggerMap.put("0", Action_0_pkuuh4);

        EventBean Action_01_pkuuh4 = new EventBean("Action_01", "转接服务", "1");
        GlobalVariableBean.event2TriggerMap.put("01", Action_01_pkuuh4);
        Action_01_pkuuh4.setAction("call", true);
        Action_01_pkuuh4.setAdditions("分号123");
        Action_0_pkuuh4.addChild(Action_01_pkuuh4);

        EventBean Action_02_pkuuh4 = new EventBean("Action_02", "信息业务", "2");
        GlobalVariableBean.event2TriggerMap.put("02", Action_02_pkuuh4);
        Action_02_pkuuh4.setAction("info", true);
        Action_02_pkuuh4.setAdditions("tbDataPlan");
        Action_0_pkuuh4.addChild(Action_02_pkuuh4);

        EventBean Action_04_pkuuh4 = new EventBean("Action_04", "录音", "4");
        GlobalVariableBean.event2TriggerMap.put("04", Action_04_pkuuh4);
        Action_04_pkuuh4.setAction("record", true);
        Action_04_pkuuh4.setAdditions("");
        Action_0_pkuuh4.addChild(Action_04_pkuuh4);

        EventBean Action_05_pkuuh4 = new EventBean("Action_05", "人工服务", "5");
        GlobalVariableBean.event2TriggerMap.put("05", Action_05_pkuuh4);
        Action_05_pkuuh4.setAction("manual", true);
        Action_05_pkuuh4.setAdditions("");
        Action_0_pkuuh4.addChild(Action_05_pkuuh4);

        EventBean Action_09_pkuuh4 = new EventBean("Action_09", "重听", "9");
        GlobalVariableBean.event2TriggerMap.put("09", Action_09_pkuuh4);
        Action_09_pkuuh4.setAction("replay", false);
        Action_09_pkuuh4.setAdditions("");
        Action_0_pkuuh4.addChild(Action_09_pkuuh4);

        EventBean Action_00_pkuuh4 = new EventBean("Action_00", "投诉", "0");
        GlobalVariableBean.event2TriggerMap.put("00", Action_00_pkuuh4);
        Action_00_pkuuh4.setAction("menu", false);
        Action_00_pkuuh4.setAdditions("");
        Action_0_pkuuh4.addChild(Action_00_pkuuh4);

        EventBean Action_009_pkuuh4 = new EventBean("Action_009", "返回上级菜单", "9");
        GlobalVariableBean.event2TriggerMap.put("009", Action_009_pkuuh4);
        Action_009_pkuuh4.setAction("back", false);
        Action_009_pkuuh4.setAdditions("");
        Action_00_pkuuh4.addChild(Action_009_pkuuh4);

        EventBean Action_000_pkuuh4 = new EventBean("Action_000", "投诉", "0");
        GlobalVariableBean.event2TriggerMap.put("000", Action_000_pkuuh4);
        Action_000_pkuuh4.setAction("menu", false);
        Action_000_pkuuh4.setAdditions("");
        Action_00_pkuuh4.addChild(Action_000_pkuuh4);

        EventBean Action_0009_pkuuh4 = new EventBean("Action_0009", "返回上级菜单", "9");
        GlobalVariableBean.event2TriggerMap.put("0009", Action_0009_pkuuh4);
        Action_0009_pkuuh4.setAction("back", false);
        Action_0009_pkuuh4.setAdditions("");
        Action_000_pkuuh4.addChild(Action_0009_pkuuh4);

        EventBean Action_0000_pkuuh4 = new EventBean("Action_0000", "结束通话", "0");
        GlobalVariableBean.event2TriggerMap.put("0000", Action_0000_pkuuh4);
        Action_0000_pkuuh4.setAction("hangup", true);
        Action_0000_pkuuh4.setAdditions("");
        Action_000_pkuuh4.addChild(Action_0000_pkuuh4);

        EventBean Action_00a_pkuuh4 = new EventBean("Action_00a", "测试星号按键", "*");
        GlobalVariableBean.event2TriggerMap.put("00*", Action_00a_pkuuh4);
        Action_00a_pkuuh4.setAction("call", true);
        Action_00a_pkuuh4.setAdditions("");
        Action_00_pkuuh4.addChild(Action_00a_pkuuh4);

        EventBean Action_00b_pkuuh4 = new EventBean("Action_00b", "测试井号按键", "#");
        GlobalVariableBean.event2TriggerMap.put("00#", Action_00b_pkuuh4);
        Action_00b_pkuuh4.setAction("hangup", true);
        Action_00b_pkuuh4.setAdditions("");
        Action_00_pkuuh4.addChild(Action_00b_pkuuh4);

    }

    public static void main(String[] args) {
        initHashMap();
        instance = new VoiceMenu();
        VoiceMenu.initHashMap();
        Init.initView(instance);
        EventLogic.runInitSetup();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        boolean isValidVariable = !(((GlobalVariableBean.curTriggerPath == null || GlobalVariableBean.curTriggerPath.length() == 0) || GlobalVariableBean.hasFinished || !GlobalVariableBean.hasStarted));
        if (isValidVariable) {
            try {
                try {
                    if (VoiceOutputUtils.waitingThr.isAlive()) {
                        VoiceOutputUtils.waitingThr.interrupt();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                EventLogic.runLogic(event, queryCase);
            } catch (IOException | InterruptedException | DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    QueryCaseImpl queryCase = () -> "";

}

