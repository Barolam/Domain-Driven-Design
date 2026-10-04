package com.taskmanagement.adapter.persistence;

import com.taskmanagement.application.boundary.TaskRepository;
import com.taskmanagement.application.boundary.TaskSaving;
import com.taskmanagement.domain.task.entity.Task;
import com.taskmanagement.domain.task.enums.Priority;
import com.taskmanagement.domain.task.enums.TaskStatus;
import com.taskmanagement.domain.task.valueobject.Deadline;
import com.taskmanagement.domain.task.valueobject.TaskTitle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class IOSaveToSQLite implements TaskSaving, TaskRepository {
    private static final String CREATE_TABLE_SQL = """
        CREATE TABLE IF NOT EXISTS tasks (
            id TEXT PRIMARY KEY,
            title TEXT NOT NULL,
            description TEXT,
            due_date TEXT,
            priority TEXT NOT NULL,
            status TEXT NOT NULL,
            project_id TEXT NOT NULL
        )
        """;

    private static final String UPSERT_SQL = """
        INSERT INTO tasks (id, title, description, due_date, priority, status, project_id)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT(id) DO UPDATE SET
            title = excluded.title,
            description = excluded.description,
            due_date = excluded.due_date,
            priority = excluded.priority,
            status = excluded.status,
            project_id = excluded.project_id
        """;

    private final String jdbcUrl;

    public IOSaveToSQLite() {
        this(Path.of(System.getProperty("taskmanagement.db", "data/tasks.db")));
    }

    public IOSaveToSQLite(Path databasePath) {
        try {
            Path absolutePath = databasePath.toAbsolutePath();
            Path parent = absolutePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            this.jdbcUrl = "jdbc:sqlite:" + absolutePath;
            initializeSchema();
        } catch (IOException | SQLException exception) {
            throw new IllegalStateException("Could not initialize the SQLite task repository.", exception);
        }
    }

    @Override
    public void saveTask(Task task) {
        save(task);
    }

    @Override
    public void save(Task task) {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(UPSERT_SQL)) {
            statement.setString(1, task.getId().toString());
            statement.setString(2, task.getTitle().value());
            statement.setString(3, task.getDescription());
            statement.setString(4, task.getDueDate() == null
                ? null
                : task.getDueDate().dueDate().toString());
            statement.setString(5, task.getPriority().name());
            statement.setString(6, task.getStatus().name());
            statement.setString(7, task.getProjectId().toString());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Could not save task " + task.getId() + " to SQLite.", exception);
        }
    }

    @Override
    public Optional<Task> findById(UUID id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapTask(resultSet)) : Optional.empty();
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Could not find task " + id + " in SQLite.", exception);
        }
    }

    @Override
    public List<Task> findAll() {
        String sql = "SELECT * FROM tasks ORDER BY rowid";
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = openConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                tasks.add(mapTask(resultSet));
            }
            return List.copyOf(tasks);
        } catch (SQLException exception) {
            throw new IllegalStateException("Could not retrieve tasks from SQLite.", exception);
        }
    }

    private void initializeSchema() throws SQLException {
        try (Connection connection = openConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE_SQL);
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }

    private Task mapTask(ResultSet resultSet) throws SQLException {
        String dueDate = resultSet.getString("due_date");
        Task task = new Task(
            UUID.fromString(resultSet.getString("id")),
            new TaskTitle(resultSet.getString("title")),
            resultSet.getString("description"),
            dueDate == null ? null : new Deadline(Instant.parse(dueDate)),
            Priority.valueOf(resultSet.getString("priority")),
            UUID.fromString(resultSet.getString("project_id"))
        );

        TaskStatus status = TaskStatus.valueOf(resultSet.getString("status"));
        switch (status) {
            case TODO -> { }
            case IN_PROGRESS -> task.start();
            case DONE -> {
                task.start();
                task.complete();
            }
            case CANCELLED -> task.cancel();
        }
        return task;
    }
}
