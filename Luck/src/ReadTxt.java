import java.io.*;
import java.util.Vector;

// ��ȡ�����ļ���Ϣ����
public class ReadTxt {

    public static void main(String[] args) throws Exception {
        String inputPath = "test.txt";
        ReadTxt te = new ReadTxt();
        Vector<String> content = te.readTxt(inputPath);
        for (String s : content)
            System.out.println(s);

    }

    // ����һ���ļ���·�� Ȼ�󷵻�һ���ַ�������
    // ��������˽�з���getCodeȥȷ��txt�ı����ʽ
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

    // ʵ�������������Ƶò��Ǻܺã������ȵ�����readTxt(String path)����
    // ֮���ǰ��Ǹ������ķ���ֵ�ַ�������ת��Ϊ�ַ���
    // ��ʵ������һ����Ҫ����readTxt(String path)�����ģ���������дһ�����ļ��ķ���
    String readTxt_II(String path) throws Exception {
        Vector<String> content = readTxt(path);
        StringBuilder content_str = new StringBuilder();
        for (int i = 0; i < content.size(); i++)
            content_str.append(content.get(i)).append(" ");
        return content_str.toString();
    }

    // windows��txt��ʽ��Щ����׽��������Ӧ����4�ָ�ʽ��ĳһ��
    // ���Ƕ�ȡtxt��ͷ�ļ���Ϣ���ж�txt�ı����ʽ
    private String getCode(String path) throws Exception {
        InputStream inputStream = new FileInputStream(path);
        byte[] head = new byte[3];
        inputStream.read(head);
        String code = "gb2312";  //��GBK
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