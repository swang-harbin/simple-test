package icu.intelli.simpletest.hexo.main;

import icu.intelli.simpletest.hexo.entity.HexoFrontMatter;
import icu.intelli.simpletest.hexo.util.FontMatterUtil;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author wangshuo
 * @date 2021/06/22
 */
public class HexoToHugoMain {

    private static final String OLD_DIR_PATH = "/home/wangshuo/Projects/blog/content/posts";

    private static final String NEW_DIR_PATH = "/home/wangshuo/Projects/blog/content/posts_new";

    public static void main(String[] args) {
        doReplace(new File(OLD_DIR_PATH));
    }

    private static void doReplace(File file) {
        if (file.isDirectory()) {
            new File(NEW_DIR_PATH, file.getAbsolutePath().replace(OLD_DIR_PATH, "")).mkdirs();
            File[] files = file.listFiles();
            for (File f : files) {
                doReplace(f);
            }
        } else {
            FontMatterUtil.replaceFontMatter(file,
                    new File(NEW_DIR_PATH, file.getName()),
                    toHugo(file));
        }
    }


    private static String toHugo(File file) {
        String yamlStr = FontMatterUtil.readYamlStr(file);
        HexoFrontMatter hexo = new Yaml().loadAs(yamlStr, HexoFrontMatter.class);
        LinkedHashMap<String, Object> hugo = new LinkedHashMap<>();
        hugo.put("title", hexo.title);
        // 2021-11-11 02:02:02
        hugo.put("date", hexo.date);
        hugo.put("tags", hexo.tags);
        List<Object> categories = hexo.categories;
        if (categories.isEmpty()) {
            System.out.println("没有categories " + file.getAbsolutePath());
        }
        Object cateObj = categories.get(0);
        if (cateObj instanceof List) {
            categories = (List) cateObj;
        }
        hugo.put("categories", categories);
        return new Yaml().dumpAs(hugo, new Tag(Tag.PREFIX.concat("---")), DumperOptions.FlowStyle.BLOCK).substring(2).concat("---\n");
    }
}
