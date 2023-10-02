package cs3500.pa03.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * tests the Coord class
 */
class CoordTest {

  Coord coord1 = new Coord(1, 1);
  Coord coord2 = new Coord(5, 15);
  Coord coord3 = new Coord(1, 9);

  @Test
  void getX() {
    assertEquals(1, coord1.getX());
    assertEquals(5, coord2.getX());
  }

  @Test
  void getY() {
    assertEquals(1, coord1.getY());
    assertEquals(15, coord2.getY());
  }

  @Test
  void makeRandom() {
    //checking that the range is inclusive
    Coord randCoord1 = Coord.makeRandom(0, 0);
    assertEquals(0, randCoord1.getX());
    assertEquals(0, randCoord1.getY());

    //checking that it is within the range properly
    for (int i = 0; i < 40; i++) {
      Coord randCoord2 = Coord.makeRandom(3, 4);
      assertTrue(randCoord2.getX() <= 3);
      assertTrue(randCoord2.getY() <= 4);
    }
  }

  @Test
  void testEquals() {
    assertFalse(coord1.equals(coord3));
    assertFalse(coord2.equals(9));
    assertTrue(coord1.equals(new Coord(1, 1)));
  }
}