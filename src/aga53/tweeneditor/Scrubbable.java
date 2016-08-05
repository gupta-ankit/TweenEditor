package aga53.tweeneditor;

import processing.core.PGraphics;

/**
 *
 * @author aga53
 */
public interface Scrubbable {

    String getName();

    void setName(String name);

    String[] getProperties();

    void setParameter(String property, float value);

    float getParameter(String property);

    void draw();

    public Scrubbable pick(int mouseX, int mouseY);

    public void updateGraphicsObject(PGraphics g);
}
