import tui.*;


void setup() {
  size(512, 512);
  //test_hash_set();
  TUI.aloita(this);
  new World();
  new Room(5, 8);
  //frameRate(10);
  player = new GameCharacter();
  player.room = default_world.rooms.get(0);
  player.room.set_obstacle(new Obstacle(), 3, 3);
  player.room.set_obstacle(new Obstacle(), 4, 3);
  player.room.set_obstacle(new Obstacle(), 2, 5);
  player.room.set_obstacle(new Obstacle(), 3, 5);
  player.room.set_obstacle(new Obstacle(), 0, 6);
  player.room.set_obstacle(new Obstacle(), 1, 6);
  player.room.set_obstacle(new Obstacle(), 2, 6);
  player.room.set_obstacle(new Obstacle(), 3, 6);
}

void draw() {
  draw_active_room();
  move_by_wasd();
  player.draw();
  player.move_to_target();
}


void keyPressed() {
  TUI.huomaa_painallus();
}

void keyReleased() {
  TUI.huomaa_vapautus();
}


void mousePressed() {
  if (SHOW_SEARCHED_POINTS) {
    background(200);
  }
  player.new_target(mouseX - x_offset, mouseY - y_offset);
}


void move_by_wasd() {
  if (TUI.nappain_painettu('W')) {
    player.move_up();
  }
  else if (TUI.nappain_painettu('S')) {
    player.move_down();
  }
  if (TUI.nappain_painettu('A')) {
    player.move_left();
  }
  else if (TUI.nappain_painettu('D')) {
    player.move_right();
  }
}