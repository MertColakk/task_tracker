package com.task_tracker.GUI;

import javax.swing.*;
import com.task_tracker.tasks.Task;
import com.task_tracker.tasks.TaskStatus;

public class UpdateTaskPopUp {
    // Attributes
    private Task updatedTask; // Stores the updated task

    // Constructor
    public UpdateTaskPopUp(Task task) {
        // Create the panel
        JPanel window = new JPanel();
        window.setLayout(new BoxLayout(window, BoxLayout.Y_AXIS));

        // Description Field
        JTextField descriptionField = new JTextField(50);
        descriptionField.setText(task.getDescription());
        window.add(new JLabel("Description: "));
        window.add(descriptionField);
        window.add(Box.createVerticalStrut(10)); // Space

        // Status ComboBox
        JComboBox<TaskStatus> statusBox = new JComboBox<>();
        for (TaskStatus status : TaskStatus.values())
            statusBox.addItem(status);
        statusBox.setSelectedItem(task.getStatus());
        window.add(new JLabel("Status: "));
        window.add(statusBox);
        window.add(Box.createVerticalStrut(10)); // Space

        // Show the pop-up dialog
        int result = JOptionPane.showConfirmDialog(
            null, 
            window, 
            "Update Task", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );

        // If the user clicks "OK"
        if (result == JOptionPane.OK_OPTION) {
            String description = descriptionField.getText().trim();
            TaskStatus status = (TaskStatus) statusBox.getSelectedItem();

            // Ensure the description is not empty
            if (!description.isEmpty()) {
                // Create the updated task
                updatedTask = new Task(task.getId(), description);
                updatedTask.setStatus(status);
            } else {
                JOptionPane.showMessageDialog(
                    null,
                    "Description cannot be empty.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // Getter for the updated task
    public Task getUpdatedTask() {
        return updatedTask;
    }
}