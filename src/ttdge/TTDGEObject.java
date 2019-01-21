package ttdge;

public abstract class TTDGEObject {

  public String id;
  public World world;

  public JSON json = null;

  public TTDGEObject(World world, String id) {
    this.world = world;
    if (id == null || id.equals("")) {
      this.id = id();
    }
    else {
      this.id = id;
    }
  }

  public JSON base_world_file_object() {
    JSON json = new JSON();
    json.set("type", this.type_name());
    json.set("id", this.id);
    return json;
  }

  public abstract String type_name();

  protected String id() {
    String new_id = type_name() + "-" + this.world.id_counter_next();
    while (this.world.things.containsKey(new_id)) {
      new_id = type_name() + "-" + this.world.id_counter_next();
    }
    return new_id;
  }

}
