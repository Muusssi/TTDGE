/* The engine for TTDGE (Tommi's Top-Down Game Engine)
 */

final String ENGINE_VERSION = "0.1";
final String WORLD_FILE_DELIMITER = "||";


import java.util.PriorityQueue;
import java.util.HashSet;

final int GRID_SIZE = 50;
final boolean SHOW_SEARCHED_POINTS = false;

int x_offset = 50;
int y_offset = 50;

GameCharacter player = null;

World default_world = null;

void fatal_error(String message) {
  println("Fatal error: " + message);
  exit();
}

void error(String message) {
  println("Error: " + message);
}


World load_world(String world_file) {
  String[] lines = loadStrings(world_file);
  World new_world = null;
  for (String line : lines) {
    if (!line.substring(0, 1).equals("#")) {
      String[] tokens = split(line, WORLD_FILE_DELIMITER);
      String thing_type = tokens[0];
      if (thing_type.equals("World")) {
        new_world = create_world(tokens);
        new_world.world_file = world_file;
      }
      else {
        if (new_world == null) {
          fatal_error("Corrupted world file '"+world_file+"'. World not first thing.");
          return null;
        }
        new_world.load_thing(tokens);
      }
    }

  }
  return new_world;
}


World create_world(String[] values) {
  if (values.length != 2) {
    fatal_error("Failed to load world. Corrupted world file.");
    return null;
  }
  return new World(values[1]);
}

HashMap<String,World> worlds = new HashMap<String,World>();

public class World {

  String name;
  String world_file = null;

  int id_counter = 1;

  HashMap<String,Thing> things = new HashMap<String,Thing>();

  ArrayList<Room> rooms = new ArrayList<Room>();
  Room starting_room = null;


  public World(String name) {
    this.name = name;
    worlds.put(name, this);
  }

  String world_file_string() {
    return "World"+WORLD_FILE_DELIMITER+name;
  }

  Thing load_thing(String[] tokens) {
    String thing_type = tokens[0];
    if (thing_type.equals("Room")) {
      create_room(tokens);
    }
    else if (thing_type.equals("Door")) {
      create_door(tokens);
    }
    else if (thing_type.equals("Obstacle")) {
      create_obstacle(tokens);
    }
    else if (thing_type.equals("GameCharacter")) {
      create_character(tokens);
    }
    else if (thing_type.equals("Item")) {
      create_item(tokens);
    }
    else {
      fatal_error("World file corrupted: '"+world_file+"'. Unsupported thing: '"+thing_type+"'");
    }
    return null;
  }

  int id_counter_next() {
    return id_counter++;
  }
}



public abstract class Thing {

  String id;
  World world;
  String name = "Thing";

  Room room;
  int room_x, room_y;

  public Thing (World world) {
    this.id = id();
    this.world = world;
    world.things.put(this.id, this);
  }

  void investigate() {}

  void hit() {}

  void eat() {}

  void open() {}

  void close() {}

  void take() {}

  void operate() {}

  boolean collide() {
    return false;
  }

  void draw() {
    error("Draw for a thing is not implemented.");
  }

  String id() {
    return id_prefix() + "-" + world.id_counter_next();
  }

  abstract String id_prefix();

  abstract String world_file_string();



}


void draw_active_room() {
  if (player != null && player.room != null) {
    player.room.draw();
  }
  else if (default_world != null && default_world.rooms.size() > 0) {
    default_world.rooms.get(0).draw();
  }
  else {
    fatal_error("Unable to draw room: active room unknown!");
  }
}

Room create_room(String[] tokens) {
  // TODO
  return null;
}

public class Room extends Thing {

  Thing[][] grid;

  int room_width;
  int room_height;

  public Room (World world, int room_width, int room_height) {
    super(world);
    grid = new Thing[room_width][room_height];
    this.room_width = room_width;
    this.room_height = room_height;
    world.rooms.add(this);
  }

  void draw() {
    pushStyle();
    if (SHOW_SEARCHED_POINTS) {
      noFill();
    }
    rect(x_offset, y_offset, room_width*GRID_SIZE, room_height*GRID_SIZE);
    popStyle();
    for (int i = 0; i < room_width; ++i) {
      for (int j = 0; j < room_height; ++j) {
        if (grid[i][j] != null) {
          grid[i][j].draw();
        }
      }
    }
  }

  Thing get(int x, int y) {
    if (x >= 0 && x < room_width && y >= 0 && y < room_height) {
      return grid[x][y];
    }
    return null;
  }

  boolean obstacle(int room_x, int room_y) {
    Thing thing = this.get(room_x, room_y);
    if (thing != null && thing.collide()) {
      return true;
    }
    else {
      return false;
    }
  }

  void set_obstacle(Obstacle obstacle, int room_x, int room_y) {
    if (grid[room_x][room_y] == null) {
      grid[room_x][room_y] = obstacle;
      obstacle.room_x = room_x;
      obstacle.room_y = room_y;
    }
    else {
      error("Can not set obstacle, slot taken.");
    }
  }

  String id_prefix() {
    return "d";
  }

