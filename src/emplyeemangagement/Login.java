package emplyeemangagement;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JTextField tfusername;
    JPasswordField pfpassword; 
    JCheckBox showPassword;   

    Login() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel lbusername = new JLabel("Username");
        lbusername.setBounds(40, 20, 100, 30);
        add(lbusername);

        tfusername = new JTextField();
        tfusername.setBounds(150, 20, 150, 30);
        add(tfusername);

        JLabel lbpassword = new JLabel("Password");
        lbpassword.setBounds(40, 70, 100, 30);
        add(lbpassword);

        pfpassword = new JPasswordField();
        pfpassword.setBounds(150, 70, 150, 30);
        add(pfpassword);

        // ✅ Thêm checkbox show/hide password
        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(150, 105, 150, 20);
        showPassword.setBackground(Color.WHITE);
        add(showPassword);

        // Sự kiện bật/tắt hiển thị mật khẩu
        showPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showPassword.isSelected()) {
                    pfpassword.setEchoChar((char) 0); // Hiện mật khẩu
                } else {
                    pfpassword.setEchoChar('•'); // Ẩn mật khẩu
                }
            }
        });

        JButton login = new JButton("LOGIN");
        login.setBounds(150, 140, 150, 30);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.addActionListener(this);
        add(login);

        setSize(600, 300);
        setLocation(450, 200);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String username = tfusername.getText();
            String password = new String(pfpassword.getPassword()); // ✅ Lấy mật khẩu an toàn

            Conn c = new Conn();
            String query = "SELECT * FROM login WHERE username = ? AND password = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Login successful!");
                setVisible(false);
                new Home();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
