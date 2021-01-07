package JFrame;

import Entity.userEntity;
import db.db;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginJFrame extends JFrame implements ActionListener {
    private JTextField username;
    private JPasswordField password;
    private JButton login, register;
    private JRadioButton[] radios;
    public userEntity userEntity;

    public loginJFrame() {
        super();
        this.setSize(500, 200);
        this.setTitle("登录");
        this.setLocationRelativeTo(getOwner());

        this.setLayout(new GridLayout(4, 2));

        this.getContentPane().add(new JLabel("身份证号/员工号"));
        this.username = new JTextField("360102199509154151");
//        this.username = new JTextField();
        this.getContentPane().add(this.username);

        this.getContentPane().add(new JLabel("密码"));
        this.password = new JPasswordField("j3vADrc0f");
//        this.password = new JPasswordField();
        this.getContentPane().add(this.password);

        String[] str = {"用户", "员工", "领导"};
        JPanel sex = new JPanel(new GridLayout(1, 3));
        ButtonGroup sex_bg = new ButtonGroup();
        this.radios = new JRadioButton[str.length];
        for (int i = 0; i < str.length; i++) {
            this.radios[i] = new JRadioButton(str[i]);
            sex_bg.add(this.radios[i]);
            sex.add(this.radios[i]);
        }
        this.radios[0].setSelected(true);
        this.getContentPane().add(new JLabel("身份"));

        this.getContentPane().add(sex);

        login = new JButton("登录");
        this.login.addActionListener(this);
        this.register = new JButton("注册");
        this.register.addActionListener(this);
        this.getContentPane().add(login);
        this.getContentPane().add(register);
        this.login.addActionListener(e -> {
            this.dispose();
        });

        this.setVisible(true);
    }


    public static void main(String[] args) {
        //然后应该要传回主函数，说明name
        new loginJFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //注册还是登录
        if (e.getSource() == this.register) {
            register_user register_user = new register_user();
            register_user.setVisible(true);
        } else if (e.getSource() == this.login) {
            //判断是用户还是员工

            //此处只能用root权限判定，当然也可以用游客权限
            db dbCon = new db(1);
            //用户登录，判断no和密码
            String name = new String(username.getText());
            String upd = new String(radios[0].isSelected() ? "0" : (radios[1].isSelected() ? "1" : "2"));
            String pass = new String(password.getPassword());


            if (upd.equals("0")) {
                String sql_pass = "select * from password where ID = (select No from user where ID = '" + name + "') and UPower ='" + upd + "' and Password ='" + pass + "'";

                String get = "";
                try {
                    ResultSet rs = dbCon.executeQuery(sql_pass);
                    while (rs.next()) {
                        get = rs.getString("ID");
                    }
//                    System.out.println(get);
                    rs.close();
                    dbCon.closeConn();
                } catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null, "登陆失败");
                }
                JOptionPane.showMessageDialog(null, "登陆成功");

                //显示界面
                userEntity = new userEntity(get);
                userApply userApply = null;
                try {
                    userApply = new userApply(get);
                } catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null, "登陆失败");
                }
                assert userApply != null;
                userApply.setVisible(true);
                dispose();
            } else if (upd.equals("1")) {
                String sql_pass = "select * from password where ID = '" + name + "' and UPower ='" + upd + "' and Password ='" + pass + "'";
                String get = "";
                try {
                    ResultSet rs = dbCon.executeQuery(sql_pass);
                    while (rs.next()) {
                        get = rs.getString("ID");
                    }

                    rs.close();
                    dbCon.closeConn();
                } catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null, "登陆失败");
                }

                employerJFrame employerJFrame = null;
                try {
                    employerJFrame = new employerJFrame(name);
                } catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null, "登陆失败");
                }
                assert employerJFrame != null;
                employerJFrame.setVisible(true);
            }
        } else JOptionPane.showMessageDialog(null, "失败");
    }
}