  String world_file_string() {
    // TODO
    return "";
  }

}


GameCharacter create_character(String[] tokens) {
  // TODO
  return null;
}

public class GameCharacter extends Thing {

  String name = "GameCharacter";
  int x = 0;
  int y = 0;

  int speed = 2;

  int radius = GRID_SIZE/3;

  ArrayList<Item> items = new ArrayList<Item>();

  // For path finding
  int room_target_x = -1;
  int room_target_y = -1;
  String path = null;

  public GameCharacter(World world) {
    super(world);
  }

  void draw() {
    ellipse(x_offset + x, y_offset + y, this.radius*2, this.radius*2);
  }

  void move(char direction) {
    switch (direction) {
      case 'w':
        move_up();
        break;
      case 'a':
        move_left();
        break;
      case 's':
        move_down();
        break;
      case 'd':
        move_right();
        break;
      case 'q':
        move_up();
        move_left();
        break;
      case 'e':
        move_up();
        move_right();
        break;
      case 'z':
        move_down();
        move_left();
        break;
      case 'x':
        move_down();
        move_right();
        break;
      default:
        error("Unsupported move direction '"+direction+"'");
        break;
    }
  }

  void move_up() {
    if (this.can_move_up()) {
      this.y -= this.speed;
    }
  }

  void move_down() {
    if (this.can_move_down()) {
      this.y += this.speed;
    }
  }

  void move_left() {
    if (this.can_move_left()) {
      this.x -= this.speed;
    }
  }

  void move_right() {
    if (this.can_move_right()) {
      this.x += this.speed;
    }
  }

  boolean can_move_up() {
    return can_move_up(this.x, this.y);
  }

  boolean can_move_up(int x, int y) {
    int new_y = y - this.speed - this.radius;
    if (new_y < 0) {
      return false;
    }
    else if (this.room.obstacle(x/GRID_SIZE, new_y/GRID_SIZE)) {
      return false;
    }
    return true;
  }

  boolean can_move_down() {
    return can_move_down(this.x, this.y);
  }

  boolean can_move_down(int x, int y) {
    int new_y = y + this.speed + this.radius;
    if (new_y > this.room.room_height*GRID_SIZE) {
      return false;
    }
    else if (this.room.obstacle(x/GRID_SIZE, new_y/GRID_SIZE)) {
      return false;
    }
    return true;
  }

  boolean can_move_left() {
    return can_move_left(this.x, this.y);
  }

  boolean can_move_left(int x, int y) {
    int new_x = x - this.speed - this.radius;
    if (new_x < 0) {
      return false;
    }
    else if (this.room.obstacle(new_x/GRID_SIZE, y/GRID_SIZE)) {
      return false;
    }
    return true;
  }

  boolean can_move_right() {
    return can_move_right(this.x, this.y);
  }

  boolean can_move_right(int x, int y) {
    int new_x = x + this.speed + this.radius;
    if (new_x > this.room.room_width*GRID_SIZE) {
      return false;
    }
    else if (this.room.obstacle(new_x/GRID_SIZE, y/GRID_SIZE)) {
      return false;
    }
    return true;
  }

  boolean touches_obstacle() {

    return false;
  }

  void move_to_target() {
    if (path != null && path.length() > 0) {
      this.move(path.charAt(path.length() - 1));
      path = path.substring(0, path.length() - 1);
    }
    else {
      path = null;
    }
  }

  void new_target(int x, int y) {
    this.room_target_x = (x/this.speed)*this.speed;
    this.room_target_y = (y/this.speed)*this.speed;
    update_path();
  }

  void follow(GameCharacter other) {
    // TODO
  }

