package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.ShipType;
import java.util.Map;

/**
 * represents the setup message that will be received from the server
 *
 * @param width the width of the board
 * @param height the height of the board
 * @param fleetSpec the number of each ship that will be placed on the board
 */
public record SetupJson(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("fleet-spec") Map<ShipType, Integer> fleetSpec
) {
}
