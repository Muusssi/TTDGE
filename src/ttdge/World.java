package ttdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class World {

  public static int WORLD_STRING_ELEMENTS = 2;

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
    String[] elements = new String[WORLD_STRING_ELEMENTS];
    elements[0] = id_prefix();
    elements[1] = name;
    return String.join(TTDGE.WORLD_FILE_DELIMITER, elements);
  }

  public static World create(String[] tokens) {
    if (tokens.length != WORLD_STRING_ELEMENTS) {
      TTDGE.fatal_error("Corrupted world file string: wrong number of elements for a world.");
      return null;
    }
    else {
      return new World(tokens[1]);
    }
  }

  public void save(String file_name) {
    ArrayList<String> lines = new ArrayList<String>();
    lines.add("# TTDGE world file. Engine version: " + TTDGE.ENGINE_VERSION);
    lines.add(this.world_file_string());
    Iterator<Thing> itr = things.values().iterator();
    while (itr.hasNext()) {
      Thing thing = itr.next();
      String world_file_string = thing.world_file_string();
      //System.out.println(world_file_string);
      lines.add(world_file_string);
    }
    TTDGE.papplet.saveStrings(file_name, lines.toArray(new String[0]));
  }

  Thing load_thing(String[] tokens) {
    String thing_type = tokens[0];
    if (thing_type.equals("Room")) {
      Room new_room = Room.create(this, tokens);
      rooms.add(new_room);
      return new_room;
    }
    else if (thing_type.equals("Door")) {
      Door.create(this, tokens);
    }
    else if (thing_type.equals("Obstacle")) {
      Obstacle.create(this, tokens);
    }
    else if (thing_type.equals("GameCharacter")) {
      GameCharacter.create(this, tokens);
    }
    else if (thing_type.equals("Door")) {
      Door.create(this, tokens);
    }
    else if (thing_type.equals("Item")) {
      //create_item(tokens);
    }
    else {
      TTDGE.fatal_error("World file corrupted: '"+world_file+"'. Unsupported thing: '"+thing_type+"'");
    }
    return null;
  }

  public void link_things() {
    Iterator<Thing> itr = things.values().iterator();
    while(itr.hasNext()) {
      itr.next().linking_actions();
    }
  }

  public String id_prefix() {
    return "World";
  }

  int id_counter_next() {
    id_counter++;
    return id_counter;
  }
}
