package icu.intelli.simpletest.hexo.main;

import icu.intelli.simpletest.hexo.entity.HugoFrontMatter;
import icu.intelli.simpletest.hexo.util.FontMatterUtil;
import org.springframework.util.FileCopyUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 根据categories创建目录并将文件移动到对应的categories中
 *
 * @author wangshuo
 * @date 2021/06/22
 */
public class CategoriesToDirMain {

    private static final String OLD_PATH = "/home/wangshuo/Projects/blog/content/posts";
    private static final String NEW_PATH = "/home/wangshuo/Temporary/posts_new";

    public static void main(String[] args) {
        File oldDir = new File(OLD_PATH);
        File[] files = oldDir.listFiles();
        for (File file : files) {
            String fontMatterStr = FontMatterUtil.readYamlStr(file);
            HugoFrontMatter hugoFrontMatter = new Yaml().loadAs(fontMatterStr, HugoFrontMatter.class);
            List<String> categories = hugoFrontMatter.categories;
            String subDir = "";
            for (String category : categories) {
                subDir = subDir.concat(category).concat("/");
            }
            File newDir = new File(NEW_PATH, subDir);
            newDir.mkdirs();
            try {
                FileCopyUtils.copy(file, new File(newDir, file.getName()));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(file.getAbsolutePath());
            }
        }
    }


}
