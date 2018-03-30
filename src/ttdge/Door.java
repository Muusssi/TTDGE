package ttdge;


public class Door extends Thing {

  public Door linked_door;
  public boolean closed = false;

  public Door(World world, String id, String name, String description) {
    super(world, id, name, description);
  }

  @Override
  public JSON world_file_object() {
    JSON json = this.base_world_file_object();
    json.set("room_x", this.room_x);
    json.set("room_y", this.room_y);
    json.set("linked_door", this.linked_door);
    json.set("closed", this.closed);
    return json;
  }

  public static Door create(World world, JSON json) {
    Door new_door = new Door(world, json.getString("id"), json.getString("name"), json.getString("description"));
    new_door.closed = json.getBoolean("closed");
    new_door.json = json;
    return new_door;
  }

  @Override
  public void linking_actions() {
    this.room = world.get_room(this.json.getString("room"));
    int room_x = json.getInt("room_x");
    int room_y = json.getInt("room_y");
    if (room_x > 0 || room_y > 0) {
      room.set_thing(this, room_x, room_y);
    }
    String door_id = json.getString("linked_door");
    if (door_id != null) {
      Door other_door = (Door)world.things.get(door_id);
      this.link_to(other_door);
    }
    json = null;
  }

  @Override
  public void destroy() {
    remove_from_room();
    this.unlink();
    world.things.remove(this.id);
    this.world = null;
  }

  public void link_to(Door other_door) {
    this.linked_door = other_door;
    other_door.linked_door = this;
  }

  public void unlink() {
    if (this.linked_door != null) {
      this.linked_door.linked_door = null;
      this.linked_door = null;
    }
  }

  @Override
  public void go(GameCharacter game_character) {
    if (this.linked_door != null) {
      if (this.closed) {
        TTDGE.message("This door closed!");
      }
      else {
        game_character.set_to(this.linked_door.room, this.linked_door.room_x, this.linked_door.room_y);
        if (TTDGE.player == game_character) {
          TTDGE.active_room = this.linked_door.room;
        }
        this.linked_door.room.visited = true;
      }
    }
    else {
      TTDGE.message("This useless passage doesn't go anywhere.");
    }
  }

  @Override
  public void open(GameCharacter game_character) {
    this.closed = false;
    if (this.linked_door != null) {
      this.linked_door.closed = false;
    }
  }

  @Override
  public void close(GameCharacter game_character) {
    this.closed = true;
    if (this.linked_door != null) {
      this.linked_door.closed = false;
    }
  }

  @Override
  public String default_name() {
    return "Door";
  }

  @Override
  public String type_name() {
    return "Door";
  }



  @Override
  public void draw() {
    TTDGE.papplet.pushStyle();
    TTDGE.papplet.fill(102, 51, 0);
    if (this.highlight) {
      TTDGE.papplet.stroke(255, 0, 0);
      TTDGE.papplet.strokeWeight(3);
    }
    TTDGE.papplet.rect(TTDGE.x_offset + room_x*TTDGE.room_grid_size, TTDGE.y_offset + room_y*TTDGE.room_grid_size, TTDGE.room_grid_size, TTDGE.room_grid_size);
    if (TTDGE.debug_mode) {
      TTDGE.papplet.stroke(0, 255, 0);
      TTDGE.papplet.fill(255, 0, 0);
      TTDGE.papplet.text(this.id, TTDGE.x_offset + room_x*TTDGE.room_grid_size, TTDGE.y_offset + room_y*TTDGE.room_grid_size);
    }
    TTDGE.papplet.popStyle();
  }

  @Override
  public void draw_on_map() {
    TTDGE.papplet.pushStyle();
    TTDGE.papplet.stroke(0);
    TTDGE.papplet.fill(102, 51, 0);
    TTDGE.papplet.rect(
        TTDGE.x_map_offset + room.world_map_x*TTDGE.map_grid_size + room_x*TTDGE.map_grid_size,
        TTDGE.y_map_offset + room.world_map_y*TTDGE.map_grid_size + room_y*TTDGE.map_grid_size,
        TTDGE.map_grid_size, TTDGE.map_grid_size);
    if (this.linked_door != null) {
      TTDGE.papplet.pushStyle();
      TTDGE.papplet.line(
          TTDGE.x_map_offset + room.world_map_x*TTDGE.map_grid_size + room_x*TTDGE.map_grid_size,
          TTDGE.y_map_offset + room.world_map_y*TTDGE.map_grid_size + room_y*TTDGE.map_grid_size,
          TTDGE.x_map_offset + linked_door.room.world_map_x*TTDGE.map_grid_size + linked_door.room_x*TTDGE.map_grid_size,
          TTDGE.y_map_offset + linked_door.room.world_map_y*TTDGE.map_grid_size + linked_door.room_y*TTDGE.map_grid_size);
      TTDGE.papplet.popStyle();
    }
    TTDGE.papplet.popStyle();
  }

  @Override
  public String default_description() {
    // TODO Auto-generated method stub
    return null;
  }



}
