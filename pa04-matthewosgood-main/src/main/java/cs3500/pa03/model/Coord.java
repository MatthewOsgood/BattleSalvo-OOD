package cs3500.pa03.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Random;

/**
 * represents a coordinate on the board starting at (0,0)
 */
public record Coord(
    @JsonProperty("x") int width,
    @JsonProperty("y") int height
) {

  /**
   * gets the horizontal x value of this coord
   *
   * @return the x of this coord
   */
  public int getX() {
    return width;
  }

  /**
   * gets the vertical y value of this coord
   *
   * @return the y value
   */
  public int getY() {
    return height;
  }

  /**
   * creates a random coord between 0 and the given max inclusive
   *
   * @param maxX the maximum x coordinate inclusive
   * @param maxY the maximum y coordinate inclusive
   * @return a random Coord within the given range
   */
  public static Coord makeRandom(int maxX, int maxY) {
    Random random = new Random();
    return new Coord(random.nextInt(maxX + 1), random.nextInt(maxY + 1));
  }

  /**
   * checks if the given object is the same as this object by comparing its fields
   *
   * @param o the object that is compared with this
   * @return if the given object is the same as this object
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Coord) {
      return this.width == ((Coord) o).width
          && this.height == ((Coord) o).height;
    } else {
      return false;
    }
  }

}
