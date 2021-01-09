package JFrame;

import db.*;
import Entity.*;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//该界面为用户登录后，选择和自己期望相同的
public class userApply extends JFrame {
    private JTable table;
    JToolBar toolBar = new JToolBar();
    private final String No;
    MyTableModel tableModel;
    JButton add, update, delete;
    JButton[] buttons;
    int which = 0;//这个数值判断是判断是订单和房子

    public userApply(String no) throws SQLException {
        super("房产中介系统 欢迎用户"+no);
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.No = no;
        String[] menus = {"管理订单", "管理房源", "看房时间查询", "个人信息管理", "成交订单查询","房源浏览筛选"};
        this.buttons = new JButton[menus.length];
        for (int i = 0; i < menus.length; i++) {
            this.buttons[i] = new JButton(menus[i]);
            this.toolBar.add(buttons[i]);
        }

        //一个动态筛选

        //添加筛选内容

        //自己的订单期望
        this.buttons[0].addActionListener(e -> {
            which = 0;
            this.add.setVisible(true);
            this.delete.setVisible(true);
            this.update.setVisible(true);
            try {
                tableModel = getModel();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            table.setModel(tableModel);

        });
        //自己的房子
        this.buttons[1].addActionListener(e -> {
            which = 1;
            //显示可见
            this.add.setVisible(true);
            this.delete.setVisible(true);
            this.update.setVisible(true);
            try {
                tableModel = getModel();
            } catch (SQLException throwables) {
//                throwables.printStackTrace();
                JOptionPane.showMessageDialog(null, "获取数据失败");
            }
            table.setModel(tableModel);

        });
        //看房
        this.buttons[2].addActionListener(e -> {
            which = 2;
            //也可以更改时间，待批准，也可以不想看房了删掉这个记录（
            //但是不能增加信息，增加信息是员工做的
            this.add.setVisible(false);
            this.update.setVisible(false);
            this.delete.setVisible(false);
            try {
                tableModel = getModel();
            } catch (SQLException throwables) {
//                JOptionPane.showMessageDialog(null, "获取数据失败");
                throwables.printStackTrace();
            }
            table.setModel(tableModel);
        });
        //个人信息
        this.buttons[3].addActionListener(e -> {
            which = 3;
            this.add.setVisible(false);
            this.update.setVisible(true);
            this.delete.setVisible(false);
            try {
                tableModel = getModel();
            } catch (SQLException throwables) {
//                throwables.printStackTrace();
                JOptionPane.showMessageDialog(null, "获取数据失败");
            }
            table.setModel(tableModel);
        });
        //成交情况管理
        this.buttons[4].addActionListener(e -> {
            which = 4;
            this.add.setVisible(false);
            this.update.setVisible(false);
            this.delete.setVisible(false);
            try {
                tableModel = getModel();
            } catch (SQLException throwables) {
//                throwables.printStackTrace();
                JOptionPane.showMessageDialog(null, "获取数据失败");

            }
            table.setModel(tableModel);
        });
        //房源浏览筛选
        this.buttons[5].addActionListener(e->{


            selectHousesJFrame selectHousesJFrame = null;
            try {
                selectHousesJFrame = new selectHousesJFrame(No);
            } catch (SQLException throwables) {
//                throwables.printStackTrace();
            }
            selectHousesJFrame.setVisible(true);

        });
        this.getContentPane().add(this.toolBar, "North");


        tableModel = getModel();
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 250));
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);


        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        jPanel.add(this.add = new JButton("添加"));
        jPanel.add(this.update = new JButton("更新"));
        jPanel.add(this.delete = new JButton("删除"));

        //添加项目，要记得刷新
        this.add.addActionListener(e -> {
            dispose();
            if (which == 0) {
                addExpectJFrame addExpectJFrame = new addExpectJFrame(No);
                addExpectJFrame.setVisible(true);

            } else if (which == 1) {
                addHouseJFrame addHouseJFrame = new addHouseJFrame(this.No);
                addHouseJFrame.setVisible(true);
            }
            try {
                tableModel = getModel();
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "获取数据失败");
            }
            table.setModel(tableModel);
        });

        //更新，更新是不需要确认的，删除需要（但是也可以加一个
        //还是加一个确认吧x不然看不出来更新了哪一个
        this.update.addActionListener(e -> {
            //第一部分是更新订单
            if (which == 0) {
                int i, index = 0, count;
                db db = new db();
                if (table.getCellEditor() != null) {
                    table.getCellEditor().stopCellEditing();
                }

                String update_sql = "update houseexpect set Type =? ,Price_low = ? ,Price_high = ? ," +
                        "Section = ?,Area_low = ?," +
                        "Area_high = ? where HouseExNo = ?";
                if (table.getSelectedRows().length > 0) {
                    //获得选中行的序列

                    StringBuilder confirm = new StringBuilder();

                    count = tableModel.getEditedIndex().size();
                    if (count > 0) {
                        for (i = 0; i < count; i++) {
                            index = tableModel.getEditedIndex().get(i);
                            confirm.append("{");
                            for (int k = 0; k < table.getColumnCount(); k++) {
                                confirm.append(table.getColumnName(k)).append(table.getValueAt(index, k)).append("\n");
                            }
                            confirm.append("}");
                        }
                    }
                    int n = JOptionPane.showConfirmDialog(null, "确认修改为" + confirm);
                    if (n == 0) {
                        try {
                            PreparedStatement presta = db.preparedStatement(update_sql);
                            //获得修改的行数
                            if (count > 0) {
                                for (i = 0; i < count; i++) {
                                    index = tableModel.getEditedIndex().get(i);
                                    presta.setString(1, (String) table.getValueAt(index, 1));
                                    presta.setString(2, (String) table.getValueAt(index, 2));
                                    presta.setString(3, (String) table.getValueAt(index, 3));
                                    presta.setString(4, (String) table.getValueAt(index, 4));
                                    presta.setString(5, (String) table.getValueAt(index, 5));
                                    presta.setString(6, (String) table.getValueAt(index, 6));
                                    presta.setString(7, (String) table.getValueAt(index, 0));
                                    presta.addBatch();
                                }
                                presta.executeBatch();
                                tableModel.clearEditedIndex();
                            }

                            JOptionPane.showMessageDialog(null, "更新成功");
                        } catch (SQLException throwables) {
                            JOptionPane.showMessageDialog(null, "更新失败");
                        }
                    }
                }
            }
            //第二部分是更新房屋信息
            else if (which == 1) {
                int i, index = 0, count;
                db db = new db();
                if (table.getCellEditor() != null) {
                    table.getCellEditor().stopCellEditing();
                }

                StringBuilder confirm = new StringBuilder();

                count = tableModel.getEditedIndex().size();
                if (count > 0) {
                    for (i = 0; i < count; i++) {
                        index = tableModel.getEditedIndex().get(i);
                        confirm.append("{");
                        for (int k = 0; k < table.getColumnCount(); k++) {
                            confirm.append(table.getColumnName(k)).append(table.getValueAt(index, k)).append("\n");
                        }
                        confirm.append("}");
                    }
                }
                int n = JOptionPane.showConfirmDialog(null, "确认修改为" + confirm);

                String update_sql = "update house set Area = ? ,Location = ? ,Type =? ," +
                        " Price = ?," +
                        "Picture = ? ,Conditions = ? where HouseNo = ?";
                try {
                    PreparedStatement presta = db.preparedStatement(update_sql);
                    //获得修改的行数
                    count = tableModel.getEditedIndex().size();
                    if (count > 0) {
                        for (i = 0; i < count; i++) {
                            index = tableModel.getEditedIndex().get(i);
                            presta.setString(1, (String) table.getValueAt(index, 1));
                            presta.setString(2, (String) table.getValueAt(index, 2));
                            presta.setString(3, (String) table.getValueAt(index, 3));
                            presta.setString(4, (String) table.getValueAt(index, 4));
                            presta.setString(5, null);
                            presta.setString(6, "空闲");
                            presta.setString(7, (String) table.getValueAt(index, 0));
                            presta.addBatch();
                        }
                        presta.executeBatch();
                        tableModel.clearEditedIndex();
                    }
                    JOptionPane.showMessageDialog(null, "更新成功");
                } catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null, "更新失败");

                }
            }
            //第三部分更新个人信息,后期应该可以用这个，做个改身份证的话要备份什么的功能，现在就先不做了
            //如果要使用更新身份证等敏感信息，可以做一个触发器的
            else if (which == 3) {
                int i, index = 0, count;
                db db = new db();
                if (table.getCellEditor() != null) {
                    table.getCellEditor().stopCellEditing();
                }

                StringBuilder confirm = new StringBuilder();

                count = tableModel.getEditedIndex().size();
                if (count > 0) {
                    for (i = 0; i < count; i++) {
                        index = tableModel.getEditedIndex().get(i);
                        confirm.append("{");
                        for (int k = 0; k < table.getColumnCount(); k++) {
                            confirm.append(table.getColumnName(k)).append(table.getValueAt(index, k)).append("\n");
                        }
                        confirm.append("}");
                    }
                }
                int n = JOptionPane.showConfirmDialog(null, "确认修改为" + confirm);
                String update_sql = "update user set tele = ? where No = '" + No + "'";
                try {
                    PreparedStatement presta = db.preparedStatement(update_sql);
                    //获得修改的行数
                    count = tableModel.getEditedIndex().size();
                    if (count > 0) {
                        for (i = 0; i < count; i++) {
                            index = tableModel.getEditedIndex().get(i);
                            presta.setString(1, (String) table.getValueAt(index, 1));
                            presta.addBatch();
                        }
                        presta.executeBatch();
                        tableModel.clearEditedIndex();
                    }
                    JOptionPane.showMessageDialog(null, "更新成功");
                } catch (SQLException throwables) {
                    JOptionPane.showMessageDialog(null, "更新失败");
                }
            }

        });

        //删除
        this.delete.addActionListener(e -> {
            //订单
            if (which == 0) {
                db db = new db();
                if (table.getSelectedRows().length > 0) {
                    //获得选中行的序列
                    int[] selRowIndexes = table.getSelectedRows();
                    StringBuilder confirm = new StringBuilder();

                    for (int rowIndex : selRowIndexes) {
                        confirm.append("{");
                        for (int k = 0; k < table.getColumnCount(); k++) {
                            confirm.append(table.getColumnName(k)).append(table.getValueAt(rowIndex, k)).append("\n");
                        }
                        confirm.append("}");
                    }

                    int n = JOptionPane.showConfirmDialog(null, "确认删除" + confirm);
                    if (n == 0) {
                        try {
                            PreparedStatement preparedStatement = db.preparedStatement("delete from houseexpect where HouseExNo = ?");
                            for (int selRowIndex : selRowIndexes) {
                                preparedStatement.setString(1, table.getValueAt(selRowIndex, 0).toString());
                                preparedStatement.addBatch();
                            }
                            preparedStatement.executeBatch();
                            tableModel = getModel();
                            table.setModel(tableModel);
                        } catch (SQLException throwables) {
//                            throwables.printStackTrace();
                            JOptionPane.showMessageDialog(null, "删除失败");
                        }
                    }
                }

            }
            //房源
            else if (which == 1) {
                    db db = new db();
                    if (table.getSelectedRows().length > 0) {
                        //获得选中行的序列
                        int[] selRowIndexes = table.getSelectedRows();
                        StringBuilder confirm = new StringBuilder();
                        try {
                            for (int rowIndex : selRowIndexes) {
                                confirm.append("{");
                                for (int k = 0; k < table.getColumnCount(); k++) {
                                    confirm.append(table.getColumnName(k)).append(table.getValueAt(rowIndex, k)).append("\n");
                                }
                                confirm.append("}");
                            }

                            int n = JOptionPane.showConfirmDialog(null, "确认删除" + confirm);
                            //如果n为确定，就删除
                            if (n == 0) {
                                PreparedStatement preparedStatement = db.preparedStatement("delete from house where HouseNo = ?");
                                for (int selRowIndex : selRowIndexes) {
                                    preparedStatement.setString(1, table.getValueAt(selRowIndex, 0).toString());
                                    preparedStatement.addBatch();
                                }
                                preparedStatement.executeBatch();
                            }
                            tableModel = getModel();
                            table.setModel(tableModel);
                            JOptionPane.showMessageDialog(null, "成功删除");

                        } catch (SQLException throwables) {
//                        throwables.printStackTrace();
                            JOptionPane.showMessageDialog(null, "删除失败");

                        }
                    }
                }

        });
        this.getContentPane().add(jPanel, "South");
        this.setVisible(true);
    }

    private MyTableModel getModel() throws SQLException {
        MyTableModel tableModel = new MyTableModel();
        db dbCon;
        dbCon = new db();

        //这里是订单
        if (which == 0) {
            int i;
            ResultSet rs = dbCon.executeQuery("select * from houseexpect where userId = '" + No + "'");
            String[] str = {"编号", "属性", "最低价格", "最高价格", "地段", "最低面积", "最高面积", "状态"};

            for (i = 0; i < str.length; i++) {
                tableModel.addColumn(str[i]);
            }

            //展示自己期望
            ArrayList<houseExceptedEntity> v = new ArrayList<houseExceptedEntity>();
            while (rs.next()) {
                //userid，employerID不用在表格中体现
                //容错
                houseExceptedEntity houseExceptedEntity = new houseExceptedEntity();
                houseExceptedEntity.setNo(rs.getString("HouseExNo"));
                houseExceptedEntity.setType(rs.getString("Type"));
                houseExceptedEntity.setPrice_low(rs.getString("Price_low"));
                houseExceptedEntity.setPrice_high(rs.getString("Price_high").equals("0") ? "无上限" : rs.getString("Price_high"));
                houseExceptedEntity.setSection(rs.getString("Section"));
                houseExceptedEntity.setArea_low(rs.getString("Area_low"));
                houseExceptedEntity.setArea_high(rs.getString("Area_high").equals("0") ? "无上限" : rs.getString("Area_high"));
                houseExceptedEntity.setConditions(rs.getString("Conditions"));
                v.add(houseExceptedEntity);
            }
            rs.close();
            for (i = 0; i < v.size(); i++) {
                tableModel.addRow(new Object[]{
                        v.get(i).getNo(),
                        v.get(i).getType(),
                        v.get(i).getPrice_low(),
                        v.get(i).getPrice_high(),
                        v.get(i).getSection(),
                        v.get(i).getArea_low(),
                        v.get(i).getArea_high(),
                        v.get(i).getConditions()
                });
            }
            dbCon.closeConn();
            return tableModel;
        }
        //这里是房子拥有的管理
        else if (which == 1) {
            int i;
            ResultSet resultSet = dbCon.executeQuery("select * from house where ownerId = '" + No + "'");
            String[] str = {"编号", "面积", "地段", "类型", "价格", "管理人"};

            for (i = 0; i < str.length; i++) {
                tableModel.addColumn(str[i]);
            }

            ArrayList<houseEntity> v = new ArrayList<houseEntity>();
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
            for (i = 0; i < v.size(); i++) {
                tableModel.addRow(new Object[]{
                        v.get(i).getNo(),
                        v.get(i).getArea(),
                        v.get(i).getLocation(),
                        v.get(i).getType(),
                        v.get(i).getPrice(),
                        v.get(i).getEmployId(),
                        v.get(i).getCondition()
                });
            }
            dbCon.closeConn();
            return tableModel;
        }
        //这里是预约的查询
        else if (which == 2) {
            //这里是看房时间
            int i;
            ResultSet resultSet = dbCon.executeQuery("select * from inspect_view where UserNo = '" + No + "'");
            //这里状态就自动生成吧（……
            //但是可以用存储过程欸x
            //这里状态先删掉x
            String[] str_inspction = {"用户姓名", "房子地址", "看房时间", "交接人姓名", "交接人电话"};

            for (i = 0; i < str_inspction.length; i++) {
                tableModel.addColumn(str_inspction[i]);
            }

            //订单时间
            ArrayList<inspectionEntity> v = new ArrayList<inspectionEntity>();
            while (resultSet.next()) {
                inspectionEntity inspectionEntity = new inspectionEntity();
                inspectionEntity.setEmployerName(resultSet.getString("employerName"));
                inspectionEntity.setEmployerTele(resultSet.getString("employerTele"));
                inspectionEntity.setLocation(resultSet.getString("houseLocation"));
                inspectionEntity.setTime(resultSet.getString("time"));
                inspectionEntity.setUserName(resultSet.getString("userName"));
                v.add(inspectionEntity);
            }
            resultSet.close();
            for (i = 0; i < v.size(); i++) {
                tableModel.addRow(new Object[]{
                        v.get(i).getUserName(),
                        v.get(i).getLocation(),
                        v.get(i).getTime(),
                        v.get(i).getEmployerName(),
                        v.get(i).getEmployerTele()
                });
            }
            dbCon.closeConn();
            return tableModel;
        }
        //这里是个人信息管理
        else if (which == 3) {
            int i;
            ResultSet resultSet = dbCon.executeQuery("select * from user where No = '" + No + "'");

            //身份证号不支持修改
            String[] str_personal = {"姓名", "电话"};
            for (i = 0; i < str_personal.length; i++) {
                tableModel.addColumn(str_personal[i]);
            }

            //自己个人信息
            ArrayList<userEntity> v = new ArrayList<userEntity>();
            while (resultSet.next()) {
                userEntity userEntity = new userEntity();
                userEntity.setTele(resultSet.getString("Tele"));
                userEntity.setName(resultSet.getString("Name"));
                v.add(userEntity);
            }
            resultSet.close();
            for (i = 0; i < v.size(); i++) {
                tableModel.addRow(new Object[]{
                        v.get(i).getName(),
                        v.get(i).getTele()
                });
            }
            dbCon.closeConn();
            return tableModel;
        }
        //这里是已经成交订单查询
        else {
            int i;
            ResultSet resultSet = dbCon.executeQuery("select * from dealinformation_view where BuyerNo = '" + No + "'");

            //身份证号不支持修改
            //应该看不到房主另一方，因为是和中介签和议
            String[] str_personal = {"房子地址", "中介人姓名", "成交时间", "成交价格"};
            for (i = 0; i < str_personal.length; i++) {
                tableModel.addColumn(str_personal[i]);
            }

            //自己个人信息
            ArrayList<dealEntity> v = new ArrayList<dealEntity>();
            while (resultSet.next()) {
                dealEntity dealEntity = new dealEntity();
                dealEntity.setContract(resultSet.getString("contract"));
                dealEntity.setDate(resultSet.getDate("Time"));
                dealEntity.setEmployId(resultSet.getString("EmployerID"));
                dealEntity.setHouseLocation(resultSet.getString("houseLocation"));
                dealEntity.setHouseId(resultSet.getString("HouseNo"));
                System.out.println(resultSet.getString("HouseNo"));
                dealEntity.setPrice(resultSet.getString("Price"));
                dealEntity.setEmployerName(resultSet.getString("EmployerName"));
                v.add(dealEntity);
            }
            resultSet.close();
            for (i = 0; i < v.size(); i++) {
                tableModel.addRow(new Object[]{
                        v.get(i).getHouseLocation(),
                        v.get(i).getEmployerName(),
                        v.get(i).getDate().toString(),
                        v.get(i).getPrice()
                });
            }
            dbCon.closeConn();
            return tableModel;
        }

    }


    public static void main(String[] args) throws SQLException {
        new userApply("244201");
    }
}
