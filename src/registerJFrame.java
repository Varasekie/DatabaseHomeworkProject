import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class registerJFrame extends JFrame implements ActionListener {
    private JTextField username;
    private JPasswordField password, passwordAgain;
    private JRadioButton male, female;

    private JTextField year;
    private JComboBox<Integer> month, day;
    private JCheckBox f1, f2, f3;
    private JButton register, cancel;
    private JTextArea remmond;
    private JScrollPane scroll;


    public registerJFrame(){
        super();
        this.setTitle("注册页面");
        this.setSize(580, 400);
        this.setLocationRelativeTo(getOwner());

        Container container = getContentPane();
        this.getContentPane().setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("username"));
        this.username = new JTextField(10);
        panel.add(username);
        //密码
        panel.add(new JLabel("password"));
        password = new JPasswordField(10);
        panel.add(password);
        panel.add(new JLabel("password Again"));
        passwordAgain = new JPasswordField(10);
        panel.add(passwordAgain);

        //性别
        panel.add(new JLabel("sex"));
        this.male = new JRadioButton("male");
        this.female = new JRadioButton("female");
        ButtonGroup bg = new ButtonGroup();
        bg.add(this.male);
        bg.add(this.female);

        JPanel sex = new JPanel(new GridLayout(1, 2));
        sex.add(this.male);
        sex.add(this.female);
        panel.add(sex);

        //日期
        panel.add(new JLabel("birthday"));
        JPanel birth = new JPanel();
        this.year = new JTextField(10);
        this.month = new JComboBox<>();
        for (int i = 0; i < 12; i++) {
            month.addItem(i + 1);
        }
        this.day = new JComboBox<>();
        for (int i = 0; i < 31; i++) {
            day.addItem(i + 1);
        }
        birth.add(year);
        birth.add(month);

        birth.add(new JLabel("-"));
        birth.add(day);
        panel.add(birth);

        panel.add(new JLabel("hobby"));
        this.f1 = new JCheckBox("sport");
        this.f2 = new JCheckBox("film");
        this.f3 = new JCheckBox("music");

        JPanel fav = new JPanel();
        fav.add(f1);
        fav.add(f2);
        fav.add(f3);
        panel.add(fav);

        JPanel cont = new JPanel(new GridLayout(1,2));
        cont.add(new JLabel("简历"));
        remmond = new JTextArea(5,10);
        scroll = new JScrollPane(this.remmond);
        cont.add(scroll);

        this.getContentPane().add(panel);
        this.getContentPane().add(cont);

        JPanel cont2 = new JPanel(new GridLayout(1,2));
        cont2.add(register = new JButton("register"));
        register.addActionListener(this::actionPerformed);
        cont2.add(cancel = new JButton( "cancel"));
        cancel.addActionListener(this::actionPerformed);
        this.getContentPane().add(cont2);
        this.setVisible(true);


    }

    public static void main(String[] args) {
        new registerJFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("register")){
            String pass = new String(password.getPassword());
            String passAgain = new String(passwordAgain.getPassword());

            if (pass.equals(passAgain)){
                String s ;
                s = "用户名"+username.getText()+"\n";
                s+="密码"+pass+"\n";
                s+="性别"+(male.isSelected()?male.getText():female.getText()) +"\n";
                s+= "爱好"+(f1.isSelected()?f1.getText():(f2.isSelected()?f2.getText():f3.getText()))+"\n";
                s+= "简历" + remmond.getText();
                JOptionPane.showMessageDialog(null,s);
            }
        }else {
            JOptionPane.showMessageDialog(null,"密码不一致");
        }
    }
}
