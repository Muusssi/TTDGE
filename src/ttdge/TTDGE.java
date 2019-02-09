package ttdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import processing.core.PApplet;

public class TTDGE {
  public final static String ENGINE_VERSION = "0.1.0";
  protected static PApplet papplet = null;
  public static TTDGEExtender extender = null;

  public static boolean show_searched_points = false;
  public static boolean debug_mode = false;

  public static int room_grid_size = 50;

  public static int x_offset = 50;
  public static int y_offset = 50;

  public static int map_grid_size = 5;

  public static int x_map_offset = 50;
  public static int y_map_offset = 50;

  public static GameCharacter player = null;
  public static TTDGEObject current_object = null;

  public static HashMap<String,World> worlds = new HashMap<String,World>();
  public static HashMap<String,TTDGEObject> objects = new HashMap<String,TTDGEObject>();

  protected static int id_counter = 1;

  public static LinkedList<String> messages = new LinkedList<String>();


  public static void start_engine(PApplet papplet) {
    TTDGE.papplet = papplet;
  }

  public static void start_engine(PApplet papplet, TTDGEExtender extender) {
    TTDGE.papplet = papplet;
    TTDGE.extender = extender;
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
    JSON json = new JSON(papplet.loadJSONObject(save_file));
    JSONarray worlds_json = json.getArray("worlds");
    World new_world = null;
    for (int w = 0; w < worlds_json.size(); w++) {
      JSON world_json = worlds_json.get(w);
      new_world = new World(world_json);
      System.out.println("Loaded world");
      TTDGE.load_rooms(new_world, world_json.getArray("rooms"));
    }
    TTDGE.current_object = new_world;
  }

  public static void load_rooms(World world, JSONarray rooms_json) {
    for (int i = 0; i < rooms_json.size(); i++) {
      JSON json = rooms_json.get(i);
      Room room = null;
      if (json.getString("type").equals("Room")) {
        System.out.println("Loading room");
        room = new Room(json, world);
        System.out.println("Room loaded");
      }
      else {
        if (extender != null) {
          room = extender.load_custom_room(world, json);
        }
        if (room == null) {
          fatal_error("Room type '" + json.getString("type") + "' of '" + json.getString("id") + "' is not known. Unable to load world.");
        }
      }
      load_things(room, json.getArray("things"));
    }
  }

  public static void load_things(Room room, JSONarray things_json) {
    for (int i = 0; i < things_json.size(); i++) {
      JSON json = things_json.get(i);
      if (json.getString("type").equals("Obstacle")) {
        System.out.println("Loading Obstacle");
        new Obstacle(json, room);
      }
      else if (json.getString("type").equals("Door")) {
        System.out.println("Loading Door");
        new Door(json, room);
      }
      else if (json.getString("type").equals("Item")) {
        System.out.println("Loading Item");
        Item item = new Item(json);
        room.add_thing(item);
      }
      else if (json.getString("type").equals("GameCharacter")) {
        System.out.println("Loading GameCharacter");
        new GameCharacter(json, room);
      }
      else {
        Thing thing = null;
        if (extender != null) {
          thing = extender.load_custom_thing(room, json);
        }
        if (thing == null) {
          fatal_error("Thing type '" + json.getString("type") + "' of '" + json.getString("id") + "' is not known. Unable to load world.");
        }
      }
    }
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

  public static void draw() {
    if (TTDGE.current_object != null) {
      TTDGE.current_object.draw();
    }
  }

  protected static int id_counter_next() {
    id_counter++;
    return id_counter;
  }

  // UI Helpers
  public static ArrayList<Button> buttons = new ArrayList<Button>();
  public static HashMap<Integer, Boolean> pressed_keys = new HashMap<Integer, Boolean>();
  public static int previous_mouse_press = -100;
  public static int clicked_x = -1;
  public static int clicked_y = -1;
  public static int pclicked_x = -1;
  public static int pclicked_y = -1;

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
    if (papplet.frameCount > previous_mouse_press) {
      pclicked_x = clicked_x;
      pclicked_y = clicked_y;
      clicked_x = papplet.mouseX;
      clicked_y = papplet.mouseY;
    }
    previous_mouse_press = papplet.frameCount;
    // Check buttons
    Iterator<Button> itr = buttons.iterator();
    while (itr.hasNext()) {
      Button btn = itr.next();
        if (btn.drawn_on_frame == papplet.frameCount && btn.cursor_points()) {
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

  public static void draw_buttons(ArrayList<Button> buttons) {
    Iterator<Button> itr = buttons.iterator();
    while (itr.hasNext()) {
      itr.next().draw();
    }
  }

  public static void dialog() {
    Object result = JOptionPane.showInputDialog(null, "Enter printer name:");
    System.out.println(result);
  }


}
