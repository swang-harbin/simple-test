package icu.intelli.simpletest.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author wangshuo
 * @date 2021/06/29
 */
public class CompareLineNumberMain {

    private static final String POST_PATH = "/home/wangshuo/Projects/blog/content/posts";

    private static final String BACK_PATH = "/home/wangshuo/Documents/posts-20210622";

    public static void main(String[] args) {
        File[] backFiles = new File(BACK_PATH).listFiles();
        Map<String, Integer> backFileMap = new HashMap<>(backFiles.length);
        for (File backFile : backFiles) {
            backFileMap.put(backFile.getName(), getFileLineNum(backFile));
        }

        List<File> files = new ArrayList<>();
        toFileArray(new File(POST_PATH), files);
        files.forEach(
                f -> {
                    Integer oldLineNum = backFileMap.get(f.getName());
                    if (Objects.isNull(oldLineNum)) {
                        System.out.println(f.getAbsolutePath() + "：不存在同名文件！");
                        return;
                    }
                    int abs = Math.abs(oldLineNum - getFileLineNum(f));
                    if (abs > 8) {
                        System.out.println(f.getAbsolutePath() + "：" + abs);
                    }
                }
        );
    }


    private static void toFileArray(File file, List<File> fileList) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                toFileArray(f, fileList);
            }
        } else {
            fileList.add(file);
        }
    }

    private static int getFileLineNum(File file) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file));) {
            while (br.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

}
