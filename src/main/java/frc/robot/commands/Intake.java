package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

// TODO: intake system has a moving arm, will need to add to the subsystem
public class Intake extends CommandBase {
    private final IntakeSubsystem intakeSubsystem;
    private final XboxController controller; // TODO: may need to change to GenericHID
    private final int forwardButton;
    private final int reverseButton;
   
    public Intake(IntakeSubsystem intakeSubsystem, XboxController controller, int forwardButton, int reverseButton) {
        this.intakeSubsystem = intakeSubsystem;
        this.controller = controller;
        this.forwardButton = forwardButton;
        this.reverseButton = reverseButton;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        boolean forward = controller.getRawButton(forwardButton);
        boolean reverse = controller.getRawButton(reverseButton);

        if (forward & !reverse) {
            // TODO: lower arm 
            intakeSubsystem.setIntakeMotors(1.0);
            // TODO: lower arm 
        } else if (!forward & reverse) {
            intakeSubsystem.setIntakeMotors(-1.0);
        } else {
            intakeSubsystem.setIntakeMotors(0);
        }
    }

    @Override
    public void initialize() {
        
    }
   
    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.setIntakeMotors(0);
    }
   
    @Override
    public boolean isFinished() {
        return false;
    }
   
}
