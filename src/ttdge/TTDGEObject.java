package ttdge;

public abstract class TTDGEObject {

  public String id;

  public TTDGEObject() {
    this.id = new_id();
    TTDGE.objects.put(this.id, this);
  }

  public TTDGEObject(String id) {
    if (id == null || id.equals("")) {
      this.id = new_id();
    }
    else if (TTDGE.objects.containsKey(id)) {
      TTDGE.fatal_error("Duplicate object id'"+ id + "'!");
    }
    else {
      this.id = id;
    }
    TTDGE.objects.put(this.id, this);
  }

  public TTDGEObject(JSON json) {
    this.id = json.getString("id");
    if (TTDGE.objects.containsKey(this.id)) {
      TTDGE.fatal_error("Duplicate object id'"+ this.id + "'!");
    }
    TTDGE.objects.put(this.id, this);
  }

  public JSON save_file_object() {
    JSON json = new JSON();
    json.set("type", this.type_name());
    json.set("id", this.id);
    return json;
  }

  public abstract void draw();

  public abstract void draw_on_parent();

  public abstract boolean is_pointed();

  public abstract boolean is_pointed_on_parent();

  public abstract Thing pointed_thing();

  public abstract String type_name();

  protected String new_id() {
    String new_id = type_name() + "-" + TTDGE.id_counter_next();
    while (TTDGE.objects.containsKey(new_id)) {
      new_id = type_name() + "-" + TTDGE.id_counter_next();
    }
    return new_id;
  }



  public void destroy() {
    TTDGE.objects.remove(this.id);
  }

}
