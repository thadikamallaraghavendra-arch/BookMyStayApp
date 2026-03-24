import java.util.*;

/**
 * UseCase8BookingHistoryReport
 *
 * Demonstrates how confirmed bookings are stored and used
 * for reporting and administrative review.
 *
 * @author YourName
 * @version 8.0
 */
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Hotel Booking System - v8.0      ");
        System.out.println("====================================");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        history.addReservation(new Reservation("RES-101", "Guest1", "Single Room"));
        history.addReservation(new Reservation("RES-102", "Guest2", "Double Room"));
        history.addReservation(new Reservation("RES-103", "Guest3", "Suite Room"));

        // Admin requests reports
        BookingReportService reportService = new BookingReportService();

        System.out.println("\n--- Booking History ---");
        reportService.displayAllBookings(history);

        System.out.println("\n--- Booking Summary Report ---");
        reportService.generateSummaryReport(history);

        System.out.println("\nApplication terminating...");
    }
}

/**
 * Reservation Entity
 * Represents a confirmed booking.
 */
class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * BookingHistory
 *
 * Stores confirmed reservations in insertion order.
 */
class BookingHistory {

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    /**
     * Add confirmed reservation to history
     */
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    /**
     * Retrieve all reservations (read-only usage expected)
     */
    public List<Reservation> getAllReservations() {
        return reservations;
    }
}

/**
 * BookingReportService
 *
 * Handles reporting logic without modifying booking history.
 */
class BookingReportService {

    /**
     * Display all bookings
     */
    public void displayAllBookings(BookingHistory history) {
        for (Reservation r : history.getAllReservations()) {
            System.out.println(
                    r.getReservationId() + " | "
                            + r.getGuestName() + " | "
                            + r.getRoomType()
            );
        }
    }

    /**
     * Generate summary report (count per room type)
     */
    public void generateSummaryReport(BookingHistory history) {

        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            summary.put(
                    r.getRoomType(),
                    summary.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
            System.out.println(entry.getKey() + " -> Total Bookings: " + entry.getValue());
        }
    }
}