package cs3500.pa04.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Simple utils class used to hold static methods that help with serializing and deserializing JSON.
 */
public class JsonUtils {
  /**
   * Converts a given record object to a MessageJson
   * if the record is null a message is returned with empty arguments
   *
   * @param record the record to convert or null for an empty message
   * @param msgName the messageName given to the returned value
   * @return the JsonNode representation of the given record in MessageJson format
   */
  public static JsonNode serializeRecord(Record record, String msgName) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode arg;
    if (record == null) {
      arg = mapper.createObjectNode();
    } else {
      arg = mapper.convertValue(record, JsonNode.class);
    }
    MessageJson msgJson = new MessageJson(msgName, arg);
    return mapper.convertValue(msgJson, JsonNode.class);
  }
}
