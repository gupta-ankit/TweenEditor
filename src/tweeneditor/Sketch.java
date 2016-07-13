package tweeneditor;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.DropdownList;
import controlP5.ListBox;
import controlP5.Slider;
import controlP5.Textlabel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

/**
 *
 * @author aga53
 */
public class Sketch extends PApplet {

    final int PLAY_BTN = 10;
    final int STOP_BTN = 11;
    final int ADD_CHNL_BTN = 12;
    final int PROPERTIES_DROPDOWN = 13;

    final int ROTATION_SLIDER = 14;
    final int SLIDER_R = 15;
    final int SLIDER_G = 16;
    final int SLIDER_B = 17;

    final int TWEEN_TOTAL_DURATION = 60 * 1000;

    Scrubber s;
    ControlP5 gui;
    Scrubbable selected;
    List<Scrubbable> scrubbables;

    Button playBtn;
    Textlabel selectedLabel;
    DropdownList propertyDropdown;
    Button addChannelButton;
    Slider rotationSlider, sliderR, sliderG, sliderB;

    long playStartTime;
    boolean play;

    @Override
    public void settings() {
        size(displayWidth, displayHeight);
    }

    @Override
    public void setup() {
        play = false;
        scrubbables = new ArrayList<>();
        s = new Scrubber(200, height / 2);
        ClockHand first = new ClockHand(width / 2, height / 4, 0, 100, 5);
        ClockHand second = new ClockHand(width / 2, height / 4, PI / 3, 50, 3);
        first.setName("first");
        second.setName("second");
        scrubbables.add(first);
        scrubbables.add(second);

        gui = new ControlP5(this);

        playBtn = gui.addButton("Play").setId(PLAY_BTN).setPosition(20, 20);
        gui.addButton("Stop").setId(STOP_BTN).setPosition(playBtn.getWidth() + 30, 20);
        PFont pfont = createFont("Arial", 20, true); // use true/false for smooth/no-smooth        

        rotationSlider = setupSlider("rotation").setRange(-180, 180).setId(ROTATION_SLIDER);
        sliderR = setupSlider("red").setRange(0, 255).setId(SLIDER_R);
        sliderG = setupSlider("green").setRange(0, 255).setId(SLIDER_G);
        sliderB = setupSlider("blue").setRange(0, 255).setId(SLIDER_B);

        rotationSlider.setVisible(false);
        sliderR.setVisible(false);
        sliderG.setVisible(false);
        sliderB.setVisible(false);

        selectedLabel = gui.addLabel("selected").setPosition(50, rotationSlider.getPosition()[1] + rotationSlider.getHeight() + 20)
                .setHeight(30)
                .setColor(color(0))
                .setFont(pfont);
        propertyDropdown = gui.addDropdownList("properties")
                .setId(PROPERTIES_DROPDOWN)
                .setPosition(selectedLabel.getPosition()[0] + selectedLabel.getWidth() + 5, selectedLabel.getPosition()[1])
                .setBarHeight(20);
        addChannelButton = gui.addButton("Add Channel").setId(ADD_CHNL_BTN).setPosition(propertyDropdown.getPosition()[0] + propertyDropdown.getWidth() + 5,
                selectedLabel.getPosition()[1]);

        s.addChannel(first, "rotation");
    }

    private Slider setupSlider(String name) {
        Slider s = gui.addSlider(name);
        s.setColorCaptionLabel(color(0)).setPosition(playBtn.getPosition()[0], playBtn.getPosition()[1] + playBtn.getHeight() + 10);
        return s;
    }

