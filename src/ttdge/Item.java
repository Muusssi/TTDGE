package ttdge;


public class Item extends Thing {

  public Item(World world, String id, String name, String description) {
    super(world, id, name, description);
  }

  @Override
  public void draw() {
    TTDGE.papplet.pushStyle();
    if (this.highlight) {
      TTDGE.papplet.stroke(255, 0, 0);
      TTDGE.papplet.strokeWeight(3);
    }
    TTDGE.papplet.fill(255, 255, 0);
    TTDGE.papplet.rect(TTDGE.x_offset + (room_x+0.25f)*TTDGE.room_grid_size, TTDGE.y_offset + (room_y+0.25f)*TTDGE.room_grid_size, TTDGE.room_grid_size/2, TTDGE.room_grid_size/2);
    TTDGE.papplet.popStyle();
  }

  @Override
  public String type_name() {
    return "Item";
  }

  @Override
  public JSON world_file_object() {
    JSON json = this.base_world_file_object();
    return json;
  }

  public static Item create(World world, JSON json) {
    String id = json.getString("id");
    String name = json.getString("name");
    String description = json.getString("description");
    Item new_item = new Item(world, id, name, description);
    new_item.json = json;
    return new_item;
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
  public void take(GameCharacter game_character) {
    game_character.items.add(this);
    if (this.room != null) {
      this.room.grid[this.room_x][this.room_y] = null;
      this.room = null;
    }
  }

  @Override
  public void put(GameCharacter game_character) {
    game_character.room.set_thing(this, game_character.room_x(), game_character.room_y());;
  }

  @Override
  public String default_name() {
    return "Item";
  }



  @Override
  public String default_description() {
    return "Just an ordinary item. You can pick it up.";
  }




}
