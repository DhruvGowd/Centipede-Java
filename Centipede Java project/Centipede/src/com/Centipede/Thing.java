package com.Centipede;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Thing {

	protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean visible;
    protected Image image;
    
    public Thing(int x, int y) {
    	this.x = x;
    	this.y = y;
    	visible = true;
    }
    
    public void loadImage(String imageSrc) {
    	ImageIcon i = new ImageIcon(imageSrc);
    	image = i.getImage();
    }
    
    public Image getImage() {
        return image;
    }
    
    protected void getImageDimensions() {

        width = image.getWidth(null);
        height = image.getHeight(null);
    } 
    
    public Rectangle getBounds() {
    	return new Rectangle(x, y, width, height);
    }
    
    public void setDimensions(int x, int y) {
    	this.width  = x;
    	this.height = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
}