    public void controlEvent(ControlEvent event) {
        switch (event.getId()) {

            case PLAY_BTN:
                playStartTime = millis();
                play = true;
                break;

            case STOP_BTN:
                play = false;
                s.setCurrentT(0);
                break;

            case ADD_CHNL_BTN:
                if (selected != null) {
                    int selectedIndex = (int) propertyDropdown.getValue();
                    Map<String, Object> item = propertyDropdown.getItem(selectedIndex);
                    String stringValue = (String) item.get("name");
                    s.addChannel(selected, stringValue);
                }
                break;

            case PROPERTIES_DROPDOWN:
                int selectedIndex = (int) propertyDropdown.getValue();
                Map<String, Object> item = propertyDropdown.getItem(selectedIndex);
                String stringValue = (String) item.get("name");

                rotationSlider.setVisible(false);
                sliderR.setVisible(false);
                sliderG.setVisible(false);
                sliderB.setVisible(false);
                switch (stringValue) {
                    case "rotation":
                        rotationSlider.setVisible(true);
                        rotationSlider.setValue(selected.getParameter("rotation"));
                        break;
                    case "red":
                        sliderR.setVisible(true);
                        sliderR.setValue(selected.getParameter("red"));
                        break;
                    case "green":
                        sliderG.setVisible(true);
                        sliderG.setValue(selected.getParameter("green"));
                        break;
                    case "blue":
                        sliderB.setVisible(true);
                        sliderB.setValue(selected.getParameter("blue"));
                        break;
                }

                break;

            case ROTATION_SLIDER:
                if (selected != null) {
                    selected.setParameter("rotation", radians(rotationSlider.getValue()));
                }
                break;

            case SLIDER_R:
                if (selected != null) {
                    selected.setParameter("red", sliderR.getValue());
                }
                break;

            case SLIDER_G:
                if (selected != null) {
                    selected.setParameter("green", sliderG.getValue());
                }
                break;

            case SLIDER_B:
                if (selected != null) {
                    selected.setParameter("blue", sliderB.getValue());
                }
                break;
        }
    }

    @Override
    public void draw() {
        background(200);
        long t = s.currentT;
        if (play) {
            t = (millis() - playStartTime);
        }
        s.setCurrentT(t);
        s.draw();

        for (Scrubbable scrubbable : scrubbables) {
            scrubbable.draw();
        }
    }

    @Override
    public void mousePressed() {
        for (Scrubbable scrubbable : scrubbables) {
            Scrubbable pick = scrubbable.pick(mouseX, mouseY);
            if (pick != null) {
                selected = pick;
                break;
            }
        }

        /*
         If selected is not null, then a scrubbable component is selected.
         */
        updateForSelected(selected);

        /*
         Let the scrubber know that a mouse press occured at mouseX and mouseY
         */
        s.mousePressed(mouseX, mouseY);
    }

    private void updateForSelected(Scrubbable s) {
        if (selected != null) {
            String[] properties = selected.getProperties();
            selectedLabel.setValue(selected.getName());
            propertyDropdown.clear();
            propertyDropdown.addItems(properties);
        }
    }

    class Scrubber {

        private final int CHANNEL_HEIGHT = 10;
        private final int CHANNEL_MARGIN = 5;
        private final int RT_MRGN = 50;
        private List<ScrubberChannel> channels;
        private int x;
        private int y;
        private long currentT;
        private final int scrubberWidth;

        public Scrubber(int x, int y) {
            this.x = x;
            this.y = y;
            this.channels = new ArrayList<>();
            this.currentT = 0;
            this.scrubberWidth = width - x - RT_MRGN; //50 is the right margin
        }

        void draw() {
            for (ScrubberChannel s : channels) {
                s.draw();
            }
            pushStyle();
            stroke(255, 0, 0);
            float currentX = (scrubberWidth / (float) TWEEN_TOTAL_DURATION) * currentT + x;
            line(currentX, y, currentX, height);
            popStyle();
        }

        private void addChannel(Scrubbable target, String parameter) {
            ScrubberChannel channel = new ScrubberChannel(target, parameter, x,
                    channels.size() * (CHANNEL_HEIGHT + CHANNEL_MARGIN) + y, scrubberWidth, CHANNEL_HEIGHT
            );
            channel.addkFrame(new KeyFrame(0, target.getParameter(parameter)));
            channels.add(channel);
        }

        private void setCurrentT(long t) {
            this.currentT = t;
        }

        public void mousePressed(int mx, int my) {

            /*
             Check if a scrubber channel is picked
             */
            for (ScrubberChannel sc : channels) {
                ScrubberChannel pick = sc.pick(mx, my);
                if (pick != null) {
                    /*
                     If a scrubber channel is clicked, then update selected to 
                     channel's target
                     */
                    selected = pick.target;
                    updateForSelected(selected);

                    /*
                     Select the property in the dropdown
                     */
                    String property = pick.property;
                    String[] properties = pick.target.getProperties();
                    int index = -1;
                    for (int i = 0; i < properties.length; i++) {
                        if (properties[i].equals(property)) {
                            index = i;
                        }
                    }
                    propertyDropdown.setValue(index);
                    propertyDropdown.close();
                }
            }
        }

        class ScrubberChannel {

