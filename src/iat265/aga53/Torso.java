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
public class Torso extends AnimalComponent {

    public Torso(PApplet p, String name, float initX, float initY, float initBranchLength, float initBranchWidth, float initRot) {
        super(p, name, initX, initY, initBranchLength, initBranchWidth, initRot);
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
        pos.x = x;
        pos.y = y;
    }
}
