package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import model.Movie;
import model.Ticket;
import model.User;
import utils.FileHandler;


public class MovieServer {
    private static final int PORT = 12345;
    private List<Movie> movies;
    private List<Ticket> tickets;
    private UserManager userManager;
    private static final String MOVIES_FILE = "data/movies.txt";
    private static final String TICKETS_FILE = "data/tickets.txt";

    public MovieServer() {
        userManager = new UserManager();
        loadData();
    }

    private void loadData() {
        movies = FileHandler.readMovies(MOVIES_FILE);
        tickets = FileHandler.readTickets(TICKETS_FILE);
    }

    private void saveData() {
        FileHandler.writeMovies(MOVIES_FILE, movies);
        FileHandler.writeTickets(TICKETS_FILE, tickets);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters and methods for client handlers to interact with
    public synchronized User login(String username, String password) {
        return userManager.loginUser(username, password);
    }

    public synchronized boolean register(String username, String password) {
        return userManager.registerUser(username, password);
    }

    public synchronized List<Movie> getMovies() {
        return new CopyOnWriteArrayList<>(movies);
    }

    public synchronized boolean addMovie(Movie movie) {
        movies.add(movie);
        saveData();
        return true;
    }

    public synchronized boolean purchaseTicket(String username, int movieId, int seatNumber) {
        Movie movie = movies.stream()
                .filter(m -> m.getId() == movieId)
                .findFirst()
                .orElse(null);
        
        if (movie != null && movie.getAvailableSeats() > 0) {
            // Kiểm tra seat number hợp lệ
            if (seatNumber < 1 || seatNumber > movie.getTotalSeats()) {
                return false;
            }
            
            // Check if seat is already taken
            boolean seatTaken = tickets.stream()
                    .anyMatch(t -> t.getMovieId() == movieId && t.getSeatNumber() == seatNumber);
            
            if (!seatTaken) {
                Ticket ticket = new Ticket(tickets.size() + 1, username, movieId, 
                                         seatNumber, new java.util.Date(), movie.getPrice());
                tickets.add(ticket);
                movie.setAvailableSeats(movie.getAvailableSeats() - 1);
                saveData();
                return true;
            }
        }
        return false;
    }

    public synchronized List<Ticket> getUserTickets(String username) {
        return tickets.stream()
                .filter(t -> t.getUsername().equals(username))
                .toList();
    }
     public synchronized boolean updateMovie(int movieId, Movie updatedMovie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId() == movieId) {
                // Giữ nguyên số ghế available
                int availableSeats = movies.get(i).getAvailableSeats();
                updatedMovie.setAvailableSeats(availableSeats);
                movies.set(i, updatedMovie);
                saveData();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteMovie(int movieId) {
        // Xóa các vé liên quan đến phim này
        tickets.removeIf(ticket -> ticket.getMovieId() == movieId);
        
        // Xóa phim
        boolean removed = movies.removeIf(movie -> movie.getId() == movieId);
        if (removed) {
            saveData();
        }
        return removed;
    }
    
    public static void main(String[] args) {
        MovieServer server = new MovieServer();
        server.start();
    }
}