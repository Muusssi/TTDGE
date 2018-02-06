package play_ground;

import processing.core.PApplet;
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
    world = TTDGE.load_world("test_world.twf");
//    world = new World("Maailma");
//    Room starting_room = new Room(world, null, null, 6, 8);
//    new Room(world, null, null, 3, 9);
//    new Room(world, null, null, 10, 10);
//    GameCharacter pelaaja = new GameCharacter(world, null, "Pelaaja");
//    pelaaja.room = starting_room;
//    Obstacle obs = new Obstacle(world, null, null);
//    TTDGE.player.room.set_obstacle(obs, 2, 2);
//    Door door1 = new Door(world, null, null);
//    TTDGE.player.room.set_thing(door1, 1, 1);
//    Door door2 = new Door(world, null, null);
//    Room r4 = (Room)world.things.get("Room-4");
//    r4.set_thing(door2, 1, 1);
//    door1.link_to(door2);

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
    if (keyCode == 'L') {
      System.out.println("Load");
      world = TTDGE.load_world("/Users/tommioinonen/Documents/TTDGE/test_world.twf");
    }
    else if (keyCode == 'S') {
      System.out.println("Save");
      world.save("test_world.twf");
    }
    else if (keyCode == 'G') {
      TTDGE.player.go_here();
    }
    else if (keyCode == 'O') {
      TTDGE.player.open_here();
    }
  }


}
