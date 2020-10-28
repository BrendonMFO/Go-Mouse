package sistema_inteligente;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Sprite extends ImageView {

  private final ObjectProperty<Integer> position;

  private final Integer linhas;

  private final Integer colunas;

  private final Duration tempoFrame;

  private SpriteSheet[] spriteSheet;

  private Timeline frameTimeline;

  public Sprite(String image, int linhas, int colunas) {
    this.linhas = linhas;
    this.colunas = colunas;
    this.tempoFrame = Duration.seconds(0.18D);
    this.position = new SimpleObjectProperty<>();
    this.readSpriteSheetJson("mouse_sprite_sheet.json");
    this.makeFrameViewport(0);
    this.setImage(new Image(App.class.getResourceAsStream(image)));
    this.createFrameAnimation(0);
  }

  public void bindPosition(ObjectProperty<Integer> property) {
    position.bind(property);
  }

  public void animationY(double toPositionY, EventHandler<ActionEvent> value) {
    double abs = Math.abs(getTranslateY() - toPositionY);
    int move = toPositionY > getTranslateY() ? 5 : -5;
    createFrameAnimation(toPositionY > getTranslateY() ? 0 : 12);
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.06D), event -> {
      if (abs != 0) {
        setTranslateY(getTranslateY() + move);
      }
    }));
    timeline.setOnFinished(value);
    timeline.setCycleCount((int) (abs / 5));
    timeline.play();
  }

  public void animationX(double toPositionX, EventHandler<ActionEvent> value) {
    createFrameAnimation(8);
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.06D), event -> {
      setTranslateX(getTranslateX() + 5);
    }));
    timeline.setOnFinished(value);
    timeline.setCycleCount((int) Math.abs(toPositionX - getTranslateX()) / 5);
    timeline.play();
  }

  private void createFrameAnimation(int initIndex) {
    if (frameTimeline != null) {
      frameTimeline.stop();
    }
    IntegerProperty column_position = new SimpleIntegerProperty(initIndex);
    BooleanProperty revert = new SimpleBooleanProperty(false);
    frameTimeline = new Timeline(new KeyFrame(tempoFrame, (event) -> {
      setViewport(makeFrameViewport(column_position.get()));
      if (column_position.get() == initIndex + 3) {
        revert.set(false);
      }
      if (column_position.get() == initIndex) {
        revert.set(true);
      }
      column_position.set(revert.get() ? column_position.get() + 1 : column_position.get() - 1);
    }));
    frameTimeline.setCycleCount(Animation.INDEFINITE);
    frameTimeline.play();
  }

  private void readSpriteSheetJson(String jsonFile) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      InputStream is = getClass().getResourceAsStream(jsonFile);
      this.spriteSheet = mapper.readValue(is, SpriteSheet[].class);
    } catch (IOException ex) {
    }
  }

  private Rectangle2D makeFrameViewport(int index) {
    return new Rectangle2D(
      Integer.parseInt(spriteSheet[index].getX()), 
      Integer.parseInt(spriteSheet[index].getY()),
      Integer.parseInt(spriteSheet[index].getWidth()), 
      Integer.parseInt(spriteSheet[index].getHeight())
    );
  }
}
