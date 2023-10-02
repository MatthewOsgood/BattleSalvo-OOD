package cs3500.pa03.view;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.Symbol;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ViewTest {
  StringBuilder output;
  StringReader input;
  View view;
  Symbol[][] board;
  Coord size;

  @BeforeEach
  void init() {
    output = new StringBuilder();
    board = new Symbol[][] {{Symbol.UNKNOWN, Symbol.MISS, Symbol.HIT},
                            {Symbol.SUNK, Symbol.SUBMARINE, Symbol.BATTLESHIP},
                            {Symbol.DESTROYER, Symbol.CARRIER, Symbol.UNKNOWN}};
    size = new Coord(9, 12);
  }


  @Test
  void write() {
    input = new StringReader("");
    view = new View(input, output);
    try {
      view.write("test");
      view.write("hello");
    } catch (IOException e) {
      fail("failed to write to output");
    }
    assertEquals("test\nhello\n", output.toString());
  }

  @Test
  void drawBoard() {
    input = new StringReader("");
    view = new View(input, output);
    String expected = """
        O - X\s
        * S B\s
        D C O\s
        """;
    try {
      view.drawBoard(board);
    } catch (IOException e) {
      fail("could not draw board");
    }
    assertEquals(expected, output.toString());
  }

  @Test
  void getBoardSize() {
    input = new StringReader("12 9 1000000");
    view = new View(input, output);
    Coord actual1 = null;
    try {
      actual1 = view.getBoardSize();
    } catch (IOException e) {
      fail("could not get board size");
    }
    assertEquals(size.getX(), actual1.getX());
    assertEquals(size.getY(), actual1.getY());
  }

  @Test
  void getFleet() {
    input = new StringReader("3 2 4 1 100000");
    view = new View(input, output);
    int[] fleet = null;
    try {
      fleet = view.getFleet(10);
    } catch (IOException e) {
      fail("could not get fleet");
    }
    assertEquals(3, fleet[0]);
    assertEquals(2, fleet[1]);
    assertEquals(4, fleet[2]);
    assertEquals(1, fleet[3]);
  }

  @Test
  void getShots() {
    input = new StringReader("""
        0 0
        1 5
        2 6
        8 2
        3 6""");
    view = new View(input, output);
    ArrayList<Coord> shots1 = null;
    ArrayList<Coord> shots2 = null;
    try {
      shots1 = view.getShots(0);
      shots2 = view.getShots(2);
    } catch (IOException e) {
      fail("could not get shots");
    }
    assertEquals(0, shots1.size());
    assertEquals(2, shots2.size());
    assertEquals(new Coord(0, 0), shots2.get(0));
  }

  @Test
  void writeCoord() {
    input = new StringReader("");
    view = new View(input, output);
    try {
      view.writeCoord(new Coord(8, 12));
    } catch (IOException e) {
      fail("could not write coord");
    }
    assertEquals("8 12\n", output.toString());
  }
}