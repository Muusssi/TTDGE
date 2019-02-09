package ttdge;


public class Item extends Thing {

  Room room;
  Container container;

  public Item(String id, String name, String description) {
    super(id, name, description);
    this.radius = TTDGE.room_grid_size/4;
  }

  public Item(JSON json) {
    super(json);
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

  @Override
  public void destroy() {
    super.destroy();
    if (this.room != null) {
      this.room.remove_thing(this);
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
  public void put(GameCharacter game_character, Thing where) {
    // TODO: put item
    if (where instanceof Room) {
      Room room = (Room)where;
      this.x = game_character.x;
      this.y = game_character.y;
      room.add_thing(this);
      game_character.inventory.remove(this);
    }
    else if (where instanceof Container) {
      Container container = (Container)where;
      container.add_thing(this);
      game_character.inventory.remove(this);
    }
    else {
      TTDGE.message("You can't put anything in there.");
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
    TTDGE.papplet.popStyle();
  }

  @Override
  public void draw_on_parent() {}

  @Override
  public boolean is_pointed() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean is_pointed_on_parent() {
    // TODO Auto-generated method stub
    return false;
  }




}
