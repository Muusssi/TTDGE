package ttdge;

import java.util.Iterator;

public class Room extends Thing {

  public Thing[][] grid;

  public int room_width;
  public int room_height;

  public boolean visited = false;

  public int world_map_x = 0;
  public int world_map_y = 0;


  public Room (World world, String id, String name, String description, int room_width, int room_height) {
    super(world, id, name, description);
    grid = new Thing[room_width][room_height];
    this.room_width = room_width;
    this.room_height = room_height;
    world.rooms.add(this);
    world_map_x = (int)(Math.random()*100);
    world_map_y = (int)(Math.random()*100);
  }

  public static Room create(World world, JSON json) {
    String id = json.getString("id");
    String name = json.getString("name");
    String description = json.getString("description");
    Room new_room = new Room(world, id, name, description, json.getInt("room_width"), json.getInt("room_height"));
    new_room.visited = json.getBoolean("visited");
    new_room.world_map_x = json.getInt("world_map_x", (int)(Math.random()*100));
    new_room.world_map_y = json.getInt("world_map_y", (int)(Math.random()*100));
    new_room.json = json;
    return new_room;
  }

  @Override
  public JSON world_file_object() {
    JSON json = this.base_world_file_object();
    json.set("room_width", this.room_width);
    json.set("room_height", this.room_height);
    json.set("visited", this.visited);
    json.set("world_map_x", this.world_map_x);
    json.set("world_map_y", this.world_map_y);
    return json;
  }

  @Override
  public void draw() {
    TTDGE.papplet.pushStyle();
    if (this.highlight) {
      TTDGE.papplet.stroke(255, 0, 0);
      TTDGE.papplet.strokeWeight(3);
    }
    if (TTDGE.show_searched_points) {
      TTDGE.papplet.noFill();
    }
    TTDGE.papplet.rect(TTDGE.x_offset, TTDGE.y_offset, room_width*TTDGE.room_grid_size, room_height*TTDGE.room_grid_size);
    TTDGE.papplet.popStyle();
    for (int i = 0; i < room_width; ++i) {
      for (int j = 0; j < room_height; ++j) {
        if (TTDGE.debug_mode) {
          TTDGE.papplet.pushStyle();
          TTDGE.papplet.noFill();
          TTDGE.papplet.rect(TTDGE.x_offset + i*TTDGE.room_grid_size, TTDGE.y_offset + j*TTDGE.room_grid_size, TTDGE.room_grid_size, TTDGE.room_grid_size);
          TTDGE.papplet.popStyle();
        }
        if (grid[i][j] != null) {
          grid[i][j].draw();
        }
      }
    }
  }

  public void draw_on_map() {
    TTDGE.papplet.pushStyle();
    if (this.highlight) {
      TTDGE.papplet.stroke(255, 0, 0);
    }
    TTDGE.papplet.rect(
          TTDGE.x_map_offset + world_map_x*TTDGE.map_grid_size, TTDGE.y_map_offset + world_map_y*TTDGE.map_grid_size,
          this.room_width*TTDGE.map_grid_size, this.room_height*TTDGE.map_grid_size
        );
    TTDGE.papplet.popStyle();
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

  public Thing pointed_thing() {
    int x = TTDGE.papplet.mouseX - TTDGE.x_offset;
    int y = TTDGE.papplet.mouseY - TTDGE.y_offset;
    if (x > 0 && y > 0 && x < room_width*TTDGE.room_grid_size && y < room_height*TTDGE.room_grid_size) {
      Iterator<GameCharacter> itr = GameCharacter.game_characters.iterator();
      while (itr.hasNext()) {
        GameCharacter character = itr.next();
        if (character.room == this && character.room_coords_point_to(x, y)) {
          return character;
        }
      }
      Thing thing = this.grid[x/TTDGE.room_grid_size][y/TTDGE.room_grid_size];
      if (thing == null) {
        return this;
      }
      return thing;
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
  public void linking_actions() {
    this.json = null;
  }

  @Override
  public String default_description() {
    return "";
  }


}
