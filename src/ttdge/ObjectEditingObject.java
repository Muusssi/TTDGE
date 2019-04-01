package ttdge;

import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ObjectEditingObject {

  JPanel panel = new JPanel();
  JComboBox<String> Task_type_list = null;
  TTDGEObject object = null;

  HashMap<String,JTextField> fields = new HashMap<String,JTextField>();
  HashMap<String,JComboBox<String>> selection_lists = new HashMap<String,JComboBox<String>>();
  HashMap<String,Integer> original_integer_values = new HashMap<String,Integer>();

  public ObjectEditingObject(TTDGEObject object) {
    this.object = object;
    panel.setLayout(new GridLayout(10,0));
  }

  public void add_field(String field_name, String label, String tool_tip, int current_value) {
    add_field(field_name, label, tool_tip, Integer.toString(current_value));
    original_integer_values.put(field_name, current_value);
  }

  public void add_field(String field_name, String label, String tool_tip, String current_value) {
    JLabel jlabel = new JLabel(label);
    if (tool_tip != null) {
      jlabel.setToolTipText(tool_tip);
    }
    panel.add(jlabel);
    JTextField input_field = new JTextField(0);
    input_field.setText(current_value);
    fields.put(field_name, input_field);
    panel.add(input_field);
  }

  public int show() {
    String title;
    if (object != null) {
      title = "Edit " + this.object.type_name() + ": " + this.object.id;
    }
    else {
      title = "Edit object";
    }
    return JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION);
  }

  public String get_string(String field_name) {
    return fields.get(field_name).getText();
  }

  public int get_int(String field_name) {
    try {
      return Integer.parseInt(fields.get(field_name).getText());
    }
    catch (Exception e) {
      return original_integer_values.get(field_name);
    }
  }

  public void add_selection_list(String field_name, String label, String tool_tip, String[] options) {
    JLabel jlabel = new JLabel(label);
    if (tool_tip != null) {
      jlabel.setToolTipText(tool_tip);
    }
    panel.add(jlabel);
    JComboBox<String> selection_list = new JComboBox<String>(options);
    selection_list.setSelectedIndex(0);
    selection_lists.put(field_name, selection_list);
    panel.add(selection_list);
  }

  public String get_selection(String field_name) {
    return (String)selection_lists.get(field_name).getSelectedItem();
  }

}
