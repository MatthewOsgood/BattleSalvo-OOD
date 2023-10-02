package cs3500.pa03.model;

/**
 * represents all possible chars that can be placed on the board
 */
public enum Symbol {
  UNKNOWN('O'),
  HIT('X'),
  MISS('-'),
  SUNK('*'),

  //following are to represent ships
  SUBMARINE('S'),
  DESTROYER('D'),
  BATTLESHIP('B'),
  CARRIER('C');

  /**
   * the char that this Symbol represents
   */
  private final char symbol;

  Symbol(char symbol) {
    this.symbol = symbol;
  }

  /**
   * turns this Symbol into the char it represents
   *
   * @return the char that this Symbol represents
   */
  public char toChar() {
    return this.symbol;
  }
}
