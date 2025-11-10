package com.example.newbingo;

public class GameInfo {
    private String username; // User's name
    private int roundNumber; // Round number of the Bingo game
    private String bingoNumbers; // Numbers called in the game
    private long systemTime; // System timestamp

    // Constructor
    public GameInfo(String username, int roundNumber, String bingoNumbers, long systemTime) {
        this.username = username;
        this.roundNumber = roundNumber;
        this.bingoNumbers = bingoNumbers;
        this.systemTime = systemTime;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public String getBingoNumbers() {
        return bingoNumbers;
    }

    public long getSystemTime() {
        return systemTime;
    }
}
