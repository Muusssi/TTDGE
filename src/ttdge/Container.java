package ttdge;

import java.util.ArrayList;

public abstract class Container extends Thing {

  Container container;
  ArrayList<Item> items = new ArrayList<Item>();

  public Container(Container container, String id, String name, String description) {
    super(id, name, description);
    this.container = container;
  }

  public Container(JSON json, Container container) {
    super(json);
    this.container = container;
  }

  @Override
  public JSON save_file_object() {
    JSON json = super.save_file_object();
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

}
