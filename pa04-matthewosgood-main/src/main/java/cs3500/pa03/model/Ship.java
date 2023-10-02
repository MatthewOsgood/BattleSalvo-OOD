package cs3500.pa03.model;

import cs3500.pa04.json.Direction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * represents a single ship on the board
 */
public class Ship {
  /**
   * the type of ship this is
   */
  private final ShipType shipType;
  /**
   * the Coords this ship occupies
   */
  private Coord[] locations;
  /**
   * the Coords where this ship has been hit
   */
  private final ArrayList<Coord> hits;

  /**
   * constructs a ship of the given type at the given locations
   *
   * @param shipType  the type of thip to be made
   * @param locations the locations this ship occupies
   */
  public Ship(ShipType shipType, Coord[] locations) {
    this.shipType = shipType;
    this.setLocations(locations);
    this.hits = new ArrayList<>();
  }

  /**
   * sets the locations of this ship ensuring that it
   *
   * @param givenLocations the locations that will be set
   */
  private void setLocations(Coord[] givenLocations) {
    if (givenLocations.length == this.shipType.getSize()) {
      this.locations = givenLocations;
    } else {
      throw new IllegalArgumentException(
          "the number of locations isn't equal to the size of this ship. \n"
              + Arrays.toString(givenLocations) + "\n"
              + "should be: " + this.shipType.getSize());
    }
  }

  /**
   * gets the symbol that represents this ship as defined by the ShipType class
   *
   * @return the symbol that represents this ship
   */
  public Symbol getSymbol() {
    return this.shipType.getSymbol();
  }

  /**
   * gets a clone of this ships locations
   *
   * @return a copy of the locations of this ship
   */
  public Coord[] getLocations() {
    return this.locations.clone();
  }

  /**
   * gets the type of this ship
   *
   * @return the type of this ship
   */
  public ShipType getShipType() {
    return this.shipType;
  }

  /**
   * checks if this ship has been sunk
   * assumes this ship has been properly formed
   *
   * @return if this ship has been suck
   */
  public boolean isSunk() {
    return this.shipType.getSize() == this.hits.size();
  }

  /**
   * checks if this ship is at the given location
   *
   * @param coord the location we weant to check
   * @return if this ship is at the given location
   */
  public boolean isAtLocation(Coord coord) {
    List<Coord> locationsList = Arrays.asList(this.locations);
    return locationsList.contains(coord);
  }

  /**
   * updates this ship if it was hit at the given Coord
   *
   * @param shot the location this ship was shot at
   * @return if the ship was hit by the given shot
   */
  public boolean shotAt(Coord shot) {
    if (this.isAtLocation(shot)) {
      this.hits.add(shot);
      return true;
    } else {
      return false;
    }
  }

  /**
   * finds the direction of this ship
   *
   * @return the direction of this ship
   */
  public Direction getDirection() {
    if (this.locations[0].getX() == this.locations[1].getX()) {
      return Direction.VERTICAL;
    } else {
      return Direction.HORIZONTAL;
    }
  }

}
