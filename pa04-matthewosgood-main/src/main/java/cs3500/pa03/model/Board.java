package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * represents a players board
 */
public class Board {
  /**
   * represents the board
   * in the form (y,x) or [column][row] starting at 0,0
   */
  private final Symbol[][] board;
  /**
   * all the ships that are on this board
   */
  private final Ship[] ships;

  /**
   * convenience constructor that creates a Board with the given board layout and ships
   *
   * @param board the board
   * @param ships the ships on this board
   */
  public Board(Symbol[][] board, Ship[] ships) {
    this.board = board;
    this.ships = ships;
  }

  /**
   * constructs a board with the given width and height
   * the number of ships that can be placed on this board is set as the minimum between the two
   *
   * @param width  the width of the board
   * @param height the height of the board
   */
  public Board(int width, int height) {
    this.ships = new Ship[Math.min(width, height)];
    this.board = new Symbol[height][width];
    for (Symbol[] row : this.board) {
      Arrays.fill(row, Symbol.UNKNOWN);
    }
  }

  /**
   * finds the width of this board
   *
   * @return the width of this board
   */
  public int getWidth() {
    return this.board[0].length;
  }

  /**
   * finds the height of this board
   *
   * @return the height of this board
   */
  public int getHeight() {
    return this.board.length;
  }

  /**
   * gets the representation of this board as a 2d array
   *
   * @return the board represented by this
   */
  public Symbol[][] getBoard() {
    return this.board;
  }

  /**
   * gets the ships on this board as an ArrayList
   *
   * @return the boards on this ship
   */
  public ArrayList<Ship> getShips() {
    return new ArrayList<>(Arrays.asList(this.ships));
  }

  /**
   * gets the symbol at the given Coord on this board
   *
   * @param coord the location on the board that will be accessed
   * @return the symbol at the given Coord
   */
  public Symbol symbolAt(Coord coord) {
    return this.board[coord.getY()][coord.getX()];
  }

  /**
   * sets the symbol at the given location to the given char
   *
   * @param coord  the locations whose symbol will be set
   * @param symbol the symbol that will be set
   */
  private void setSymbolAt(Coord coord, Symbol symbol) {
    this.board[coord.getY()][coord.getX()] = symbol;
  }

  /**
   * updates this board and the ship in this board at the given location to show it was fired at
   *
   * @param coord the location that was shot at
   */
  public void updateHit(Coord coord) {
    this.setSymbolAt(coord, Symbol.HIT);
    for (Ship ship : this.ships) {
      if (ship != null && ship.shotAt(coord)) {
        if (ship.isSunk()) {
          for (Coord location : ship.getLocations()) {
            this.setSymbolAt(location, Symbol.SUNK);
          }
        }
        break;
      }
    }
  }

  /**
   * updates the given coord to have been missed
   *
   * @param coord the coord that will be updated
   */
  public void updateMiss(Coord coord) {
    this.setSymbolAt(coord, Symbol.MISS);
  }

  /**
   * adds the given ship to this board and updates the board
   *
   * @param ship the ship that will be added to this boards ships
   * @throws IllegalCoordException if the given ship cannot be added
   */
  private void addShip(Ship ship) throws IllegalCoordException {
    for (int i = 0; i < this.ships.length; i++) {
      if (this.ships[i] == null) {
        this.ships[i] = ship;
        for (Coord location : ship.getLocations()) {
          this.setSymbolAt(location, ship.getSymbol());
        }
        return;
      }
    }
    throw new IllegalCoordException("this board cannot hold anymore ships");
  }

  /**
   * checks which of the given shots hit and returns only the shots that did
   * also update this board to reflect all hits and misses
   *
   * @param shots the list of shots that will be checks
   * @return the list of shots that hit
   */
  public ArrayList<Coord> checkShots(List<Coord> shots) {
    ArrayList<Coord> ans = new ArrayList<>();
    for (Coord shot : shots) {
      Symbol symbol = this.symbolAt(shot);
      if (symbol != Symbol.UNKNOWN) {
        ans.add(shot);
        this.updateHit(shot);
      } else {
        this.updateMiss(shot);
      }
    }
    return ans;
  }

