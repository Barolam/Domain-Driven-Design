package com.taskmanagement.infrastructure.config;

import com.taskmanagement.adapter.persistence.IOSaveToMemory;
import com.taskmanagement.adapter.persistence.IOSaveToSQLite;
import com.taskmanagement.adapter.presenter.TaskConsolePresenter;
import com.taskmanagement.adapter.presenter.TaskGUIPresenter;
import com.taskmanagement.adapter.presenter.TaskWebPresenter;
import com.taskmanagement.application.boundary.TaskSaving;
import com.taskmanagement.application.boundary.TaskShowing;
import com.taskmanagement.application.control.TaskControl;
import com.taskmanagement.infrastructure.ui.GUISwing;
import com.taskmanagement.infrastructure.ui.IOConsole;
import com.taskmanagement.infrastructure.ui.WebUI;

public class Main {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   CLEAN ARCHITECTURE & DDD - TASK MANAGEMENT   ");
        System.out.println("=================================================\n");

        // --- Cấu hình 1: Chạy với Console Presenter & Memory Persistence ---
        System.out.println(">>> DEMO 1: Console UI + Memory Database");
        TaskShowing consolePresenter = new TaskConsolePresenter();
        TaskSaving memoryStorage = new IOSaveToMemory();
        TaskControl control1 = new TaskControl(consolePresenter, memoryStorage);

        IOConsole consoleUI = new IOConsole(control1);
        consoleUI.runDemo();

        // --- Cấu hình 2: Chạy với Swing GUI Presenter & SQLite Storage ---
        System.out.println(">>> DEMO 2: Swing GUI + SQLite Storage");
        TaskShowing guiPresenter = new TaskGUIPresenter();
        TaskSaving sqliteStorage = new IOSaveToSQLite();
        TaskControl control2 = new TaskControl(guiPresenter, sqliteStorage);

        GUISwing swingUI = new GUISwing(control2);
        swingUI.simulateUserClick();

        // --- Cấu hình 3: Chạy với Web Presenter ---
        System.out.println(">>> DEMO 3: Web REST API + SQLite Storage");
        TaskShowing webPresenter = new TaskWebPresenter();
        TaskControl control3 = new TaskControl(webPresenter, sqliteStorage);

        WebUI webUI = new WebUI(control3);
        webUI.handleHttpRequest();

        System.out.println("\n=================================================");
        System.out.println("   MÔ PHỎNG HOÀN TẤT THÀNH CÔNG - DIP THÀNH CÔNG!");
        System.out.println("=================================================");
    }
}
