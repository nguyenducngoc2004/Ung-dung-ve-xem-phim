package client;

import model.Movie;
import model.Ticket;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException; // THÊM IMPORT NÀY
import java.text.SimpleDateFormat;
import java.util.Date; // THÊM IMPORT NÀY
import java.util.List;

public class ClientGUI extends JFrame {
    private MovieClient client;
    private User currentUser;
    
    // GUI Components
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    // Login Panel
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    
    // Register Panel
    private JTextField registerUsernameField;
    private JPasswordField registerPasswordField;
    private JPasswordField registerConfirmPasswordField;
    
    // Movie Panel
    private JList<Movie> movieList;
    private DefaultListModel<Movie> movieListModel;
    private JTextArea movieDetailsArea;
    private JButton bookButton;
    private JButton viewTicketsButton;
    private JButton logoutButton;
    private JButton addMovieButton;
    
    // Add Movie Panel
    private JTextField movieTitleField;
    private JTextArea movieDescriptionArea;
    private JTextField moviePriceField;
    private JTextField movieShowtimeField;
    private JTextField movieLocationField;
    private JTextField movieSeatsField;
    
    // Tickets Panel
    private JList<Ticket> ticketsList;
    private DefaultListModel<Ticket> ticketsListModel;
    private JButton backToMoviesButton;

    public ClientGUI() {
        client = new MovieClient();
        initializeGUI();
        connectToServer();
    }

