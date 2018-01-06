import java.util.PriorityQueue;
import java.util.HashSet;

final int GRID_SIZE = 50;

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


public class World {

  ArrayList<Room> rooms = new ArrayList<Room>();
  Room starting_room = null;


  public World() {
    if (default_world == null) {
      default_world = this;
    }
  }

}

void draw_acticve_room() {
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

public class Room {

  Thing[][] grid;

  int room_width;
  int room_height;

  public Room (int room_width, int room_height) {
    grid = new Thing[room_width][room_height];
    this.room_width = room_width;
    this.room_height = room_height;
    default_world.rooms.add(this);
  }

  void draw() {
    pushStyle();
    //noFill();
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

}


public class Thing {

  Room room;
  int room_x, room_y;
  String name = "Thing";

  public Thing () {

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

  public GameCharacter () {
    super();
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

  /**
   * Updates the GameCharaters path using the A* algorithm
   */
  void update_path() {
    PriorityQueue<WayPoint> open = new PriorityQueue<WayPoint>();
    HashSet<String> closed = new HashSet<String>();

    WayPoint wp = new WayPoint("", this.x, this.y, this.room_target_x, this.room_target_y, this.speed);
    open.add(wp);
    String wp_key = this.x + "_" + this.y;
    closed.add(wp_key);

    while (open.size() > 0) {
      wp = open.poll();
      if (wp.estimate == 0) {
        this.path = wp.path_here;
        return;
      }

      if (this.can_move_up(wp.x, wp.y)) {
        wp_key = wp.x + "_" + (wp.y - this.speed);
        if (!closed.contains(wp_key)) {
          closed.add(wp_key);
          open.add(new WayPoint("w" + wp.path_here, wp.x, wp.y - this.speed, this.room_target_x, this.room_target_y, this.speed));
        }

        if (this.can_move_left(wp.x, wp.y)) {
          wp_key = (wp.x - this.speed) + "_" + (wp.y - this.speed);
          if (!closed.contains(wp_key)) {
            closed.add(wp_key);
            open.add(new WayPoint("q" + wp.path_here, wp.x - this.speed, wp.y - this.speed, this.room_target_x, this.room_target_y, this.speed));
          }
        }
        if (this.can_move_right(wp.x, wp.y)) {
          wp_key = (wp.x + this.speed) + "_" + (wp.y - this.speed);
          if (!closed.contains(wp_key)) {
            closed.add(wp_key);
            open.add(new WayPoint("e" + wp.path_here, wp.x + this.speed, wp.y - this.speed, this.room_target_x, this.room_target_y, this.speed));
          }
        }
      }

      if (this.can_move_left(wp.x, wp.y)) {
        wp_key = (wp.x - this.speed) + "_" + wp.y;
        if (!closed.contains(wp_key)) {
          closed.add(wp_key);
          open.add(new WayPoint("a" + wp.path_here, wp.x - this.speed, wp.y, this.room_target_x, this.room_target_y, this.speed));
        }
      }

      if (this.can_move_down(wp.x, wp.y)) {
        wp_key = wp.x + "_" + (wp.y + this.speed);
        if (!closed.contains(wp_key)) {
          closed.add(wp_key);
          open.add(new WayPoint("s" + wp.path_here, wp.x, wp.y + this.speed, this.room_target_x, this.room_target_y, this.speed));
        }

        if (this.can_move_left(wp.x, wp.y)) {
          wp_key = (wp.x - this.speed) + "_" + (wp.y + this.speed);
          if (!closed.contains(wp_key)) {
            closed.add(wp_key);
            open.add(new WayPoint("z" + wp.path_here, wp.x - this.speed, wp.y + this.speed, this.room_target_x, this.room_target_y, this.speed));
          }
        }
        if (this.can_move_right(wp.x, wp.y)) {
          wp_key = (wp.x + this.speed) + "_" + (wp.y + this.speed);
          if (!closed.contains(wp_key)) {
            closed.add(wp_key);
            open.add(new WayPoint("x" + wp.path_here, wp.x + this.speed, wp.y + this.speed, this.room_target_x, this.room_target_y, this.speed));
          }
        }
      }

      if (this.can_move_right(wp.x, wp.y)) {
        wp_key = (wp.x + this.speed) + "_" + wp.y;
        if (!closed.contains(wp_key)) {
          closed.add(wp_key);
          open.add(new WayPoint("d" + wp.path_here, wp.x + this.speed, wp.y, this.room_target_x, this.room_target_y, this.speed));
        }
      }

    }
  }

  void drop(Item item) {
    // TODO
    if (this.room != null) {

    }
    else {
      error("Can not drop item '"+item.name+"'!");
    }
  }

}


public class Obstacle extends Thing {

  String name = "Obstacle";

  public Obstacle () {
    super();
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

}


public class Door extends Thing {

  String name = "Door";

  public Door () {
    super();
  }

}

public class Item extends Thing {

  String name = "Door";
  Room room;
  int x = 0;
  int y = 0;

  public Item () {
    super();
  }

  void put(Room room, int x, int y) {
    if (room.get(x, y) == null) {
      room.grid[x][y] = this;
      this.room = room;
      this.x = x;
      this.y = y;
    }
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

class WayPoint implements Comparable<WayPoint>  {

  String path_here = "";
  int x, y, estimate;

  public WayPoint(String path_here, int x, int y, int target_x, int target_y, int speed) {
    this.path_here = path_here;
    this.x = x;
    this.y = y;
    this.estimate = (abs(x - target_x) + abs(y - target_y));
  }


  //@Override
  public int compareTo(WayPoint other) {
    return Integer.compare(
        this.path_here.length() + this.estimate,
        other.path_here.length() + other.estimate
      );
  }
}


