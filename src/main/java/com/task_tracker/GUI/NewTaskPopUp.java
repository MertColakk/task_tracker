package com.task_tracker.GUI;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.task_tracker.tasks.TaskStatus;

public class NewTaskPopUp {
    // Attributes
    private String description;
    private TaskStatus status;

    public NewTaskPopUp() {
        boolean validInput = false;

        while (!validInput) {
            // Window
            JPanel window = new JPanel();
            window.setLayout(new BoxLayout(window, BoxLayout.Y_AXIS));

            // Description Field
            JTextField descriptionField = new JTextField(50);
            window.add(new JLabel("Enter Description: "));
            window.add(descriptionField);
            window.add(Box.createVerticalStrut(10)); // Space

            // Status ComboBox
            JComboBox<TaskStatus> statusBox = new JComboBox<>();
            for (TaskStatus status : TaskStatus.values()) 
                statusBox.addItem(status);
            
            window.add(new JLabel("Select Status: "));
            window.add(statusBox);
            window.add(Box.createVerticalStrut(10)); // Space

            // Key Listener for Enter Key
            descriptionField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (!descriptionField.getText().isEmpty()) {
                            description = descriptionField.getText().trim();
                            status = (TaskStatus) statusBox.getSelectedItem();
                            SwingUtilities.getWindowAncestor(window).dispose();
                        } else {
                            JOptionPane.showMessageDialog(
                                null,
                                "All fields must be filled. Please try again.",
                                "Input Error",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            });

            // Show Dialog
            int result = JOptionPane.showConfirmDialog(
                null,
                window,
                "Add New Task!",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                description = descriptionField.getText().trim();
                status = (TaskStatus) statusBox.getSelectedItem();

                if (description.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        null,
                        "All fields must be filled. Please try again.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    validInput = true;
                }
            } else {
                validInput = true;
                description = null;
                status = null;
            }
        }
    }

    // Getters
    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }
}
