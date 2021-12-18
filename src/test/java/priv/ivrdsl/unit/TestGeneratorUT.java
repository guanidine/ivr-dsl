package priv.ivrdsl.unit;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.internal.collections.Pair;
import priv.ivrdsl.model.IvrMap;
import priv.ivrdsl.service.IvrParser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static priv.ivrdsl.service.CommandParser.commandParser;

@Test(groups = "ut")
public class TestGeneratorUT {
    IvrMap schema = IvrMap.getInstance();

    @DataProvider
    public Object[][] commandDataProvider() {
        return new Object[][]{
                {new Pair<>("begin", "00#"), "00#$"},
                {new Pair<>("end", "00#"), "00"},
                {new Pair<>("end", "0"), ""}
        };
    }

    @DataProvider
    public Object[][] commandParserDataProvider() {
        return new Object[][]{
                {new Pair<>("init -playback=欢迎致电申国移动 -title=China-Mobile", "0"), "0$"},
                {new Pair<>("add 1 -event=转接服务 -action=call -additions=分号123", "0$"), "01"},
                {new Pair<>("add 2 -event=信息业务 -action=info -additions=tbDataPlan", "01"), "02"},
                {new Pair<>("add 3 -event=删除事件测试 -action=hangup", "02"), "03"},
                {new Pair<>("remove 03", "03"), "0$"},
                {new Pair<>("add 0 -event=事件覆盖测试 -action=hangup", "0$"), "00"},
                {new Pair<>("add 0 -event=投诉 -action=menu", "00"), "00"},
                {new Pair<>("add * -event=测试星号按键 -action=call", "00$"), "00*"},
                {new Pair<>("add 0 -event=投诉 -action=menu", "00$"), "000"},
                {new Pair<>("add 0 -event=结束通话 -action=hangup", "000$"), "0000"},
                {new Pair<>("add 5 -event=删除目录测试 -action=menu", "0000"), "0005"},
                {new Pair<>("add 1 -event=事件1 -action=back", "0005$"), "00051"},
                {new Pair<>("add 9 -event=返回上级菜单 -action=back", "0005"), "0009"},
                {new Pair<>("add # -event=测试井号按键 -action=hangup", "000"), "00#"},
                {new Pair<>("add 9 -event=返回上级菜单 -action=back", "00#"), "009"},
                {new Pair<>("remove 0005", "009"), "009"},
                {new Pair<>("status", "009"), "009"}
        };
    }

    @Test(dataProvider = "commandDataProvider", priority = 1)
    public void testParseCommand(final Pair<String, String> input, final String result) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Constructor<IvrParser> c = IvrParser.class.getDeclaredConstructor();
        c.setAccessible(true);
        IvrParser ivrParser = c.newInstance();
        Method m = IvrParser.class.getDeclaredMethod("parseCommand", String.class, String.class);
        m.setAccessible(true);
        String ans = (String) m.invoke(ivrParser, input.first(), input.second());
        Assert.assertEquals(ans, result);
    }

    @Test(dataProvider = "commandParserDataProvider", priority = 2)
    public void testCommandParser(final Pair<String, String> input, final String result) throws IOException {
        String ans = commandParser(input.first().split(" "), input.second(), schema);
        Assert.assertEquals(ans, result);
    }

    @Test(priority = 3)
    public void testIvrMap() {
        String expected = """
                0 --> [China-Mobile, , 欢迎致电申国移动]
                	01 --> [转接服务, call, 分号123]
                	02 --> [信息业务, info, tbDataPlan]
                	00 --> [投诉, menu, ]
                		009 --> [返回上级菜单, back, ]
                		000 --> [投诉, menu, ]
                			0009 --> [返回上级菜单, back, ]
                			0000 --> [结束通话, hangup, ]
                		00* --> [测试星号按键, call, ]
                		00# --> [测试井号按键, hangup, ]
                """;
        Assert.assertEquals(schema.toString(), expected);
    }

    @Test(priority = 10)
    public void testExport() throws IOException {
        String[] args = {"export", "--debug", "src/test/resources/unit"};
        commandParser(args, "00", schema);
        File file = new File("src/test/resources/unit/VoiceMenu.java");
        Assert.assertTrue(file.exists());
        List<String> allLines = Files.readAllLines(Path.of("src/test/resources/unit/VoiceMenu.java"));
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("Action_[0-9|ab]+_[0-9a-z]{6}");
        for (String line : allLines) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String eventUtilName = matcher.group();
                int idx = line.indexOf(eventUtilName);
                int len = eventUtilName.length();
                line = line.substring(0, idx + len - 7) + line.substring(idx + len);
            }
            result.add(line);
        }
        List<String> expected = Files.readAllLines(Path.of("src/test/resources/unit/VoiceMenu"));
        Assert.assertEquals(result, expected);
    }
}
