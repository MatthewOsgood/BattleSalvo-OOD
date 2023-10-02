package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManualPlayerTest {

  Salvo salvo;
  ManualPlayer p1;
  ManualPlayer p2;
  HashMap<ShipType, Integer> specifications;
  Symbol[][] boardLayout;
  Ship submarine;
  Coord coord1;
  Coord coord2;
  Coord coord3;
  Board board1;
  Board board2;
  ArrayList<Coord> shots1;
  ArrayList<Coord> shots2;
  ArrayList<Coord> shots3;
  HashMap<ShipType, Integer> spec2;


  @BeforeEach
  void init() {
    salvo = new Salvo();
    p1 = new ManualPlayer("test", salvo);
    specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 3);
    specifications.put(ShipType.BATTLESHIP, 2);
    specifications.put(ShipType.DESTROYER, 4);
    specifications.put(ShipType.SUBMARINE, 2);
    boardLayout = new Symbol[][] {{Symbol.UNKNOWN, Symbol.SUBMARINE, Symbol.UNKNOWN},
                                  {Symbol.UNKNOWN, Symbol.SUBMARINE, Symbol.UNKNOWN},
                                  {Symbol.UNKNOWN, Symbol.SUBMARINE, Symbol.UNKNOWN}};
    coord1 = new Coord(1, 0);
    coord2 = new Coord(1, 1);
    coord3 = new Coord(1, 1);
    submarine = new Ship(ShipType.SUBMARINE, new Coord[] {coord1, coord2, coord3});
    board1 = new Board(boardLayout, new Ship[] {submarine});
    board2 = new Board(3, 3);
    p2 = new ManualPlayer("P2", salvo, board1, board2);
    shots1 = new ArrayList<>(Arrays.asList(coord2, new Coord(2, 2)));
    shots2 = new ArrayList<>();
    shots2.add(coord2);
    shots3 = new ArrayList<>();
    shots3.add(new Coord(0, 0));
    shots3.add(new Coord(1, 1));
    shots3.add(new Coord(2, 2));
    shots3.add(new Coord(3, 3));
    shots3.add(new Coord(4, 4));
    shots3.add(new Coord(5, 5));
    spec2 = new HashMap<>();
    spec2.put(ShipType.CARRIER, 1);
    spec2.put(ShipType.SUBMARINE, 0);
    spec2.put(ShipType.BATTLESHIP, 0);
    spec2.put(ShipType.DESTROYER, 0);
  }

  @Test
  void name() {
    assertEquals("test", p1.name());
  }

  @Test
  void setup() {
    p1.setup(15, 11, specifications); //165 total squares
    assertEquals(11, p1.numOfShots());
    assertEquals(115, p1.getMyBoard().getEmptyCoords().size());
    assertEquals(11, p1.getMyBoard().getShips().size());
  }

  @Test
  void reportDamage() {
    assertEquals(shots2, p2.reportDamage(shots1));

    p1.setup(6, 6, spec2);
    assertEquals(1, p1.reportDamage(shots3).size());
  }

  @Test
  void successfulHits() {
    salvo.setShots(shots1);
    p2.successfulHits(shots2);
    assertEquals(Symbol.HIT, board2.symbolAt(coord2));
    assertEquals(Symbol.MISS, board2.symbolAt(new Coord(2, 2)));

  }

  @Test
  void numOfShots() {
    p1.setup(11, 13, specifications);
    assertEquals(11, p1.numOfShots());
  }

  @Test
  void takeShots() {
    salvo.setShots(shots1);
    assertEquals(shots1, p1.takeShots());
    salvo.setShots(shots2);
    assertEquals(shots2, p1.takeShots());
  }
}