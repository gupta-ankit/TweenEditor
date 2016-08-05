package aga53.tweeneditor;

import aga53.AnimalComponent;
import aga53.BackLeg;
import aga53.FrontLeg;
import aga53.Head;
import aga53.Neck;
import aga53.Torso;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.DropdownList;
import controlP5.Slider;
import controlP5.Textlabel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import processing.core.PApplet;
import static processing.core.PConstants.PI;
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

    final int SAVE_BTN = 18;
    final int LOAD_BTN = 19;

    Scrubber scrubber;
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
    private Button saveBtn;

    @Override
    public void settings() {
        size(displayWidth, displayHeight);
    }

    public static Scrubbable getAnimal() {
        AnimalComponent creature = new Torso("Torso", 700, 300, 200.0f, 15.0f, PI / 2); // depth starts at 0
        BackLeg lbl = new BackLeg("LeftBackLeg", creature, 60);
        BackLeg rbl = new BackLeg("RightBackLeg", creature, 120);
        FrontLeg lfl = new FrontLeg("LeftFrontLeg", creature, 60);
        FrontLeg rfl = new FrontLeg("RightFrontLeg", creature, 120);

        creature.addLeftChild(lbl);
        creature.addRightChild(rbl);
        creature.addLeftChild(lfl);
        creature.addRightChild(rfl);

        Neck neck = new Neck("Neck", creature, -135);
        creature.addLeftChild(neck);

        Head head = new Head("Head", neck);
        creature.addLeftChild(head);

        return creature;
    }

    @Override
    public void setup() {
        play = false;
        scrubbables = new ArrayList<>();
        scrubber = new Scrubber(200, height / 2, width, height);
        scrubber.g = this.g;

        gui = new ControlP5(this);

        playBtn = gui.addButton("Play").setId(PLAY_BTN).setPosition(20, 20);
        gui.addButton("Stop").setId(STOP_BTN).setPosition(playBtn.getWidth() + 30, 20);

        saveBtn = gui.addButton("Save").setId(SAVE_BTN).setPosition(playBtn.getPosition()[0],
                playBtn.getPosition()[1] + playBtn.getHeight() + 30);
        gui.addButton("Load").setId(LOAD_BTN).setPosition(saveBtn.getWidth() + 30, saveBtn.getPosition()[1]);

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

        AnimalComponent animal = (AnimalComponent) getAnimal();
        animal.updateGraphicsObject(this.g);

        scrubbables.add(animal);
    }

    private Slider setupSlider(String name) {
        Slider slider = gui.addSlider(name);
        slider.setColorCaptionLabel(color(0)).setPosition(saveBtn.getPosition()[0], saveBtn.getPosition()[1] + saveBtn.getHeight() + 30);
        return slider;
    }

    public void controlEvent(ControlEvent event) {
        switch (event.getId()) {

            case PLAY_BTN:
                playStartTime = millis();
                play = true;
                break;

            case STOP_BTN:
                play = false;
                scrubber.setCurrentT(0);
                break;

            case SAVE_BTN:
                scrubber.saveAnimation("animation.xml");
                break;
                
            case LOAD_BTN:
                scrubber.loadAnimation("animation.xml");
                break;

            case ADD_CHNL_BTN:
                if (selected != null) {
                    int selectedIndex = (int) propertyDropdown.getValue();
                    Map<String, Object> item = propertyDropdown.getItem(selectedIndex);
                    String stringValue = (String) item.get("name");
                    scrubber.addChannel(selected, stringValue);
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

        long t = scrubber.getCurrentT();
        if (play) {
            t = (millis() - playStartTime);
        }
        scrubber.setCurrentT(t);
        scrubber.draw();

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
            ScrubberChannel channel = scrubber.mousePressed(selected, mouseX, mouseY);
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
        } else {

        }
    }
}
