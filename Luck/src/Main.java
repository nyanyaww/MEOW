import java.util.Vector;

public class Main {
    public static void main(String[] args) throws Exception {
        // ʵ����ReadTxt��
        ReadTxt input = new ReadTxt();
        // ���ı����ַ���������ȡ����
        Vector<String> content = input.readTxt("test.txt");
        // ���ı����ַ��������ĳ��ȴ��ݸ�GUI��
        GUI t = new GUI(content.size());
        // ����GUI
        t.run();
    }
}
