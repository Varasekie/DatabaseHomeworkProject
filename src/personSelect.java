
import db.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class personSelect extends JFrame implements ActionListener {

    private JTable table;
    private MyTableModel tableModel;
    private JTextField select_text = new JTextField("00102");
    private JButton select_botton = new JButton("查询");

    public personSelect() throws SQLException {
        this.setSize(600, 300);
        this.setTitle("员工信息");

        this.setLocationRelativeTo(getOwner());
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        tableModel = getModel();
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 250));
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        this.select_botton.addActionListener(this);
        panel.add(this.select_botton);
        panel.add(this.select_text);
        this.getContentPane().add(panel,"South");
    }

    private MyTableModel getModel() throws SQLException {
        MyTableModel tableModel = new MyTableModel();
        db dbCon;
        dbCon = new db();
//        ResultSet rs = dbCon.executeQuery("select * from person");
        ResultSet rs = dbCon.executeQuery("select * from view_personDp");

        String[] str = {"号码", "名字", "性别", "生日", "职位", "部门名称"};
        ResultSetMetaData rsmd = rs.getMetaData();
        int column = rsmd.getColumnCount();
        int i;
        for (i = 0; i < column; i++) {
            tableModel.addColumn(str[i]);
        }

        ArrayList<PersonEntity> v = new ArrayList<PersonEntity>();
        while (rs.next()) {
            PersonEntity person = new PersonEntity();
            person.setNo(rs.getString("No"));
//            System.out.println(rs.getString("no"));
            person.setName(rs.getString("name"));
            person.setSex(rs.getString("sex"));
            person.setBirthday(rs.getDate("birthday"));
            person.setProfessor(rs.getString("professor"));
            person.setDeptNo(rs.getString("deptName"));
            v.add(person);
        }
//        System.out.println(v);
        rs.close();
        for (i = 0; i < v.size(); i++) {
            tableModel.addRow(new Object[]{
                    v.get(i).getNo(),
                    v.get(i).getName(),
                    v.get(i).getSex(),
                    v.get(i).getBirthday(),
                    v.get(i).getProfessor(),
                    v.get(i).getDeptNo(),
            });
        }
        dbCon.closeConn();
        return tableModel;
    }


    public static void main(String[] args) throws SQLException {
        personSelect w = new personSelect();
        w.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.select_botton){
            this.tableModel.setRowCount(0);

            String deptno = this.select_text.getText();
            db dbCon;
            dbCon = new db();
            try {
                ResultSet rs = dbCon.executeQuery("select * from person where DeptNo = '"+ deptno+"'");
                String[] str = {"号码", "名字", "  性别", "生日", "职位", "部门名称"};
                ResultSetMetaData rsmd = rs.getMetaData();
//                int column = rsmd.getColumnCount();
                int i;
//                for (i = 0; i < column; i++) {
//                    tableModel.addColumn(str[i]);
//                }

                ArrayList<PersonEntity> v = new ArrayList<PersonEntity>();
                while (rs.next()) {
                    PersonEntity person = new PersonEntity();
                    person.setNo(rs.getString("No"));
//            System.out.println(rs.getString("no"));
                    person.setName(rs.getString("name"));
                    person.setSex(rs.getString("sex"));
                    person.setBirthday(rs.getDate("birthday"));
                    person.setProfessor(rs.getString("professor"));
                    person.setDeptNo(rs.getString("deptNo"));
                    v.add(person);
                }
//        System.out.println(v);
                rs.close();
                for (i = 0; i < v.size(); i++) {
                    tableModel.addRow(new Object[]{
                            v.get(i).getNo(),
                            v.get(i).getName(),
                            v.get(i).getSex(),
                            v.get(i).getBirthday(),
                            v.get(i).getProfessor(),
                            v.get(i).getDeptNo(),
                    });
                }
                dbCon.closeConn();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }
}
