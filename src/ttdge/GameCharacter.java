package ttdge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class GameCharacter extends Thing {

  public Room room;
  public char last_direction = 's';
  public char direction = 0;

  public int speed = 3;

  public ArrayList<Item> inventory = new ArrayList<Item>();

  // For path finding
  public String target_room_id = null;
  public int room_target_x = -1;
  public int room_target_y = -1;
  public String path = null;

  public GameCharacter(String id, String name, String description, Room room) {
    super(id, name, description);
    this.room = room;
    this.room.add_thing(this);
    radius = TTDGE.room_grid_size/3;
    if (TTDGE.player == null) {
      TTDGE.player = this;
    }
  }

  @Override
  public JSON save_file_object() {
    JSON json = super.save_file_object();
    json.set("speed", this.speed);
    json.set("last_direction", this.last_direction);
    json.set("player", TTDGE.player == this);
    json.set("path", this.path);
    json.set("room_target_x", this.room_target_x);
    json.set("room_target_y", this.room_target_y);
    json.set("target_room", this.target_room_id);
    return json;
  }

  public GameCharacter(JSON json, Room room) {
    super(json);
    this.room = room;
    this.room.add_thing(this);
    this.speed = json.getInt("speed");
    this.last_direction = (char)json.getInt("last_direction");
    this.path = json.getString("path");
    this.room_target_x = json.getInt("room_target_x");
    this.room_target_y = json.getInt("room_target_y");
    this.target_room_id = json.getString("target_room_id");
    if (json.getBoolean("player")) {
      TTDGE.player = this;
    }
  }

  @Override
  public void destroy() {
    super.destroy();
    if (this.room != null) {
      this.room.remove_thing(this);
    }
  }

  @Override
  public String default_image_file_name() {
    return "/images/TTDGE1.png";
  }

  @Override
  public void draw() {
    if (this.used_image_file_name != null) {
      TTDGE.papplet.pushMatrix();
      TTDGE.papplet.translate(this.x + TTDGE.x_offset, this.y + TTDGE.y_offset);
      if (this.direction != 0) {
        TTDGE.papplet.rotate(this.direction_to_angle(this.direction));
      }
      else {
        TTDGE.papplet.rotate(this.direction_to_angle(this.last_direction));
      }
      PImage image = this.get_image();
      TTDGE.papplet.image(image, -image.width/2, -image.height/2);
      TTDGE.papplet.popMatrix();
    }
    else {
      TTDGE.papplet.ellipse(TTDGE.x_offset + x, TTDGE.y_offset + y, this.radius*2, this.radius*2);
    }
    if (this.highlight) {
      TTDGE.papplet.pushStyle();
      TTDGE.papplet.stroke(255, 0, 0);
      TTDGE.papplet.strokeWeight(3);
      TTDGE.papplet.noFill();
      TTDGE.papplet.ellipse(TTDGE.x_offset + x, TTDGE.y_offset + y, this.radius*4, this.radius*4);
      TTDGE.papplet.popStyle();
    }
    if (this.direction != 0) {
      this.last_direction = this.direction;
    }
    this.direction = 0;
  }

  public void move(char direction) {
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
        TTDGE.error("Unsupported move direction '"+direction+"'");
        break;
    }
  }

  public void set_direction(char direction) {
    if (this.direction != 0) {
      if (this.direction == 'w') {
        if (direction == 'a') {
          this.direction = 'q';
        }
        else if (direction == 'd') {
          this.direction = 'e';
        }
        else {
          this.direction = direction;
        }
      }
      else if (this.direction == 'a') {
        if (direction == 'w') {
          this.direction = 'q';
        }
        else if (direction == 's') {
          this.direction = 'z';
        }
        else {
          this.direction = direction;
        }
      }
      else if (this.direction == 's') {
        if (direction == 'a') {
          this.direction = 'z';
        }
        else if (direction == 'd') {
          this.direction = 'x';
        }
        else {
          this.direction = direction;
        }
      }
      else if (this.direction == 'd') {
        if (direction == 'w') {
          this.direction = 'e';
        }
        else if (direction == 'x') {
          this.direction = 's';
        }
        else {
          this.direction = direction;
        }
      }
      else {
        this.direction = direction;
      }
    }
    else {
      this.direction = direction;
    }
  }

  public void move_up() {
    if (this.can_move_up()) {
      this.y -= this.speed;
      this.set_direction('w');
    }
  }

  public void move_down() {
    if (this.can_move_down()) {
      this.y += this.speed;
      this.set_direction('s');
    }
  }

  public void move_left() {
    if (this.can_move_left()) {
      this.x -= this.speed;
      this.set_direction('a');
    }
  }

  public void move_right() {
    if (this.can_move_right()) {
      this.x += this.speed;
      this.set_direction('d');
    }
  }

  public boolean can_move_up() {
    return can_move_up(this.x, this.y);
  }

  public boolean can_move_up(int x, int y) {
    if (this.room.allowed_position(this, x, y - this.speed)) {
      return true;
    }
    return false;
  }

  public boolean can_move_down() {
    return can_move_down(this.x, this.y);
  }

  public boolean can_move_down(int x, int y) {
    if (this.room.allowed_position(this, x, y + this.speed)) {
      return true;
    }
    return false;
  }

  public boolean can_move_left() {
    return can_move_left(this.x, this.y);
  }

  public boolean can_move_left(int x, int y) {
    if (this.room.allowed_position(this, x - this.speed, y)) {
      return true;
    }
    return false;
  }

  public boolean can_move_right() {
    return can_move_right(this.x, this.y);
  }

  public boolean can_move_right(int x, int y) {
    if (this.room.allowed_position(this, x + this.speed, y)) {
      return true;
    }
    return false;
  }


  public void move_towards_target() {
    if (path != null && path.length() > 0) {
      this.move(path.charAt(path.length() - 1));
      path = path.substring(0, path.length() - 1);
    }
    else {
      path = null;
    }
  }

  public void new_target(int x, int y) {
    this.room_target_x = (x/this.speed)*this.speed;
    this.room_target_y = (y/this.speed)*this.speed;
    update_path();
  }

