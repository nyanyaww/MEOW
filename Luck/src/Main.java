import java.util.Vector;

public class Main {
    public static void main(String[] args) throws Exception {
        // 实例化ReadTxt类
        ReadTxt input = new ReadTxt();
        // 将文本的字符串向量提取出来
        Vector<String> content = input.readTxt("test.txt");
        // 把文本的字符串向量的长度传递给GUI类
        GUI t = new GUI(content.size());
        // 运行GUI
        t.run();
    }
}
