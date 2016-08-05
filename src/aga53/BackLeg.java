/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aga53;

/**
 *
 * @author ankit
 */
public class BackLeg extends AnimalComponent {

    private final AnimalComponent parent;

    public BackLeg(String name, AnimalComponent parent, float angle) {
        super(name, parent.pos.x + parent.lengthVector.x, parent.pos.y + parent.lengthVector.y,
                .7f * parent.branchLength, .8f * parent.branchWidth,
                parent.rot + radians(angle));
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
        line(0, 0, 0, -branchLength);
        popMatrix();
        popStyle();
    }

    @Override
    public void updatePosition(float x, float y) {
        pos.x = parent.pos.x + parent.lengthVector.x;
        pos.y = parent.pos.y + parent.lengthVector.y;
    }
}
