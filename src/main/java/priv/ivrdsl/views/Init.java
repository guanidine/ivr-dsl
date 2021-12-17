package priv.ivrdsl.views;

import priv.ivrdsl.beans.EnumBean;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 创建 IVR 图形界面。
 *
 * @author Guanidine Beryllium
 */
public class Init {
    /**
     * 初始化 IVR 图形界面。
     *
     * @param instance 事件监听器
     */
    public static void initView(ActionListener instance) {
        Style.setContent();
        addButtons(Style.panelOfButtons, instance);
        Style.phone.add(Style.panelOfButtons, BorderLayout.SOUTH);
        Style.setFrame();
    }

    /**
     * 为图形界面添加按键。
     *
     * @param myPanelOfButtons 按键
     * @param instance         触发动作
     */
    public static void addButtons(JPanel myPanelOfButtons, ActionListener instance) {
        for (EnumBean.Button item : EnumBean.Button.values()) {
            JButton button = new JButton(item.getCode());
            button.setActionCommand(item.getCode());
            button.addActionListener(instance);
            button.setPreferredSize(new Dimension(78, 76));
            Border border = new LineBorder(Color.white, 13);
            button.setBorder(border);
            myPanelOfButtons.add(button);
        }
    }


}
