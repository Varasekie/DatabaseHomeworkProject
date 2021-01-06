package JFrame;

import db.db;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class register_user extends JFrame implements ActionListener, FocusListener {

    private JTextField username, ID, name, tele;
    private JPasswordField password, passwordAgain;
    private JRadioButton male, female;
    private JButton register, cancel;
    private final JLabel userIdMesssage1;
    private final JLabel passwordMesssage;

    private final JLabel confirmPasswordMesssage;
    private final JLabel emailMessage2, phoneMessage1;

    public register_user() {
        super();
        this.setTitle("注册页面");
        this.setSize(580, 400);
        this.setLocationRelativeTo(getOwner());

        Container container = getContentPane();
        this.getContentPane().setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));


        JPanel panel = new JPanel(new GridLayout(7, 3));


        //自动生成
        panel.add(new JLabel("用户编号"));
        //一般是不会出错的……orz
        JLabel userIdMesssage2 = new JLabel("已注册");
        userIdMesssage2.setBounds(580, 125, 135, 20);
        userIdMesssage2.setVisible(false);
        panel.add(userIdMesssage2);
        this.username = new JTextField("" + (int) (Math.random() * 1000000));
        panel.add(username);


        panel.add(new JLabel("真实姓名"));
        //首先是错误信息提示
        userIdMesssage1 = new JLabel("用户名不能为空");
        userIdMesssage1.setBounds(250, 158, 135, 20);
        userIdMesssage1.setVisible(false);
        panel.add(userIdMesssage1);
        panel.add(name = new JTextField(10));

        //然后添加监视器
        //姓名规范
        this.name.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                String names = name.getText();
                if (names.equals("") || names.matches("[0-9]+")) {
                    userIdMesssage1.setVisible(true);
                    return;
                }
                userIdMesssage1.setVisible(false);
            }
        });

        //密码
        panel.add(new JLabel("密码"));
        password = new JPasswordField(10);
        passwordMesssage = new JLabel("密码不符合规范");
        passwordMesssage.setBounds(250, 223, 135, 20);
        passwordMesssage.setVisible(false);
        panel.add(passwordMesssage);
        panel.add(password);


        panel.add(new JLabel("重复密码"));
        passwordAgain = new JPasswordField(10);
        confirmPasswordMesssage = new JLabel("两次密码不一致");
        confirmPasswordMesssage.setBounds(250, 290, 135, 20);
        confirmPasswordMesssage.setVisible(false);
        panel.add(confirmPasswordMesssage);
        panel.add(passwordAgain);


        //密码输入
        password.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                String pass = new String(password.getPassword());
                if (pass.equals("") || pass.length() > 16 || pass.length() < 6) {
                    passwordMesssage.setVisible(true);
                    return;
                }
                passwordMesssage.setVisible(false);
            }
        });


        //重复密码
        passwordAgain.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                String pass = new String(password.getPassword());
                String passAgain = new String(passwordAgain.getPassword());
                if (!pass.equals(passAgain)) {
                    confirmPasswordMesssage.setVisible(true);
                    return;
                }
                confirmPasswordMesssage.setVisible(false);
            }
        });


        //性别
        panel.add(new JLabel("性别"));
        this.male = new JRadioButton("男");
        this.female = new JRadioButton("女");
        ButtonGroup bg = new ButtonGroup();
        bg.add(this.male);
        bg.add(this.female);

        panel.add(new JLabel(""));
        JPanel sex = new JPanel(new GridLayout(1, 2));
        sex.add(this.male);
        sex.add(this.female);
        panel.add(sex);

        //日期
        //直接写身份证
        panel.add(new JLabel("身份证号"));
        emailMessage2 = new JLabel("已注册");
        emailMessage2.setBounds(580, 455, 135, 20);
        emailMessage2.setVisible(false);
        panel.add(emailMessage2);
        //只是测试用,选了一个已存在的
        ID = new JTextField("110101199007134713");
        //new JTextField(10);
        ;

        //判断身份证号
        ID.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            //失去焦点时判断
            @Override
            public void focusLost(FocusEvent e) {
                String idText = ID.getText();
                if (idText.length() != 18 || !idText.matches("[0-9]+")) {
                    //这里其实可以更加规范一些的，因为最后一位可以是X，属于java正则表达式范围，不予考虑
                    JOptionPane.showMessageDialog(null, "身份证号码不符合规范");

                }

                //然后在判断是否数据库中已经有了这个身份证，有的话显示已经有了
                String sql_id = "select * from user where ID = '" + idText + "'";
                //暂时先都用root权限登录
                //后期这里改成了游客
                db dbCon = new db(1);
                try {
                    ResultSet rs = dbCon.executeQuery(sql_id);
                    if (rs.next()) {
                        //防止结果集为空报错
                        rs.last();
                        emailMessage2.setVisible(true);
                    } else emailMessage2.setVisible(false);
                    rs.close();
                    dbCon.closeConn();
                } catch (SQLException ignored) {

                }
            }
        });
        panel.add(ID);

        panel.add(new JLabel("电话号码"));
        tele = new JTextField("11111111111");
        //new JTextField(10);
        phoneMessage1 = new JLabel("手机号不符合规范");
        phoneMessage1.setBounds(250, 555, 150, 20);
        phoneMessage1.setVisible(false);
        panel.add(phoneMessage1);
        panel.add(tele);
        tele.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                String teles = tele.getText();
                if (teles.length() != 11 || !teles.matches("[0-9]+")) {
                    phoneMessage1.setVisible(true);
                    return;
                }
                phoneMessage1.setVisible(false);
            }
        });

        this.getContentPane().add(panel);

        JPanel cont2 = new JPanel(new GridLayout(1, 2));
        cont2.add(register = new JButton("注册"));
        register.addActionListener(this);
        cont2.add(cancel = new JButton("取消"));
        cancel.addActionListener(this);
        this.getContentPane().add(cont2);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //进行注册
        if (e.getSource() == this.register) {
            String no = username.getText();
            String pass = new String(password.getPassword());
            String passAgain = new String(passwordAgain.getPassword());
            String tele = this.tele.getText();
            //虽然代码有点乱，这个id是代表身份证号
            String sex = (male.isSelected() ? "男" : "女");
            String No = this.username.getText();
            //这一堆都是容错
            if (pass.equals(passAgain)) {
                String s;
                s = "用户名" + username.getText() + "\n";
                s += "密码" + pass + "\n";
                s += "性别" + (male.isSelected() ? male.getText() : female.getText()) + "\n";
                s += "身份证号" + (this.ID.getText());
                s += "电话" + (this.tele.getText());
                int value = JOptionPane.showConfirmDialog(null, s);
                if (value == JOptionPane.YES_OPTION) {
                    String sql_register_pass = "insert into password(ID,Password,Upower) " +
                            "Values (" + No + "," + pass + "," + 0 + ")";
                    String sql_register_user = "insert into user(No,Name,Sex,ID,Tele)" +
                            "Values (" + No + "," + name + "," + sex + "," + username + "," + tele + ")";

                    db dbCon = new db();
                    try {
                        //怎么插入来着……忘了
                        ResultSet rs = dbCon.executeQuery(sql_register_user);
                        ResultSet rs1 = dbCon.executeQuery(sql_register_pass);
                        if (rs.next()) {
                            //防止结果集为空报错
                            rs.last();
                        }
                        rs.close();
                        dbCon.closeConn();
                        this.dispose();
                        userApply userApply = new userApply(no);
                        userApply.setVisible(true);
                    } catch (SQLException ignored) {

                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "密码不一致");
            }
        }

        //退出系统
        if (e.getSource() == this.cancel) {
            this.dispose();
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

    }
}
