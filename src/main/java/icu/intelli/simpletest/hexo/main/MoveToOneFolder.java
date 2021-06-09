package icu.intelli.simpletest.hexo.main;

import java.io.File;

/**
 * 递归将目录下的所有文件移动到某一个目录
 *
 * @author wangshuo
 * @date 2021/06/09
 */
public class MoveToOneFolder {

    private static final File OLD_FOLDER = new File("C:\\Users\\wangshuo\\Desktop\\notes");
    private static final File NEW_FOLDER = new File("C:\\Users\\wangshuo\\Desktop\\notes_new");

    public static void main(String[] args) {
        if (!NEW_FOLDER.exists()) {
            NEW_FOLDER.mkdirs();
        }
        doMove(OLD_FOLDER);
    }

    private static void doMove(File file) {
        if (file.isFile()) {
            file.renameTo(new File(NEW_FOLDER, file.getName()));
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                doMove(f);
            }
        }
    }
}
