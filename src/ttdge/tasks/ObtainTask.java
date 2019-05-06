package ttdge.tasks;

import ttdge.Item;
import ttdge.JSON;
import ttdge.Mission;
import ttdge.ObjectEditingObject;
import ttdge.TTDGE;
import ttdge.Task;

public class ObtainTask extends Task {

  public String target_item_id;

  public ObtainTask(String id, Mission mission, Item item) {
    super(id, mission);
    this.target_item_id = item.id;
  }

  public ObtainTask(JSON json, Mission mission) {
    super(json, mission);
    this.target_item_id = json.getString("target_item_id");
  }

  @Override
  protected JSON save_file_object() {
    JSON json = super.save_file_object();
    json.set("target_item_id", this.target_item_id);
    return json;
  }

  @Override
  protected ObjectEditingObject get_editing_panel() {
    ObjectEditingObject panel = super.get_editing_panel();
    panel.add_field("target_item_id", "Target item", "The ID of the target item.", this.target_item_id);
    return panel;
  }

  @Override
  protected void update_after_editing(ObjectEditingObject oeo) {
    super.update_after_editing(oeo);
    this.target_item_id = oeo.get_string("target_item_id");
  }

  @Override
  public boolean check() {
    boolean done = false;
    for (Item item : TTDGE.player.inventory.items) {
      done = done || item.id.equals(this.target_item_id);
    }
    this.done = done;
    return this.done;
  }

  @Override
  public String type_name() {
    return "ObtainTask";
  }

  @Override
  public String instructions() {
    return "Obtain item '" + this.target_item_id + "'.";
  }

}
