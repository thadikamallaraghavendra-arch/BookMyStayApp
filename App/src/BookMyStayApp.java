import java.util.*;

/**
 * UseCase6RoomAllocationService
 *
 * Demonstrates booking confirmation and room allocation with:
 * - FIFO queue processing
 * - Unique room ID assignment
 * - Inventory synchronization
 *
 * @author YourName
 * @version 6.0
 */
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Hotel Booking System - v6.0      ");
        System.out.println("====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);

        // Initialize booking queue
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Guest1", "Single Room"));
        queue.addRequest(new Reservation("Guest2", "Single Room"));
        queue.addRequest(new Reservation("Guest3", "Single Room")); // exceeds availability
        queue.addRequest(new Reservation("Guest4", "Double Room"));

        // Allocation service
        BookingService bookingService = new BookingService(inventory);

        System.out.println("\nProcessing Booking Requests...\n");
        bookingService.processQueue(queue);

        System.out.println("\nFinal Inventory State:");
        inventory.displayInventory();

        System.out.println("\nApplication terminating...");
    }
}

/**
 * Reservation entity
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
 * FIFO Booking Queue
 */
class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

/**
 * Inventory Service
 */
class RoomInventory {

    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
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
 * Booking Service (Core Allocation Logic)
 */
class BookingService {

    private RoomInventory inventory;

    // Track allocated room IDs (global uniqueness)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type -> assigned room IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Process all booking requests in FIFO order
     */
    public void processQueue(BookingRequestQueue queue) {

        while (!queue.isEmpty()) {

            Reservation request = queue.getNextRequest();
            String type = request.getRoomType();

            System.out.println("Processing request for "
                    + request.getGuestName()
                    + " (" + type + ")");

            if (inventory.getAvailability(type) > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(type);

                // Ensure uniqueness (defensive)
                if (allocatedRoomIds.contains(roomId)) {
                    System.out.println("Duplicate room ID detected! Skipping...");
                    continue;
                }

                // Allocate room
                allocatedRoomIds.add(roomId);

                roomAllocations
                        .computeIfAbsent(type, k -> new HashSet<>())
                        .add(roomId);

                // Update inventory immediately (atomic step)
                inventory.decrement(type);

                System.out.println("Booking CONFIRMED for "
                        + request.getGuestName()
                        + " | Room ID: " + roomId);

            } else {
                System.out.println("Booking FAILED for "
                        + request.getGuestName()
                        + " | No rooms available");
            }

            System.out.println("------------------------------------");
        }
    }

    /**
     * Generates a unique room ID
     */
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 5);
    }
}