package ttdge;

import processing.data.JSONObject;

public class JSON {

  public JSONObject json;

  public JSON() {
    this.json = new JSONObject();
  }

  public JSON(JSONObject json) {
    this.json = json;
  }

  public void save(String file_name) {
    TTDGE.papplet.saveJSONObject(json, file_name);
  }


  // Setters

  public void set(String key, int value) {
    json.setInt(key, value);
  }

  public void set(String key, String value) {
    json.setString(key, value);
  }

  public void set(String key, boolean value) {
    json.setBoolean(key, value);
  }

  public void set(String key, Thing thing) {
    if (thing != null) {
      json.setString(key, thing.id);
    }
  }

  public void set(String key, JSON json) {
    this.json.setJSONObject(key, json.json);
  }

  public void set(String key, JSONarray array) {
    json.setJSONArray(key, array.array);
  }


  // Getters

  public int getInt(String key) {
    return json.getInt(key);
  }

  public int getInt(String key, int default_value) {
    try {
      return json.getInt(key);
    } catch (Exception e) {
      return default_value;
    }
  }

  public String getString(String key) {
    return json.getString(key);
  }

  public String getString(String key, String default_value) {
    try {
      return json.getString(key);
    } catch (Exception e) {
      return default_value;
    }
  }

  public boolean getBoolean(String key) {
    return json.getBoolean(key);
  }

  public boolean getBoolean(String key, boolean default_value) {
    try {
      return json.getBoolean(key);
    } catch (Exception e) {
      return default_value;
    }
  }

  public JSONarray getArray(String key) {
    return new JSONarray(json.getJSONArray(key));
  }

  public JSON getObject(String key) {
    return new JSON(json.getJSONObject(key));
  }

}
