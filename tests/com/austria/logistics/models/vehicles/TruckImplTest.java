package com.austria.logistics.models.vehicles;

import com.austria.logistics.models.enums.TruckType;
import com.austria.logistics.models.vehicles.contracts.Truck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TruckImplTest {

    private static final int TRUCK_ID = 1001;
    private Truck truck;

    @BeforeEach
    void before() {
        truck = new TruckImpl(TRUCK_ID, TruckType.SCANIA);
    }

    @Test
    public void constructor_Should_InitializeTruck_When_ArgumentsAreValid() {
        // Arrange, Act, Assert
        assertAll(
                () -> assertEquals(TRUCK_ID, truck.getId()),
                () -> assertEquals(TruckType.SCANIA, truck.getTruckType()),
                () -> assertFalse(truck.isAssigned()),
                () -> assertEquals(0, truck.getCurrentLoad())
        );
    }

    @Test
    public void getId_Should_ReturnCorrectId() {
        // Arrange, Act, Assert
        assertEquals(TRUCK_ID, truck.getId());
    }

    @Test
    public void getTruckType_Should_ReturnCorrectTruckType() {
        // Arrange
        Truck scaniaTruck = new TruckImpl(1001, TruckType.SCANIA);
        Truck manTruck = new TruckImpl(1011, TruckType.MAN);
        Truck actrosTruck = new TruckImpl(1026, TruckType.ACTROS);

        // Act, Assert
        assertAll(
                () -> assertEquals(TruckType.SCANIA, scaniaTruck.getTruckType()),
                () -> assertEquals(TruckType.MAN, manTruck.getTruckType()),
                () -> assertEquals(TruckType.ACTROS, actrosTruck.getTruckType())
        );
    }

    @Test
    public void isAssigned_Should_ReturnFalse_When_TruckIsNotAssigned() {
        // Arrange, Act, Assert
        assertFalse(truck.isAssigned());
    }

    @Test
    public void isAssigned_Should_ReturnTrue_When_TruckIsAssigned() {
        // Arrange
        truck.assign();

        // Act, Assert
        assertTrue(truck.isAssigned());
    }

    @Test
    public void getCurrentLoad_Should_ReturnZero_When_TruckIsNew() {
        // Arrange, Act, Assert
        assertEquals(0, truck.getCurrentLoad());
    }

    @Test
    public void getCurrentLoad_Should_ReturnCorrectValue_When_LoadIsAdded() {
        // Arrange
        truck.addLoad(1000);
        truck.addLoad(500);

        // Act, Assert
        assertEquals(1500, truck.getCurrentLoad());
    }

    @Test
    public void getMaxCapacity_Should_ReturnCorrectCapacity_ForScania() {
        // Arrange
        Truck scaniaTruck = new TruckImpl(1001, TruckType.SCANIA);

        // Act, Assert
        assertEquals(42000, scaniaTruck.getMaxCapacity());
    }

    @Test
    public void getMaxCapacity_Should_ReturnCorrectCapacity_ForMan() {
        // Arrange
        Truck manTruck = new TruckImpl(1011, TruckType.MAN);

        // Act, Assert
        assertEquals(37000, manTruck.getMaxCapacity());
    }

    @Test
    public void getMaxCapacity_Should_ReturnCorrectCapacity_ForActros() {
        // Arrange
        Truck actrosTruck = new TruckImpl(1026, TruckType.ACTROS);

        // Act, Assert
        assertEquals(26000, actrosTruck.getMaxCapacity());
    }

    @Test
    public void addLoad_Should_IncreaseCurrentLoad_When_WeightIsAdded() {
        // Arrange
        int initialLoad = truck.getCurrentLoad();

        // Act
        truck.addLoad(5000);

        // Assert
        assertEquals(initialLoad + 5000, truck.getCurrentLoad());
    }

    @Test
    public void addLoad_Should_AccumulateLoad_When_CalledMultipleTimes() {
        // Arrange, Act
        truck.addLoad(1000);
        truck.addLoad(2000);
        truck.addLoad(3000);

        // Assert
        assertEquals(6000, truck.getCurrentLoad());
    }

    @Test
    public void assign_Should_SetIsAssignedToTrue() {
        // Arrange
        assertFalse(truck.isAssigned());

        // Act
        truck.assign();

        // Assert
        assertTrue(truck.isAssigned());
    }

    @Test
    public void unassign_Should_SetIsAssignedToFalse_And_ResetLoad() {
        // Arrange
        truck.assign();
        truck.addLoad(10000);
        assertTrue(truck.isAssigned());
        assertEquals(10000, truck.getCurrentLoad());

        // Act
        truck.unassign();

        // Assert
        assertAll(
                () -> assertFalse(truck.isAssigned()),
                () -> assertEquals(0, truck.getCurrentLoad())
        );
    }

    @Test
    public void unassign_Should_ResetCurrentLoadToZero() {
        // Arrange
        truck.addLoad(15000);
        assertEquals(15000, truck.getCurrentLoad());

        // Act
        truck.unassign();

        // Assert
        assertEquals(0, truck.getCurrentLoad());
    }
}
