package ttdge;

import java.util.ArrayList;
import java.util.Iterator;


public abstract class TTDGEObject {

  public String id;

  //public JSON json = null;

  public int x, y;
  public int width, height;

  public TTDGEObject parent = null;
  public ArrayList<TTDGEObject> children = new ArrayList<TTDGEObject>();

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
    json.set("children", this.children_json());
    return json;
  }

  public JSONarray children_json() {
    JSONarray array = new JSONarray();
    Iterator<TTDGEObject> itr = this.children.iterator();
    while (itr.hasNext()) {
      array.append(itr.next().save_file_object());
    }
    return array;
  }

  public abstract void draw();

  public abstract void draw_on_parent();

  public abstract String type_name();

  protected String new_id() {
    String new_id = type_name() + "-" + TTDGE.id_counter_next();
    while (TTDGE.objects.containsKey(new_id)) {
      new_id = type_name() + "-" + TTDGE.id_counter_next();
    }
    return new_id;
  }

  public void add_child(TTDGEObject child) {
    this.children.add(child);
    child.parent = this;
    // TODO: efficient data structure for the child objects. e.g. k-d tree
  }

  public void remove_child(TTDGEObject child) {
    this.children.remove(child);
  }

  public void destroy() {
    Iterator<TTDGEObject> itr = children.iterator();
    while(itr.hasNext()) {
      itr.next().destroy();
    }
    if (this.parent != null) {
      this.parent.children.remove(this);
    }
    TTDGE.objects.remove(this.id);
  }

  public boolean collide(GameCharacter game_character) {
    return false;
  }

}
