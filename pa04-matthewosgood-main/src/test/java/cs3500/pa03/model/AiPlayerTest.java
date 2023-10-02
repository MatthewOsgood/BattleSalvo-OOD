package cs3500.pa03.model;

import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AiPlayerTest {

  Salvo salvo = new Salvo();
  AiPlayer player = new AiPlayer("test");
  HashMap<ShipType, Integer> specifications = new HashMap<>();

  @Test
  void takeShots() {
    player.currentSalvo = salvo;
    specifications.put(ShipType.CARRIER, 2);
    specifications.put(ShipType.BATTLESHIP, 2);
    specifications.put(ShipType.DESTROYER, 2);
    specifications.put(ShipType.SUBMARINE, 2);
    player.setup(8, 12, specifications);

    Assertions.assertEquals(8, player.takeShots().size());
    for (Coord shot : salvo.getShots()) {
      Assertions.assertTrue(shot.getX() < 12);
      Assertions.assertTrue(shot.getY() < 8);
    }
  }
}