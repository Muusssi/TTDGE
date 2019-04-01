package ttdge.tasks;

import ttdge.JSON;
import ttdge.Mission;
import ttdge.ObjectEditingObject;
import ttdge.Room;
import ttdge.TTDGE;
import ttdge.Task;

public class VisitTask extends Task {

  public String target_room_id;

  public VisitTask(String id, Mission mission, Room room) {
    super(id, mission);
    this.target_room_id = room.id;
  }

  public VisitTask(JSON json, Mission mission) {
    super(json, mission);
    this.target_room_id = json.getString("target_room_id");
  }

  @Override
  protected JSON save_file_object() {
    JSON json = super.save_file_object();
    json.set("target_room_id", this.target_room_id);
    return json;
  }

  @Override
  protected ObjectEditingObject get_editing_panel() {
    ObjectEditingObject panel = super.get_editing_panel();
    panel.add_field("target_room_id", "Target room", "The ID of the target room.", this.target_room_id);
    return panel;
  }

  @Override
  protected void update_after_editing(ObjectEditingObject oeo) {
    super.update_after_editing(oeo);
    this.target_room_id = oeo.get_string("target_room_id");
  }

  @Override
  public boolean check() {
    this.done = this.done || TTDGE.player.room.id.equals(this.target_room_id);
    return this.done;
  }

  @Override
  public String type_name() {
    return "VisitTask";
  }

  @Override
  public String instructions() {
    return "Go to room '" + this.target_room_id + "'.";
  }

}
