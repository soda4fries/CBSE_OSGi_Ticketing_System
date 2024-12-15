package com.ticketsystem.consumer;

import com.ticketsystem.api.model.Ticket;
import com.ticketsystem.api.model.Reply;
import com.ticketsystem.api.service.TicketService;
import java.util.*;

public class TicketConsumer {
    private final TicketService ticketService;
    private final Map<String, TestResult> testResults = new HashMap<>();

    public TicketConsumer(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public static class TestResult {
        public String testName;
        public boolean passed;
        public String message;
        public long executionTime;

        public TestResult(String testName, boolean passed, String message, long executionTime) {
            this.testName = testName;
            this.passed = passed;
            this.message = message;
            this.executionTime = executionTime;
        }
    }

    public void runAllTests() {
        System.out.println("Starting Comprehensive Ticket System Tests...\n");

        // Advanced Operations Tests
        testReplyTreeManagement();
        testTicketUpdates();

        // Viewing and Search Tests
        testTicketRetrieval();
        testStatusFiltering();
        testAssigneeFiltering();
        testDepartmentGrouping();
        testSearchFunctionality();

        printTestResults();
    }

    private void testReplyTreeManagement() {
        long startTime = System.currentTimeMillis();
        try {
            // Create a ticket with a complex reply structure
            Ticket ticket = ticketService.createTicket("Reply Tree Test", "Testing nested replies");

            // Create a complex reply tree
            Reply root1 = ticketService.addReply(ticket.getId(), "Root Reply 1", null);
            Reply root2 = ticketService.addReply(ticket.getId(), "Root Reply 2", null);

            Reply child1 = ticketService.addReply(ticket.getId(), "Child 1 of Root 1", root1.getId());
            Reply child2 = ticketService.addReply(ticket.getId(), "Child 2 of Root 1", root1.getId());

            ticketService.addReply(ticket.getId(), "Child of Child 1", child1.getId());

            // Get the reply tree
            List<Reply> replyTree = ticketService.getTicketRepliesTree(ticket.getId());

            boolean success = replyTree.size() == 2 && // Two root replies
                    replyTree.get(0).getChildren().size() == 2 && // First root has two children
                    replyTree.get(0).getChildren().get(0).getChildren().size() == 1; // First child has one child

            testResults.put("replyTreeManagement", new TestResult(
                    "Reply Tree Management",
                    success,
                    success ? "Successfully managed reply tree structure" : "Failed to maintain correct reply tree structure",
                    System.currentTimeMillis() - startTime
            ));
        } catch (Exception e) {
            testResults.put("replyTreeManagement", new TestResult(
                    "Reply Tree Management",
                    false,
                    "Exception: " + e.getMessage(),
                    System.currentTimeMillis() - startTime
            ));
        }
    }

    private void testTicketRetrieval() {
        long startTime = System.currentTimeMillis();
        try {
            // Create multiple tickets
            Ticket ticket1 = ticketService.createTicket("Test Ticket 1", "Description 1");
            ticketService.createTicket("Test Ticket 2", "Description 2");

            // Test individual ticket retrieval
            Ticket retrieved = ticketService.getTicket(ticket1.getId());

            // Test all tickets retrieval
            List<Ticket> allTickets = ticketService.getAllTickets();

            boolean success = retrieved != null &&
                    retrieved.getId().equals(ticket1.getId()) &&
                    allTickets.size() >= 2;

            testResults.put("ticketRetrieval", new TestResult(
                    "Ticket Retrieval",
                    success,
                    success ? "Successfully retrieved tickets" : "Failed to retrieve tickets correctly",
                    System.currentTimeMillis() - startTime
            ));
        } catch (Exception e) {
            testResults.put("ticketRetrieval", new TestResult(
                    "Ticket Retrieval",
                    false,
                    "Exception: " + e.getMessage(),
                    System.currentTimeMillis() - startTime
            ));
        }
    }

    private void testStatusFiltering() {
        long startTime = System.currentTimeMillis();
        try {
            // Create tickets with different statuses
            Ticket openTicket = ticketService.createTicket("Open Ticket", "Description");

            Ticket resolvedTicket = ticketService.createTicket("Resolved Ticket", "Description");
            ticketService.resolveTicket(resolvedTicket.getId());

            // Test filtering
            List<Ticket> openTickets = ticketService.getTicketsByStatus("OPEN");
            List<Ticket> resolvedTickets = ticketService.getTicketsByStatus("RESOLVED");

            boolean success = openTickets.stream().allMatch(t -> "OPEN".equals(t.getStatus())) &&
                    resolvedTickets.stream().allMatch(t -> "RESOLVED".equals(t.getStatus()));

            testResults.put("statusFiltering", new TestResult(
                    "Status Filtering",
                    success,
                    success ? "Successfully filtered tickets by status" : "Failed to filter tickets by status correctly",
                    System.currentTimeMillis() - startTime
            ));
        } catch (Exception e) {
            testResults.put("statusFiltering", new TestResult(
                    "Status Filtering",
                    false,
                    "Exception: " + e.getMessage(),
                    System.currentTimeMillis() - startTime
            ));
        }
    }

    private void testAssigneeFiltering() {
        long startTime = System.currentTimeMillis();
        try {
            // Create tickets with different assignees
            Ticket ticket1 = ticketService.createTicket("Ticket 1", "Description");
            Ticket ticket2 = ticketService.createTicket("Ticket 2", "Description");

            ticketService.assignTicket(ticket1.getId(), "agent1");
            ticketService.assignTicket(ticket2.getId(), "agent2");

            // Test filtering
            List<Ticket> agent1Tickets = ticketService.getTicketsByAssignee("agent1");

            boolean success = agent1Tickets.stream().allMatch(t -> "agent1".equals(t.getAssignedTo()));

            testResults.put("assigneeFiltering", new TestResult(
                    "Assignee Filtering",
                    success,
                    success ? "Successfully filtered tickets by assignee" : "Failed to filter tickets by assignee correctly",
                    System.currentTimeMillis() - startTime
            ));
        } catch (Exception e) {
            testResults.put("assigneeFiltering", new TestResult(
                    "Assignee Filtering",
                    false,
                    "Exception: " + e.getMessage(),
                    System.currentTimeMillis() - startTime
            ));
        }
    }

    private void testSearchFunctionality() {
        long startTime = System.currentTimeMillis();
        try {
            // Create tickets with searchable content
            ticketService.createTicket("Network Issue", "Connection problems");
            ticketService.createTicket("Database Error", "SQL query timeout");
            ticketService.createTicket("UI Bug", "Button not working");

            // Test search
            List<Ticket> networkResults = ticketService.searchTickets("network");
            List<Ticket> sqlResults = ticketService.searchTickets("SQL");
            List<Ticket> nonexistentResults = ticketService.searchTickets("nonexistent");

            boolean success = !networkResults.isEmpty() &&
                    !sqlResults.isEmpty() &&
                    nonexistentResults.isEmpty();

            testResults.put("searchFunctionality", new TestResult(
                    "Search Functionality",
                    success,
                    success ? "Successfully performed ticket searches" : "Failed to search tickets correctly",
                    System.currentTimeMillis() - startTime
            ));
        } catch (Exception e) {
            testResults.put("searchFunctionality", new TestResult(
                    "Search Functionality",
                    false,
                    "Exception: " + e.getMessage(),
                    System.currentTimeMillis() - startTime
            ));
        }
    }



    private void testTicketUpdates() {
        long startTime = System.currentTimeMillis();
        try {
            // Create and update ticket
            Ticket ticket = ticketService.createTicket("Original Title", "Original description");

            ticket.setTitle("Updated Title");
            ticket.setDescription("Updated description");
            ticketService.updateTicket(ticket);

            Ticket updated = ticketService.getTicket(ticket.getId());

            boolean success = "Updated Title".equals(updated.getTitle()) &&
                    "Updated description".equals(updated.getDescription());

            testResults.put("ticketUpdates", new TestResult(
                    "Ticket Updates",
                    success,
                    success ? "Successfully updated ticket" : "Failed to update ticket correctly",
                    System.currentTimeMillis() - startTime
            ));
        } catch (Exception e) {
            testResults.put("ticketUpdates", new TestResult(
                    "Ticket Updates",
                    false,
                    "Exception: " + e.getMessage(),
                    System.currentTimeMillis() - startTime
            ));
        }
    }

    private void testDepartmentGrouping() {
        long startTime = System.currentTimeMillis();
        try {
            // Create tickets for different departments
            Ticket ticket1 = ticketService.createTicket("IT Issue", "Description");
            Ticket ticket2 = ticketService.createTicket("HR Issue", "Description");

            ticketService.assignTicket(ticket1.getId(), "it.agent1");
            ticketService.assignTicket(ticket2.getId(), "hr.agent1");

            Map<String, List<Ticket>> departmentTickets = ticketService.getTicketsByDepartment();

            boolean success = departmentTickets.containsKey("it") &&
                    departmentTickets.containsKey("hr");

            testResults.put("departmentGrouping", new TestResult(
                    "Department Grouping",
                    success,
                    success ? "Successfully grouped tickets by department" : "Failed to group tickets by department",
                    System.currentTimeMillis() - startTime
            ));
        } catch (Exception e) {
            testResults.put("departmentGrouping", new TestResult(
                    "Department Grouping",
                    false,
                    "Exception: " + e.getMessage(),
                    System.currentTimeMillis() - startTime
            ));
        }
    }

    private void printTestResults() {
        System.out.println("\nTest Results Summary:");
        System.out.println("====================");

        int totalTests = testResults.size();
        int passedTests = 0;
        long totalExecutionTime = 0;

        for (TestResult result : testResults.values()) {
            System.out.printf("\nTest: %s\n", result.testName);
            System.out.printf("Status: %s\n", result.passed ? "PASSED" : "FAILED");
            System.out.printf("Message: %s\n", result.message);
            System.out.printf("Execution Time: %dms\n", result.executionTime);
            System.out.println("--------------------");

            if (result.passed) passedTests++;
            totalExecutionTime += result.executionTime;
        }

        System.out.printf("\nFinal Summary:\n");
        System.out.printf("Tests Passed: %d/%d (%.2f%%)\n",
                passedTests, totalTests, (passedTests * 100.0) / totalTests);
        System.out.printf("Total Execution Time: %dms\n", totalExecutionTime);
    }

    public void demonstrateAllFeatures() {
        System.out.println("\nDemonstrating All Ticket System Features:");
        System.out.println("=======================================");

        try {
            // Create tickets
            createSampleTickets();

            // Demonstrate viewing features
            demonstrateViewingFeatures();

            // Demonstrate filtering and search
            demonstrateFilteringAndSearch();

            // Demonstrate reply management
            demonstrateReplyManagement();

            // Demonstrate statistics
            demonstrateStatistics();

        } catch (Exception e) {
            System.out.println("Error during demonstration: " + e.getMessage());
        }
    }

    private void createSampleTickets() {
        // Create various tickets with different states and assignments
        ticketService.createTicket("Urgent Server Issue", "Production server down");
        Ticket ticket = ticketService.createTicket("Network Problem", "VPN connection failing");
        ticketService.assignTicket(ticket.getId(), "network.expert");

        Ticket resolvedTicket = ticketService.createTicket("Software Bug", "Application crash");
        ticketService.resolveTicket(resolvedTicket.getId());
    }

    private void demonstrateViewingFeatures() {
        System.out.println("\n1. Viewing Features:");

        // Show all tickets
        List<Ticket> allTickets = ticketService.getAllTickets();
        System.out.printf("Total Tickets: %d\n", allTickets.size());

        // Show recent tickets
        List<Ticket> recentTickets = ticketService.getRecentTickets(3);
        System.out.println("\nMost Recent Tickets:");
        recentTickets.forEach(t -> System.out.printf("- %s (%s)\n", t.getTitle(), t.getStatus()));
    }

    private void demonstrateFilteringAndSearch() {
        System.out.println("\n2. Filtering and Search:");

        // Show status filtering
        List<Ticket> openTickets = ticketService.getTicketsByStatus("OPEN");
        System.out.printf("Open Tickets: %d\n", openTickets.size());
        openTickets.forEach(t -> System.out.printf("- %s\n", t.getTitle()));

        // Show assignee filtering
        List<Ticket> networkTeamTickets = ticketService.getTicketsByAssignee("network.expert");
        System.out.println("\nNetwork Team Tickets:");
        networkTeamTickets.forEach(t -> System.out.printf("- %s\n", t.getTitle()));

        // Show department grouping
        Map<String, List<Ticket>> departmentTickets = ticketService.getTicketsByDepartment();
        System.out.println("\nTickets by Department:");
        departmentTickets.forEach((dept, tickets) ->
                System.out.printf("- %s: %d tickets\n", dept, tickets.size()));

        // Show search
        List<Ticket> searchResults = ticketService.searchTickets("server");
        System.out.println("\nSearch Results for 'server':");
        searchResults.forEach(t -> System.out.printf("- %s\n", t.getTitle()));
    }

    private void demonstrateReplyManagement() {
        System.out.println("\n3. Reply Management:");

        // Create a ticket with complex reply structure
        Ticket ticket = ticketService.createTicket("Reply Test", "Testing reply structure");

        // Add replies
        Reply rootReply = ticketService.addReply(ticket.getId(), "Initial investigation", null);
        System.out.println("Added root reply");

        Reply childReply1 = ticketService.addReply(ticket.getId(), "Found potential cause", rootReply.getId());
        System.out.println("Added child reply 1");

        Reply childReply2 = ticketService.addReply(ticket.getId(), "Additional information", rootReply.getId());
        System.out.println("Added child reply 2");

        ticketService.addReply(ticket.getId(), "Resolution steps", childReply1.getId());
        System.out.println("Added nested reply");

        // Edit a reply
        ticketService.editReply(ticket.getId(), childReply2.getId(), "Updated information");
        System.out.println("Edited reply");

        // Show reply tree
        List<Reply> replyTree = ticketService.getTicketRepliesTree(ticket.getId());
        System.out.println("\nReply Tree Structure:");
        printReplyTree(replyTree, 0);
    }

    private void printReplyTree(List<Reply> replies, int level) {
        String indent = "  ".repeat(level);
        for (Reply reply : replies) {
            System.out.printf("%s- %s\n", indent, reply.getContent());
            if (reply.getChildren() != null && !reply.getChildren().isEmpty()) {
                printReplyTree(reply.getChildren(), level + 1);
            }
        }
    }

    private void demonstrateStatistics() {
        System.out.println("\n4. Ticket Statistics:");

        // Show overall statistics
        Map<String, Integer> stats = ticketService.getTicketStatistics();
        System.out.println("\nStatus Distribution:");
        stats.forEach((status, count) ->
                System.out.printf("- %s: %d tickets\n", status, count));

        // Show unassigned tickets
        List<Ticket> unassignedTickets = ticketService.getUnassignedTickets();
        System.out.printf("\nUnassigned Tickets: %d\n", unassignedTickets.size());

        // Show overdue tickets
        List<Ticket> overdueTickets = ticketService.getOverdueTickets();
        System.out.printf("Overdue Tickets: %d\n", overdueTickets.size());
    }

    // Additional test methods for edge cases and error conditions
    private void testEdgeCases() {
        long startTime = System.currentTimeMillis();
        try {
            boolean success = true;
            StringBuilder message = new StringBuilder();

            // Test 1: Try to get non-existent ticket
            try {
                ticketService.getTicket("nonexistent-id");
                success = false;
                message.append("Failed to handle non-existent ticket properly\n");
            } catch (IllegalArgumentException e) {
                // Expected behavior
            }

            // Test 2: Try to edit non-existent reply
            try {
                Ticket ticket = ticketService.createTicket("Test Ticket", "Description");
                ticketService.editReply(ticket.getId(), "nonexistent-reply", "content");
                success = false;
                message.append("Failed to handle non-existent reply properly\n");
            } catch (IllegalArgumentException e) {
                // Expected behavior
            }

            // Test 3: Try to resolve already resolved ticket
            try {
                Ticket ticket = ticketService.createTicket("Test Ticket", "Description");
                ticketService.resolveTicket(ticket.getId());
                ticketService.resolveTicket(ticket.getId());
                success = false;
                message.append("Failed to handle double resolution properly\n");
            } catch (IllegalStateException e) {
                // Expected behavior
            }

            testResults.put("edgeCases", new TestResult(
                    "Edge Cases",
                    success,
                    success ? "Successfully handled edge cases" : message.toString(),
                    System.currentTimeMillis() - startTime
            ));
        } catch (Exception e) {
            testResults.put("edgeCases", new TestResult(
                    "Edge Cases",
                    false,
                    "Exception: " + e.getMessage(),
                    System.currentTimeMillis() - startTime
            ));
        }
    }

    private void testConcurrentOperations() {
        long startTime = System.currentTimeMillis();
        try {
            Ticket ticket = ticketService.createTicket("Concurrent Test", "Testing concurrent operations");

            // Simulate concurrent operations
            Thread[] threads = new Thread[5];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> {
                    try {
                        ticketService.addReply(ticket.getId(), "Concurrent reply", null);
                        ticketService.assignTicket(ticket.getId(), "agent" + Thread.currentThread().getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                threads[i].start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Verify the final state
            Ticket finalState = ticketService.getTicket(ticket.getId());
            List<Reply> replies = ticketService.getTicketRepliesTree(ticket.getId());

            boolean success = finalState != null && replies.size() == 5;

            testResults.put("concurrentOperations", new TestResult(
                    "Concurrent Operations",
                    success,
                    success ? "Successfully handled concurrent operations" : "Failed to handle concurrent operations properly",
                    System.currentTimeMillis() - startTime
            ));
        } catch (Exception e) {
            testResults.put("concurrentOperations", new TestResult(
                    "Concurrent Operations",
                    false,
                    "Exception: " + e.getMessage(),
                    System.currentTimeMillis() - startTime
            ));
        }
    }
}