// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    // SPARK MAX Motors 
    // TODO: Placeholder values as of now 
    public static final int RIGHT_CANSPARKMAX = 1;
    public static final int LEFT_CANSPARKMAX = 2;
    public static final int RIGHT_CANSPARKMAX_FOLLOWER = 3;
    public static final int LEFT_CANSPARKMAX_FOLLOWER = 4;

    // Shooter Motors 
    // TODO: Write constants for shooter motors 

    // SPARK MAX Encoder 
    // TODO: change wheel diameter 
    // TODO: change gearbox ratio 
    public static final double WHEEL_DIAMETER = 4.0;
    public static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
    public static final double GEARBOX_RATIO = 0;
    public static final double POSITION_CONVERSION_FACTOR = WHEEL_CIRCUMFERENCE * GEARBOX_RATIO;
    public static final double VELOCITY_CONVERSION_FACTOR = WHEEL_CIRCUMFERENCE * GEARBOX_RATIO / 60;
    public static final int COUNTS_PER_REVOLUTION = 42; 
}
