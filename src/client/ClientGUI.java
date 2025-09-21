package client;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Movie;
import model.Ticket;
import model.User;

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
    // Thêm các thành phần cho quản lý phim
    private JButton editMovieButton;
    private JButton deleteMovieButton;
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
        loginPanel.setBackground(new Color(240, 240, 240));

        // Panel chính chứa form
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(240, 240, 240));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        // Tiêu đề
        JLabel titleLabel = new JLabel("XENDRE", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(75, 0, 130)); // Màu tím đậm
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 240, 240));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        JLabel userLabel = new JLabel("USERNAME:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userLabel.setForeground(new Color(75, 0, 130));
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(userLabel, gbc);

        loginUsernameField = new JTextField(20);
        loginUsernameField.setPreferredSize(new Dimension(250, 35));
        loginUsernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(loginUsernameField, gbc);

        // Password
        JLabel passLabel = new JLabel("PASSWORD:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passLabel.setForeground(new Color(75, 0, 130));
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(passLabel, gbc);

        loginPasswordField = new JPasswordField(20);
        loginPasswordField.setPreferredSize(new Dimension(250, 35));
        loginPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(loginPasswordField, gbc);

        // Nút Login
        JButton loginButton = new JButton("LOGIN");
        loginButton.setBackground(new Color(75, 0, 130));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(loginButton, gbc);

        // Nút Signup
        JButton registerButton = new JButton("SIGNUP");
        registerButton.setBackground(Color.WHITE);
        registerButton.setForeground(new Color(75, 0, 130));
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(75, 0, 130)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.insets = new Insets(10, 10, 10, 10);
        formPanel.add(registerButton, gbc);

        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(formPanel, BorderLayout.CENTER);

        loginPanel.add(centerPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "Register"));
        
        loginPasswordField.addActionListener(e -> handleLogin());

        mainPanel.add(loginPanel, "Login");
    }
     private void createRegisterPanel() {
        JPanel registerPanel = new JPanel(new BorderLayout(10, 10));
        registerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        registerPanel.setBackground(new Color(240, 240, 240));

        // Panel chính chứa form
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(240, 240, 240));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // Tiêu đề
        JLabel titleLabel = new JLabel("CREATE ACCOUNT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(75, 0, 130));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 240, 240));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Username
        JLabel userLabel = new JLabel("USERNAME:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userLabel.setForeground(new Color(75, 0, 130));
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(userLabel, gbc);

        registerUsernameField = new JTextField(20);
        registerUsernameField.setPreferredSize(new Dimension(250, 35));
        registerUsernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(registerUsernameField, gbc);

        // Password
        JLabel passLabel = new JLabel("PASSWORD:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passLabel.setForeground(new Color(75, 0, 130));
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(passLabel, gbc);

        registerPasswordField = new JPasswordField(20);
        registerPasswordField.setPreferredSize(new Dimension(250, 35));
        registerPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(registerPasswordField, gbc);

        // Confirm Password
        JLabel confirmPassLabel = new JLabel("CONFIRM PASSWORD:");
        confirmPassLabel.setFont(new Font("Arial", Font.BOLD, 12));
        confirmPassLabel.setForeground(new Color(75, 0, 130));
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(confirmPassLabel, gbc);

        registerConfirmPasswordField = new JPasswordField(20);
        registerConfirmPasswordField.setPreferredSize(new Dimension(250, 35));
        registerConfirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(registerConfirmPasswordField, gbc);

        // Nút Register
        JButton registerButton = new JButton("SIGN UP");
        registerButton.setBackground(new Color(75, 0, 130));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(registerButton, gbc);

        // Nút Back
        JButton backButton = new JButton("BACK TO LOGIN");
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(new Color(75, 0, 130));
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(75, 0, 130)),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.insets = new Insets(10, 10, 10, 10);
        formPanel.add(backButton, gbc);

        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(formPanel, BorderLayout.CENTER);

        registerPanel.add(centerPanel, BorderLayout.CENTER);

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
                    updateMovieButtons(selectedMovie);
                }
            }
        });

        // Buttons
        bookButton = new JButton("Book Ticket");
        viewTicketsButton = new JButton("View My Tickets");
        logoutButton = new JButton("Logout");
        addMovieButton = new JButton("Add Movie");
        editMovieButton = new JButton("Edit Movie");
        deleteMovieButton = new JButton("Delete Movie");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(bookButton);
        buttonPanel.add(viewTicketsButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(addMovieButton);
        buttonPanel.add(editMovieButton);
        buttonPanel.add(deleteMovieButton);

        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, detailsScrollPane);
        splitPane.setDividerLocation(400);

        moviePanel.add(splitPane, BorderLayout.CENTER);
        moviePanel.add(buttonPanel, BorderLayout.SOUTH);

        bookButton.addActionListener(e -> handleBookTicket());
        viewTicketsButton.addActionListener(e -> showUserTickets());
        logoutButton.addActionListener(e -> handleLogout());
        addMovieButton.addActionListener(e -> cardLayout.show(mainPanel, "AddMovie"));
        editMovieButton.addActionListener(e -> handleEditMovie());
        deleteMovieButton.addActionListener(e -> handleDeleteMovie());

        mainPanel.add(moviePanel, "Movies");
    }
     private void updateMovieButtons(Movie movie) {
        boolean isAdmin = currentUser != null && currentUser.isAdmin();
        boolean hasTickets = movie.getAvailableSeats() < movie.getTotalSeats();
        
        editMovieButton.setVisible(isAdmin);
        deleteMovieButton.setVisible(isAdmin && !hasTickets); // Chỉ xóa được phim chưa có vé
    }

      private void handleEditMovie() {
        Movie selectedMovie = movieList.getSelectedValue();
        if (selectedMovie == null) {
            JOptionPane.showMessageDialog(this, "Please select a movie to edit!");
            return;
        }
      // Hiển thị dialog chỉnh sửa (có thể tạo panel riêng giống AddMovie)
        JDialog editDialog = new JDialog(this, "Edit Movie", true);
        editDialog.setSize(400, 500);
        editDialog.setLocationRelativeTo(this);

        // Tạo form chỉnh sửa tương tự AddMoviePanel
        // [Code tạo form ở đây - giống AddMovie nhưng với giá trị hiện tại]

        editDialog.setVisible(true);
      }
        private void handleDeleteMovie() {
        Movie selectedMovie = movieList.getSelectedValue();
        if (selectedMovie == null) {
            JOptionPane.showMessageDialog(this, "Please select a movie to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete '" + selectedMovie.getTitle() + "'?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String response = client.deleteMovie(selectedMovie.getId());
            if ("SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(this, "Movie deleted successfully!");
                loadMovies();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete movie!");
            }
        }
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
        boolean isAdmin = currentUser != null && currentUser.isAdmin();
        addMovieButton.setVisible(isAdmin);
        editMovieButton.setVisible(isAdmin);
        deleteMovieButton.setVisible(isAdmin);
        bookButton.setVisible(!isAdmin); // Admin không đặt vé
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
            // HIỂN THỊ ĐÚNG SỐ VÉ CÒN LẠI
            setText(String.format("%s ($%.2f) - %d/%d seats available", 
                    movie.getTitle(), movie.getPrice(), 
                    movie.getAvailableSeats(), movie.getTotalSeats()));
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