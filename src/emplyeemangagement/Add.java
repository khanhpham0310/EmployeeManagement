package emplyeemangagement;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import com.toedter.calendar.JDateChooser;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Add extends JFrame implements ActionListener {
    Random ran = new Random();
    JTextField tfname, tffname, tfsalary, tfaddress, tfphone, tfemail, tfdesignation, tfcccd;
    JDateChooser dcdob;
    JComboBox tfeducation;
    JLabel lblEmployeeID;
    JButton add, back;

    Add() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("Add Employee Detail");
        heading.setBounds(320, 30, 500, 50);
        heading.setFont(new Font("SAN_SERIF", Font.BOLD, 25));
        add(heading);

        JLabel labelname = new JLabel("Name");
        labelname.setBounds(50, 150, 150, 30);
        labelname.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelname);

        tfname = new JTextField();
        tfname.setBounds(200, 150, 150, 30);
        add(tfname);

        JLabel labelfname = new JLabel("Father's Name");
        labelfname.setBounds(400, 150, 150, 30);
        labelfname.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelfname);

        tffname = new JTextField();
        tffname.setBounds(600, 150, 150, 30);
        add(tffname);

        JLabel labeldob = new JLabel("Date of Birth");
        labeldob.setBounds(50, 200, 150, 30);
        labeldob.setFont(new Font("serif", Font.PLAIN, 20));
        add(labeldob);

        dcdob = new JDateChooser();
        dcdob.setBounds(200, 200, 150, 30);
        add(dcdob);

        JLabel labelsalary = new JLabel("Salary");
        labelsalary.setBounds(400, 200, 150, 30);
        labelsalary.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelsalary);

        tfsalary = new JTextField();
        tfsalary.setBounds(600, 200, 150, 30);
        add(tfsalary);

        JLabel labeladdress = new JLabel("Address");
        labeladdress.setBounds(50, 250, 150, 30);
        labeladdress.setFont(new Font("serif", Font.PLAIN, 20));
        add(labeladdress);

        tfaddress = new JTextField();
        tfaddress.setBounds(200, 250, 150, 30);
        add(tfaddress);

        JLabel labelphone = new JLabel("Phone");
        labelphone.setBounds(400, 250, 150, 30);
        labelphone.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelphone);

        tfphone = new JTextField();
        tfphone.setBounds(600, 250, 150, 30);
        add(tfphone);

        JLabel labelemail = new JLabel("Email");
        labelemail.setBounds(50, 300, 150, 30);
        labelemail.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelemail);

        tfemail = new JTextField();
        tfemail.setBounds(200, 300, 150, 30);
        add(tfemail);

        JLabel labeleducation = new JLabel("Education");
        labeleducation.setBounds(400, 300, 150, 30);
        labeleducation.setFont(new Font("serif", Font.PLAIN, 20));
        add(labeleducation);

        String[] courses = {"High School", "Bachelor's", "Master's", "PhD"};
        tfeducation = new JComboBox(courses);
        tfeducation.setBackground(Color.WHITE);
        tfeducation.setBounds(600, 300, 150, 30);
        add(tfeducation);

        JLabel labeldesignation = new JLabel("Designation");
        labeldesignation.setBounds(50, 350, 150, 30);
        labeldesignation.setFont(new Font("serif", Font.PLAIN, 20));
        add(labeldesignation);

        tfdesignation = new JTextField();
        tfdesignation.setBounds(200, 350, 150, 30);
        add(tfdesignation);

        JLabel labelcccd = new JLabel("CCCD");
        labelcccd.setBounds(400, 350, 150, 30);
        labelcccd.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelcccd);

        tfcccd = new JTextField();
        tfcccd.setBounds(600, 350, 150, 30);
        add(tfcccd);

        JLabel labelID = new JLabel("Employee ID");
        labelID.setBounds(50, 400, 150, 30);
        labelID.setFont(new Font("serif", Font.PLAIN, 20));
        add(labelID);

        lblEmployeeID = new JLabel(generateUniqueID());
        lblEmployeeID.setBounds(200, 400, 150, 30);
        lblEmployeeID.setFont(new Font("serif", Font.PLAIN, 20));
        add(lblEmployeeID);

        add = new JButton("Add Details");
        add.setBounds(250, 550, 150, 40);
        add.addActionListener(this);
        add.setBackground(Color.BLACK);
        add.setForeground(Color.WHITE);
        add(add);

        back = new JButton("Back");
        back.setBounds(450, 550, 150, 40);
        back.addActionListener(this);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        add(back);

        setSize(900, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String generateUniqueID() {
        return String.valueOf(ran.nextInt(999999));
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == add) {
            String name = tfname.getText();
            String fname = tffname.getText();
            String dob = dcdob.getDate() != null ? new SimpleDateFormat("yyyy-MM-dd").format(dcdob.getDate()) : "";
            String salary = tfsalary.getText();
            String address = tfaddress.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String education = (String) tfeducation.getSelectedItem();
            String designation = tfdesignation.getText();
            String cccd = tfcccd.getText();
            String emID = lblEmployeeID.getText();

            if (name.isEmpty() || fname.isEmpty() || dob.isEmpty() || salary.isEmpty() || address.isEmpty() ||
                phone.isEmpty() || email.isEmpty() || designation.isEmpty() || cccd.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields");
                return;
            }

            try {
                Conn conn = new Conn();
                String query = "INSERT INTO employee (name, fname, dob, salary, address, phone, email, education, designation, cccd, emID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.c.prepareStatement(query);
                ps.setString(1, name);
                ps.setString(2, fname);
                ps.setString(3, dob);
                ps.setString(4, salary);
                ps.setString(5, address);
                ps.setString(6, phone);
                ps.setString(7, email);
                ps.setString(8, education);
                ps.setString(9, designation);
                ps.setString(10, cccd);
                ps.setString(11, emID);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Details added successfully");
                goToHome();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: Unable to add details. Please try again.");
                e.printStackTrace();
            }
        } else {
            goToHome();
        }
    }

    private void goToHome() {
        setVisible(false);
        new Home();
    }

    public static void main(String[] args) {
        new Add();
    }
}