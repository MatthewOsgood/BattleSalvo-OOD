package cs3500.pa04.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.model.GameResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonUtilsTest {

  EndGameJson endGameJson = new EndGameJson(GameResult.WIN, "You Won");
  ObjectMapper mapper = new ObjectMapper();


  @Test
  void serializeRecord() {
    JsonNode endGameMsg = JsonUtils.serializeRecord(endGameJson, "end-game");
    MessageJson msg = mapper.convertValue(endGameMsg, MessageJson.class);
    Assertions.assertEquals("end-game", msg.messageName());
    EndGameJson actual = mapper.convertValue(msg.arguments(), EndGameJson.class);
    Assertions.assertEquals(endGameJson, actual);
    JsonNode empty = JsonUtils.serializeRecord(null, "voidTest");
    MessageJson emptyMsg = mapper.convertValue(empty, MessageJson.class);
    try {
      Assertions.assertEquals("{}", mapper.writeValueAsString(emptyMsg.arguments()));
    } catch (JsonProcessingException e) {
      Assertions.fail("could not write empty node as string");
    }
  }
}