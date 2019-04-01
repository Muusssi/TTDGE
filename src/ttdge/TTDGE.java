package ttdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PImage;
import ttdge.tasks.ObtainTask;
import ttdge.tasks.VisitTask;

public class TTDGE {
  public final static String ENGINE_VERSION = "0.3.0";
  protected static PApplet papplet = null;
  public static TTDGEExtender extender = null;

  public static boolean show_searched_points = false;
  public static boolean debug_mode = false;

  public static int room_grid_size = 60;

  public static int x_offset = 50;
  public static int y_offset = 50;

  public static int map_grid_size = 5;

  public static int x_map_offset = 50;
  public static int y_map_offset = 50;

  public static int menu_scroll_offset = 0;

  public static GameCharacter player = null;
  public static TTDGEObject current_object = null;

  public static HashMap<String,World> worlds = new HashMap<String,World>();
  public static HashMap<String,TTDGEObject> objects = new HashMap<String,TTDGEObject>();
  public static HashMap<String,Room> rooms = new HashMap<String,Room>();
  public static HashMap<String,Item> items = new HashMap<String,Item>();

  public static ArrayList<Quest> quests = new ArrayList<Quest>();
  public static ArrayList<Quest> started_quests = new ArrayList<Quest>();

  protected static HashMap<String,PImage> loaded_images = new HashMap<String,PImage>();

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
      TTDGE.load_rooms(new_world, world_json.getArray("rooms"));
    }
    TTDGE.current_object = new_world;
    load_quests(json.getArray("quests"), false);
    load_quests(json.getArray("started_quests"), true);
  }

  protected static void load_quests(JSONarray quests_json, boolean started) {
    if (quests_json != null) {
      Quest new_quest;
      for (int q = 0; q < quests_json.size(); q++) {
        JSON quest_json = quests_json.get(q);
        new_quest = new Quest(quest_json);
        if (started) {
          TTDGE.started_quests.add(new_quest);
        }
        else {
          TTDGE.quests.add(new_quest);
        }
      }
    }
  }

  protected static void load_rooms(World world, JSONarray rooms_json) {
    for (int i = 0; i < rooms_json.size(); i++) {
      JSON json = rooms_json.get(i);
      Room room = null;
      if (json.getString("type").equals("Room")) {
        room = new Room(json, world);
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

  protected static void load_things(Room room, JSONarray things_json) {
    for (int i = 0; i < things_json.size(); i++) {
      JSON json = things_json.get(i);
      if (json.getString("type").equals("Obstacle")) {
        new Obstacle(json, room);
      }
      else if (json.getString("type").equals("Door")) {
        new Door(json, room);
      }
      else if (json.getString("type").equals("Item")) {
        new Item(json, room);
      }
      else if (json.getString("type").equals("GameCharacter")) {
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

  public static ArrayList<String> supported_tasks() {
    ArrayList<String> supported_tasks = new ArrayList<String>();
    supported_tasks.add("ObtainTask");
    supported_tasks.add("VisitTask");
    if (TTDGE.extender != null) {
      supported_tasks.addAll(TTDGE.extender.custom_tasks());
    }
    return supported_tasks;
  }

  protected static void load_tasks(Mission mission, JSONarray tasks_json) {
    for (int i = 0; i < tasks_json.size(); i++) {
      JSON json = tasks_json.get(i);
      System.out.println(json);
      if (json.getString("type").equals("ObtainTask")) {
        new ObtainTask(json, mission);
      }
      else if (json.getString("type").equals("VisitTask")) {
        new VisitTask(json, mission);
      }
      else {
        Task task = null;
        if (extender != null) {
          task = extender.load_custom_task(mission, json);
        }
        if (task == null) {
          fatal_error("Taks type '" + json.getString("type") + "' of task '" + json.getString("id") + "' is not known. Unable to load world.");
        }
      }
    }
  }

  public static void save(String save_file) {
    JSON json = new JSON();
    json.set("engine_version", TTDGE.ENGINE_VERSION);
    json.set("worlds", worlds_json());
    json.set("quests", quests_json());
    json.set("started_quests", started_quests_json());
    json.save(save_file);
  }

  protected static JSONarray worlds_json() {
    JSONarray worlds_json = new JSONarray();
    for (World w : worlds.values()) {
      worlds_json.append(w.save_file_object());
    }
    return worlds_json;
  }

  protected static JSONarray quests_json() {
    JSONarray quests_json = new JSONarray();
    for (Quest q : TTDGE.quests) {
      quests_json.append(q.save_file_object());
    }
    return quests_json;
  }

  protected static JSONarray started_quests_json() {
    JSONarray quests_json = new JSONarray();
    for (Quest q : TTDGE.started_quests) {
      quests_json.append(q.save_file_object());
    }
    return quests_json;
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

  protected static boolean load_image(String file_name) {
    if (TTDGE.loaded_images.containsKey(file_name)) {
      return true;
    }
    else {
      try {
        PImage image = papplet.loadImage(file_name);
        loaded_images.put(file_name, image);
        return true;
      } catch (Exception e) {
        TTDGE.error("Unable to load image '" + file_name + "'.");
      }
    }
    return false;

  }

  public static Thing pointed_thing() {
    if (TTDGE.current_object != null) {
      return TTDGE.current_object.pointed_thing();
    }
    return null;
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

  public static int calculate_rows(String text, int max_width) {
    String[] words = text.split(" ");
    float space_width = TTDGE.papplet.textWidth(' ');
    int rows = 1;
    float width = 0;
    for (String word : words) {
      float word_width = TTDGE.papplet.textWidth(word);
      if (width + word_width < max_width) {
        width += word_width + space_width;
      }
      else {
        rows++;
        width = word_width + space_width;
      }
    }
    return rows;
  }

  public static float long_text(String text, float offset, int text_size) {
    papplet.textSize(text_size);
    float height = TTDGE.calculate_rows(text, papplet.width - 60)*text_size*1.5f;
    papplet.text(text, 30, offset, papplet.width - 60, height);
    return offset + height;
  }

}
