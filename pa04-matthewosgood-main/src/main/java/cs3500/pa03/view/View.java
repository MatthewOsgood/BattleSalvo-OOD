package cs3500.pa03.view;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.Symbol;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * represents input and output
 */
public class View {
  /**
   * where input will be read
   */
  private final Readable input;
  /**
   * where output will be written to
   */
  private final Appendable output;

  /**
   * constructs a View
   *
   * @param input  the input that will be scanned
   * @param output the output that will be written to
   */
  public View(Readable input, Appendable output) {
    this.input = input;
    this.output = output;
  }

  /**
   * appends the given msg to this output with a \n at the end
   *
   * @param msg the message that will be written
   * @throws IOException if the output of this View cannot be written to
   */
  public void write(String msg) throws IOException {
    output.append(msg).append("\n");
  }

  /**
   * displays the given board to this output
   *
   * @param board the board that will be drawn
   * @throws IOException if the output cannot be written to
   */
  public void drawBoard(Symbol[][] board) throws IOException {
    for (Symbol[] row : board) {
      StringBuilder msg = new StringBuilder();
      for (Symbol symbol : row) {
        msg.append(symbol.toChar()).append(" ");
      }
      this.write(msg.toString());
    }
  }

  /**
   * gets the next 2 ints from the user as the board size
   *
   * @return the board size represented as a coord
   * @throws IOException if the input cannot be scanned
   */
  public Coord getBoardSize() throws IOException {
    Scanner scanner = new Scanner(this.input);
    this.write("Please enter the height and width of the board: ");
    int height = scanner.nextInt();
    int width = scanner.nextInt();
    return new Coord(width, height);
  }

  /**
   * gets the fleet from the user and returns them as an array
   * the fleet is received and returned in the order carrier, battleship, destroyer, submarine
   *
   * @param size the size of the fleet
   * @return the fleet as an array
   * @throws IOException if the input cannot be scanned or the output cannot be written to
   */
  public int[] getFleet(int size) throws IOException {
    Scanner scanner = new Scanner(this.input);
    this.write("Please enter your fleet in the order: Carrier, Battleship, Destroyer, Submarine");
    this.write("The size of your fleet must be " + size);
    int[] fleet = new int[4];
    for (int i = 0; i < 4; i++) {
      fleet[i] = scanner.nextInt();
    }
    return fleet;
  }

  /**
   * gets the given number of shots from the player
   *
   * @param numOfShots the number of shots that will be received
   * @return the users shots
   * @throws IOException if an exception occurs with input and output
   */
  public ArrayList<Coord> getShots(int numOfShots) throws IOException {
    Scanner scanner = new Scanner(this.input);
    ArrayList<Coord> shots = new ArrayList<>();
    this.write("Please enter " + numOfShots + " shots:");
    for (int i = 0; i < numOfShots; i++) {
      int width = scanner.nextInt();
      int height = scanner.nextInt();
      shots.add(new Coord(width, height));
    }
    return shots;
  }

  /**
   * outputs the given Coord on its own line
   *
   * @param coord the coord that will be written
   * @throws IOException if an IO exception occurs
   */
  public void writeCoord(Coord coord) throws IOException {
    this.write(coord.getX() + " " + coord.getY());
  }
}
