package tweeneditor;

import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author aga53
 */
public class Scrubber extends PApplet {

    private final int CHANNEL_HEIGHT = 10;
    private final int CHANNEL_MARGIN = 5;
    private final int RT_MRGN = 50;
    private final List<ScrubberChannel> channels;
    private final int x;
    private final int y;
    private long currentT;
    private final int scrubberWidth;
    final int TWEEN_TOTAL_DURATION = 60 * 1000;

    public Scrubber(int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.channels = new ArrayList<>();
        this.currentT = 0;
        this.scrubberWidth = width - x - RT_MRGN; //50 is the right margin
    }

    @Override
    public void draw() {
        for (ScrubberChannel s : channels) {
            s.update(currentT);
            s.draw();
        }
        pushStyle();
        stroke(255, 0, 0);
        float currentX = (scrubberWidth / (float) TWEEN_TOTAL_DURATION) * currentT + x;
        line(currentX, y, currentX, height);
        popStyle();
    }

    public void addChannel(Scrubbable target, String parameter) {
        ScrubberChannel channel = new ScrubberChannel(target, parameter, x,
                channels.size() * (CHANNEL_HEIGHT + CHANNEL_MARGIN) + y, 
                scrubberWidth, CHANNEL_HEIGHT,
                TWEEN_TOTAL_DURATION
        );
        channel.g = this.g;
        channel.addkFrame(new KeyFrame(0, target.getParameter(parameter)));
        channels.add(channel);
    }

    public void setCurrentT(long t) {
        this.currentT = t;
    }

    public ScrubberChannel mousePressed(Scrubbable selected, int mx, int my) {
        /*
             Check if a scrubber channel is picked
         */
        for (ScrubberChannel sc : channels) {
            boolean picked = sc.pick(selected, mx, my);
            if (picked) {
                /*
                     If a scrubber channel is clicked, then update selected to 
                     channel's target
                 */
                return sc;
            }
        }
        return null;
    }

    public long getCurrentT() {
        return currentT;
    }
   
}
