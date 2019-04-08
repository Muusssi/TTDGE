package ttdge;


public class Item extends Thing {

  Room room;
  Container container;

  public Item(String id, String name, String description) {
    super(id, name, description);
    this.radius = TTDGE.room_grid_size/4;
    TTDGE.items.put(this.id, this);
  }

  public Item(String id, String name, String description, Room room) {
    super(id, name, description);
    this.room = room;
    this.room.add_thing(this);
    this.radius = TTDGE.room_grid_size/4;
    TTDGE.items.put(this.id, this);
  }

  public Item(JSON json) {
    super(json);
  }

  public Item(JSON json, Room room) {
    super(json);
    this.room = room;
    this.room.add_thing(this);
  }

  @Override
  public String type_name() {
    return "Item";
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
    TTDGE.items.remove(this.id);
    if (this.room != null) {
      this.room.remove_thing(this);
    }
  }

  @Override
  public void default_action(GameCharacter game_character) {
    if (game_character.inventory.contains(this)) {
      this.drop(game_character);
    }
    else {
      this.take(game_character);
    }
  }

  @Override
  public void take(GameCharacter game_character) {
    game_character.inventory.add(this);
    if (this.room != null) {
      this.room.remove_thing(this);
    }
    if (this.container != null) {
      this.container.remove_thing(this);
    }
  }

  public void put_into(Thing where) {
    if (where instanceof Room) {
      Room room = (Room)where;
      this.room = room;
      room.add_thing(this);
    }
    else if (where instanceof Container) {
      Container container = (Container)where;
      container.add_thing(this);
    }
    else {
      TTDGE.message("You can't put anything in there.");
    }
  }

  @Override
  public void drop(GameCharacter game_character) {
    if (game_character.room != null) {
      this.x = game_character.x;
      this.y = game_character.y;
      room.add_thing(this);
      game_character.inventory.remove(this);
    }
    else {
      TTDGE.message("Im floating in a void. The item would be lost if I dropped it here.");
    }
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
    TTDGE.papplet.rect(TTDGE.x_offset + x - this.radius, TTDGE.y_offset + y - this.radius, this.radius*2, this.radius*2);
    if (TTDGE.debug_mode) {
      TTDGE.papplet.fill(255, 0, 0);
      TTDGE.papplet.stroke(0, 255, 0);
      TTDGE.papplet.text(this.id + ": " + this.name, TTDGE.x_offset + x, TTDGE.y_offset + y);
    }
    TTDGE.papplet.popStyle();
  }

  @Override
  public void draw_on_parent() {}

  @Override
  public boolean is_pointed_on_parent() {
    // TODO Auto-generated method stub
    return false;
  }




}
