package priv.ivrdsl.unit;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import priv.ivrdsl.impl.QueryCaseImpl;
import priv.ivrdsl.model.GlobalVariableBean;
import priv.ivrdsl.util.StringProcessUtils;
import priv.ivrdsl.util.VoiceOutputUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import static priv.ivrdsl.controller.DataAccessor.data2String;

@Test(groups = "ut", priority = 10)
public class TestUtilsUT {
    @DataProvider
    public Object[][] removeCharDataProvider() {
        return new Object[][]{
                {"1234567", "123456"}, {"1234567890987654321", "123456789098765432"},
                {"12", "1"}, {"1", ""}, {"", null}, {null, null}
        };
    }

    @DataProvider
    public Object[][] getCharDataProvider() {
        return new Object[][]{
                {"1234567", "7"}, {"1234567890987654321", "1"},
                {"12", "2"}, {"1", "1"}, {"", null}, {null, null}
        };
    }

    @DataProvider
    public Object[][] splitStringDataProvider() {
        return new Object[][]{
                {"123 45 6 789", new String[]{"123", "45", "6", "789"}},
                {"123 \"45 6\" 789", new String[]{"123", "45 6", "789"}},
                {"add # \"带 空 格 的 事 件\" hangup",
                        new String[]{"add", "#", "带 空 格 的 事 件", "hangup"}},
                {"add # \"event=带 空 格 的 事 件\" action=hangup",
                        new String[]{"add", "#", "event=带 空 格 的 事 件", "action=hangup"}},
                {"add # event=\"带 空 格 的 事 件\" action=hangup",
                        new String[]{"add", "#", "event=\"带", "空", "格", "的", "事", "件\"", "action=hangup"}}
        };
    }

    @DataProvider
    public Object[][] voiceOutputProcessDataProvider() {
        return new Object[][]{
                {",,欢迎致电申国移动, ,", "欢迎致电申国移动"},
                {",,欢迎致电申国移动, Action_2 请按1,", "欢迎致电申国移动,事件2请按1"},
                {",,欢迎致电申国移动,,, Action_3 请按0 , ,", "欢迎致电申国移动,事件3请按0"},
                {",,欢迎致电申国移动,, Action_1 请按#, ,, Action_4 请按* ,", "欢迎致电申国移动,事件1请按井号键,事件4请按星号键"},
                {",", ""}
        };
    }

    @DataProvider
    public Object[][] sqlQueryDataProvider() {
        return new Object[][]{
                {new SqlQuery("tbDataPlan", () -> ""),
                        "尊敬的客户18012345678您好您的流量套餐为套餐一流量余额为1.05GB话费余额为20元7角套餐到期时间为2022年12月31日"},
                {new SqlQuery("tbDataPlan", () -> "where mobile = '18987654321'"),
                        "尊敬的客户18987654321您好您的流量套餐为套餐二流量余额为不限量话费余额为0元套餐到期时间为2021年12月31日"},
                {new SqlQuery("tbDataPlan", () -> "where mobile = '8987654321'"),
                        "抱歉没有查到您的信息"}
        };
    }

    @Test(dataProvider = "removeCharDataProvider")
    public void testRemoveLastChar(final String text, final String result) {
        String ans = StringProcessUtils.removeLastChar(text);
        Assert.assertEquals(ans, result);
    }

    @Test(dataProvider = "getCharDataProvider")
    public void testGetLastChar(final String text, final String result) {
        String ans = StringProcessUtils.getLastChar(text);
        Assert.assertEquals(ans, result);
    }

    @Test(dataProvider = "splitStringDataProvider")
    public void testSplitString(final String text, final String[] result) {
        String[] ans = StringProcessUtils.splitString(text);
        Assert.assertEquals(ans, result);
    }

    @Test(dataProvider = "voiceOutputProcessDataProvider")
    public void testVoiceOutput(final String text, final String result) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        GlobalVariableBean.event2VoiceTextMap = new HashMap<>() {{
            put("Action_1", "事件1");
            put("Action_2", "事件2");
            put("Action_3", "事件3");
            put("Action_4", "事件4");
        }};
        VoiceOutputUtils output = new VoiceOutputUtils();
        output.addText(text);
        Method m = VoiceOutputUtils.class.getDeclaredMethod("processText");
        m.setAccessible(true);
        String ans = (String) m.invoke(output);
        Assert.assertEquals(ans, result);
    }

    @Test(dataProvider = "sqlQueryDataProvider")
    public void testSqlQuery(final SqlQuery query, final String result) {
        String ans = data2String(query.table, query.queryCase);
        ans = ans.replaceAll(",", "").replaceAll(" ", "");
        Assert.assertEquals(ans, result);
    }

    private static class SqlQuery {
        String table;
        QueryCaseImpl queryCase;

        public SqlQuery(String table, QueryCaseImpl queryCase) {
            this.table = table;
            this.queryCase = queryCase;
        }
    }
}
