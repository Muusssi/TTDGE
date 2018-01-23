package ttdge;

public abstract class Thing {

  public String id;
  public World world;
  public String name;;

  public Room room;
  public int room_x, room_y;

  public Thing (World world, String id, String name) {
    if (name == null || name.equals("")) {
      this.name = this.default_name();
    }
    else {
      this.name = name;
    }
    this.name = name;
    this.world = world;

    if (id == null) {
      this.id = id();
    }
    else {
      this.id = id;
    }
    world.things.put(this.id, this);

  }

  public void investigate() {}

  public void hit() {}

  public void eat() {}

  public void open() {}

  public void close() {}

  public void take() {}

  public void operate() {}

  public boolean collide() {
    return false;
  }


  protected String id() {
    return id_prefix() + "-" + this.world.id_counter_next();
  }

  public abstract String default_name();

  public abstract String id_prefix();

  public abstract String world_file_string();

  public abstract void draw();







}
