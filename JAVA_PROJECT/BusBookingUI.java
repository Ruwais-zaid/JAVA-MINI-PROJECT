package JAVA_PROJECT;

import com.toedter.calendar.JCalendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;




public class BusBookingUI extends JFrame implements MouseListener {
    private JTextField customerNameField;
    private JLabel priceField;
    private JTextArea billArea;

    private JButton cancelButton;
    private JCalendar dateCalendar;
    private List<JLabel> seatLabels;
    private JPanel seatPanel;
    private JButton bookButton;

    private Connection con;
    private PreparedStatement pst;

    private JLabel usernameLabel;

    private JLabel emailLabel;

    private JTextArea bookingsTextArea;

    private JButton showBookingButton;

    private JComboBox<String> From;

    private JComboBox<String> To;

    private JLabel fromLabel;
    private JLabel toLabel;

    private Map<String, Map<String,Double>> priceMap;


    private String username;

    private String email;

    private int seatno = 0;

    public BusBookingUI() {
        initializeComponents();
        setLayout();
        setupEventHandlers();
        connect();// Establish the database connection
        initializePriceMap();
    }
    private void initializePriceMap(){
        //Initaialize routes to differnet location using double Hashmap datastructure for dynamic price
        priceMap =new HashMap<>();
        //khradi routes
        priceMap.put("Kharadi",new HashMap<>());
        priceMap.get("Kharadi").put("Hadapsar",500.00);
        priceMap.get("Kharadi").put("Swargate", 300.00);
        priceMap.get("Kharadi").put("Kothrod", 600.00);
        priceMap.get("Kharadi").put("Warje", 1200.00);
        priceMap.get("Kharadi").put("Kondhwa", 370.00);
        //Hadapsar
        priceMap.put("Hadapsar",new HashMap<>());
        priceMap.get("Hadapsar").put("Swargate",600.00);
        priceMap.get("Hadapsar").put("Kothrud", 1400.00);
        priceMap.get("Hadapsar").put("Warje", 700.00);
        priceMap.get("Hadapsar").put("Kondhwa",800.00);
        //Swargate
        priceMap.put("Swargate",new HashMap<>());
        priceMap.get("Swargate").put("Kothrud",900.00);
        priceMap.get("Swargate").put("Warje", 1200.00);
        priceMap.get("Swargate").put("Kondhwa", 800.00);
        //Kothrud
        priceMap.put("Kothrud",new HashMap<>());
        priceMap.get("Kothrud").put("Warje",1100.00);
        priceMap.get("Kothrud").put("Kondhwa", 700.00);
        //Warje
        priceMap.put("Warje",new HashMap<>());
        priceMap.get("Warje").put("Kondhwa",900.00);
    }
    private double getPrice(String from,String to){
        return priceMap.get(from).get(to);
    }

    private void initializeComponents() {
        customerNameField = new JTextField();
        priceField = new JLabel();
        billArea = new JTextArea();
        dateCalendar = new JCalendar();
        seatLabels = new ArrayList<>();
        From =new JComboBox<>();
        To =new JComboBox<>();
        fromLabel = new JLabel();
        toLabel = new JLabel();
        cancelButton=new JButton();
        showBookingButton=new JButton();
        bookButton = new JButton("BOOK");

        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Initialize seat labels
        seatPanel = new JPanel(new GridLayout(2, 6));
        for (int i = 1; i <= 12; i++) {
            JLabel seatLabel = new JLabel(String.valueOf(i));
            seatLabel.addMouseListener(this);
            seatLabels.add(seatLabel);
            seatPanel.add(seatLabel);
        }
    }

    private void setLayout() {
        setTitle("PUNE MOVERS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("PUNE MOVERS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 50));

