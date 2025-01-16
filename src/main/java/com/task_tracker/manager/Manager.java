package com.task_tracker.manager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.task_tracker.GUI.Application;
import com.task_tracker.settings.LocalDateTimeAdapter;
import com.task_tracker.tasks.Task;

public class Manager {
    private static int id = 1;
    private static final String filePath = "src/main/java/com/task_tracker/data/tasks.json";
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static Gson gson = new GsonBuilder()
                                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                                .create();

    public static void main(String[] args) throws IOException {
        // Load JSON File
        load();

        // Create GUI
        new Application();
    }

    // JSON Operations

    // Write to JSON
    public static void save() throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(tasks, writer);
        }
    }

    // Load from JSON
    public static void load() throws IOException {
        File dbFile = new File(filePath);

        // Create file if not exists
        dbFile.createNewFile();

        tasks = gson.fromJson(new FileReader(filePath), new TypeToken<ArrayList<Task>>() {}.getType());

        if (tasks != null && !tasks.isEmpty()) {
            id = tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
        } else {
            tasks = new ArrayList<>();
        }
    }

    // Add new task
    public static void addTask(Task task) {
        tasks.add(task);
    }

    public static int getNewTaskId() {
        return id++;
    }

    public static ArrayList<Task> getTasks() {
        return tasks;
    }
}