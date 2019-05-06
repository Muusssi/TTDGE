package ttdge;

public class Inventory extends Container {

  public GameCharacter character;
  public Item highlighted_item = null;

  public Inventory(GameCharacter character) {
    super(null, null, null);
    this.character = character;
  }

  public Inventory(JSON json, GameCharacter character) {
    super(json);
    this.character = character;
  }

  @Override
  protected JSON save_file_object() {
    JSON json = super.save_file_object();
    return json;
  }


  @Override
  public String default_name() {
    return "Inventory of a character";
  }

  @Override
  public String default_description() {
    return "This is the inventory of a character";
  }

  public void print() {
    for (Item item : items) {
      System.out.println(item.name);
    }
  }

  @Override
  public void draw() {
    float offset = TTDGE.menu_scroll_offset + 30;
    TTDGE.papplet.pushStyle();
    TTDGE.papplet.fill(0);
    offset = TTDGE.long_text(this.name, offset, 30);
    for (Item item : items) {
      offset = item.draw_in_inventory(offset, item == highlighted_item);
      item.y = (int)offset;
    }
    TTDGE.papplet.popStyle();
  }

  public Item pointed_item() {
    Item pointed = null;
    for (int i = items.size() - 1;  i >= 0; i--) {
      Item item = items.get(i);
      if (TTDGE.papplet.mouseY < item.y) {
        pointed = item;
      }
    }
    highlighted_item = pointed;
    return pointed;
  }

  @Override
  public void draw_on_parent() {}

  @Override
  public boolean is_pointed_on_parent() {
    return false;
  }

  @Override
  public String type_name() {
    return "Inventory";
  }

  @Override
  public void destroy() {
    this.character.inventory = null;
  }

}
