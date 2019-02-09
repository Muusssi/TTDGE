package ttdge;

import processing.data.JSONArray;

public class JSONarray {

  public JSONArray array;

  public JSONarray() {
    this.array = new JSONArray();
  }

  public JSONarray(JSONArray array) {
    this.array = array;
  }

  public JSON get(int i) {
    return new JSON(array.getJSONObject(i));
  }

  public void append(JSON json) {
    array.append(json.json);
  }

  public int size() {
    return array.size();
  }

}
