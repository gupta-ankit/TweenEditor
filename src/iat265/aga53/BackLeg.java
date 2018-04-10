/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iat265.aga53;

import processing.core.PApplet;

/**
 *
 * @author ankit
 */
public class BackLeg extends AnimalComponent {

    private final AnimalComponent parent;

    public BackLeg(PApplet p, String name, AnimalComponent parent, float angle) {
        super(p, name, parent.pos.x + parent.lengthVector.x, parent.pos.y + parent.lengthVector.y,
                .7f * parent.branchLength, .8f * parent.branchWidth,
                parent.rot + p.radians(angle));
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
        p.line(0, 0, 0, -branchLength);
        p.popMatrix();
        p.popStyle();
    }

    @Override
    public void updatePosition(float x, float y) {
        pos.x = parent.pos.x + parent.lengthVector.x;
        pos.y = parent.pos.y + parent.lengthVector.y;
    }
}
