package cs3500.pa03.controller;

import com.fasterxml.jackson.databind.JsonNode;
import cs3500.pa03.model.AiPlayer;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.view.View;
import cs3500.pa04.Mocket;
import cs3500.pa04.json.EndGameJson;
import cs3500.pa04.json.GameType;
import cs3500.pa04.json.JoinJson;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.SetupJson;
import cs3500.pa04.json.VolleyJson;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProxyServerTest {

  ByteArrayOutputStream testLog;
  ProxyServer proxy;
  AiPlayer player;
  View view;
  StringReader input;
  StringBuilder output;
  EndGameJson endGameJson;
  JsonNode endGameNode;
  String endGameString;
  Map<ShipType, Integer> spec;


  /**
   * Converts the ByteArrayOutputStream log to a string in UTF_8 format
   *
   * @return String representing the current log buffer
   */
  private String logToString() {
    return testLog.toString(StandardCharsets.UTF_8);
  }

  /**
   * Reset the test log before each test is run.
   */
  @BeforeEach
  public void setup() {
    this.testLog = new ByteArrayOutputStream(2048);
    Assertions.assertEquals("", logToString());
    player = new AiPlayer("CPU");
    input = new StringReader("");
    output = new StringBuilder();
    view = new View(input, output);
    endGameJson = new EndGameJson(GameResult.DRAW, "end test");
    endGameNode = JsonUtils.serializeRecord(endGameJson, "end-game");
    endGameString = "\n{\"method-name\":\"end-game\",\"arguments\":{}}\n";
    spec = new HashMap<>();
    spec.put(ShipType.CARRIER, 2);
    spec.put(ShipType.BATTLESHIP, 4);
    spec.put(ShipType.DESTROYER, 1);
    spec.put(ShipType.SUBMARINE, 3);
  }

  @Test
  void testJoin() {
    JoinJson joinJson = new JoinJson("test name", GameType.SINGLE);
    JsonNode node = JsonUtils.serializeRecord(joinJson, "join");
    Mocket mocket = new Mocket(testLog, List.of(node.toString(), endGameNode.toString()));
    try {
      proxy = new ProxyServer(mocket, player, GameType.SINGLE, view);
    } catch (IOException e) {
      Assertions.fail("could not create proxy");
    }
    proxy.play();
    String expected = "{\"method-name\":\"join\","
        + "\"arguments\":{\"name\":\"CPU\",\"game-type\":\"SINGLE\"}}"
        + endGameString;
    Assertions.assertEquals(expected, logToString());
  }

  @Test
  void testSetup() {
    SetupJson setupJson = new SetupJson(10, 10, spec);
    JsonNode node = JsonUtils.serializeRecord(setupJson, "setup");
    Mocket mocket = new Mocket(testLog, List.of(node.toString(), endGameNode.toString()));
    try {
      proxy = new ProxyServer(mocket, player, GameType.SINGLE, view);
    } catch (IOException e) {
      Assertions.fail("could not create proxy");
    }
    proxy.play();
    Assertions.assertTrue(logToString().contains("method-name\":\"setup\""));
    Assertions.assertTrue(logToString().contains("arguments\":{\"fleet\":[{\"coord\":{"));
  }

  @Test
  void testTakeShots() {
    player.setup(10, 10, spec);
    JsonNode msgNode = JsonUtils.serializeRecord(null, "take-shots");
    Mocket mocket = new Mocket(testLog,
        List.of(msgNode.toString(), endGameNode.toString()));
    try {
      proxy = new ProxyServer(mocket, player, GameType.MULTI, view);
    } catch (IOException e) {
      Assertions.fail("could not create proxy");
    }
    proxy.play();
    String expected = "{\"method-name\":\"take-shots\",\""
        + "arguments\":{\"coordinates\":[{\"x\"";
    Assertions.assertTrue(logToString().contains(expected));
  }

  @Test
  void testReportDamage() {
    player.setup(10, 10, spec);
    Coord coord = new Coord(2, 3);
    List<Coord> shots = new ArrayList<>();
    shots.add(coord);
    VolleyJson volley = new VolleyJson(shots);
    JsonNode node = JsonUtils.serializeRecord(volley, "report-damage");
    Mocket mocket = new Mocket(testLog, List.of(node.toString(), endGameNode.toString()));
    try {
      proxy = new ProxyServer(mocket, player, GameType.SINGLE, view);
    } catch (IOException e) {
      Assertions.fail("could not create proxy");
    }
    proxy.play();
    String expected = "{\"method-name\":\"report-damage\",\"arguments\":";
    Assertions.assertTrue(logToString().contains(expected));
  }

  @Test
  void testSuccessfulHits() {
    player.setup(10, 10, spec);
    Coord coord = new Coord(2, 3);
    List<Coord> shots = new ArrayList<>();
    shots.add(coord);
    VolleyJson volleyJson = new VolleyJson(shots);
    JsonNode node = JsonUtils.serializeRecord(volleyJson, "successful-hits");
    Mocket mocket = new Mocket(testLog, List.of(node.toString(), endGameNode.toString()));
    try {
      proxy = new ProxyServer(mocket, player, GameType.SINGLE, view);
    } catch (IOException e) {
      Assertions.fail("could not create proxy");
    }
    proxy.play();
    String expected = "{\"method-name\":\"successful-hits\",\"arguments\":{}}"
        + endGameString;
    Assertions.assertEquals(expected, logToString());
  }

  @Test
  void testDelegateMessageError() {
    JsonNode badNode = JsonUtils.serializeRecord(null, "INVALID MSG NAME");
    Mocket mocket = new Mocket(testLog, List.of(badNode.toString(), endGameNode.toString()));
    try {
      proxy = new ProxyServer(mocket, player, GameType.SINGLE, view);
    } catch (IOException e) {
      Assertions.fail("could not create proxy");
    }
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> proxy.play());
  }
}