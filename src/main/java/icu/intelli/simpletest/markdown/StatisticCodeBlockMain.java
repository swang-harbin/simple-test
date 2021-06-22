package icu.intelli.simpletest.markdown;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 */
public class StatisticCodeBlockMain {

    public static final File DIR = new File("/home/wangshuo/Projects/hexo.swang-harbin.github.io/source/_posts");

    public static void main(String[] args) {
        doStatistic(DIR);
    }

    private static void doStatistic(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                doStatistic(f);
            }
        } else {
            boolean has = false;
            int count = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.matches(".*```.*")) {
                        count++;
                    }
                    if (count == 1) {
                        continue;
                    }
                    count = 0;
                    if (line.matches(".*==.*==.*")) {
                        has = true;
                        System.out.println(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(has){
                System.out.println(file.getAbsolutePath());
                System.out.println("************************************************************************************************");
            }
        }
    }
}
