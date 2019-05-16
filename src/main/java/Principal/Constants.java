package Principal;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

final class Constants {
    /*-------------------------------------------------------------------------------------------*/
    /*------------------------------------------- MAIN ------------------------------------------*/
    /*-------------------------------------------------------------------------------------------*/
    //Array of ProjectPanel objects
    public static final ArrayList<ProjectPanel> selected_projects = new ArrayList<>();

    //Console for the output
    public static final Output out = new Output();

    //JButtons color
    public static final Color color_selected = new Color(128, 191, 255);
    public static final Color color_danger = new Color(255, 128, 128);

    //component sizes
    public static final int add_project_size = 30;
    public static final int compile_width = 200;
    public static final int compile_height = 30;
    public static final int reset_width = 90;
    public static final int reset_height = 30;
    public static final int save_width = 90;
    public static final int save_height = 30;

    //commands to execute
    public static final String compileCommand = "mvn clean install";

    /*-------------------------------------------------------------------------------------------*/
    /*-------------------------------------- PROJECT PANEL --------------------------------------*/
    /*-------------------------------------------------------------------------------------------*/

    //Border and colors for components
    public static final Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    public static final Color notWhite = new Color(230, 247, 255);

    public static final String cloneCommand = "git clone";

    //Where to start painting components
    public static final int x_initial = 50;
    public static final int y_initial = 50;

    //JFrame margin
    public static final int x_margin = 10;

    //Component sizes
    public static final int jl_order_size = 30;
    public static final int jl_path_width = 400;
    public static final int jl_path_height = 30;
    public static final int jbs_width = 90;
    public static final int jbs_height = 30;
    public static final int tickLabel_size = 35;
}
