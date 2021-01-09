package JFrame;

import db.db;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

//
public class addDealJFrame extends JFrame implements FocusListener {
    JButton cancel, add;
    JTextField[] jTextFields;
    JComboBox<String> year, month, date;
    private final JLabel[] messsage;

    public addDealJFrame(String No) {
        super("添加成交订单");
        this.setSize(600, 400);

        //房子和买家，作为输入项，需要保证外键约束，使用消息提示框。

        String[] deal_add = {"房子编号", "买家编号", "成交价格", "成交时间"};
        this.getContentPane().setLayout(new GridLayout(deal_add.length + 1, 3));
        this.setLocationRelativeTo(null);

        this.jTextFields = new JTextField[deal_add.length - 1];
        this.messsage = new JLabel[4];
        String[] messages = {"房子编号不存在", "买家编号不存在", "", ""};
        for (int i = 0; i < deal_add.length - 1; i++) {
            this.getContentPane().add(new JLabel(deal_add[i]));
            this.getContentPane().add(this.messsage[i] = new JLabel(messages[i]));
            this.messsage[i].setVisible(false);
            this.getContentPane().add(this.jTextFields[i] = new JTextField());
        }

        //判断房子编号
        this.jTextFields[0].addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            //类比身份证号
            @Override
            public void focusLost(FocusEvent e) {
                String idText = jTextFields[0].getText();
                if (idText.length() != 6 || !idText.matches("[0-9]+")) {
                    //这里其实可以更加规范一些的，因为最后一位可以是X，属于java正则表达式范围，不予考虑
                    JOptionPane.showMessageDialog(null, "长度不符合规范");

                }

                //然后在判断是否数据库中已经有了，有的话显示已经有了
                String sql_id = "select * from house where HouseNo = '" + idText + "'";
                //可以root权限登录
                db dbCon = new db(1);
                try {
                    ResultSet rs = dbCon.executeQuery(sql_id);
                    if (rs.next()) {
                        //防止结果集为空报错
                        rs.last();
                        messsage[0].setVisible(false);
                    } else messsage[0].setVisible(true);
                    rs.close();
                    dbCon.closeConn();
                } catch (SQLException ignored) {

                }
            }
        });

        //判断用户编号
        this.jTextFields[1].addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                String idText = jTextFields[0].getText();
                if (idText.length() != 6 || !idText.matches("[0-9]+")) {
                    //这里其实可以更加规范一些的，因为最后一位可以是X，属于java正则表达式范围，不予考虑
                    JOptionPane.showMessageDialog(null, "长度不符合规范");

                }

                //然后在判断是否数据库中已经有了，有的话显示已经有了
                String sql_id = "select * from user where No = '" + idText + "'";
                //可以root权限登录
                db dbCon = new db(1);
                try {
                    ResultSet rs = dbCon.executeQuery(sql_id);
                    if (rs.next()) {
                        //防止结果集为空报错
                        rs.last();
                        messsage[1].setVisible(false);
                    } else messsage[1].setVisible(true);
                    rs.close();
                    dbCon.closeConn();
                } catch (SQLException ignored) {

                }
            }
        });
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
        this.getContentPane().add(new JLabel(""));
        this.getContentPane().add(section);

        this.add = new JButton("添加");
        //添加数据
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

            //获得当前的时间
            java.sql.Date date2 = new java.sql.Date(2021,1,8);
            //直接在这里判断，是否有不一样的
            try {
                PreparedStatement preparedStatement = db.preparedStatement(add_sql);
                preparedStatement.setString(1, HouseNo);
                preparedStatement.setString(2, No);
                preparedStatement.setString(3, userId);//用户
                preparedStatement.setDate(4, date2);
                preparedStatement.setString(5, price);
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
                JOptionPane.showMessageDialog(null, "插入成功");
                this.dispose();
                employerJFrame employerJFrame = new employerJFrame(No);
                employerJFrame.setVisible(true);
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "输入信息有误");
            }
        });
        this.cancel = new JButton("取消");
        this.cancel.addActionListener(e -> {
            this.dispose();
        });
        this.getContentPane().add(this.add);
        this.getContentPane().add(new JLabel(""));
        this.getContentPane().add(this.cancel);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        new addDealJFrame("0004");
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

    }
}
