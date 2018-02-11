package ttdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import processing.data.JSONArray;
import processing.data.JSONObject;

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

  public JSONObject world_file_object() {
    JSONObject json = new JSONObject();
    json.setString("type", "World");
    json.setString("engine_version", TTDGE.ENGINE_VERSION);
    json.setString("name", this.name);
    return json;
  }

  public static World create(JSONObject world_json) {
    World new_world = new World(world_json.getString("name"));
    JSONArray things_json = world_json.getJSONArray("things");
    for (int i = 0; i < things_json.size(); i++) {
      JSONObject json = things_json.getJSONObject(i);
      new_world.load_thing(json);
    }

    return new_world;
  }

  public void save(String file_name) {
    JSONObject world_json = world_file_object();
    Iterator<Thing> itr = things.values().iterator();
    JSONArray things_json = new JSONArray();
    while (itr.hasNext()) {
      Thing thing = itr.next();
      things_json.append(thing.world_file_object());
    }
    world_json.setJSONArray("things", things_json);
    TTDGE.papplet.saveJSONObject(world_json, file_name);
  }

  Thing load_thing(JSONObject json) {
    String thing_type = json.getString("type");
    if (thing_type.equals("Room")) {
      Room new_room = Room.create(this, json);
      rooms.add(new_room);
      return new_room;
    }
    else if (thing_type.equals("Door")) {
      Door.create(this, json);
    }
    else if (thing_type.equals("Obstacle")) {
      Obstacle.create(this, json);
    }
    else if (thing_type.equals("GameCharacter")) {
      GameCharacter.create(this, json);
    }
//    else if (thing_type.equals("Door")) {
//      Door.create(this, json);
//    }
    else if (thing_type.equals("Item")) {
      //create_item(tokens);
    }
    else {
      TTDGE.fatal_error("World file corrupted: '"+world_file+"'. Unsupported thing: '"+thing_type+"'");
    }
    return null;
  }

  public Room get_room(String room_id) {
    Room room = null;
    if (room_id != null) {
      room = (Room)things.get(room_id);
    }
    return room;
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
