package ttdge;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import ttdge.tasks.ObtainTask;
import ttdge.tasks.VisitTask;

public class Mission extends TTDGEObject {

  public Quest quest;
  public String title;
  public String prolog;
  public String epilog;
  public boolean done;

  public ArrayList<Task> tasks = new ArrayList<Task>();

  public Mission(String id, Quest quest, String title, String prolog, String epilog) {
    super(id);
    this.quest = quest;
    quest.following_missions.add(this);
    this.title = title;
    this.prolog = prolog;
    this.epilog = epilog;
  }

  public Mission(JSON json, Quest quest) {
    super(json);
    this.quest = quest;
    this.title = json.getString("title");
    this.prolog = json.getString("prolog");
    this.epilog = json.getString("epilog");
    this.done = json.getBoolean("done");
    if (this.done) {
      quest.done_missions.add(this);
    }
    else {
      quest.following_missions.add(this);
    }
  }

  @Override
  protected JSON save_file_object() {
    JSON json = super.save_file_object();
    json.set("title", this.title);
    json.set("prolog", this.prolog);
    json.set("epilog", this.epilog);
    json.set("done", this.done);
    json.set("tasks", this.tasks_json());
   return json;
  }

  @Override
  protected ObjectEditingObject get_editing_panel() {
    ObjectEditingObject panel = super.get_editing_panel();
    panel.add_field("title", "Title", "The title of the quest", this.title);
    panel.add_field("prolog", "Title", "The title of the quest", this.prolog);
    panel.add_field("epilog", "Title", "The title of the quest", this.epilog);
    return panel;
  }

  @Override
  protected void update_after_editing(ObjectEditingObject oeo) {
    super.update_after_editing(oeo);
    this.title = oeo.get_string("title");
    this.prolog = oeo.get_string("prolog");
    this.epilog = oeo.get_string("epilog");
  }

  @Override
  public void post_editing_action() {
    if (JOptionPane.showConfirmDialog(null, "Would you like to edit the tasks?", "Edit tasks", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      for (Task task : this.tasks) {
        task.edit();
      }

      while (JOptionPane.showConfirmDialog(null, "Would you like to add a task?", "Add task", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        this.add_task_dialog();
      }
    }
  }

  public void add_task_dialog() {
    ObjectEditingObject oeo = new ObjectEditingObject(null);
    ArrayList<String> supported_tasks = TTDGE.supported_tasks();
    String[] tasks = new String[supported_tasks.size()];
    tasks = supported_tasks.toArray(tasks);
    oeo.add_selection_list("task_type", "Type of the task", "Some instructions", tasks);
    oeo.add_field("target_object_id", "Target object id", "Id of the object to be targeted.", "");
    if (oeo.show() == JOptionPane.OK_OPTION) {
      this.add_task(oeo.get_selection("task_type"), oeo.get_string("target_object_id"));
    }
  }

  protected JSONarray tasks_json() {
    JSONarray array = new JSONarray();
    for (Task task : tasks) {
      array.append(task.save_file_object());
    }
    return array;
  }

  public boolean check() {
    this.done = true;
    for (Task task : tasks) {
      this.done = task.check() && this.done;
    }
    return this.done;
  }

  protected void begin() {
    TTDGE.message("Starting new mission: " + this.title);
    TTDGE.message(this.prolog);
  }

  protected void end() {
    TTDGE.message(this.epilog);
    TTDGE.message("Finished mission: " + this.title);
  }

  @Override
  public String type_name() {
    return "Mission";
  }

  @Override
  public void draw() {
    this.draw(0);
  }

  public float draw(float offset) {
    offset += 20;
    offset = TTDGE.long_text(this.prolog, offset, 13);
    for (Task task : tasks) {
      offset = task.draw(offset);
    }
    if (this.done || TTDGE.debug_mode) {
      offset = TTDGE.long_text(this.epilog, offset, 13);
    }
    return offset;
  }

  @Override
  public void destroy() {
    for (Task task : tasks) {
      task.destroy();
    }
    quest.done_missions.remove(this);
    quest.following_missions.remove(this);
    super.destroy();
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

  public Task add_task(String task_type, String target_object_id) {
    Object object = TTDGE.objects.get(target_object_id);
    if (object == null) {
      TTDGE.error("Object with given id was not found.");
      return null;
    }
    if (task_type.equals("VisitTask")) {
      Room room;
      try {
        room = (Room)object;
      } catch (Exception e) {
        TTDGE.error("Given id must be a room id.");
        return null;
      }
      return new VisitTask(null, this, room);
    }
    else if (task_type.equals("ObtainTask")) {
      Item item;
      try {
        item = (Item)object;
      } catch (Exception e) {
        TTDGE.error("Given id must be a item id.");
        return null;
      }
      return new ObtainTask(null, this, item);
    }
    return null;
  }

}
