package emplyeemangagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Remove extends JFrame implements ActionListener {
    Choice cEmpId;
    JButton delete, back;
    JLabel lblname, lblphone, lblemail;

    Remove() {
        // M�u n?n tr?ng
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Ti�u d?
        JLabel heading = new JLabel("Remove Employee");
        heading.setBounds(330, 20, 400, 40);
        heading.setFont(new Font("SansSerif", Font.BOLD, 28));
        heading.setForeground(new Color(30, 30, 30));
        add(heading);

        // Label ch?n ID
        JLabel labelempId = new JLabel("Select Employee ID:");
        labelempId.setBounds(100, 100, 180, 30);
        labelempId.setFont(new Font("SansSerif", Font.PLAIN, 18));
        add(labelempId);

        // Dropdown ch?n ID
        cEmpId = new Choice();
        cEmpId.setBounds(300, 100, 200, 30);
        add(cEmpId);

        // Load danh s�ch nh�n vi�n
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee";
            ResultSet rs = c.s.executeQuery(query);
            while (rs.next()) {
                cEmpId.add(rs.getString("emID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Label & th�ng tin nh�n vi�n
        JLabel labelname = new JLabel("Name:");
        labelname.setBounds(100, 160, 100, 30);
        labelname.setFont(new Font("SansSerif", Font.PLAIN, 18));
        add(labelname);

        lblname = new JLabel();
        lblname.setBounds(250, 160, 250, 30);
        lblname.setFont(new Font("SansSerif", Font.PLAIN, 16));
        add(lblname);

        JLabel labelphone = new JLabel("Phone:");
        labelphone.setBounds(100, 200, 100, 30);
        labelphone.setFont(new Font("SansSerif", Font.PLAIN, 18));
        add(labelphone);

        lblphone = new JLabel();
        lblphone.setBounds(250, 200, 250, 30);
        lblphone.setFont(new Font("SansSerif", Font.PLAIN, 16));
        add(lblphone);

        JLabel labelemail = new JLabel("Email:");
        labelemail.setBounds(100, 240, 100, 30);
        labelemail.setFont(new Font("SansSerif", Font.PLAIN, 18));
        add(labelemail);

        lblemail = new JLabel();
        lblemail.setBounds(250, 240, 250, 30);
        lblemail.setFont(new Font("SansSerif", Font.PLAIN, 16));
        add(lblemail);

        // L?y th�ng tin ban d?u
        loadEmployeeDetails(cEmpId.getSelectedItem());

        // Khi ch?n nh�n vi�n kh�c
        cEmpId.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                loadEmployeeDetails(cEmpId.getSelectedItem());
            }
        });

        // N�t X�a
        delete = new JButton("Delete");
        delete.setBounds(220, 320, 150, 40);
        delete.setBackground(new Color(220, 53, 69));
        delete.setForeground(Color.WHITE);
        delete.setFont(new Font("SansSerif", Font.BOLD, 16));
        delete.setFocusPainted(false);
        delete.setBorderPainted(false);
        delete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        delete.addActionListener(this);
        add(delete);

        // N�t Quay l?i
        back = new JButton("Back");
        back.setBounds(400, 320, 150, 40);
        back.setBackground(new Color(33, 37, 41));
        back.setForeground(Color.WHITE);
        back.setFont(new Font("SansSerif", Font.BOLD, 16));
        back.setFocusPainted(false);
        back.setBorderPainted(false);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addActionListener(this);
        add(back);

        // C?u h�nh c?a s?
        setSize(750, 450);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadEmployeeDetails(String empId) {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee WHERE emID = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lblname.setText(rs.getString("name"));
                lblphone.setText(rs.getString("phone"));
                lblemail.setText(rs.getString("email"));
            } else {
                lblname.setText("");
                lblphone.setText("");
                lblemail.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == delete) {
            try {
                Conn c = new Conn();
                String query = "DELETE FROM employee WHERE emID = ?";
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setString(1, cEmpId.getSelectedItem());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Employee deleted successfully!");
                setVisible(false);
                new Home();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new Remove();
    }
}
