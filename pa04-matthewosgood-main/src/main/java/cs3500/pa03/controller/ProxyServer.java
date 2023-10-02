package cs3500.pa03.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.model.AiPlayer;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.view.View;
import cs3500.pa04.json.EndGameJson;
import cs3500.pa04.json.FleetJson;
import cs3500.pa04.json.GameType;
import cs3500.pa04.json.JoinJson;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.SetupJson;
import cs3500.pa04.json.ShipAdapter;
import cs3500.pa04.json.VolleyJson;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * acts as a proxy to communicate with a server and play Battle Salvo
 */
public class ProxyServer implements Controller {

  /**
   * the server this is proxying for
   */
  private final Socket server;
  /**
   * where input is received from the server
   */
  private final InputStream in;
  /**
   * where output is sent to the server
   */
  private final PrintStream out;
  /**
   * mapper used to convert between JsonNodes and Records
   */
  private final ObjectMapper mapper;
  /**
   * the local player
   */
  private final AiPlayer player;
  /**
   * the type of game being played on the server
   */
  private final GameType gameType;
  private final View view;

  /**
   * construct a ProxyServer with the given server, player, and GameType
   *
   * @param server   the server to be played on
   * @param player   the player that will play locally
   * @param gameType the type of game to play
   * @param view     the view used to output the game
   * @throws IOException if an error with the server connection occurs
   */
  public ProxyServer(Socket server, AiPlayer player, GameType gameType, View view)
      throws IOException {
    this.server = server;
    in = server.getInputStream();
    out = new PrintStream(server.getOutputStream());
    this.view = view;
    this.mapper = new ObjectMapper();
    this.player = player;
    this.gameType = gameType;
  }

  /**
   * plays a game of BattleSalvo
   */
  @Override
  public void play() {
    JsonParser parser;
    try {
      parser = this.mapper.getFactory().createParser(this.in);
      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        this.delegateMessage(message);
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Sorry. An unexpected issue occurred with the server");
    }
  }

  /**
   * delegates the given message to the appropriate helper method
   * if the game is over the socket is closed
   *
   * @param msg the message that will be handled
   */
  private void delegateMessage(MessageJson msg) {
    switch (msg.messageName()) {
      case "join" -> this.handleJoin();
      case "setup" -> this.handleSetup(msg.arguments());
      case "take-shots" -> this.handleTakeShots();
      case "report-damage" -> this.handleReportDamage(msg.arguments());
      case "successful-hits" -> this.handleSuccessfulHits(msg.arguments());
      case "end-game" -> this.handleEndGame(msg.arguments());
      default -> throw new IllegalArgumentException("not a valid message");
    }
  }

  /**
   * generates the proper join message response to the server
   */
  private void handleJoin() {
    JoinJson joinMsg = new JoinJson(this.player.name(), this.gameType);
    JsonNode response = JsonUtils.serializeRecord(joinMsg, "join");
    this.out.println(response);
    try {
      this.view.write("joined server");
    } catch (IOException e) {
      System.out.println("VIEW ERROR: joined server");
    }
  }

  /**
   * parses the setup message from the server and generates the proper response
   *
   * @param arg the setup node received from the server
   */
  private void handleSetup(JsonNode arg) {
    SetupJson setupJson = this.mapper.convertValue(arg, SetupJson.class);
    int height = setupJson.height();
    int width = setupJson.width();
    Map<ShipType, Integer> spec = setupJson.fleetSpec();
    List<Ship> ships = this.player.setup(height, width, spec);

    ArrayList<ShipAdapter> shipAdapters = new ArrayList<>();
    for (Ship ship : ships) {
      if (ship != null) {
        shipAdapters.add(new ShipAdapter(ship));
      }
    }
    FleetJson fleetJson = new FleetJson(shipAdapters);
    JsonNode response = JsonUtils.serializeRecord(fleetJson, "setup");
    this.out.println(response);
    try {
      this.view.write("Opponents Board:");
      this.view.drawBoard(this.player.getOpponentsBoard().getBoard());
      this.view.write("AI Board:");
      this.view.drawBoard(this.player.getMyBoard().getBoard());
    } catch (IOException e) {
      System.out.println("VIEW ERROR: setup");
    }
  }

  /**
   * has the player take shots and generates the proper message to the server
   */
  private void handleTakeShots() {
    List<Coord> shots = this.player.takeShots();
    VolleyJson coordinates = new VolleyJson(shots);
    JsonNode response = JsonUtils.serializeRecord(coordinates, "take-shots");
    this.out.println(response);
  }

  /**
   * parses the report damage message from the server, delegates to the player for the damage done,
   * and generates the proper response
   *
   * @param arg the Json coordinates that will be parsed
   */
  private void handleReportDamage(JsonNode arg) {
    VolleyJson shots = this.mapper.convertValue(arg, VolleyJson.class);
    List<Coord> hits = this.player.reportDamage(shots.coordinates());
    VolleyJson damage = new VolleyJson(hits);
    JsonNode response = JsonUtils.serializeRecord(damage, "report-damage");
    this.out.println(response);
  }

  /**
   * parses the given successful hits, notifies the player, and forms a response
   * to be sent to the server
   *
   * @param arg the volley of shots that were successful in Json form
   */
  private void handleSuccessfulHits(JsonNode arg) {
    VolleyJson hits = this.mapper.convertValue(arg, VolleyJson.class);
    this.player.successfulHits(hits.coordinates());
    JsonNode response = JsonUtils.serializeRecord(null, "successful-hits");
    this.out.println(response);
    try {
      this.view.write("Opponents Board:");
      this.view.drawBoard(this.player.getOpponentsBoard().getBoard());
      this.view.write("AI Board:");
      this.view.drawBoard(this.player.getMyBoard().getBoard());
    } catch (IOException e) {
      System.out.println("VIEW ERROR: successful hits board output");
    }
  }

  /**
   * parses the given end game message and notifies the player, before returning an empty response
   * to be sent to the server
   *
   * @param arg the EndGame Json received from the server
   */
  private void handleEndGame(JsonNode arg) {
    EndGameJson endGameJson = this.mapper.convertValue(arg, EndGameJson.class);
    this.player.endGame(endGameJson.result(), endGameJson.reason());
    JsonNode response = JsonUtils.serializeRecord(null, "end-game");
    this.out.println(response);
    try {
      this.server.close();
    } catch (IOException e) {
      System.out.println("failed to close socket");
    }
    try {
      this.view.write(endGameJson.reason());
    } catch (IOException e) {
      System.out.println("VIEW ERROR: " + endGameJson.reason());
    }
  }

}

