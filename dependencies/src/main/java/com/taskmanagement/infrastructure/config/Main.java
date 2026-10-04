package com.taskmanagement.infrastructure.config;

import com.taskmanagement.adapter.persistence.IOSaveToSQLite;
import com.taskmanagement.adapter.presenter.TaskConsolePresenter;
import com.taskmanagement.application.boundary.TaskShowing;
import com.taskmanagement.application.control.TaskControl;
import com.taskmanagement.application.dto.TaskInputDTO;
import com.taskmanagement.domain.task.enums.Priority;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // Khoi tao Presenter va Storage
        TaskShowing consolePresenter = new TaskConsolePresenter();
        IOSaveToSQLite sqliteStorage = new IOSaveToSQLite();
        TaskControl control = new TaskControl(consolePresenter, sqliteStorage, sqliteStorage);

        Scanner scanner = new Scanner(System.in);
        UUID currentTaskId = null;

        while (true) {
            System.out.println("\n=========================================");
            System.out.println("   HE THONG QUAN LY CONG VIEC (INTERACTIVE)  ");
            System.out.println("=========================================");
            System.out.println("1. Tao Task moi (Nhap tu ban phim)");
            System.out.println("2. Bat dau Task (TODO -> IN_PROGRESS)");
            System.out.println("3. Hoan thanh Task (IN_PROGRESS -> DONE)");
            System.out.println("0. Thoat");
            System.out.print("Chon chuc nang (0-3): ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Nhap tieu de Task (3-100 ky tu): ");
                        String title = scanner.nextLine();

                        System.out.print("Nhap mo ta Task: ");
                        String description = scanner.nextLine();

                        System.out.print("Chon muc uu tien (1: LOW, 2: MEDIUM, 3: HIGH): ");
                        String pChoice = scanner.nextLine();
                        Priority priority = switch (pChoice) {
                            case "1" -> Priority.LOW;
                            case "3" -> Priority.HIGH;
                            default -> Priority.MEDIUM;
                        };

                        TaskInputDTO input = new TaskInputDTO(
                            title,
                            description,
                            Instant.now().plus(1, ChronoUnit.DAYS),
                            priority,
                            UUID.randomUUID()
                        );

                        var result = control.createTask(input);
                        currentTaskId = UUID.fromString(result.taskId());
                        System.out.println("-> DA TAO THANH CONG Task ID: " + currentTaskId);
                    }
                    case "2" -> {
                        if (currentTaskId == null) {
                            System.out.print("Nhap UUID cua Task can bat dau: ");
                            currentTaskId = UUID.fromString(scanner.nextLine().trim());
                        }
                        control.startTask(currentTaskId);
                        System.out.println("-> DA BAT DAU TASK!");
                    }
                    case "3" -> {
                        if (currentTaskId == null) {
                            System.out.print("Nhap UUID cua Task can hoan thanh: ");
                            currentTaskId = UUID.fromString(scanner.nextLine().trim());
                        }
                        control.completeTask(currentTaskId);
                        System.out.println("-> DA HOAN THANH TASK!");
                    }
                    case "0" -> {
                        System.out.println("Thoat chuong trinh.");
                        return;
                    }
                    default -> System.out.println("Lua chon khong hop le, vui long nhap tu 0 den 3.");
                }
            } catch (Exception e) {
                System.out.println("LOI VI PHAM NGHIEP VU: " + e.getMessage());
            }
        }
    }
}