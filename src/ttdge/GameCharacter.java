package ttdge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

import processing.core.PConstants;
import processing.core.PImage;
import processing.data.JSONObject;

public class GameCharacter extends Thing {

  public int x = 0;
  public int y = 0;
  public char last_direction = 's';
  public char direction = 0;

  public int speed = 2;

  public int radius = TTDGE.room_grid_size/3;

  public ArrayList<Item> items = new ArrayList<Item>();

  public PImage image = null;

  // For path finding
  public int room_target_x = -1;
  public int room_target_y = -1;
  public String path = null;

  public GameCharacter(World world, String id, String name, String description) {
    super(world, id, name, description);
    if (TTDGE.player == null) {
      TTDGE.player = this;
    }
    image = TTDGE.papplet.loadImage("smile.png");
  }

  @Override
  public JSONObject world_file_object() {
    JSONObject json = this.base_world_file_object();
    json.setInt("x", this.x);
    json.setInt("y", this.y);
    json.setBoolean("player", TTDGE.player == this);
    return json;
  }

  public static GameCharacter create(World world, JSONObject json) {
    String id = json.getString("id");
    String name = json.getString("name");
    String description = json.getString("description");
    GameCharacter new_character = new GameCharacter(world, id, name, description);
    new_character.json = json;
    return new_character;
  }

  @Override
  public void linking_actions() {
    this.room = world.get_room(this.json.getString("room"));
    this.x = json.getInt("x");
    this.y = json.getInt("y");
    if (TTDGE.player == null && this.json.getBoolean("player")) {
      TTDGE.player = this;
    }
    this.json = null;
  }

  @Override
  public void draw() {
    // TODO:
    if (this.image == null) {
      TTDGE.papplet.pushMatrix();
      TTDGE.papplet.translate(this.image.width/2, this.image.height/2);
      if (this.direction != 0) {
        TTDGE.papplet.rotate(this.direction);
      }
      else {
        TTDGE.papplet.rotate(this.last_direction);
      }
      //TTDGE.imageMode(PConstants.CENTER);
      TTDGE.papplet.image(image, 0, 0);
      TTDGE.papplet.popMatrix();
      TTDGE.papplet.rect(0, 0, TTDGE.room_grid_size/2, TTDGE.room_grid_size/2);
      TTDGE.papplet.ellipse(TTDGE.x_offset + x, TTDGE.y_offset + y, this.radius*2, this.radius*2);
      TTDGE.papplet.text(this.last_direction, 100, 100);

      this.last_direction = this.direction;
      this.direction = 0;
    }
    else {
      TTDGE.papplet.ellipse(TTDGE.x_offset + x, TTDGE.y_offset + y, this.radius*2, this.radius*2);
    }
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
    int new_y = y - this.speed - this.radius;
    if (new_y < 0) {
      return false;
    }
    else if (this.room.obstacle(this, x/TTDGE.room_grid_size, new_y/TTDGE.room_grid_size)) {
      return false;
    }
    return true;
  }

  public boolean can_move_down() {
    return can_move_down(this.x, this.y);
  }

  public boolean can_move_down(int x, int y) {
    int new_y = y + this.speed + this.radius;
    if (new_y > this.room.room_height*TTDGE.room_grid_size) {
      return false;
    }
    else if (this.room.obstacle(this, x/TTDGE.room_grid_size, new_y/TTDGE.room_grid_size)) {
      return false;
    }
    return true;
  }

  public boolean can_move_left() {
    return can_move_left(this.x, this.y);
  }

  public boolean can_move_left(int x, int y) {
    int new_x = x - this.speed - this.radius;
    if (new_x < 0) {
      return false;
    }
    else if (this.room.obstacle(this, new_x/TTDGE.room_grid_size, y/TTDGE.room_grid_size)) {
      return false;
    }
    return true;
  }

  public boolean can_move_right() {
    return can_move_right(this.x, this.y);
  }

  public boolean can_move_right(int x, int y) {
    int new_x = x + this.speed + this.radius;
    if (new_x > this.room.room_width*TTDGE.room_grid_size) {
      return false;
    }
    else if (this.room.obstacle(this, new_x/TTDGE.room_grid_size, y/TTDGE.room_grid_size)) {
      return false;
    }
    return true;
  }

  public boolean touches_obstacle() {

    return false;
  }

  public void move_to_target() {
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

  public void follow(GameCharacter other) {
    // TODO: follow(GameCharacter other)
  }

  /**
   * Updates the GameCharacters path using the A* algorithm
   */
  public void update_path() {
    PriorityQueue<WayPoint> open = new PriorityQueue<WayPoint>();
    HashSet<String> closed = new HashSet<String>();

    WayPoint wp = new WayPoint("", this.x, this.y, this.room_target_x,
                                this.room_target_y, this.speed);
    open.add(wp);
    String wp_key = this.x + "_" + this.y;
    closed.add(wp_key);

    while (open.size() > 0) {
      wp = open.poll();
      if (TTDGE.show_searched_points) {
        TTDGE.papplet.pushStyle();
        TTDGE.papplet.fill(255, 0, 0);
        TTDGE.papplet.noStroke();
        TTDGE.papplet.ellipse(wp.x + TTDGE.x_offset, wp.y + TTDGE.y_offset, 5, 5);
        TTDGE.papplet.popStyle();
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

  public Thing thing_here() {
    return this.room.get(this.x/TTDGE.room_grid_size, this.y/TTDGE.room_grid_size);
  }

  public void go_here() {
    Thing thing = thing_here();
    if (thing != null) {
      thing.go(this);
    }
  }

  public void open_here() {
    Thing thing = thing_here();
    if (thing != null) {
      thing.open(this);
    }
  }

  public void close_here() {
    Thing thing = thing_here();
    if (thing != null) {
      thing.open(this);
    }
  }

  public void take_here() {
    Thing thing = thing_here();
    if (thing != null) {
      thing.take(this);
    }
  }

  public void put_here() {
    if (this.items.size() > 0) {
      if (this.thing_here() == null) {
        Item item = this.items.remove(0);
        item.put(this);
      }
      else {
        System.out.println("There is already an item here.");
      }
    }
    else {
      System.out.println("There is no item here.");
    }
  }

  public int room_x() {
    return this.x/TTDGE.room_grid_size;
  }

  public int room_y() {
    return this.y/TTDGE.room_grid_size;
  }


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

    @SuppressWarnings("static-access")
    public WayPoint(String path_here, int x, int y, int target_x, int target_y, int speed) {
      this.path_here = path_here;
      this.x = x;
      this.y = y;
      this.estimate = (TTDGE.papplet.abs(x - target_x) + TTDGE.papplet.abs(y - target_y));
    }

    @Override
    public int compareTo(WayPoint other) {
      return Integer.compare(
          this.path_here.length() + this.estimate,
          other.path_here.length() + other.estimate
        );
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






}
