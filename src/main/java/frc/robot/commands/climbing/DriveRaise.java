package frc.robot.commands.climbing;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.Drivetrain;

/**
 * Raises the climb hook halfway and drives under the low bar.    
 */
public class DriveRaise extends CommandBase {
    
    private final ClimbSubsystem climbSubsystem; 
    private final Drivetrain drivetrain; 

    private double initialTime; 

    
    public DriveRaise(ClimbSubsystem climbSubsystem, Drivetrain drivetrain) {
        this.climbSubsystem = climbSubsystem; 
        this.drivetrain = drivetrain; 
    }   

    @Override 
    public void initialize() {
        this.initialTime = Timer.getFPGATimestamp();

        climbSubsystem.setHookMotorSpeed(1.0);
    }
    
    @Override 
    public void execute() {
        double currentTime = Timer.getFPGATimestamp(); 

        if (currentTime - this.initialTime >= Constants.RAISE_HALFWAY) {
           climbSubsystem.stopHookMotor();
           drivetrain.setMotors(0.5, 0.5);
        } 
        else if (currentTime - this.initialTime >= Constants.CLIMB_DRIVE_TIME) {
           drivetrain.setMotors(0.0, 0.0);
        }
    }
}
