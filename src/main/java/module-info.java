module sistema_inteligente {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.fasterxml.jackson.databind;

  opens sistema_inteligente to javafx.fxml;

  exports sistema_inteligente;
}