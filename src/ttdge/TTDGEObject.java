package ttdge;

import javax.swing.JOptionPane;

public abstract class TTDGEObject {

  public String id;
  protected boolean destroyed = false;

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

  protected JSON save_file_object() {
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

  protected ObjectEditingObject get_editing_panel() {
    ObjectEditingObject panel = new ObjectEditingObject(this);
    panel.add_field("id", "ID", "The unique id of this object that can be used to refer to this object.", this.id);
    return panel;
  }

  public boolean update_id(String new_id) {
    if (TTDGE.objects.containsKey(new_id)) {
      TTDGE.error("Unable to update id. Given id '" + new_id + "' already in use.");
    }
    else {
      TTDGE.update_object_maps(this, new_id);
      this.id = new_id;
      return true;
    }
    return false;
  }

  protected void update_after_editing(ObjectEditingObject oeo) {
    String new_id = oeo.get_string("id");
    if (this.id.equals(new_id)) {
      return;
    }
    this.update_id(new_id);
  }

  protected void post_editing_action() {}

  public boolean edit() {
    ObjectEditingObject oeo = this.get_editing_panel();
    int res = oeo.show();
    if (res == JOptionPane.OK_OPTION) {
      this.update_after_editing(oeo);
      post_editing_action();
      return true;
    }
    post_editing_action();
    return false;
  }

  public void destroy() {
    TTDGE.objects.remove(this.id);
    this.destroyed = true;
  }

}
