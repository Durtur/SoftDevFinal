package model;

import javafx.scene.image.Image;

/**
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class Offer {
    private Image image;
    private String text;
    private String destination;
    private String price;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Offer(Image image, String text, String destination, String price) {
        this.image = image;
        this.text = text;
        this.destination = destination;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
