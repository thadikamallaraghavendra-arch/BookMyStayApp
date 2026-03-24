import java.io.*;
import java.util.*;

/**
 * UseCase12DataPersistenceRecovery
 *
 * Demonstrates saving and restoring booking and inventory state
 * using Java serialization to ensure recovery after application restart.
 *
 * Author: YourName
 * Version: 12.0
 */
public class UseCase12DataPersistenceRecovery {

    private static final String DATA_FILE = "hotel_system_state.ser";

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Hotel Booking System - v12.0     ");
        System.out.println("====================================");

        SystemState state;

        // Attempt to restore previous state
        state = PersistenceService.restoreState(DATA_FILE);

        if (state == null) {
            System.out.println("No previous state found. Initializing new system...");
            state = new SystemState();
            state.inventory.addRoomType("Single Room", 3);
            state.inventory.addRoomType("Double Room", 2);
        } else {
            System.out.println("Previous state successfully restored!");
        }

        System.out.println("\nCurrent Inventory:");
        state.inventory.displayInventory();

        System.out.println("\nProcessing sample booking...");
        Reservation res = new Reservation("Guest1", "Single Room");
        boolean booked = state.bookRoom(res);

        if (booked) {
            System.out.println("Booking SUCCESS: " + res.getGuestName());
        } else {
            System.out.println("Booking FAILED: " + res.getGuestName());
        }

        System.out.println("\nSaving system state before shutdown...");
        PersistenceService.saveState(DATA_FILE, state);

        System.out.println("System state saved. Application terminating...");
    }
}

/**
 * SystemState
 *
 * Holds inventory and booking history together
 */
class SystemState implements Serializable {

    public ConcurrentRoomInventory inventory;
    public List<Reservation> bookingHistory;

    public SystemState() {
        this.inventory = new ConcurrentRoomInventory();
        this.bookingHistory = new ArrayList<>();
    }

    /**
     * Attempt to book a room and update history
     */
    public boolean bookRoom(Reservation reservation) {
        synchronized (inventory) {
            boolean success = inventory.allocateRoom(reservation.getRoomType());
            if (success) {
                bookingHistory.add(reservation);
            }
            return success;
        }
    }
}

/**
 * Reservation entity
 */
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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
 * Thread-safe room inventory
 */
class ConcurrentRoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> availability = new HashMap<>();

    public synchronized void addRoomType(String type, int count) {
        availability.put(type, count);
    }

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
 * PersistenceService
 *
 * Handles saving and restoring system state to/from a file
 */
class PersistenceService {

    public static void saveState(String fileName, SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(state);
            System.out.println("System state saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Failed to save system state: " + e.getMessage());
        }
    }

    public static SystemState restoreState(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (SystemState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to restore system state: " + e.getMessage());
            return null;
        }
    }
}