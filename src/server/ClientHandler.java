package server;

import model.Movie;
import model.Ticket;
import model.User;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket socket;
    private MovieServer server;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private User currentUser;

    public ClientHandler(Socket socket, MovieServer server) {
        this.socket = socket;
        this.server = server;
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String command = (String) input.readObject();
                switch (command) {
                    case "LOGIN":
                        handleLogin();
                        break;
                    case "REGISTER":
                        handleRegister();
                        break;
                    case "GET_MOVIES":
                        handleGetMovies();
                        break;
                    case "ADD_MOVIE":
                        handleAddMovie();
                        break;
                    case "PURCHASE_TICKET":
                        handlePurchaseTicket();
                        break;
                    case "GET_MY_TICKETS":
                        handleGetMyTickets();
                        break;
                    case "LOGOUT":
                        currentUser = null;
                        break;
                    case "EXIT":
                        return;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected: " + socket.getInetAddress());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleLogin() throws IOException, ClassNotFoundException {
        String username = (String) input.readObject();
        String password = (String) input.readObject();
        
        User user = server.login(username, password);
        if (user != null) {
            currentUser = user;
            output.writeObject("SUCCESS");
            output.writeObject(user);
        } else {
            output.writeObject("FAILURE");
        }
        output.flush();
    }

    private void handleRegister() throws IOException, ClassNotFoundException {
        String username = (String) input.readObject();
        String password = (String) input.readObject();
        
        boolean success = server.register(username, password);
        output.writeObject(success ? "SUCCESS" : "FAILURE");
        output.flush();
    }

    private void handleGetMovies() throws IOException {
        List<Movie> movies = server.getMovies();
        output.writeObject(movies);
        output.flush();
    }

    private void handleAddMovie() throws IOException, ClassNotFoundException {
        if (currentUser != null && currentUser.isAdmin()) {
            Movie movie = (Movie) input.readObject();
            boolean success = server.addMovie(movie);
            output.writeObject(success ? "SUCCESS" : "FAILURE");
        } else {
            output.writeObject("UNAUTHORIZED");
        }
        output.flush();
    }

    private void handlePurchaseTicket() throws IOException, ClassNotFoundException {
        if (currentUser != null) {
            int movieId = (Integer) input.readObject();  // SỬA: readObject thay vì readInt
            int seatNumber = (Integer) input.readObject(); // SỬA: readObject thay vì readInt
            
            boolean success = server.purchaseTicket(currentUser.getUsername(), movieId, seatNumber);
            output.writeObject(success ? "SUCCESS" : "FAILURE");
        } else {
            output.writeObject("UNAUTHORIZED");
        }
        output.flush();
    }

    private void handleGetMyTickets() throws IOException {
        if (currentUser != null) {
            List<Ticket> tickets = server.getUserTickets(currentUser.getUsername());
            output.writeObject(tickets);
        } else {
            output.writeObject("UNAUTHORIZED");
        }
        output.flush();
    }
}