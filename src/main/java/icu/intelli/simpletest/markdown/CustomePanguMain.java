package icu.intelli.simpletest.markdown;

import ws.vinta.pangu.Pangu;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangshuo
 * @date 2021/06/25
 */
public class CustomePanguMain {


    /**
     * CJK后跟着英文字母或数字，一定要在之间加空格
     */
    public static final Pattern CJK_ANS = Pattern.compile(
            "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])" +
                    "([a-z0-9])",
            Pattern.CASE_INSENSITIVE
    );
    /**
     * 英文字母或数字后跟着CJK，一定要在之间加空格
     */
    public static final Pattern ANS_CJK = Pattern.compile(
            "([a-z0-9])" +
                    "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])",
            Pattern.CASE_INSENSITIVE
    );
    /**
     * 中文之间使用了英文符号，换成中文的，如果包含空格，把空格也要去掉
     */
    public static final Pattern CJK_ANS_CJK = Pattern.compile(
            "(.+[\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])" +
                    "\\s*" +
                    "([\\,\\.\\:\\;])" +
                    "\\s*" +
                    "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}].+)"
    );

    /**
     * 英文括号之间只有中文，把括号换成中文的
     */
    public static final Pattern QUOTE_CJK_QUOTE = Pattern.compile(
            "\\(" +
                    "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}]+)" +
                    "\\)",
            Pattern.CASE_INSENSITIVE);


    /**
     * 中文符号前有空格的，去掉空格
     */
    public static final Pattern SPACE_CJK = Pattern.compile(
            "\\s+" +
                    "([，。：；（）“”‘’【】、《》])"
    );
    /**
     * 中文符号前后空格的，去掉空格
     */
    public static final Pattern CJK_SPACE = Pattern.compile(
            "([，。：；（）“”‘’【】、《》])" +
                    "\\s+"
    );
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
                spacingFile(file, newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void spacingFile(File inputFile, File outputFile) throws IOException {

        FileReader fr = new FileReader(inputFile);
        BufferedReader br = new BufferedReader(fr);

        outputFile.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(outputFile, false);
        BufferedWriter bw = new BufferedWriter(fw);

        try {
            // readLine() do not contain newline char
            String line = br.readLine();

            while (line != null) {
                line = spacingText(line);

                // TODO: keep file's raw newline char from difference OS platform
                bw.write(line);
                bw.newLine();

                line = br.readLine();
            }
        } finally {
            br.close();

            // 避免 writer 沒有實際操作就 close()，產生一個空檔案
            if (bw != null) {
                bw.close();
            }
        }
    }


    private static String spacingText(String text) {
        // 中文后跟着英文字母或数字，中间加空格
        Matcher caMatcher = CJK_ANS.matcher(text);
        text = caMatcher.replaceAll("$1 $2");
        // 英文字母或者数字后面跟着中文，中间加空格
        Matcher acMatcher = ANS_CJK.matcher(text);
        text = acMatcher.replaceAll("$1 $2");

        // 中文之间使用了英文符号，换成中文的，如果包含空格，把空格也要去掉
        Matcher cacMatcher = CJK_ANS_CJK.matcher(text);
        while (cacMatcher.matches()) {
            String symbol = cacMatcher.group(2);
            symbol = replaceToChinaSymbol(symbol);
            text = cacMatcher.replaceFirst("$1" + symbol + "$3");
            cacMatcher = CJK_ANS_CJK.matcher(text);
        }
        // 中文符号前后有空格的，去掉空格
        Matcher scMatcher = SPACE_CJK.matcher(text);
        text = scMatcher.replaceAll("$1");
        Matcher csMatcher = CJK_SPACE.matcher(text);
        text = csMatcher.replaceAll("$1");

        return text;
    }

    private static String replaceToChinaSymbol(String enSymbol) {
        switch (enSymbol) {
            case ",":
                return "，";
            case ".":
                return "。";
            case ":":
                return "：";
            case ";":
                return "；";
            default:
                return enSymbol;
        }
    }
}
