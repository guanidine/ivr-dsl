package priv.ivrdsl.unit;

import com.beust.jcommander.ParameterException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import priv.ivrdsl.exception.MissingPropsException;
import priv.ivrdsl.exception.SyntaxErrorException;
import priv.ivrdsl.service.IvrParser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static priv.ivrdsl.service.CommandParser.commandParser;
import static priv.ivrdsl.util.SqlQueryUtils.query;

@Test(groups = "ut", priority = 1)
public class TestExceptionUT {
    @BeforeClass
    public void beforeClass() {
        System.out.println("-------------------Test Exceptions");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("-------------------Test Exceptions");
    }

    @Test(expectedExceptions = MissingPropsException.class)
    public void testDatabasePropsException() {
        System.out.println("Test DatabasePropsException");
        query("tbTest", null, null);
    }

    @Test(expectedExceptions = ParameterException.class)
    public void testParameterException1() throws IOException {
        System.out.println("Test ParameterException 1");
        String[] args = {"config", "-driver=org.postgresql.Driver", "-user=user86", "-passwd=user86@bupt"};
        commandParser(args, "0#", null);
    }

    @Test(expectedExceptions = ParameterException.class)
    public void testParameterException2() throws IOException {
        System.out.println("Test ParameterException 2");
        String[] args = {"add", "1", "-event=测试", "-action=error action"};
        commandParser(args, "0#", null);
    }

    @Test(expectedExceptions = ParameterException.class)
    public void testParameterException3() throws IOException {
        System.out.println("Test ParameterException 3");
        String[] args = {"add", "!", "-event=测试", "-action=HANGUP"};
        commandParser(args, "0#", null);
    }

    @Test(expectedExceptions = SyntaxErrorException.class)
    public void testSyntaxException1() throws IOException {
        System.out.println("Test SyntaxException 1");
        String[] args = {"add", "0", "-event=测试", "-action=back"};
        commandParser(args, "0$", null);
    }

    @Test(expectedExceptions = {SyntaxErrorException.class, InvocationTargetException.class})
    public void testSyntaxException2() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        System.out.println("Test SyntaxException 2");
        Constructor<IvrParser> c = IvrParser.class.getDeclaredConstructor();
        c.setAccessible(true);
        IvrParser ivrParser = c.newInstance();
        Field f = ivrParser.getClass().getDeclaredField("hasInit");
        f.setAccessible(true);
        f.set(ivrParser, false);
        Method m = IvrParser.class.getDeclaredMethod("parseLine", String.class, String.class);
        m.setAccessible(true);
        m.invoke(ivrParser, "    begin", "0#1");
    }

    @Test(expectedExceptions = {SyntaxErrorException.class, InvocationTargetException.class})
    public void testSyntaxException3() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        System.out.println("Test SyntaxException 3");
        Constructor<IvrParser> c = IvrParser.class.getDeclaredConstructor();
        c.setAccessible(true);
        IvrParser ivrParser = c.newInstance();
        Field f = ivrParser.getClass().getDeclaredField("hasInit");
        f.setAccessible(true);
        f.set(ivrParser, true);
        Method m = IvrParser.class.getDeclaredMethod("parseLine", String.class, String.class);
        m.setAccessible(true);
        m.invoke(ivrParser, "init", "0#1");
    }
}
