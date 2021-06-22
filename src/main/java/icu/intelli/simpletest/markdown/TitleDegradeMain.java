package icu.intelli.simpletest.markdown;

import java.io.*;

/**
 * Markdown文档标题降级
 *
 * @author wangshuo
 * @date 2021/06/18
 */
public class TitleDegradeMain {

    public static final File OLD_DIR = new File("/home/wangshuo/Temporary/_posts");

    public static final File NEW_DIR = new File("/home/wangshuo/Temporary/_posts_new");

    public static void main(String[] args) {
        doDegrade(OLD_DIR);
    }

    public static void doDegrade(File file) {
        if (file.isDirectory()) {
            File newDir = new File(NEW_DIR, file.getAbsolutePath().replace(OLD_DIR.getAbsolutePath(), ""));
            newDir.mkdirs();
            File[] files = file.listFiles();
            for (File f : files) {
                doDegrade(f);
            }
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(file));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(new File(NEW_DIR, file.getAbsolutePath().replace(OLD_DIR.getAbsolutePath(), ""))))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.matches("^# .*$")) {
                        // 一级标题
                        continue;
                    } else if (line.matches("^#{2,6} .*$")) {
                        // 其他级别标题
                        line = line.substring(1);
                    }
                    bw.write(line.concat("\n"));
                }
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
