package icu.intelli.simpletest.hexo.main;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.*;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * 递归把目录下的所有yyyy-MM-dd-标题名.md的文件转成hexo格式的文件
 * 在文件中添加FontMatter, 并将文件重命名为 标题名.md
 *
 * @author wangshuo
 * @date 2021/06/09
 */
public class AddHexoFontMatterMain {


    private static final File OLD_FOLDER = new File("C:\\Users\\wangshuo\\Desktop\\notes");
    private static final File NEW_FOLDER = new File("C:\\Users\\wangshuo\\Desktop\\notes_new");

    public static void main(String[] args) {
        moveFile(OLD_FOLDER);
    }

    private static void moveFile(File file) {
        if (file.isFile()) {
            // 重写到新目录里
            String fontMatter = generateFontMatter(file);
            writeNewFile(file,
                    new File(NEW_FOLDER, file.getAbsolutePath()
                            .replace(OLD_FOLDER.getAbsolutePath(), "")
                            .replaceAll("(.*)(\\d{4}-\\d{2}-\\d{2}-)(.*\\.md)", "$1$3")),
                    fontMatter);
        } else {
            // 如果是目录, 在新文件夹里创建相同的目录
            new File(NEW_FOLDER, file.getAbsolutePath().replace(OLD_FOLDER.getAbsolutePath(), "")).mkdirs();
            File[] files = file.listFiles();
            for (File f : files) {
                moveFile(f);
            }
        }
    }


    private static void writeNewFile(File oldFile, File newFile, String fontMatter) {
        try (BufferedReader br = new BufferedReader(new FileReader(oldFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(newFile))) {
            bw.write(fontMatter);
            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line.concat("\n"));
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件写入失败, 源文件: ".concat(oldFile.getAbsolutePath()).concat(", 目标文件: ".concat(newFile.getAbsolutePath())));
        }
    }


    private static String generateFontMatter(File file) {
        LinkedHashMap<String, Object> hexo = new LinkedHashMap<>();
        String name = file.getName();
        File parentFile = file.getParentFile();
        String tag = parentFile.getName();
        hexo.put("title", name.replaceAll(".*-(.*)\\.md", "$1"));
        hexo.put("date", name.replaceAll("(\\d{4}-\\d{2}-\\d{2}).*", "$1").concat(" 00:00:00"));
        hexo.put("updated", hexo.get("date"));
        hexo.put("tags", Collections.singletonList(tag));
        hexo.put("categories", Collections.singletonList(tag));
        return new Yaml().dumpAs(hexo, new Tag(Tag.PREFIX.concat("---")), DumperOptions.FlowStyle.BLOCK).substring(2).concat("---\n");
    }
}
