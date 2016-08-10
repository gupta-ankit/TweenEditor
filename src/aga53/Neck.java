package aga53;

/**
 *
 * @author ankit
 */
public class Neck extends AnimalComponent {

    AnimalComponent parent;

    public Neck(String name, AnimalComponent parent, float angle) {
        super(name, parent.pos.x, parent.pos.y, .2f * parent.branchLength, 20f, parent.rot + radians(angle));
        this.parent = parent;
    }

    @Override
    public void updatePosition(float x, float y) {
        this.branchLength = .2f * parent.branchLength;
    }

    @Override
    public void drawShape() {
        pushStyle();
        fill(c);
        pushMatrix();
        translate(pos.x, pos.y);
        rotate(rot);
        stroke(c);
        strokeWeight(branchWidth);
        line(0, 0, 0, -branchLength);
        popMatrix();
        popStyle();
    }

}
