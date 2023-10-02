package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.GameResult;

/**
 * the EndGame message received from the server in Json format
 *
 * @param result whether the player receiving the msg won, lost, or tied
 * @param reason the reason for the game ending
 */
public record EndGameJson(
    @JsonProperty("result") GameResult result,
    @JsonProperty("reason") String reason
) {
}
