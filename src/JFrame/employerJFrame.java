package JFrame;

import Entity.dealEntity;
import Entity.inspectionEntity;
import Entity.*;
import db.MyTableModel;
import db.db;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class employerJFrame extends JFrame {
    private JTable table;
    JToolBar toolBar = new JToolBar();
    JButton[] buttons;
    private String No;
    MyTableModel tableModel;
    JButton add, update, delete, match;

    int which = 1;

    public employerJFrame(String no) throws SQLException {
        super("管理员界面");
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.No = no;
        //工资查看可以给一个存储过程，根据完成了多少订单返回一个提成量
        //根据待处理订单，可以更改需求房子的订单
        //待处理订单，根据订单可以匹配房子，订单，点击匹配看到有多少合适的房子
        //工资查看，直接看自己完成了多少订单，触发器自动生成
        //看房预约，需要增加，删除，更新信息
        String[] menus = {"待处理订单", "成交管理", "看房预约", "工资查看", "个人信息更改","用户管理"};

        this.buttons = new JButton[menus.length];
        for (int i = 0; i < menus.length; i++) {
            this.buttons[i] = new JButton(menus[i]);
            this.toolBar.add(buttons[i]);
        }
        //待处理订单
        this.buttons[0].addActionListener(e -> {
            this.which = 0;
            this.add.setVisible(true);
            this.delete.setVisible(true);
            this.update.setVisible(true);
            this.match.setVisible(true);
            try {
                tableModel = getModel();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            assert tableModel != null;
            table.setModel(tableModel);

        });

        //成交管理
        this.buttons[1].addActionListener(e -> {
            this.which = 1;
            this.add.setVisible(true);
            this.delete.setVisible(true);
            this.update.setVisible(true);
            this.match.setVisible(false);

            try {
                tableModel = getModel();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            assert tableModel != null;
            table.setModel(tableModel);

        });

        //预约
        this.buttons[2].addActionListener(e -> {
            this.which = 0;
            this.add.setVisible(true);
            this.delete.setVisible(true);
            this.update.setVisible(true);
            this.match.setVisible(false);

            try {
                tableModel = getModel();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            assert tableModel != null;
            table.setModel(tableModel);
        });
        //工资查看
        //这个也只能查看（
        //只支持查看这个月的，由于没有建工资表（……
        this.buttons[3].addActionListener(e -> {
            this.add.setVisible(false);
            this.delete.setVisible(false);
            this.update.setVisible(false);
            this.match.setVisible(false);

            try {
                tableModel = getModel();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            assert tableModel != null;
            table.setModel(tableModel);
        });
        //个人信息更改
        //只能更改
        this.buttons[4].addActionListener(e -> {
            this.which = 4;
            this.add.setVisible(false);
            this.delete.setVisible(false);
            this.update.setVisible(true);
            this.match.setVisible(false);

            try {
                tableModel = getModel();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            assert tableModel != null;
            table.setModel(tableModel);

        });
        //
        this.buttons[5].addActionListener(e->{
            this.which = 5;
            this.add.setVisible(false);
            this.delete.setVisible(true);
            this.update.setVisible(false);
            this.match.setVisible(false);
            try {
                tableModel = getModel();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            assert tableModel != null;
            table.setModel(tableModel);
        });

        //这里是获取table
        tableModel = getModel();
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 250));
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        //然后要给每个menus添加事情
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        jPanel.add(this.add = new JButton("添加"));
        jPanel.add(this.update = new JButton("更新"));
        jPanel.add(this.delete = new JButton("删除"));
        jPanel.add(this.match = new JButton("匹配"));
        this.getContentPane().add(jPanel, "South");


        this.add.addActionListener(e -> {
            //管理员只能管理属于他的订单，查看是否有匹配的房子，
            //没有add订单

            //成交管理
            if (which == 1) {
                addDealJFrame addDealJFrame = new addDealJFrame(this.No);
                addDealJFrame.setVisible(true);
            }
            //添加看房预约
            if (which == 2) {
                addInspctionJFrame addInspctionJFrame = new addInspctionJFrame(this.No);
                addInspctionJFrame.setVisible(true);
            }

            //添加
            //工资和个人信息不支持添加

            //用户是自动添加的
        });
        //更新按钮
        this.update.addActionListener(e -> {
            //第一部分是更新房子期望（？这里其实不用更新，写个触发器吧，更新状态
            //如果有成交，房子状态变了，期望就是已处理

            //第二部分更新成交管理
            //订单不支持更新，因为时间和价格都是定好的在合同中，不需要更新
            //但是做了这个功能，可以更新时间和价格
            if (which == 1) {
                int i, index = 0, count;
                db db = new db();
                if (table.getCellEditor() != null) {
                    table.getCellEditor().stopCellEditing();
                }

                String update_sql = "update deal set Time = ? ,Price = ? where HouseNo = ? and No = ? and EmployerId = '" + this.No + "'"
                        ;
                try {
                    PreparedStatement presta = db.preparedStatement(update_sql);
                    //获得修改的行数
                    count = tableModel.getEditedIndex().size();
                    if (count > 0) {
                        for (i = 0; i < count; i++) {
                            index = tableModel.getEditedIndex().get(i);
                            presta.setString(1, (String) table.getValueAt(index, 2));
                            presta.setString(2, (String) table.getValueAt(index, 3));
                            presta.setString(3, (String) table.getValueAt(index, 0));
                            presta.setString(4, (String) table.getValueAt(index, 1));
                            presta.addBatch();
                        }

                        presta.executeBatch();
                        tableModel.clearEditedIndex();
                    }

                    JOptionPane.showMessageDialog(null, "更新成功");
                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
                    JOptionPane.showMessageDialog(null,"更新失败");
                }
            }
            //第三部分更新看房预约
            if (which == 2) {
                int i, index = 0, count;
                db db = new db();
                if (table.getCellEditor() != null) {
                    table.getCellEditor().stopCellEditing();
                }

                String update_sql = "update inspect_view set userName =? ,address = ? ,time = ? ," +
                        "employerName = ?,userTele = ?," +
                        "employerTele = ? where employerNo = ?";
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
//                    throwables.printStackTrace();
                    JOptionPane.showMessageDialog(null,"更新失败");

                }
            }
            //第四部分工资不支持更改

            //第五部分更新个人信息
            if (which == 4) {
                int i, index = 0, count;
                db db = new db();
                if (table.getCellEditor() != null) {
                    table.getCellEditor().stopCellEditing();
                }

                String update_sql = "update employer set name = ?,Tele = ? ,Sex = ?,ID = ? where EmployerId = '" + this.No + "'";
                try {
                    PreparedStatement presta = db.preparedStatement(update_sql);
                    //获得修改的行数
                    count = tableModel.getEditedIndex().size();
                    if (count > 0) {
                        for (i = 0; i < count; i++) {
                            index = tableModel.getEditedIndex().get(i);
                            presta.setString(1, (String) table.getValueAt(index, 0));
                            presta.setString(2, (String) table.getValueAt(index, 1));
                            presta.setString(3, (String) table.getValueAt(index, 2));
                            presta.setString(4, (String) table.getValueAt(index, 3));
                            presta.addBatch();
                        }
                        presta.executeBatch();
                        tableModel.clearEditedIndex();
                    }

                    JOptionPane.showMessageDialog(null, "更新成功");
                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
                    JOptionPane.showMessageDialog(null,"更新失败");

                }
            }
            try {
                tableModel = getModel();
            } catch (SQLException throwables) {
//                throwables.printStackTrace();
                JOptionPane.showMessageDialog(null,"获取数据失败");
            }
            assert tableModel != null;
            table.setModel(tableModel);
        });

        //删除
        this.delete.addActionListener(e -> {
            //删除待处理订单
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
                            PreparedStatement preparedStatement =
                                    db.preparedStatement("delete from houseexpect where HouseExNo = ?");
                            for (int selRowIndex : selRowIndexes) {
                                preparedStatement.setString(1, table.getValueAt(selRowIndex, 0).toString());
                                preparedStatement.addBatch();
                            }
                            preparedStatement.executeBatch();
                            tableModel = getModel();
                            JOptionPane.showMessageDialog(null,"成功删除");
                            assert tableModel != null;
                            table.setModel(tableModel);
                        } catch (SQLException throwables) {
//                            throwables.printStackTrace();
                            JOptionPane.showMessageDialog(null,"删除失败");
                        }
                    }
                }
            }
            //删除成交信息
            if (which == 1) {
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
                            PreparedStatement preparedStatement =
                                    db.preparedStatement("delete from deal where HouseNo = ? and EmployerId = ? and No = ? ");
                            for (int selRowIndex : selRowIndexes) {
                                preparedStatement.setString(1, table.getValueAt(selRowIndex, 0).toString());
                                preparedStatement.setString(2, this.No);
                                preparedStatement.setString(3, table.getValueAt(selRowIndex, 1).toString());
                                preparedStatement.addBatch();
                            }
                            preparedStatement.executeBatch();
                            JOptionPane.showMessageDialog(null,"成功删除");

                            tableModel = getModel();
                            assert tableModel != null;
                            table.setModel(tableModel);
                        } catch (SQLException throwables) {
//                            throwables.printStackTrace();
                            JOptionPane.showMessageDialog(null,"删除失败");

                        }
                    }
                }
            }
            //删除预约信息
            if (which == 2) {
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
                            PreparedStatement preparedStatement =
                                    db.preparedStatement("delete from inspection where HouseNo = ? and EmployerId = ? and UserNo = ? ");
                            for (int selRowIndex : selRowIndexes) {
                                preparedStatement.setString(1, table.getValueAt(selRowIndex, 0).toString());
                                preparedStatement.setString(2, this.No);
                                preparedStatement.setString(3, table.getValueAt(selRowIndex, 1).toString());
                                preparedStatement.addBatch();
                            }
                            preparedStatement.executeBatch();
                            JOptionPane.showMessageDialog(null,"成功删除");
                            tableModel = getModel();
                            assert tableModel != null;
                            table.setModel(tableModel);
                        } catch (SQLException throwables) {
//                            throwables.printStackTrace();
                            JOptionPane.showMessageDialog(null,"删除失败");

                        }
                    }
                }

            }

            //不能删除工资
            //不能删除个人信息
            //可以删除用户信息
            if (which == 5){
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
                            PreparedStatement preparedStatement =
                                    db.preparedStatement("delete from user where No = ?");
                            for (int selRowIndex : selRowIndexes) {
                                preparedStatement.setString(1, table.getValueAt(selRowIndex, 0).toString());
                                preparedStatement.addBatch();
                            }
                            preparedStatement.executeBatch();
                            JOptionPane.showMessageDialog(null,"成功删除");

                            tableModel = getModel();
                            assert tableModel != null;
                            table.setModel(tableModel);
                        } catch (SQLException throwables) {
                            JOptionPane.showMessageDialog(null,"删除失败");
//                            throwables.printStackTrace();
                        }
                    }
                }
            }

        });
        this.getContentPane().add(this.toolBar, "North");


        this.setVisible(true);
    }

    private MyTableModel getModel() throws SQLException {
        MyTableModel tableModel = new MyTableModel();
        db dbCon;
        dbCon = new db();
        //这里是对房子的期望，由谁来管，对于房子的期望应该可以弄个匹配

        if (which == 0) {
            int i;
            ResultSet rs = dbCon.executeQuery("select * from houseexpect where EmployerID = '" + No + "'");
            //中介应该能看到比较详细的内容(哎算了都一样，看到的都是那些而已
            String[] str_houseexpect = {"编号", "属性", "最低价格", "最高价格", "地段", "最低面积", "最高面积", "状态"};

            for (i = 0; i < str_houseexpect.length; i++) {
                tableModel.addColumn(str_houseexpect[i]);
            }

            ArrayList<houseExceptedEntity> v = new ArrayList<houseExceptedEntity>();
            while (rs.next()) {
                houseExceptedEntity houseExceptedEntity = new houseExceptedEntity();
                houseExceptedEntity.setNo(rs.getString("HouseExNo"));
                houseExceptedEntity.setType(rs.getString("Type"));
                houseExceptedEntity.setPrice_low(rs.getString("Price_low"));
                houseExceptedEntity.setPrice_high(rs.getString("Price_high").equals("") ? "无上限" : rs.getString("Price_high"));
                houseExceptedEntity.setSection(rs.getString("Section"));
                houseExceptedEntity.setArea_low(rs.getString("Area_low"));
                houseExceptedEntity.setArea_high(rs.getString("Area_high").equals("") ? "无上限" : rs.getString("Area_high"));
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
        //这个是成交
        if (which == 1) {
            int i;
            ResultSet resultSet = dbCon.executeQuery("select * from deal where employerID = '" + No + "'");
            //中介应该能看到比较详细的内容(哎算了都一样，看到的都是那些而已
            //这里换了一个
            String[] str_personal = {"房子编码", "用户编号", "成交时间", "成交价格"};
            for (i = 0; i < str_personal.length; i++) {
                tableModel.addColumn(str_personal[i]);
            }

            //自己个人信息
            ArrayList<dealEntity> v = new ArrayList<dealEntity>();
            while (resultSet.next()) {
                dealEntity dealEntity = new dealEntity();
                dealEntity.setContract(resultSet.getString("contract"));
                dealEntity.setDate(resultSet.getDate("Time"));
                dealEntity.setNo(resultSet.getString("No"));
                dealEntity.setEmployId(resultSet.getString("EmployerID"));
                dealEntity.setHouseId(resultSet.getString("HouseNo"));
                dealEntity.setPrice(resultSet.getString("Price"));
                v.add(dealEntity);
            }
            resultSet.close();
            for (i = 0; i < v.size(); i++) {
                tableModel.addRow(new Object[]{
                        v.get(i).getHouseId(),
                        v.get(i).getNo(),
                        v.get(i).getDate().toString(),
                        v.get(i).getPrice(),
                });
            }
            dbCon.closeConn();
            return tableModel;
        }
        //预约信息的获取,看房时间
        if (which == 2) {
            int i;
            ResultSet resultSet = dbCon.executeQuery("select * from inspect_view where employerNo = '" + No + "'");
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
                inspectionEntity.setLocation(resultSet.getString("address"));
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

        //查看自己的工资
        //利用存储过程，获得一个返回值（……？

        //这个是个人信息更改
        if (which == 4) {
            int i;
            ResultSet resultSet = dbCon.executeQuery("select * from employer where EmployerId = '" + No + "'");
            //身份证号不支持修改
            String[] str_personal = {"姓名", "电话", "性别", "身份证号"};
            for (i = 0; i < str_personal.length; i++) {
                tableModel.addColumn(str_personal[i]);
            }

            //自己个人信息
            ArrayList<employerEntity> v = new ArrayList<employerEntity>();
            while (resultSet.next()) {
                employerEntity employerEntity = new employerEntity(this.No);
                employerEntity.setNo(this.No);
                employerEntity.setSex(resultSet.getString("Sex"));
                employerEntity.setName(resultSet.getString("name"));
                employerEntity.setTele(resultSet.getString("tele"));
                employerEntity.setIDCard(resultSet.getString("ID"));
                v.add(employerEntity);
            }
            resultSet.close();
            for (i = 0; i < v.size(); i++) {
                tableModel.addRow(new Object[]{
                        v.get(i).getName(),
                        v.get(i).getTele(),
                        v.get(i).getSex(),
                        v.get(i).getIDCard()
                });
            }
            dbCon.closeConn();
            return tableModel;
        }

        //员工能对所有普通用户进行删除操作（比如成员违规
        if (which == 5){
            int i;
            ResultSet resultSet = dbCon.executeQuery("select * from user");
            //中介应该能看到比较详细的内容(哎算了都一样，看到的都是那些而已
            //这里换了一个
            String[] str_personal = {"编码", "姓名", "性别", "身份证号","电话"};
            for (i = 0; i < str_personal.length; i++) {
                tableModel.addColumn(str_personal[i]);
            }

            //自己个人信息
            ArrayList<userEntity> v = new ArrayList<userEntity>();
            while (resultSet.next()) {
                userEntity userEntity = new userEntity();
                userEntity.setName(resultSet.getString("Name"));
                userEntity.setNo(resultSet.getString("No"));
                userEntity.setSex(resultSet.getString("Sex"));
                userEntity.setIDCard(resultSet.getString("ID"));
                userEntity.setTele(resultSet.getString("Tele"));

                v.add(userEntity);
            }
            resultSet.close();
            for (i = 0; i < v.size(); i++) {
                tableModel.addRow(new Object[]{
                        v.get(i).getNo(),
                        v.get(i).getName(),
                        v.get(i).getSex(),
                        v.get(i).getIDCard(),
                        v.get(i).getTele()
                });
            }
            dbCon.closeConn();
            return tableModel;
        }
        return null;
    }


    public static void main(String[] args) throws SQLException {
        new employerJFrame("0004");
    }
}
