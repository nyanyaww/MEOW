import java.util.Random;
import java.util.Vector;

// 生成不重复数字的类
// 本类拥有一个接口NPRandom(int total)去供外部调用
public class NonRepetitiveRandom {
    private Random r = new Random();
    private Vector<Integer> chosen = new Vector<Integer>();

    // 本类重要的方法 事实上也是唯一的给外部调用的接口
    // 在给定范围[0，total]内生成一个不会重复的数字
    // 如果生成的数字与之前的数字有重复，则返回-1
    // 值得一提的是我们使用了int向量去存储之前生成的数字
    int NPRandom(int total) {
        int temp;
        long start_time = System.currentTimeMillis();
        long end_time;
        while (true) {
            end_time = System.currentTimeMillis();
            temp = r.nextInt(total);
            // 判断此次生成的数字是否重复
            // 如果不重复就添加到int向量，并且返回这个数字
            // 重复的话就再循环一次 直到生成不重复的数字
            if (!is_repeat(temp)) {
                chosen.add(temp);
                return temp;
            }
            // 如果在这个循环存留的时间过长（大于1.5s）
            // 我们就认为所有数字都已经出现过一次了，没法再次生成不重复的数字了
            if (end_time - start_time >= 1500)
                return -1;

        }
    }

    // 判断生成的随机数是否与之前生成的随机数重复
    private boolean is_repeat(int number) {
        for (int i = 0; i < chosen.size(); i++) {
            if (chosen.get(i) == number)
                return true;
        }
        return false;
    }

    // 测试 如果这个类运行良好 应该会输出4950
    public static void main(String[] args) {
        NonRepetitiveRandom test = new NonRepetitiveRandom();
        int COUNT = 0;
        for (int i = 0; i < 100; i++) {
            COUNT += test.NPRandom(100);
        }
        System.out.println(COUNT);
    }
}
