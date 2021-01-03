package JFrame;

import db.db;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class addExpectJFrame extends JFrame implements ActionListener {
    JTextField[] textFields;
    protected JRadioButton[] radios;
    JButton cancel, add;
    JComboBox<String> province, city, region;
    static String[] provinces_string = {"江苏省", "北京市", "浙江", "安徽", "", "", ""};
    static String[][] cities_string = {{"南京市", "苏州市", "无锡市", "徐州市"}, {}, {"绍兴", "嘉兴", "杭州", "衢州"}, {"芜湖"}};
    String No;
    //要有不给区的限制
    static String[][][] regions_string = {{
            {"", "江宁区", "武侯区"},
            {"", "吴中区", "虎丘区", "相城区"},
            {"", "梁溪区", "新区", "湖滨区"},
            {"", "鼓楼区", "泉山区"}},
            {{"", "朝阳区", "海淀区"}},
            {{"", "柯桥区", "上虞区"}, {"", "南湖区", "秀洲区"}, {"", "滨江区", "江干区"}},
            {{"", "镜湖区", "繁昌区"}
            }};

    public addExpectJFrame(String No) {
        super("添加信息");
        //添加信号
        this.No = No;
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        String[] strings = {"编号", "最低价格", "最高价格", "最低面积", "最高面积"};
        this.setSize(600, 400);
        this.getContentPane().setLayout(new GridLayout(strings.length + 3, 2));
        //为了化简，这里就不做哪些功能了，太麻烦了，代码量又大,仅仅做故障处理
        textFields = new JTextField[strings.length];
        for (int i = 0; i < strings.length; i++) {
            this.getContentPane().add(new JLabel(strings[i]));
            this.getContentPane().add(this.textFields[i] = new JTextField());
        }
        this.textFields[0].setEditable(false);
        this.textFields[0].setText(String.valueOf((int) (Math.random() * 1000000)));

        //租还是卖，单选按钮,这里套了去年java课设代码
        String[] str = {"租", "卖"};
        JPanel sex = new JPanel(new GridLayout(1, 2));
        ButtonGroup sex_bg = new ButtonGroup();
        this.radios = new JRadioButton[str.length];
        for (int i = 0; i < str.length; i++) {
            this.radios[i] = new JRadioButton(str[i]);
            sex_bg.add(this.radios[i]);
            sex.add(this.radios[i]);
        }
        this.radios[0].setSelected(true);
        this.getContentPane().add(new JLabel("类型"));
        this.getContentPane().add(sex);
        //地区开始
        this.province = new JComboBox<>(provinces_string);
        this.province.addActionListener(this);
        //默认是第一个
        this.city = new JComboBox<>(cities_string[0]);
        this.region = new JComboBox<>(regions_string[0][0]);
        JPanel section = new JPanel(new GridLayout(1, 3));
        this.getContentPane().add(new JLabel("地段"));
        section.add(this.province);
        section.add(this.city);
        section.add(this.region);
        this.getContentPane().add(section);
        this.add = new JButton("添加");
        this.add.addActionListener(e -> {
            db db = new db();
            //这里就要看一下……应该有点不太一样
            String insert_sql = "insert into houseexpect(HouseExNo,Price_low,Price_high,Area_low,Area_high,Type,Section,EmployerId,UserId,Conditions" +
                    ") " +
                    "values (?,?,?,?,?,?,?,?,?,?)";
            int[] ints = new int[textFields.length];
            try {
                for (int i = 0; i < textFields.length; i++) {
                    ints[i] = Integer.parseInt(this.textFields[i].getText());
                }
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "输入数字有错误");
            }

            String type_string = this.radios[0].isSelected() ? "租" : "卖";
            String section_string = Objects.requireNonNull(this.province.getSelectedItem()).toString()
                    + Objects.requireNonNull(this.city.getSelectedItem()).toString()
                    + Objects.requireNonNull(this.region.getSelectedItem()).toString();
            //直接在这里判断，是否有不一样的
            try {
                PreparedStatement preparedStatement = db.preparedStatement(insert_sql);
                for (int i = 0; i < ints.length; i++) {
                    preparedStatement.setString(i + 1, "" + ints[i]);
                }
                preparedStatement.setString(6, type_string);
                preparedStatement.setString(7, section_string);
                preparedStatement.setString(8, "0001");
                preparedStatement.setString(9, this.No);
                preparedStatement.setString(10, "未处理");

                preparedStatement.addBatch();
                preparedStatement.executeBatch();

                JOptionPane.showMessageDialog(null, "插入成功");
                this.dispose();
                userApply userApply = new userApply(No);
                userApply.setVisible(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        this.cancel = new JButton("取消");
        this.cancel.addActionListener(e -> dispose());
        this.getContentPane().add(this.add);
        this.getContentPane().add(this.cancel);
        this.setVisible(true);


    }

    public static void main(String[] args) {
        new addExpectJFrame("351265");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.province) {
            int i = this.province.getSelectedIndex();
//            System.out.println(""+i);

            if (city != null && i != -1) {
                this.city.removeAllItems();
                for (int j = 0; j < cities_string[i].length; j++) {
                    this.city.addItem(cities_string[i][j]);
                }
            }


        }

        if (e.getSource() == this.city) {
            System.out.println("test");
            int i = this.province.getSelectedIndex();
            int j = this.city.getSelectedIndex();
//            System.out.println(""+i+j);
            if (region != null && j != -1 && i != -1) {
                this.region.removeAllItems();
                for (int k = 0; k < regions_string[i][j].length; k++) {
                    this.region.addItem(regions_string[i][j][k]);
                }
            }
        }

    }
}
