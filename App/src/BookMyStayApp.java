import java.util.HashMap;
import java.util.Map;

/**
 * UseCase4RoomSearch
 *
 * Demonstrates read-only room search functionality using centralized inventory.
 * Only available rooms are displayed without modifying system state.
 *
 * @author YourName
 * @version 4.0
 */
public class UseCase4RoomSearch {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Hotel Booking System - v4.0      ");
        System.out.println("====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 0); // unavailable
        inventory.addRoomType("Suite Room", 2);

        // Initialize room objects (domain model)
        Map<String, Room> roomCatalog = new HashMap<>();
        roomCatalog.put("Single Room", new SingleRoom());
        roomCatalog.put("Double Room", new DoubleRoom());
        roomCatalog.put("Suite Room", new SuiteRoom());

        // Perform search (read-only)
        RoomSearchService searchService = new RoomSearchService();
        searchService.searchAvailableRooms(inventory, roomCatalog);

        System.out.println("\nApplication terminating...");
    }
}

/**
 * RoomInventory - centralized state holder (read-only access used here)
 */
class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoomType(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }

    // Read-only method
    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    // Expose full map safely (read-only usage assumed)
    public Map<String, Integer> getAllAvailability() {
        return roomAvailability;
    }
}

/**
 * RoomSearchService - handles search logic without modifying state
 */
class RoomSearchService {

    public void searchAvailableRooms(RoomInventory inventory, Map<String, Room> roomCatalog) {

        System.out.println("\nAvailable Rooms:\n");

        for (Map.Entry<String, Integer> entry : inventory.getAllAvailability().entrySet()) {

            String roomType = entry.getKey();
            int available = entry.getValue();

            // Defensive check: only show available rooms
            if (available > 0) {

                Room room = roomCatalog.get(roomType);

                if (room != null) {
                    room.displayDetails();
                    System.out.println("Available Rooms: " + available);
                    System.out.println("------------------------------------");
                }
            }
        }
    }
}

/**
 * Abstract Room class (Domain Model)
 */
abstract class Room {

    private int numberOfBeds;
    private double pricePerNight;
    private String roomSize;

    public Room(int numberOfBeds, double pricePerNight, String roomSize) {
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
        this.roomSize = roomSize;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public abstract void displayDetails();
}

/**
 * Single Room implementation
 */
class SingleRoom extends Room {

    public SingleRoom() {
        super(1, 2000.0, "Small");
    }

    public void displayDetails() {
        System.out.println("Room Type: Single Room");
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Price per Night: ₹" + getPricePerNight());
        System.out.println("Size: " + getRoomSize());
    }
}

/**
 * Double Room implementation
 */
class DoubleRoom extends Room {

    public DoubleRoom() {
        super(2, 3500.0, "Medium");
    }

    public void displayDetails() {
        System.out.println("Room Type: Double Room");
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Price per Night: ₹" + getPricePerNight());
        System.out.println("Size: " + getRoomSize());
    }
}

/**
 * Suite Room implementation
 */
class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 6000.0, "Large");
    }

    public void displayDetails() {
        System.out.println("Room Type: Suite Room");
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Price per Night: ₹" + getPricePerNight());
        System.out.println("Size: " + getRoomSize());
    }
}