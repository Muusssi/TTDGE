package ttdge;

import processing.data.JSONObject;

public class Room extends Thing {

  public Thing[][] grid;

  public int room_width;
  public int room_height;

  public boolean visited = false;


  public Room (World world, String id, String name, String description, int room_width, int room_height) {
    super(world, id, name, description);
    grid = new Thing[room_width][room_height];
    this.room_width = room_width;
    this.room_height = room_height;
    world.rooms.add(this);
  }

  public static Room create(World world, JSONObject json) {
    String id = json.getString("id");
    String name = json.getString("name");
    String description = json.getString("description");
    Room new_room = new Room(world, id, name, description, json.getInt("room_width"), json.getInt("room_height"));
    new_room.visited = json.getBoolean("visited");
    new_room.json = json;
    return new_room;
  }

  @Override
  public JSONObject world_file_object() {
    JSONObject json = this.base_world_file_object();
    json.setInt("room_width", this.room_width);
    json.setInt("room_height", this.room_height);
    json.setBoolean("visited", this.visited);
    return json;
  }

  @Override
  public void draw() {
    TTDGE.papplet.pushStyle();
    if (TTDGE.show_searched_points) {
      TTDGE.papplet.noFill();
    }
    TTDGE.papplet.rect(TTDGE.x_offset, TTDGE.y_offset, room_width*TTDGE.room_grid_size, room_height*TTDGE.room_grid_size);
    TTDGE.papplet.popStyle();
    for (int i = 0; i < room_width; ++i) {
      for (int j = 0; j < room_height; ++j) {
        if (grid[i][j] != null) {
          grid[i][j].draw();
        }
      }
    }
  }

  public Thing get(int x, int y) {
    if (x >= 0 && x < room_width && y >= 0 && y < room_height) {
      return grid[x][y];
    }
    return null;
  }

  public boolean obstacle(GameCharacter game_character, int room_x, int room_y) {
    Thing thing = this.get(room_x, room_y);
    if (thing != null && thing.collide(game_character)) {
      return true;
    }
    else {
      return false;
    }
  }


  public void set_thing(Thing thing, int room_x, int room_y) {
    if (grid[room_x][room_y] == null) {
      grid[room_x][room_y] = thing;
      thing.room = this;
      thing.room_x = room_x;
      thing.room_y = room_y;
    }
    else {
      TTDGE.error("Can not set obstacle, slot taken.");
    }
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
  public void linking_actions() {
    this.json = null;
  }

  @Override
  public String default_description() {
    return "";
  }


}
