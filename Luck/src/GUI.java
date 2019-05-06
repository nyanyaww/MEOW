import javax.swing.*;               // 导入以AWT为基础的图形界面系统控件的类
import java.awt.event.*;            // 包含供 Swing 组件触发事件的类
import java.awt.*;                  // 导入java抽象窗口工具包,提供许多用来设计GUI组件的类
import java.util.Vector;            // 用于存储用户信息的数据结构，本次使用了字符串向量

class GUI extends JFrame {          // GUI类继承自JFrame 本类只有一个run()接口可供外部调用

    private JButton start_bottom;
    private JButton end_bottom;
    private int total;
    private int temp;

    // 实例化一个不会重复的对象
    private NonRepetitiveRandom r = new NonRepetitiveRandom();
    // 实例化获得给定文本输入的对象
    private ReadTxt input = new ReadTxt();
    // 将文本以向量的形式存储，此次，我们使用的字符串向量大致类似与字符串栈的功能
    private Vector<String> content = input.readTxt("test.txt");
    // 将文本以字符串的形式存储，这个主要是给滚动条显示滚动文本用的
    private String content_str = input.readTxt_II("test.txt");


    private JPanel text;
    public JPanel message;
    private int flag = -1;

    private int x = 300;

    // GUI类的初始化 接受一个total参数
    // total参数是给定文件（txt格式）的用户数量
    GUI(int total) throws Exception {
        this.total = total;
        setTitle("抽奖小程序");
        setLocation(650, 300);
        setSize(600, 450);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUI();
    }

    // 设置UI的布局
    private void setUI() {
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JLabel top = new JLabel("点一下，玩一年", JLabel.CENTER);
        message = new JPanel();
        start_bottom = new JButton("开始抽奖");
        end_bottom = new JButton("结束抽奖");


        text = new JPanel() {
            // 在创建文本区域，即显示滚动条或者显示抽奖名字的时候我们重载了JPanel()的paint(Graphics g)方法
            // 这是一个实时更新的数据的方法，具体实现使用了一个10ms的定时器，即数据的刷新率是100fps
            // 使用了一个flag去控制显示滚动条还是显示抽奖名字
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                Font font = new Font("宋体", Font.PLAIN, 30);
                g.setFont(font);
                FontMetrics fm = g.getFontMetrics();
                int strWidth = fm.stringWidth(content_str);                            // 得到字符串的宽度
                switch (flag) {
                    case -1:
                        g.drawString("", 0, 0);
                        break;
                    case 0:
                        if (x < 0 && Math.abs(x) > strWidth) {
                            x = 0;
                        }
                        g.drawString(content_str, x, 150);
                        g.drawString(content_str, strWidth + 10 + x, 150);
                        break;
                    case 1:
                        if (temp != -1)
                            g.drawString(content.get(temp), 200, 150);
                        else
                            g.drawString("已经抽完", 250, 150);
                        break;
                }
            }

            @Override
            public void update(Graphics g) {
                paint(g);
            }

        };


        top.setBackground(Color.red);
        top.setOpaque(true);
        top.setFont(new Font("黑体", 0, 24));
        top.setForeground(Color.white);

        start_bottom.setBackground(Color.blue);
        start_bottom.setOpaque(true);
        start_bottom.setFont(new Font("宋体", 0, 36));
        start_bottom.setForeground(Color.white);

        end_bottom.setBackground(Color.red);
        end_bottom.setOpaque(true);
        end_bottom.setFont(new Font("宋体", 0, 36));
        end_bottom.setForeground(Color.white);

        add(p1, BorderLayout.NORTH);
        add(p2, BorderLayout.SOUTH);
        add(text, BorderLayout.CENTER);


        p1.setBackground(Color.red);
        p2.setBackground(Color.white);


        p1.add(top);
        p2.add(start_bottom);
        p2.add(end_bottom);
        text.setLayout(null);
    }

    // 开始按钮的监听 当开始按钮被按下的时候将一个不会重复出现的随机变量赋值给temp
    // 同时，将flag置为0，表示程序已经开始运行，文本区域同时显示滚动条
    class start_bottom_listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == start_bottom) {
                temp = r.NPRandom(total);
//                if (temp != -1)
//                    System.out.println(content.get(temp));
                flag = 0;
            }
        }
    }

    // 结束按钮的监听 当结束按钮被按下的时候简单地将flag置为1，表示文本区域应该显示抽奖的用户
    class end_bottom_listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == end_bottom) {
                flag = 1;
                if (temp != -1)
                    JOptionPane.showMessageDialog(message, content.get(temp), "你运气真好！", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(message, "已经抽完了！", "你运气真好！", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // 刷新时间 这个是每次到指定刷新时间就更新一次变量，并且刷新文本区域
    class refresh implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            x = x - 3;
            repaint();
        }
    }

    // 定时器 这次我们使用了10ms的定时器去使用refresh方法
    private void time_run() {
        Timer timeAction = new Timer(10, new refresh());
        timeAction.start();
    }

    // GUI程序的运行
    // 包含定时器监听
    // 开始与结束按钮的按下事件监听
    void run() {
        time_run();

        start_bottom.addActionListener(new start_bottom_listener());
        end_bottom.addActionListener(new end_bottom_listener());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

}


