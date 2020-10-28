package sistema_inteligente;

public interface IConfigurationChange {

  void onPosicaoRatoChange(Integer oldValue, Integer newValue);

  void onPosicaoQueijoChange(Integer oldValue, Integer newValue);

}
