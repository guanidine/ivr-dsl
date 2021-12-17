package priv.ivrdsl;

import priv.ivrdsl.services.IvrParser;

import java.io.File;
import java.io.IOException;

/**
 * IVR 解析器项目入口。
 *
 * @author Guanidine Beryllium
 */
public class Application {
    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            IvrParser.parse();
        } else {
            IvrParser.parse(new File(args[0]));
        }
    }
}
