package cs3500.pa04.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.model.Coord;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class FleetJsonTest {

  /**
   * Jackson practice
   */
  @Test
  void testJson() {
    ShipAdapter ship1 = new ShipAdapter(new Coord(0, 0), 3, Direction.HORIZONTAL);
    ShipAdapter ship2 = new ShipAdapter(new Coord(0, 4), 5, Direction.VERTICAL);
    ArrayList<ShipAdapter> fleet = new ArrayList<>(Arrays.asList(ship1, ship2));
    FleetJson fleetJson = new FleetJson(fleet);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.convertValue(fleetJson, JsonNode.class);
    assertEquals(fleetJson, mapper.convertValue(node, fleetJson.getClass()));
  }
}