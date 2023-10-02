package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * represents a message to be sent/received to/from the server
 *
 * @param messageName the name of the message
 * @param arguments the content of the message
 */
public record MessageJson(
    @JsonProperty("method-name") String messageName,
    @JsonProperty("arguments") JsonNode arguments
) {
}
