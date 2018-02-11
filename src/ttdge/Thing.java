package ttdge;

import processing.data.JSONObject;

public abstract class Thing {

  public String id;
  public World world;
  public String name;
  public String description;

  public Room room;
  public int room_x, room_y;

  public JSONObject json = null;

  public Thing (World world, String id, String name, String description) {
    if (name == null || name.equals("null")) {
      this.name = this.default_name();
    }
    else {
      this.name = name;
    }
    this.name = name;
    this.world = world;

    if (id == null || id.equals("null")) {
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

  public JSONObject base_world_file_object() {
    JSONObject json = new JSONObject();
    json.setString("type", this.type_name());
    json.setString("id", this.id);
    json.setString("name", this.name);
    json.setString("description", this.name);
    if (this.room != null) {
      json.setString("room", this.room.id);
    }
    else {
      json.setString("room", null);
      System.out.println("wtf");
    }
    json.setInt("room_x", this.room_x);
    json.setInt("room_y", this.room_y);

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

  public void operate(GameCharacter game_character) {}

  public boolean collide(GameCharacter game_character) {
    return false;
  }


  protected String id() {
    return type_name() + "-" + this.world.id_counter_next();
  }

  public abstract String default_name();

  public abstract String default_description();

  public abstract String type_name();

  public abstract JSONObject world_file_object();

  public abstract void draw();

  public abstract void linking_actions();


}
