package priv.ivrdsl.controller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import priv.ivrdsl.model.JdbcBean;
import priv.ivrdsl.util.SpeechSynthesisUtils;
import priv.ivrdsl.util.SqlQueryUtils;

import java.io.*;
import java.util.List;

/**
 * 对 jdbc 配置文件的增删改查操作。
 *
 * @author Guanidine Beryllium
 * @see JdbcBean
 */
public class XmlOperator {

    /**
     * 在配置文件中增加一个 jdbc 配置项。
     *
     * @param jdbc     jdbc 配置项
     * @param filePath 配置文件路径
     */
    public static void add(JdbcBean jdbc, String filePath) {
        try {
            Document document = getDocument(new FileInputStream(filePath));
            Element rootNode = document.getRootElement();
            Element node = rootNode.addElement("jdbc");
            node.addAttribute("name", jdbc.getName());
            node.addElement("driver").setText(jdbc.getDriver());
            node.addElement("url").setText(jdbc.getUrl());
            node.addElement("user").setText(jdbc.getUser());
            node.addElement("passwd").setText(jdbc.getPasswd());
            write2Xml(document, filePath);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 在配置文件中查找一个 jdbc 配置项。
     *
     * @param name 关系表名，也是配置文件中 jdbc 标签的属性值。
     * @param is   配置文件输入流
     */
    public static JdbcBean find(String name, InputStream is) {
        try {
            Document document = getDocument(is);
            Element e = (Element) document.selectSingleNode("//jdbc[@name=\"%s\"]".formatted(name));
            if (e != null) {
                return new JdbcBean(
                        e.attributeValue("name"),
                        e.element("driver").getText(),
                        e.element("url").getText(),
                        e.element("user").getText(),
                        e.element("passwd").getText()
                );
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 在配置文件中查找一个 jdbc 配置项。通过反射访问编译后的配置文件，运行 IVR 程序时使用。
     *
     * @param name 关系表名，也是配置文件中 jdbc 标签的属性值。
     * @see SqlQueryUtils#query
     */
    public static JdbcBean find(String name) {
        return find(name, SpeechSynthesisUtils.class.getClassLoader().getResourceAsStream("jdbc.xml"));
    }

    /**
     * 在配置文件中查找一个 jdbc 配置项。从 resources 文件夹访问配置文件，编译 IVR 脚本程序时使用。
     *
     * @param name     关系表名，也是配置文件中 jdbc 标签的属性值。
     * @param filePath 配置文件路径
     * @see JdbcSetter#setJdbc
     */
    public static JdbcBean find(String name, String filePath) throws IOException {
        if (hasFile(filePath)) {
            return find(name, new FileInputStream(filePath));
        }
        return null;
    }

    /**
     * 从配置文件中删除一个 jdbc 配置项。
     *
     * @param name     关系表名，也是配置文件中 jdbc 标签的属性值。
     * @param filePath 配置文件路径
     */
    public static void delete(String name, String filePath) {
        try {
            Document document = getDocument(new FileInputStream(filePath));
            Element root = document.getRootElement();
            List<Element> list = root.elements();
            for (Element e : list) {
                if (e.attributeValue("name").equals(name)) {
                    e.detach();
                }
            }
            write2Xml(document, filePath);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 更改配置文件中的 jdbc 配置项。
     *
     * @param jdbc     jdbc 配置项
     * @param filePath 配置文件路径
     */
    public static void alter(JdbcBean jdbc, String filePath) {
        delete(jdbc.getName(), filePath);
        add(jdbc, filePath);
    }

    /**
     * 将内存中的内容写入 xml 配置文件。
     *
     * @param document dom4j 的 xml 操作对象
     * @param filePath 配置文件路径
     * @throws IOException 写入配置文件失败
     * @see Document
     */
    private static void write2Xml(Document document, String filePath) throws IOException {
        if (hasFile(filePath)) {
            OutputFormat formatter = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileOutputStream(filePath), formatter);
            writer.write(document);
            writer.close();
        }
    }

    /**
     * 获得操作 xml 的 {@code Document} 对象
     *
     * @param is 输入流
     * @return dom4j 的 xml 操作对象
     * @throws DocumentException 读取配置文件失败
     */
    private static Document getDocument(InputStream is) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(is);
    }

    /**
     * 判断文件是否存在。若文件不存在，则会创建一个只有根元素的 xml 配置文件。
     *
     * @param filePath 文件路径
     * @return 文件是否创建成功
     * @throws IOException 读写配置文件失败
     */
    private static boolean hasFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            if (file.createNewFile()) {
                String init = """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <properties>
                        </properties>
                        """;
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
                    bufferedWriter.write(init);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new FileNotFoundException();
            }
        }
        return true;
    }
}