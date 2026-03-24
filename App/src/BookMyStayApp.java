import java.util.LinkedList;
import java.util.Queue;

/**
 * UseCase5BookingRequestQueue
 *
 * Demonstrates booking request handling using FIFO Queue.
 * Requests are stored in arrival order without modifying inventory.
 *
 * @author YourName
 * @version 5.0
 */
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Hotel Booking System - v5.0      ");
        System.out.println("====================================");

        // Initialize booking request queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Simulating guest booking requests
        requestQueue.addRequest(new Reservation("Guest1", "Single Room"));
        requestQueue.addRequest(new Reservation("Guest2", "Double Room"));
        requestQueue.addRequest(new Reservation("Guest3", "Suite Room"));
        requestQueue.addRequest(new Reservation("Guest4", "Single Room"));

        // Display queued requests
        System.out.println("\nBooking Requests in Queue:");
        requestQueue.displayQueue();

        System.out.println("\nAll requests are queued (no allocation done).");
        System.out.println("Application terminating...");
    }
}

/**
 * Reservation
 *
 * Represents a guest's booking request.
 */
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * BookingRequestQueue
 *
 * Handles FIFO booking requests using Queue.
 */
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    /**
     * Adds a booking request to the queue.
     */
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added: "
                + reservation.getGuestName()
                + " -> "
                + reservation.getRoomType());
    }

    /**
     * Displays all queued requests in order.
     */
    public void displayQueue() {
        for (Reservation r : queue) {
            System.out.println(r.getGuestName() + " requested " + r.getRoomType());
        }
    }
}