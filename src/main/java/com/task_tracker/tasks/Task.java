package com.task_tracker.tasks;

import java.time.LocalDateTime;

public class Task {
    // Attributes
    private int id;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    

    // Constructor
    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.status = TaskStatus.TODO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


    // Getter and Setters
    public int getId(){return id;}
    public void setId(int id) {this.id = id; updateTimestamp();}


    public String getDescription(){return description;}
    public void setDescription(String description) {this.description = description; updateTimestamp();}


    public TaskStatus getStatus(){return status;}
    public void setStatus(TaskStatus status){this.status = status; updateTimestamp();}


    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt = createdAt; updateTimestamp();}


    public LocalDateTime getUpdatedAt(){return updatedAt;}
    public void setUpdatedAt(LocalDateTime updatedAt){this.updatedAt = updatedAt; updateTimestamp();}

    // Update the timestamp when changes are made
    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
