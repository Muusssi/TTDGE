package ttdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class World {
  public String name;
  public String world_file = null;

  protected int id_counter = 1;

  public HashMap<String,Thing> things = new HashMap<String,Thing>();

  public ArrayList<Room> rooms = new ArrayList<Room>();
  public Room starting_room = null;


  public World(String name) {
    this.name = name;
    TTDGE.worlds.put(name, this);
  }

  String world_file_string() {
    return "World" + TTDGE.WORLD_FILE_DELIMITER + name;
  }

  public static World create(String[] tokens) {
    // TODO
    return null;
  }

  void save_world(String file_name) {
    ArrayList<String> lines = new ArrayList<String>();
    Iterator<Thing> itr = things.values().iterator();
    while (itr.hasNext()) {
      Thing thing = itr.next();
      lines.add(thing.world_file_string());
    }
    TTDGE.papplet.saveStrings(file_name, (String[]) lines.toArray());
  }

  Thing load_thing(String[] tokens) {
    String thing_type = tokens[0];
    if (thing_type.equals("Room")) {
      //return new Room(this, tokens[1], int(tokens[2]), int(tokens[3]));
    }
    else if (thing_type.equals("Door")) {
      //create_door(tokens);
    }
    else if (thing_type.equals("Obstacle")) {
      //create_obstacle(tokens);
    }
    else if (thing_type.equals("GameCharacter")) {
      //create_character(tokens);
    }
    else if (thing_type.equals("Item")) {
      //create_item(tokens);
    }
    else {
      TTDGE.fatal_error("World file corrupted: '"+world_file+"'. Unsupported thing: '"+thing_type+"'");
    }
    return null;
  }

  int id_counter_next() {
    id_counter++;
    return id_counter;
  }
}
