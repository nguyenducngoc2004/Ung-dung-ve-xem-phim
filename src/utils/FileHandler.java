package utils;

import model.User;
import model.Movie;
import model.Ticket;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    // Thêm phương thức tạo thư mục nếu chưa tồn tại
    private static void ensureDataDirectory() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    // User file operations
    public static List<User> readUsers(String filename) {
        ensureDataDirectory(); // Đảm bảo thư mục tồn tại
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = User.fromString(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.out.println("Creating new users file: " + filename);
        }
        return users;
    }

    public static void writeUsers(String filename, List<User> users) {
        ensureDataDirectory(); // Đảm bảo thư mục tồn tại
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (User user : users) {
                writer.println(user.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Movie file operations
    public static List<Movie> readMovies(String filename) {
        ensureDataDirectory(); // Đảm bảo thư mục tồn tại
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Movie movie = Movie.fromString(line);
                if (movie != null) {
                    movies.add(movie);
                }
            }
        } catch (IOException e) {
            System.out.println("Creating new movies file: " + filename);
        }
        return movies;
    }

    public static void writeMovies(String filename, List<Movie> movies) {
        ensureDataDirectory(); // Đảm bảo thư mục tồn tại
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Movie movie : movies) {
                writer.println(movie.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ticket file operations
    public static List<Ticket> readTickets(String filename) {
        ensureDataDirectory(); // Đảm bảo thư mục tồn tại
        List<Ticket> tickets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Ticket ticket = Ticket.fromString(line);
                if (ticket != null) {
                    tickets.add(ticket);
                }
            }
        } catch (IOException e) {
            System.out.println("Creating new tickets file: " + filename);
        }
        return tickets;
    }

    public static void writeTickets(String filename, List<Ticket> tickets) {
        ensureDataDirectory(); // Đảm bảo thư mục tồn tại
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Ticket ticket : tickets) {
                writer.println(ticket.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}