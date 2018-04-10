package iat265.aga53;

import processing.core.PApplet;

/**
 *
 * @author ankit
 */
public class Head extends AnimalComponent {

    private final AnimalComponent parent;

    public Head(PApplet p, String name, AnimalComponent parent) {
        super(p, name, parent.pos.x + parent.lengthVector.x, parent.pos.y + parent.lengthVector.y,
                50, 20, 0);
        this.parent = parent;
    }

    @Override
    public void drawShape() {
        p.pushStyle();
        p.fill(c);
        p.pushMatrix();
        p.translate(pos.x, pos.y);
        p.rotate(rot);
        p.stroke(c);
        p.strokeWeight(branchWidth);
        p.ellipse(- branchLength/2, -branchLength/2, branchLength, branchLength);
        p.popMatrix();
        p.popStyle();
    }

    @Override
    public void updatePosition(float x, float y) {
        pos.x = parent.pos.x + parent.lengthVector.x;
        pos.y = parent.pos.y + parent.lengthVector.y;
    }

}
