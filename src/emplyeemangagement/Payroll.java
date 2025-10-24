package emplyeemangagement;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Calendar;

public class Payroll extends JFrame implements ActionListener {
    Choice cEmID, cMonth, cYear;
    JButton calculate, back;
    JLabel lblBaseSalary, lblLateDays, lblDeduction, lblFinalSalary;

    public Payroll() {
        setTitle("Payroll Calculation");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 247, 250));

        // Employee ID
        JLabel lblEmID = new JLabel("Employee ID");
        lblEmID.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEmID.setBounds(20, 20, 100, 25);
        add(lblEmID);

        cEmID = new Choice();
        cEmID.setBounds(120, 20, 150, 25);
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("SELECT emID FROM employee");
            while (rs.next()) {
                cEmID.add(rs.getString("emID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(cEmID);

        // Month
        JLabel lblMonth = new JLabel("Month");
        lblMonth.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMonth.setBounds(300, 20, 100, 25);
        add(lblMonth);

        cMonth = new Choice();
        for (int i = 1; i <= 12; i++) {
            cMonth.add(String.valueOf(i));
        }
        cMonth.setBounds(400, 20, 100, 25);
        add(cMonth);

        // Year
        JLabel lblYear = new JLabel("Year");
        lblYear.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblYear.setBounds(20, 60, 100, 25);
        add(lblYear);

        cYear = new Choice();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 5; i <= currentYear; i++) {
            cYear.add(String.valueOf(i));
        }
        cYear.setBounds(120, 60, 100, 25);
        add(cYear);

        // Buttons
        calculate = new JButton("Calculate");
        calculate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        calculate.setBackground(Color.WHITE);
        calculate.setForeground(new Color(33, 97, 140));
        calculate.setBounds(20, 100, 120, 30);
        calculate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        calculate.addActionListener(this);
        add(calculate);

        back = new JButton("Back");
        back.setFont(new Font("Segoe UI", Font.BOLD, 14));
        back.setBackground(Color.WHITE);
        back.setForeground(new Color(33, 97, 140));
        back.setBounds(150, 100, 120, 30);
        back.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        back.addActionListener(this);
        add(back);

        // Hover effects for buttons
        for (JButton btn : new JButton[]{calculate, back}) {
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(new Color(33, 97, 140));
                    btn.setForeground(Color.WHITE);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(Color.WHITE);
                    btn.setForeground(new Color(33, 97, 140));
                }
            });
        }

        // Result Labels
        lblBaseSalary = new JLabel("Base Salary: ");
        lblBaseSalary.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBaseSalary.setBounds(20, 150, 500, 25);
        add(lblBaseSalary);

        lblLateDays = new JLabel("Late Days: ");
        lblLateDays.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblLateDays.setBounds(20, 180, 500, 25);
        add(lblLateDays);

        lblDeduction = new JLabel("Deduction: ");
        lblDeduction.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDeduction.setBounds(20, 210, 500, 25);
        add(lblDeduction);

        lblFinalSalary = new JLabel("Final Salary: ");
        lblFinalSalary.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFinalSalary.setBounds(20, 240, 500, 25);
        add(lblFinalSalary);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == calculate) {
            String emID = cEmID.getSelectedItem();
            String month = cMonth.getSelectedItem();
            String year = cYear.getSelectedItem();

            try {
                Conn conn = new Conn();
                // Get base salary
                String salaryQuery = "SELECT salary FROM employee WHERE emID = ?";
                PreparedStatement psSalary = conn.c.prepareStatement(salaryQuery);
                psSalary.setString(1, emID);
                ResultSet rsSalary = psSalary.executeQuery();
                double baseSalary = 0.0;
                if (rsSalary.next()) {
                    baseSalary = Double.parseDouble(rsSalary.getString("salary"));
                } else {
                    JOptionPane.showMessageDialog(null, "Employee not found");
                    return;
                }

                // Count late days
                String lateQuery = "SELECT COUNT(*) FROM attendance WHERE emID = ? AND MONTH(date) = ? AND YEAR(date) = ? AND status = 'Late'";
                PreparedStatement psLate = conn.c.prepareStatement(lateQuery);
                psLate.setString(1, emID);
                psLate.setString(2, month);
                psLate.setString(3, year);
                ResultSet rsLate = psLate.executeQuery();
                int lateDays = 0;
                if (rsLate.next()) {
                    lateDays = rsLate.getInt(1);
                }

                // Calculate deduction and final salary
                double deductionPercentage = lateDays * 2.0; // 2% per late day
                double deduction = baseSalary * (deductionPercentage / 100.0);
                double finalSalary = baseSalary - deduction;

                // Format for display
                DecimalFormat df = new DecimalFormat("#,##0.00");
                lblBaseSalary.setText("Base Salary: $" + df.format(baseSalary));
                lblLateDays.setText("Late Days: " + lateDays);
                lblDeduction.setText("Deduction: " + df.format(deductionPercentage) + "% ($" + df.format(deduction) + ")");
                lblFinalSalary.setText("Final Salary: $" + df.format(finalSalary));

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error calculating payroll: " + e.getMessage());
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Home().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Payroll());
    }
}