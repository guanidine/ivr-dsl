package priv.ivrdsl.global;

import org.testng.Assert;
import org.testng.annotations.Test;
import priv.ivrdsl.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCompiler {
    @Test
    public void testFileExport() throws IOException {
        Application.main(new String[]{"src/test/resources/global/test.ivr"});
        File file = new File("src/main/java/priv/ivrdsl/VoiceMenu.java");
        Assert.assertTrue(file.exists());
        List<String> allLines = Files.readAllLines(Path.of("src/main/java/priv/ivrdsl/VoiceMenu.java"));
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
        List<String> expected = Files.readAllLines(Path.of("src/test/resources/global/test"));
        Assert.assertEquals(result, expected);
    }
}
