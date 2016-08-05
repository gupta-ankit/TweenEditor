package tweeneditor;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.DropdownList;
import controlP5.Slider;
import controlP5.Textlabel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import processing.core.PApplet;
import processing.core.PFont;

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
        s = new Scrubber(200, height / 2, width, height);
        s.g = this.g;

        ClockHand first = new ClockHand(width / 2, height / 4, 0, 100, 5);
        ClockHand second = new ClockHand(width / 2, height / 4, PI / 3, 50, 3);
        first.g = this.g;
        second.g = this.g;
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
                .setPosition(selectedLabel.getPosition()[0],
                        selectedLabel.getPosition()[1] + selectedLabel.getHeight() + 5)
                .setBarHeight(20);
        addChannelButton = gui.addButton("Add Channel")
                .setId(ADD_CHNL_BTN)
                .setPosition(propertyDropdown.getPosition()[0] + propertyDropdown.getWidth() + 5,
                        propertyDropdown.getPosition()[1]);

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

        //draw panel borders: menu, scrubber, and editable area
        rect(0, 0, 250, height / 2 - 5); // menu
        line(0, height / 2 - 5, width, height / 2 - 5);

        long t = s.getCurrentT();
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
        /*
        User click in the slider region
         */
        if (mouseX < 250 && mouseY < height / 2) {
            println("menu");
        }

        if (mouseX > 250 && mouseY < height / 2) {
            selected = null;
            for (Scrubbable sb : scrubbables) {
                Scrubbable pick = sb.pick(mouseX, mouseY);
                if (pick != null) {
                    selected = pick;
                    break;
                }
            }
            updateForSelected(selected);
        }

        //clicking scrubber region
        if (mouseY > height / 2) {
            ScrubberChannel channel = s.mousePressed(selected, mouseX, mouseY);
            String property = null;
            if (channel != null) {
                selected = channel.getTarget();
                property = channel.getProperty();
            } else {
                selected = null;
            }
            updateForSelected(selected);
            updatePropertyDropdown(selected, property);
        }
//
//        for (Scrubbable scrubbable : scrubbables) {
//            Scrubbable pick = scrubbable.pick(mouseX, mouseY);
//            if (pick != null) {
//                selected = pick;
//                break;
//            }
//        }
//
//        /*
//         If selected is not null, then a scrubbable component is selected.
//         */
//        updateForSelected(selected);
//
//        /*
//        Let the scrubber know that a mouse press occured at mouseX and mouseY
//         */
//        ScrubberChannel found = s.mousePressed(mouseX, mouseY);
//        if (found != null) {
//            updateForSelected(found.getTarget());
//
//            /*
//                     Select the property in the dropdown
//             */
//            String property = found.getProperty();
//            String[] properties = found.getTarget().getProperties();
//            int index = -1;
//            for (int i = 0; i < properties.length; i++) {
//                if (properties[i].equals(property)) {
//                    index = i;
//                }
//            }
//            propertyDropdown.setValue(index);
//            propertyDropdown.close();
//        }

    }

    private void updateForSelected(Scrubbable s) {
        if (selected != null) {
            String[] properties = selected.getProperties();
            selectedLabel.setValue(selected.getName());
            propertyDropdown.clear();
            propertyDropdown.addItems(properties);
        } else {
            selectedLabel.setValue("No Selection");
            propertyDropdown.clear();
        }
    }

    private void updatePropertyDropdown(Scrubbable selected, String property) {
        if (selected != null && property != null) {
            String[] properties = selected.getProperties();
            int index = -1;
            for (int i = 0; i < properties.length; i++) {
                if (properties[i].equals(property)) {
                    index = i;
                }
            }
            println(index);
            propertyDropdown.setValue(index);
//            propertyDropdown.close();
        }else{
            
        }
    }
}
