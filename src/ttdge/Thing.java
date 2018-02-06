package ttdge;

public abstract class Thing {

  public String id;
  public World world;
  public String name;;

  public Room room;
  public int room_x, room_y;

  public String[] load_tokens = null;

  public Thing (World world, String id, String name) {
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
    world.things.put(this.id, this);

  }

  public void investigate() {
    TTDGE.message("This clearly is a " + this.name);
  }

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
    return id_prefix() + "-" + this.world.id_counter_next();
  }

  public abstract String default_name();

  public abstract String id_prefix();

  public abstract String world_file_string();

  public abstract void draw();

  public abstract void linking_actions();







}
