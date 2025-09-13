package model;

import java.io.Serializable;
import java.util.Date;

public class Ticket implements Serializable {
    private int id;
    private String username;
    private int movieId;
    private int seatNumber;
    private Date purchaseDate;
    private double price;

    public Ticket(int id, String username, int movieId, int seatNumber, Date purchaseDate, double price) {
        this.id = id;
        this.username = username;
        this.movieId = movieId;
        this.seatNumber = seatNumber;
        this.purchaseDate = purchaseDate;
        this.price = price;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public int getMovieId() { return movieId; }
    public int getSeatNumber() { return seatNumber; }
    public Date getPurchaseDate() { return purchaseDate; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return id + "," + username + "," + movieId + "," + seatNumber + "," +
               purchaseDate.getTime() + "," + price;
    }

    public static Ticket fromString(String data) {
        String[] parts = data.split(",");
        if (parts.length == 6) {
            try {
                int id = Integer.parseInt(parts[0]);
                String username = parts[1];
                int movieId = Integer.parseInt(parts[2]);
                int seatNumber = Integer.parseInt(parts[3]);
                Date purchaseDate = new Date(Long.parseLong(parts[4]));
                double price = Double.parseDouble(parts[5]);
                
                return new Ticket(id, username, movieId, seatNumber, purchaseDate, price);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}