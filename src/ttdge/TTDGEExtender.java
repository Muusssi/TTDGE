package ttdge;

import java.util.ArrayList;

public abstract class TTDGEExtender {

  public abstract Room load_custom_room(World world, JSON json);

  public abstract Thing load_custom_thing(Room room, JSON json);

  public abstract Task load_custom_task(Mission mission, JSON json);
  public abstract ArrayList<String> custom_tasks();

}
