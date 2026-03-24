import java.util.*;

/**
 * UseCase10BookingCancellation
 *
 * Demonstrates safe cancellation of bookings with inventory rollback.
 * Ensures system consistency using stack-based rollback logic.
 *
 * @author YourName
 * @version 10.0
 */
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Hotel Booking System - v10.0     ");
        System.out.println("====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);

        // Simulate confirmed bookings
        BookingService bookingService = new BookingService(inventory);
        bookingService.confirmBooking("RES-101", "Guest1", "Single Room");
        bookingService.confirmBooking("RES-102", "Guest2", "Single Room");

        // Cancellation service
        CancellationService cancellationService = new CancellationService(inventory, bookingService);

        // Perform cancellations
        System.out.println("\nProcessing Cancellations...\n");

        cancellationService.cancelBooking("RES-102"); // valid
        cancellationService.cancelBooking("RES-999"); // invalid
        cancellationService.cancelBooking("RES-102"); // already cancelled

        System.out.println("\nFinal Inventory State:");
        inventory.displayInventory();

        System.out.println("\nApplication terminating...");
    }
}

/**
 * RoomInventory
 */
class RoomInventory {

    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void increment(String type) {
        availability.put(type, getAvailability(type) + 1);
    }

    public void decrement(String type) {
        availability.put(type, getAvailability(type) - 1);
    }

    public void displayInventory() {
        for (Map.Entry<String, Integer> e : availability.entrySet()) {
            System.out.println(e.getKey() + " -> Available: " + e.getValue());
        }
    }
}

/**
 * BookingService (simplified allocation tracker)
 */
class BookingService {

    private RoomInventory inventory;

    // ReservationId -> RoomType
    private Map<String, String> reservations = new HashMap<>();

    // Track allocated room IDs
    private Map<String, String> roomAssignments = new HashMap<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(String resId, String guest, String type) {

        if (inventory.getAvailability(type) <= 0) {
            System.out.println("Booking failed for " + guest + " (No availability)");
            return;
        }

        String roomId = type.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 5);

        reservations.put(resId, type);
        roomAssignments.put(resId, roomId);

        inventory.decrement(type);

        System.out.println("Booking CONFIRMED: " + resId + " | Room ID: " + roomId);
    }

    public boolean exists(String resId) {
        return reservations.containsKey(resId);
    }

    public String getRoomType(String resId) {
        return reservations.get(resId);
    }

    public String removeReservation(String resId) {
        roomAssignments.remove(resId);
        return reservations.remove(resId);
    }
}

/**
 * CancellationService
 */
class CancellationService {

    private RoomInventory inventory;
    private BookingService bookingService;

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    // Track cancelled reservations
    private Set<String> cancelledReservations = new HashSet<>();

    public CancellationService(RoomInventory inventory, BookingService bookingService) {
        this.inventory = inventory;
        this.bookingService = bookingService;
    }

    public void cancelBooking(String resId) {

        System.out.println("Cancelling: " + resId);

        // Validate existence
        if (!bookingService.exists(resId)) {
            System.out.println("Cancellation FAILED: Reservation does not exist");
            return;
        }

        // Prevent duplicate cancellation
        if (cancelledReservations.contains(resId)) {
            System.out.println("Cancellation FAILED: Already cancelled");
            return;
        }

        // Get room type
        String roomType = bookingService.getRoomType(resId);

        // Push to rollback stack
        rollbackStack.push(resId);

        // Remove reservation
        bookingService.removeReservation(resId);

        // Restore inventory
        inventory.increment(roomType);

        // Mark as cancelled
        cancelledReservations.add(resId);

        System.out.println("Cancellation SUCCESS for " + resId);
        System.out.println("Inventory restored for " + roomType);
        System.out.println("------------------------------------");
    }
}