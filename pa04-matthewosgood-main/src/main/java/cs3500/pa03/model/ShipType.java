package cs3500.pa03.model;

/**
 * represents all possible types of ships
 * with their associated size
 * and the character that is used to represent them on the board
 */
public enum ShipType {
  SUBMARINE(3, Symbol.SUBMARINE),
  DESTROYER(4, Symbol.DESTROYER),
  BATTLESHIP(5, Symbol.BATTLESHIP),
  CARRIER(6, Symbol.CARRIER);

  /**
   * represents the size of this ship
   */
  private final int size;
  /**
   * the char that will be used to denote a ship of this type on the board
   */
  private final Symbol symbol;

  /**
   * creates a ShipType
   *
   * @param size   the size of this ship
   * @param symbol the symbol that represents this ship
   */
  ShipType(int size, Symbol symbol) {
    this.size = size;
    this.symbol = symbol;
  }

  /**
   * gets the size of this ShipType
   *
   * @return the size of this ShipType
   */
  public int getSize() {
    return this.size;
  }

  /**
   * gets the symbol that represents this ShipType
   *
   * @return the symbol that represents this ShipType
   */
  public Symbol getSymbol() {
    return this.symbol;
  }

  /**
   * @param size the size of shipType that will be returned. Must be between [3, 6]
   * @return the ShipType that has the given length
   */
  public static   ShipType fromSize(int size) {
    switch (size) {
      case 3 -> {
        return ShipType.SUBMARINE;
      }
      case 4 -> {
        return ShipType.DESTROYER;
      }
      case 5 -> {
        return ShipType.BATTLESHIP;
      }
      case 6 -> {
        return ShipType.CARRIER;
      }
      default -> throw new IllegalArgumentException("size must be between [3, 5]. "
          + size + " is not in this range.");

    }
  }
}
