package aga53;

/**
 *
 * @author ankit
 */
public class Head extends AnimalComponent {

    private final AnimalComponent parent;

    public Head(String name, AnimalComponent parent) {
        super(name, parent.pos.x + parent.lengthVector.x, parent.pos.y + parent.lengthVector.y,
                50, 20, 0);
        this.parent = parent;
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
        ellipse(- branchLength/2, -branchLength/2, branchLength, branchLength);
        popMatrix();
        popStyle();
    }

    @Override
    public void updatePosition(float x, float y) {
        pos.x = parent.pos.x + parent.lengthVector.x;
        pos.y = parent.pos.y + parent.lengthVector.y;
    }

}
