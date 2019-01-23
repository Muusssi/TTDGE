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
    JSON json = super.world_file_object();
    json.set("room_width", this.room_width);
    json.set("room_height", this.room_height);
    json.set("visited", this.visited);
    json.set("world_map_x", this.world_map_x);
    json.set("world_map_y", this.world_map_y);
    return json;
  }

  @Override
  public void destroy() {
    world.things.remove(this.id);
    world.rooms.remove(this);
    for (int i=0; i < this.room_width; i++) {
      for (int j=0; j < this.room_height; j++) {
        this.grid[i][j].destroy();
      }
    }
    this.world = null;
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
   Iterator<GameCharacter> itr = world.game_characters.iterator();
   while (itr.hasNext()) {
     GameCharacter character = itr.next();
     if (character.room == TTDGE.active_room) {
       character.draw();
     }
   }
  }

  @Override
  public void draw_on_map() {
    TTDGE.papplet.pushStyle();
    if (this.highlight) {
      TTDGE.papplet.stroke(255, 0, 0);
    }
    TTDGE.papplet.rect(
          TTDGE.x_map_offset + world_map_x*TTDGE.map_grid_size, TTDGE.y_map_offset + world_map_y*TTDGE.map_grid_size,
          this.room_width*TTDGE.map_grid_size, this.room_height*TTDGE.map_grid_size
        );

    for (int i = 0; i < room_width; ++i) {
      for (int j = 0; j < room_height; ++j) {
        Thing thing = this.grid[i][j];
        if (thing != null) {
          thing.draw_on_map();
        }
      }
    }

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
      if (thing.room != null) {
        thing.room.grid[thing.room_x][thing.room_y] = null;
      }
      grid[room_x][room_y] = thing;
      thing.room = this;
      thing.room_x = room_x;
      thing.room_y = room_y;
    }
    else {
      TTDGE.error("Can not set obstacle, slot taken.");
    }
  }

  public int pointed_cell_x() {
    int x = TTDGE.papplet.mouseX - TTDGE.x_offset;
    if (x > 0 && x < room_width*TTDGE.room_grid_size) {
      return x/TTDGE.room_grid_size;
    }
    else {
      return -999999;
    }
  }

  public int pointed_cell_y() {
    int y = TTDGE.papplet.mouseY - TTDGE.y_offset;
    if (y > 0 && y < room_height*TTDGE.room_grid_size) {
      return y/TTDGE.room_grid_size;
    }
    else {
      return -999999;
    }
  }

  public Thing pointed_thing() {
    int x = this.pointed_cell_x();
    int y = this.pointed_cell_y();
    if (x + y >= 0) {
      Iterator<GameCharacter> itr = GameCharacter.game_characters.iterator();
      while (itr.hasNext()) {
        GameCharacter character = itr.next();
        int room_coord_x = TTDGE.papplet.mouseX - TTDGE.x_offset;
        int room_coord_y = TTDGE.papplet.mouseY - TTDGE.y_offset;
        if (character.room == this && character.room_coords_point_to(room_coord_x, room_coord_y)) {
          return character;
        }
      }
      Thing thing = this.grid[x][y];
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
