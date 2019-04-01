package ttdge;




public class Obstacle extends Thing {

  Room room;

  public Obstacle(String id, String name, String description, Room room) {
    super(id, name, description);
    this.room = room;
    room.add_thing(this);
  }

  public Obstacle(JSON json, Room room) {
    super(json);
    this.room = room;
    room.add_thing(this);
  }

  @Override
  protected JSON save_file_object() {
    JSON json = super.save_file_object();
    return json;
  }

  @Override
  protected ObjectEditingObject get_editing_panel() {
    ObjectEditingObject panel = super.get_editing_panel();
    return panel;
  }

  @Override
  protected void update_after_editing(ObjectEditingObject oeo) {
    super.update_after_editing(oeo);
  }

  @Override
  public void destroy() {
    super.destroy();
    if (this.room != null) {
      this.room.remove_thing(this);
    }
  }


  @Override
  public boolean collide_in_position(GameCharacter game_character, int x, int y) {
    if (x + game_character.radius > this.x - this.radius && x - game_character.radius < this.x + this.radius &&
        y + game_character.radius > this.y - this.radius && y - game_character.radius < this.y + this.radius) {
      return true;
    }
    return false;
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
    TTDGE.papplet.pushStyle();
    TTDGE.papplet.fill(0);
    if (this.highlight) {
      TTDGE.papplet.stroke(255, 0, 0);
      TTDGE.papplet.strokeWeight(3);
    }
    TTDGE.papplet.rect(TTDGE.x_offset + x - this.radius, TTDGE.y_offset + y - this.radius,
                       this.radius*2, this.radius*2);
    TTDGE.papplet.popStyle();
  }

  @Override
  public void draw_on_parent() {
    TTDGE.papplet.pushStyle();
    TTDGE.papplet.fill(0);
    TTDGE.papplet.rect(
        TTDGE.x_map_offset + room.x + (x - this.radius)/TTDGE.map_grid_size,
        TTDGE.y_map_offset + room.y + (y - this.radius)/TTDGE.map_grid_size,
        2*this.radius/TTDGE.map_grid_size, 2*this.radius/TTDGE.map_grid_size);
    TTDGE.papplet.popStyle();
  }

  @Override
  public boolean is_pointed_on_parent() {
    // TODO Auto-generated method stub
    return false;
  }


}
