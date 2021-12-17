package priv.ivrdsl.models;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
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
    /**
     * 命令 {@code add}：添加一个事件。
     */
    @Parameters(separators = "=", commandDescription = "Add new event")
    public static class CommandAdd {
        @Parameter(description = "Button to trigger this event", required = true,
                validateWith = Validation.ValidTrigger.class)
        public
        String button;
        @Parameter(names = {"-event"}, description = "Name of the event", required = true)
        public
        String event;
        @Parameter(names = {"-action"}, description = "Action to execute during this event", required = true,
                validateWith = Validation.ValidAction.class)
        public
        String action;
        @Parameter(names = "-additions", description = "Addition information for some of the action", variableArity = true)
        public
        List<String> additions = new ArrayList<>();
    }

    /**
     * 命令 {@code config}：项目配置。
     */
    @Parameters(separators = "=",
            commandDescription = "Set Baidu APPID/AK/SK and SQL tables from where to get customer information")
    public static class CommandConfig {
        @Parameter(names = {"-appid"}, description = "Set Baidu API app_id")
        public
        String appId = null;
        @Parameter(names = {"-apikey"}, description = "Set Baidu API api_key")
        public
        String apiKey = null;
        @Parameter(names = {"-secretkey"}, description = "Set Baidu API secret_key")
        public
        String secretKey = null;
        @Parameter(names = {"-name", "-table"},
                description = "Set name of the relational database table to be added/changed")
        public
        String table = null;
        @Parameter(names = {"-driver"}, description = "Set jdbc driver of the relational database")
        public
        String driver = null;
        @Parameter(names = {"-url"}, description = "Set url of the relational database")
        public
        String url = null;
        @Parameter(names = {"-user"}, description = "Set user name to access the relational database")
        public
        String user = null;
        @Parameter(names = {"-passwd", "-password"}, description = "Set password to access the relational database")
        public
        String passwd = null;
        @Parameter(names = {"-help", "-h", "?"}, description = "Display help information about ApiProps", help = true)
        boolean help = false;
        //TODO: help

        public final String apiConfig = "src/main/resources/apikey.properties";
        public final String jdbcConfig = "src/main/resources/jdbc.xml";
    }

    /**
     * 命令 {@code export}：导出 IVR 脚本。
     */
    @Parameters(separators = "=", commandDescription = "Export generated java source code along with its dependencies")
    public static class CommandExport {
        @Parameter(names = "-path", description = "Export file location")
        public
        String exportPath = "";
    }

    /**
     * 命令 {@code init}：初始化一个 IVR 脚本，并设置脚本标题。
     */
    @Parameters(separators = "=", commandDescription = "Init an IVR program")
    public static class CommandInit {
        @Parameter(names = {"-title"}, description = "Title of the IVR program")
        public
        String title = "Voice Menu";
        @Parameter(names = {"-playback"}, description = "Welcome playback content when put through")
        public
        String playback = "欢迎致电";
    }

    /**
     * 命令 {@code remove}：删除一个事件。
     */
    @Parameters(separators = "=", commandDescription = "Remove event via trigger path")
    public static class CommandRemove {
        @Parameter(description = "Trigger path from root as \"0\"", required = true)
        public
        String path;
    }

    /**
     * 命令 {@code status}：查看当前的 IVR 事件树。
     */
    @Parameters(commandDescription = "Show relation schema of the events")
    public static class CommandStatus {
    }

    /**
     * 命令 {@code test}：将脚本导出到项目中进行测试。
     */
    @Parameters(commandDescription = "Generate java code inside the program for test")
    public static class CommandTest {
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
