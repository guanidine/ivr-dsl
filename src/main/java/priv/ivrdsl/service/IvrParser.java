package priv.ivrdsl.service;

import priv.ivrdsl.exception.SyntaxErrorException;
import priv.ivrdsl.model.IvrMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static priv.ivrdsl.util.StringProcessUtils.removeLastChar;
import static priv.ivrdsl.util.StringProcessUtils.splitString;

/**
 * 解析 IVR 脚本语言文件。
 *
 * @author Guanidine Beryllium
 * @see IvrMap
 */
public class IvrParser {
    private static volatile IvrParser IVRParser;
    /** 解析结果输出 */
    private final IvrMap schema;
    /** init 仅允许出现一次，且必须在开头 */
    private static boolean hasInit = false;

    private IvrParser() {
        schema = IvrMap.getInstance();
    }

    /**
     * 单例模式创建一个解析器。
     *
     * @return 创建的解析器对象
     */
    private static IvrParser getInstance() {
        if (IVRParser == null) {
            synchronized (IvrParser.class) {
                if (IVRParser == null) {
                    IVRParser = new IvrParser();
                }
            }
        }
        return IVRParser;
    }

    /**
     * 从文件中解析 IVR 脚本语言。
     *
     * @param file IVR 脚本
     * @throws IOException 读取配置文件失败
     */
    public static void parse(File file) throws IOException {
        IVRParser = getInstance();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String path = "0";
        String line;
        while ((line = br.readLine()) != null) {
            path = IVRParser.parseLine(line, path);
        }
    }

    /**
     * 从命令行输入解析 IVR 脚本语言。
     *
     * @throws IOException 读取配置文件失败
     */
    public static void parse() throws IOException {
        IVRParser = getInstance();
        Scanner scanner = new Scanner(System.in);
        String path = "0";
        String line;
        while ((line = scanner.nextLine()) != null) {
            path = IVRParser.parseLine(line, path);
        }
    }

    /**
     * 解析单个命令。
     *
     * @param command 命令字符串，即处理后的单行输入字符串
     * @param path    当前 trigger 路径
     * @return 处理后的 trigger 路径
     * @throws IOException 读取配置文件失败
     */
    private String parseCommand(String command, String path) throws IOException {
        return switch (command) {
            case "begin" -> path + "$";
            case "end" -> removeLastChar(path);
            default -> CommandParser.commandParser(splitString(command), path, IVRParser.schema);
        };
    }

    /**
     * 解析单行输入。
     *
     * @param line 用户输入
     * @param path 当前 trigger 路径
     * @return 处理后的 trigger 路径
     * @throws IOException 读取配置文件失败
     */
    private String parseLine(String line, String path) throws IOException {
        String delim = " ";
        boolean isInit = !"".equals(line) && "init".equalsIgnoreCase(line.split(delim)[0]);
        boolean isHelp = !"".equals(line) && "help".equalsIgnoreCase(line.split(delim)[0]);
        if (!hasInit) {
            if (!isInit && !isHelp) {
                throw new SyntaxErrorException("ERROR: Expect Command \"init\" first (got \"" + line + "\")");
            } else {
                path = IVRParser.parseCommand(line.trim(), path);
                hasInit = true;
            }
        } else {
            if (isInit) {
                throw new SyntaxErrorException(
                        "ERROR: Cannot use Command \"init\" except the beginning (got \"" + line + "\")");
            } else {
                path = IVRParser.parseCommand(line.trim(), path);
            }
        }
        return path;
    }
}
