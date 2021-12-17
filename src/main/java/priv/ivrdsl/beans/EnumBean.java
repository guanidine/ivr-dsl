package priv.ivrdsl.beans;

/**
 * 枚举类。用于检测用户输入合法性，以及 {@code switch-case} 语句中。
 *
 * @author Guanidine Beryllium
 */
public class EnumBean {
    /**
     * 可选按键。
     */
    public enum Button {
        /** 电话按键1 */
        BUTTON_1("1"),
        /** 电话按键2 */
        BUTTON_2("2"),
        /** 电话按键3 */
        BUTTON_3("3"),
        /** 电话按键4 */
        BUTTON_4("4"),
        /** 电话按键5 */
        BUTTON_5("5"),
        /** 电话按键6 */
        BUTTON_6("6"),
        /** 电话按键7 */
        BUTTON_7("7"),
        /** 电话按键8 */
        BUTTON_8("8"),
        /** 电话按键9 */
        BUTTON_9("9"),
        /** 电话按键* */
        BUTTON_STAR("*"),
        /** 电话按键0 */
        BUTTON_0("0"),
        /** 电话按键# */
        BUTTON_HASH("#");

        /** 按键对应字符串值。 */
        private final String code;

        Button(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name();
        }

        /**
         * 查找按键 {@code code} 对应的枚举值。
         * <p>
         * 如果找不到对应枚举值则返回 {@code null}。
         *
         * @param code 按键值
         * @return 对应枚举值，不存在则返回 {@code null}
         */
        public static Button getByCode(String code) {
            for (Button value : values()) {
                if (value.getCode().equals(code)) {
                    return value;
                }
            }
            return null;
        }
    }

    /**
     * 可选动作。
     */
    public enum Action {
        /** 退回上级菜单 */
        ACTION_BACK("back"),
        /** 转接服务 */
        ACTION_CALL("call"),
        /** 客户信息服务 */
        ACTION_INFO("info"),
        /** 结束通话 */
        ACTION_HANGUP("hangup"),
        /** 人工服务 */
        ACTION_MANUAL("manual"),
        /** 目录 */
        ACTION_MENU("menu"),
        /** 录音 */
        ACTION_RECORD("record"),
        /** 重听自助服务菜单 */
        ACTION_REPLAY("replay");

        /** 动作对应字符串值。 */
        private final String code;

        Action(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        /**
         * 查找按键 {@code code} 对应的枚举值，不区分大小写。
         * <p>
         * 如果找不到对应枚举值则返回 {@code null}。
         *
         * @param code 按键值
         * @return 对应枚举值，不存在则返回 {@code null}
         */
        public static Action getByCode(String code) {
            for (Action value : values()) {
                if (value.getCode().equalsIgnoreCase(code)) {
                    return value;
                }
            }
            return null;
        }
    }

    /**
     * 可选命令。
     */
    public enum Command {
        /** 脚本配置 */
        COMMAND_CONFIG("config"),
        /** 初始化一个 IVR 项目 */
        COMMAND_INIT("init"),
        /** 添加事件 */
        COMMAND_ADD("add"),
        /** 删除事件 */
        COMMAND_REMOVE("remove"),
        /** 展示关系图 */
        COMMAND_STATUS("status"),
        /** 导出 */
        COMMAND_EXPORT("export"),
        /** 测试 */
        COMMAND_TEST("test"),
        /** 帮助 */
        COMMAND_HELP("help");

        /** 命令对应字符串值。 */
        private final String code;

        Command(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        /**
         * 查找命令 {@code code} 对应的枚举值，不区分大小写。
         * <p>
         * 如果找不到对应枚举值则返回 {@code null}。
         *
         * @param code 命令
         * @return 对应枚举值，不存在则返回 {@code null}
         */
        public static Command getByCode(String code) {
            for (Command value : values()) {
                if (value.getCode().equalsIgnoreCase(code)) {
                    return value;
                }
            }
            return null;
        }
    }
}
