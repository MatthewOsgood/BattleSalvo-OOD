package cs3500.pa03.controller;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.ManualPlayer;
import cs3500.pa03.model.Player;
import cs3500.pa03.model.Salvo;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.model.Symbol;
import cs3500.pa03.view.View;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * controls a game of BattleSalvo
 */
public class LocalGameController implements Controller {

  /**
   * represents the first player. Output is given from this players POV
   */
  private final ManualPlayer p1;
  /**
   * represents the second player
   */
  private final Player p2;
  /**
   * the view that will be handling input and output
   */
  private final View view;
  private final Salvo p1Salvo;

  /**
   * constructs a Controller with the given players and view and plays from p1's point of view
   *
   * @param p1   the first player. Output will be given from this players POV
   * @param p2   the second player
   * @param view the view that will handle input and output
   */
  public LocalGameController(ManualPlayer p1, Salvo p1Salvo, Player p2, View view) {
    this.p1 = p1;
    this.p1Salvo = p1Salvo;
    this.p2 = p2;
    this.view = view;
  }

  /**
   * plays a game of BattleSalvo
   */
  public void play() {
    try {
      Coord boardSize = this.getValidBoardSize();
      int fleetSize = Math.min(boardSize.getX(), boardSize.getY());
      HashMap<ShipType, Integer> fleet = this.getValidFleet(fleetSize);
      this.p1.setup(boardSize.getY(), boardSize.getX(), fleet);
      this.p2.setup(boardSize.getY(), boardSize.getX(), fleet);

      ArrayList<Coord> shots1;
      List<Coord> shots2;
      while (true) {
        this.displayBoards();
        shots1 = this.getValidShots();
        p1Salvo.setShots(shots1);
        shots2 = this.p2.takeShots();
        if (this.isGameOver(shots1, shots2)) {
          return;
        }
        List<Coord> hitsOn1 = this.p1.reportDamage(shots2);
        List<Coord> hitsOn2 = this.p2.reportDamage(shots1);
        this.p1.successfulHits(hitsOn2);
        this.p2.successfulHits(hitsOn1);
      }
    } catch (IOException e) {
      System.out.println("Sorry. An unexpected error occurred.");
    }
  }

  /**
   * checks if the game is over
   * if it is the players are notified
   *
   * @param shots1 p1's shots
   * @param shots2 p2's shots
   * @return if the game is over
   * @throws IOException if an IO exception occurs
   */
  public boolean isGameOver(List<Coord> shots1, List<Coord> shots2) throws IOException {
    if (shots1.size() == 0) {
      this.view.write("You Lost :(");
      this.p1.endGame(GameResult.LOSE, "You lost");
      this.p2.endGame(GameResult.WIN, "You won");
      return true;
    } else if (shots2.size() == 0) {
      this.view.write("You Won!!!");
      this.p1.endGame(GameResult.WIN, "You won");
      this.p2.endGame(GameResult.LOSE, "You lost");
      return true;
    } else {
      return false;
    }
  }

  /**
   * displays the boards from the POV of p1
   */
  public void displayBoards() throws IOException {
    this.view.write("Opponents Board Data:");
    this.view.drawBoard(this.p1.getOpponentsBoard().getBoard());
    this.view.write("Your Board:");
    this.view.drawBoard(this.p1.getMyBoard().getBoard());
  }

  /**
   * gets a board with dimension [6, 15], [6, 15]
   *
   * @return the board size represented as a Coord
   */
  public Coord getValidBoardSize() throws IOException {
    Coord boardSize = this.view.getBoardSize();
    if (boardSize.getX() >= 6 && boardSize.getX() <= 15
        && boardSize.getY() >= 6 && boardSize.getY() <= 15) {
      return boardSize;
    } else {
      view.write("Invalid board size. Please remember that the height and width must be "
          + "between [6, 15]");
      return this.getValidBoardSize();
    }
  }

  /**
   * gets a fleet of the given size with at least 1 of each ShipType
   *
   * @param size the total size of the fleet
   * @return the fleet
   * @throws IOException if an input/output exception occurs
   */
  public HashMap<ShipType, Integer> getValidFleet(int size) throws IOException {
    int[] numOfEach = this.view.getFleet(size);
    int total = 0;
    for (int i : numOfEach) {
      if (i < 1) {
        this.view.write("You must have at least 1 of each ship");
        return this.getValidFleet(size);
      }
      total += i;
    }
    if (total != size) {
      this.view.write("You must have " + size + " ships.");
      return this.getValidFleet(size);
    }
    HashMap<ShipType, Integer> fleet = new HashMap<>();
    int index = 0;
    ShipType[] shipTypes = ShipType.values();
    Collections.reverse(Arrays.asList(shipTypes));
    for (ShipType shipType : shipTypes) {
      fleet.put(shipType, numOfEach[index]);
      index++;
    }
    return fleet;
  }

  /**
   * gets valid shots from p1
   *
   * @return a valid list of shots
   * @throws IOException if an IO exception occurs
   */
  public ArrayList<Coord> getValidShots() throws IOException {
    ArrayList<Coord> shots = this.view.getShots(this.p1.numOfShots());
    for (Coord shot : shots) {
      Symbol symbol;
      try {
        symbol = this.p1.getOpponentsBoard().symbolAt(shot);
      } catch (ArrayIndexOutOfBoundsException e) {
        this.view.write("Invalid shots: Shots went off board");
        return this.getValidShots();
      }
      if (symbol == Symbol.HIT
          || symbol == Symbol.MISS
          || symbol == Symbol.SUNK) {
        this.view.write("Invalid shots: Cannot fire at the same spot twice");
        return this.getValidShots();
      }
    }
    return shots;
  }

}