  /**
   * Updates the GameCharacters path using the A* algorithm
   */
  void update_path() {
    PriorityQueue<WayPoint> open = new PriorityQueue<WayPoint>();
    HashSet<String> closed = new HashSet<String>();

    WayPoint wp = new WayPoint("", this.x, this.y, this.room_target_x,
                                this.room_target_y, this.speed);
    open.add(wp);
    String wp_key = this.x + "_" + this.y;
    closed.add(wp_key);

    while (open.size() > 0) {
      wp = open.poll();
      if (SHOW_SEARCHED_POINTS) {
        pushStyle();
        fill(255, 0, 0);
        noStroke();
        ellipse(wp.x + x_offset, wp.y + y_offset, 5, 5);
        popStyle();
      }
      if (wp.estimate == 0) {
        this.path = wp.path_here;
        return;
      }

      if (this.can_move_up(wp.x, wp.y)) {
        wp_key = wp.x + "_" + (wp.y - this.speed);
        if (!closed.contains(wp_key)) {
          closed.add(wp_key);
          open.add(new WayPoint("w" + wp.path_here, wp.x, wp.y - this.speed,
                  this.room_target_x, this.room_target_y, this.speed));
        }

        if (this.can_move_left(wp.x, wp.y)) {
          wp_key = (wp.x - this.speed) + "_" + (wp.y - this.speed);
          if (!closed.contains(wp_key)) {
            closed.add(wp_key);
            open.add(new WayPoint("q" + wp.path_here, wp.x - this.speed, wp.y - this.speed,
                    this.room_target_x, this.room_target_y, this.speed));
          }
        }
        if (this.can_move_right(wp.x, wp.y)) {
          wp_key = (wp.x + this.speed) + "_" + (wp.y - this.speed);
          if (!closed.contains(wp_key)) {
            closed.add(wp_key);
            open.add(new WayPoint("e" + wp.path_here, wp.x + this.speed, wp.y - this.speed,
                                  this.room_target_x, this.room_target_y, this.speed));
          }
        }
      }

      if (this.can_move_left(wp.x, wp.y)) {
        wp_key = (wp.x - this.speed) + "_" + wp.y;
        if (!closed.contains(wp_key)) {
          closed.add(wp_key);
          open.add(new WayPoint("a" + wp.path_here, wp.x - this.speed, wp.y,
                                this.room_target_x, this.room_target_y, this.speed));
        }
      }

      if (this.can_move_down(wp.x, wp.y)) {
        wp_key = wp.x + "_" + (wp.y + this.speed);
        if (!closed.contains(wp_key)) {
          closed.add(wp_key);
          open.add(new WayPoint("s" + wp.path_here, wp.x, wp.y + this.speed,
                                this.room_target_x, this.room_target_y, this.speed));
        }

        if (this.can_move_left(wp.x, wp.y)) {
          wp_key = (wp.x - this.speed) + "_" + (wp.y + this.speed);
          if (!closed.contains(wp_key)) {
            closed.add(wp_key);
            open.add(new WayPoint("z" + wp.path_here, wp.x - this.speed, wp.y + this.speed,
                                  this.room_target_x, this.room_target_y, this.speed));
          }
        }
        if (this.can_move_right(wp.x, wp.y)) {
          wp_key = (wp.x + this.speed) + "_" + (wp.y + this.speed);
          if (!closed.contains(wp_key)) {
            closed.add(wp_key);
            open.add(new WayPoint("x" + wp.path_here, wp.x + this.speed, wp.y + this.speed,
                                  this.room_target_x, this.room_target_y, this.speed));
          }
        }
      }

      if (this.can_move_right(wp.x, wp.y)) {
        wp_key = (wp.x + this.speed) + "_" + wp.y;
        if (!closed.contains(wp_key)) {
          closed.add(wp_key);
          open.add(new WayPoint("d" + wp.path_here, wp.x + this.speed, wp.y,
                                this.room_target_x, this.room_target_y, this.speed));
        }
      }
    }
  }

  String id_prefix() {
    return "GameCharacter";
  }

  String world_file_string() {
    // TODO
    return "";
  }

}


Obstacle create_obstacle(String[] tokens) {
  // TODO
  return null;
}

public class Obstacle extends Thing {

  String name = "Obstacle";

  public Obstacle(World world) {
    super(world);
  }

  void draw() {
    pushStyle();
    fill(0);
    rect(x_offset + room_x*GRID_SIZE, y_offset + room_y*GRID_SIZE, GRID_SIZE, GRID_SIZE);
    popStyle();
  }

  boolean collide() {
    return true;
  }

  String id_prefix() {
    return "Obstacle";
  }

  String world_file_string() {
    // TODO
    return "";
  }

}


Door create_door(String[] tokens) {
  // TODO
  return null;
}

public class Door extends Thing {

  String name = "Door";

  public Door(World world) {
    super(world);
  }

  String id_prefix() {
    return "Door";
  }

  String world_file_string() {
    // TODO
    return "";
  }

}


Item create_item(String[] tokens) {
  // TODO
  return null;
}

public class Item extends Thing {

  String name = "Door";
  Room room;
  int x = 0;
  int y = 0;

  public Item(World world) {
    super(world);
  }

  void put(Room room, int x, int y) {
    if (room.get(x, y) == null) {
      room.grid[x][y] = this;
      this.room = room;
      this.x = x;
      this.y = y;
    }
  }

  String id_prefix() {
    return "Item";
  }

  String world_file_string() {
    // TODO
    return "";
  }



}




ArrayList<Quest> quests = new ArrayList<Quest>();

public class Quest {

  ArrayList<Quest> missions = new ArrayList<Quest>();

  public Quest () {

  }

}

public class Mission {

  ArrayList<Task> tasks = new ArrayList<Task>();

  public Mission () {

  }

}

public class Task {

  public Task () {

  }

}





/*

Various utilities

*/

/**
 * Used for GameCharacter path finding
 */
class WayPoint implements Comparable<WayPoint>  {

  String path_here = "";
  int x, y, estimate;

  public WayPoint(String path_here, int x, int y, int target_x, int target_y, int speed) {
    this.path_here = path_here;
    this.x = x;
    this.y = y;
    this.estimate = (abs(x - target_x) + abs(y - target_y));
  }

  public int compareTo(WayPoint other) {
    return Integer.compare(
        this.path_here.length() + this.estimate,
        other.path_here.length() + other.estimate
      );
  }
}


