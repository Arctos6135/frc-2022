package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shooter;

public class Shoot extends CommandBase {
    
    private static final double VELOCITY_TOLERANCE = 100; // TODO: will probably change 
    private final Shooter shooter; 

    private double targetVelocity = 0; 
    private boolean velocityReached = false; 

    private boolean finished = false; 

    public Shoot(Shooter shooter) {
        this.shooter = shooter; 
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
