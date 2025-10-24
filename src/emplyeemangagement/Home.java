package emplyeemangagement;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.URL;

public class Home extends JFrame implements ActionListener {
    JButton view, add, update, remove, attendance, statistics, payroll, report;

    public Home() {
        // --- Main Frame ---
        setTitle("Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 247, 250));

        // --- Header ---
        JLabel heading = new JLabel("Employee Management System", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(33, 97, 140));
        heading.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(heading, BorderLayout.NORTH);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 25, 25));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 40, 60));
        buttonPanel.setBackground(new Color(245, 247, 250));

        add = createButton("Add Employee", "icon/add.png");
        add.addActionListener(this);
        buttonPanel.add(add);

        view = createButton("View Employees", "icon/view.png");
        view.addActionListener(this);
        buttonPanel.add(view);

        update = createButton("Update Employee", "icon/update.png");
        update.addActionListener(this);
        buttonPanel.add(update);

        remove = createButton("Remove Employee", "icon/remove.png");
        remove.addActionListener(this);
        buttonPanel.add(remove);

        attendance = createButton("Attendance", "icon/attendance.png");
        attendance.addActionListener(this);
        buttonPanel.add(attendance);

        statistics = createButton("Statistics", "icon/statistics.png");
        statistics.addActionListener(this);
        buttonPanel.add(statistics);

        payroll = createButton("Payroll", "icon/payroll.png");
        payroll.addActionListener(this);
        buttonPanel.add(payroll);

        report = createButton("Report", "icon/report.png");
        report.addActionListener(this);
        buttonPanel.add(report);

        add(buttonPanel, BorderLayout.CENTER);

        // --- Footer ---
        JLabel footer = new JLabel("© 2025 Employee Management System", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setForeground(new Color(100, 100, 100));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(33, 97, 140));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setPreferredSize(new Dimension(260, 70));

        // Add icon if available
        URL resource = ClassLoader.getSystemResource(iconPath);
        if (resource != null) {
            ImageIcon icon = new ImageIcon(resource);
            Image scaledIcon = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledIcon));
            button.setHorizontalTextPosition(SwingConstants.RIGHT);
            button.setIconTextGap(10);
        }

        // Smooth hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(33, 97, 140));
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(new Color(33, 97, 140));
            }
        });

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == add) {
            setVisible(false);
            new Add();
        } else if (ae.getSource() == view) {
            setVisible(false);
            new View();
        } else if (ae.getSource() == update) {
            setVisible(false);
            new Update("");
        } else if (ae.getSource() == remove) {
            setVisible(false);
            new Remove();
        } else if (ae.getSource() == attendance) {
            setVisible(false);
            new Attendance();
        } else if (ae.getSource() == statistics) {
            setVisible(false);
            new Statistics();
        } else if (ae.getSource() == payroll) {
            setVisible(false);
            new Payroll();
        } else if (ae.getSource() == report) {
            setVisible(false);
            new Report();
        }
    }

    public static void main(String[] args) {
        new Home();
    }
}