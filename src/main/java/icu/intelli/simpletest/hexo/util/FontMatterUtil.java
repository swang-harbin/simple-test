package icu.intelli.simpletest.hexo.util;

import java.io.*;

/**
 * @author wangshuo
 * @date 2021/06/22
 */
public class FontMatterUtil {

    public static String readYamlStr(File file) {
        int count = 0;
        StringBuffer sb = new StringBuffer();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("---")) {
                    count++;
                    continue;
                }
                if (count == 2) {
                    break;
                }
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(file.getAbsolutePath(), e);
        }
        return sb.toString();
    }

    public static void replaceFontMatter(File oldFile, File newFile, String newFontMatter) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(oldFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(newFile))) {
            bw.write(newFontMatter);
            String line;
            while ((line = br.readLine()) != null) {
                if (count == 2) {
                    bw.write(line.concat("\n"));
                }
                if (line.startsWith("---")) {
                    count++;
                }
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
