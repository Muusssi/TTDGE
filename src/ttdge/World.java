package ttdge;

import java.util.ArrayList;
import java.util.Iterator;

public class World extends TTDGEObject {

  public String name;

  public ArrayList<Room> rooms = new ArrayList<Room>();
  public Room active_room = null;

  public World(String name) {
    super();
    this.name = name;
    TTDGE.worlds.put(name, this);
  }

  public World(JSON json) {
    super(json);
    this.name = json.getString("name");
    TTDGE.worlds.put(name, this);
  }

  @Override
  public JSON save_file_object() {
    JSON json = super.save_file_object();
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

  @Override
  public void draw_on_parent() {}

  @Override
  public void draw() {
    for (Room room : rooms) {
      room.draw_on_parent();
    }
  }

  public String id_prefix() {
    return "World";
  }

  @Override
  public String type_name() {
    return "World";
  }

  @Override
  public Thing pointed_thing() {
    for (Room room : rooms) {
      if (room.is_pointed_on_parent()) {
        return room;
      }
    }
    return null;
  }

  @Override
  public boolean is_pointed() {
    return false;
  }

  @Override
  public boolean is_pointed_on_parent() {
    return false;
  }


}
