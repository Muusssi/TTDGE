package ttdge;

import processing.core.PConstants;

public abstract class Button extends TUIelement {

  public int x, y;
  public int width;
  public int height = 30;
  public String text;

  public int background_r = 255;
  public int background_g = 255;
  public int background_b = 255;

  public int active_background_r = 230;
  public int active_background_g = 230;
  public int active_background_b = 230;

  public int text_r = 0;
  public int text_g = 0;
  public int text_b = 0;

  public boolean stay_down = false;
  public boolean pressed = false;
  public int pressed_on_frame = -1;
  public int drawn_on_frame = -1;


  public Button(String text, int x, int y) {
      this.text = text;
      this.x = x;
      this.y = y;
      if (text.length() < 4) {
          this.width = 40;
      }
      else {
          this.width = text.length()*10;
      }
      TTDGE.buttons.add(this);
  }

  public boolean cursor_points() {
      if (TTDGE.papplet.mouseX >= x && TTDGE.papplet.mouseX <= x + width &&
          TTDGE.papplet.mouseY >= y && TTDGE.papplet.mouseY <= y + height) {
          return true;
      }
      else {
          return false;
      }
  }

  public void draw() {
    drawn_on_frame = TTDGE.papplet.frameCount;
    TTDGE.papplet.pushStyle();
    if (pressed || cursor_points()){
        if (pressed || TTDGE.papplet.mousePressed) {
          TTDGE.papplet.fill(text_r, text_g, text_b);
        }
        else {
          TTDGE.papplet.fill(active_background_r, active_background_g, active_background_b);
        }
    }
    else {
      TTDGE.papplet.fill(background_r, background_g, background_b);
    }
    TTDGE.papplet.stroke(text_r, text_g, text_b);
    TTDGE.papplet.rect(x, y, width, height, 10);

    if (pressed || (cursor_points() && TTDGE.papplet.mousePressed)) {
      TTDGE.papplet.stroke(active_background_r, active_background_g, active_background_b);
      TTDGE.papplet.fill(active_background_r, active_background_g, active_background_b);
    }
    else {
      TTDGE.papplet.stroke(text_r, text_g, text_b);
      TTDGE.papplet.fill(text_r, text_g, text_b);
    }

    TTDGE.papplet.textAlign(PConstants.CENTER, PConstants.CENTER);
    TTDGE.papplet.text(text, x + width/2, y + height/2);
    TTDGE.papplet.popStyle();
  }

  public void background_color(int r, int g, int b) {
      this.background_r = r;
      this.background_g = g;
      this.background_b = b;
  }

  public void background_color(int c) {
    background_color(c, c, c);
  }

  public void active_background_color(int r, int g, int b) {
      this.active_background_r = r;
      this.active_background_g = g;
      this.active_background_b = b;
  }

  public void text_color(int r, int g, int b) {
      this.text_r = r;
      this.text_g = g;
      this.text_b = b;
  }

  public void text_color(int c) {
    text_color(c, c, c);
  }

  public void press() {
    if (pressed_on_frame != TTDGE.papplet.frameCount) {
      if (stay_down) {
        if (pressed) {
          pressed = false;
          release_action();
        }
        else {
          pressed = true;
          action();
        }
      }
      else {
        action();
      }
    }
    pressed_on_frame = TTDGE.papplet.frameCount;
  }

  /**
   * This is the method that should be implemented
   */
  public abstract void action();

  public void release_action() {}

}
