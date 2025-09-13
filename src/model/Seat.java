package model;

import java.io.Serializable;

public class Seat implements Serializable {
    private int seatNumber;
    private boolean isAvailable;
    private String seatType; // e.g., "Standard", "VIP", "Couple"

    public Seat(int seatNumber, boolean isAvailable, String seatType) {
        this.seatNumber = seatNumber;
        this.isAvailable = isAvailable;
        this.seatType = seatType;
    }

    // Getters and setters
    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public String getSeatType() { return seatType; }
    public void setSeatType(String seatType) { this.seatType = seatType; }

    @Override
    public String toString() {
        return seatNumber + "," + isAvailable + "," + seatType;
    }

    public static Seat fromString(String data) {
        String[] parts = data.split(",");
        if (parts.length == 3) {
            try {
                int seatNumber = Integer.parseInt(parts[0]);
                boolean isAvailable = Boolean.parseBoolean(parts[1]);
                String seatType = parts[2];
                return new Seat(seatNumber, isAvailable, seatType);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}