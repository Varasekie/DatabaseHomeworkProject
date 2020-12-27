package JFrame;

import db.MyTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class employerJFrame extends JFrame implements ActionListener {
    private JTable table;
    JToolBar toolBar = new JToolBar();
    JButton[] buttons;
    private String No;
    JMenuBar jMenuBar;
    JMenu[] menus;
    JMenuItem[][] menuItems;
    MyTableModel tableModel;
    JButton add, update, delete;
    public employerJFrame(String no){
        super("管理员界面");
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.No = no;
        //工资查看可以给一个存储过程，根据完成了多少订单返回一个提成量
        //根据待处理订单，可以更改需求房子的订单
        String[] menus = {"待处理订单", "看房预约","工资查看"};

        this.buttons = new JButton[menus.length];
        for (int i =0;i<menus.length;i++){
            this.buttons[i] = new JButton(menus[i]);
            this.buttons[i].addActionListener(this::actionPerformed);
            this.toolBar.add(buttons[i]);
        }

        this.getContentPane().add(this.toolBar,"North");


    }

    public static void main(String[] args) {
        new employerJFrame("0001");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.buttons[0]){

        }
    }
}
