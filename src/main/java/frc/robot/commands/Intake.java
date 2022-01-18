package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

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
            intakeSubsystem.setIntakeMotors(1.0);
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
