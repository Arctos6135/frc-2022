package frc.robot;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import com.arctos6135.robotlib.logging.RobotLogger;
import com.arctos6135.robotlib.newcommands.triggers.AnalogTrigger;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import frc.robot.commands.Intake;
import frc.robot.commands.SensoredRoll; 
import frc.robot.commands.Shoot;
import frc.robot.commands.TeleopDrive;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterFeederSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

// Contains all commands and subsystems
public class RobotContainer {
	// The robot's subsystems and commands are defined here...
	private final Drivetrain drivetrain;
	private final IntakeSubsystem intakeSubsystem;
	private final ShooterFeederSubsystem shooterFeederSubsystem; 
	private final Shooter shooterSubsystem;

	private static final XboxController driverController = new XboxController(Constants.XBOX_DRIVER);
	private static final XboxController operatorController = new XboxController(Constants.XBOX_OPERATOR);

	// Shuffleboard Tabs
	final ShuffleboardTab configTab;
	final ShuffleboardTab driveTab;
	final ShuffleboardTab colorTab; 
	final ShuffleboardTab prematchTab;
	final ShuffleboardTab debugTab;

	// Network Tables for Smart Dashboard
	NetworkTableEntry driveReversedEntry;
	NetworkTableEntry precisionDriveEntry;
	NetworkTableEntry overrideModeEntry;
	
	// Logging Related
	NetworkTableEntry lastError;
	NetworkTableEntry lastWarning;
	
	// Drivetrain Status
	SimpleWidget drivetrainMotorStatus;
	SimpleWidget shooterMotorStatus;

	static final RobotLogger logger = new RobotLogger();

	// contains subsystems, OI devices, and commands
	public RobotContainer() {
		drivetrain = new Drivetrain(Constants.RIGHT_CANSPARKMAX, Constants.LEFT_CANSPARKMAX,
			Constants.RIGHT_CANSPARKMAX_FOLLOWER, Constants.LEFT_CANSPARKMAX_FOLLOWER);
		drivetrain.setDefaultCommand(
			new TeleopDrive(drivetrain, driverController, Constants.DRIVE_FWD_REV, Constants.DRIVE_LEFT_RIGHT));

		intakeSubsystem = new IntakeSubsystem(Constants.LEFT_INTAKE_MOTOR, Constants.RIGHT_INTAKE_MOTOR);
		intakeSubsystem.setDefaultCommand(
			new Intake(intakeSubsystem, driverController, Constants.INTAKE_FORWARD_BUTTON, Constants.INTAKE_REVERSE_BUTTON)
		);

		shooterFeederSubsystem = new ShooterFeederSubsystem(Constants.ROLLER_MOTOR); 
		shooterFeederSubsystem.setDefaultCommand(
			new SensoredRoll(shooterFeederSubsystem) 
		);

		shooterSubsystem = new Shooter(Constants.MAIN_SHOOTER_MOTOR, Constants.AUXILLIARY_SHOOTER_MOTOR);
		shooterSubsystem.setDefaultCommand(
			new Shoot(shooterSubsystem, shooterFeederSubsystem)
		);

		// Shuffle Board Tabs
		configTab = Shuffleboard.getTab("Config");
		driveTab = Shuffleboard.getTab("Drive");
		colorTab = Shuffleboard.getTab("Color"); 
		prematchTab = Shuffleboard.getTab("Pre-match");
		debugTab = Shuffleboard.getTab("Debug");

		configureDashboard();

		while (!DriverStation.getInstance().isDSAttached()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		initLogger();
		
		// Configure the button bindings
		configureButtonBindings();
	}

	void configureDashboard() {
		// Configure Tabs
		configTab.add("Precision Drive Factor", TeleopDrive.getPrecisionFactor()).withPosition(0, 0).withSize(9, 4)
		.withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0.0, "max", 1.0)).getEntry()
		.addListener(notif -> {
			TeleopDrive.setPrecisionFactor(notif.value.getDouble());
				}, EntryListenerFlags.kUpdate);
		
