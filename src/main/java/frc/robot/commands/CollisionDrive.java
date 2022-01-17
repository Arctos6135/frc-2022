package frc.robot.commands;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Drivetrain;

/**
 * Drives forward until the robot collides into the fender. 
 * 
 * This will be done by calculating acceleration and jerk. Once the jerk 
 */
public class CollisionDrive extends CommandBase {

    private AHRS ahrs;
    private Drivetrain drivetrain;
    private double speed;
    private boolean collisionDetected = false; 

    // Acceleration 
    double lastAccelX = 0;
    double lastAccelY = 0;
    
    public CollisionDrive(AHRS ahrs, Drivetrain drivetrain, double speed) {
        this.ahrs = ahrs;
        this.drivetrain = drivetrain;
        this.speed = speed;

        addRequirements(drivetrain);
    }
    
    @Override 
    public void initialize() {
        // Set the motors to a slow speed, since it will be colliding with the fender. 
        if (Math.abs(speed) > 0.5) {
            speed = Math.copySign(0.5, speed); 
            drivetrain.setMotors(speed, speed); 
        } else {
            drivetrain.setMotors(speed, speed);
        }
    }

    @Override 
    public void execute() {
        double currAccelX = ahrs.getWorldLinearAccelX();
        double currAccelY = ahrs.getWorldLinearAccelY();
        
        double currJerkX = currAccelX - lastAccelX;
        double currJerkY = currAccelY - lastAccelY;
        
        lastAccelX = currAccelX;
        lastAccelY = currAccelY;

        // Detect Collision 
        if ((Math.abs(currJerkX) > Constants.COLLISION_THRESHOLD)
                || (Math.abs(currJerkY) > Constants.COLLISION_THRESHOLD)) {
            collisionDetected = true;
        }
    }

    @Override 
    public boolean isFinished() {
        return collisionDetected;
    }

    @Override 
    public void end(boolean interrupted) {
        // Stop the robot
        drivetrain.setMotors(0, 0); 
    }
}