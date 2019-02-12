package ttdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Room extends Thing {

  World world;

  public ArrayList<Thing> things = new ArrayList<Thing>();

  // TODO: register character visits
  public HashMap<String,Integer> visits = new HashMap<String,Integer>();

  public Room (World world, String id, String name, String description, int width, int height) {
    super(id, name, description);
    this.world = world;
    this.width = width;
    this.height = height;
    world.rooms.add(this);
  }

  public Room (JSON json, World world) {
    super(json);
    this.world = world;
    this.width = json.getInt("width");
    this.height = json.getInt("height");
    world.rooms.add(this);
  }

  @Override
  public JSON save_file_object() {
    JSON json = super.save_file_object();
    json.set("width", this.width);
    json.set("height", this.height);
    json.set("visits", this.visits_json());
    json.set("things", things_json());
    return json;
  }

  public JSONarray things_json() {
    JSONarray things_json = new JSONarray();
    for (Thing thing : things) {
      things_json.append(thing.save_file_object());
    }
    return things_json;
  }

  public void load_visits() {
    // TODO: load_visits()
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
    world.rooms.remove(this);
    for (int i = this.things.size() - 1; i >= 0; i--) {
      this.things.get(i).destroy();
    }
  }

  @Override
  public void draw_on_parent() {
    TTDGE.papplet.pushStyle();
    if (this.highlight) {
      TTDGE.papplet.stroke(255, 0, 0);
    }
    TTDGE.papplet.rect(
          TTDGE.x_map_offset + x, TTDGE.y_map_offset + y, this.width/TTDGE.map_grid_size, this.height/TTDGE.map_grid_size);

    TTDGE.papplet.popStyle();
    for (Thing thing : things) {
      thing.draw_on_parent();
    }
  }

  @Override
  public void draw() {
    TTDGE.papplet.pushStyle();
    TTDGE.papplet.rect(TTDGE.x_offset, TTDGE.y_offset, width, height);
    if (TTDGE.debug_mode) {
      TTDGE.papplet.fill(255, 0, 0);
      TTDGE.papplet.stroke(0, 255, 0);
      TTDGE.papplet.text(this.id + ": " + this.name, TTDGE.x_offset, TTDGE.y_offset);
    }
    TTDGE.papplet.popStyle();
    for (Thing thing : things) {
      thing.draw();
    }
  }

  public boolean allowed_position(GameCharacter charater, int x, int y) {
    if (x < charater.radius) {
      return false;
    }
    if (x > this.width - charater.radius) {
      return false;
    }
    if (y < charater.radius) {
      return false;
    }
    if (y > this.height - charater.radius) {
      return false;
    }
    for (Thing thing : this.things) {
      if (thing != charater) {
        if (thing.collide_in_position(charater, x, y)) {
          return false;
        }
      }
    }
    return true;
  }

  public void add_thing(Thing thing) {
    // TODO: K-d tree or something
    this.things.add(thing);
  }

  public void remove_thing(Thing thing) {
    this.things.remove(thing);
  }


  @Override
  public Thing pointed_thing() {
    for (Thing thing : things) {
      if (thing.is_pointed()) {
        return thing;
      }
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

  @Override
  public boolean is_pointed() {
    if (TTDGE.papplet.mouseX > TTDGE.x_offset && TTDGE.papplet.mouseY > TTDGE.y_offset &&
        TTDGE.papplet.mouseX < TTDGE.x_offset + width && TTDGE.papplet.mouseY < TTDGE.y_offset + height) {
      return true;
    }
    return false;
  }

  @Override
  public boolean is_pointed_on_parent() {
    if (TTDGE.papplet.mouseX > TTDGE.x_map_offset + x &&
        TTDGE.papplet.mouseY > TTDGE.y_map_offset + y &&
        TTDGE.papplet.mouseX < TTDGE.x_map_offset + x + width/TTDGE.map_grid_size &&
        TTDGE.papplet.mouseY < TTDGE.y_map_offset + y + height/TTDGE.map_grid_size) {
      return true;
    }
    return false;
  }


}
