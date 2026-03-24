import java.util.*;
import java.util.concurrent.*;

/**
 * UseCase11ConcurrentBookingSimulation
 *
 * Demonstrates thread-safe booking handling under concurrent guest requests.
 * Synchronization ensures inventory consistency and prevents double allocation.
 *
 * Author: YourName
 * Version: 11.0
 */
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("====================================");
        System.out.println("   Hotel Booking System - v11.0     ");
        System.out.println("====================================");

        // Initialize shared inventory
        ConcurrentRoomInventory inventory = new ConcurrentRoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);

        // Shared booking queue
        BlockingQueue<ReservationRequest> bookingQueue = new LinkedBlockingQueue<>();

        // Simulate multiple guests submitting requests
        bookingQueue.add(new ReservationRequest("Guest1", "Single Room"));
        bookingQueue.add(new ReservationRequest("Guest2", "Single Room"));
        bookingQueue.add(new ReservationRequest("Guest3", "Single Room")); // Only 2 available
        bookingQueue.add(new ReservationRequest("Guest4", "Double Room"));
        bookingQueue.add(new ReservationRequest("Guest5", "Double Room")); // Only 1 available

        // Thread pool to simulate concurrent processing
        int numThreads = 3;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(new BookingProcessor(bookingQueue, inventory));
        }

        // Shutdown executor and wait for threads to finish
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\nFinal Inventory State:");
        inventory.displayInventory();

        System.out.println("\nApplication terminating...");
    }
}

/**
 * Reservation request entity
 */
class ReservationRequest {
    private String guestName;
    private String roomType;

    public ReservationRequest(String guestName, String roomType) {
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
 * Thread-safe room inventory
 */
class ConcurrentRoomInventory {

    private final Map<String, Integer> availability = new HashMap<>();

    public synchronized void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    /**
     * Thread-safe decrement for allocation
     */
    public synchronized boolean allocateRoom(String type) {
        int current = availability.getOrDefault(type, 0);
        if (current > 0) {
            availability.put(type, current - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        for (Map.Entry<String, Integer> e : availability.entrySet()) {
            System.out.println(e.getKey() + " -> Available: " + e.getValue());
        }
    }
}

/**
 * Booking processor runnable
 */
class BookingProcessor implements Runnable {

    private final BlockingQueue<ReservationRequest> queue;
    private final ConcurrentRoomInventory inventory;

    public BookingProcessor(BlockingQueue<ReservationRequest> queue, ConcurrentRoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (!queue.isEmpty()) {
            ReservationRequest request = queue.poll();
            if (request == null) continue;

            String guest = request.getGuestName();
            String room = request.getRoomType();

            // Critical section handled inside allocateRoom
            boolean success = inventory.allocateRoom(room);

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " -> Booking SUCCESS: " + guest + " | " + room);
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " -> Booking FAILED: " + guest + " | " + room + " (No availability)");
            }

            // Simulate processing delay
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}