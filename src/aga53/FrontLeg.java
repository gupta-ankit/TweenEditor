package aga53;

/**
 *
 * @author ankit
 */
public class FrontLeg extends AnimalComponent {

    public FrontLeg(String name, AnimalComponent parent, float angle) {
        super(name, parent.pos.x, parent.pos.y,
                .7f * parent.branchLength, .8f * parent.branchWidth,
                parent.rot + radians(angle));
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

    @Override
    public void updatePosition(float x, float y) {
        //do nothing
    }
}
