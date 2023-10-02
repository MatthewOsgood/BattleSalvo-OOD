package cs3500.pa04.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.model.Coord;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class VolleyJsonTest {

  @Test
  void testVolleyJson() {
    List<Coord> coords = new ArrayList<>();
    coords.add(new Coord(0, 0));
    coords.add(new Coord(1, 1));
    coords.add(new Coord(2, 2));

    VolleyJson volleyJson = new VolleyJson(coords);

    ObjectMapper mapper = new ObjectMapper();
    String volleyJsonAsString = null;
    try {
      volleyJsonAsString = mapper.writeValueAsString(volleyJson);
    } catch (JsonProcessingException e) {
      fail("failed to write VolleyJson as string");
    }
    String jsonString = """
        {"coordinates":[{"x":0,"y":0},{"x":1,"y":1},{"x":2,"y":2}]}""";

    assertEquals(jsonString, volleyJsonAsString);
  }

}