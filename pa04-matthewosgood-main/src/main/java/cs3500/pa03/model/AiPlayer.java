package cs3500.pa03.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * represents an AI player
 */
public class AiPlayer extends AnyPlayer {
  /**
   * constructs a player with the given name
   *
   * @param username the name of the player
   */
  public AiPlayer(String username) {
    super(username, new Salvo());
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    ArrayList<Coord> empty = this.opponentsBoard.getEmptyCoords();
    ArrayList<Coord> shots = new ArrayList<>();
    Random random = new Random();
    for (int i = 0; i < this.numOfShots(); i++) {
      int index = random.nextInt(empty.size());
      shots.add(empty.remove(index));
    }
    this.currentSalvo.setShots(shots);
    return shots;
  }
}
