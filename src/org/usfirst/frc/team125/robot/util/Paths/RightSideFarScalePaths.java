package org.usfirst.frc.team125.robot.util.Paths;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightSideFarScalePaths {

    public static Waypoint[] toBeforeScaleTurn = new Waypoint[]{
            new Waypoint(0.0, 0.0, Pathfinder.d2r(0.0)),
            new Waypoint(3.7, 0.0, Pathfinder.d2r(0.0)),
            new Waypoint(5.2, 1.5, Pathfinder.d2r(90.0)),
            new Waypoint(5.2, 3.5, Pathfinder.d2r(90.0)),
    };

    public static Waypoint[] turnToScale = new Waypoint[]{
            new Waypoint(0.0, 0.0, Pathfinder.d2r(0.0)),
            new Waypoint(1.05, -1.0, Pathfinder.d2r(-90.0)),
    };

}
