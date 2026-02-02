package com.austria.logistics.models.vehicles;

import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.vehicles.contracts.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TruckImplTest {

    private static final int TRUCK_ID = 1001;
    private Truck scaniaTruck;
    private Truck manTruck;
    private Truck actrosTruck;

    @BeforeEach
    void setUp() {
        scaniaTruck = new TruckImpl(TRUCK_ID, TruckType.SCANIA);
        manTruck = new TruckImpl(1011, TruckType.MAN);
        actrosTruck = new TruckImpl(1026, TruckType.ACTROS);
    }

    // Constructor tests
    @Test
    void constructor_Should_InitializeTruck_When_ArgumentsAreValid() {
        // Arrange, Act
        Truck truck = new TruckImpl(1001, TruckType.SCANIA);

        // Assert
        assertAll(
                () -> assertEquals(1001, truck.getId()),
                () -> assertEquals(TruckType.SCANIA, truck.getTruckType()),
                () -> assertFalse(truck.isAssigned()),
                () -> assertEquals(0, truck.getCurrentLoad())
        );
    }

    @Test
    void constructor_Should_InitializeWithDifferentTruckTypes() {
        // Assert
        assertAll(
                () -> assertEquals(TruckType.SCANIA, scaniaTruck.getTruckType()),
                () -> assertEquals(TruckType.MAN, manTruck.getTruckType()),
                () -> assertEquals(TruckType.ACTROS, actrosTruck.getTruckType())
        );
    }

    // getId tests
    @Test
    void getId_Should_ReturnCorrectId() {
        // Assert
        assertEquals(TRUCK_ID, scaniaTruck.getId());
    }

    // getTruckType tests
    @Test
    void getTruckType_Should_ReturnCorrectTruckType() {
        // Assert
        assertEquals(TruckType.SCANIA, scaniaTruck.getTruckType());
    }

    // isAssigned tests
    @Test
    void isAssigned_Should_ReturnFalse_When_TruckIsNew() {
        // Assert
        assertFalse(scaniaTruck.isAssigned());
    }

    @Test
    void isAssigned_Should_ReturnTrue_When_TruckIsAssigned() {
        // Arrange
        scaniaTruck.assign();

        // Assert
        assertTrue(scaniaTruck.isAssigned());
    }

    // getCurrentLoad tests
    @Test
    void getCurrentLoad_Should_ReturnZero_When_TruckIsNew() {
        // Assert
        assertEquals(0, scaniaTruck.getCurrentLoad());
    }

    @Test
    void getCurrentLoad_Should_ReturnCorrectLoad_After_AddingLoad() {
        // Arrange
        scaniaTruck.addLoad(5000);

        // Assert
        assertEquals(5000, scaniaTruck.getCurrentLoad());
    }

    // getMaxCapacity tests
    @Test
    void getMaxCapacity_Should_ReturnCorrectCapacity_ForScania() {
        // Assert - SCANIA capacity is 42000
        assertEquals(42000, scaniaTruck.getMaxCapacity());
    }

    @Test
    void getMaxCapacity_Should_ReturnCorrectCapacity_ForMan() {
        // Assert - MAN capacity is 37000
        assertEquals(37000, manTruck.getMaxCapacity());
    }

    @Test
    void getMaxCapacity_Should_ReturnCorrectCapacity_ForActros() {
        // Assert - ACTROS capacity is 26000
        assertEquals(26000, actrosTruck.getMaxCapacity());
    }

    // addLoad tests
    @Test
    void addLoad_Should_IncreaseCurrentLoad_When_LoadIsAdded() {
        // Arrange
        int initialLoad = scaniaTruck.getCurrentLoad();

        // Act
        scaniaTruck.addLoad(10000);

        // Assert
        assertEquals(initialLoad + 10000, scaniaTruck.getCurrentLoad());
    }

    @Test
    void addLoad_Should_AccumulateLoad_When_CalledMultipleTimes() {
        // Arrange, Act
        scaniaTruck.addLoad(5000);
        scaniaTruck.addLoad(3000);
        scaniaTruck.addLoad(2000);

        // Assert
        assertEquals(10000, scaniaTruck.getCurrentLoad());
    }

    @Test
    void addLoad_Should_HandleZeroLoad() {
        // Arrange
        scaniaTruck.addLoad(5000);

        // Act
        scaniaTruck.addLoad(0);

        // Assert
        assertEquals(5000, scaniaTruck.getCurrentLoad());
    }

    // assign tests
    @Test
    void assign_Should_SetIsAssignedToTrue() {
        // Arrange
        assertFalse(scaniaTruck.isAssigned());

        // Act
        scaniaTruck.assign();

        // Assert
        assertTrue(scaniaTruck.isAssigned());
    }

    @Test
    void assign_Should_RemainAssigned_When_CalledMultipleTimes() {
        // Arrange, Act
        scaniaTruck.assign();
        scaniaTruck.assign();

        // Assert
        assertTrue(scaniaTruck.isAssigned());
    }

    // unassign tests
    @Test
    void unassign_Should_SetIsAssignedToFalse() {
        // Arrange
        scaniaTruck.assign();
        assertTrue(scaniaTruck.isAssigned());

        // Act
        scaniaTruck.unassign();

        // Assert
        assertFalse(scaniaTruck.isAssigned());
    }

    @Test
    void unassign_Should_ResetCurrentLoadToZero() {
        // Arrange
        scaniaTruck.addLoad(15000);
        scaniaTruck.assign();
        assertEquals(15000, scaniaTruck.getCurrentLoad());

        // Act
        scaniaTruck.unassign();

        // Assert
        assertEquals(0, scaniaTruck.getCurrentLoad());
    }

    @Test
    void unassign_Should_ResetBothAssignedStatusAndLoad() {
        // Arrange
        scaniaTruck.addLoad(20000);
        scaniaTruck.assign();

        // Act
        scaniaTruck.unassign();

        // Assert
        assertAll(
                () -> assertFalse(scaniaTruck.isAssigned()),
                () -> assertEquals(0, scaniaTruck.getCurrentLoad())
        );
    }

    // Integration-like tests
    @Test
    void truck_Should_MaintainState_Through_FullLifecycle() {
        // Arrange - New truck
        Truck truck = new TruckImpl(9999, TruckType.MAN);
        assertFalse(truck.isAssigned());
        assertEquals(0, truck.getCurrentLoad());

        // Act & Assert - Assign and load
        truck.assign();
        assertTrue(truck.isAssigned());

        truck.addLoad(10000);
        truck.addLoad(5000);
        assertEquals(15000, truck.getCurrentLoad());

        // Act & Assert - Unassign resets everything
        truck.unassign();
        assertFalse(truck.isAssigned());
        assertEquals(0, truck.getCurrentLoad());

        // Act & Assert - Can be reassigned
        truck.assign();
        truck.addLoad(8000);
        assertTrue(truck.isAssigned());
        assertEquals(8000, truck.getCurrentLoad());
    }
}
