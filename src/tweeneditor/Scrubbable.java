package tweeneditor;

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
}