    private void initializeGUI() {
        setTitle("Movie Ticket Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createLoginPanel();
        createRegisterPanel();
        createMoviePanel();
        createAddMoviePanel();
        createTicketsPanel();

        add(mainPanel);
        setVisible(true);
    }

    private void connectToServer() {
        if (client.connect("localhost", 12345)) {
            JOptionPane.showMessageDialog(this, "Connected to server successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to connect to server!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void createLoginPanel() {
        JPanel loginPanel = new JPanel(new BorderLayout(10, 10));
        loginPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        formPanel.add(new JLabel("Username:"));
        loginUsernameField = new JTextField();
        formPanel.add(loginUsernameField);
        
        formPanel.add(new JLabel("Password:"));
        loginPasswordField = new JPasswordField();
        formPanel.add(loginPasswordField);
        
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        loginPanel.add(new JLabel("Login", JLabel.CENTER), BorderLayout.NORTH);
        loginPanel.add(formPanel, BorderLayout.CENTER);
        loginPanel.add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "Register"));
        
        loginPasswordField.addActionListener(e -> handleLogin());

        mainPanel.add(loginPanel, "Login");
    }

    private void createRegisterPanel() {
        JPanel registerPanel = new JPanel(new BorderLayout(10, 10));
        registerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        
        formPanel.add(new JLabel("Username:"));
        registerUsernameField = new JTextField();
        formPanel.add(registerUsernameField);
        
        formPanel.add(new JLabel("Password:"));
        registerPasswordField = new JPasswordField();
        formPanel.add(registerPasswordField);
        
        formPanel.add(new JLabel("Confirm Password:"));
        registerConfirmPasswordField = new JPasswordField();
        formPanel.add(registerConfirmPasswordField);
        
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        registerPanel.add(new JLabel("Register", JLabel.CENTER), BorderLayout.NORTH);
        registerPanel.add(formPanel, BorderLayout.CENTER);
        registerPanel.add(buttonPanel, BorderLayout.SOUTH);

        registerButton.addActionListener(e -> handleRegister());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        mainPanel.add(registerPanel, "Register");
    }

    private void createMoviePanel() {
        JPanel moviePanel = new JPanel(new BorderLayout(10, 10));
        moviePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Movie list
        movieListModel = new DefaultListModel<>();
        movieList = new JList<>(movieListModel);
        movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        movieList.setCellRenderer(new MovieListRenderer());
        
        JScrollPane listScrollPane = new JScrollPane(movieList);
        listScrollPane.setPreferredSize(new Dimension(300, 400));

        // Movie details
        movieDetailsArea = new JTextArea();
        movieDetailsArea.setEditable(false);
        movieDetailsArea.setLineWrap(true);
        movieDetailsArea.setWrapStyleWord(true);
        JScrollPane detailsScrollPane = new JScrollPane(movieDetailsArea);
        detailsScrollPane.setPreferredSize(new Dimension(300, 200));

        // Tooltip for movie hover
        movieList.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = movieList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    Movie movie = movieListModel.getElementAt(index);
                    movieList.setToolTipText(createMovieTooltip(movie));
                }
            }
        });

        movieList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Movie selectedMovie = movieList.getSelectedValue();
                if (selectedMovie != null) {
                    movieDetailsArea.setText(createMovieDetails(selectedMovie));
                }
            }
        });

        // Buttons
        bookButton = new JButton("Book Ticket");
        viewTicketsButton = new JButton("View My Tickets");
        logoutButton = new JButton("Logout");
        addMovieButton = new JButton("Add Movie");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(bookButton);
        buttonPanel.add(viewTicketsButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(addMovieButton);

        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, detailsScrollPane);
        splitPane.setDividerLocation(400);

        moviePanel.add(splitPane, BorderLayout.CENTER);
        moviePanel.add(buttonPanel, BorderLayout.SOUTH);

        bookButton.addActionListener(e -> handleBookTicket());
        viewTicketsButton.addActionListener(e -> showUserTickets());
        logoutButton.addActionListener(e -> handleLogout());
        addMovieButton.addActionListener(e -> cardLayout.show(mainPanel, "AddMovie"));

        mainPanel.add(moviePanel, "Movies");
    }

    private void createAddMoviePanel() {
        JPanel addMoviePanel = new JPanel(new BorderLayout(10, 10));
        addMoviePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        
        formPanel.add(new JLabel("Title:"));
        movieTitleField = new JTextField();
        formPanel.add(movieTitleField);
        
        formPanel.add(new JLabel("Description:"));
        movieDescriptionArea = new JTextArea(3, 20);
        JScrollPane descScroll = new JScrollPane(movieDescriptionArea);
        formPanel.add(descScroll);
        
        formPanel.add(new JLabel("Price:"));
        moviePriceField = new JTextField();
        formPanel.add(moviePriceField);
        
        formPanel.add(new JLabel("Showtime (yyyy-MM-dd HH:mm):"));
        movieShowtimeField = new JTextField();
        formPanel.add(movieShowtimeField);
        
        formPanel.add(new JLabel("Location:"));
        movieLocationField = new JTextField();
        formPanel.add(movieLocationField);
        
        formPanel.add(new JLabel("Total Seats:"));
        movieSeatsField = new JTextField();
        formPanel.add(movieSeatsField);

        JButton addButton = new JButton("Add Movie");
        JButton backButton = new JButton("Back to Movies");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        addMoviePanel.add(new JLabel("Add New Movie", JLabel.CENTER), BorderLayout.NORTH);
        addMoviePanel.add(formPanel, BorderLayout.CENTER);
        addMoviePanel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> handleAddMovie());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Movies"));

        mainPanel.add(addMoviePanel, "AddMovie");
    }

    private void createTicketsPanel() {
        JPanel ticketsPanel = new JPanel(new BorderLayout(10, 10));
        ticketsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        ticketsListModel = new DefaultListModel<>();
        ticketsList = new JList<>(ticketsListModel);
        ticketsList.setCellRenderer(new TicketListRenderer());
        
        JScrollPane scrollPane = new JScrollPane(ticketsList);

        backToMoviesButton = new JButton("Back to Movies");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backToMoviesButton);

        ticketsPanel.add(new JLabel("My Tickets", JLabel.CENTER), BorderLayout.NORTH);
        ticketsPanel.add(scrollPane, BorderLayout.CENTER);
        ticketsPanel.add(buttonPanel, BorderLayout.SOUTH);

        backToMoviesButton.addActionListener(e -> cardLayout.show(mainPanel, "Movies"));

        mainPanel.add(ticketsPanel, "Tickets");
    }

    private void handleLogin() {
        String username = loginUsernameField.getText().trim();
        String password = new String(loginPasswordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password!");
            return;
        }
        
        String response = client.login(username, password);
        if ("SUCCESS".equals(response)) {
            currentUser = client.getCurrentUser();
            loadMovies();
            updateUIForUser();
            cardLayout.show(mainPanel, "Movies");
        } else {
            JOptionPane.showMessageDialog(this, "Login failed! Invalid username or password.");
        }
    }

    private void handleRegister() {
        String username = registerUsernameField.getText().trim();
        String password = new String(registerPasswordField.getPassword());
        String confirmPassword = new String(registerConfirmPasswordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }
        
        String response = client.register(username, password);
        if ("SUCCESS".equals(response)) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
            cardLayout.show(mainPanel, "Login");
            clearRegisterFields();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed! Username may already exist.");
        }
    }

    private void handleBookTicket() {
        Movie selectedMovie = movieList.getSelectedValue();
        if (selectedMovie == null) {
            JOptionPane.showMessageDialog(this, "Please select a movie first!");
            return;
        }
        
        if (selectedMovie.getAvailableSeats() <= 0) {
            JOptionPane.showMessageDialog(this, "Sorry, no seats available for this movie!");
            return;
        }
        
        String seatInput = JOptionPane.showInputDialog(this, 
            "Enter seat number (1-" + selectedMovie.getTotalSeats() + "):");
        
        if (seatInput != null && !seatInput.trim().isEmpty()) {
            try {
                int seatNumber = Integer.parseInt(seatInput.trim());
                if (seatNumber < 1 || seatNumber > selectedMovie.getTotalSeats()) {
                    JOptionPane.showMessageDialog(this, "Invalid seat number!");
                    return;
                }
                
                String response = client.purchaseTicket(selectedMovie.getId(), seatNumber);
                if ("SUCCESS".equals(response)) {
                    JOptionPane.showMessageDialog(this, "Ticket booked successfully!");
                    loadMovies(); // Refresh movie list
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to book ticket. Seat may be taken.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid seat number!");
            }
        }
    }

    private void handleAddMovie() {
        if (!currentUser.isAdmin()) {
            JOptionPane.showMessageDialog(this, "Only administrators can add movies!");
            return;
        }
        
        try {
            String title = movieTitleField.getText().trim();
            String description = movieDescriptionArea.getText().trim();
            double price = Double.parseDouble(moviePriceField.getText().trim());
            String showtimeStr = movieShowtimeField.getText().trim();
            String location = movieLocationField.getText().trim();
            int totalSeats = Integer.parseInt(movieSeatsField.getText().trim());
            
            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || showtimeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            
            if (price <= 0 || totalSeats <= 0) {
                JOptionPane.showMessageDialog(this, "Price and seats must be positive numbers!");
                return;
            }
            
            // SỬA: Thêm xử lý lỗi parse date cụ thể
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date showtime;
            try {
                showtime = dateFormat.parse(showtimeStr);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format! Please use yyyy-MM-dd HH:mm");
                return;
            }
            
            // Kiểm tra ngày không được ở quá khứ
            if (showtime.before(new Date())) {
                JOptionPane.showMessageDialog(this, "Showtime cannot be in the past!");
                return;
            }
            
            // Generate a simple ID (in real app, use proper ID generation)
            int newId = movieListModel.size() + 1;
            
            Movie newMovie = new Movie(newId, title, description, price, showtime, location, totalSeats);
            String response = client.addMovie(newMovie);
            
            if ("SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(this, "Movie added successfully!");
                clearAddMovieFields();
                loadMovies();
                cardLayout.show(mainPanel, "Movies");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add movie!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter valid numbers for price and seats.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void handleLogout() {
        currentUser = null;
        client.logout();
        clearLoginFields();
        cardLayout.show(mainPanel, "Login");
    }

    private void showUserTickets() {
        List<Ticket> tickets = client.getUserTickets();
        ticketsListModel.clear();
        
        if (tickets.isEmpty()) {
            ticketsListModel.addElement(null); // Add null to show message in renderer
        } else {
            for (Ticket ticket : tickets) {
                ticketsListModel.addElement(ticket);
            }
        }
        
        cardLayout.show(mainPanel, "Tickets");
    }

    private void loadMovies() {
        List<Movie> movies = client.getMovies();
        movieListModel.clear();
        for (Movie movie : movies) {
            movieListModel.addElement(movie);
        }
    }

    private void updateUIForUser() {
        addMovieButton.setVisible(currentUser != null && currentUser.isAdmin());
    }

    private String createMovieTooltip(Movie movie) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return String.format("<html><b>%s</b><br>Price: $%.2f<br>Showtime: %s<br>Location: %s<br>Seats: %d/%d</html>",
                movie.getTitle(), movie.getPrice(), dateFormat.format(movie.getShowtime()),
                movie.getLocation(), movie.getAvailableSeats(), movie.getTotalSeats());
    }

    private String createMovieDetails(Movie movie) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return String.format("Title: %s\n\nDescription: %s\n\nPrice: $%.2f\nShowtime: %s\nLocation: %s\nAvailable Seats: %d/%d",
                movie.getTitle(), movie.getDescription(), movie.getPrice(),
                dateFormat.format(movie.getShowtime()), movie.getLocation(),
                movie.getAvailableSeats(), movie.getTotalSeats());
    }

    private void clearLoginFields() {
        loginUsernameField.setText("");
        loginPasswordField.setText("");
    }

    private void clearRegisterFields() {
        registerUsernameField.setText("");
        registerPasswordField.setText("");
        registerConfirmPasswordField.setText("");
    }

    private void clearAddMovieFields() {
        movieTitleField.setText("");
        movieDescriptionArea.setText("");
        moviePriceField.setText("");
        movieShowtimeField.setText("");
        movieLocationField.setText("");
        movieSeatsField.setText("");
    }

    // Custom renderers
    class MovieListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Movie) {
                Movie movie = (Movie) value;
                setText(String.format("%s ($%.2f) - %d seats available", 
                        movie.getTitle(), movie.getPrice(), movie.getAvailableSeats()));
            }
            return this;
        }
    }

    class TicketListRenderer extends DefaultListCellRenderer {
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value == null) {
                setText("No tickets purchased yet.");
            } else if (value instanceof Ticket) {
                Ticket ticket = (Ticket) value;
                setText(String.format("Ticket #%d - Seat %d - $%.2f - %s", 
                        ticket.getId(), ticket.getSeatNumber(), ticket.getPrice(),
                        dateFormat.format(ticket.getPurchaseDate())));
            }
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientGUI());
    }
}