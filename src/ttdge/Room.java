package ttdge;

public class Room extends Thing {

  Thing[][] grid;

  int room_width;
  int room_height;


  public Room (World world, String id, int room_width, int room_height) {
    super(world, id, "Room");
    grid = new Thing[room_width][room_height];
    this.room_width = room_width;
    this.room_height = room_height;
    world.rooms.add(this);
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

  public boolean obstacle(int room_x, int room_y) {
    Thing thing = this.get(room_x, room_y);
    if (thing != null && thing.collide()) {
      return true;
    }
    else {
      return false;
    }
  }

  public void set_obstacle(Obstacle obstacle, int room_x, int room_y) {
    if (grid[room_x][room_y] == null) {
      grid[room_x][room_y] = obstacle;
      obstacle.room_x = room_x;
      obstacle.room_y = room_y;
    }
    else {
      TTDGE.error("Can not set obstacle, slot taken.");
    }
  }

  @Override
  public String id_prefix() {
    return "Room";
  }

  @Override
  public String world_file_string() {
    String[] tokens = new String[5];
    tokens[0] = "Room";
    tokens[1] = this.id;
    tokens[2] = this.name;
    tokens[3] = Integer.toString(this.room_width);
    tokens[4] = Integer.toString(this.room_height);
    return String.join(TTDGE.WORLD_FILE_DELIMITER, tokens);
  }

  @Override
  public String default_name() {
    return "Room";
  }


}