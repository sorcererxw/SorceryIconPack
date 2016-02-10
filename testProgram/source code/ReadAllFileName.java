import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Sorcerer on 2016/2/9 0009.
 */
public class ReadAllFileName {
    public static void main(String[] args) {

        // This is the path where the file's name you want to take.
        String path = "C:\\Users\\Sorcerer\\AndroidStudioProjects\\SorceryIconPack\\app\\src\\main\\res\\drawable-nodpi";
        writeFile("C:\\Users\\Sorcerer\\AndroidStudioProjects\\SorceryIconPack\\testProgram\\iconpack.txt", getFile(path));
    }

    private static String getFile(String path) {
        File file = new File(path);
        File[] array = file.listFiles();
        String res = "";
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                String tmp = array[i].getName();
                tmp = tmp.substring(0, tmp.indexOf('.'));
                res += ("<item>" + tmp + "</item>" + "\n");
            } else if (array[i].isDirectory()) {
//                getFile(array[i].getPath());
            }
        }
        return res;
    }

    public static void writeFile(String filePathAndName, String fileContent) {
        try {
            File f = new File(filePathAndName);
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(fileContent);
            writer.close();
        } catch (Exception e) {
            System.out.println("写文件内容操作出错");
            e.printStackTrace();
        }
    }
}