//  public void follow(GameCharacter other) {
//    // TODO: follow(GameCharacter other)
//  }

  /**
   * Updates the GameCharacters path using the A* algorithm
   */
  public void update_path() {
    if (!this.room.allowed_position(this, room_target_x, room_target_y)) {
      this.path = "";
      return;
    }

    PriorityQueue<WayPoint> open = new PriorityQueue<WayPoint>();
    HashSet<String> closed = new HashSet<String>();

    WayPoint wp = new WayPoint("", this.x, this.y, this.room_target_x, this.room_target_y, this.speed);
    open.add(wp);
    String wp_key = this.x + "_" + this.y;
    closed.add(wp_key);

    while (open.size() > 0) {
      wp = open.poll();
      if (TTDGE.show_searched_points) {
        TTDGE.papplet.pushStyle();
        TTDGE.papplet.fill(255, 0, 0);
        TTDGE.papplet.noStroke();
        TTDGE.papplet.ellipse(wp.x + TTDGE.x_offset, wp.y + TTDGE.y_offset, this.speed, this.speed);
        TTDGE.papplet.popStyle();
      }
      if (wp.estimate < this.speed) {
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


//  TODO: public Thing thing_here() {
//    return this.room.get(this.room_x(), this.room_y());
//  }

//  public void go_thing_here() {
//    Thing thing = thing_here();
//    if (thing != null) {
//      thing.go(this);
//    }
//  }
//
//  public void open_thing_here() {
//    Thing thing = thing_here();
//    if (thing != null) {
//      thing.open(this);
//    }
//  }
//
//  public void close_thing_here() {
//    Thing thing = thing_here();
//    if (thing != null) {
//      thing.open(this);
//    }
//  }
//
//  public void take_thing_here() {
//    Thing thing = thing_here();
//    if (thing != null) {
//      thing.take(this);
//    }
//  }
//
//  public void put_thing_here() {
//    if (this.items.size() > 0) {
//      if (this.thing_here() == null) {
//        Item item = this.items.remove(0);
//        item.put(this);
//      }
//      else {
//        System.out.println("There is already an item here.");
//      }
//    }
//    else {
//      System.out.println("There is no item here.");
//    }
//  }


  @Override
  public String type_name() {
    return "GameCharacter";
  }


  @Override
  public String default_name() {
    return "GameCharacter";
  }


  @Override
  public String default_description() {
    // TODO:
    return "";
  }

  public class WayPoint implements Comparable<WayPoint> {

    String path_here = "";
    int x, y, estimate;

    public WayPoint(String path_here, int x, int y, int target_x, int target_y, int speed) {
      this.path_here = path_here;
      this.x = x;
      this.y = y;
      this.estimate = (int)PApplet.dist(x, y, target_x, target_y);
    }

    @Override
    public int compareTo(WayPoint other) {
      if (this.path_here.length() + this.estimate == other.path_here.length() + other.estimate) {
        if (this.x == other.x) {
          return Integer.compare(this.y, other.y);
        }
        else {
          return Integer.compare(this.x, other.x);
        }

      }
      else {
        return Integer.compare(
            this.path_here.length() + this.estimate,
            other.path_here.length() + other.estimate
          );
      }

    }
  }

  private float direction_to_angle(char direction) {
    switch (direction) {
    case 'w':
      return PConstants.PI;
    case 'a':
      return PConstants.HALF_PI;
    case 's':
      return 0;
    case 'd':
      return -PConstants.HALF_PI;
    case 'q':
      return PConstants.QUARTER_PI*3;
    case 'e':
      return -PConstants.QUARTER_PI*3;
    case 'z':
      return PConstants.QUARTER_PI;
    case 'x':
      return -PConstants.QUARTER_PI;
    default:
      return 0;

    }
  }


  public void enter(Room room) {
    // TODO: enter(Room room)
  }

  @Override
  public void draw_on_parent() {}

  @Override
  public boolean is_pointed_on_parent() {
    return false;
  }


}
