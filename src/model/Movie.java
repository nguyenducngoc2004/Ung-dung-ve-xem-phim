package model;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Serializable {
    private int id;
    private String title;
    private String description;
    private double price;
    private Date showtime;
    private String location;
    private int totalSeats;
    private int availableSeats;

    public Movie(int id, String title, String description, double price, 
                Date showtime, String location, int totalSeats) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.showtime = showtime;
        this.location = location;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public Date getShowtime() { return showtime; }
    public String getLocation() { return location; }
    public int getTotalSeats() { return totalSeats; }
    public int getAvailableSeats() { return availableSeats; }
    
    public void setAvailableSeats(int availableSeats) { 
        this.availableSeats = availableSeats; 
    }

    @Override
    public String toString() {
        return id + "," + title + "," + description + "," + price + "," +
               showtime.getTime() + "," + location + "," + totalSeats + "," + availableSeats;
    }

    public static Movie fromString(String data) {
        String[] parts = data.split(",");
        if (parts.length == 8) {
            try {
                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                String description = parts[2];
                double price = Double.parseDouble(parts[3]);
                Date showtime = new Date(Long.parseLong(parts[4]));
                String location = parts[5];
                int totalSeats = Integer.parseInt(parts[6]);
                int availableSeats = Integer.parseInt(parts[7]);
                
                Movie movie = new Movie(id, title, description, price, showtime, location, totalSeats);
                movie.setAvailableSeats(availableSeats);
                return movie;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}