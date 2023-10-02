package cs3500.pa04.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.model.Coord;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import org.junit.jupiter.api.Test;

class ShipAdapterTest {

  Coord[] coordsHoriz = new Coord[] {new Coord(0, 0),
      new Coord(1, 0),
      new Coord(2, 0)};

  Coord[] coordsVert = new Coord[] {new Coord(0, 0),
      new Coord(0, 1),
      new Coord(0, 2)};

  Ship shipHoriz = new Ship(ShipType.SUBMARINE, coordsHoriz);
  Ship shipVert = new Ship(ShipType.SUBMARINE, coordsVert);

  ShipAdapter shipAdapterHoriz = new ShipAdapter(shipHoriz);
  ShipAdapter shipAdapterVert = new ShipAdapter(shipVert);

  @Test
  public void toShip() {
    Ship ship1 = shipAdapterHoriz.toShip();
    assertEquals(shipHoriz.getShipType(), ship1.getShipType());
    assertEquals(shipHoriz.getDirection(), ship1.getDirection());
    assertEquals(shipHoriz.getLocations()[0], ship1.getLocations()[0]);
    assertEquals(shipHoriz.getLocations()[1], ship1.getLocations()[1]);
    assertEquals(shipHoriz.getLocations()[2], ship1.getLocations()[2]);

    Ship ship2 = shipAdapterVert.toShip();
    assertEquals(shipVert.getShipType(), ship2.getShipType());
    assertEquals(shipVert.getDirection(), ship2.getDirection());
    assertEquals(shipVert.getLocations()[0], ship2.getLocations()[0]);
    assertEquals(shipVert.getLocations()[1], ship2.getLocations()[1]);
    assertEquals(shipVert.getLocations()[2], ship2.getLocations()[2]);
  }
}