package sistema_inteligente;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Objects;
import java.util.Optional;

public class BoardGridPane extends GridPane {

  private static final int COL_ROW = 10;

  public BoardGridPane() {
    super();
    drawBoard();
  }

  public Optional<Node> getNodeInColRow(Integer col, Integer row) {
    return getChildren().stream()
        .filter(node -> Objects.equals(getColumnIndex(node), col) && Objects.equals(getRowIndex(node), row))
        .findFirst();
  }

  public void clearCellInColRow(Integer col, Integer oldValue) {
    getNodeInColRow(col, oldValue - 1).ifPresent(node1 -> {
      StackPane pane = ((StackPane) node1);
      pane.getChildren().removeAll(pane.getChildren());
    });
  }

  public void setNodeColor(Integer col, Integer row, String color) {
    getNodeInColRow(col, row).ifPresent(node -> {
      node.setStyle("-fx-background-color: " + color);
    });
  }

  private void drawBoard() {
    for (int i = 0; i < COL_ROW; i++) {
      for (int j = 0; j < COL_ROW; j++) {
        StackPane stackPane = new StackPane();
        stackPane.setStyle((i + j) % 2 == 0 ? "-fx-background-color: #7b6817" : "fx-background-color: #fff");
        add(stackPane, i, j);
        stackPane.toBack();
      }
    }
  }
}
