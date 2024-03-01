import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class Employee {
    private JPanel Main;
    private JTable emp_table;
    private JTextField txtName;
    private JTextField txtSalary;
    private JTextField txtMobile;
    private JButton saveButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTextField txtid;
    private JButton searchButton;
    private JScrollPane emp_table_scroll;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    Connection con;
    PreparedStatement pst;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/employee_java_db", "root", "password");
            System.out.println("Successs");
        }
        catch(ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    void table_load() {
        try {
            pst = con.prepareStatement("select * from employee");
            ResultSet result = pst.executeQuery();
            emp_table.setModel(DbUtils.resultSetToTableModel(result));
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Employee() {
        connect();
        table_load();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emp_name, emp_salary, emp_mobile;

                emp_name = txtName.getText();
                emp_salary = txtSalary.getText();
                emp_mobile = txtMobile.getText();

                try {
                    pst = con.prepareStatement("insert into employee(emp_name, emp_salary, emp_mobile)values(?, ?, ?)");
                    pst.setString(1, emp_name);
                    pst.setString(2, emp_salary);
                    pst.setString(3, emp_mobile);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Added!");
                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();
                }
                catch(SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = txtid.getText();

                    pst = con.prepareStatement("select emp_name, emp_salary, emp_mobile from employee where id = ?");
                    pst.setString(1, id);
                    ResultSet result = pst.executeQuery();

                    if(result.next() == true) {
                        String emp_name = result.getString(1);
                        String emp_salary = result.getString(2);
                        String emp_mobile = result.getString(3);

                        txtName.setText(emp_name);
                        txtSalary.setText(emp_salary);
                        txtMobile.setText(emp_mobile);
                    }
                    else {
                        txtName.setText("");
                        txtSalary.setText("");
                        txtMobile.setText("");
                        JOptionPane.showMessageDialog(null, "Invalid Employee Number");
                    }
                }
                catch(SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emp_id, emp_name, emp_salary, emp_mobile;

                emp_name = txtName.getText();
                emp_salary = txtSalary.getText();
                emp_mobile = txtMobile.getText();
                emp_id = txtid.getText();

                try{
                    pst = con.prepareStatement("update employee set emp_name = ?, emp_salary = ?, emp_mobile = ? where id = ?");
                    pst.setString(1, emp_name);
                    pst.setString(2, emp_salary);
                    pst.setString(3, emp_mobile);
                    pst.setString(4, emp_id);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Updated!");
                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();
                }
                catch(SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emp_id;
                emp_id = txtid.getText();

                try {
                    pst = con.prepareStatement("delete from employee where id = ?");
                    pst.setString(1, emp_id);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Deleted!");
                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtid.setText("");
                    txtid.requestFocus();
                }
                catch(SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
