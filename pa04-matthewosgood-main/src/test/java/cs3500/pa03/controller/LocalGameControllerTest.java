package cs3500.pa03.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import cs3500.pa03.model.AiPlayer;
import cs3500.pa03.model.Board;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ManualPlayer;
import cs3500.pa03.model.Salvo;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.view.View;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalGameControllerTest {

  LocalGameController localGameController1;
  LocalGameController localGameController2;
  LocalGameController localGameController3;
  ManualPlayer p1;
  Salvo salvo = new Salvo();
  AiPlayer p2;
  View view1;
  View view2;
  View view3;
  StringBuilder output;
  StringReader input1;
  StringReader input2;
  StringReader input3;
  HashMap<ShipType, Integer> specifications;
  Board board;

  @BeforeEach
  void init() {
    board = new Board(6, 6);
    p1 = new ManualPlayer("P1", salvo, board, board);
    p2 = new AiPlayer("P2");
    output = new StringBuilder();
    input1 = new StringReader("""
        12 10
        1 1 1 1
        0 0
        1 1
        2 2
        3 3""");
    input2 = new StringReader("1 2 3 4");
    input3 = new StringReader("""
        0 0
        1 1
        2 2
        3 3
        4 4
        5 5
        """);
    view1 = new View(input1, output);
    view2 = new View(input2, output);
    view3 = new View(input3, output);
    localGameController1 = new LocalGameController(p1, salvo, p2, view1);
    localGameController2 = new LocalGameController(p1, salvo, p2, view2);
    localGameController3 = new LocalGameController(p1, salvo, p2, view3);
    specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 3);
  }

  @Test
  void getValidBoardSize() {
    Coord boardSize = null;
    try {
      boardSize = localGameController1.getValidBoardSize();
    } catch (IOException e) {
      fail("could not get valid board size");
    }
    assertEquals(10, boardSize.getX());
    assertEquals(12, boardSize.getY());
  }

  @Test
  void getValidFleet() {
    HashMap<ShipType, Integer> fleet = null;
    try {
      fleet = localGameController2.getValidFleet(10);
    } catch (IOException e) {
      fail("could not get fleet");
    }
    assertEquals(1, fleet.get(ShipType.CARRIER));
    assertEquals(2, fleet.get(ShipType.BATTLESHIP));
    assertEquals(3, fleet.get(ShipType.DESTROYER));
    assertEquals(4, fleet.get(ShipType.SUBMARINE));
  }

  @Test
  void displayBoards() {
    try {
      localGameController1.displayBoards();
    } catch (IOException e) {
      fail("failed to display boards");
    }
    String expected = """
        Opponents Board Data:
        O O O O O O\s
        O O O O O O\s
        O O O O O O\s
        O O O O O O\s
        O O O O O O\s
        O O O O O O\s
        Your Board:
        O O O O O O\s
        O O O O O O\s
        O O O O O O\s
        O O O O O O\s
        O O O O O O\s
        O O O O O O\s
        """;

    assertEquals(expected, output.toString());
  }

  @Test
  void isGameOver() {
    ArrayList<Coord> shots1 = new ArrayList<>();
    shots1.add(new Coord(0, 0));
    ArrayList<Coord> shots2 = new ArrayList<>();
    shots2.add(new Coord(3, 15));
    ArrayList<Coord> shots3 = new ArrayList<>();

    try {
      assertFalse(localGameController1.isGameOver(shots1, shots2));
      assertTrue(localGameController1.isGameOver(shots1, shots3));
      assertTrue(localGameController1.isGameOver(shots3, shots2));
    } catch (IOException e) {
      fail("could not check if the game is over");
    }
  }

  @Test
  void getValidShots() {
    p1.setup(6, 6, specifications);
    StringReader input4 = new StringReader("""
        8 8
        0 0
        0 0
        1 1
        2 2
        """);
    View view4 = new View(input4, output);
    LocalGameController localGameController4 = new LocalGameController(p1, salvo, p2, view4);
    try {
      assertEquals(6, localGameController3.getValidShots().size());
      assertThrows(NoSuchElementException.class,
          localGameController4::getValidShots);
      assertThrows(NoSuchElementException.class,
          () -> localGameController1.getValidShots());
      assertThrows(NoSuchElementException.class,
          () -> localGameController2.getValidShots());
    } catch (IOException e) {
      fail();
    }
  }

  @Test
  void testPlay() {
    assertThrows(NoSuchElementException.class,
        () -> localGameController1.play());
  }
}