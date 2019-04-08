package ttdge;


public class Door extends Thing {

  public Room room;
  public String linked_door_id;

  public Door(String id, String name, String description, Room room) {
    super(id, name, description);
    this.room = room;
    this.room.add_thing(this);
  }

  public Door(JSON json, Room room) {
    super(json);
    this.room = room;
    this.room.add_thing(this);
    this.linked_door_id = json.getString("linked_door");
  }

  @Override
  protected JSON save_file_object() {
    JSON json = super.save_file_object();
    json.set("linked_door", this.linked_door_id);
    return json;
  }

  @Override
  protected ObjectEditingObject get_editing_panel() {
    ObjectEditingObject panel = super.get_editing_panel();
    panel.add_field("linked_door", "Linked door", "ID of the door that this door is linked to.", this.linked_door_id);
    return panel;
  }

  @Override
  protected void update_after_editing(ObjectEditingObject oeo) {
    super.update_after_editing(oeo);
    this.linked_door_id = oeo.get_string("linked_door");
  }

  @Override
  public void destroy() {
    super.destroy();
    if (this.room != null) {
      this.room.remove_thing(this);
    }
    Door linked_door = this.linked_door();
    if (linked_door != null) {
      linked_door.linked_door_id = null;
    }
  }

  public void link_with(Door other_door) {
    this.linked_door_id = other_door.id;
    other_door.linked_door_id = this.id;
  }

  public Door linked_door() {
    Door linked_door = null;
    if (TTDGE.objects.containsKey(this.linked_door_id)) {
      linked_door = (Door)TTDGE.objects.get(this.linked_door_id);
    }
    else {
      this.linked_door_id = null;
    }
    return linked_door;
  }

  @Override
  public void default_action(GameCharacter game_character) {
    this.go(game_character);
  }

  @Override
  public void go(GameCharacter game_character) {
    Door linked_door = this.linked_door();
    if (linked_door != null) {
      game_character.enter(linked_door);
    }
    else {
      TTDGE.message("This useless passage doesn't go anywhere.");
    }
  }

//  @Override
//  public void open(GameCharacter game_character) {
//    this.closed = false;
//    Door linked_door = this.linked_door();
//    if (linked_door != null) {
//      linked_door.closed = false;
//    }
//  }
//
//  @Override
//  public void close(GameCharacter game_character) {
//    this.closed = true;
//    if (this.linked_door != null) {
//      this.linked_door.closed = false;
//    }
//  }

  @Override
  public String default_name() {
    return "Door";
  }

  @Override
  public String type_name() {
    return "Door";
  }



  @Override
  public void draw() {
    TTDGE.papplet.pushStyle();
    TTDGE.papplet.fill(102, 51, 0);
    if (this.highlight) {
      TTDGE.papplet.stroke(255, 0, 0);
      TTDGE.papplet.strokeWeight(3);
    }
    TTDGE.papplet.rect(TTDGE.x_offset + x - this.radius, TTDGE.y_offset + y - this.radius,
                       this.radius*2, this.radius*2);
    if (TTDGE.debug_mode) {
      TTDGE.papplet.fill(255, 0, 0);
      if (this.linked_door() != null) {
        TTDGE.papplet.ellipse(TTDGE.x_offset + this.x, TTDGE.y_offset + this.y, 10, 10);
      }
      TTDGE.papplet.stroke(0, 255, 0);
      TTDGE.papplet.text(this.id, TTDGE.x_offset + x, TTDGE.y_offset + y);
    }
    TTDGE.papplet.popStyle();
  }

  @Override
  public void draw_on_parent() {
    TTDGE.papplet.pushStyle();
    TTDGE.papplet.stroke(0);
    TTDGE.papplet.fill(102, 51, 0);
    TTDGE.papplet.rect(
        TTDGE.x_map_offset + room.x + (x - this.radius)/TTDGE.map_grid_size,
        TTDGE.y_map_offset + room.y + (y - this.radius)/TTDGE.map_grid_size,
        2*this.radius/TTDGE.map_grid_size, 2*this.radius/TTDGE.map_grid_size);
        Door linked_door = this.linked_door();
        if (linked_door != null) {
          TTDGE.papplet.stroke(255, 0, 0);
          TTDGE.papplet.line(TTDGE.x_map_offset + room.x + this.x/TTDGE.map_grid_size,
                             TTDGE.y_map_offset + room.y + this.y/TTDGE.map_grid_size,
                             TTDGE.x_map_offset + linked_door.room.x + linked_door.x/TTDGE.map_grid_size,
                             TTDGE.y_map_offset + linked_door.room.y + linked_door.y/TTDGE.map_grid_size);
        }
    TTDGE.papplet.popStyle();
  }

  @Override
  public String default_description() {
    return "It is a door. I wonder where it goes...";
  }

  @Override
  public boolean is_pointed_on_parent() {
    return false;
  }



}
