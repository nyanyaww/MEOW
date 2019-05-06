import javax.swing.*;               // ������AWTΪ������ͼ�ν���ϵͳ�ؼ�����
import java.awt.event.*;            // ������ Swing ��������¼�����
import java.awt.*;                  // ����java���󴰿ڹ��߰�,�ṩ����������GUI�������
import java.util.Vector;            // ���ڴ洢�û���Ϣ�����ݽṹ������ʹ�����ַ�������

class GUI extends JFrame {          // GUI��̳���JFrame ����ֻ��һ��run()�ӿڿɹ��ⲿ����

    private JButton start_bottom;
    private JButton end_bottom;
    private int total;
    private int temp;

    // ʵ����һ�������ظ��Ķ���
    private NonRepetitiveRandom r = new NonRepetitiveRandom();
    // ʵ������ø����ı�����Ķ���
    private ReadTxt input = new ReadTxt();
    // ���ı�����������ʽ�洢���˴Σ�����ʹ�õ��ַ������������������ַ���ջ�Ĺ���
    private Vector<String> content = input.readTxt("test.txt");
    // ���ı����ַ�������ʽ�洢�������Ҫ�Ǹ���������ʾ�����ı��õ�
    private String content_str = input.readTxt_II("test.txt");


    private JPanel text;
    public JPanel message;
    private int flag = -1;

    private int x = 300;

    // GUI��ĳ�ʼ�� ����һ��total����
    // total�����Ǹ����ļ���txt��ʽ�����û�����
    GUI(int total) throws Exception {
        this.total = total;
        setTitle("�齱С����");
        setLocation(650, 300);
        setSize(600, 450);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUI();
    }

    // ����UI�Ĳ���
    private void setUI() {
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JLabel top = new JLabel("��һ�£���һ��", JLabel.CENTER);
        message = new JPanel();
        start_bottom = new JButton("��ʼ�齱");
        end_bottom = new JButton("�����齱");


        text = new JPanel() {
            // �ڴ����ı����򣬼���ʾ������������ʾ�齱���ֵ�ʱ������������JPanel()��paint(Graphics g)����
            // ����һ��ʵʱ���µ����ݵķ���������ʵ��ʹ����һ��10ms�Ķ�ʱ���������ݵ�ˢ������100fps
            // ʹ����һ��flagȥ������ʾ������������ʾ�齱����
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                Font font = new Font("����", Font.PLAIN, 30);
                g.setFont(font);
                FontMetrics fm = g.getFontMetrics();
                int strWidth = fm.stringWidth(content_str);                            // �õ��ַ����Ŀ��
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
                            g.drawString("�Ѿ�����", 250, 150);
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
        top.setFont(new Font("����", 0, 24));
        top.setForeground(Color.white);

        start_bottom.setBackground(Color.blue);
        start_bottom.setOpaque(true);
        start_bottom.setFont(new Font("����", 0, 36));
        start_bottom.setForeground(Color.white);

        end_bottom.setBackground(Color.red);
        end_bottom.setOpaque(true);
        end_bottom.setFont(new Font("����", 0, 36));
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

    // ��ʼ��ť�ļ��� ����ʼ��ť�����µ�ʱ��һ�������ظ����ֵ����������ֵ��temp
    // ͬʱ����flag��Ϊ0����ʾ�����Ѿ���ʼ���У��ı�����ͬʱ��ʾ������
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

    // ������ť�ļ��� ��������ť�����µ�ʱ��򵥵ؽ�flag��Ϊ1����ʾ�ı�����Ӧ����ʾ�齱���û�
    class end_bottom_listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == end_bottom) {
                flag = 1;
                if (temp != -1)
                    JOptionPane.showMessageDialog(message, content.get(temp), "��������ã�", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(message, "�Ѿ������ˣ�", "��������ã�", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // ˢ��ʱ�� �����ÿ�ε�ָ��ˢ��ʱ��͸���һ�α���������ˢ���ı�����
    class refresh implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            x = x - 3;
            repaint();
        }
    }

    // ��ʱ�� �������ʹ����10ms�Ķ�ʱ��ȥʹ��refresh����
    private void time_run() {
        Timer timeAction = new Timer(10, new refresh());
        timeAction.start();
    }

    // GUI���������
    // ������ʱ������
    // ��ʼ�������ť�İ����¼�����
    void run() {
        time_run();

        start_bottom.addActionListener(new start_bottom_listener());
        end_bottom.addActionListener(new end_bottom_listener());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

}


