package aga53.tweeneditor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
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

    public void saveAnimation(String xmlFile) {
        Document dom;
        Element e = null;

        // instance of a DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            dom = db.newDocument();

            // create the root element
            Element root = dom.createElement("scrubbers");
            for (ScrubberChannel ch : channels) {

                e = dom.createElement("scrubber");
                Element name = createTag(dom, "name", ch.getTarget().getName());
                Element property = createTag(dom, "property", ch.getProperty());

                Element frames = dom.createElement("frames");

                e.appendChild(name);
                e.appendChild(property);
                e.appendChild(frames);

                for (KeyFrame f : ch.getkFrames()) {
                    Element frame = dom.createElement("frame");
                    Element time = createTag(dom, "time", f.getT() + "");
                    Element value = createTag(dom, "value", f.getValue() + "");
                    frame.appendChild(time);
                    frame.appendChild(value);
                    frames.appendChild(frame);
                }

                root.appendChild(e);
            }

            dom.appendChild(root);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                // send DOM to file
                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream(xmlFile)));
            } catch (TransformerException | IOException te) {
                System.out.println(te.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }

    private Element createTag(Document dom, String tagName, String value) throws DOMException {
        Element tag = dom.createElement(tagName);
        tag.appendChild(dom.createTextNode(value));
        return tag;
    }

    public void loadAnimation(String xmlFile) {
        List<ScrubberChannel> channels = new ArrayList<>();

        Document dom;
        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the    
            // XML file
            dom = db.parse(xmlFile);

            Element doc = dom.getDocumentElement();
            NodeList scrubbers = doc.getElementsByTagName("scrubber");

            for (int i = 0; i < scrubbers.getLength(); i++) {
                println("here");
                Node item = scrubbers.item(i);
                NodeList channelNodes = item.getChildNodes();
                for (int j = 0; j < channelNodes.getLength(); j++) {
                    println(i + " " + j);
                }
            }

        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException se) {
            System.out.println(se.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    private String getTextValue(String def, Element doc, String tag) {
        String value = def;
        NodeList nl;
        nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
            value = nl.item(0).getFirstChild().getNodeValue();
        }
        return value;
    }
}
