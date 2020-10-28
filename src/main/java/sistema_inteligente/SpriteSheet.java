package sistema_inteligente;

public class SpriteSheet {
  private String height;
  private String width;
  private String name;
  private String y;
  private String x;

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getY() {
    return y;
  }

  public void setY(String y) {
    this.y = y;
  }

  public String getX() {
    return x;
  }

  public void setX(String x) {
    this.x = x;
  }

  @Override
  public String toString() {
    return "ClassPojo [height = " + height + ", width = " + width + ", name = " + name + ", y = " + y + ", x = " + x
        + "]";
  }
}