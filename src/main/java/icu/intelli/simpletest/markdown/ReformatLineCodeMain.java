package icu.intelli.simpletest.markdown;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * 将一行中的```xxx```替换为`xxx`
 *
 * @author wangshuo
 * @date 2021/06/18
 */
public class ReformatLineCodeMain {

    public static final File OLD_DIR = new File("/home/wangshuo/Temporary/_posts");

    public static final File NEW_DIR = new File("/home/wangshuo/Temporary/_posts_new");

    public static final String PATTERN = "(```)(.*)?(```)";

    public static void main(String[] args) throws InterruptedException {
        String line = "```nihao``````world```";
        line = "nihao```w```s```n```nihao";
        while (line.matches(PATTERN)) {
            line = line.replace("```", "`");
            System.out.println(line);
            TimeUnit.SECONDS.sleep(1);
        }
//        doDegrade(OLD_DIR);
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

                }
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
