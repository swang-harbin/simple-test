package icu.intelli.simpletest.youdao;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author wangshuo
 * @date 2021/06/07
 */
public class SeleniumMain {

    public static void main(String[] args) {
        String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.setProperty("webdriver.chrome.driver",
                classPath.concat("selenium/chromedriver_91_win32.exe"));
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.baidu.com");
        driver.get("https://github-pages.王硕.我爱你");
        driver.get("https://www.baidu.com");
        driver.close();
        driver.close();
        driver.close();
        driver.close();
    }
}