            private Scrubbable target;
            private String property;
            private float x, y, scrubberW, scrubberH, duration;
            private List<KeyFrame> kFrames;

            public float getDuration() {
                return duration;
            }

            public void setDuration(float duration) {
                this.duration = duration;
            }

            public ScrubberChannel(Scrubbable target, String property, float x, float y, float w, float h) {
                this.target = target;
                this.property = property;
                this.x = x;
                this.y = y;
                this.scrubberW = w;
                this.scrubberH = h;
                this.duration = TWEEN_TOTAL_DURATION;
                this.kFrames = new ArrayList<>();
            }

            public float getX() {
                return x;
            }

            public void setX(float x) {
                this.x = x;
            }

            public float getY() {
                return y;
            }

            public void setY(float y) {
                this.y = y;
            }

            public float getW() {
                return scrubberW;
            }

            public void setW(float w) {
                this.scrubberW = w;
            }

            public float getH() {
                return scrubberH;
            }

            public void setH(float h) {
                this.scrubberH = h;
            }

            public List<KeyFrame> getkFrames() {
                return kFrames;
            }

            public void addkFrame(KeyFrame f) {
                kFrames.add(f);
                kFrames.sort(new Comparator<KeyFrame>() {
                    @Override
                    public int compare(KeyFrame o1, KeyFrame o2) {
                        return (int) (o1.t - o2.t);
                    }
                });
            }

            public void removekFrame(KeyFrame f) {
                kFrames.remove(f);
            }

            void draw() {
                pushStyle();
                noStroke();
                fill(128);
                text(target.getName() + ":" + property, 10, y + 10);
                rect(x, y, scrubberW, scrubberH);

                /*
                 Draw key frames
                 */
                for (int i = 0; i < kFrames.size(); i++) {
                    KeyFrame f = kFrames.get(i);
                    float anchorX = x + (scrubberW / duration) * f.t;
                    fill(0, 0, 255);
                    rect(anchorX, y, ANCHOR_WIDTH, scrubberH);
                }

                /*
                 Update properties
                 */
                if (play) {
                    for (int i = 0; i < kFrames.size() - 1; i++) {
                        KeyFrame keyFrame = kFrames.get(i);
                        KeyFrame nextKeyFrame = kFrames.get(i + 1);
                        if (currentT >= keyFrame.t && currentT <= nextKeyFrame.t) {
                            float delta = (nextKeyFrame.value - keyFrame.value) / (nextKeyFrame.t - keyFrame.t);
                            float newValue = keyFrame.value + delta * (currentT - keyFrame.t);
                            target.setParameter(property, newValue);
                        }
                    }
                }

                popStyle();
            }
            private static final int ANCHOR_WIDTH = 5;

            private ScrubberChannel pick(int mouseX, int mouseY) {
                if (mouseX > x && mouseX < x + scrubberW && mouseY > y && mouseY < y + scrubberH) {
                    /*
                     If the user clicks on existing key frame, remove it and return this
                     */
                    for (int i = 0; i < kFrames.size(); i++) {
                        KeyFrame f = kFrames.get(i);
                        float anchorX = x + (scrubberW / duration) * f.t;
                        //if clicking on existing keyframe remove it
                        if (mouseX > anchorX && mouseX < anchorX + ANCHOR_WIDTH) {
                            removekFrame(f);
                            return this;
                        }
                    }

                    /*
                     If the user clicks on a new space
                     */
                    long t = (long) ((mouseX - x) * (duration / scrubberW));
                    if (selected == this.target) {
                        //add a new key frame if the target is the currently selected scrubbable
                        final KeyFrame frame = new KeyFrame(t, selected.getParameter(this.property));
                        setCurrentT(t);
                        addkFrame(frame);
                    }
                    //in any case -- whether or not a key frame is added, return this.
                    return this;
                }
                return null;
            }
        }

        class KeyFrame {

            private long t;
            private float value;

            public KeyFrame(long t, float value) {
                this.t = t;
                this.value = value;
            }

            public void setProperty(float v) {
                value = v;
            }

            public long getT() {
                return t;
            }

            public float getValue() {
                return value;
            }
        }
    }

    class ClockHand implements Scrubbable {

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

    interface Scrubbable {

        String getName();

        void setName(String name);

        String[] getProperties();

        void setParameter(String property, float value);

        float getParameter(String property);

        void draw();

        public Scrubbable pick(int mouseX, int mouseY);
    }
}
