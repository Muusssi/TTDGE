package ttdge;

public class Obstacle extends Thing {

  public int x = -1;
  public int y = -1;
  protected String[] for_linking = null;

  public Obstacle(World world, String id, String name) {
    super(world, id, name);
  }

  public static Obstacle create(World world, String[] tokens) {
    Obstacle new_obstacle = new Obstacle(world, tokens[1], tokens[2]);
    new_obstacle.load_tokens = tokens;
    return new_obstacle;
  }

  @Override
  public String world_file_string() {
    String[] tokens = new String[6];
    tokens[0] = "Obstacle";
    tokens[1] = this.id;
    tokens[2] = this.name;
    if (room != null) {
      tokens[3] = this.room.id;
    }
    else {
      tokens[3] = "null";
    }
    tokens[4] = Integer.toString(this.room_x);
    tokens[5] = Integer.toString(this.room_y);
    return String.join(TTDGE.WORLD_FILE_DELIMITER, tokens);
  }

  @Override
  public void linking_actions() {
    String room_id = load_tokens[3];
    if (!room_id.equals("null")) {
      room = (Room)world.things.get(room_id);
      room.set_thing(this, Integer.parseInt(load_tokens[4]), Integer.parseInt(load_tokens[5]));
    }
    load_tokens = null;
  }

  @Override
  public void draw() {
    TTDGE.papplet.pushStyle();
    TTDGE.papplet.fill(0);
    TTDGE.papplet.rect(TTDGE.x_offset + room_x*TTDGE.room_grid_size, TTDGE.y_offset + room_y*TTDGE.room_grid_size, TTDGE.room_grid_size, TTDGE.room_grid_size);
    TTDGE.papplet.popStyle();
  }


  @Override
  public boolean collide(GameCharacter game_character) {
    return true;
  }

  @Override
  public String id_prefix() {
    return "Obstacle";
  }

  @Override
  public String default_name() {
    return "Obstacle";
  }


}
