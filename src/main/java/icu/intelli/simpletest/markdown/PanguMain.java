package icu.intelli.simpletest.markdown;

import ws.vinta.pangu.Pangu;

import java.io.File;
import java.io.IOException;

/**
 * 在中文和英文之间加空格
 *
 * @author wangshuo
 * @date 2021/06/24
 */
public class PanguMain {

    public static final File OLD_DIR = new File("/home/wangshuo/Projects/blog/content/posts");

    public static final File NEW_DIR = new File("/home/wangshuo/Temporary/posts_new");

    public static void main(String[] args) {
        doPangu(OLD_DIR);
    }

    public static void doPangu(File file) {
        File newFile = new File(NEW_DIR, file.getAbsolutePath().replace(OLD_DIR.getAbsolutePath(), ""));
        if (file.isDirectory()) {
            newFile.mkdirs();
            File[] files = file.listFiles();
            for (File f : files) {
                doPangu(f);
            }
        } else {
            try {
                new Pangu().spacingFile(file, newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
