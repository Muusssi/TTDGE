package ttdge;

public class Obstacle extends Thing {

  public Obstacle(World world, String id, String name) {
    super(world, id, name);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void draw() {
    TTDGE.papplet.pushStyle();
    TTDGE.papplet.fill(0);
    TTDGE.papplet.rect(TTDGE.x_offset + room_x*TTDGE.room_grid_size, TTDGE.y_offset + room_y*TTDGE.room_grid_size, TTDGE.room_grid_size, TTDGE.room_grid_size);
    TTDGE.papplet.popStyle();
  }

  @Override
  public String id_prefix() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String world_file_string() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String default_name() {
    // TODO Auto-generated method stub
    return null;
  }

}
