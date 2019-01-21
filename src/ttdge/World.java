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
  public ArrayList<GameCharacter> game_characters = new ArrayList<GameCharacter>();

  public World(String name) {
    this.name = name;
    TTDGE.worlds.put(name, this);
  }

  public JSON world_file_object() {
    JSON json = new JSON();
    json.set("type", "World");
    json.set("engine_version", TTDGE.ENGINE_VERSION);
    json.set("name", this.name);
    return json;
  }

  public static World create(JSON world_json) {
    World new_world = new World(world_json.getString("name"));
    JSONarray things_json = world_json.getArray("things");
    for (int i = 0; i < things_json.array.size(); i++) {
      JSON json = things_json.get(i);
      new_world.load_thing(json);
    }

    return new_world;
  }

  public void save(String file_name) {
    JSON world_json = world_file_object();
    Iterator<Thing> itr = things.values().iterator();
    JSONarray things_json = new JSONarray();
    while (itr.hasNext()) {
      Thing thing = itr.next();
      things_json.append(thing.world_file_object());
    }
    world_json.set("things", things_json);
    TTDGE.papplet.saveJSONObject(world_json.json, file_name);
  }

  Thing load_thing(JSON json) {
    String thing_type = json.getString("type");
    Thing loaded_thing = null;
    if (thing_type.equals("Room")) {
      loaded_thing = Room.create(this, json);
    }
    else if (thing_type.equals("Door")) {
      loaded_thing = Door.create(this, json);
    }
    else if (thing_type.equals("Obstacle")) {
      loaded_thing = Obstacle.create(this, json);
    }
    else if (thing_type.equals("GameCharacter")) {
      loaded_thing = GameCharacter.create(this, json);
    }
    else if (thing_type.equals("Item")) {
      loaded_thing = Item.create(this, json);
    }
    else {
      TTDGE.fatal_error("World file corrupted: '"+world_file+"'. Unsupported thing: '"+thing_type+"'");
    }
    return loaded_thing;
  }

  public void draw_map() {
    Iterator<Room> itr = rooms.iterator();
    while (itr.hasNext()) {
      itr.next().draw_on_map();
      // TODO: door link lines
    }
  }

  public Room pointed_thing_on_map() {
    Iterator<Room> itr = this.rooms.iterator();
    while (itr.hasNext()) {
      Room room = itr.next();
      if (TTDGE.papplet.mouseX - TTDGE.x_map_offset > room.world_map_x*TTDGE.map_grid_size
        && TTDGE.papplet.mouseX - TTDGE.x_map_offset < room.world_map_x*TTDGE.map_grid_size + room.room_width*TTDGE.map_grid_size
        && TTDGE.papplet.mouseY - TTDGE.y_map_offset > room.world_map_y*TTDGE.map_grid_size
        && TTDGE.papplet.mouseY - TTDGE.y_map_offset < room.world_map_y*TTDGE.map_grid_size + room.room_height*TTDGE.map_grid_size) {
        return room;
      }
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
