package JFrame;

import Entity.houseExceptedEntity;
import db.MyTableModel;
import db.db;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.sql.Date;

public class matchHouseJFrame extends JFrame {
    private JTable table;
    MyTableModel tableModel;
    JComboBox<String> year, month, date, hour, min;
    JButton inspect;

    public matchHouseJFrame(MyTableModel myTableModel, String employerId, String userId) throws SQLException {
        super("相匹配的房子");
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);

        tableModel = myTableModel;
        table = new JTable(tableModel);

        table.setPreferredScrollableViewportSize(new Dimension(500, 250));
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
//预约时间
        String[] year = new String[30];
        String[] month = new String[12];
        String[] date = new String[31];
        for (int i = 0; i < year.length; i++) {
            year[i] = i +2000 + "";
        }
        for (int i = 0; i < month.length; i++) {
            month[i] = i + "";
        }
        for (int i = 0; i < date.length; i++) {
            date[i] = i + "";
        }

        this.year = new JComboBox<>(year);
        this.month = new JComboBox<>(month);
        this.date = new JComboBox<>(date);
        JPanel section = new JPanel(new GridLayout(1, 4));
        section.add(this.year);
        section.add(this.month);
        section.add(this.date);
        this.inspect = new JButton("看房预约");
        section.add(this.inspect);
        this.getContentPane().add(section, "South");
        //预约时间


        this.inspect.addActionListener(e -> {
            //直接根据匹配的房子，选择一个房子

            int row = table.getSelectedRow();
            //根据选中的这一行的房子，insert一个看房
            String insert_sql = "insert inspection (HouseNo,EmployerID,No,Time,condiotion) values (?,?,?,?,'未处理')";

            db db = new db(1);
            int[] time = new int[5];
            time[0] = this.year.getSelectedIndex();
            time[1] = this.year.getSelectedIndex();
            time[2] = this.year.getSelectedIndex();
            Date date1 = new Date(time[0]+10, time[1], time[2]);
            System.out.println(time[0]);
            System.out.println(time[1]);
            System.out.println(time[2]);
            System.out.println(date1.toString());
//            date1.setTime(1999-3-3);
            //直接在这里判断，是否有不一样的

            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = db.preparedStatement(insert_sql);
                preparedStatement.setString(1, table.getValueAt(row, 0).toString());
                System.out.println(table.getValueAt(row, 0).toString());
                preparedStatement.setString(2, employerId);
                preparedStatement.setString(3, userId);
                preparedStatement.setDate(4, date1);
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
                JOptionPane.showMessageDialog(null, "成功预约时间");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });
    }
}
