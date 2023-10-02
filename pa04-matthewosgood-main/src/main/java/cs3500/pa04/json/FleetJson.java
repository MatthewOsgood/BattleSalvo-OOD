package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * represents a fleet of ships in a Json form
 *
 * @param fleet the list of ShipAdaptors this represents
 */
public record FleetJson(
    @JsonProperty("fleet") List<ShipAdapter> fleet
) {
}
