package sistema_inteligente;

import javafx.beans.property.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Configuration implements Serializable {

  private static final long serialVersionUID = 1L;

  public enum CONFIGS {
    POSICAO_RATO, TAXA_MUTACAO, TAXA_POPULACAO, QUANT_GERACOES, POSICAO_QUEIJO, TAXA_CRUZAMENTO, MECANISMO_SELECAO
  }

  private final Map<CONFIGS, SimpleObjectProperty<Integer>> configs = new HashMap<>(6);

  public Configuration() {
    configs.put(CONFIGS.POSICAO_RATO, new SimpleObjectProperty<>());
    configs.put(CONFIGS.TAXA_MUTACAO, new SimpleObjectProperty<>());
    configs.put(CONFIGS.TAXA_POPULACAO, new SimpleObjectProperty<>());
    configs.put(CONFIGS.QUANT_GERACOES, new SimpleObjectProperty<>());
    configs.put(CONFIGS.POSICAO_QUEIJO, new SimpleObjectProperty<>());
    configs.put(CONFIGS.TAXA_CRUZAMENTO, new SimpleObjectProperty<>());
    configs.put(CONFIGS.MECANISMO_SELECAO, new SimpleObjectProperty<>());
  }

  public Integer getConfigValue(CONFIGS config) {
    return configs.get(config).getValue();
  }

  public void configBind(CONFIGS config, ObjectProperty<Integer> property) {
    configs.get(config).bindBidirectional(property);
  }

  private void readObject(ObjectInputStream inputStream) {
    configs.values().forEach(v -> {
      try {
        v.setValue(inputStream.readInt());
      } catch (IOException e) {
      }
    });
  }

  private void writeObject(ObjectOutputStream outputStream) {
    configs.values().forEach(v -> {
      try {
        outputStream.writeInt(v.get());
      } catch (IOException e) {
      }
    });
  }
}
