package sistema_inteligente;

import javafx.util.StringConverter;

import java.util.Map;

public class MapConverter extends StringConverter<Integer> {

  private final Map<Integer, String> values;

  public MapConverter(Map<Integer, String> values) {
    this.values = values;
  }

  @Override
  public String toString(Integer object) {
    return values.get(object);
  }

  @Override
  public Integer fromString(String string) {
    return values.entrySet()
        .stream()
        .filter(entry -> entry.getValue().equals(string))
        .findFirst()
        .map(Map.Entry::getKey)
        .orElse(null);
  }
}
