package ttdge;

import processing.data.JSONObject;

public class Obstacle extends Thing {

  public int x = -1;
  public int y = -1;
  protected String[] for_linking = null;

  public Obstacle(World world, String id, String name, String description) {
    super(world, id, name, description);
  }

  public static Obstacle create(World world, JSONObject json) {
    String id = json.getString("id");
    String name = json.getString("name");
    String description = json.getString("description");
    Obstacle new_obstacle = new Obstacle(world, id, name, description);
    new_obstacle.json = json;
    return new_obstacle;
  }

  @Override
  public JSONObject world_file_object() {
    JSONObject json = this.base_world_file_object();
    return json;
  }

  @Override
  public void linking_actions() {
    this.room = world.get_room(this.json.getString("room"));
    int room_x = json.getInt("room_x");
    int room_y = json.getInt("room_y");
    if (room_x > 0 || room_y > 0) {
      room.set_thing(this, room_x, room_y);
    }
    this.json = null;
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
  public String type_name() {
    return "Obstacle";
  }

  @Override
  public String default_name() {
    return "Obstacle";
  }

  @Override
  public String default_description() {
    // TODO Auto-generated method stub
    return null;
  }


}