		configTab.add("Motor Ramping Rate", TeleopDrive.getRampingRate()).withPosition(9, 0).withSize(9, 4)
		.withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0.0, "max", 1.0)).getEntry()
		.addListener(notif -> {
			TeleopDrive.setRampingRate(notif.value.getDouble());
				}, EntryListenerFlags.kUpdate);
				
		configTab.add("Motor Warning Temp", Constants.MOTOR_WARNING_TEMP).withWidget(BuiltInWidgets.kNumberSlider)
		.withPosition(18, 0).withSize(9, 4).withProperties(Map.of("min", 0.0, "max", 150.0)).getEntry()
		.addListener(notif -> {
			Constants.MOTOR_WARNING_TEMP = notif.value.getDouble();
				}, EntryListenerFlags.kUpdate);
				
		configTab.add("Motor Shutoff Temp", Constants.MOTOR_SHUTOFF_TEMP).withWidget(BuiltInWidgets.kNumberSlider)
		.withPosition(27, 0).withSize(9, 4).withProperties(Map.of("min", 0.0, "max", 150.0)).getEntry()
		.addListener(notif -> {
			Constants.MOTOR_SHUTOFF_TEMP = notif.value.getDouble();
				}, EntryListenerFlags.kUpdate);
				
		// Write Settings of Spark Max Motors on Drivetrain and Shooter 
		InstantCommand burnFlashCommand = new InstantCommand(() -> {
			drivetrain.burnFlash(); 
			shooterSubsystem.burnFlash(); 
		}); 
		burnFlashCommand.setName("Burn Flash");
		configTab.add("Burn Spark Motors", burnFlashCommand).withWidget(BuiltInWidgets.kCommand).withPosition(36, 0); 

		// Drive Tabs
		driveTab.add("Gyro", drivetrain.getAHRS()).withWidget(BuiltInWidgets.kGyro).withPosition(0, 4).withSize(9, 10);

		// Driving Related Entries
		driveReversedEntry = driveTab.add("Reversed", TeleopDrive.isReversed()).withWidget(BuiltInWidgets.kBooleanBox).withPosition(0, 0).withSize(4, 4).getEntry();
		
		precisionDriveEntry = driveTab.add("Precision", TeleopDrive.isPrecisionDrive()).withWidget(BuiltInWidgets.kBooleanBox).withPosition(4, 0).withSize(4, 4).getEntry();
		
		// Color Detection of Balls 
		colorTab.add("Red Color", shooterFeederSubsystem.getColorSensor().getRed());
		colorTab.add("Blue Color", shooterFeederSubsystem.getColorSensor().getBlue());
		colorTab.add("Unknown Color", shooterFeederSubsystem.getColorSensor().getIR()); 

		// Overheating Warnings
		drivetrain.getMonitorGroup().setOverheatShutoffCallback((motor, temp) -> {
			if (!drivetrain.getOverheatShutoffOverride()) {
				drivetrainMotorStatus.withProperties(Map.of("color when false", Constants.COLOR_MOTOR_SHUTOFF)).getEntry().setBoolean(false);
			}
			getLogger().logError("Drivetrain motor " + motor.getDeviceId() + " reached overheat shutoff limit at " + temp + "C!");
		});

		drivetrain.getMonitorGroup().setOverheatWarningCallback((motor, temp) -> {
			if (!drivetrain.getOverheatShutoffOverride())
				drivetrainMotorStatus.withProperties(Map.of("color when false", Constants.COLOR_MOTOR_WARNING)).getEntry().setBoolean(false);
			getLogger().logWarning("Drivetrain motor " + motor.getDeviceId() + " reached overheat warning at " + temp + " C!");
		});

		drivetrain.getMonitorGroup().setNormalTempCallback(() -> drivetrainMotorStatus.getEntry().setBoolean(true));

		shooterSubsystem.getMonitorGroup().setOverheatShutoffCallback((motor, temp) -> {
			if (!shooterSubsystem.getOverheatShutoffOverride())
				shooterMotorStatus.withProperties(Map.of("color when false", Constants.COLOR_MOTOR_SHUTOFF)).getEntry().setBoolean(false);
			getLogger().logError("Shooter motor " + motor.getDeviceId() + " reched overheat shutoff limit at " + temp + "C!");
		});

		shooterSubsystem.getMonitorGroup().setOverheatWarningCallback((motor, temp) -> {
			if (!shooterSubsystem.getOverheatShutoffOverride())
				shooterMotorStatus.withProperties(Map.of("color when false", Constants.COLOR_MOTOR_WARNING)).getEntry().setBoolean(false);
			getLogger().logWarning("Shooter motor " + motor.getDeviceId() + " reached overheat warning at " + temp + " C!");
		});
	}

	private void configureButtonBindings() {
		// Driving Related 
		Button reverseDriveButton = new JoystickButton(driverController, Constants.REVERSE_DRIVE_DIRECTION);
		Button dtOverheatOverrideButton = new JoystickButton(driverController, Constants.OVERRIDE_MOTOR_PROTECTION);
		Button precisionDriveButton = new JoystickButton(driverController, Constants.PRECISION_DRIVE_TOGGLE);
		// TODO: connect to shooter data
		// Shooter Related 
		Button shooterSpeed = new JoystickButton(operatorController, Constants.SHOOTER_SPEED_BUTTON);
		Button deployShooter = new JoystickButton(operatorController, Constants.DEPLOY_SHOOTER_BUTTON);
		Button prepareShooter = new JoystickButton(operatorController, Constants.PREPARE_SHOOTER_BUTTON);
		
		AnalogTrigger precisionDriveTrigger = new AnalogTrigger(driverController, Constants.PRECISION_DRIVE_HOLD, 0.5);

		// Driver Button Bindings
		reverseDriveButton.whenPressed(() -> {
			TeleopDrive.toggleReverseDrive();
			getLogger().logInfo("Drive reverse set to " + TeleopDrive.isReversed());
		});

		precisionDriveButton.whenPressed(() -> {
			TeleopDrive.togglePrecisionDrive();
		});
		
		precisionDriveTrigger.setMinTimeRequired(0.05);
		precisionDriveTrigger.whileActiveOnce(new FunctionalCommand(() -> TeleopDrive.togglePrecisionDrive(), () -> {}, (interrupted) -> TeleopDrive.togglePrecisionDrive(), () -> false));

		dtOverheatOverrideButton.whenPressed(() -> {
			// Toggle overheat shutoff override
			boolean override = !drivetrain.getOverheatShutoffOverride();
			drivetrain.setOverheatShutoffOverride(override);
			
			if (override) {
				getLogger().logWarning("Drivetrain motor temperature protection overridden.");
			} else {
				getLogger().logInfo("Drivetrain motor temperature protection re-enabled.");
			}
		});

		shooterSpeed.whileHeld(() -> {
			// modify to multiply by some constant, post-assembly.
			// shooterSubsystem.shooterSpeed = shooterSubsystem.getVelocity();

		}); 

		prepareShooter.whenPressed(() -> {
			// prepare shooter
			shooterSubsystem.shooterReady = true;
		});

		deployShooter.whileActiveOnce(
			new Shoot(shooterSubsystem, shooterFeederSubsystem)
			// TODO: check if shooter is ready in the Shoot command
		); 

	}

	public Command getAutonomousCommand() {
		return new InstantCommand();
	}

	private void initLogger() {
		try {
			logger.init(Robot.class, new File(Filesystem.getOperatingDirectory().getCanonicalPath() + "/frc-robot-logs"));

			logger.setLevel(Level.FINE);
			
			logger.setLogHandler((level, message) -> {
				if (level == Level.SEVERE) {
					lastError.setString(message);
				} else if (level == Level.WARNING) {
					lastWarning.setString(message);
				}
			});

			logger.cleanLogs(72);
			logger.logInfo("Logger initialized");
			
		} catch (IOException e) {
			e.printStackTrace();
			lastError.setString("Failed to initialize logger");
		}
	}
	
	public static RobotLogger getLogger() {
		return logger;
	}
}