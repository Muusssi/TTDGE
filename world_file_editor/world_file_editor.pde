import ttdge.*;

// Modes
final static int NORMAL_MODE = 1;
final static int MAP_MODE = 2;
final static int NEW_ROOM_MODE = 3;

int mode = MAP_MODE;

World world;

Thing selected_thing = null;
Room selected_room = null;

ThingButton next_new_thing = null;

void setup() {
  size(800, 600);
  TTDGE.start_engine(this);
  TTDGE.x_offset = 200;
  TTDGE.debug_mode = true;
  world = TTDGE.load_world("test_world.json");
  create_buttons();
}

void draw() {
  background(200);
  if (mode == MAP_MODE) {
    world.draw_map();
  }
  else {
    TTDGE.draw_active_room();
    TTDGE.draw_buttons();

    if (TTDGE.is_key_pressed(38)) TTDGE.y_offset += 3;
    if (TTDGE.is_key_pressed(37)) TTDGE.x_offset += 3;
    if (TTDGE.is_key_pressed(40)) TTDGE.y_offset -= 3;
    if (TTDGE.is_key_pressed(39)) TTDGE.x_offset -= 3;
  }
  TTDGE.draw_buttons();
}


void mousePressed() {
  TUIelement pressed_ui_element = TTDGE.notice_mouse_press();
  if (pressed_ui_element == null) {
    if (next_new_thing != null && mode == NORMAL_MODE) {
      next_new_thing.set();
    } else {
      handle_thing_selection();
    }
  }
}

void handle_thing_selection() {
  if (selected_room != null) selected_room.highlight = false;
  if (selected_thing != null) selected_thing.highlight = false;
  if (mode == MAP_MODE) {
    selected_room = world.pointed_thing_on_map();
    if (selected_room != null) {
      selected_room.highlight = true;
      TTDGE.active_room = selected_room;
    }
  }
  else {
    selected_thing = TTDGE.active_room.pointed_thing();
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
    world.save("test_world.json");
  }
  if (keyCode == 'M') {
    if (mode == NORMAL_MODE) {
      mode = MAP_MODE;
    }
    else {
     mode = NORMAL_MODE;
    }
  }
  if (selected_thing != null) {
    if (keyCode == 'G') {
      if (selected_thing instanceof Door) {
        Door other_door = ((Door)selected_thing).linked_door;
        if (other_door != null) {
          selected_room = other_door.room;
          TTDGE.active_room = selected_room;
          selected_thing.highlight = false;
          selected_thing = null;
        }
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

void mouseDragged() {
  if (mode == MAP_MODE && selected_room != null) {
    selected_room.world_map_x = (mouseX - TTDGE.x_map_offset)/TTDGE.map_grid_size;
    selected_room.world_map_y = (mouseY - TTDGE.y_map_offset)/TTDGE.map_grid_size;
  }
}

void keyReleased() {
  TTDGE.notice_key_release();
}