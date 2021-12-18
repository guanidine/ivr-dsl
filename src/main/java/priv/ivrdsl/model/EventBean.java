package priv.ivrdsl.model;

import lombok.Getter;
import priv.ivrdsl.model.EnumBean.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件节点。记录事件本身的名称、触发方式、触发动作、从属事件集等信息。
 *
 * @author Guanidine Beryllium
 */
@Getter
public class EventBean {
    /** 事件名 */
    private final String name;
    /** 触发事件的按键 */
    private final String trigger;
    /** 事件的子事件节点 */
    private final List<EventBean> childs;
    /** 补充信息 */
    private final String additions;
    /** 事件触发动作 */
    private Action action;
    /** 事件完成后是否结束程序 */
    private boolean isFinal;

    /**
     * 构造一个标准的事件。面向中文用户，因此在构造同时可为事件设定一个中文名称。
     *
     * @param name    事件名（面向程序，符合 Java 命名规范的名称）
     * @param cnName  在语音中播放使用的事件名（可以为中文、符号等）
     * @param trigger 触发事件的按键（当前菜单下触发事件的单个按键字符）
     */
    public EventBean(String name, String cnName, String trigger, String additions) {
        this.name = name;
        this.trigger = trigger;
        this.action = null;
        this.childs = new ArrayList<>();
        this.isFinal = false;
        this.additions = additions;
        GlobalVariableBean.event2VoiceTextMap.put(name, cnName);
    }

    /**
     * 设置事件触发的动作。
     *
     * @param action  事件动作
     * @param isFinal 动作完成后是否结束整个 IVR 服务
     */
    public void setAction(String action, boolean isFinal) {
        this.action = Action.getByCode(action);
        this.isFinal = isFinal;
    }

    /**
     * 添加事件从属的子事件。
     *
     * @param child 子事件节点
     */
    public void addChild(EventBean child) {
        childs.add(child);
    }
}
