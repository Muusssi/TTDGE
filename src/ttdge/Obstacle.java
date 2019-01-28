package ttdge;


public class Obstacle extends Thing {

  public int x, y;
  protected String[] for_linking = null;

  public Obstacle(Room room, String id, String name, String description, int x, int y) {
    super(id, name, description);
    this.x = x;
    this.y = y;
    room.set_thing(this, x, y);
  }

  public Obstacle(JSON json, Room room) {
    super(json);
    this.x = json.getInt("x");
    this.y = json.getInt("y");
    room.add_child(this);
  }

  @Override
  public JSON save_file_object() {
    JSON json = super.save_file_object();
    return json;
  }

  @Override
  public void destroy() {
    super.destroy();
  }

  @Override
  public void draw_on_parent() {
    TTDGE.papplet.pushStyle();
    TTDGE.papplet.fill(0);
    if (this.highlight) {
      TTDGE.papplet.stroke(255, 0, 0);
      TTDGE.papplet.strokeWeight(3);
    }
    TTDGE.papplet.rect(TTDGE.x_offset + x - TTDGE.room_grid_size/2,
                       TTDGE.y_offset + y - TTDGE.room_grid_size/2,
                       TTDGE.room_grid_size, TTDGE.room_grid_size);
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

  @Override
  public void draw() {
    // TODO Auto-generated method stub
  }


}
