package core;

import javax.swing.*;

public class Helper {
    public static void setTheme() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
        }
    }

    public static void showMessage(String str) {
        String msg;
        String title;
        switch (str) {
            case "fill":
                msg = "Please fill out all the fields.";
                title = "Error";
                break;
            case "done":
                msg = "Operation has been completed.";
                title = "Success";
                break;
            case "notFound":
                msg = "No matching result(s) in the database";
                title = "Notification";
                break;
            case "error":
                msg = "An error occurred.";
                title = "Error";
                break;
            default:
                msg = str;
                title = "Rent a Car";
        }
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String str) {
        String msg;
        if (str.equals("confirm")) {
            msg = "Are you sure you want to proceed with this operation?";
        } else {
            msg = str;
        }

        return JOptionPane.showConfirmDialog(null, msg, "Confirmation", JOptionPane.YES_NO_OPTION) == 0;
    }

    public static boolean isFieldEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    public static boolean isFieldListEmpty(JTextField[] fieldList) {
        for (JTextField field : fieldList) {
            if (isFieldEmpty(field)) return true;
        }
        return false;
    }
}
