package icu.intelli.simpletest.hexo.main;

import icu.intelli.simpletest.hexo.entity.JekyllFrontMatter;
import icu.intelli.simpletest.hexo.util.FontMatterUtil;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * 将Jekyll格式的FrontMatter转换为Hexo格式的FrontMatter, 写到新的文件里
 *
 * @author wangshuo
 * @date 2021/04/26
 */
public class JekyllToHexoMain {
    private static final String newDirPath = "C:\\Users\\33722\\Desktop\\_posts_new";

    public static void main(String[] args) {
        File dir = new File("C:\\Users\\33722\\Desktop\\_posts\\_posts");
        File[] files = dir.listFiles();
        int len = files.length;
        for (int i = 0; i < len; i++) {
            System.out.println("第" + i + "个开始, 共" + len + "个");
            File file = files[i];
            try {
                String hexo = toHexo(file);
                FontMatterUtil.replaceFontMatter(file,
                        new File(newDirPath, file.getName().replaceAll("\\d{4}-\\d{2}-\\d{2}-", "")),
                        hexo);
            } catch (Exception e) {
                throw new RuntimeException(file.getAbsolutePath(), e);
            }
            System.out.println("第" + i + "结束, 共" + len + "个");

        }

    }

    private static String toHexo(File file) {
        String yamlStr = FontMatterUtil.readYamlStr(file);
        JekyllFrontMatter jekyll = new Yaml().loadAs(yamlStr, JekyllFrontMatter.class);
        LinkedHashMap<String, Object> hexo = new LinkedHashMap<>();
        hexo.put("title", jekyll.title);
        hexo.put("date", file.getName().replaceAll("(\\d{4}-\\d{2}-\\d{2}).*", "$1").concat(" 00:00:00"));
        hexo.put("updated", hexo.get("date"));
        hexo.put("tags", Arrays.asList(Optional.ofNullable(jekyll.tags).orElse("").trim().split(" ")));
        hexo.put("categories", (Arrays.asList(Optional.ofNullable(jekyll.categories).orElse("").trim().split(" "))));
        return new Yaml().dumpAs(hexo, new Tag(Tag.PREFIX.concat("---")), DumperOptions.FlowStyle.BLOCK).substring(2).concat("---\n");
    }


}
