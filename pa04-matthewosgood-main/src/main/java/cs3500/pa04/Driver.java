package cs3500.pa04;

import cs3500.pa03.controller.LocalGameController;
import cs3500.pa03.controller.ProxyServer;
import cs3500.pa03.model.AiPlayer;
import cs3500.pa03.model.ManualPlayer;
import cs3500.pa03.model.Salvo;
import cs3500.pa03.view.View;
import cs3500.pa04.json.GameType;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      Driver.playLocal();
    } else {
      String host = args[0];
      int port = Integer.parseInt(args[1]);
      try {
        Driver.runOnline(host, port);
      } catch (IOException e) {
        System.out.println("failed to connect to the server");
      }
    }
  }

  /**
   * plays PA03 functionality
   */
  private static void playLocal() {
    Salvo salvo = new Salvo();
    ManualPlayer p1 = new ManualPlayer("MatthewOsgood", salvo);
    AiPlayer p2 = new AiPlayer("AI");
    InputStreamReader input = new InputStreamReader(System.in);
    View view = new View(input, System.out);
    LocalGameController localGameController = new LocalGameController(p1, salvo, p2, view);
    localGameController.play();
  }

  /**
   * plays PA04 functionality
   *
   * @param host the server host
   * @param port the server port
   * @throws IOException if an IO Exception occurs connecting to the server
   */
  private static void runOnline(String host, int port) throws IOException {
    try (Socket server = new Socket(host, port)) {
      AiPlayer cpu = new AiPlayer("MatthewOsgood");
      View view = new View(new InputStreamReader(System.in), System.out);
      ProxyServer proxy = new ProxyServer(server, cpu, GameType.SINGLE, view);
      proxy.play();
    }
  }
}