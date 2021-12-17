package priv.ivrdsl.models;

import com.beust.jcommander.*;
import priv.ivrdsl.beans.EnumBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置 JCommander 各命令可接受的参数。
 *
 * @author Guanidine Beryllium
 * @see com.beust.jcommander.JCommander
 * @see Parameter
 * @see Parameters
 */
public class Commands {
    @Parameters(separators = "=", commandDescription = "添加一个事件")
    public static class CommandAdd {
        @Parameter(description = "触发该事件的按键", required = true,
                validateWith = Validation.ValidTrigger.class)
        public String button;
        @Parameter(names = {"-event"}, description = "事件名称", required = true)
        public String event;
        @Parameter(names = {"-action"}, description = "事件触发时执行的动作", required = true,
                validateWith = Validation.ValidAction.class)
        public String action;
        @Parameter(names = "-additions", description = "部分动作所需的额外信息", variableArity = true)
        public List<String> additions = new ArrayList<>();
        @Parameter(names = {"-help", "-h", "?"}, description = "显示帮助", help = true, hidden = true)
        public boolean help = false;
    }

    @Parameters(separators = "=", commandDescription = "配置 API 和 JDBC")
    public static class CommandConfig {
        @Parameter(names = {"-appid"}, description = "语音合成 appid")
        public String appId = null;
        @Parameter(names = {"-apikey"}, description = "语音合成 apikey")
        public String apiKey = null;
        @Parameter(names = {"-secretkey"}, description = "语音合成 secretkey")
        public String secretKey = null;
        @Parameter(names = {"-name", "-table"}, description = "数据库连接 待查关系表名")
        public String table = null;
        @Parameter(names = {"-driver"}, description = "数据库驱动")
        public String driver = null;
        @Parameter(names = {"-url"}, description = "数据库 url")
        public String url = null;
        @Parameter(names = {"-user"}, description = "访问数据库的用户名")
        public String user = null;
        @Parameter(names = {"-passwd", "-password"}, description = "访问数据库的密码")
        public String passwd = null;
        @Parameter(names = {"-help", "-h", "?"}, description = "显示帮助", help = true, hidden = true)
        public boolean help = false;

        public final String apiConfig = "src/main/resources/apikey.properties";
        public final String jdbcConfig = "src/main/resources/jdbc.xml";
    }

    @Parameters(separators = "=", commandDescription = "导出 IVR 项目代码及依赖")
    public static class CommandExport {
        @Parameter(names = "-path", description = "项目导出路径")
        public String exportPath = "";
        @Parameter(names = {"-help", "-h", "?"}, description = "显示帮助", help = true, hidden = true)
        public boolean help = false;
    }

    @Parameters(separators = "=", commandDescription = "初始化一个 IVR 项目")
    public static class CommandInit {
        @Parameter(names = {"-title"}, description = "IVR 程序的标题")
        public String title = "Voice Menu";
        @Parameter(names = {"-playback"}, description = "IVR 程序的欢迎语音")
        public String playback = "欢迎致电" + title;
        @Parameter(names = {"-help", "-h", "?"}, description = "显示帮助", help = true, hidden = true)
        public boolean help = false;
    }

    @Parameters(separators = "=", commandDescription = "删除一个事件")
    public static class CommandRemove {
        @Parameter(description = "事件逻辑路径", required = true)
        public String path;
        @Parameter(names = {"-help", "-h", "?"}, description = "显示帮助", help = true, hidden = true)
        public boolean help = false;
    }

    @Parameters(commandDescription = "查看当前 IVR 程序的逻辑树")
    public static class CommandStatus {
        @Parameter(names = {"-help", "-h", "?"}, description = "显示帮助", help = true, hidden = true)
        public boolean help = false;
    }

    @Parameters(commandDescription = "在项目内部生成测试用的 IVR 代码")
    public static class CommandTest {
        @Parameter(names = {"-help", "-h", "?"}, description = "显示帮助", help = true, hidden = true)
        public boolean help = false;
    }

    @Parameters(commandDescription = "显示帮助")
    public static class CommandHelp {
        @Parameter(names = {"-help", "-h", "?"}, description = "显示帮助", help = true, hidden = true)
        public boolean help = false;
    }

    /**
     * 参数合法性检验。
     */
    public static class Validation {
        /**
         * {@code add} 命令的参数 {@code -action} 需要在枚举 {@code Action} 可接受的范围内。
         */
        static public class ValidAction implements IParameterValidator {
            @Override
            public void validate(String name, String value) throws ParameterException {
                EnumBean.Action action = EnumBean.Action.getByCode(value);
                if (action == null) {
                    throw new ParameterException("Action should be among \"Back\", \"Call\", \"Info\", " +
                            "\"Hangup\", \"Menu\", \"Record\" and \"Replay\" (found " + value + ")");
                }
            }
        }

        /**
         * {@code add} 命令的参数 {@code -trigger} 需要在枚举 {@code Button} 可接受的范围内。
         */
        static public class ValidTrigger implements IParameterValidator {
            @Override
            public void validate(String name, String value) throws ParameterException {
                EnumBean.Button button = EnumBean.Button.getByCode(value);
                if (button == null) {
                    throw new ParameterException("Trigger Button should be among \"0\", \"1\", \"2\", \"3\", \"4\", " +
                            "\"5\", \"6\", \"7\", \"8\", \"9\", \"*\", \"#\", (found " + value + ")");
                }
            }
        }
    }
}
