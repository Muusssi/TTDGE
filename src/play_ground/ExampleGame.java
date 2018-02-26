package play_ground;

import processing.core.PApplet;
import ttdge.Item;
import ttdge.TTDGE;
import ttdge.World;

public class ExampleGame extends PApplet{

  World world;

  public static void main(String[] args) {
    PApplet.main("play_ground.ExampleGame");
  }

  @Override
  public void settings(){
    size(800, 600);
  }

  @Override
  public void setup(){
    TTDGE.start_engine(this);

    world = TTDGE.load_world("test_world.json");
//    world = new World("Maailma");
//    Room starting_room = new Room(world, null, null, null, 6, 8);
//    Room corridor = new Room(world, null, null, null, 3, 9);
//    Room hall = new Room(world, null, null, null, 10, 10);
//    GameCharacter pelaaja = new GameCharacter(world, null, "Pelaaja", null);
//    pelaaja.room = starting_room;
//    Obstacle obs = new Obstacle(world, null, null, null);
//    TTDGE.player.room.set_thing(obs, 2, 2);
//
//    Door door1 = new Door(world, null, null, null);
//    starting_room.set_thing(door1, 1, 1);
//    Door door2 = new Door(world, null, null, null);
//    corridor.set_thing(door2, 1, 1);
//    door1.link_to(door2);
//
//    Door door3 = new Door(world, null, null, null);
//    Door door4 = new Door(world, null, null, null);
//    door3.link_to(door4);
//    corridor.set_thing(door3, 2, 8);
//    hall.set_thing(door4, 1, 1);
//
//    Item key = new Item(world, null, null, null);
//    hall.set_thing(key, 5, 6);

  }

  @Override
  public void draw(){
    background(200);
    TTDGE.draw_active_room();
    TTDGE.player.draw();
    TTDGE.player.move_to_target();
  }

  @Override
  public void mousePressed(){
    TTDGE.player.new_target(mouseX - TTDGE.x_offset, mouseY - TTDGE.y_offset);
  }

  @Override
  public void keyPressed() {
    if (keyCode == 'S') {
      System.out.println("Save");
      world.save("test_world.json");
    }
    else if (keyCode == 'G') {
      TTDGE.player.go_thing_here();
    }
    else if (keyCode == 'O') {
      TTDGE.player.open_thing_here();
    }
    else if (keyCode == 'C') {
      TTDGE.player.close_thing_here();
    }
    else if (keyCode == 'T') {
      TTDGE.player.take_thing_here();
    }
    else if (keyCode == 'P') {
      TTDGE.player.put_thing_here();
    }
    else if (keyCode == 'N') {
      Item item = new Item(world, null, null, null);
      item.put(TTDGE.player);
    }
  }


}
