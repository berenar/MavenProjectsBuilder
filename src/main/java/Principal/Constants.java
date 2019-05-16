package Principal;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.Color;
import java.util.ArrayList;

final class Constants {

    //Array of ProjectPanel objects
    public static final ArrayList<ProjectPanel> selectedProjects = new ArrayList<>();

    //Colors for components
    public static final Color notWhite = new Color(230, 247, 255);
    public static final Color moreBlue = new Color(128, 191, 255);
    public static final Color mostBlue = new Color(0, 53, 102);
    public static final Color dangerRed = new Color(255, 128, 128);

    //Invisible border
    public static final Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);

    //Margins
    public static final int initial = 50;
    public static final int xMargin = 10;

    //component sizes
    public static final int squareComponent = 30;
    public static final int componentHeight = 30;
    public static final int buttonWidth = 90;
    public static final int compileWidth = 200;
    public static final int jlPathWidth = 400;
    public static final int tickLabelSize = 35;
    public static final int outputHeight = 200;

    //commands to execute
    public static final String compileCommand = "mvn clean install";
    public static final String cloneCommand = "git clone";

    //Console for the output
    public static final Output out = new Output();

}
