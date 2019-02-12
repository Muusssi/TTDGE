package ttdge;

import processing.core.PApplet;
import processing.core.PImage;


public abstract class Thing extends TTDGEObject {

  public String name;
  public String description;

  public int x, y;
  public int width = 0;
  public int height = 0;

  public String image_file_name = null;
  public String used_image_file_name = null;
  public int radius = TTDGE.room_grid_size/2;

  public boolean highlight = false;

  public Thing (String id, String name, String description) {
    super(id);
    if (name == null) {
      this.name = this.default_name();
    }
    else {
      this.name = name;
    }

    if (description == null) {
      this.description = default_description();
    }
    else {
      this.description = description;
    }
  }

  public Thing(JSON json) {
    super(json);
    this.name = json.getString("name");
    this.description = json.getString("description");
    this.x = json.getInt("x");
    this.y = json.getInt("y");
    this.set_image(json.getString("image"));
    this.radius = json.getInt("radius");
  }

  @Override
  public JSON save_file_object() {
    JSON json = super.save_file_object();
    json.set("name", this.name);
    json.set("description", this.name);
    json.set("x", this.x);
    json.set("y", this.y);
    json.set("image", this.image_file_name);
    json.set("radius", radius);
    return json;
  }

  public void set_image(String file_name) {
    if (file_name == null) {
      file_name = this.default_image_file_name();
    }
    this.image_file_name = file_name;
    if (file_name != null && TTDGE.load_image(file_name)) {
      this.used_image_file_name = file_name;
    }
  }

  public PImage get_image() {
    return TTDGE.loaded_images.get(this.used_image_file_name);
  }


  // Character actions
  public void investigate(GameCharacter game_character) {
    TTDGE.message("This clearly is " + TTDGEUtils.article_for(this.name) + " " + this.name);
  }

  public void go(GameCharacter game_character) {
    TTDGE.message("I can't go there!");
  }

  public void hit(GameCharacter game_character) {
    TTDGE.message("POW! Auts... Nothing happens.");
  }

  public void eat(GameCharacter game_character) {
    TTDGE.message("No, I'm not eating that.");
  }

  public void open(GameCharacter game_character) {
    TTDGE.message("I can't open it.");
  }

  public void close(GameCharacter game_character) {
    TTDGE.message("I can't close it.");
  }

  public void take(GameCharacter game_character) {
    TTDGE.message("I can't take it.");
  }

  public void put(GameCharacter game_character, Container where) {
    TTDGE.message("I can't put it there.");
  }

  public void put(GameCharacter game_character, Thing where) {
    TTDGE.message("I can't leave it here.");
  }

  public void combine(GameCharacter game_character, Thing with) {
    TTDGE.message("I have no idea how to combine these.");
  }

  public void operate(GameCharacter game_character) {
    TTDGE.message("I don't know how to operate that.");
  }

  public String default_image_file_name() {
    return null;
  }

  @Override
  public Thing pointed_thing() {
    // TODO: pointed_thing()
    return null;
  }

  @Override
  public boolean is_pointed() {
    if (PApplet.dist(this.x, this.y, TTDGE.papplet.mouseX - TTDGE.x_offset, TTDGE.papplet.mouseY - TTDGE.y_offset) < this.radius) {
      return true;
    }
    return false;
  }

  public abstract String default_name();

  public abstract String default_description();

  @Override
  public void destroy() {
    super.destroy();
  }

  public boolean collide_in_position(GameCharacter game_character, int x, int y) {
    return false;
  }


}
