package ttdge;


public abstract class Thing {

  public String id;
  public World world;
  public String name;
  public String description;

  public String image_file_name;

  public Room room;
  public int room_x, room_y;

  public JSON json = null;

  public boolean highlight = false;

  public Thing (World world, String id, String name, String description) {
    if (name == null || name.equals("null")) {
      this.name = this.default_name();
    }
    else {
      this.name = name;
    }
    this.name = name;
    this.world = world;

    if (id == null || id.equals("")) {
      this.id = id();
    }
    else {
      this.id = id;
    }
    if (description == null || description.equals("null")) {
      this.description = default_description();
    }
    else {
      this.description = description;
    }
    world.things.put(this.id, this);

  }

  public JSON base_world_file_object() {
    JSON json = new JSON();
    json.set("type", this.type_name());
    json.set("id", this.id);
    json.set("name", this.name);
    json.set("description", this.name);
    json.set("room", this.room);
    json.set("room_x", this.room_x);
    json.set("room_y", this.room_y);
    return json;
  }

  public void investigate() {
    TTDGE.message("This clearly is a " + this.name);
  }

  public void investigate(GameCharacter game_character) {}

  public void go(GameCharacter game_character) {}

  public void hit(GameCharacter game_character) {}

  public void eat(GameCharacter game_character) {}

  public void open(GameCharacter game_character) {}

  public void close(GameCharacter game_character) {}

  public void take(GameCharacter game_character) {}

  public void put(GameCharacter game_character) {}

  public void operate(GameCharacter game_character) {}

  public boolean collide(GameCharacter game_character) {
    return false;
  }

  public String default_image_file_name() {
    return null;
  }


  protected String id() {
    return type_name() + "-" + this.world.id_counter_next();
  }

  public abstract String default_name();

  public abstract String default_description();

  public abstract String type_name();

  public abstract JSON world_file_object();

  public abstract void draw();

  public abstract void linking_actions();


}
