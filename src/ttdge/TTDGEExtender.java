package ttdge;

public abstract class TTDGEExtender {

  public abstract Room load_custom_room(World world, JSON json);

  public abstract Thing load_custom_thing(Room room, JSON json);

}
