package JFrame;

import Entity.houseEntity;
import Entity.*;
import db.db;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class timeSelectJFrame extends JFrame {
    String No;
    JButton add, cancel;
    JComboBox<String> year, month, date, employer;

    public timeSelectJFrame(String No,String houseNo) {
        super("添加预约信息"+No);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.No = No;
        this.getContentPane().setLayout(new GridLayout(3, 2));
        this.setSize(600, 400);

        this.getContentPane().add(new JLabel("接待人"));
        this.getContentPane().add(this.employer = new JComboBox<>());
        //string数组
        ArrayList<employerEntity> v = getemployers();
        String[] string_name = new String[v.size()];
        String[] string_ID = new String[v.size()];
        //获得名字
        for (int i =0;i<string_name.length;i++){
            string_name[i] = v.get(i).getName();
        }

        for (String string : string_name) {
            this.employer.addItem(string);
        }

        //同理，获取id
        for (int i =0;i<string_name.length;i++){
            string_ID[i] = v.get(i).getNo();
        }

        this.getContentPane().add(new JLabel("预约时间"));

        //预约时间
        String[] year = new String[30];
        String[] month = new String[12];
        String[] date = new String[31];
        for (int i = 0; i < year.length; i++) {
            year[i] = i + 2000 + "";
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
        JPanel section = new JPanel(new GridLayout(1, 3));
        section.add(this.year);
        section.add(this.month);
        section.add(this.date);
        this.getContentPane().add(section);

        this.getContentPane().add(this.add = new JButton("添加"));
        this.getContentPane().add(this.cancel = new JButton("取消"));
        this.add.addActionListener(e -> {
            //这里是添加
            db db = new db(1);

            String add_sql = "insert inspection (HouseNo,EmployerId,No,Time) values(?,?,?,?)";

            int[] time = new int[3];
            time[0] = Integer.parseInt((String) Objects.requireNonNull(this.year.getSelectedItem()));
            time[1] = Integer.parseInt((String) Objects.requireNonNull(this.month.getSelectedItem()));
            time[2] = Integer.parseInt((String) Objects.requireNonNull(this.date.getSelectedItem()));
            Date date1 = new Date(time[0]-1900, time[1], time[2]);
            date1 = new Date(1999,9,8);
//            System.out.println(date1);
            //如何储存时间

            int employer = this.employer.getSelectedIndex();
            //直接在这里判断，是否有不一样的
//            try {
//                PreparedStatement preparedStatement = db.preparedStatement(add_sql);
//
//                preparedStatement.setString(1, houseNo);
//                preparedStatement.setString(2, string_ID[employer]);
//                preparedStatement.setString(3, No);//用户
//                preparedStatement.setDate(4, date1);//时间
//
//                preparedStatement.addBatch();
//                preparedStatement.executeBatch();
//                JOptionPane.showMessageDialog(null, "插入成功");
//                this.dispose();
//            } catch (SQLException throwables) {
////                throwables.printStackTrace();
//            }

            String message = "预约成功";
            try {
                CallableStatement proc = db.prepareCall("{call inspect_proc(?,?,?,?,?) }");
                proc.setString(1, houseNo);
                proc.setString(2, string_ID[employer]);
                proc.setString(3, No);
                proc.setDate(4, date1);
                proc.setString(5,"预约成功");
                proc.execute();

                message = proc.getString(5);

            } catch (SQLException throwables) {
//                throwables.printStackTrace();
                JOptionPane.showMessageDialog(null, "已预约过");

            }
            JOptionPane.showMessageDialog(null, "预约成功");

        });
        this.cancel.addActionListener(e -> {
            this.dispose();
        });
        this.setVisible(true);
    }


    public ArrayList<employerEntity> getemployers() {
        String[] strings = new String[0];
        ArrayList<employerEntity> v = null;
        db db = new db(1);
        try {
            //筛选
            String sql = "select *  from employer";
            ResultSet resultSet = db.executeQuery(sql);

            v = new ArrayList<employerEntity>();
            while (resultSet.next()) {
                employerEntity employerEntity = new employerEntity();
                employerEntity.setName(resultSet.getString("Name"));
                employerEntity.setNo(resultSet.getString("EmployerID"));
                v.add(employerEntity);
            }
            resultSet.close();

            strings = new String[v.size()];
            for (int i = 0; i < strings.length; i++) {
                strings[i] = v.get(i).getName();
            }
            db.closeConn();

        } catch (SQLException e) {
//            e.printStackTrace();
        }
        return v;

    }
}
