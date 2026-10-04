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
        // Khởi tạo Presenter và Storage
        TaskShowing consolePresenter = new TaskConsolePresenter();
        IOSaveToSQLite sqliteStorage = new IOSaveToSQLite();
        TaskControl control = new TaskControl(consolePresenter, sqliteStorage, sqliteStorage);

        Scanner scanner = new Scanner(System.in);
        UUID currentTaskId = null;

        while (true) {
            System.out.println("\n=========================================");
            System.out.println("   HỆ THỐNG QUẢN LÝ TÁC VỤ (INTERACTIVE)  ");
            System.out.println("=========================================");
            System.out.println("1. Tạo Task mới (Nhập từ bàn phím)");
            System.out.println("2. Bắt đầu Task (TODO -> IN_PROGRESS)");
            System.out.println("3. Hoàn thành Task (IN_PROGRESS -> DONE)");
            System.out.println("0. Thoát");
            System.out.print("Chọn chức năng (0-3): ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Nhập tiêu đề Task (3-100 ký tự): ");
                        String title = scanner.nextLine();

                        System.out.print("Nhập mô tả Task: ");
                        String description = scanner.nextLine();

                        System.out.print("Chọn mức ưu tiên (1: LOW, 2: MEDIUM, 3: HIGH): ");
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
                        System.out.println("-> ĐÃ TẠO THÀNH CÔNG Task ID: " + currentTaskId);
                    }
                    case "2" -> {
                        if (currentTaskId == null) {
                            System.out.print("Nhập UUID của Task cần bắt đầu: ");
                            currentTaskId = UUID.fromString(scanner.nextLine().trim());
                        }
                        control.startTask(currentTaskId);
                        System.out.println("-> ĐÃ BẮT ĐẦU TASK!");
                    }
                    case "3" -> {
                        if (currentTaskId == null) {
                            System.out.print("Nhập UUID của Task cần hoàn thành: ");
                            currentTaskId = UUID.fromString(scanner.nextLine().trim());
                        }
                        control.completeTask(currentTaskId);
                        System.out.println("-> ĐÃ HOÀN THÀNH TASK!");
                    }
                    case "0" -> {
                        System.out.println("Thoát chương trình.");
                        return;
                    }
                    default -> System.out.println("Lựa chọn không hợp lệ, vui lòng nhập từ 0 đến 3.");
                }
            } catch (Exception e) {
                System.out.println("LỖI VI PHẠM NGHIỆP VỤ: " + e.getMessage());
            }
        }
    }
}