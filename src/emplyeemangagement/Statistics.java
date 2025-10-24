// Statistics.java
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
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;

public class Statistics extends JFrame implements ActionListener {
    Choice cMonth, cYear, cEmID;
    JButton calculate, back;
    JLabel lblPresent, lblAbsent, lblLate;
    JPanel chartPanel;

    Statistics() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel lblMonth = new JLabel("Month");
        lblMonth.setBounds(20, 20, 100, 20);
        add(lblMonth);

        cMonth = new Choice();
        for (int i = 1; i <= 12; i++) {
            cMonth.add(String.valueOf(i));
        }
        cMonth.setBounds(120, 20, 100, 20);
        add(cMonth);

        JLabel lblYear = new JLabel("Year");
        lblYear.setBounds(230, 20, 100, 20);
        add(lblYear);

        cYear = new Choice();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear + 5; i >= currentYear; i--) {
            cYear.add(String.valueOf(i));
        }
        cYear.setBounds(330, 20, 100, 20);
        add(cYear);

        JLabel lblEmID = new JLabel("Employee ID");
        lblEmID.setBounds(440, 20, 100, 20);
        add(lblEmID);

        cEmID = new Choice();
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select emID from employee");
            while (rs.next()) {
                cEmID.add(rs.getString("emID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cEmID.setBounds(550, 20, 150, 20);
        add(cEmID);

        calculate = new JButton("Calculate");
        calculate.setBounds(20, 50, 100, 25);
        calculate.addActionListener(this);
        add(calculate);

        back = new JButton("Back");
        back.setBounds(130, 50, 100, 25);
        back.addActionListener(this);
        add(back);

        lblPresent = new JLabel("Present Days: 0");
        lblPresent.setBounds(20, 90, 200, 25);
        add(lblPresent);

        lblAbsent = new JLabel("Absent Days: 0");
        lblAbsent.setBounds(20, 120, 200, 25);
        add(lblAbsent);

        lblLate = new JLabel("Late Days: 0");
        lblLate.setBounds(20, 150, 200, 25);
        add(lblLate);

        // Panel for Pie Chart
        chartPanel = new JPanel();
        chartPanel.setBounds(250, 80, 500, 450);
        chartPanel.setLayout(new BorderLayout());
        add(chartPanel);

        setSize(800, 600);
        setLocation(300, 100);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == calculate) {
            String month = cMonth.getSelectedItem();
            String year = cYear.getSelectedItem();
            String emID = cEmID.getSelectedItem();

            int present = 0, absent = 0, late = 0;

            try {
                Conn conn = new Conn();
                String query = "SELECT status FROM attendance WHERE emID = ? AND date BETWEEN ? AND ?";
                PreparedStatement ps = conn.c.prepareStatement(query);

                int y = Integer.parseInt(year);
                int m = Integer.parseInt(month);
                Calendar cal = Calendar.getInstance();

                // Ngày đầu tháng
                cal.set(y, m - 1, 1);
                java.sql.Date startDate = new java.sql.Date(cal.getTimeInMillis());

                // Ngày cuối tháng
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                java.sql.Date endDate = new java.sql.Date(cal.getTimeInMillis());

                ps.setString(1, emID);
                ps.setDate(2, startDate);
                ps.setDate(3, endDate);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String status = rs.getString("status");
                    if ("Present".equalsIgnoreCase(status)) present++;
                    else if ("Absent".equalsIgnoreCase(status)) absent++;
                    else if ("Late".equalsIgnoreCase(status)) late++;
                }

                lblPresent.setText("Present Days: " + present);
                lblAbsent.setText("Absent Days: " + absent);
                lblLate.setText("Late Days: " + late);

                // Create Pie Chart dataset
                DefaultPieDataset dataset = new DefaultPieDataset();
                dataset.setValue("Present", present);
                dataset.setValue("Absent", absent);
                dataset.setValue("Late", late);

                JFreeChart chart = ChartFactory.createPieChart(
                        "Attendance Statistics for " + month + "/" + year,
                        dataset,
                        true, true, false);

                // Format chart
                PiePlot plot = (PiePlot) chart.getPlot();
                plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
                plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                        "{0}: {2}", new DecimalFormat("0"), new DecimalFormat("0.0%")));
                plot.setSimpleLabels(true);
                plot.setCircular(true);
                plot.setInteriorGap(0.10);
                plot.setBackgroundPaint(Color.WHITE);
                plot.setOutlineVisible(false);
                plot.setLabelBackgroundPaint(new Color(255, 255, 255, 180));
                plot.setLabelShadowPaint(null);
                plot.setLabelOutlinePaint(null);

                // Update chart panel
                chartPanel.removeAll();
                ChartPanel cp = new ChartPanel(chart);
                cp.setPreferredSize(new Dimension(350, 300));
                chartPanel.add(cp, BorderLayout.CENTER);
                chartPanel.revalidate();
                chartPanel.repaint();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Home();
        }
    }

    public static void main(String[] args) {
        new Statistics();
    }
}
