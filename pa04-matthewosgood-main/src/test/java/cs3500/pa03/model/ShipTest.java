package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa04.json.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipTest {

  Coord coord1;
  Coord coord2;
  Coord coord3;
  Coord coord4;

  Coord coord5;
  Coord coord6;

  Coord[] locations1;
  Coord[] locations2;
  Coord[] locations3;
  Coord[] locations4;

  Ship submarine;
  Ship destroyer;
  Ship battleship;
  Ship carrier;

  @BeforeEach
  void init() {
    coord1 = new Coord(0, 0);
    coord2 = new Coord(0, 1);
    coord3 = new Coord(0, 2);
    coord4 = new Coord(0, 3);
    coord5 = new Coord(0, 4);
    coord6 = new Coord(0, 5);

    locations1 = new Coord[] {coord1, coord2, coord3};
    locations2 = new Coord[] {coord1, coord2, coord3, coord4};
    locations3 = new Coord[] {coord1, coord2, coord3, coord4, coord5};
    locations4 = new Coord[] {coord1, coord2, coord3, coord4, coord5, coord6};

    submarine = new Ship(ShipType.SUBMARINE, locations1);
    destroyer = new Ship(ShipType.DESTROYER, locations2);
    battleship = new Ship(ShipType.BATTLESHIP, locations3);
    carrier = new Ship(ShipType.CARRIER, locations4);
  }


  /**
   * tests the creation of ships
   */
  @Test
  void testCreation() {
    assertThrows(IllegalArgumentException.class,
        () -> new Ship(ShipType.CARRIER, locations1));
    assertThrows(IllegalArgumentException.class,
        () -> new Ship(ShipType.BATTLESHIP, locations4));
    assertDoesNotThrow(
        () -> new Ship(ShipType.DESTROYER, locations2));
  }

  /**
   * tests getSymbol
   */
  @Test
  void getSymbol() {
    assertEquals(Symbol.SUBMARINE, submarine.getSymbol());
    assertEquals(Symbol.DESTROYER, destroyer.getSymbol());
    assertEquals(Symbol.BATTLESHIP, battleship.getSymbol());
    assertEquals(Symbol.CARRIER, carrier.getSymbol());
  }

  @Test
  void shotAt() {
    assertTrue(submarine.shotAt(coord1));
    assertTrue(submarine.shotAt(coord2));
    assertTrue(submarine.shotAt(coord3));
    assertFalse(submarine.shotAt(coord4));
    assertTrue(destroyer.shotAt(coord4));
    assertTrue(carrier.shotAt(coord5));
    assertFalse(battleship.shotAt(coord6));
  }

  @Test
  void isSunk() {
    submarine.shotAt(coord1);
    submarine.shotAt(coord2);
    submarine.shotAt(coord3);

    destroyer.shotAt(coord1);
    destroyer.shotAt(coord2);
    destroyer.shotAt(coord3);

    assertTrue(submarine.isSunk());
    assertFalse(destroyer.isSunk());
    assertFalse(carrier.isSunk());
  }

  @Test
  void getShipType() {
    assertEquals(ShipType.SUBMARINE, submarine.getShipType());
    assertEquals(ShipType.DESTROYER, destroyer.getShipType());
    assertEquals(ShipType.BATTLESHIP, battleship.getShipType());
    assertEquals(ShipType.CARRIER, carrier.getShipType());
  }

  @Test
  void getDirection() {
    assertEquals(Direction.VERTICAL, submarine.getDirection());
    assertEquals(Direction.VERTICAL, carrier.getDirection());

    Coord[] horiz = new Coord[] {coord1, new Coord(1, 0), new Coord(2, 0)};
    Ship horizShip = new Ship(ShipType.SUBMARINE, horiz);
    assertEquals(Direction.HORIZONTAL, horizShip.getDirection());
  }
}