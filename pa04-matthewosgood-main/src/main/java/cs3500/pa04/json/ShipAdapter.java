package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;

/**
 * adapts the ship class into the JSON format required to communicate with the server
 */
public record ShipAdapter(
    @JsonProperty("coord") Coord coord,
    @JsonProperty("length") int length,
    @JsonProperty("direction") Direction direction
) {

  /**
   * adapts a ship into a ShipAdapter
   *
   * @param ship the ship this ShipAdapter is adapting
   */
  public ShipAdapter(Ship ship) {
    this(ship.getLocations()[0], ship.getShipType().getSize(), ship.getDirection());
  }


  /**
   * Returns the Ship that this represents
   *
   * @return the Ship that this ShipAdapter represents
   */
  public Ship toShip() {
    Coord[] locations = new Coord[this.length];
    locations[0] = this.coord;
    for (int i = 1; i < this.length; i++) {
      if (this.direction == Direction.HORIZONTAL) {
        locations[i] = new Coord(this.coord.getX() + i, this.coord.getY());
      } else {
        locations[i] = new Coord(this.coord.getX(), this.coord.getY() + i);
      }
    }
    return new Ship(ShipType.fromSize(this.length), locations);
  }
}
