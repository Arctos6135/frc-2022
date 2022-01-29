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

	final CANSparkMax masterShooterMotor;
	final CANSparkMax followerShooterMotor;
	final CANEncoder shooterEncoder;
	final CANPIDController pidController;

	public static final double BASE_SPEED = 0;
	public boolean shooterReady = false;
	public double  shooterDist;
	double velocity = 0;

	final MonitoredCANSparkMaxGroup monitorGroup;
	
	boolean protectionOverridden = false;

	static final double kP = 0, kI = 0, kD = 0, kF = 0;

	public Shooter(int masterMotor, int followerMotor) {
		this.masterShooterMotor = new CANSparkMax(masterMotor, MotorType.kBrushless);
		this.followerShooterMotor = new CANSparkMax(followerMotor, MotorType.kBrushless);

		monitorGroup = new MonitoredCANSparkMaxGroup("Shooter", Constants.MOTOR_WARNING_TEMP, Constants.MOTOR_SHUTOFF_TEMP,
		this.masterShooterMotor, this.followerShooterMotor);

		this.shooterEncoder = masterShooterMotor.getEncoder(EncoderType.kHallSensor, Constants.COUNTS_PER_REVOLUTION);

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

	public CANEncoder getEncoder() {
		return this.shooterEncoder;
	}

	// angular velocities must be converted from rpm

	public double getVelocity() {
		return this.shooterEncoder.getVelocity()*2*Math.pi/60;
	}

	public double getVelocitySetpoint() {
		return this.velocity;
	}

	public void setVelocity(double v) {
		this.pidController.setReference(monitorGroup.getOverheatShutoff() && !protectionOverridden
		? 0 : v, ControlType.kVelocity);
		this.velocity = v*2*Math.pi/60;
	}

	public CANPIDController getPIDController() {
		return this.pidController;
	}

	public MonitoredCANSparkMaxGroup getMonitorGroup() {
		return this.monitorGroup;
	}

	public boolean getOverheatShutoffOverride() {
		return this.protectionOverridden;
	}

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
	
	double shooterDistToPower(double x, double y) {
		// angle is 1.222, sec(1.222) = 1.926, csc2(1.222) = 1.132
		return 1.132*9.807*x*x/(1.926*x-y)*Constants.BALL_MASS;
	}

	void fire(boolean upper) {
		double power = shooterDistToPower(shooterDist, upper ? Constants.UPPER_HUB : Constants.LOWER_HUB);
		
		// fire at power calculated
	}
}
