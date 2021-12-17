package priv.ivrdsl.services;

import org.dom4j.DocumentException;
import priv.ivrdsl.models.IvrMap;
import priv.ivrdsl.exceptions.SyntaxErrorException;

import java.io.*;
import java.util.Scanner;

import static priv.ivrdsl.utils.StringProcessUtils.removeLastChar;
import static priv.ivrdsl.utils.StringProcessUtils.splitString;

/**
 * 解析 IVR 脚本语言文件。
 *
 * @author Guanidine Beryllium
 * @see IvrMap
 */
public class IvrParser {
    private static volatile IvrParser IVRParser;
    private final IvrMap schema;
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
        String initCommand = "init";
        String delim = " ";
        if (!hasInit) {
            if ("".equals(line) || !initCommand.equalsIgnoreCase(line.split(delim)[0])) {
                throw new SyntaxErrorException("ERROR: Expect Command \"init\" first (got \"" + line + "\")");
            } else {
                path = IVRParser.parseCommand(line.trim(), path);
                hasInit = true;
            }
        } else {
            if ("".equals(line) || !initCommand.equalsIgnoreCase(line.split(delim)[0])) {
                path = IVRParser.parseCommand(line.trim(), path);
            } else {
                throw new SyntaxErrorException(
                        "ERROR: Cannot use Command \"init\" except the beginning (got \"" + line + "\")");
            }
        }
        return path;
    }
}
