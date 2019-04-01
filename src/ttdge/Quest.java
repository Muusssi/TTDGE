package ttdge;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Quest extends TTDGEObject {

  public ArrayList<Mission> done_missions = new ArrayList<Mission>();
  public ArrayList<Mission> following_missions = new ArrayList<Mission>();

  public String title;
  public String prolog;
  public String epilog;

  public boolean finished = false;

  public Quest(String id, String title, String prolog, String epilog) {
    super(id);
    this.title = title;
    this.prolog = prolog;
    this.epilog = epilog;
    TTDGE.quests.add(this);
  }

  public Quest(JSON json) {
    super(json);
    this.title = json.getString("title");
    this.prolog = json.getString("prolog");
    this.epilog = json.getString("epilog");
    this.finished = json.getBoolean("finished");
    load_missions(json.getArray("missions"));
  }

  protected void load_missions(JSONarray missions_json) {
    for (int i = 0; i < missions_json.size(); i++) {
      new Mission(missions_json.get(i), this);
    }
  }

  @Override
  protected JSON save_file_object() {
    JSON json = super.save_file_object();
    json.set("title", this.title);
    json.set("prolog", this.prolog);
    json.set("epilog", this.epilog);
    json.set("finished", this.finished);
    json.set("missions", this.missions_json());
   return json;
  }

  @Override
  protected ObjectEditingObject get_editing_panel() {
    ObjectEditingObject panel = super.get_editing_panel();
    panel.add_field("title", "Title", "The title of the quest", this.title);
    panel.add_field("prolog", "Prolog", "This message is given when the player begins the quest.", this.prolog);
    panel.add_field("epilog", "Epilog", "This message is given when the player finishes the quest.", this.epilog);
    return panel;
  }

  @Override
  protected void update_after_editing(ObjectEditingObject oeo) {
    super.update_after_editing(oeo);
    this.title = oeo.get_string("title");
    this.prolog = oeo.get_string("prolog");
    this.epilog = oeo.get_string("epilog");
  }

  protected JSONarray missions_json() {
    JSONarray array = new JSONarray();
    for (Mission mission : done_missions) {
      array.append(mission.save_file_object());
    }
    for (Mission mission : following_missions) {
      array.append(mission.save_file_object());
    }
    return array;
  }

  public void begin() {
    TTDGE.message("Starting new quest: " + this.title);
    TTDGE.message(this.prolog);
    if (!this.following_missions.isEmpty()) {
      this.following_missions.get(0).begin();
    }
    TTDGE.quests.remove(this);
    TTDGE.started_quests.add(this);
  }

  protected void end() {
    TTDGE.message(this.epilog);
    TTDGE.message("Finished quest: " + this.title);
  }

  public Mission current_mission() {
    if (this.following_missions.isEmpty()) {
      return null;
    }
    else {
      return this.following_missions.get(0);
    }
  }

  public void check() {
    Mission current_mission = this.current_mission();
    if (current_mission != null && current_mission.check()) {
      current_mission.end();
      this.done_missions.add(this.following_missions.remove(0));
      if (!this.following_missions.isEmpty()) {
        current_mission = this.current_mission();
        current_mission.begin();
      }
    }

    if (this.following_missions.isEmpty()) {
      this.finished = true;
      this.end();
    }
  }

  @Override
  public String type_name() {
    return "Quest";
  }

  @Override
  public void draw() {
    float offset = TTDGE.menu_scroll_offset + 30;
    TTDGE.papplet.pushStyle();
    TTDGE.papplet.fill(0);
    offset = TTDGE.long_text(this.title, offset, 30);
    offset = TTDGE.long_text(this.prolog, offset, 20);

    for (Mission mission : this.done_missions) {
      offset = mission.draw(offset);
    }
    if (!this.following_missions.isEmpty()) {
      offset = this.following_missions.get(0).draw(offset);
    }
    if (TTDGE.debug_mode) {
      for (int i=1; i<this.following_missions.size(); i++) {
        offset = this.following_missions.get(i).draw(offset);
      }
    }
    if (this.finished || TTDGE.debug_mode) {
      offset += 20;
      offset = TTDGE.long_text(this.epilog, offset, 20);
    }
    TTDGE.papplet.popStyle();
  }

  @Override
  public void post_editing_action() {
    if (JOptionPane.showConfirmDialog(null, "Would you like to edit the missions?", "Edit missions", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      for (Mission mission : this.following_missions) {
        mission.edit();
      }
      while (JOptionPane.showConfirmDialog(null, "Would you like to add a mission?", "Add missions", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        Mission mission = new Mission(null, this, "", "", "");
        mission.edit();
      }
    }
  }

  @Override
  public void draw_on_parent() {}

  @Override
  public Thing pointed_thing() {
    return null;
   }

  @Override
  public boolean is_pointed() {
    return false;
  }

  @Override
  public boolean is_pointed_on_parent() {
    return false;
  }

}
