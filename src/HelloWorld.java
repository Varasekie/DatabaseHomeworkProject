import javax.swing.*;

public class HelloWorld extends JFrame {
    private JMenuBar menuBar;
    private JMenu menu1 ;
    private JMenuItem m11;
    private JMenuItem m12;
    private JMenu menu2;
    private JMenuItem m21;
    private JMenuItem m22;
    private JMenuItem m23;
    private JMenuItem m24;

    private JButton jButton1,jButton2;
    private JToolBar tool;

    public HelloWorld(){
        super();
        this.setSize(400,300);
        this.setTitle("helloworld");
        this.setLocationRelativeTo(getOwner());
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        this.menu1 = new JMenu("系统");
        this.m11 = new JMenuItem("用户管理");
        this.m12 = new JMenuItem("退出");
        menu1.add(m11);
        menu1.add(m12);

        this.menu2 = new JMenu("数据管理");
        this.m21 = new JMenuItem("查");
        this.m22 = new JMenuItem("添");
        this.m23 = new JMenuItem("改");
        this.m24 = new JMenuItem("删");

        this.menu2.add(this.m21);
        this.menu2.add(this.m22);
        this.menu2.add(this.m23);
        this.menu2.add(this.m24);

        this.menuBar = new JMenuBar();
        menuBar.add(menu1);
        menuBar.add(menu2);
        setJMenuBar(menuBar);

        this.jButton1 = new JButton("查");
        jButton1.setToolTipText("查询");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(getInsets().bottom);

        jButton2 = new JButton("添加");
        jButton2.setToolTipText("添加");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(getInsets().bottom);

        tool = new JToolBar();
        tool.add(jButton1);
        tool.add(jButton2);
        tool.setRollover(true);

        this.add(tool,"North");
        this.setVisible(true);


    }

    public static void main(String[] args) {
        HelloWorld w = new HelloWorld();

    }
}
