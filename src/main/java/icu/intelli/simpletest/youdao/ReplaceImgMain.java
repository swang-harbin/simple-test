package icu.intelli.simpletest.youdao;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 递归将文件夹下的所有youdao云笔记的图片路径通过picgo进行上传后, 进行替换
 *
 * @author wangshuo
 * @date 2021/06/04
 */
public class ReplaceImgMain {


    private static final String REGEX = "(.*?!\\[.*?]\\()(https://note\\.youdao\\.com.*?)(\\).*)";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    private static final File OLD_FOLDER = new File("C:\\Users\\wangshuo\\Desktop\\notes");
    private static final File NEW_FOLDER = new File("C:\\Users\\wangshuo\\Desktop\\notes_new");

    private static final WebDriver WEB_DRIVER;

    static {
        // 开启浏览器, 进行登录
        String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.setProperty("webdriver.chrome.driver",
                classPath.concat("selenium/chromedriver_91_win32.exe"));
        WEB_DRIVER = new ChromeDriver();
        WEB_DRIVER.manage().window().maximize();
        WEB_DRIVER.get("https://note.youdao.com/web/");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String next = scanner.next();
        System.out.println(next);

        recursion(OLD_FOLDER);
        WEB_DRIVER.close();
    }


    private static void recursion(File file) {
        if (file.isFile()) {
            doReplaceImage(file, new File(NEW_FOLDER, file.getAbsolutePath().replace(OLD_FOLDER.getAbsolutePath(), "")));
        } else {
            // 如果是目录, 在新文件夹里创建相同的目录
            new File(NEW_FOLDER, file.getAbsolutePath().replace(OLD_FOLDER.getAbsolutePath(), "")).mkdirs();
            File[] files = file.listFiles();
            for (File f : files) {
                recursion(f);
            }
        }
    }

    private static void doReplaceImage(File oldFile, File newFile) {
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(oldFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(newFile))) {
            while ((line = br.readLine()) != null) {
                Matcher matcher = PATTERN.matcher(line);
                if (matcher.matches()) {
                    String youDaoUrl = matcher.group(2);
                    WEB_DRIVER.get(youDaoUrl);
                    WebElement element = WEB_DRIVER.findElement(By.cssSelector("html > body > img"));
                    File screenshot = element.getScreenshotAs(OutputType.FILE);
                    PicGoUploadRequest request = new PicGoUploadRequest();
                    request.setList(Collections.singletonList(screenshot.getAbsolutePath()));
                    RestTemplate restTemplate = new RestTemplate();
                    PicGoUploadResponse response = restTemplate.postForObject("http://127.0.0.1:36677/upload", request, PicGoUploadResponse.class);
                    if (Objects.nonNull(response) && Objects.equals(response.getSuccess(), Boolean.TRUE)) {
                        String newUrl = response.getResult().get(0);
                        System.out.println(line);
                        line = line.replaceAll(REGEX, "$1" + newUrl + "$3");
                        System.out.println(line);
                    }
                }
                bw.write(line.concat("\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("出现异常, 文件路径: ".concat(oldFile.getAbsolutePath()).concat(", 行: ").concat(line));
        }
    }


}
