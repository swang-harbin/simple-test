package icu.intelli.simpletest.hexo.main;

import icu.intelli.simpletest.hexo.entity.HugoFrontMatter;
import icu.intelli.simpletest.hexo.util.FontMatterUtil;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wangshuo
 * @date 2021/06/22
 */
public class DeleteCategoriesMain {

    private static final String OLD_DIR_PATH = "/home/wangshuo/Temporary/posts";

    private static final String NEW_DIR_PATH = "/home/wangshuo/Temporary/posts_new";

    public static void main(String[] args) {
        doDelete(new File(OLD_DIR_PATH));
    }

    private static void doDelete(File file) {
        if (file.isDirectory()) {
            new File(NEW_DIR_PATH, file.getAbsolutePath().replace(OLD_DIR_PATH, "")).mkdirs();
            File[] files = file.listFiles();
            for (File f : files) {
                doDelete(f);
            }
        } else {
            String fontMatterStr = FontMatterUtil.readYamlStr(file);
            HugoFrontMatter hugoFrontMatter = new Yaml().loadAs(fontMatterStr, HugoFrontMatter.class);
            Map<String, Object> newFrontMatter = new LinkedHashMap<>();
            newFrontMatter.put("title", hugoFrontMatter.title);
            newFrontMatter.put("date", hugoFrontMatter.date);
            newFrontMatter.put("tags", hugoFrontMatter.tags);

            FontMatterUtil.replaceFontMatter(file,
                    new File(NEW_DIR_PATH, file.getAbsolutePath().replace(OLD_DIR_PATH, "")),
                    new Yaml().dumpAs(newFrontMatter, new Tag(Tag.PREFIX.concat("---")), DumperOptions.FlowStyle.BLOCK).substring(2).concat("---\n")
            );

        }
    }

}
