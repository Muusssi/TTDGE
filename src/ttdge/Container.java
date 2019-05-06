package ttdge;

import java.util.ArrayList;

public abstract class Container extends Thing {

  public ArrayList<Item> items = new ArrayList<Item>();

  public Container(String id, String name, String description) {
    super(id, name, description);
  }

  public Container(JSON json) {
    super(json);
  }

  @Override
  protected JSON save_file_object() {
    JSON json = super.save_file_object();
    JSONarray items_json = new JSONarray();
    for (Item item : items) {
      items_json.append(item.save_file_object());
    }
    json.set("items", items_json);
    return json;
  }

  public void add_thing(Thing thing) {
    if (thing instanceof Item) {
      Item item = (Item)thing;
      this.items.add(item);
    }
    else {
      TTDGE.message("Only items can be put in there.");
    }
  }

  public void remove_thing(Thing thing) {
    this.items.remove(thing);
  }

  @Override
  public void destroy() {
    for (Item item : items) {
      item.destroy();
    }
    super.destroy();
  }

}
