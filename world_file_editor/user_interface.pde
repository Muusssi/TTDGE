
ArrayList<ThingButton> thing_buttons = new ArrayList<ThingButton>();

abstract class ThingButton extends Button {

  ThingButton(String text, int x, int y) {
   super(text, x, y);
   this.stay_down = true;
   thing_buttons.add(this);
  }

  @Override
  public void action() {
    for (ThingButton btn : thing_buttons) {
      btn.pressed = false;
    }
    next_new_thing = this;
    this.pressed = true;
  }

  public void release_action() {
    next_new_thing = null;
  }

  public void set() {
    Thing pointed_thing = TTDGE.active_room.pointed_thing();
    if (pointed_thing == null || pointed_thing.type_name() == "Room") {
      set_new_thing();
    }
    //next_new_thing = null;
    //this.pressed = false;
  }

  public abstract void set_new_thing();
}


class ObstacleButton extends ThingButton {

  ObstacleButton() {
   super("Obstacle", 10, 100);
  }

  public void set_new_thing() {
    TTDGE.active_room.set_thing(
      new Obstacle(world, null, null, null),
      TTDGE.active_room.pointed_cell_x(),
      TTDGE.active_room.pointed_cell_y()
    );
  }
}

class DoorButton extends ThingButton {

  DoorButton() {
   super("Door", 10, 130);
  }

  public void set_new_thing() {
    TTDGE.active_room.set_thing(
      new Door(world, null, null, null),
      TTDGE.active_room.pointed_cell_x(),
      TTDGE.active_room.pointed_cell_y()
    );
  }
}

class ItemButton extends ThingButton {

  ItemButton() {
   super("Item", 10, 160);
  }

  public void set_new_thing() {
    TTDGE.active_room.set_thing(
      new Item(world, null, null, null),
      TTDGE.active_room.pointed_cell_x(),
      TTDGE.active_room.pointed_cell_y()
    );
  }
}


