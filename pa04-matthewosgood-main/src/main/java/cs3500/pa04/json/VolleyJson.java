package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.Coord;
import java.util.List;

/**
 * represents a volley in a Json format
 *
 * @param coordinates the coordinates of each shot in the volley
 */
public record VolleyJson(
    @JsonProperty("coordinates") List<Coord> coordinates
) {
}
