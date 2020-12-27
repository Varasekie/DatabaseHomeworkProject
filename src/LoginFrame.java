import db.db;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame implements ActionListener {
    private JTextField username;
    private JPasswordField password;
    private JButton login, register;
    private JRadioButton[] radios;

    public LoginFrame() {
        super();
        this.setSize(300, 200);
        this.setTitle("登录");
        this.setLocationRelativeTo(getOwner());

        this.setLayout(new GridLayout(4, 2));

        this.getContentPane().add(new JLabel("用户名"));
        this.username = new JTextField("root");
        this.getContentPane().add(this.username);

        this.getContentPane().add(new JLabel("密码"));
        this.password = new JPasswordField("root");
        this.getContentPane().add(this.password);

        String[] str = {"0", "1"};
//        JPanel sex = new JPanel(new GridLayout(1, 2));
        ButtonGroup sex_bg = new ButtonGroup();
        this.radios = new JRadioButton[str.length];
        for (int i = 0; i < str.length; i++) {
            this.radios[i] = new JRadioButton(str[i]);
            sex_bg.add(this.radios[i]);
            this.getContentPane().add(radios[i]);
//            sex.add(this.radios[i]);
        }
        this.radios[0].setSelected(true);
//        this.getContentPane().add();

        login = new JButton("login");
        this.login.addActionListener(this::actionPerformed);    
        this.register = new JButton("register");
        this.getContentPane().add(login);
        this.getContentPane().add(register);



        this.setVisible(true);
    }

    public static void main(String[] args) {
        new LoginFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        db dbCon = new db();
        String name = new String(username.getText());
        String upd = new String(radios[0].isSelected()?radios[0].getText():radios[1].getText());
        String sql_pass = "select * from user where Uname = '" + name + "'";
        String get_pass = "";
        String get_upd = "";
        try {
            ResultSet rs = dbCon.executeQuery(sql_pass);
            while (rs.next()) {
                get_pass= rs.getString("Upwd");
                get_upd= rs.getString("Upower");
//                System.out.println(get_pass);
            }

            rs.close();
            dbCon.closeConn();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(get_pass);
        String pass = new String(password.getPassword());

        if (pass.equals(get_pass) && upd.equals(get_upd) ) {
            JOptionPane.showMessageDialog(null, "登陆成功");

        }else             JOptionPane.showMessageDialog(null, "失败");


//        if (username.getText().equals("root") && pass.equals("123456")){
//            HelloWorld helloWorld = new HelloWorld();
//            helloWorld.setVisible(true);
//            dispose();
//        }else {
//            JOptionPane.showMessageDialog(null,"再次输入");
//        }
    }
}
