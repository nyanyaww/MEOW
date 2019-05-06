import java.util.Random;
import java.util.Vector;

// ���ɲ��ظ����ֵ���
// ����ӵ��һ���ӿ�NPRandom(int total)ȥ���ⲿ����
public class NonRepetitiveRandom {
    private Random r = new Random();
    private Vector<Integer> chosen = new Vector<Integer>();

    // ������Ҫ�ķ��� ��ʵ��Ҳ��Ψһ�ĸ��ⲿ���õĽӿ�
    // �ڸ�����Χ[0��total]������һ�������ظ�������
    // ������ɵ�������֮ǰ���������ظ����򷵻�-1
    // ֵ��һ���������ʹ����int����ȥ�洢֮ǰ���ɵ�����
    int NPRandom(int total) {
        int temp;
        long start_time = System.currentTimeMillis();
        long end_time;
        while (true) {
            end_time = System.currentTimeMillis();
            temp = r.nextInt(total);
            // �жϴ˴����ɵ������Ƿ��ظ�
            // ������ظ�����ӵ�int���������ҷ����������
            // �ظ��Ļ�����ѭ��һ�� ֱ�����ɲ��ظ�������
            if (!is_repeat(temp)) {
                chosen.add(temp);
                return temp;
            }
            // ��������ѭ��������ʱ�����������1.5s��
            // ���Ǿ���Ϊ�������ֶ��Ѿ����ֹ�һ���ˣ�û���ٴ����ɲ��ظ���������
            if (end_time - start_time >= 1500)
                return -1;

        }
    }

    // �ж����ɵ�������Ƿ���֮ǰ���ɵ�������ظ�
    private boolean is_repeat(int number) {
        for (int i = 0; i < chosen.size(); i++) {
            if (chosen.get(i) == number)
                return true;
        }
        return false;
    }

    // ���� ���������������� Ӧ�û����4950
    public static void main(String[] args) {
        NonRepetitiveRandom test = new NonRepetitiveRandom();
        int COUNT = 0;
        for (int i = 0; i < 100; i++) {
            COUNT += test.NPRandom(100);
        }
        System.out.println(COUNT);
    }
}
