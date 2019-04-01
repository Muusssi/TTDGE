package ttdge;


public abstract class Task extends TTDGEObject {

  public boolean done = false;
  public Mission mission = null;

  public Task(String id, Mission mission) {
    super(id);
    this.mission = mission;
    mission.tasks.add(this);
  }

  public Task(JSON json, Mission mission) {
    super(json);
    this.mission = mission;
    mission.tasks.add(this);
    this.done = json.getBoolean("done");
  }

  @Override
  protected JSON save_file_object() {
    JSON json = super.save_file_object();
    json.set("done", this.done);
   return json;
  }

  public abstract boolean check();

  public abstract String instructions();

  @Override
  public void destroy() {
    mission.tasks.remove(this);
    super.destroy();
  }

  @Override
  public void draw() {
    this.draw(0);
  }

  public float draw(float offset) {
    String done_text = "";
    if (this.done) {
      done_text = "Done";
    }
    offset = TTDGE.long_text("- " + this.instructions() + done_text, offset, 12);
    return offset;
  }

  @Override
  public void draw_on_parent() {}

  @Override
  public boolean is_pointed() {
    return false;
  }

  @Override
  public boolean is_pointed_on_parent() {
    return false;
  }

  @Override
  public Thing pointed_thing() {
    return null;
  }

}
