
void create_buttons() {
  new ObstacleButton();
  new DoorButton();
  new ItemButton();
  new RoomButton();
  new CharacterButton();
}

void draw_buttons(ArrayList<Button> buttons) {
  Iterator<Button> itr = buttons.iterator();
  while (itr.hasNext()) {
    itr.next().draw();
  }
}

ArrayList<Button> room_buttons = new ArrayList<Button>();
ArrayList<Button> thing_buttons = new ArrayList<Button>();


class ObstacleButton extends Button {

  ObstacleButton() {
   super("Obstacle", 10, 100);
   thing_buttons.add(this);
  }

  public void action() {
    selected_thing = new Obstacle(null, null, null, (Room)TTDGE.current_object);
    set_mode(NEW_THING_MODE);
  }
}

class DoorButton extends Button {

  DoorButton() {
   super("Door", 10, 130);
   thing_buttons.add(this);
  }

  public void action() {
    selected_thing = new Door(null, null, null, (Room)TTDGE.current_object);
    set_mode(NEW_THING_MODE);
  }
}

class ItemButton extends Button {

  ItemButton() {
   super("Item", 10, 160);
   thing_buttons.add(this);
  }

  public void action() {
    selected_thing = new Item(null, null, null, (Room)TTDGE.current_object);
    set_mode(NEW_THING_MODE);
  }
}

class CharacterButton extends Button {

  CharacterButton() {
   super("Character", 10, 190);
   thing_buttons.add(this);
  }

  public void action() {
    selected_thing = new GameCharacter(null, null, null, (Room)TTDGE.current_object);
    selected_thing.set_image("TTDGE2.png");
    set_mode(NEW_THING_MODE);
  }
}



public class RoomButton extends Button {

  public RoomButton() {
    super("Room", 20, 100);
    room_buttons.add(this);
  }

  @Override
  public void action() {
    set_mode(NEW_ROOM_MODE);
  }
}
