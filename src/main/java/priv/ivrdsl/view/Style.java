package priv.ivrdsl.view;

import priv.ivrdsl.model.EventBean;
import priv.ivrdsl.model.GlobalVariableBean;
import priv.ivrdsl.util.VoiceOutputUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * JFrame UI 配置。
 *
 * @author Guanidine Beryllium
 */
public class Style {
    public static JTextArea screen = new JTextArea("", 3, 8);
    public static JFrame frame = new JFrame("JetPhone");

    public static JPanel phone = new JPanel(new BorderLayout(5, 0));
    public static JPanel panelOfButtons = new JPanel(new GridLayout(4, 3, 2, 2));

    private static void customizeButton(JButton button, Border border, Dimension dim, Color color) {
        button.setBackground(color);
        button.setOpaque(true);
        button.setPreferredSize(dim);
        button.setBorder(border);
    }

    private static void readChildren() throws IOException {
        EventBean tmp = GlobalVariableBean.event2TriggerMap.get("0");
        GlobalVariableBean.voiceOutput.addText(tmp.getAdditions());
        for (EventBean child : tmp.getChilds()) {
            GlobalVariableBean.voiceOutput.addText(", " + child.getName() + " 请按 " + child.getTrigger());
            GlobalVariableBean.possibleOptionList.add(child.getTrigger());
        }
        GlobalVariableBean.voiceOutput.speak();
    }

    private static void addListeners(JButton call, JButton end) {
        call.addActionListener(p0 -> {
            if (!GlobalVariableBean.hasStarted) {
                GlobalVariableBean.hasStarted = true;
                try {
                    setTextToScreen("Home");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                GlobalVariableBean.curTriggerPath = "0";
                try {
                    readChildren();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        end.addActionListener(p0 -> {
            VoiceOutputUtils.waitingThr.interrupt();
            System.out.println("Terminating program");
            System.exit(0);
        });
    }

    private static void setCallButtons(JPanel panel) {
        Border border = new LineBorder(Color.white, 22);
        Border emptyBorder = new LineBorder(Color.white, 10);
        Dimension dim = new Dimension(78, 50);
        JButton buttonCall = new JButton("");
        JButton buttonVoice = new JButton("HOME");
        JButton buttonEnd = new JButton("");
        customizeButton(buttonCall, border, dim, Color.GREEN);
        customizeButton(buttonVoice, emptyBorder, dim, Color.lightGray);
        customizeButton(buttonEnd, border, dim, Color.RED);
        addListeners(buttonCall, buttonEnd);
        panel.add(buttonCall, BorderLayout.LINE_START);
        panel.add(buttonVoice, BorderLayout.CENTER);
        panel.add(buttonEnd, BorderLayout.LINE_END);
    }

    private static void setDeliminatorLine(JPanel panel) {
        JPanel topLine = new JPanel();
        topLine.setBackground(Color.lightGray);
        topLine.setPreferredSize(new Dimension(80, 4));
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        JPanel line2 = new JPanel(new GridLayout(1, 3, 0, 0));
        setCallButtons(line2);
        line2.setBackground(Color.white);
        JPanel bottomLine = new JPanel();
        bottomLine.setBackground(Color.lightGray);
        bottomLine.setPreferredSize(new Dimension(80, 1));
        panel.add(topLine, BorderLayout.NORTH);
        panel.add(line2, BorderLayout.CENTER);
        panel.add(bottomLine, BorderLayout.SOUTH);
    }

    /**
     * 设置输出到 UI 界面的文字。
     *
     * @param text 待输出字符串
     * @throws UnsupportedEncodingException 编码格式不支持
     */
    public static void setTextToScreen(String text) throws UnsupportedEncodingException {
        if (text != null && text.length() > 0) {
            screen.setText(text);
        }
    }

    private static void setScreen(JTextArea myScreen) {
        myScreen.setFont(new Font("Arial", Font.PLAIN, 16));
        myScreen.setMargin(new Insets(5, 5, 5, 5));
    }

    private static void setTopPanel(JPanel topPanel) {
        JPanel myHeader = new JPanel(new BorderLayout(0, 0));
        setScreen(screen);
        topPanel.add(myHeader, BorderLayout.NORTH);
        topPanel.add(screen, BorderLayout.SOUTH);
    }

    private static void setMainPanel(JPanel myPhone) {
        JPanel topPanel = new JPanel(new BorderLayout(0, 0));
        JPanel myLine = new JPanel(new BorderLayout(0, 0));
        setTopPanel(topPanel);
        setDeliminatorLine(myLine);
        myPhone.add(topPanel, BorderLayout.NORTH);
        myPhone.add(myLine, BorderLayout.CENTER);
    }

    /**
     * JFrame UI 框架配置。
     */
    public static void setFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        frame.setSize(width / 2, height / 2);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(phone);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /**
     * JFrame UI 内容分配置。
     */
    public static void setContent() {
        setMainPanel(phone);
    }
}
