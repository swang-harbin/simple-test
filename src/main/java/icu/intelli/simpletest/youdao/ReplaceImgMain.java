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
 * @author wangshuo
 * @date 2021/06/04
 */
public class ReplaceImgMain {


    private static final String REGEX = "(.*?!\\[.*?]\\()(https://note\\.youdao\\.com.*?)(\\).*)";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static final String OLD_PATH = "C:\\Users\\33722\\Desktop\\_posts";
    public static final String NEW_PATH = "C:\\Users\\33722\\Desktop\\_posts_new";

    public static void main(String[] args) {
        // 开启浏览器, 进行登录
        String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.setProperty("webdriver.chrome.driver",
                classPath.concat("selenium/chromedriver_91_win32.exe"));
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://note.youdao.com/web/");
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        // 读取图片路径
        File[] files = new File(OLD_PATH).listFiles();
        for (File file : files) {
            File newFile = new File(NEW_PATH, file.getName());
            String line = "";
            try (BufferedReader br = new BufferedReader(new FileReader(file));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(newFile))) {
                while ((line = br.readLine()) != null) {
                    Matcher matcher = PATTERN.matcher(line);
                    if (matcher.matches()) {
                        String youDaoUrl = matcher.group(2);
                        driver.get(youDaoUrl);
                        WebElement element = driver.findElement(By.cssSelector("html > body > img"));
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
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("上传图片过程出现异常, 文件路径: " + file.getAbsolutePath() + ", 内容: " + line);
            }
        }
        driver.close();
    }
}
