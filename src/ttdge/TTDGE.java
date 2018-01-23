package ttdge;

import java.util.HashMap;

import processing.core.PApplet;

public class TTDGE {
  public final static String ENGINE_VERSION = "0.1.0.0";
  public final static String WORLD_FILE_DELIMITER = "||";

  protected static PApplet papplet = null;

  public static boolean show_searched_points = false;

  public static int room_grid_size = 50;

  public static int x_offset = 50;
  public static int y_offset = 50;

  public static GameCharacter player;

  public static HashMap<String,World> worlds = new HashMap<String,World>();


  public void start_engine(PApplet papplet) {
    TTDGE.papplet = papplet;
  }

  public static void fatal_error(String message) {
    System.out.println("Fatal error: " + message);
    System.exit(1);
  }

  public static void error(String message) {
    System.out.println("Error: " + message);
  }


  public static World load_world(String world_file) {
    String[] lines = papplet.loadStrings(world_file);
    World new_world = null;
    for (String line : lines) {
      if (!line.substring(0, 1).equals("#")) {
        String[] tokens = line.split(WORLD_FILE_DELIMITER);
        String thing_type = tokens[0];
        if (thing_type.equals("World")) {
          new_world = World.create(tokens);
          new_world.world_file = world_file;
        }
        else {
          if (new_world == null) {
            fatal_error("Corrupted world file '"+world_file+"'. World not first thing.");
            return null;
          }
          new_world.load_thing(tokens);
        }
      }

    }
    return new_world;
  }

  static void draw_active_room() {
    if (player != null && player.room != null) {
      player.room.draw();
    }
    else {
      fatal_error("Unable to draw room: active room unknown!");
    }
  }

}
