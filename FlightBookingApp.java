
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

// Main class
public class FlightBookingApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FlightBookingSystem system = new FlightBookingSystem(sc);
        system.run();
        sc.close();
    }
}

// Flight class
class Flight {
    private String code;
    private String airline;
    private String source;
    private String destination;
    private int availableSeats;
    private double baseFare;

    public Flight(String code, String airline, String source,
            String destination, int seats, double fare) {
        this.code = code;
        this.airline = airline;
        this.source = source;
        this.destination = destination;
        this.availableSeats = seats;
        this.baseFare = fare;
    }

    public String getCode() {
        return code;
    }

    public String getAirline() {
        return airline;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public boolean hasSeats(int count) {
        return count > 0 && count <= availableSeats;
    }

    public void reserveSeats(int count) {
        if (hasSeats(count)) {
            availableSeats -= count;
        }
    }

    public void displayFlight() {
        System.out.printf(
                "%-8s %-15s %-12s %-12s %-8d Rs.%.2f%n",
                code, airline, source, destination,
                availableSeats, baseFare);
    }
}

// Abstract Passenger class
abstract class Passenger {
    private String name;
    private int age;

    public Passenger(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public abstract double getDiscountPercentage();

    public abstract String getCategory();
}

// Regular passenger
class RegularPassenger extends Passenger {
    public RegularPassenger(String name, int age) {
        super(name, age);
    }

    @Override
    public double getDiscountPercentage() {
        return 0.0;
    }

    @Override
    public String getCategory() {
        return "Regular";
    }
}

// Student passenger
class StudentPassenger extends Passenger {
    public StudentPassenger(String name, int age) {
        super(name, age);
    }

    @Override
    public double getDiscountPercentage() {
        return 10.0;
    }

    @Override
    public String getCategory() {
        return "Student";
    }
}

// Booking class
class Booking {
    private String bookingId;
    private Flight flight;
    private Passenger passenger;
    private int ticketCount;
    private double baseAmount;
    private double discount;
    private double totalFare;

    public Booking(Flight flight, Passenger passenger, int count) {
        this.bookingId = "BK" +
                UUID.randomUUID().toString()
                        .substring(0, 8).toUpperCase();

        this.flight = flight;
        this.passenger = passenger;
        this.ticketCount = count;

        this.baseAmount = flight.getBaseFare() * count;
        this.discount = baseAmount *
                passenger.getDiscountPercentage() / 100.0;
        this.totalFare = baseAmount - discount;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void displaySummary() {
        System.out.println("\n========== BOOKING SUMMARY ==========");
        System.out.println("Booking ID     : " + bookingId);
        System.out.println("Passenger Name : " + passenger.getName());
        System.out.println("Age            : " + passenger.getAge());
        System.out.println("Category       : " + passenger.getCategory());
        System.out.println("Flight Code    : " + flight.getCode());
        System.out.println("Airline        : " + flight.getAirline());
        System.out.println("Route          : " + flight.getSource()
                + " to " + flight.getDestination());
        System.out.println("Tickets        : " + ticketCount);
        System.out.printf("Base Fare      : Rs.%.2f per ticket%n",
                flight.getBaseFare());
        System.out.printf("Base Amount    : Rs.%.2f%n", baseAmount);
        System.out.printf("Discount       : Rs.%.2f (%.0f%%)%n",
                discount, passenger.getDiscountPercentage());
        System.out.printf("Final Amount   : Rs.%.2f%n", totalFare);
        System.out.println("=====================================");
    }
}

// Main booking system
class FlightBookingSystem {
    private Scanner sc;
    private List<Flight> flights;
    private Booking latestBooking;

    public FlightBookingSystem(Scanner sc) {
        this.sc = sc;
        flights = new ArrayList<>();

        flights.add(new Flight(
                "AI101", "Air India", "Mumbai",
                "Delhi", 40, 4500));

        flights.add(new Flight(
                "6E202", "IndiGo", "Pune",
                "Bengaluru", 30, 3500));

        flights.add(new Flight(
                "UK303", "Vistara", "Delhi",
                "Chennai", 25, 5500));
    }

    // Main menu
    public void run() {
        int choice;

        do {
            System.out.println("\n===== FLIGHT BOOKING SYSTEM =====");
            System.out.println("1. View Flights");
            System.out.println("2. Book Flight");
            System.out.println("3. View Booking");
            System.out.println("4. Exit");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    displayFlights();
                    break;

                case 2:
                    confirmBooking();
                    break;

                case 3:
                    displayLatestBooking();
                    break;

                case 4:
                    System.out.println("Thank you for using our Flight Booking System!");
                    break;

                default:
                    System.out.println("Invalid menu choice. Please select 1 to 4.");
            }
        } while (choice != 4);
    }

    // Display all flights
    public void displayFlights() {
        System.out.println("\n================ AVAILABLE FLIGHTS ================");
        System.out.printf("%-8s %-15s %-12s %-12s %-8s %s%n",
                "Code", "Airline", "Source", "Destination",
                "Seats", "Base Fare");

        for (Flight flight : flights) {
            flight.displayFlight();
        }
    }

    // Find flight using its code
    public Flight findFlightByCode(String code) {
        for (Flight flight : flights) {
            if (flight.getCode().equalsIgnoreCase(code)) {
                return flight;
            }
        }
        return null;
    }

    // Validate booking details
    public boolean validateBookingDetails(
            Flight flight, String name, int age, int count) {

        if (flight == null) {
            System.out.println("Error: Invalid flight code.");
            return false;
        }

        if (name.trim().isEmpty()) {
            System.out.println("Error: Passenger name cannot be empty.");
            return false;
        }

        if (age < 1 || age > 120) {
            System.out.println("Error: Enter a valid age (1-120).");
            return false;
        }

        if (count <= 0) {
            System.out.println("Error: Number of tickets must be positive.");
            return false;
        }

        if (!flight.hasSeats(count)) {
            System.out.println("Error: Not enough seats available.");
            return false;
        }

        return true;
    }

    // Confirm booking only after validation
    public void confirmBooking() {
        displayFlights();

        System.out.print("\nEnter flight code: ");
        String code = sc.nextLine().trim();

        Flight flight = findFlightByCode(code);

        System.out.print("Enter passenger name: ");
        String name = sc.nextLine().trim();

        int age = readInt("Enter passenger age: ");
        int count = readInt("Enter number of tickets: ");

        System.out.println("Passenger category:");
        System.out.println("1. Regular Passenger (0% discount)");
        System.out.println("2. Student Passenger (10% discount)");

        int category = readInt("Choose category: ");

        if (category != 1 && category != 2) {
            System.out.println("Error: Invalid passenger category.");
            return;
        }

        if (!validateBookingDetails(flight, name, age, count)) {
            return;
        }

        Passenger passenger;

        if (category == 1) {
            passenger = new RegularPassenger(name, age);
        } else {
            passenger = new StudentPassenger(name, age);
        }

        // Create booking and update seats after validation
        Booking booking = new Booking(flight, passenger, count);
        flight.reserveSeats(count);
        latestBooking = booking;

        System.out.println("\nBooking confirmed successfully!");
        booking.displaySummary();
    }

    // Display most recent booking
    public void displayLatestBooking() {
        if (latestBooking == null) {
            System.out.println("No confirmed booking found.");
        } else {
            latestBooking.displaySummary();
        }
    }

    // Safely read integer input
    private int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid whole number.");
            }
        }
    }
}
