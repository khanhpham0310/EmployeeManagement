package emplyeemangagement;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Splash extends JFrame implements ActionListener {
    JButton clickButton;

    Splash() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("EMPLOYEE MANAGEMENT SYSTEM");
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setFont(new Font("serif", Font.PLAIN, 60));
        heading.setForeground(Color.RED);
        add(heading, BorderLayout.NORTH);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/giphy.gif"));
        JLabel imageLabel = new JLabel(i1);
        imageLabel.setLayout(new GridBagLayout());
        add(imageLabel, BorderLayout.CENTER);

        clickButton = new JButton("Click here to continue");
        clickButton.setFont(new Font("sans_serif", Font.BOLD, 20));
        clickButton.setBackground(Color.BLACK);
        clickButton.setForeground(Color.WHITE);
        clickButton.setFocusPainted(false);
        clickButton.setMargin(new Insets(20, 20, 20, 20));
        clickButton.addActionListener(this);
        clickButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(250, 0, 0, 0);
        imageLabel.add(clickButton, gbc);

        setSize(1170, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == clickButton) {
            setVisible(false);
            new Login();
        }
    }

    public static void main(String[] args) {
        new Splash();
    }
}
