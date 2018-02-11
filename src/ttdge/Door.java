package ttdge;

import processing.data.JSONObject;

public class Door extends Thing {

  public Door linked_door;
  public boolean closed = false;

  public Door(World world, String id, String name, String description) {
    super(world, id, name, description);
  }

  @Override
  public JSONObject world_file_object() {
    JSONObject json = this.base_world_file_object();
    json.setInt("room_x", this.room_x);
    json.setInt("room_y", this.room_y);
    if (this.linked_door != null) {
      json.setString("linked_door", this.linked_door.id);
    }
    else {
      json.setString("linked_door", null);
    }
    json.setBoolean("closed", this.closed);

    return json;
  }

  public static Door create(World world, JSONObject json) {
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

  public void link_to(Door other_door) {
    this.linked_door = other_door;
    other_door.linked_door = this;
  }

  @Override
  public void go(GameCharacter game_character) {
    if (this.linked_door != null) {
      if (this.closed) {
        TTDGE.message("This door closed!");
      }
      else {
        game_character.room = this.linked_door.room;
        game_character.x = this.linked_door.room_x*TTDGE.room_grid_size;
        game_character.y = this.linked_door.room_y*TTDGE.room_grid_size;
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
    TTDGE.papplet.fill(250);
    TTDGE.papplet.rect(TTDGE.x_offset + room_x*TTDGE.room_grid_size, TTDGE.y_offset + room_y*TTDGE.room_grid_size, TTDGE.room_grid_size, TTDGE.room_grid_size);
    TTDGE.papplet.popStyle();
  }

  @Override
  public String default_description() {
    // TODO Auto-generated method stub
    return null;
  }



}
