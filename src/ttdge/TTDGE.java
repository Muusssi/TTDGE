package ttdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PApplet;

public class TTDGE {
  public final static String ENGINE_VERSION = "0.1.0.0";
  protected static PApplet papplet = null;

  public static boolean show_searched_points = false;
  public static boolean debug_mode = false;

  public static int room_grid_size = 50;

  public static int x_offset = 50;
  public static int y_offset = 50;

  public static int map_grid_size = 5;

  public static int x_map_offset = 50;
  public static int y_map_offset = 50;

  public static GameCharacter player;
  public static Room active_room = null;

  public static HashMap<String,World> worlds = new HashMap<String,World>();

  public static LinkedList<String> messages = new LinkedList<String>();


  public static void start_engine(PApplet papplet) {
    TTDGE.papplet = papplet;
  }

  public static void fatal_error(String message) {
    System.out.println("Fatal error: " + message);
    System.exit(1);
  }

  public static void error(String message) {
    System.out.println("Error: " + message);
  }

  public static void message(String message) {
    messages.add(message);
    System.out.println(message);
  }


  public static World load_world(String world_file) {
    JSON world_json = new JSON(papplet.loadJSONObject(world_file));
    World new_world = World.create(world_json);
    new_world.link_things();
    return new_world;
  }

  public static void draw_active_room() {
    if (active_room != null) {
      active_room.draw();
    }
    else {
      fatal_error("Unable to draw room: active room unknown!");
    }
  }

  // UI Helpers
  public static ArrayList<Button> buttons = new ArrayList<Button>();
  public static HashMap<Integer, Boolean> pressed_keys = new HashMap<Integer, Boolean>();

  // Key follower
  public static boolean is_key_pressed(int key) {
      if (pressed_keys.containsKey(key) ) {
          return pressed_keys.get(key);
      }
      return false;
  }

  public static void notice_key_press() {
    pressed_keys.put(TTDGE.papplet.keyCode, true);
    pressed_keys.put((int)TTDGE.papplet.key, true);
  }

  public static void notice_key_release() {
    pressed_keys.put(TTDGE.papplet.keyCode, false);
    pressed_keys.put((int)TTDGE.papplet.key, false);
  }

  public static TUIelement notice_mouse_press() {
      Iterator<Button> itr = buttons.iterator();
      while (itr.hasNext()) {
        Button btn = itr.next();
          if (btn.cursor_points()) {
              btn.press();
              return btn;
          }
      }
      return null;
  }

  public static void draw_buttons() {
      Iterator<Button> itr = buttons.iterator();
      while (itr.hasNext()) {
          itr.next().draw();
      }
  }



}
