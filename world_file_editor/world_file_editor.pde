import ttdge.*;

import java.util.Iterator;

// Modes
final static int ROOM_MODE = 1;
final static int MAP_MODE = 2;
final static int NEW_ROOM_MODE = 3;
final static int NEW_THING_MODE = 4;

int mode = MAP_MODE;

World world;

Thing selected_thing = null;
int click_offset_x = 0;
int click_offset_y = 0;

void setup() {
  size(800, 600, P2D);
  TTDGE.start_engine(this);
  TTDGE.x_offset = 200;
  TTDGE.debug_mode = true;

  //TTDGE.load("foo.json");
  //world = (World)TTDGE.current_object;
  world = new World("First world");
  create_buttons();
}

void draw() {
  background(200);
  if (mode == MAP_MODE) {
    draw_buttons(room_buttons);
  }
  else if (mode == NEW_ROOM_MODE) {
    if (TTDGE.clicked_x > 0 && TTDGE.clicked_y > 0) {
      rect(TTDGE.clicked_x, TTDGE.clicked_y, mouseX - TTDGE.clicked_x, mouseY - TTDGE.clicked_y);
    }
  }
  else if (mode == NEW_THING_MODE) {
    selected_thing.x = mouseX - TTDGE.x_offset;
    selected_thing.y = mouseY - TTDGE.y_offset;
  }
  else if (mode == ROOM_MODE) {
    draw_buttons(thing_buttons);
  }
  else {

    // if (TTDGE.is_key_pressed(38)) TTDGE.y_offset += 3;
    // if (TTDGE.is_key_pressed(37)) TTDGE.x_offset += 3;
    // if (TTDGE.is_key_pressed(40)) TTDGE.y_offset -= 3;
    // if (TTDGE.is_key_pressed(39)) TTDGE.x_offset -= 3;
  }
  TTDGE.draw();
}

void set_mode(int next_mode) {
  if (next_mode == MAP_MODE) {
    TTDGE.current_object = world;
  }
  else if (next_mode == NEW_ROOM_MODE) {
    TTDGE.clicked_x = -1;
    TTDGE.clicked_y = -1;
    TTDGE.pclicked_x = -1;
    TTDGE.pclicked_y = -1;
    TTDGE.current_object = null;
  }
  else if (next_mode == ROOM_MODE) {
    if (mode == MAP_MODE) {
      TTDGE.current_object = selected_thing;
      clear_selection();
    }

  }
  else if (next_mode == NEW_THING_MODE) {

  }
  mode = next_mode;
}

boolean can_select() {
  if (mode == ROOM_MODE || mode == MAP_MODE) {
    return true;
  }
  else {
    return false;
  }
}

void clear_selection() {
  if (selected_thing != null) selected_thing.highlight = false;
  selected_thing = null;
}


void mousePressed() {
  TTDGE.notice_mouse_press();
  TUIelement pressed_ui_element = TTDGE.notice_mouse_press();
  if (pressed_ui_element == null) {
    if (can_select() && handle_thing_selection() != null) {
      // thing selected
    }
    else {
      // Add new room
      if (mode == NEW_ROOM_MODE && TTDGE.pclicked_x > 0 && TTDGE.pclicked_y > 0) {
        new Room(world, null, null, null,
                 TTDGE.clicked_x - TTDGE.pclicked_x,
                 TTDGE.clicked_y - TTDGE.pclicked_y);
        set_mode(MAP_MODE);
      }
      else if (mode == NEW_THING_MODE) {
        set_mode(ROOM_MODE);
      }
    }
  }
}

Thing handle_thing_selection() {
  clear_selection();
  selected_thing = world.pointed_thing();
  if (selected_thing != null) {
    selected_thing.highlight = true;
    if (TTDGE.current_object instanceof World) {
      click_offset_x = (selected_thing.x + TTDGE.x_map_offset) - TTDGE.clicked_x;
      click_offset_y = (selected_thing.y + TTDGE.y_map_offset) - TTDGE.clicked_y;
    }
    else {
      click_offset_x = TTDGE.clicked_x - selected_thing.x - TTDGE.x_offset;
      click_offset_y = TTDGE.clicked_y - selected_thing.y - TTDGE.y_offset;
    }

  }
  return selected_thing;
}

void keyPressed() {
  TTDGE.notice_key_press();
  if (TTDGE.is_key_pressed(CONTROL) && keyCode == 'S') {
    TTDGE.save("foo.json");
  }
  else if (keyCode == 'M') {
    if (mode == MAP_MODE && selected_thing instanceof Room) {
      set_mode(ROOM_MODE);
    }
    else {
      set_mode(MAP_MODE);
    }

  }
  if (selected_thing != null) {
    if (keyCode == 'G') {
      if (selected_thing instanceof Door) {
        // Door other_door = ((Door)selected_thing).linked_door();
        // if (other_door != null) {
        //   selected_room = other_door.room;
        //   TTDGE.current_object = selected_room;
        //   selected_thing.highlight = false;
        //   selected_thing = null;
        // }
      }
      else {
        selected_thing.go(TTDGE.player);
      }
    } else if (keyCode == 'I') {
      selected_thing.investigate(TTDGE.player);
    } else if (keyCode == 'O') {
      selected_thing.open(TTDGE.player);
    } else if (keyCode == 'C') {
      selected_thing.close(TTDGE.player);
    } else if (keyCode == BACKSPACE) {
      selected_thing.destroy();
    }
  }
}

void mouseDragged() {
  if (mode == MAP_MODE && selected_thing instanceof Room) {
    // TODO: click_offset not working correctly
    selected_thing.x = mouseX - TTDGE.x_map_offset + click_offset_x;
    selected_thing.y = mouseY - TTDGE.y_map_offset + click_offset_y;
  }
}

void keyReleased() {
  TTDGE.notice_key_release();
}