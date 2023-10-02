package cs3500.pa03.model;

import java.util.List;
import java.util.Map;

/**
 * represents either a manual or AI player
 */
public abstract class AnyPlayer implements Player {

  /**
   * the name of this player
   */
  private final String username;
  /**
   * this players board
   */
  protected Board myBoard;
  /**
   * the board of this players opponent
   */
  protected Board opponentsBoard;
  /**
   * used to get this players shots
   */
  protected Salvo currentSalvo;

  /**
   * constructs a player with the given name salvo
   *
   * @param username the name of the player
   * @param salvo    the salvo to be used for shot generation
   */
  public AnyPlayer(String username, Salvo salvo) {
    this.username = username;
    this.currentSalvo = salvo;
  }

  /**
   * convenience contructor
   *
   * @param username the name of this player
   * @param salvo the salvo used to get shots
   * @param myBoard the board of this player
   * @param opponentsBoard the board of this players opponent
   */
  protected AnyPlayer(String username, Salvo salvo, Board myBoard, Board opponentsBoard) {
    this.username = username;
    this.currentSalvo = salvo;
    this.myBoard = myBoard;
    this.opponentsBoard = opponentsBoard;
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return this.username;
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    this.myBoard = new Board(width, height);
    this.opponentsBoard = new Board(width, height);
    int num;
    for (ShipType shipType : ShipType.values()) {
      num = specifications.get(shipType);
      for (int i = 0; i < num; i++) {
        try {
          this.myBoard.place(shipType);
        } catch (IllegalCoordException e) {
          // I think this case is impossible, but I will leave this here to be safe
          this.myBoard.reset();
          this.setup(height, width, specifications);
        }
      }
    }
    return this.myBoard.getShips();
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public abstract List<Coord> takeShots();

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   *        ship on this board
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    return this.myBoard.checkShots(opponentShotsOnBoard);
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    for (Coord shot : this.currentSalvo.getShots()) {
      if (shotsThatHitOpponentShips.contains(shot)) {
        this.opponentsBoard.updateHit(shot);
      } else {
        this.opponentsBoard.updateMiss(shot);
      }
    }
  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
    // don't need for now
  }

  /**
   * calculates the number of shots this player has remaining
   *
   * @return the number of shots this player has remaining
   */
  public int numOfShots() {
    return Math.min(
        this.myBoard.numOfShipsRemaining(),
        this.opponentsBoard.getEmptyCoords().size());
  }

  /**
   * gets this players board
   *
   * @return the players board
   */
  public Board getMyBoard() {
    return this.myBoard;
  }

  /**
   * gets this players opponents board
   *
   * @return the players opponents board
   */
  public Board getOpponentsBoard() {
    return this.opponentsBoard;
  }
}
