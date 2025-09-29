package com.example.eventplatform.controller;

public class AdminStats {
    private int totalEvents;
    private int pendingReservations;
    private long totalSales;
    private int todayReservations;

    public AdminStats(int totalEvents, int pendingReservations, long totalSales, int todayReservations) {
        this.totalEvents = totalEvents;
        this.pendingReservations = pendingReservations;
        this.totalSales = totalSales;
        this.todayReservations = todayReservations;
    }

    // Getters
    public int getTotalEvents() { return totalEvents; }
    public int getPendingReservations() { return pendingReservations; }
    public long getTotalSales() { return totalSales; }
    public int getTodayReservations() { return todayReservations; }
}
