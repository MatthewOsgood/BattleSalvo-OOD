package cs3500.pa03.model;

import java.util.ArrayList;

/**
 * represents a list of shots in a turn
 * mutable to allow for dependency injection
 */
public class Salvo {
  /**
   * the shots in this salvo
   */
  private ArrayList<Coord> shots;

  /**
   * constructor
   */
  public Salvo() {
    this.shots = new ArrayList<>();
  }

  /**
   * gets the shots in this salvo
   *
   * @return the shots in this salvo
   */
  public ArrayList<Coord> getShots() {
    return shots;
  }

  /**
   * sets the shots in this salvo to the given shots
   *
   * @param shots the shots that this salvo will hold
   */
  public void setShots(ArrayList<Coord> shots) {
    this.shots = shots;
  }
}
