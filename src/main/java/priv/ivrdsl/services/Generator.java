package priv.ivrdsl.services;

import org.apache.commons.lang3.RandomStringUtils;
import priv.ivrdsl.beans.EnumBean;
import priv.ivrdsl.models.IvrMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static priv.ivrdsl.utils.StringProcessUtils.getLastChar;
import static priv.ivrdsl.utils.StringProcessUtils.removeLastChar;

/**
 * Java 生成器。将编译后的 IVR 脚本解析成 Java 程序。
 *
 * @author Guanidine Beryllium
 */
public class Generator {
    private int idx;
    private final String filename;
    private final StringBuilder ivrBuilder;
    private final String eventRandomName;
    private final IvrMap schema;

    /**
     * 构造一个 Java 生成器。指定包路径 {@code packagePath} 可以使得生成的程序在项目中运行。
     *
     * @param filepath    Java 文件保存路径（不包括文件名，因为生成的文件名是固定的）
     * @param schema      IVR trigger-event映射
     * @param packagePath 项目包路径
     * @see IvrMap
     */
    public Generator(String filepath, IvrMap schema, String packagePath) {
        String STR = "abcdefghijklmnopqrstuvwxyz0123456789";
        this.eventRandomName = "_" + RandomStringUtils.random(6, STR);
        this.filename =  filepath + "/VoiceMenu.java";
        this.ivrBuilder = new StringBuilder();
        this.schema = schema;

        packagePath = "".equals(packagePath) ? "" : packagePath + ";\n\n";
        String begin = """
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
                """;
        String end = """
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
                                
                    QueryCaseImpl queryCase = () -> {
                        // TODO: Override this method before executing!
                    };
                
                }

                """;
        ivrBuilder.append(packagePath).append(begin);
        idx = ivrBuilder.length();
        ivrBuilder.append(end);
    }

    /**
     * 构造一个 Java 生成器。
     *
     * @param filepath Java 文件保存路径（不包括文件名，因为生成的文件名是固定的）
     * @param schema   IVR trigger-event映射
     * @see IvrMap
     */
    public Generator(String filepath, IvrMap schema) {
        this(filepath, schema, "");
    }

    /**
     * 将生成器的生成结果输出。
     */
    public void generateIvr() {
        Set<Map.Entry<String, List<String>>> entrySet = schema.getMap().entrySet();
        List<Map.Entry<String, List<String>>> list = new ArrayList<>(entrySet);
        list.sort(schema);
        for (Map.Entry<String, List<String>> item : list) {
            String path = item.getKey();
            String event = item.getValue().get(0);
            String action = item.getValue().get(1);
            String additions = item.getValue().get(2);
            if ("0".equals(path)) {
                generateTitle(event, additions);
            } else {
                generateEvent(path, event, action, additions);
            }
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename))) {
            bufferedWriter.write(ivrBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成标题部分代码。
     *
     * @param title     语音播放使用的标题名
     * @param additions 语音播放中的欢迎语音
     */
    private void generateTitle(String title, String additions) {
        String homeUtilName = "Action_0" + eventRandomName;
        String init = String.format("""
                        EventBean %s = new EventBean("Home", "%s", "0");
                        %s.setAction("", true);
                                
                        %s.setAdditions("%s");
                                
                        GlobalVariableBean.event2TriggerMap.put("0", %s);
                        
                """, homeUtilName, title, homeUtilName, homeUtilName, additions, homeUtilName);
        ivrBuilder.insert(idx, init);
        idx += init.length();
    }

    /**
     * 生成事件构造代码。
     *
     * @param trigger   触发此处事件的按键路径。根节点路径为“0”，所以所有事件路径都需要加上根节点的“0”，如先后按键“9”“#”触发的事件，其路径为“09#”
     * @param event     语音播放使用的事件名
     * @param action    当前事件触发的动作
     * @param additions 部分动作执行时需要额外的参数，具体请参考 {@link EventLogic#runLogic}
     */
    private void generateEvent(String trigger, String event, String action, String additions) {
        String trigger0 = trigger.replaceAll("\\*", "a");
        trigger0 = trigger0.replaceAll("#", "b");
        String eventName = "Action_" + trigger0;
        String eventUtilName = eventName + eventRandomName;
        String parentUtilName = "Action_" + removeLastChar(trigger0) + eventRandomName;
        String isFinal = "menu".equalsIgnoreCase(action) ? "false" :
                switch (Objects.requireNonNull(EnumBean.Action.getByCode(action))) {
                    case ACTION_CALL, ACTION_INFO, ACTION_HANGUP, ACTION_MANUAL, ACTION_RECORD -> "true";
                    case ACTION_REPLAY, ACTION_BACK, ACTION_MENU -> "false";
                };
        String init = String.format("""
                                EventBean %s = new EventBean("%s", "%s", "%s");
                                GlobalVariableBean.event2TriggerMap.put("%s", %s);
                                %s.setAction("%s", %s);
                                %s.setAdditions("%s");
                                %s.addChild(%s);
                                                
                        """, eventUtilName, eventName, event, getLastChar(trigger), trigger, eventUtilName,
                eventUtilName, action, isFinal, eventUtilName, additions, parentUtilName, eventUtilName);

        ivrBuilder.insert(idx, init);
        idx += init.length();
    }
}
