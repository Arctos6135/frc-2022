package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterFeederSubsystem;

public class Shoot extends CommandBase {
    
    private static final double VELOCITY_TOLERANCE = 100; // TODO: will probably change 
    
    private final Shooter shooter; 
    private final ShooterFeederSubsystem shooterFeederSubsystem; 

    private double targetVelocity = 0; 
    private boolean velocityReached = false; 

    private boolean finished = false; 

    public Shoot(Shooter shooter, ShooterFeederSubsystem shooterFeederSubsystem) {
        this.shooter = shooter; 
        this.shooterFeederSubsystem = shooterFeederSubsystem; 
        shooterFeederSubsystem.setRollDirection(true);

        addRequirements(shooter, shooterFeederSubsystem);
    }

    @Override 
    public void initialize() {
        finished = false; 
        velocityReached = false; 

        // TODO: check for aiming via Limelight 
        if (!shooter.getOverheatShutoffOverride() && shooter.getMonitorGroup().getOverheatShutoff()) {
            finished = true; 
            RobotContainer.getLogger().logError("Shooter is overheating, cannot shoot."); 
        }

    }

    @Override 
    public void execute() {
        // Check if a ball is in the feeder. 
        if (!this.shooterFeederSubsystem.getBallInShotPosition()) {
            finished = true; 
            RobotContainer.getLogger().logError("Shooter has no ball to shoot."); 
        } else {
            if (Math.abs(shooter.getVelocity() - targetVelocity) < VELOCITY_TOLERANCE) {
                // Shoot the ball 
                velocityReached = true; 
                shooterFeederSubsystem.startRoller(); 

            } else {
                // Stop feeding balls 
                shooterFeederSubsystem.stopRoller();
                // A ball was shot 
                if (velocityReached) {
                    shooterFeederSubsystem.decrementBallCount();
                }

                velocityReached = false; 
            }
        }

            
    }

    @Override 
    public void end(boolean interrupted) {
        // Stop the feeder 
        shooter.setVelocity(0);
    }

    @Override 
    public boolean isFinished() {
        return finished; 
    }
}
