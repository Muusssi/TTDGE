package ttdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PApplet;

public class TTDGE {
  public final static String ENGINE_VERSION = "0.1.0";
  protected static PApplet papplet = null;

  public static boolean show_searched_points = false;
  public static boolean debug_mode = false;

  public static int room_grid_size = 50;

  public static int x_offset = 50;
  public static int y_offset = 50;

  public static int map_grid_size = 5;

  public static int x_map_offset = 50;
  public static int y_map_offset = 50;

  public static GameCharacter player = null;
  public static World active_world = null;

  public static HashMap<String,World> worlds = new HashMap<String,World>();
  public static HashMap<String,TTDGEObject> objects = new HashMap<String,TTDGEObject>();

  protected static int id_counter = 1;

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

  public static void load(String save_file) {
    // TODO: load()
  }

  public static void save(String save_file) {
    JSON json = new JSON();
    json.set("engine_version", TTDGE.ENGINE_VERSION);
    JSONarray worlds_json = new JSONarray();
    for (World w : worlds.values()) {
      worlds_json.append(w.save_file_object());
    }
    json.set("worlds", worlds_json);
    json.save(save_file);
  }

  protected static int id_counter_next() {
    id_counter++;
    return id_counter;
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