  /**
   * places a ship of the given type randomly on the board
   *
   * @param shipType the type of ship to be placed
   * @throws IllegalCoordException if a ship of this type cannot be placed
   */
  public void place(ShipType shipType) throws IllegalCoordException {
    ArrayList<Coord> emptyCoords = this.getEmptyCoords();
    Random random = new Random();
    int index;
    Coord start;
    while (emptyCoords.size() != 0) {
      index = random.nextInt(emptyCoords.size());
      start = emptyCoords.remove(index);
      try {
        Coord[] locations = this.generateAdjacentCoords(start, shipType.getSize());
        Ship ship = new Ship(shipType, locations);
        this.addShip(ship);
        return;
      } catch (IllegalCoordException ignored) {
        //ignored bc we are going to try other start Coords
      }
    }
    throw new IllegalCoordException("could not place " + shipType);
  }

  /**
   * generates a list of adjacent coords randomly to the right or down all in a line
   * starting from the given Coord
   *
   * @param start  the Coord that will be started at
   * @param length the total number of Coords that will be found, including the given
   * @return an array of coords all adjacent to each other on this board starting at the given Coord
   * @throws IllegalCoordException if it could not generate valid coords from the given start
   */
  private Coord[] generateAdjacentCoords(Coord start, int length) throws IllegalCoordException {
    Random random = new Random();
    boolean isVertical = random.nextBoolean();
    if (isVertical) {
      try {
        return this.generateVerticalCoords(start, length);
      } catch (IllegalCoordException e) {
        return this.generateHorizontalCoords(start, length);
      }
    } else {
      try {
        return this.generateHorizontalCoords(start, length);
      } catch (IllegalCoordException e) {
        return this.generateVerticalCoords(start, length);
      }
    }
  }

  /**
   * generates the given number of Coords (including the given start) horizontal to the given coord
   * going right
   *
   * @param start  the Coord that will be started with
   * @param length the total number of Coords that will be produced including the given Coord
   * @return an array of Coords horizontal to the given Coord and including the given Coord
   * @throws IllegalCoordException if the coords could not be generated
   */
  private Coord[] generateHorizontalCoords(Coord start, int length) throws
      IllegalCoordException {
    Coord[] ans = new Coord[length];
    ans[0] = start;
    Coord prev = start;
    for (int i = 1; i < length; i++) {
      Coord next = new Coord(prev.getX() + 1, prev.getY());
      if (next.getX() >= this.getWidth()) {
        throw new IllegalCoordException("coords went off board");
      } else if (this.symbolAt(next) != Symbol.UNKNOWN) {
        // split off to avoid symbolAt throwing an IndexOutOfBounds Exception
        throw new IllegalCoordException("coords not on UNKNOWN spots");
      } else {
        ans[i] = next;
        prev = next;
      }
    }
    return ans;
  }

  /**
   * generates the given number of Coords (including the given start) vertical to the given coord
   * going down
   *
   * @param start  the Coord that will be started with
   * @param length the total number of Coords that will be produced including the given Coord
   * @return an array of Coords down from the given Coord and including the given Coord
   * @throws IllegalCoordException if the coords could not be generated
   */
  private Coord[] generateVerticalCoords(Coord start, int length) throws IllegalCoordException {
    Coord[] ans = new Coord[length];
    ans[0] = start;
    Coord prev = start;
    for (int i = 1; i < length; i++) {
      Coord next = new Coord(prev.getX(), prev.getY() + 1);
      if (next.getY() >= this.getHeight()) {
        throw new IllegalCoordException("Coords went off board");
      } else if (this.symbolAt(next) != Symbol.UNKNOWN) {
        throw new IllegalCoordException("Coords not on UNKNOWN spots");
      } else {
        ans[i] = next;
        prev = next;
      }
    }
    return ans;
  }

  /**
   * gets all spots on this board that are empty
   *
   * @return every spot on this board that is UNKNOWN
   */
  public ArrayList<Coord> getEmptyCoords() {
    ArrayList<Coord> ans = new ArrayList<>();
    for (int i = 0; i < this.getWidth(); i++) {
      for (int j = 0; j < this.getHeight(); j++) {
        Coord next = new Coord(i, j);
        if (this.symbolAt(next) == Symbol.UNKNOWN) {
          ans.add(next);
        }
      }
    }
    return ans;
  }

  /**
   * resets this board to have all UNKNOWNS and no ships
   */
  public void reset() {
    for (Symbol[] row : this.board) {
      Arrays.fill(row, Symbol.UNKNOWN);
    }
    Arrays.fill(this.ships, null);
  }

  /**
   * finds the number of ships on this board that have not been sunk
   *
   * @return the number of non sunk ships on this board
   */
  public int numOfShipsRemaining() {
    int numOfRemaining = 0;
    for (Ship ship : this.ships) {
      if (ship != null && !ship.isSunk()) {
        numOfRemaining++;
      }
    }
    return numOfRemaining;
  }
}
