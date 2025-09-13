package client;

import model.Movie;
import model.Ticket;
import model.User;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class MovieClient {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private User currentUser;

    public boolean connect(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String login(String username, String password) {
        try {
            output.writeObject("LOGIN");
            output.writeObject(username);
            output.writeObject(password);
            output.flush();
            
            String response = (String) input.readObject();
            if ("SUCCESS".equals(response)) {
                currentUser = (User) input.readObject();
            }
            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "FAILURE";
        }
    }

    public String register(String username, String password) {
        try {
            output.writeObject("REGISTER");
            output.writeObject(username);
            output.writeObject(password);
            output.flush();
            
            return (String) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "FAILURE";
        }
    }

    public List<Movie> getMovies() {
        try {
            output.writeObject("GET_MOVIES");
            output.flush();
            
            return (List<Movie>) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public String purchaseTicket(int movieId, int seatNumber) {
        try {
            output.writeObject("PURCHASE_TICKET");
            output.writeObject(movieId);  // SỬA: writeObject thay vì writeInt
            output.writeObject(seatNumber); // SỬA: writeObject thay vì writeInt
            output.flush();
            
            return (String) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "FAILURE";
        }
    }

    public List<Ticket> getUserTickets() {
        try {
            output.writeObject("GET_MY_TICKETS");
            output.flush();
            
            return (List<Ticket>) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public String addMovie(Movie movie) {
        try {
            output.writeObject("ADD_MOVIE");
            output.writeObject(movie);
            output.flush();
            
            return (String) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "FAILURE";
        }
    }

    public void logout() {
        try {
            output.writeObject("LOGOUT");
            output.flush();
            currentUser = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                output.writeObject("EXIT");
                output.flush();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}