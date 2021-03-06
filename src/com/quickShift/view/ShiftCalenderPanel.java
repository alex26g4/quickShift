package com.quickShift.view;

import com.quickShift.controller.LoginController;
import com.quickShift.controller.RegisterController;
import com.quickShift.model.Employee;
import com.quickShift.model.EmployeeServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShiftCalenderPanel extends JPanel implements MouseListener {
    private static volatile ShiftCalenderPanel shiftCalenderPanel = null;
    private LoginController loginController = LoginController.getInstance();
    public static ShiftPanel[][] shiftPanelMatrix;

    public static ShiftCalenderPanel getInstance(Employee e) {
        synchronized (ShiftCalenderPanel.class) {
            if (shiftCalenderPanel == null) {
                shiftCalenderPanel = new ShiftCalenderPanel(e);
            }
        }
        return shiftCalenderPanel;
    }

    private ShiftCalenderPanel(Employee employee) {
        super();

        //setMinimumSize(new Dimension(500, 500)); //Uncomment this if you want to set minimum size
        //setMaximumSize(new Dimension(500, 500));  //Uncomment this if you want to set maximum size
        setLayout(new GridLayout(4, 5, 0, 0));
        setBackground(new Color(210, 210, 180));
        shiftPanelMatrix = new ShiftPanel[3][5];

        JLabel sunday = new JLabel("Sunday"); sunday.setHorizontalAlignment(SwingConstants.CENTER); add(sunday);
        JLabel monday = new JLabel("Monday"); monday.setHorizontalAlignment(SwingConstants.CENTER); add(monday);
        JLabel tuesday = new JLabel("Tuesday"); tuesday.setHorizontalAlignment(SwingConstants.CENTER); add(tuesday);
        JLabel wednesday = new JLabel("Wednesday"); wednesday.setHorizontalAlignment(SwingConstants.CENTER); add(wednesday);
        JLabel thursday = new JLabel("Thursday"); thursday.setHorizontalAlignment(SwingConstants.CENTER); add(thursday);

        shiftPanelMatrix = loginController.loadShiftsTableDB();

        for (int i = 0; i < 3; i++) { //Shift type
            for (int j = 0; j < 5; j++) { //Day of week

                if (employee.getMangerPosition()) {
                    shiftPanelMatrix[i][j].addMouseListener(this);
                }
                add(shiftPanelMatrix[i][j]);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ShiftPanel shiftPanel = ((ShiftPanel)(e.getComponent()));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShiftEditor(shiftPanel);
            }
        });
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void arrangeShiftsRandomly() {
        RegisterController registerController = RegisterController.getInstance();
        List<Employee> employeeListForAssignment = registerController.getEmployeeList();
        List<Employee> employeeListAssigned = new ArrayList<>();

        Employee[][] employeesShiftTable = new Employee[3][5];
        Random random = new Random();

        Employee currentEmployee = employeeListForAssignment.get(random.nextInt(employeeListForAssignment.size()));
        int currentEmployeeID = currentEmployee.getContactInfo().getId();
        Employee lastEmployee = employeeListForAssignment.get(random.nextInt(employeeListForAssignment.size()));
        int lastEmployeeID = lastEmployee.getContactInfo().getId();

        for (int j = 0; j < 5; j++) { //Day of week
            for (int i = 0; i < 3; i++) { //Shift type

                while(currentEmployeeID == lastEmployeeID){
                    currentEmployee = employeeListForAssignment.get(random.nextInt(employeeListForAssignment.size()));
                    currentEmployeeID = currentEmployee.getContactInfo().getId();
                }

                if(i==0){ //Randomize the first Employee which not worked in the last evening shift
                    employeesShiftTable[0][j] = currentEmployee;
                }
                if(i==1){ //Randomize the first Employee which not worked at the same day in the morning shift
                    while(i==1 && employeesShiftTable[0][j].getContactInfo().getId() == currentEmployeeID){
                        currentEmployee = employeeListForAssignment.get(random.nextInt(employeeListForAssignment.size()));
                        currentEmployeeID = currentEmployee.getContactInfo().getId();
                    }
                }
                if(i==2){ ////Randomize the first Employee which not worked at the same day in morning or midday shifts
                    while(i==2 && (employeesShiftTable[0][j].getContactInfo().getId() == currentEmployeeID)||(employeesShiftTable[1][j].getContactInfo().getId() == currentEmployeeID)){
                        currentEmployee = employeeListForAssignment.get(random.nextInt(employeeListForAssignment.size()));
                        currentEmployeeID = currentEmployee.getContactInfo().getId();
                    }
                }

                //Initialized the selected employee to Employee Table
                employeesShiftTable[i][j] = currentEmployee;
                lastEmployeeID = currentEmployeeID;

                //Adds the relevant data from the selected employee to the shiftPanel view
                shiftPanelMatrix[i][j].setEmployeeNameTxt(currentEmployee.getContactInfo().getFullName());
                shiftPanelMatrix[i][j].setEmployee(currentEmployee);
                shiftPanelMatrix[i][j].invalidate();

                employeeListAssigned.add(currentEmployee);
                employeeListForAssignment.remove(currentEmployee);

                if (employeeListForAssignment.size() == 0) {
                    employeeListForAssignment.addAll(employeeListAssigned);
                    employeeListAssigned.clear();
                }

            }
        }
        loginController.saveShiftsTableDB(shiftPanelMatrix);
    }

    public void clearShiftTable() {
        for (int j = 0; j < 5; j++) { //Day of week
            for (int i = 0; i < 3; i++) { //Shift type
                shiftPanelMatrix[i][j].setEmployeeNameTxt("");
                if(i==0){
                    shiftPanelMatrix[i][j].setShiftStartTimeTxt("Start: 09:00");
                    shiftPanelMatrix[i][j].setShiftEndTimeTxt("End: 15:00");
                }else if(i==1){
                    shiftPanelMatrix[i][j].setShiftStartTimeTxt("Start: 12:00");
                    shiftPanelMatrix[i][j].setShiftEndTimeTxt("End: 18:00");
                }else{
                    shiftPanelMatrix[i][j].setShiftStartTimeTxt("Start: 15:00");
                    shiftPanelMatrix[i][j].setShiftEndTimeTxt("End: 21:00");
                }
                shiftPanelMatrix[i][j].invalidate();
            }
        }
    }
}

