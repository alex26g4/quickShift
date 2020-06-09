package com.quickShift.view;


import com.quickShift.model.EmployeeImpl;
import com.quickShift.model.Manger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MenuFrame extends JFrame{
    private JButton reportHourBtn;
    private JPanel sideBar;
    private JPanel topBar;
    private JPanel mainBar;
    private JPanel mainMenu;
    private JButton addEmployeeBtn;
    private JLabel gratingLbl;
    private JButton deleteEmployeeBtn;
    private JButton updateInfoBtn;
    private JPanel shiftTable;

    public MenuFrame() {
        this.setTitle("QuickShift");
        this.setLocation(getWidth(), getHeight());
        this.setSize(1000, 600);
        this.add(mainMenu);
        setVisible(true); //Only for testing the ShiftCalenderPanel

        mainBar.removeAll();
        mainBar.repaint();
        mainBar.revalidate();

        mainBar.add(new ShiftCalenderPanel());
        mainBar.repaint();
        mainBar.revalidate();

    }

    public MenuFrame(EmployeeImpl employeeImpl){
        this.setTitle("QuickShift");
        this.setLocation(getWidth(),getHeight());
        this.setSize(1000,600);
        this.add(mainMenu);

        if(employeeImpl.getMangerPosition()){
            this.addEmployeeBtn.setVisible(true);
            this.deleteEmployeeBtn.setVisible(true);
        }

        shiftTable = new TestGrid();

        mainBar.removeAll();
        mainBar.repaint();
        mainBar.revalidate();

        mainBar.add(new ShiftCalenderPanel());
        mainBar.repaint();
        mainBar.revalidate();


    }

    public void setGratingMessage (String fistName,String lastName){
        this.gratingLbl.setText("Welcome Back "+fistName+" "+lastName);
    }

    public void reportHourBtnListener(ActionListener listenForReportHourBtn){
        reportHourBtn.addActionListener(listenForReportHourBtn);}

    public void addAddEmployeeListener(ActionListener listenForAddEmployeeBtn){addEmployeeBtn.addActionListener(listenForAddEmployeeBtn);}
    public void addDeleteEmployeeListener (ActionListener listenForDeleteEmployee){deleteEmployeeBtn.addActionListener(listenForDeleteEmployee);}
    public void addUpdateInfoListener (ActionListener listenForUpdateInfo){updateInfoBtn.addActionListener(listenForUpdateInfo);}
}
