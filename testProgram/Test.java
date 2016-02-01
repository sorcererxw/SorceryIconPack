import java.io.*;

/**
 * Created by Sorcerer on 2016/1/30.
 */
public class Test {

    public static FileWriter writer;

    public static void main(String[] args) throws IOException {
        String s1 = getContent("C:\\Users\\Sorcerer\\AndroidStudioProjects\\SorceryIconPack\\app\\src\\main\\res\\xml\\appfilter.xml");
        String s2 = compare("C:\\Users\\Sorcerer\\AndroidStudioProjects\\SorceryIconPack\\testProgram\\test.xml", s1);
        if (s2 != null) {
            writeFile("C:\\Users\\Sorcerer\\AndroidStudioProjects\\SorceryIconPack\\testProgram\\output.txt", s2);
        }
    }

    public static String getContent(String filePath) {
        File file = new File(filePath);
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            // System.out.println("读取" + filePath);
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                builder.append(tempString);
                line++;
            }
            reader.close();
            System.out.println("ok");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
                return builder.toString();
            }
            return null;
        }
    }

    public static String compare(String filePath, String orgin) {
        File file = new File(filePath);
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                if (line % 3 == 2) {
                    if (orgin.indexOf(tempString) == -1) {
                        builder.append(tempString + "\n");
                    }
                } else {
                    builder.append(tempString + "\n");
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
                return builder.toString();
            }
            return null;
        }
    }

    public static void writeFile(String filePathAndName, String fileContent) {
//        try {
//            File f = new File(filePathAndName);
//            if (!f.exists()) {
//                f.createNewFile();
//            }
//            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
//            BufferedWriter writer = new BufferedWriter(write);
//            writer.write(fileContent);
//            writer.close();
//        } catch (Exception e) {
//            System.out.println("写文件内容操作出错");
//            e.printStackTrace();
//        }
        File f = new File(filePathAndName);
        try {
            FileOutputStream fop = new FileOutputStream(f);
            // 构建FileOutputStream对象,文件不存在会自动新建
            OutputStreamWriter writer = new OutputStreamWriter(fop, "GBK");
            // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码,windows上是gbk
            writer.write(fileContent);
            //      writer.flush();
            // 刷新缓存冲,写入到文件,如果下面已经没有写入的内容了,直接close也会写入
            writer.close();
            //关闭写入流,同时会把缓冲区内容写入文件,所以上面的注释掉
            fop.close();
            // 关闭输出流,释放系统资源
        }catch (Exception e){

        }
    }
}

