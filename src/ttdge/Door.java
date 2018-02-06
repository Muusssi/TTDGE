package ttdge;

public class Door extends Thing {

  public Door linked_door;
  public boolean closed = false;

  public Door(World world, String id, String name) {
    super(world, id, name);
  }

  @Override
  public String world_file_string() {
    String[] tokens = new String[8];
    tokens[0] = "Door";
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
    if (this.linked_door != null) {
      tokens[6] = this.linked_door.id;
    }
    else {
      tokens[6] = "null";
    }
    tokens[7] = Boolean.toString(this.closed);

    return String.join(TTDGE.WORLD_FILE_DELIMITER, tokens);
  }

  public static Door create(World world, String[] tokens) {
    Door new_door = new Door(world, tokens[1], tokens[2]);
    new_door.load_tokens = tokens;
    new_door.closed = Boolean.parseBoolean(tokens[7]);
    return new_door;
  }

  @Override
  public void linking_actions() {
    String room_id = load_tokens[3];
    if (!room_id.equals("null")) {
      room = (Room)world.things.get(room_id);
      room.set_thing(this, Integer.parseInt(load_tokens[4]), Integer.parseInt(load_tokens[5]));
    }
    String door_id = load_tokens[6];
    if (!door_id.equals("null")) {
      Door other_door = (Door)world.things.get(door_id);
      this.link_to(other_door);
    }
    load_tokens = null;
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
  public String default_name() {
    return "Door";
  }

  @Override
  public String id_prefix() {
    return "Door";
  }



  @Override
  public void draw() {
    TTDGE.papplet.pushStyle();
    TTDGE.papplet.fill(250);
    TTDGE.papplet.rect(TTDGE.x_offset + room_x*TTDGE.room_grid_size, TTDGE.y_offset + room_y*TTDGE.room_grid_size, TTDGE.room_grid_size, TTDGE.room_grid_size);
    TTDGE.papplet.popStyle();
  }



}
