/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iat265.aga53;

import processing.core.PApplet;
import static processing.core.PConstants.PI;

/**
 *
 * @author aga53
 */
public class MyAnimalFactory implements CreatureFactory {

    @Override
    public Scrubbable getCreature(PApplet pa) {
        AnimalComponent creature = new Torso(pa, "Torso", 700, 300, 200.0f, 15.0f, PI / 2); // depth starts at 0
        BackLeg lbl = new BackLeg(pa,"LeftBackLeg", creature, 60);
        BackLeg rbl = new BackLeg(pa,"RightBackLeg", creature, 120);
        FrontLeg lfl = new FrontLeg(pa,"LeftFrontLeg", creature, 60);
        FrontLeg rfl = new FrontLeg(pa,"RightFrontLeg", creature, 120);

        creature.addLeftChild(lbl);
        creature.addRightChild(rbl);
        creature.addLeftChild(lfl);
        creature.addRightChild(rfl);

        Neck neck = new Neck(pa,"Neck", creature, -135);
        creature.addLeftChild(neck);

        Head head = new Head(pa,"Head", neck);
        neck.addLeftChild(head);

        return creature;
    }

}
