package ttdge;

public class Key extends Item {

  public Key(String id, String name, String description) {
    super(id, name, description);
    TTDGE.keys.put(this.id, this);
  }

  public Key(String id, String name, String description, Room room) {
    super(id, name, description, room);
    TTDGE.keys.put(this.id, this);
  }

  public Key(String id, String name, String description, Container container) {
    super(id, name, description, container);
    TTDGE.keys.put(this.id, this);
  }

  public Key(JSON json) {
    super(json);
    TTDGE.keys.put(this.id, this);
  }

  public Key(JSON json, Room room) {
    super(json, room);
    TTDGE.keys.put(this.id, this);
  }

  public Key(JSON json, Container container) {
    super(json, container);
    TTDGE.keys.put(this.id, this);
  }

  @Override
  protected JSON save_file_object() {
    JSON json = super.save_file_object();
    return json;
  }

  @Override
  protected ObjectEditingObject get_editing_panel() {
    ObjectEditingObject panel = super.get_editing_panel();
    return panel;
  }

  @Override
  protected void update_after_editing(ObjectEditingObject oeo) {
    super.update_after_editing(oeo);
  }


  @Override
  public String type_name() {
    return "Key";
  }

  @Override
  public String default_name() {
    return "Key";
  }

  @Override
  public String default_description() {
    return "This seems to be a key. Maybe you can unlock something with it.";
  }

  @Override
  public void destroy() {
    super.destroy();
    TTDGE.keys.remove(this.id);
  }

}
