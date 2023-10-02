package cs3500.pa03.model;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * tests the ShipType class
 */
class ShipTypeTest {

  /**
   * tests the getSize method
   */
  @Test
  void getSize() {
    assertEquals(3, ShipType.SUBMARINE.getSize());
    assertEquals(4, ShipType.DESTROYER.getSize());
    assertEquals(5, ShipType.BATTLESHIP.getSize());
    assertEquals(6, ShipType.CARRIER.getSize());
  }

  /**
   * tests the getSymbol method
   */
  @Test
  void getSymbol() {
    assertEquals(Symbol.SUBMARINE, ShipType.SUBMARINE.getSymbol());
    assertEquals(Symbol.DESTROYER, ShipType.DESTROYER.getSymbol());
    assertEquals(Symbol.BATTLESHIP, ShipType.BATTLESHIP.getSymbol());
    assertEquals(Symbol.CARRIER, ShipType.CARRIER.getSymbol());
  }

  @Test
  void fromSize() {
    assertEquals(ShipType.SUBMARINE, ShipType.fromSize(3));
    assertEquals(ShipType.DESTROYER, ShipType.fromSize(4));
    assertEquals(ShipType.BATTLESHIP, ShipType.fromSize(5));
    assertEquals(ShipType.CARRIER, ShipType.fromSize(6));
  }
}