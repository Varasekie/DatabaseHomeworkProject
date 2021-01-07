package JFrame;

import Entity.*;
import db.MyTableModel;
import db.db;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class selectHousesJFrame extends JFrame implements ActionListener {
    private JTable table;
    MyTableModel tableModel;
    String user_inneedId;
    //原本为textFields，现在全为下拉框
    JComboBox[] jComboBoxes;
    JTextField[] textFields;
    protected JRadioButton[] radios;
    JButton add, cancel,insepct;
    static String[] provinces_string = {"江苏省", "北京市", "浙江省", "安徽省", "", "", ""};
    static String[][] cities_string = {{"南京市", "苏州市", "无锡市", "徐州市"}, {}, {"绍兴市", "嘉兴市", "杭州市", "衢州市"}, {"芜湖市"}};
    JComboBox<String> province, city;



    public selectHousesJFrame(String No) throws SQLException {
        super("筛选信息");
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        //接下来是筛选面板
        String[] strings = {"最低价格", "最高价格", "最低面积", "最高面积"};
//        this.getContentPane().setLayout(new GridLayout(strings.length + 3, 2));
        JPanel panel = new JPanel(new GridLayout(strings.length + 2, 2));

        //为了化简，这里就不做哪些功能了，太麻烦了，代码量又大,仅仅做故障处理
//        textFields = new JTextField[strings.length];
        jComboBoxes = new JComboBox[strings.length];
        for (int i = 0; i < strings.length; i++) {
            panel.add(new JLabel(strings[i]));
            panel.add(this.jComboBoxes[i] = new JComboBox());
        }

        //下面添加内容
        //开始梯度为100,之后为

        for (int i = 0; i < jComboBoxes.length; i++) {
            this.jComboBoxes[i].addItem("无要求");
        }
        for (int i = 0; i < 1000; i += 100) {
            this.jComboBoxes[0].addItem("" + i);
            this.jComboBoxes[1].addItem("" + i);
        }

        for (int i = 1000; i < 10000; i += 1000) {
            this.jComboBoxes[0].addItem("" + i);
            this.jComboBoxes[1].addItem("" + i);
        }

        //接下来是对面积，从0到1000不等
        for (int i = 0; i < 100; i += 10) {
            this.jComboBoxes[2].addItem("" + i);
            this.jComboBoxes[3].addItem("" + i);
        }
        for (int i = 100; i < 1000; i += 100) {
            this.jComboBoxes[2].addItem("" + i);
            this.jComboBoxes[3].addItem("" + i);
        }
//        this.textFields[0].setEditable(false);
//        this.textFields[0].setText(String.valueOf((int) (Math.random() * 1000000)));

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
        panel.add(new JLabel("类型"));
        panel.add(sex);
        //地区开始
        this.province = new JComboBox<>(provinces_string);
        this.province.addActionListener(this);
        //默认是第一个
        this.city = new JComboBox<>(cities_string[0]);

        JPanel section = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("地段"));
        section.add(this.province);
        section.add(this.city);
        panel.add(section);
        this.getContentPane().add(panel, "West");
        tableModel = new MyTableModel();
        table = new JTable(tableModel);

        table.setPreferredScrollableViewportSize(new Dimension(500, 250));
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        this.add = new JButton("查询");
        this.add.addActionListener(e -> {

            try {
                this.tableModel = getModel();
                this.table.setModel(tableModel);
            } catch (SQLException throwables) {
//                throwables.printStackTrace();
            }
        });
        //客户也可以增加需求

        this.insepct = new JButton("看房预约");
        this.insepct.addActionListener(e->{
            //选择预约时间
            //时间面板

        });
        this.cancel = new JButton("取消");
        this.cancel.addActionListener(e -> {
            this.dispose();
        });
        JPanel jPanel = new JPanel(new GridLayout(1, 3));
        jPanel.add(this.add);
        jPanel.add(this.insepct);
        jPanel.add(this.cancel);
        this.getContentPane().add(jPanel, "South");
    }

    //需要一个getmodel
    private MyTableModel getModel() throws SQLException {
        MyTableModel tableModel = new MyTableModel();
        db db;
        db = new db(1);

        //这里是房子拥有的管理
        try {
            //筛选

            String low_price = this.jComboBoxes[0].getSelectedItem().toString();
            String high_price = this.jComboBoxes[1].getSelectedItem().toString();
            String low_area = this.jComboBoxes[2].getSelectedItem().toString();
            String high_area = this.jComboBoxes[3].getSelectedItem().toString();

            System.out.println(low_price);
            System.out.println(high_area);
            System.out.println(high_price);
            System.out.println(low_area);
            String type = this.radios[0].isSelected() ? "租" : "卖";
            String location = this.province.getSelectedItem().toString() + this.city.getSelectedItem().toString();
            System.out.println(type);
            System.out.println(location);

            String sql = "select *  from house where conditions = '空闲' " +
                    "and price >='" + (low_price.equals("无要求") ? "0" : (String) this.jComboBoxes[0].getSelectedItem()) +
                    "' and price <= '" + (high_price.equals("无要求") ? "99999" : (String) this.jComboBoxes[1].getSelectedItem()) +
                    "' and Location like '" + location +
                    "%' and area >= '" + (low_area.equals("无要求") ? "0" : (String) this.jComboBoxes[2].getSelectedItem()) +
                    "' and area <= '" + (high_area.equals("无要求") ? "99999" : (String) this.jComboBoxes[3].getSelectedItem()) +
                    "' and type = '" + type + "'";
            ResultSet resultSet = db.executeQuery(sql);

            String[] str = {"房子编号", "面积", "地段", "类型", "价格"};

            for (String s : str) {
                tableModel.addColumn(s);
            }


            ArrayList<houseEntity> v = new ArrayList<houseEntity>();
//            System.out.println(resultSet == null);
            while (resultSet.next()) {
                houseEntity houseEntity = new houseEntity();
                houseEntity.setNo(resultSet.getString("HouseNo"));
                houseEntity.setArea(resultSet.getString("Area"));
                houseEntity.setType(resultSet.getString("Type"));
                houseEntity.setCondition(resultSet.getString("Conditions"));
                houseEntity.setLocation(resultSet.getString("Location"));
                houseEntity.setPrice(resultSet.getString("Price"));
                houseEntity.setEmployId(resultSet.getString("EmployerId"));
                v.add(houseEntity);
            }
            resultSet.close();

            for (Entity.houseEntity houseEntity : v) {
                tableModel.addRow(new Object[]{
                        houseEntity.getNo(),
                        houseEntity.getArea(),
                        houseEntity.getLocation().substring(0,6),
                        houseEntity.getType(),
                        houseEntity.getPrice(),

                });
            }
            db.closeConn();
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(null, "匹配失败");
//                            throwables.printStackTrace();
        }

        return tableModel;
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
    }
}
