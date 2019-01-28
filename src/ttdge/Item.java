package ttdge;


public class Item extends Thing {

  public Item(String id, String name, String description) {
    super(id, name, description);
  }

  @Override
  public String type_name() {
    return "Item";
  }

  @Override
  public JSON save_file_object() {
    JSON json = super.save_file_object();
    return json;
  }

  public Item(JSON json) {
    super(json);
  }

  @Override
  public void destroy() {
    super.destroy();
  }

  @Override
  public void take(GameCharacter game_character) {
    game_character.items.add(this);
    this.parent.remove_child(this);
  }

  @Override
  public void put(GameCharacter game_character, Thing where) {
    where.add_child(this);
    this.x = game_character.x;
    this.y = game_character.y;
  }

  @Override
  public String default_name() {
    return "Item";
  }

  @Override
  public String default_description() {
    return "Just an ordinary item. You can pick it up.";
  }

  @Override
  public void draw() {
    TTDGE.papplet.pushStyle();
    if (this.highlight) {
      TTDGE.papplet.stroke(255, 0, 0);
      TTDGE.papplet.strokeWeight(3);
    }
    TTDGE.papplet.fill(255, 255, 0);
    TTDGE.papplet.rect(TTDGE.x_offset + (x+0.25f)*TTDGE.room_grid_size, TTDGE.y_offset + (y+0.25f)*TTDGE.room_grid_size, TTDGE.room_grid_size/2, TTDGE.room_grid_size/2);
    TTDGE.papplet.popStyle();
  }

  @Override
  public void draw_on_parent() {
    // TODO Auto-generated method stub
  }




}
