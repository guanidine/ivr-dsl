package priv.ivrdsl.services;

import com.beust.jcommander.JCommander;
import org.apache.commons.io.FileUtils;
import priv.ivrdsl.models.IvrMap;
import priv.ivrdsl.models.Commands;
import priv.ivrdsl.exceptions.ParametersException;
import priv.ivrdsl.exceptions.SyntaxErrorException;

import java.io.File;
import java.io.IOException;

import static priv.ivrdsl.beans.EnumBean.Command;
import static priv.ivrdsl.managers.ApiSetter.setApi;
import static priv.ivrdsl.managers.JdbcSetter.setJdbc;
import static priv.ivrdsl.utils.StringProcessUtils.removeLastChar;

/**
 * 分析单个用户命令。
 *
 * @author Guanidine Beryllium
 * @see Commands
 */
public class CommandParser {
    Commands.CommandConfig config;
    Commands.CommandInit init;
    Commands.CommandAdd add;
    Commands.CommandRemove remove;
    Commands.CommandStatus status;
    Commands.CommandExport export;
    Commands.CommandTest test;

    private CommandParser() {
        config = new Commands.CommandConfig();
        init = new Commands.CommandInit();
        add = new Commands.CommandAdd();
        remove = new Commands.CommandRemove();
        status = new Commands.CommandStatus();
        export = new Commands.CommandExport();
        test = new Commands.CommandTest();
    }

    /**
     * 解析单个命令，并将结果记录在 {@code schema} 中。
     *
     * @param args   单个命令
     * @param path   当前逻辑路径
     * @param schema trigger-event 映射
     * @return 处理完命令后的逻辑路径
     * @throws IOException 配置文件读取失败
     * @see priv.ivrdsl.services.IvrParser
     * @see IvrMap
     */
    public static String commandParser(String[] args, String path, IvrMap schema) throws IOException {
        CommandParser cm = new CommandParser();
        JCommander jc =
                JCommander.newBuilder()
                        .addObject(cm)
                        .addCommand("config", cm.config)
                        .addCommand("init", cm.init)
                        .addCommand("add", cm.add)
                        .addCommand("remove", cm.remove)
                        .addCommand("status", cm.status)
                        .addCommand("export", cm.export)
                        .addCommand("test", cm.test)
                        .build();
        jc.parse(args);
        Command command = Command.getByCode(jc.getParsedCommand());
        if (command == null) {
            return null;
        }
        switch (command) {
            case COMMAND_CONFIG -> {
                if (cm.config.appId != null || cm.config.apiKey != null || cm.config.secretKey != null) {
                    setApi(cm.config.appId, cm.config.apiKey, cm.config.secretKey, cm.config.apiConfig);
                }
                if (cm.config.table != null || cm.config.driver != null || cm.config.url != null || cm.config.user != null || cm.config.passwd != null) {
                    String result = setJdbc(cm.config.table, cm.config.driver, cm.config.url, cm.config.user, cm.config.passwd, cm.config.jdbcConfig);
                    if (result != null) {
                        throw new ParametersException("ERROR: In command \"config\", parameters \"table\", " +
                                "\"driver\", \"url\", \"user\" and \"passwd\" " + "are expected to appear in group, " +
                                "missing " + result);
                    }
                }
            }
            case COMMAND_INIT -> {
                schema.put(path, cm.init.title, "", cm.init.playback);
                path += "$";
            }
            case COMMAND_ADD -> {
                if (path.length() < 3 && "back".equalsIgnoreCase(cm.add.action)) {
                    throw new SyntaxErrorException("ERROR: Cannot execute \"back\" action in the main menu " +
                            "(add -event=" + cm.add.event + " -action=back)");
                }
                path = removeLastChar(path) + cm.add.button;
                schema.put(path, cm.add.event, cm.add.action, String.join(" ", cm.add.additions));
            }
            case COMMAND_REMOVE -> {
                schema.remove(cm.remove.path);
                if (path.startsWith(cm.remove.path)) {
                    path = removeLastChar(cm.remove.path) + "$";
                }
            }
            case COMMAND_STATUS -> System.out.println(schema.toString());
            case COMMAND_EXPORT -> {
                String url = (cm.export.exportPath.length() == 0 || cm.export.exportPath.endsWith("/") || cm.export.exportPath.endsWith("\\")) ?
                        cm.export.exportPath + "VoiceMenu" : cm.export.exportPath + "/VoiceMenu";
                File dir = new File(url);
                if (!dir.exists()) {
                    if (!dir.mkdir()) {
                        throw new IOException();
                    }
                }
                FileUtils.copyFile(new File("target/ivrdsl-1.0-SNAPSHOT-jar-with-dependencies.jar"),
                        new File(url + "/ivrdsl-1.0-SNAPSHOT-jar-with-dependencies.jar"));
                Generator gen = new Generator(url, schema);
                gen.generateIvr();
            }
            case COMMAND_TEST -> {
                String filepath = "src/main/java/priv/ivrdsl/";
                String packagePath = "package priv.ivrdsl";
                Generator gen = new Generator(filepath, schema, packagePath);
                gen.generateIvr();
            }
        }
        return path;
    }
}
