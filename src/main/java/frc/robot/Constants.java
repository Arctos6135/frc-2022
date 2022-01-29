package frc.robot;

import com.arctos6135.robotpathfinder.core.RobotSpecs;

import edu.wpi.first.wpilibj.XboxController;

public final class Constants {

    // SPARK MAX Motors 
    public static final int RIGHT_CANSPARKMAX = 1;
    public static final int LEFT_CANSPARKMAX = 2;
    public static final int RIGHT_CANSPARKMAX_FOLLOWER = 3;
    public static final int LEFT_CANSPARKMAX_FOLLOWER = 4;

    // Shooter Motors 
    public static final int MAIN_SHOOTER_MOTOR = 5;
    public static final int AUXILLIARY_SHOOTER_MOTOR = 6;

    // Intake Motors 
    public static final int LEFT_INTAKE_MOTOR = 0;
    public static final int RIGHT_INTAKE_MOTOR = 0; 

    // SPARK MAX Encoders (in inches)
    public static final double WHEEL_DIAMETER = 4.0;
    public static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
    public static final double GEARBOX_RATIO = 0;
    public static final double POSITION_CONVERSION_FACTOR = WHEEL_CIRCUMFERENCE * GEARBOX_RATIO;
    public static final double VELOCITY_CONVERSION_FACTOR = WHEEL_CIRCUMFERENCE * GEARBOX_RATIO / 60;
    public static final int COUNTS_PER_REVOLUTION = 42;
    
    // Warning and Shutoff Temperatures (in centigrade, for inconsistency)
    public static double MOTOR_WARNING_TEMP = 70;
    public static double MOTOR_SHUTOFF_TEMP = 90;
    
    // Xbox Controller 
    public static final double CONTROLLER_DEADZONE = 0.15;

    // Driver Controller 
    public static final int XBOX_DRIVER = 0;
    public static final int DRIVE_FWD_REV = XboxController.Axis.kLeftY.value;
    public static final int DRIVE_LEFT_RIGHT = XboxController.Axis.kRightX.value;
    public static final int REVERSE_DRIVE_DIRECTION = XboxController.Button.kStickLeft.value;
    public static final int OVERRIDE_MOTOR_PROTECTION = XboxController.Button.kB.value;
    public static final int PRECISION_DRIVE_TOGGLE = XboxController.Button.kX.value;
    public static final int PRECISION_DRIVE_HOLD = XboxController.Axis.kLeftTrigger.value; 
    public static final int INTAKE_FORWARD_BUTTON = XboxController.Button.kBumperLeft.value; 
    public static final int INTAKE_REVERSE_BUTTON = XboxController.Button.kBumperRight.value; 

    // Operator Controller 
    public static final int XBOX_OPERATOR = 1;
    public static final int DEPLOY_SHOOTER = XboxController.Button.kX.value;
    public static final int PREPARE_SHOOTER = XboxController.Button.kA.value; 
    

    // Robot Dimensions 
    // TODO: change these to match robot 
    public static final double ROBOT_MAX_VELOCITY = 0;
    public static final double ROBOT_MAX_ACCELERATION = 0;
    public static final double ROBOT_BASE_WIDTH = 0; // inches 
    public static final RobotSpecs ROBOT_SPECS = new RobotSpecs(ROBOT_MAX_VELOCITY, ROBOT_MAX_ACCELERATION,
            ROBOT_BASE_WIDTH); 

    // Drive 
    public static final double COLLISION_THRESHOLD = 0.5f; // TODO: tune the collision threshold 

    public static final int COLOR_MOTOR_OK = 0x00FF00FF;
    public static final int COLOR_MOTOR_WARNING = 0xFFFF00FF;
    public static final int COLOR_MOTOR_SHUTOFF = 0xFF0000FF;
    public static final int COLOR_MOTOR_OVERRIDDEN = 0xA72DFFFF;

    // Shooter Motor PDP Pins
    public static final int SHOOTER_MASTER = 0; 
    public static final int SHOOTER_FOLLOWER = 0;
    
}
