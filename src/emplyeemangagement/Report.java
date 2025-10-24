package emplyeemangagement;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;

public class Report extends JFrame implements ActionListener {
    Choice cYear;
    JButton generate, back;
    JLabel lblTotalEmployees, lblTotalPayroll;
    JPanel chartPanel;

    public Report() {
        setTitle("Annual Payroll Report");
        setSize(900, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 247, 250));
        setLayout(null);

        // Year selection
        JLabel lblYear = new JLabel("Select Year:");
        lblYear.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblYear.setBounds(20, 20, 100, 25);
        add(lblYear);

        cYear = new Choice();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 5; i <= currentYear; i++) {
            cYear.add(String.valueOf(i));
        }
        cYear.setBounds(120, 20, 100, 25);
        add(cYear);

        // Buttons
        generate = new JButton("Generate Report");
        generate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        generate.setBackground(Color.WHITE);
        generate.setForeground(new Color(33, 97, 140));
        generate.setBounds(20, 60, 150, 30);
        generate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        generate.addActionListener(this);
        add(generate);

        back = new JButton("Back");
        back.setFont(new Font("Segoe UI", Font.BOLD, 14));
        back.setBackground(Color.WHITE);
        back.setForeground(new Color(33, 97, 140));
        back.setBounds(180, 60, 100, 30);
        back.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        back.addActionListener(this);
        add(back);

        // Hover effects for buttons
        for (JButton btn : new JButton[]{generate, back}) {
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

        // Summary labels
        lblTotalEmployees = new JLabel("Total Employees: 0");
        lblTotalEmployees.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTotalEmployees.setBounds(20, 100, 200, 25);
        add(lblTotalEmployees);

        lblTotalPayroll = new JLabel("Total Payroll: $0.00");
        lblTotalPayroll.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTotalPayroll.setBounds(20, 130, 200, 25);
        add(lblTotalPayroll);

        // Chart panel
        chartPanel = new JPanel();
        chartPanel.setBounds(20, 160, 850, 400);
        chartPanel.setLayout(new BorderLayout());
        chartPanel.setBackground(new Color(245, 247, 250));
        add(chartPanel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == generate) {
            String year = cYear.getSelectedItem();
            try {
                Conn conn = new Conn();

                // Count total employees
                String employeeQuery = "SELECT COUNT(*) AS total FROM employee";
                ResultSet rsEmployees = conn.s.executeQuery(employeeQuery);
                int totalEmployees = 0;
                if (rsEmployees.next()) {
                    totalEmployees = rsEmployees.getInt("total");
                }

                // Calculate monthly payroll totals
                String payrollQuery = "SELECT month, SUM(final_salary) AS total_salary " +
                                     "FROM payroll WHERE year = ? GROUP BY month ORDER BY month";
                PreparedStatement psPayroll = conn.c.prepareStatement(payrollQuery);
                psPayroll.setString(1, year);
                ResultSet rsPayroll = psPayroll.executeQuery();

                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                double annualPayroll = 0.0;
                String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                
                // Initialize dataset with zeros for all months
                for (int i = 1; i <= 12; i++) {
                    dataset.addValue(0.0, "Payroll", months[i - 1]);
                }

                // Update dataset with actual payroll data
                while (rsPayroll.next()) {
                    int month = rsPayroll.getInt("month");
                    double totalSalary = rsPayroll.getDouble("total_salary");
                    dataset.setValue(totalSalary, "Payroll", months[month - 1]);
                    annualPayroll += totalSalary;
                }

                // Update summary labels
                lblTotalEmployees.setText("Total Employees: " + totalEmployees);
                DecimalFormat df = new DecimalFormat("#,##0.00");
                lblTotalPayroll.setText("Total Payroll: $" + df.format(annualPayroll));

                // Create bar chart
                JFreeChart chart = ChartFactory.createBarChart(
                        "Monthly Payroll for " + year,
                        "Month",
                        "Payroll Amount ($)",
                        dataset,
                        PlotOrientation.VERTICAL,
                        true, true, false
                );

                // Customize chart
                chart.setBackgroundPaint(new Color(245, 247, 250));
                BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
                renderer.setBarPainter(new StandardBarPainter());
                renderer.setSeriesPaint(0, new Color(33, 97, 140));
                chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
                chart.getCategoryPlot().setDomainGridlinePaint(new Color(200, 200, 200));
                chart.getCategoryPlot().setRangeGridlinePaint(new Color(200, 200, 200));

                // Update chart panel
                chartPanel.removeAll();
                ChartPanel cp = new ChartPanel(chart);
                cp.setPreferredSize(new Dimension(850, 400));
                chartPanel.add(cp, BorderLayout.CENTER);
                chartPanel.revalidate();
                chartPanel.repaint();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error generating report: " + e.getMessage());
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Report());
    }
}