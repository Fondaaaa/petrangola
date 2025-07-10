package petrangola.ui;

import java.net.URL;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import petrangola.model.Card;

public class CardButton extends ToggleButton {

    private ImageView imageView;

    public CardButton(String text) {
        super(text);
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        bindSizeTo();
    }

    public void bindSizeTo() {
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Region root = (Region) newScene.getRoot(); // Assumes root is a Region
                imageView.fitWidthProperty().bind(root.widthProperty().divide(3.5));
                imageView.fitHeightProperty().bind(root.heightProperty().divide(3.5));
            }
        });
    }

    public void setCard(Card card) {
        String path = "/resources/images/" + card.getSeed() + (card.getRank().ordinal() + 1) + ".png";
        URL url = getClass().getResource(path.toLowerCase());
        imageView.setImage(new Image( url.toExternalForm()));
    }

    public ImageView getImageView() {
        return this.imageView;
    }
}