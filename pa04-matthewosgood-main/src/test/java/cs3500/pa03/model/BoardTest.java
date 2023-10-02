package cs3500.pa03.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {

  Symbol[][] boardLayout1;
  Symbol[][] boardLayout2;
  Coord shot1;
  Coord shot2;
  ArrayList<Coord> shots;
  Coord[] hits;
  Ship submarine1;
  Ship submarine2;
  Coord spot1;
  Coord spot2;
  Coord spot3;
  Coord spot4;
  Coord spot5;
  Coord spot6;
  Coord[] locations;
  Ship[] ships;
  Board board1;
  Board board2;
  Board board3;

  @BeforeEach
  void init() {
    boardLayout1 = new Symbol[][] {{Symbol.UNKNOWN, Symbol.SUBMARINE, Symbol.UNKNOWN},
        {Symbol.UNKNOWN, Symbol.SUBMARINE, Symbol.UNKNOWN},
        {Symbol.UNKNOWN, Symbol.SUBMARINE, Symbol.UNKNOWN}};

    boardLayout2 = new Symbol[][] {{Symbol.SUNK, Symbol.SUBMARINE, Symbol.UNKNOWN},
        {Symbol.SUNK, Symbol.SUBMARINE, Symbol.UNKNOWN},
        {Symbol.SUNK, Symbol.SUBMARINE, Symbol.UNKNOWN}};
    shot1 = new Coord(0, 0);
    shot2 = new Coord(1, 2);
    shots = new ArrayList<>(Arrays.asList(shot1, shot2));
    hits = new Coord[] {shot2};

    spot1 = new Coord(1, 0);
    spot2 = new Coord(1, 1);
    spot3 = new Coord(1, 2);
    locations = new Coord[] {spot1, spot2, spot3};
    submarine1 = new Ship(ShipType.SUBMARINE, locations);

    spot4 = new Coord(0, 0);
    spot5 = new Coord(0, 1);
    spot6 = new Coord(0, 2);
    submarine2 = new Ship(ShipType.SUBMARINE, new Coord[] {spot4, spot5, spot6});
    submarine2.shotAt(spot4);
    submarine2.shotAt(spot5);
    submarine2.shotAt(spot6);

    ships = new Ship[] {submarine1};
    board1 = new Board(boardLayout1, ships);
    board2 = new Board(8, 10);
    board3 = new Board(boardLayout2, new Ship[] {submarine1, submarine2});
  }

  @Test
  void checkShots() {
    ArrayList<Coord> actualHits = board1.checkShots(shots);
    assertEquals(hits.length, actualHits.size());
    assertEquals(Symbol.MISS, board1.symbolAt(shot1));
    assertEquals(Symbol.HIT, board1.symbolAt(shot2));
  }

  @Test
  void getWidth() {
    assertEquals(3, board1.getWidth());
    assertEquals(8, board2.getWidth());
  }

  @Test
  void getHeight() {
    assertEquals(3, board1.getHeight());
    assertEquals(10, board2.getHeight());
  }

  @Test
  void getBoard() {
    assertEquals(boardLayout1, board1.getBoard());
    for (Symbol[] row : board2.getBoard()) {
      for (Symbol symbol : row) {
        assertEquals(Symbol.UNKNOWN, symbol);
      }
    }
  }

  @Test
  void place() {
    assertThrows(IllegalCoordException.class,
        () -> board1.place(ShipType.CARRIER));
    assertThrows(IllegalCoordException.class,
        () -> board1.place(ShipType.SUBMARINE));
    try {
      board2.place(ShipType.BATTLESHIP);
    } catch (IllegalCoordException e) {
      fail("could not place a battleship on board2");
    }
    assertEquals(75, board2.getEmptyCoords().size());
    try {
      board2.place(ShipType.CARRIER);
    } catch (IllegalCoordException e) {
      fail("could not place carrier on board2");
    }
    assertEquals(69, board2.getEmptyCoords().size());
    try {
      board2.place(ShipType.SUBMARINE);
    } catch (IllegalCoordException e) {
      fail("could not place submarine on board2");
    }
    assertEquals(66, board2.getEmptyCoords().size());
  }

  @Test
  void getEmptyCoords() {
    ArrayList<Coord> empty = board1.getEmptyCoords();
    assertEquals(6, empty.size());
    assertFalse(empty.contains(shot2));
    assertTrue(empty.contains(shot1));
    for (Coord coord : empty) {
      assertEquals(Symbol.UNKNOWN, board1.symbolAt(coord));
    }
    assertEquals(80, board2.getEmptyCoords().size());
  }

  @Test
  void symbolAt() {
    assertEquals(Symbol.UNKNOWN, board1.symbolAt(new Coord(0, 1)));
    assertEquals(Symbol.SUBMARINE, board1.symbolAt(shot2));
    assertThrows(IndexOutOfBoundsException.class,
        () -> board2.symbolAt(new Coord(100, 100)));
  }

  @Test
  void reset() {
    board1.reset();
    assertEquals(9, board1.getEmptyCoords().size());
    try {
      board1.place(ShipType.SUBMARINE);
    } catch (IllegalCoordException e) {
      fail("array of ships on board1 was not cleared");
    }
  }

  @Test
  void getShips() {
    assertEquals(Arrays.asList(ships), board1.getShips());
  }

  @Test
  void updateHit() {
    board2.updateHit(shot1);
    assertEquals(Symbol.HIT, board2.symbolAt(shot1));
    board1.updateHit(shot2);
    assertEquals(Symbol.HIT, board1.symbolAt(shot2));

  }

  @Test
  void updateMiss() {
    board1.updateMiss(shot1);
    assertEquals(Symbol.MISS, board1.symbolAt(shot1));
    board2.updateMiss(shot2);
    assertEquals(Symbol.MISS, board2.symbolAt(shot2));
  }

  @Test
  void numOfShipsRemaining() {
    assertEquals(1, board1.numOfShipsRemaining());
    assertEquals(1, board3.numOfShipsRemaining());
    assertEquals(0, board2.numOfShipsRemaining());
  }
}