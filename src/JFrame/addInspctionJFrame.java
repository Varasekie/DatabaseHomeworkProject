package JFrame;

import db.db;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class addInspctionJFrame extends JFrame implements ActionListener {
    String No;
    JTextField[] textFields;
    JButton add, cancel;
    JComboBox<String> year, month, date, hour, min;

    public addInspctionJFrame(String No) {
        super("添加用户信息");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.No = No;
        this.getContentPane().setLayout(new GridLayout(4, 2));
        this.setSize(600, 400);
        String[] strings = {"房子编号", "用户编号", "预约时间"};
        this.textFields = new JTextField[strings.length];
        for (int i = 0; i < strings.length - 1; i++) {
            this.getContentPane().add(new JLabel(strings[i]));
            this.getContentPane().add(this.textFields[i] = new JTextField());
        }

        this.getContentPane().add(new JLabel(strings[2]));

        //预约时间
        String[] year = new String[30];
        String[] month = new String[12];
        String[] date = new String[31];
        String[] hour = new String[24];
        String[] min = new String[60];
        for (int i = 0; i < year.length; i++) {
            year[i] = i + 2000 + "";
        }
        for (int i = 0; i < month.length; i++) {
            month[i] = i + "";
        }
        for (int i = 0; i < date.length; i++) {
            date[i] = i + "";
        }
        for (int i = 0; i < hour.length; i++) {
            hour[i] = i + "";
        }
        for (int i = 0; i < min.length; i++) {
            min[i] = i + "";
        }
        this.year = new JComboBox<>(year);
        this.month = new JComboBox<>(month);
        this.date = new JComboBox<>(date);
        this.hour = new JComboBox<>(hour);
        this.min = new JComboBox<>(min);
        JPanel section = new JPanel(new GridLayout(1, 5));
        section.add(this.year);
        section.add(this.month);
        section.add(this.date);
        section.add(this.hour);
        section.add(this.min);
        this.getContentPane().add(section);

        this.getContentPane().add(this.add = new JButton("添加"));
        this.getContentPane().add(this.cancel = new JButton("取消"));
        this.add.addActionListener(e -> {


            //这里是添加
            db db = new db();
            String add_sql = "insert inspection (HouseNo,EmployerId,UserNo,Time) values(?,?,?,?)";
            int[] ints = new int[textFields.length];
            try {
                for (int i = 0; i < textFields.length-1; i++) {
                    ints[i] = Integer.parseInt(this.textFields[i].getText());
                }
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "输入数字有错误2563280005");
            }


            int[] time = new int[5];
            time[0] = Integer.parseInt((String) Objects.requireNonNull(this.year.getSelectedItem()));
            time[1] = Integer.parseInt((String) Objects.requireNonNull(this.month.getSelectedItem()));
            time[2] = Integer.parseInt((String) Objects.requireNonNull(this.date.getSelectedItem()));
            time[3] = Integer.parseInt((String) Objects.requireNonNull(this.hour.getSelectedItem()));
            time[4] = Integer.parseInt((String) Objects.requireNonNull(this.min.getSelectedItem()));
            Date date1 = new Date(time[0], time[1], time[2]);
            //如何储存时间
            String str = (String) Objects.requireNonNull(this.year.getSelectedItem())+"-"+(String) Objects.requireNonNull(this.month.getSelectedItem())+
                    "-"+(String) Objects.requireNonNull(this.date.getSelectedItem())+" "+(String) Objects.requireNonNull(this.hour.getSelectedItem())+
                    ":"+(String) Objects.requireNonNull(this.min.getSelectedItem());
            //直接在这里判断，是否有不一样的
            try {
                PreparedStatement preparedStatement = db.preparedStatement(add_sql);
                preparedStatement.setString(1, "" + ints[0]);
                preparedStatement.setString(2, No);
                preparedStatement.setString(3, "" + ints[1]);//用户
                preparedStatement.setString(4, str);
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
                JOptionPane.showMessageDialog(null, "插入成功");
                this.dispose();
                employerJFrame employerJFrame = new employerJFrame(No);
                employerJFrame.setVisible(true);
            } catch (SQLException throwables) {
//                throwables.printStackTrace();
            }
        });
        this.cancel.addActionListener(e -> {
            this.dispose();
        });
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        new addInspctionJFrame("0004");
    }
}
