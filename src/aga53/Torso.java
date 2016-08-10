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
public class Torso extends AnimalComponent {

    public Torso(String name, float initX, float initY, float initBranchLength, float initBranchWidth, float initRot) {
        super(name, initX, initY, initBranchLength, initBranchWidth, initRot);
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
        pos.x = x;
        pos.y = y;
    }
}
