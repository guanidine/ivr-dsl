package priv.ivrdsl.beans;

import lombok.Data;
import priv.ivrdsl.beans.EnumBean.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件节点。记录事件本身的名称、触发方式、触发动作、从属事件集等信息。
 *
 * @author Guanidine Beryllium
 * @see priv.ivrdsl.utils.ConstructorUtil#constructEvents
 */
@Data
public class EventBean {
    private String name;
    private String trigger;
    private List<EventBean> childs;
    private Action action;
    private String additions;
    private boolean isFinal;

    public EventBean() {
        this.isFinal = false;
    }

    /**
     * 构造一个标准的事件。面向中文用户，因此在构造同时可为事件设定一个中文名称。
     *
     * @param name    事件名
     * @param cnName  在语音中播放使用的事件名
     * @param trigger 触发事件的按键
     */
    public EventBean(String name, String cnName, String trigger) {
        this.name = name;
        this.trigger = trigger;
        this.action = null;
        this.childs = new ArrayList<>();
        this.isFinal = false;
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
     * 设置事件触发的动作。
     *
     * @param action 事件动作
     */
    public void setAction(String action) {
        this.action = Action.getByCode(action);
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
