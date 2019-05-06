import java.io.*;
import java.util.Vector;

// 读取给定文件信息的类
public class ReadTxt {

    public static void main(String[] args) throws Exception {
        String inputPath = "test.txt";
        ReadTxt te = new ReadTxt();
        Vector<String> content = te.readTxt(inputPath);
        for (String s : content)
            System.out.println(s);

    }

    // 读入一个文件的路径 然后返回一个字符串向量
    // 它利用了私有方法getCode去确定txt的编码格式
    Vector<String> readTxt(String path) throws Exception {
        Vector<String> temp = new Vector<String>();
        String code = getCode(path);
        File file = new File(path);
        InputStream is = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(is, code);
        BufferedReader br = new BufferedReader(isr);
        String str;
        String line;
        while (null != (str = br.readLine())) {
            line = str.trim();
            temp.add(line);
        }
        br.close();
        return temp;
    }

    // 实际上这个方法设计得不是很好，它首先调用了readTxt(String path)方法
    // 之后是把那个方法的返回值字符串向量转化为字符串
    // 事实上它不一定需要调用readTxt(String path)方法的，它可以重写一个读文件的方法
    String readTxt_II(String path) throws Exception {
        Vector<String> content = readTxt(path);
        StringBuilder content_str = new StringBuilder();
        for (int i = 0; i < content.size(); i++)
            content_str.append(content.get(i)).append(" ");
        return content_str.toString();
    }

    // windows的txt格式有些难以捉摸，但它应该是4种格式的某一种
    // 我们读取txt的头文件信息来判断txt的编码格式
    private String getCode(String path) throws Exception {
        InputStream inputStream = new FileInputStream(path);
        byte[] head = new byte[3];
        inputStream.read(head);
        String code = "gb2312";  //或GBK
        if (head[0] == -1 && head[1] == -2)
            code = "UTF-16";
        else if (head[0] == -2 && head[1] == -1)
            code = "Unicode";
        else if (head[0] == -17 && head[1] == -69 && head[2] == -65)
            code = "UTF-8";
        inputStream.close();
        return code;
    }

}