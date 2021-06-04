package icu.intelli.simpletest.hexo.main;

import java.io.*;
import java.util.regex.Pattern;

/**
 * 替换文件中的字符
 *
 * @author wangshuo
 * @date 2021/06/03
 */
public class ReplaceMain {

    private static final String OLD_DIR_PATH = "C:\\Users\\33722\\Desktop\\_posts";
    private static final String NEW_DIR_PATH = "C:\\Users\\33722\\Desktop\\_posts_new";

    private static final String REGEX = "(.*)(cc\\.ccue|zone\\.wwwww)(.*)";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static void main(String[] args) {
        File[] files = new File(OLD_DIR_PATH).listFiles();
        for (File file : files) {
            replace(file);
        }
    }

    private static void replace(File file) {
        boolean flag = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter(new File(NEW_DIR_PATH, file.getName())))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.matches(REGEX)) {
                    flag = true;
                }
            }
            if (flag) {
                System.out.println(file.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException(file.getAbsolutePath(), e);
        }
    }
}
