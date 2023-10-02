package cs3500.pa03.model;

import java.util.List;

/**
 * represents a human player
 */
public class ManualPlayer extends AnyPlayer {
  /**
   * constructs a player with the given name salvo
   *
   * @param username the name of the player
   * @param salvo    the salvo to be used for shot generation
   */
  public ManualPlayer(String username, Salvo salvo) {
    super(username, salvo);
  }

  /**
   * convenience constructor
   *
   * @param username this players username
   * @param salvo used to get this players shots
   * @param myBoard the board of this player
   * @param opponentsBoard the board of this players opponent
   */
  public ManualPlayer(String username, Salvo salvo, Board myBoard, Board opponentsBoard) {
    super(username, salvo, myBoard, opponentsBoard);
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    return this.currentSalvo.getShots();
  }
}
