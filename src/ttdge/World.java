package ttdge;

import java.util.ArrayList;
import java.util.Iterator;

public class World extends TTDGEObject {

  public String name;

  public ArrayList<Room> rooms = new ArrayList<Room>();
  public Room active_room = null;
  public ArrayList<GameCharacter> game_characters = new ArrayList<GameCharacter>();

  public World(String name) {
    super();
    this.name = name;
    TTDGE.worlds.put(name, this);
  }

  @Override
  public JSON save_file_object() {
    JSON json = new JSON();
    json.set("type", "World");
    json.set("name", this.name);
    json.set("rooms", rooms_json());
    return json;
  }

  public JSONarray rooms_json() {
    JSONarray rooms_json = new JSONarray();
    Iterator<Room> itr = rooms.iterator();
    while (itr.hasNext()) {
      rooms_json.append(itr.next().save_file_object());
    }
    return rooms_json;
  }

  public Room add_room(String name, String description, int width, int height) {
    return new Room(this, null, null, null, width, height);
  }

  public Room add_room(int width, int height) {
    return add_room(null, null, width, height);
  }

  @Override
  public void draw_on_parent() {}


  @Override
  public void draw() {
    Iterator<TTDGEObject> itr = children.iterator();
    while (itr.hasNext()) {
      itr.next().draw();
    }
  }

  public Room pointed_thing_on_map() {
    Iterator<Room> itr = this.rooms.iterator();
    while (itr.hasNext()) {
      Room room = itr.next();
      if (TTDGE.papplet.mouseX - TTDGE.x_map_offset > room.world_map_x*TTDGE.map_grid_size
        && TTDGE.papplet.mouseX - TTDGE.x_map_offset < room.world_map_x*TTDGE.map_grid_size + room.width*TTDGE.map_grid_size
        && TTDGE.papplet.mouseY - TTDGE.y_map_offset > room.world_map_y*TTDGE.map_grid_size
        && TTDGE.papplet.mouseY - TTDGE.y_map_offset < room.world_map_y*TTDGE.map_grid_size + room.height*TTDGE.map_grid_size) {
        return room;
      }
    }
    return null;
  }

  public String id_prefix() {
    return "World";
  }

  @Override
  public String type_name() {
    return "World";
  }


}
