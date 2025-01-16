package com.task_tracker.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.io.IOException;

import com.task_tracker.tasks.Task;
import com.task_tracker.tasks.TaskStatus;
import com.task_tracker.manager.Manager;

public class Application {
    private int width = 1024, height = 768;

    // GUI Elements
    private JFrame frame;

    // Buttons
    private JButton addTaskButton, removeTaskButton, updateTaskButton;

    // Lists
    private JList<Task> todoList, inProgressList, doneList;
    private DefaultListModel<Task> todoModel, inProgressModel, doneModel;

    public Application(){
        // Main window settings
        this.frame = new JFrame("To Do App");
        this.frame.setSize(this.width, this.height);
        this.frame.setLayout(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // List Models
        this.todoModel = new DefaultListModel<>();
        this.inProgressModel = new DefaultListModel<>();
        this.doneModel = new DefaultListModel<>();

        // ToDo List
        JLabel todoLabel = new JLabel("To-Do List");
        todoLabel.setBounds(150, 0, 200, 20); 
        todoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.frame.add(todoLabel);

        this.todoList = new JList<>(todoModel);
        this.todoList.setBounds(150, 20, 200, 550);
        this.todoList.setCellRenderer(new TaskDescriptionRenderer());
        this.frame.add(this.todoList);

        // InProgress List
        JLabel inProgressLabel = new JLabel("In-Progress List");
        inProgressLabel.setBounds(370, 0, 200, 20); 
        inProgressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.frame.add(inProgressLabel);

        this.inProgressList = new JList<>(inProgressModel);
        this.inProgressList.setBounds(370, 20, 200, 550);
        this.inProgressList.setCellRenderer(new TaskDescriptionRenderer());
        this.frame.add(this.inProgressList);

        // Done List
        JLabel doneLabel = new JLabel("Done List");
        doneLabel.setBounds(590, 0, 200, 20); 
        doneLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.frame.add(doneLabel);

        this.doneList = new JList<>(doneModel);
        this.doneList.setBounds(590, 20, 200, 550);
        this.doneList.setCellRenderer(new TaskDescriptionRenderer());
        this.frame.add(this.doneList);

        // Buttons
        int buttonWidth = 170, buttonHeight = 25, buttonY = 620;

        // Add Task Button
        int addTaskButtonX = todoList.getX() + (todoList.getWidth() - buttonWidth) / 2;
        this.addTaskButton = new JButton("Add Task!");
        this.addTaskButton.setBounds(addTaskButtonX, buttonY, buttonWidth, buttonHeight);
        this.frame.add(this.addTaskButton);

        // Update Task Button
        int updateTaskButtonX = inProgressList.getX() + (inProgressList.getWidth() - buttonWidth) / 2;
        this.updateTaskButton = new JButton("Update Task!");
        this.updateTaskButton.setBounds(updateTaskButtonX, buttonY, buttonWidth, buttonHeight);
        this.frame.add(this.updateTaskButton);

        // Remove Task Button
        int removeTaskButtonX = doneList.getX() + (doneList.getWidth() - buttonWidth) / 2;
        this.removeTaskButton = new JButton("Remove Task!");
        this.removeTaskButton.setBounds(removeTaskButtonX, buttonY, buttonWidth, buttonHeight);
        this.frame.add(this.removeTaskButton);

        // Load existing tasks and add them to the appropriate lists
        loadTasksIntoLists();

        // Add Task Button Action Listener
        this.addTaskButton.addActionListener(e -> {
            NewTaskPopUp taskPopUp = new NewTaskPopUp();
            if (taskPopUp.getDescription() != null) {
                int newId = Manager.getNewTaskId();
                Task newTask = new Task(newId, taskPopUp.getDescription());
                newTask.setStatus(taskPopUp.getStatus());

                // Add to appropriate list
                moveToList(newTask, null);

                // Save task
                Manager.addTask(newTask);
                try {
                    Manager.save();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Update Task Button Action Listener
        this.updateTaskButton.addActionListener(e -> {
            Task selectedTask = getSelectedTask();
            if (selectedTask != null) 
                updateTask(selectedTask);
            else 
                JOptionPane.showMessageDialog(
                        null,
                        "Please select a task to update!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
        });

        // Double click handling for all lists
        MouseAdapter doubleClickListener = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                if (evt.getClickCount() == 2) {
                    JList<Task> source = (JList<Task>) evt.getSource();
                    Task selectedTask = source.getSelectedValue();
                    if (selectedTask != null) 
                        updateTask(selectedTask);
                }
            }
        };
        this.todoList.addMouseListener(doubleClickListener);
        this.inProgressList.addMouseListener(doubleClickListener);
        this.doneList.addMouseListener(doubleClickListener);

        // Remove Task Button Action Listener
        this.removeTaskButton.addActionListener(e -> {
            Task selectedTask = getSelectedTask();
            if (selectedTask != null) 
                removeTask(selectedTask);
            else 
                JOptionPane.showMessageDialog(
                        null,
                        "Please select a task to remove!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
            );  
        });

        // Show frame
        this.frame.setVisible(true);
    }

    // Load tasks from Manager and add them to the appropriate list based on their status
    private void loadTasksIntoLists(){
        for (Task task : Manager.getTasks()) 
            moveToList(task, null);
        
    }

    // Update a task
    private void updateTask(Task selectedTask){
        UpdateTaskPopUp updateTaskPopUp = new UpdateTaskPopUp(selectedTask);
        Task updatedTask = updateTaskPopUp.getUpdatedTask();

        if (updatedTask != null) {
            moveToList(updatedTask, selectedTask);
            Manager.getTasks().remove(selectedTask);
            Manager.getTasks().add(updatedTask);

            try{
                Manager.save();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    // Remove a task
    private void removeTask(Task selectedTask){
        if (selectedTask.getStatus() == TaskStatus.TODO) 
            todoModel.removeElement(selectedTask);
        else if (selectedTask.getStatus() == TaskStatus.IN_PROGRESS) 
            inProgressModel.removeElement(selectedTask);
         else if (selectedTask.getStatus() == TaskStatus.DONE) 
            doneModel.removeElement(selectedTask);
        
        Manager.getTasks().remove(selectedTask);

        try {
            Manager.save();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Move a task to the appropriate list
    private void moveToList(Task task, Task oldTask){
        if (oldTask != null) 
            removeTask(oldTask);
        
        if (task.getStatus() == TaskStatus.TODO) 
            todoModel.addElement(task);
        else if (task.getStatus() == TaskStatus.IN_PROGRESS) 
            inProgressModel.addElement(task);
        else if (task.getStatus() == TaskStatus.DONE) 
            doneModel.addElement(task);
    }

    // Get the selected task from any list
    private Task getSelectedTask(){
        if(!todoList.isSelectionEmpty()) 
            return todoList.getSelectedValue();
        else if (!inProgressList.isSelectionEmpty()) 
            return inProgressList.getSelectedValue();
        else if (!doneList.isSelectionEmpty()) 
            return doneList.getSelectedValue();
        
        return null;
    }

    // Task Description Renderer to display only the task description in the list
    private class TaskDescriptionRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Task) 
                setText(((Task) value).getDescription());
            
            return this;
        }
    }
}
