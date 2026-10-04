Feature: Task Lifecycle and Business Rules
  As a user of the Task Management System
  I want to create, start, and complete tasks
  So that I can organize and track my work effectively

  Scenario: Creating a task without specifying a project defaults to INBOX
    Given the task management system is ready
    When I create a task with title "Learn Clean Architecture" and description "Study chapters 4 and 5" with priority "HIGH"
    Then the task should be created successfully
    And the task status should be "Can lam"
    And the task should belong to the "INBOX" project

  Scenario: Successfully starting and completing a task
    Given the task management system is ready
    And a task exists with title "Refactor Database Layer"
    When I start the task
    Then the task status should be "Dang thuc hien"
    When I complete the task
    Then the task status should be "Da hoan thanh"

  Scenario: Cannot complete a task that has not been started
    Given the task management system is ready
    And a task exists with title "Write Unit Tests"
    When I attempt to complete the task directly
    Then the operation should fail with an invalid transition error
