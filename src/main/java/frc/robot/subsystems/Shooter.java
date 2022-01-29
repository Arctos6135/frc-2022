package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.EncoderType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.MonitoredCANSparkMaxGroup;

public class Shooter extends SubsystemBase {

	private final CANSparkMax masterShooterMotor;
	private final CANSparkMax followerShooterMotor;
	private final CANEncoder shooterEncoder;
	private final CANPIDController pidController;

	public static final double BASE_SPEED = 0;
	public boolean shooterReady = false;
	public double shooterDist;
	private double velocity = 0;

	private final MonitoredCANSparkMaxGroup monitorGroup;
	
	boolean protectionOverridden = false;

	static final double kP = 0, kI = 0, kD = 0, kF = 0;

	public Shooter(int masterMotor, int followerMotor) {
		this.masterShooterMotor = new CANSparkMax(masterMotor, MotorType.kBrushless);
		this.followerShooterMotor = new CANSparkMax(followerMotor, MotorType.kBrushless);

		monitorGroup = new MonitoredCANSparkMaxGroup("Shooter", Constants.MOTOR_WARNING_TEMP, Constants.MOTOR_SHUTOFF_TEMP,
		this.masterShooterMotor, this.followerShooterMotor);

		this.shooterEncoder = masterShooterMotor.getEncoder(EncoderType.kHallSensor, Constants.COUNTS_PER_REVOLUTION);
		this.shooterEncoder.setVelocityConversionFactor(2 * Math.PI / 60); // TODO: may not need conversion from RPM

		this.pidController = masterShooterMotor.getPIDController();

		// Spin Motors in the Same Direction
		this.masterShooterMotor.setInverted(false);
		this.followerShooterMotor.setInverted(true);

		this.followerShooterMotor.follow(this.masterShooterMotor);

		// Set Constants for the Shooter PID Controller
		pidController.setP(kP);
		pidController.setI(kI);
		pidController.setD(kD);
		pidController.setFF(kF);

		pidController.setOutputRange(-1.0, 1.0);
		pidController.setReference(0.0, ControlType.kVelocity);
	}

	/**
	 * Get the shooter encoder. 
	 * 
	 * @return the shooter encoder (from the main shooter motor). 
	 */
	public CANEncoder getEncoder() {
		return this.shooterEncoder;
	}

	// angular velocities must be converted from rpm

	/**
	 * Get the shooter velocity in RPM. 
	 * 
	 * Velocity in RPM is easier to use with the PID controller. 
	 * 
	 * @return the velocity read by the shooter encoder. 
	 */
	public double getVelocity() {
		return this.shooterEncoder.getVelocity();
	}

	/**
	 * Get the velocity setpoint for the shooter. 
	 * 
	 * @return the desired velocity of the shooter motors. 
	 */
	public double getVelocitySetpoint() {
		return this.velocity;
	}

	/**
	 * Set the desired velocity for the shooter in RPM. 
	 * 
	 * @param rpm the velocity setpoint for the shooter motors. 
	 */
	public void setVelocity(double rpm) {
		this.pidController.setReference(monitorGroup.getOverheatShutoff() && !protectionOverridden
		? 0 : rpm, ControlType.kVelocity);
		this.velocity = rpm;   
	}

	/**
	 * Get the PID controller from the shooter motors. 
	 * 
	 * @return
	 */
	public CANPIDController getPIDController() {
		return this.pidController;
	}

	/**
	 * Get the Monitor Group of the shooter motors. 
	 * 
	 * @return the shooter monitor group. 
	 */
	public MonitoredCANSparkMaxGroup getMonitorGroup() {
		return this.monitorGroup;
	}

	/**
	 * Get whether the shooter monitor group protection has been overridden. 
	 * 
	 * @return whether the overheating protection has been overridden. 
	 */
	public boolean getOverheatShutoffOverride() {
		return this.protectionOverridden;
	}

	/**
	 * Set whether the shooter monitor group protection should be overridden. 
	 * 
	 * @param protectionOverridden whether the overheating protection should be overridden. 
	 */
	public void setOverheatShutoffOverride(boolean protectionOverridden) {
		this.protectionOverridden = protectionOverridden;
	}

	@Override
	public void periodic() {
		this.monitorGroup.monitorOnce();

		if (this.monitorGroup.getOverheatShutoff()) {
			setVelocity(0);
			masterShooterMotor.stopMotor();
		}
	}

	// settings of the shooter SPARK MAX motors.
	public void burnFlash() {
		masterShooterMotor.burnFlash();
		followerShooterMotor.burnFlash();
	}
	
	public double shooterDistToPower(double x, double y) {
		// angle is 1.222, sec(1.222) = 1.926, csc2(1.222) = 1.132
		return 1.132*9.807*x*x/(1.926*x-y)*Constants.BALL_MASS;
	}

	public void fire(boolean upper) {
		double power = shooterDistToPower(shooterDist, upper ? Constants.UPPER_HUB : Constants.LOWER_HUB);
		
		// fire at power calculated
	}
}