        usernameLabel = new JLabel("Username: " + username);
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        emailLabel = new JLabel("Email: " + email);
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));

        JLabel customerLabel = new JLabel("CUSTOMER");
        customerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        cancelButton =new JButton("Cancel Booking");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        showBookingButton =new JButton("Show All Bookings");
        showBookingButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel priceLabel = new JLabel("PRICE");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel seatsLabel = new JLabel("SEATS");
        seatsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        String [] city={"Kharadi","Hadapsar","Swargate","Kothrud","Warje","Kondhwa"};
        fromLabel =new JLabel("From:");
        fromLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        From =new JComboBox<>(city);
        toLabel=new JLabel("To:    ");
        toLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        To=new JComboBox<>(city);

        JScrollPane billScrollPane = new JScrollPane(billArea);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(titleLabel)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(customerLabel)
                                        .addComponent(customerNameField, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(seatPanel)
                                        .addComponent(seatsLabel))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(fromLabel)
                                        .addComponent(From, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(toLabel)
                                        .addComponent(To, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(priceLabel)
                                        .addComponent(priceField, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                .addComponent(bookButton)
                                .addComponent(cancelButton)
                                .addComponent(showBookingButton))
                        .addComponent(dateCalendar, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                        .addComponent(billScrollPane)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(usernameLabel)
                                .addComponent(emailLabel))
        );




        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(titleLabel)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(customerLabel)
                                .addComponent(customerNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(seatsLabel)
                                .addComponent(seatPanel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(fromLabel)
                                .addComponent(From,GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(toLabel)
                                .addComponent(To,GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(priceLabel)
                                .addComponent(priceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addComponent(bookButton)
                        .addComponent(cancelButton)
                        .addComponent(showBookingButton)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(dateCalendar, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                .addComponent(billScrollPane, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(usernameLabel)
                                .addComponent(emailLabel))
        );

        pack();
    }
    public void setUserDetails(String username, String email) {
        this.username = username;
        this.email = email;
        if (usernameLabel != null && emailLabel != null) {
            usernameLabel.setText("Username: " + username);
            emailLabel.setText("Email: " + email);
        }
    }

    private void setupEventHandlers() {
        // Add action listener to the existing button
        bookButton.addActionListener(e -> handleBooking());
        cancelButton.addActionListener(e->CancelBooking());
        showBookingButton.addActionListener(e->ShowAllBooking());
    }
    private void ShowAllBooking(){
        try {
            String selectQuery = "SELECT * FROM bookings";
            PreparedStatement pst = con.prepareStatement(selectQuery);

            ResultSet resultSet = pst.executeQuery();
            StringBuilder bookingsInfo = new StringBuilder();
            bookingsInfo.append("********** All Bookings **********\n");
            while (resultSet.next()) {
                bookingsInfo.append("Customer Name: ").append(resultSet.getString("customer_name")).append("\n");
                bookingsInfo.append("Date: ").append(resultSet.getDate("booking_date")).append("\n");
                bookingsInfo.append("Seats: ").append(resultSet.getInt("seats")).append("\n");
                bookingsInfo.append("From: ").append(resultSet.getString("From")).append("\n");
                bookingsInfo.append("To: ").append(resultSet.getString("To")).append("\n");
                bookingsInfo.append("Price: ").append(resultSet.getDouble("price")).append("\n");
                bookingsInfo.append("-----------------------------------\n");
            }
            JTextArea textArea = new JTextArea(bookingsInfo.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            JOptionPane.showMessageDialog(this, scrollPane, "***************************All Bookings********************", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private List<Integer> bookedSeats = new ArrayList<>();

    private void CancelBooking() {
        String seatInput = JOptionPane.showInputDialog(this, "Enter the seat number to cancel booking:");
        if (seatInput != null && !seatInput.isEmpty()) {
            try {
                int seatToDelete = Integer.parseInt(seatInput);
                if (seatToDelete < 1 || seatToDelete > 12) {
                    JOptionPane.showMessageDialog(this, "Invalid seat number. Seat number must be between 1 and 12.");
                    return;
                }
                // Prepare the SQL statement for deleting the booking
                String deleteQuery = "DELETE FROM bookings WHERE seats = ?";
                pst = con.prepareStatement(deleteQuery);
                pst.setInt(1, seatToDelete);

                // Execute the SQL statement for deleting the booking
                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Booking for seat " + seatToDelete + " cancelled successfully!");
                    // Remove seat from bookedSeats list
                    bookedSeats.remove(Integer.valueOf(seatToDelete));
                    // Clear the seat label
                    seatLabels.get(seatToDelete - 1).setText(String.valueOf(seatToDelete));
                } else {
                    JOptionPane.showMessageDialog(this, "No booking found for seat " + seatToDelete);
                }
            } catch (NumberFormatException | SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            } finally {
                try {
                    // Close the resources
                    if (pst != null) {
                        pst.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(BusBookingUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No seat number entered.");
        }
    }
    private void handleBooking() {
        StringBuilder billBuilder = new StringBuilder();
        billBuilder.append("Customer Name: ").append(customerNameField.getText()).append("\n");
        billBuilder.append("Date: ").append(dateCalendar.getCalendar().getTime()).append("\n");
        billBuilder.append("Selected Seats: ").append(seatno).append("\n");
        billBuilder.append("From: ").append((String) From.getSelectedItem()).append("\n");
        billBuilder.append("To:   ").append((String) To.getSelectedItem()).append("\n");
        double price = getPrice((String) From.getSelectedItem(), (String) To.getSelectedItem());
        priceField.setText(String.valueOf(price));
        priceField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        billBuilder.append("Total Price: ").append(price);
        billArea.setText(""); // Clear previous conten
        billArea.append("*********************** BILL **********************\n");
        billArea.append(billBuilder.toString());
        billArea.append("\n");
        billArea.append("\n THANKS FRO VISITING OUR APPLICATION");

        try {
            // Prepare the SQL statement for inserting data
            String insertQuery = "INSERT INTO bookings (customer_name, booking_date, seats, price, `From`, `To`) VALUES (?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(insertQuery);
            pst.setString(1, customerNameField.getText());
            pst.setDate(2, new java.sql.Date(dateCalendar.getCalendar().getTime().getTime()));
            pst.setInt(3, seatno);
            pst.setDouble(4, Double.parseDouble(priceField.getText()));
            pst.setString(5, (String) From.getSelectedItem());
            pst.setString(6, (String) To.getSelectedItem());

            // Execute the SQL statement for inserting data
            int k = pst.executeUpdate();
            if (k == 1) {
                JOptionPane.showMessageDialog(this, "Seat Booked");
            } else {
                JOptionPane.showMessageDialog(this, "Something is Wrong");
            }

            JOptionPane.showMessageDialog(this, "Booking successful!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        } finally {
            try {
                // Close the resources
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BusBookingUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bus", "root", "Zaid@123");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserRegistration userRegistration = new UserRegistration();
            userRegistration.setVisible(true);
            userRegistration.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    if (userRegistration.isRegistrationSuccessful()) {
                        BusBookingUI bookingUI = new BusBookingUI();
                        bookingUI.setVisible(true);
                    }
                }
            });
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < seatLabels.size(); i++) {
            if (e.getSource() == seatLabels.get(i)) {
                seatno = i + 1;
                if (bookedSeats.contains(seatno)) {
                    JOptionPane.showMessageDialog(this, "Seat " + seatno+ " is already booked!");
                    break;
                } else {
                    bookedSeats.add(seatno);
                    JOptionPane.showMessageDialog(this, "Selected seat number: " + seatno);
                }
                break;
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
