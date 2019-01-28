package ttdge;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import processing.core.PApplet;

public class Room extends Thing {

  // TODO: register character visits
  public HashMap<String,Integer> visits = new HashMap<String,Integer>();

  public int world_map_x = 0;
  public int world_map_y = 0;


  public Room (World world, String id, String name, String description, int width, int height) {
    super(id, name, description);
    this.width = width;
    this.height = height;
    world.rooms.add(this);

    world_map_x = (int)(Math.random()*100);
    world_map_y = (int)(Math.random()*100);
  }

  public Room (JSON json, World world) {
    super(json);
    world.add_child(this);
    this.width = json.getInt("width");
    this.height = json.getInt("height");
    world.rooms.add(this);

    world_map_x = (int)(Math.random()*100);
    world_map_y = (int)(Math.random()*100);
  }

  @Override
  public JSON save_file_object() {
    JSON json = super.save_file_object();
    json.set("width", this.width);
    json.set("height", this.height);
    json.set("visits", this.visits_json());
    json.set("x", this.x);
    json.set("y", this.y);
    return json;
  }

  public JSON visits_json() {
    JSON json = new JSON();
    for (Entry<String, Integer> entry : visits.entrySet()) {
      json.set(entry.getKey(), entry.getValue());
    }
    return json;
  }

  @Override
  public void destroy() {
    super.destroy();
    ((World) this.parent).rooms.remove(this);
  }

  @Override
  public void draw_on_parent() {
    TTDGE.papplet.pushStyle();
    if (this.highlight) {
      TTDGE.papplet.stroke(255, 0, 0);
    }
    TTDGE.papplet.rect(
          TTDGE.x_map_offset + world_map_x*TTDGE.map_grid_size, TTDGE.y_map_offset + world_map_y*TTDGE.map_grid_size,
          this.width*TTDGE.map_grid_size, this.height*TTDGE.map_grid_size);

    Iterator<TTDGEObject> itr = this.children.iterator();
    while (itr.hasNext()) {
      itr.next().draw_on_parent();
    }
    TTDGE.papplet.popStyle();
  }

  @Override
  public void draw() {
    TTDGE.papplet.pushStyle();
    TTDGE.papplet.rect(TTDGE.x_offset, TTDGE.y_offset, width*TTDGE.room_grid_size, height*TTDGE.room_grid_size);
    TTDGE.papplet.popStyle();

    Iterator<TTDGEObject> itr = this.children.iterator();
    while (itr.hasNext()) {
      itr.next().draw_on_parent();
    }
  }

  public boolean allowed_position(GameCharacter charater, int x, int y) {
    Iterator<TTDGEObject> itr = this.children.iterator();
    while (itr.hasNext()) {
      TTDGEObject object = itr.next();
      if (object.collide(charater)) {
        if (PApplet.dist(this.x, this.y, charater.x, charater.y) < this.radius + charater.radius) {
          return false;
        }
      }
    }
    return true;
  }


  public void set_thing(Thing thing, int x, int y) {
    this.add_child(thing);
    thing.x = x;
    thing.y = y;
  }

  public Thing pointed_thing() {
    Thing thing = null;
    Iterator<TTDGEObject> itr = this.children.iterator();
    while (itr.hasNext()) {
      thing = (Thing) itr.next();
      if (thing.pointed()) {
        return thing;
      }
    }
    if (this.pointed()) {
      return this;
    }
    return null;
  }

  @Override
  public String type_name() {
    return "Room";
  }

  @Override
  public String default_name() {
    return "Room";
  }

  @Override
  public String default_description() {
    return "";
  }


}
