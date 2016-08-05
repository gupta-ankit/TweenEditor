/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aga53;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import aga53.tweeneditor.Scrubbable;

/**
 *
 * @author ankit
 */
public class AnimalComponent extends PApplet implements Scrubbable {

    //the starting position
    PVector pos;
    //the endPos stores the x and y components of given branch
    PVector lengthVector;

    //angle with x axis
    float rot;

    //tree component dimensions
    float branchLength, branchWidth;

    //color of the component
    int c;

    ArrayList<AnimalComponent> left;
    ArrayList<AnimalComponent> right;

    private BoundingBox bbox;
    private String name;

    AnimalComponent(String name, float initX, float initY,
            float initBranchLength,
            float initBranchWidth, float initRot) {
        
        this.name = name;

        this.pos = new PVector(initX, initY);
        this.branchLength = initBranchLength;
        this.branchWidth = initBranchWidth;
        this.c = color(128, 128, 128);

        this.rot = initRot;

        this.lengthVector = new PVector(0.0f, -branchLength);
        this.lengthVector.rotate(this.rot);

        this.bbox = new BoundingBox();

        computeBBox();

        this.left = new ArrayList<AnimalComponent>();
        this.right = new ArrayList<AnimalComponent>();
    }
    private static final int ANGLE = 30;

    /**
     * drawShape is a function called by draw() and overridden by child classes
     */
    public void drawShape() {
    }

    ;

    public void addLeftChild(AnimalComponent c) {
        this.left.add(c);
    }

    public void addRightChild(AnimalComponent c) {
        this.right.add(c);
    }

    @Override
    public void draw() {
        for (AnimalComponent c : left) {
            c.draw();
        }
        drawShape();
        for (AnimalComponent c : right) {
            c.draw();
        }
    }

    /*
     This function searches the tree to find the selected tree component
     */
    @Override
    public Scrubbable pick(int mx, int my) {
        if (mx > bbox.minX && mx < bbox.maxX && my > bbox.minY && my < bbox.maxY) {
            return this;
        } else {
            for (Scrubbable c : this.left) {
                Scrubbable found = c.pick(mx, my);
                if (found != null) {
                    return found;
                }
            }

            for (Scrubbable c : this.right) {
                Scrubbable found = c.pick(mx, my);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private void computeBBox() {
        if (lengthVector.x > 0) {
            bbox.minX = pos.x - branchWidth / 2;
            bbox.maxX = pos.x + lengthVector.x + branchWidth / 2;
        } else {
            bbox.maxX = pos.x + branchWidth / 2;
            bbox.minX = pos.x + lengthVector.x - branchWidth / 2;
        }

        if (lengthVector.y > 0) {
            bbox.minY = pos.y - branchWidth / 2;
            bbox.maxY = pos.y + lengthVector.y + branchWidth / 2;
        } else {
            bbox.maxY = pos.y + branchWidth / 2;
            bbox.minY = pos.y + lengthVector.y - branchWidth / 2;
        }
    }

    void setRotation(float radians) {
        revisit(pos.x, pos.y, radians);
    }

    void setBranchLength(float value) {
        this.branchLength = value;
        revisit(pos.x, pos.y, rot);
    }

    public void updatePosition(float x, float y) {
    }

    ;

    private void revisit(float x, float y, float newRot) {
        float diffRot = newRot - rot;

        updatePosition(x, y);
        rot = newRot;

        lengthVector.x = 0f;
        lengthVector.y = -branchLength;
        lengthVector.rotate(newRot);

        computeBBox();

        for (AnimalComponent c : this.left) {
            c.revisit(pos.x + lengthVector.x, pos.y + lengthVector.y,
                    c.rot + diffRot);
        }

        for (AnimalComponent c : this.right) {
            c.revisit(pos.x + lengthVector.x, pos.y + lengthVector.y,
                    c.rot + diffRot);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String[] getProperties() {
        return new String[]{"rotation", "red"};
    }

    @Override
    public void setParameter(String property, float value) {
        switch (property) {
            case "rotation":
                this.rot = value;
                break;
            case "red":
                this.c = color(value, green(this.c), blue(this.c));
                break;
            default:
                throw new RuntimeException("Unknown property: " + property);
        }
    }

    @Override
    public float getParameter(String property) {
        switch (property) {
            case "rotation":
                return this.rot;
            case "red":
                return red(this.c);
            default:
                throw new RuntimeException("Unknown property: " + property);
        }
    }

    @Override
    public void updateGraphicsObject(PGraphics g) {
        for (int i = 0; i < left.size(); i++) {
            left.get(i).updateGraphicsObject(g);
        }
        this.g = g;
        for (int i = 0; i < right.size(); i++) {
            right.get(i).updateGraphicsObject(g);
        }
    }
}
