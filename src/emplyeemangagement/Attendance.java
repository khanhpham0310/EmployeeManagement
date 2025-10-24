package emplyeemangagement;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import com.toedter.calendar.JDateChooser;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class Attendance extends JFrame implements ActionListener {

    JTable table;
    JDateChooser dcDate;
    JButton save, back;

    public Attendance() {
        setTitle("Employee Attendance");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 720);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 247, 250));
        setLayout(null);

        // ===== HEADER =====
        JLabel title = new JLabel("EMPLOYEE ATTENDANCE");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(44, 62, 80));
        title.setBounds(30, 12, 400, 30);
        add(title);

        // ===== DATE CHOOSER =====
        JLabel lblDate = new JLabel("Select Date:");
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDate.setBounds(30, 60, 100, 25);
        add(lblDate);

        dcDate = new JDateChooser();
        dcDate.setBounds(120, 60, 160, 25);
        add(dcDate);

        // ===== BUTTONS =====
        save = new JButton("Save Attendance");
        styleButton(save, new Color(46, 204, 113));
        save.setBounds(300, 60, 160, 28);
        save.addActionListener(this);
        add(save);

        back = new JButton("Back");
        styleButton(back, new Color(231, 76, 60));
        back.setBounds(480, 60, 100, 28);
        back.addActionListener(this);
        add(back);

        // ===== TABLE MODEL =====
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Employee ID", "Name", "Có mặt", "Vắng mặt", "Muộn"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 2) return Boolean.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 2;
            }
        };

        // ===== LOAD EMPLOYEES =====
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT emID, name FROM employee ORDER BY emID ASC");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("emID"),
                    rs.getString("name"),
                    false, false, false
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // ===== TABLE =====
        table = new JTable(model);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(189, 195, 199));
        table.setGridColor(new Color(220, 220, 220));
        table.setFocusable(false); // tránh nhảy checkbox khi rê chuột

        // ===== CHỈ CHO 1 CHECKBOX/ROW =====
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() != TableModelEvent.UPDATE) return;
                int row = e.getFirstRow();
                int col = e.getColumn();
                if (row < 0 || col < 2) return;

                Boolean val = (Boolean) model.getValueAt(row, col);
                if (val != null && val) {
                    for (int i = 2; i <= 4; i++) {
                        if (i != col) model.setValueAt(false, row, i);
                    }
                }
            }
        });

        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(20, 110, 900, 520);
        jsp.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(jsp);

        setVisible(true);
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();

        if (src == save) {
            if (dcDate.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please select a date.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.sql.Date sqlDate = new java.sql.Date(dcDate.getDate().getTime());
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            try {
                Conn conn = new Conn();
                PreparedStatement ps = conn.c.prepareStatement(
                    "INSERT INTO attendance (emID, date, status) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE status = VALUES(status)"
                );

                boolean any = false;
                for (int i = 0; i < model.getRowCount(); i++) {
                    String emID = model.getValueAt(i, 0).toString();
                    boolean present = Boolean.TRUE.equals(model.getValueAt(i, 2));
                    boolean absent = Boolean.TRUE.equals(model.getValueAt(i, 3));
                    boolean late = Boolean.TRUE.equals(model.getValueAt(i, 4));

                    String status = null;
                    if (present) status = "Present";
                    else if (absent) status = "Absent";
                    else if (late) status = "Late";

                    if (status != null) {
                        ps.setString(1, emID);
                        ps.setDate(2, sqlDate);
                        ps.setString(3, status);
                        ps.addBatch();
                        any = true;
                    }
                }

                if (!any) {
                    JOptionPane.showMessageDialog(this, "No attendance selected to save.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                ps.executeBatch();
                JOptionPane.showMessageDialog(this, "Attendance saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (src == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new Attendance();
    }
}
