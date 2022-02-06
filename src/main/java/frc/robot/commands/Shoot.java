package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shooter;

public class Shoot extends CommandBase {
    
    private static final double VELOCITY_TOLERANCE = 100; // TODO: will probably change 
    private final Shooter shooter; 

    private double targetVelocity = 0; 
    private boolean velocityReached = false; 
    private boolean lowerHub; 

    private boolean finished = false; 

    public Shoot(Shooter shooter, boolean lowerHub) {
        this.shooter = shooter; 
        this.lowerHub = lowerHub; 
        addRequirements(shooter);
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
        if (Math.abs(shooter.getVelocity() - targetVelocity) < VELOCITY_TOLERANCE) {
            // Shoot the ball 
            velocityReached = true; 

            if (lowerHub && shooter.shooterReady) {
                try {
                    shooter.fire(false); 
                } catch (Shooter.PowerException exception) {
                    RobotContainer.getLogger().logError("THe shooter motor cannot support the lower hub shot!");
                }
            } else if (!lowerHub && shooter.shooterReady) {
                try {
                    shooter.fire(true); 
                } catch (Shooter.PowerException exception) {
                    RobotContainer.getLogger().logError("The shooter motor cannot support the upper hub shot!"); 
                }
            } else {
                RobotContainer.getLogger().logError("The shooter is not ready to shoot any shot!"); 
            }
            
        } else {
            // Stop feeding balls 

            // A ball was shot 
            if (velocityReached) {

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
