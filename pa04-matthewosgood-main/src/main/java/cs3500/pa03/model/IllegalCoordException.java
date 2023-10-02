package cs3500.pa03.model;

/**
 * thrown in the Board class when a Coord is checked to not be on the Board
 */
public class IllegalCoordException extends Exception {
  public IllegalCoordException(String message) {
    super(message);
  }
}
