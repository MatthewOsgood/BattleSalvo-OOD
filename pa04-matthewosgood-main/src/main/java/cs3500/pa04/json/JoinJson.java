package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * represents the join message that will be sent to the server
 *
 * @param name the users name
 * @param gameType the type of game that will be played
 */
public record JoinJson(
    @JsonProperty("name") String name,
    @JsonProperty("game-type") GameType gameType
) {
}
