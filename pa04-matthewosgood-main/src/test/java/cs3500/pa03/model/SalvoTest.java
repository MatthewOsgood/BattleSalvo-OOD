package cs3500.pa03.model;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class SalvoTest {
  Salvo salvo = new Salvo();
  Coord coord1 = new Coord(1, 2);
  Coord coord2 = new Coord(3, 4);
  Coord coord3 = new Coord(5, 6);
  ArrayList<Coord> shots = new ArrayList<>(Arrays.asList(coord1, coord2, coord3));


  /**
   * tests both set and get methods
   */
  @Test
  void testSalvo() {
    assertEquals(new ArrayList<Coord>(), salvo.getShots());
    salvo.setShots(shots);
    assertEquals(shots, salvo.getShots());

  }

}