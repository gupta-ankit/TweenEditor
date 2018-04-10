package iat265.aga53;

import processing.core.PApplet;

/**
 *
 * @author ankit
 */
public class Neck extends AnimalComponent {

    AnimalComponent parent;

    public Neck(PApplet p, String name, AnimalComponent parent, float angle) {
        super(p, name, parent.pos.x, parent.pos.y, .2f * parent.branchLength, 20f, parent.rot + p.radians(angle));
        this.parent = parent;
    }

    @Override
    public void updatePosition(float x, float y) {
        this.branchLength = .2f * parent.branchLength;
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
        p.line(0, 0, 0, -branchLength);
        p.popMatrix();
        p.popStyle();
    }

}
