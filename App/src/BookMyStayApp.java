/**
 * UseCase2RoomInitialization
 *
 * This class demonstrates basic object-oriented design using abstraction,
 * inheritance, and polymorphism in a Hotel Booking System.
 *
 * It creates different room types and displays their details along with
 * static availability.
 *
 * @author YourName
 * @version 2.1
 */
public class UseCase2RoomInitialization {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("   Hotel Booking System - v2.1      ");
        System.out.println("====================================");

        // Creating room objects (Polymorphism)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Static availability variables
        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        // Displaying room details and availability
        singleRoom.displayDetails();
        System.out.println("Available Rooms: " + singleAvailability);
        System.out.println("------------------------------------");

        doubleRoom.displayDetails();
        System.out.println("Available Rooms: " + doubleAvailability);
        System.out.println("------------------------------------");

        suiteRoom.displayDetails();
        System.out.println("Available Rooms: " + suiteAvailability);
        System.out.println("------------------------------------");

        System.out.println("Application terminating...");
    }
}

/**
 * Abstract class representing a generic Room.
 * Defines common properties and behavior for all room types.
 */
abstract class Room {

    // Encapsulated attributes
    private int numberOfBeds;
    private double pricePerNight;
    private String roomSize;

    // Constructor
    public Room(int numberOfBeds, double pricePerNight, String roomSize) {
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
        this.roomSize = roomSize;
    }

    // Getter methods (Encapsulation)
    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public String getRoomSize() {
        return roomSize;
    }

    // Abstract method
    public abstract void displayDetails();
}

/**
 * Represents a Single Room.
 */
class SingleRoom extends Room {

    public SingleRoom() {
        super(1, 2000.0, "Small");
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: Single Room");
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Price per Night: ₹" + getPricePerNight());
        System.out.println("Size: " + getRoomSize());
    }
}

/**
 * Represents a Double Room.
 */
class DoubleRoom extends Room {

    public DoubleRoom() {
        super(2, 3500.0, "Medium");
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: Double Room");
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Price per Night: ₹" + getPricePerNight());
        System.out.println("Size: " + getRoomSize());
    }
}

/**
 * Represents a Suite Room.
 */
class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 6000.0, "Large");
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: Suite Room");
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Price per Night: ₹" + getPricePerNight());
        System.out.println("Size: " + getRoomSize());
    }
}