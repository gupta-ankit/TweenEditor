package tweeneditor;

import processing.core.PApplet;
import processing.core.PVector;

class ClockHand extends PApplet implements Scrubbable {

    PVector pos;
    PVector lengthVector;
    int handLength, handWidth;
    private float rot;
    private String name;
    int red, green, blue;

    ClockHand(float x, float y, float startRot, int handLength, int handWidth) {
        this.name = "hand";
        this.pos = new PVector(x, y);
        this.rot = startRot;
        this.handLength = handLength;
        this.handWidth = handWidth;
        this.lengthVector = new PVector(0f, -handLength);
        this.lengthVector.rotate(startRot);
        this.red = 255;
        this.green = 0;
        this.blue = 0;
    }

    @Override
    public void draw() {
        pushStyle();
        pushMatrix();
        stroke(red, green, blue);
        translate(pos.x, pos.y);
        rotate(rot);
        strokeWeight(handWidth);
        line(0, 0, 0, -handLength);
        popMatrix();
        popStyle();
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
        return new String[]{"rotation", "red", "green", "blue"};
    }

    @Override
    public void setParameter(String property, float value) {
        switch (property) {
            case "rotation":
                this.rot = value;
                break;
            case "red":
                System.out.println(value);
                this.red = (int) value;
                break;
            case "green":
                this.green = (int) value;
                break;
            case "blue":
                this.blue = (int) value;
                break;
            default:
                throw new RuntimeException("Invalid property : " + property);
        }
    }

    @Override
    public float getParameter(String property) {
        switch (property) {
            case "rotation":
                return this.rot;
            case "red":
                return this.red;
            case "green":
                return this.green;
            case "blue":
                return this.blue;
            default:
                throw new RuntimeException("Invalid property : " + property);
        }
    }

    @Override
    public Scrubbable pick(int mx, int my) {
        float minX, maxX, minY, maxY;
        if (lengthVector.x > 0) {
            minX = pos.x - handWidth / 2;
            maxX = pos.x + lengthVector.x + handWidth / 2;
        } else {
            maxX = pos.x + handWidth / 2;
            minX = pos.x + lengthVector.x - handWidth / 2;
        }

        if (lengthVector.y > 0) {
            minY = pos.y;
            maxY = pos.y + lengthVector.y;
        } else {
            maxY = pos.y;
            minY = pos.y + lengthVector.y;
        }

        if (mx > minX && mx < maxX && my > minY && my < maxY) {
            return this;
        } else {
            return null;
        }
    }
}
