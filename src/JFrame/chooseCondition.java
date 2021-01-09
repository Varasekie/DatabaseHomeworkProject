package JFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.*;

public class chooseCondition extends JFrame {

    String houseNo, employerNo, userNo;
    JTextField price;
    JButton deal, rollback, add, dispose;

    public chooseCondition(String houseNo, String employerNo, String userNo) {
        super("看房订单更进");
        this.setSize(600, 200);

        this.houseNo = houseNo;
        this.employerNo = employerNo;
        this.userNo = userNo;
        //选择跟进或者直接取消订单
        this.deal = new JButton("成交");
        this.rollback = new JButton("无意愿");

        JPanel jPanel = new JPanel(new GridLayout(1, 2));
        jPanel.add(this.deal);
        jPanel.add(this.rollback);
        this.getContentPane().add(jPanel, "North");

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.setVisible(false);
        this.price = new JTextField();
        panel.add(new JLabel("价格"));
        panel.add(this.price = new JTextField());

        panel.add(this.add = new JButton("添加成交信息"));
        panel.add(this.dispose = new JButton("取消"));
        this.getContentPane().add(panel, "Center");

        //将订单状态变成已完成
        this.deal.addActionListener(e -> {
            panel.setVisible(true);

        });

        this.dispose.addActionListener(e -> {
            this.dispose();
        });
        this.add.addActionListener(e -> {
            db db = new db(1);

            //本质添加订单
            String price = this.price.getText();

            //保证能转化
            try {
                int prices = Integer.parseInt(price);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "输入价格有误");
            }

            String add_sql = "insert into deal(HouseNo,EmployerId,No,Time,Price) " +
                    "values (?,?,?,?,?)";

            //获得当前的时间

            java.sql.Date date2 = new java.sql.Date(21, 1, 8);
            //直接在这里判断，是否有不一样的
            try {
                PreparedStatement preparedStatement = db.preparedStatement(add_sql);
                preparedStatement.setString(1, this.houseNo);
                preparedStatement.setString(2, this.employerNo);
                preparedStatement.setString(3, this.userNo);//用户
                preparedStatement.setDate(4, date2);
                preparedStatement.setString(5, price);
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
                JOptionPane.showMessageDialog(null, "插入成功");
                this.dispose();
            } catch (SQLException throwables) {
//                throwables.printStackTrace();

//                JOptionPane.showMessageDialog(null, "输入信息有误");
            }

            //插入成功后再触发器
            CallableStatement proc = null;
            String i = "";
            try {
                proc = db.prepareCall("{ call changeCondition(?,?,?) }");

                proc.setString(1, houseNo);
                proc.setString(2, "");
                proc.setString(3, userNo );
                proc.execute();

               i = proc.getString(2);
            } catch (SQLException throwables) {
//                throwables.printStackTrace();
            }

//            JOptionPane.showMessageDialog(null,""+i);

        });
        //修改订单状态变成已退回
        this.rollback.addActionListener(e -> {
            db db = new db(1);
            String sql = "update inspection set condiotion = '已退回' where HouseNo = ? and EmployerId = ? and No = ?";

            try {
                PreparedStatement presta = db.preparedStatement(sql);
                presta.setString(1, houseNo);
                presta.setString(2, employerNo);
                presta.setString(3, userNo);
                presta.addBatch();

                presta.executeBatch();

                JOptionPane.showMessageDialog(null, "成功修改订单状态为已退回");
                presta.close();
//                db.closeConn();
                this.dispose();

            } catch (SQLException throwables) {
//                    throwables.printStackTrace();
                JOptionPane.showMessageDialog(null, "更新失败");

            }

            //房子还要变成待处理
            String sql_houseexpect = "update houseexpect set conditions = '未处理' where userId = ? and employerId = ? and conditions = '已处理'";
            try {
                PreparedStatement presta = db.preparedStatement(sql_houseexpect);
                presta.setString(1, houseNo);
                presta.setString(2, employerNo);
                presta.addBatch();

                presta.executeBatch();

                presta.close();
//                db.closeConn();
                this.dispose();

            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "更新失败");
            }


        });
    }
}
