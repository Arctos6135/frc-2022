// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.arctos6135.robotlib.logging.RobotLogger;
import com.arctos6135.robotlib.newcommands.triggers.AnalogTrigger;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.TeleopDrive;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drivetrain drivetrain;

  private static final XboxController driverController = new XboxController(Constants.XBOX_DRIVER); 
  private static final XboxController operatorController = new XboxController(Constants.XBOX_OPERATOR); 

  private static final RobotLogger logger = new RobotLogger();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    drivetrain = new Drivetrain(Constants.RIGHT_CANSPARKMAX, Constants.LEFT_CANSPARKMAX,
        Constants.RIGHT_CANSPARKMAX_FOLLOWER, Constants.LEFT_CANSPARKMAX_FOLLOWER);
        



    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    Button reverseDriveButton = new JoystickButton(driverController, Constants.REVERSE_DRIVE_DIRECTION);
    Button dtOverheatOverrideButton = new JoystickButton(driverController, Constants.OVERRIDE_MOTOR_PROTECTION);
    Button precisionDriveButton = new JoystickButton(driverController, Constants.PRECISION_DRIVE_TOGGLE);
    AnalogTrigger precisionDriveTrigger = new AnalogTrigger(driverController, Constants.PRECISION_DRIVE_HOLD, 0.5);

    // Driver Button Bindings 
    // TODO: add tab entries on shuffle board
    reverseDriveButton.whenPressed(() -> {
      TeleopDrive.toggleReverseDrive();
      getLogger().logInfo("Drive reverse set to " + TeleopDrive.isReversed());
    });

    precisionDriveButton.whenPressed(() -> {
      TeleopDrive.togglePrecisionDrive();
    });
    
    precisionDriveTrigger.setMinTimeRequired(0.05);
    precisionDriveTrigger.whileActiveOnce(new FunctionalCommand(() -> {
        TeleopDrive.togglePrecisionDrive();
      }, () -> {
      }, (interrupted) -> {
        TeleopDrive.togglePrecisionDrive();
    }, () -> false));

    dtOverheatOverrideButton.whenPressed(() -> {
      // Toggle between overriding and not overriding overheat shutoff.
      boolean override = !drivetrain.getOverheatShutoffOverride();
      drivetrain.setOverheatShutoffOverride(override);
      
      if (override) {
        getLogger().logWarning("Drivetrain motor temperature protection overridden.");
      } else {
        getLogger().logInfo("Drivetrain motor temperature protection re-enabled."); 
      }
    });
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // TODO: implement an autonomous command 
    return new InstantCommand();
  }
  
  /**
   * Get the robot logger. 
   * 
   * @return the robot logger.
   */
  public static RobotLogger getLogger() {
    return logger; 
  }
  
}
