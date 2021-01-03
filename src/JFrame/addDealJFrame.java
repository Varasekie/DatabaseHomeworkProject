package JFrame;

import db.db;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

//
public class addDealJFrame extends JFrame {
    JButton cancel, add;
    JTextField[] jTextFields;
    JComboBox<String> year, month, date;

    public addDealJFrame(String No) {
        super("添加成交订单");
//        String[] deal_str = {"房子编号","卖家姓名","卖家电话","买家姓名","买家电话","成交时间","成交价格"};
        this.setSize(600, 400);

        String[] deal_add = {"房子编号", "买家编号", "成交价格", "成交时间"};
        this.getContentPane().setLayout(new GridLayout(deal_add.length + 1, 2));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.jTextFields = new JTextField[deal_add.length - 1];
        for (int i = 0; i < deal_add.length - 1; i++) {
            this.getContentPane().add(new JLabel(deal_add[i]));
            this.getContentPane().add(this.jTextFields[i] = new JTextField());
        }

        //可以直接设置成交时间是现在录入吗x
        this.getContentPane().add(new JLabel("成交时间"));
        String[] year = new String[30];
        String[] month = new String[12];
        String[] date = new String[31];
        for (int i = 0; i < year.length; i++) {
            year[i] = i + 2019 + "";
        }
        for (int i = 1; i <= month.length; i++) {
            month[i - 1] = i + "";
        }
        for (int i = 1; i <= date.length; i++) {
            date[i - 1] = i + "";
        }

        this.year = new JComboBox<>(year);
        this.month = new JComboBox<>(month);
        this.date = new JComboBox<>(date);
        JPanel section = new JPanel(new GridLayout(1, 3));
        section.add(this.year);
        section.add(this.month);
        section.add(this.date);
        this.getContentPane().add(section);

        this.add = new JButton("添加");
        this.add.addActionListener(e -> {
            String HouseNo = this.jTextFields[0].getText();
            String userId = this.jTextFields[1].getText();
            String price = this.jTextFields[2].getText();
            int[] time = new int[5];
            time[0] = Integer.parseInt((String) Objects.requireNonNull(this.year.getSelectedItem()));
            time[1] = Integer.parseInt((String) Objects.requireNonNull(this.month.getSelectedItem()));
            time[2] = Integer.parseInt((String) Objects.requireNonNull(this.date.getSelectedItem()));
            Date date1 = new Date(time[0], time[1], time[2]);
            //如何储存时间
            String str = (String) Objects.requireNonNull(this.year.getSelectedItem()) + "-"
                    + (String) Objects.requireNonNull(this.month.getSelectedItem()) +
                    "-" + (String) Objects.requireNonNull(this.date.getSelectedItem());

            String add_sql = "insert into deal(HouseNo,EmployerId,No,Time,Price) " +
                    "values (?,?,?,?,?)";
            db db = new db();

            //直接在这里判断，是否有不一样的
            try {
                PreparedStatement preparedStatement = db.preparedStatement(add_sql);
                preparedStatement.setString(1, HouseNo);
                preparedStatement.setString(2, No);
                preparedStatement.setString(3, userId);//用户
                preparedStatement.setString(4, str);
                preparedStatement.setString(5, price);
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
                JOptionPane.showMessageDialog(null, "插入成功");
                this.dispose();
                employerJFrame employerJFrame = new employerJFrame(No);
                employerJFrame.setVisible(true);
            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//                System.out.println(1);
                JOptionPane.showMessageDialog(null,"输入信息有误");
            }
        });
        this.cancel = new JButton("取消");
        this.cancel.addActionListener(e -> {
            try {
                this.dispose();
                employerJFrame employerJFrame = new employerJFrame(No);
                employerJFrame.setVisible(true);
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null,"无法关闭");
//                throwables.printStackTrace();
            }

        });
        this.getContentPane().add(this.add);
        this.getContentPane().add(this.cancel);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        new addDealJFrame("0004");
    }
}
