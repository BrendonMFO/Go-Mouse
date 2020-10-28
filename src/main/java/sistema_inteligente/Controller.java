package sistema_inteligente;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import sistema_inteligente.Configuration.CONFIGS;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.control.Button;

public class Controller implements Initializable, IConfigurationChange {

  private Sprite mouseSprite;
  private final Configuration configuration = new Configuration();
  private final Image queijoImage = new Image(App.class.getResourceAsStream("Queijo.png"));
  private final Map<Integer, String> mecanismoSelecao = new HashMap<>(6);

  @FXML
  private Button btnInit;
  @FXML
  private BoardGridPane gridPane;
  @FXML
  private Spinner<Integer> cfgPosicaoRatoField, cfgPosicaoQueijoField;
  @FXML
  private ComboBox<Integer> cfgQuantGeracao, cfgTamanhoPopulacaoBox, cfgQuantGeracoes, cfgTaxaCruzamentoBox,
      cfgTaxaMutacaoBox, cfgSelecaoBox;

  @FXML
  private void executarSimulacao() {
    positionSprite();
    enableDisableControls(true);
    Simulacao simulacao = new Simulacao(configuration);
    Individuo individuo = simulacao.iniciar();
    animar(individuo, 1);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeSprites();
    initializeMap();
    bindMethods();
    bindValues();
    initializeValues();
    positionSprite();
  }

  @Override
  public void onPosicaoRatoChange(Integer oldValue, Integer newValue) {
    Node node = gridPane.getNodeInColRow(0, newValue - 1).get();
    mouseSprite.setTranslateY(node.getLayoutY());
    mouseSprite.setTranslateX(node.getLayoutX());
  }

  @Override
  public void onPosicaoQueijoChange(Integer oldValue, Integer newValue) {
    if (oldValue != null) {
      gridPane.clearCellInColRow(9, oldValue);
    }
    if (newValue != null) {
      gridPane.getNodeInColRow(9, newValue - 1).ifPresent(node1 -> {
        StackPane pane = ((StackPane) node1);
        pane.getChildren().add(new ImageView(queijoImage));
      });
    }
  }

  private void enableDisableControls(Boolean b) {
    btnInit.disableProperty().set(b);
    cfgPosicaoRatoField.disableProperty().set(b);
    cfgPosicaoQueijoField.disableProperty().set(b);
  }

  private void initializeSprites() {
    mouseSprite = new Sprite("mousesprite.png", 4, 4);
    gridPane.getNodeInColRow(0, 0).ifPresent(node1 -> {
      StackPane pane = ((StackPane) node1);
      pane.getChildren().add(mouseSprite);
    });
  }

  private void initializeMap() {
    mecanismoSelecao.put(1, "Rank");
    mecanismoSelecao.put(2, "Torneio");
    mecanismoSelecao.put(3, "Rank + Torneio");
  }

  private void initializeValues() {
    cfgSelecaoBox.setValue(1);
    cfgTaxaMutacaoBox.setValue(1);
    cfgTaxaCruzamentoBox.setValue(85);
    cfgTamanhoPopulacaoBox.setValue(100);
    cfgQuantGeracao.setValue(100);
    cfgQuantGeracao.setItems(FXCollections.observableArrayList(100, 500, 1000, 5000));
    cfgPosicaoRatoField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
    cfgPosicaoQueijoField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
    cfgTamanhoPopulacaoBox.setItems(FXCollections.observableArrayList(100, 500, 1000, 5000));
    cfgTaxaCruzamentoBox.setItems(FXCollections.observableArrayList(85, 90));
    cfgTaxaMutacaoBox.setItems(FXCollections.observableArrayList(1, 3, 5));
    cfgSelecaoBox.setItems(FXCollections.observableArrayList(mecanismoSelecao.keySet()));
    cfgSelecaoBox.setConverter(new MapConverter(mecanismoSelecao));
  }

  private void bindMethods() {
    cfgPosicaoRatoField.valueProperty()
        .addListener((observable, oldValue, newValue) -> onPosicaoRatoChange(oldValue, newValue));
    cfgPosicaoQueijoField.valueProperty()
        .addListener((observable, oldValue, newValue) -> onPosicaoQueijoChange(oldValue, newValue));
  }

  private void bindValues() {
    configuration.configBind(CONFIGS.POSICAO_RATO, (ObjectProperty<Integer>) cfgPosicaoRatoField.valueProperty());
    configuration.configBind(CONFIGS.POSICAO_QUEIJO, (ObjectProperty<Integer>) cfgPosicaoQueijoField.valueProperty());
    configuration.configBind(CONFIGS.QUANT_GERACOES, cfgQuantGeracao.valueProperty());
    configuration.configBind(CONFIGS.TAXA_POPULACAO, cfgTamanhoPopulacaoBox.valueProperty());
    configuration.configBind(CONFIGS.TAXA_CRUZAMENTO, cfgTaxaCruzamentoBox.valueProperty());
    configuration.configBind(CONFIGS.TAXA_MUTACAO, cfgTaxaMutacaoBox.valueProperty());
    configuration.configBind(CONFIGS.MECANISMO_SELECAO, cfgSelecaoBox.valueProperty());
  }

  private void animar(Individuo individuo, int index) {
    if (index == 10) {
      Integer position = individuo.getPositions()[index - 1];
      double layoutY = gridPane.getNodeInColRow(index - 1, position - 1).get().getLayoutY();
      mouseSprite.animationY(layoutY, a -> {
        enableDisableControls(false);
      });
      return;
    }
    Integer position = individuo.getPositions()[index - 1];
    double layoutX = gridPane.getNodeInColRow(index, position - 1).get().getLayoutX();
    double layoutY = gridPane.getNodeInColRow(index, position - 1).get().getLayoutY();
    mouseSprite.animationX(layoutX, event -> {
      mouseSprite.animationY(layoutY, a -> {
        animar(individuo, index + 1);
      });
    });
  }

  private void positionSprite() {
    gridPane.getNodeInColRow(0, configuration.getConfigValue(CONFIGS.POSICAO_RATO) - 1).ifPresent(node1 -> {
      mouseSprite.setTranslateX(node1.getLayoutX());
      mouseSprite.setTranslateY(node1.getLayoutY());
    });
  }
}
