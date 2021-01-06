package JFrame;

import Entity.houseExceptedEntity;
import db.MyTableModel;
import db.db;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class matchHouseJFrame extends JFrame {
    private JTable table;
    MyTableModel tableModel;

    public matchHouseJFrame(MyTableModel myTableModel) throws SQLException {
        super("相匹配的房子");
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);

        tableModel = myTableModel;
        table = new JTable(tableModel);

        table.setPreferredScrollableViewportSize(new Dimension(500, 250));
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
}
