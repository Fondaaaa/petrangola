package petrangola.ui;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import petrangola.model.Card;

public class CardButton extends ToggleButton {

    private Card card;
    private ImageView imageView;

    public CardButton(String text, Card card) {
        super(text);
        this.card = card;
        this.imageView = new ImageView(new Image(card.getImageURL().toExternalForm()));

    }

    public void setCard(Card card) {
       this.card = card;
       this.imageView.setImage(new Image(card.getImageURL().toExternalForm()));
    }

    public ImageView getImageView() {
        return this.imageView;
    }
}