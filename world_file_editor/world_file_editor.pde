import ttdge.*;

World world;

boolean map_view = false;

Room active_room = null;
Thing selected_thing = null;
Room selected_room = null;

void setup() {
  size(800, 600);
  TTDGE.start_engine(this);
  TTDGE.debug_mode = true;
  world = TTDGE.load_world("../test_world.json");
  TTDGE.player.image = loadImage("TTDGE1.png");
}

void draw() {
  background(200);
  if (map_view) {
    world.draw_map();
  } else {
    TTDGE.draw_active_room();
    TTDGE.player.draw();
    //TTDGE.player.move_to_target();
    TTDGE.draw_buttons();

    if (TTDGE.is_key_pressed('W')) TTDGE.y_offset += 3;
    if (TTDGE.is_key_pressed('A')) TTDGE.x_offset += 3;
    if (TTDGE.is_key_pressed('S')) TTDGE.y_offset -= 3;
    if (TTDGE.is_key_pressed('D')) TTDGE.x_offset -= 3;
  }
}


void mousePressed() {
  TTDGE.notice_mouse_press();
  if (selected_room != null) selected_room.highlight = false;
  if (selected_thing != null) selected_thing.highlight = false;
  if (map_view) {
    selected_room = world.pointed_thing_on_map();
    if (selected_room != null) {
      selected_room.highlight = true;
      TTDGE.active_room = selected_room;
    }
  } else {
    selected_thing = TTDGE.player.room.pointed_thing();
    println(selected_thing);
    if (selected_thing != null) {
      selected_thing.highlight = true;
      if (selected_thing.type_name().equals("Room")) {
        selected_room = (Room)selected_thing;
        TTDGE.active_room = selected_room;
      }
    }
  }
}

void keyPressed() {
  TTDGE.notice_key_press();
  if (TTDGE.is_key_pressed(CONTROL) && keyCode == 'S') {
    System.out.println("Save");
    world.save("../test_world.json");
  }
  if (keyCode == 'M') {
    map_view = !map_view;
  }
  if (selected_thing != null) {
    if (keyCode == 'G') {
      if (selected_thing.type_name().equals("Door")) {
        selected_room = ((Door)selected_thing).linked_door.room;
        selected_thing = null;
      }
      else {
        selected_thing.go(TTDGE.player);
      }
    } else if (keyCode == 'O') {
      selected_thing.open(TTDGE.player);
    } else if (keyCode == 'C') {
      selected_thing.close(TTDGE.player);
    }
  }
}

void keyReleased() {
  TTDGE.notice_key_release();
}