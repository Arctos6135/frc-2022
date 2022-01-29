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
    private double velocity = 0; 

    private final MonitoredCANSparkMaxGroup monitorGroup;
    
    private boolean protectionOverridden = false; 

    private static final double kP = 0, kI = 0, kD = 0, kF = 0;

    /**
     * Create new shooter subsystem instance. 
     * 
     * @param masterMotor the PDP pin of the master shooter motor. 
     * @param followerMotor the PDP pin of the follower shooter motor. 
     */
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

    /**
     * Get the shooter encoder. 
     * 
     * @return the shooter encoder. 
     */
    public CANEncoder getEncoder() {
        return this.shooterEncoder; 
    }

    /**
     * Get the shooter RPM from the encoder. 
     * 
     * @return the shooter RPM. 
     */
    public double getVelocity() {
        return this.shooterEncoder.getVelocity(); 
    }

    /**
     * Get the desired velocity of the shooter. 
     * 
     * @return the desired shooter RPM. 
     */
    public double getVelocitySetpoint() {
        return this.velocity; 
    }

    /**
     * Set the velocity of the shooter (RPM). 
     * 
     * @param rpm the required velocity of the shooter. 
     */
    public void setVelocity(double rpm) {
        this.pidController.setReference(monitorGroup.getOverheatShutoff() && !protectionOverridden 
        ? 0 : rpm, ControlType.kVelocity);
        this.velocity = rpm; 
    }

    /**
     * Get the PID controller used by the shooter. 
     * 
     * @return the shooter PID controller. 
     */
    public CANPIDController getPIDController() {
        return this.pidController; 
    }

    /**
     * Get the Spark MAX Monitor. 
     * 
     * @return the monitor group for the shooter. 
     */
    public MonitoredCANSparkMaxGroup getMonitoredCANSparkMaxGroup() {
        return this.monitorGroup; 
    }

    /**
     * Get whether the shooter is overriding overheating protection. 
     * 
     * @return whether the shooter overrides protection. 
     */
    public boolean getOverheatShutoffOverride() {
        return this.protectionOverridden; 
    }

    /**
     * Set whether the shooter is overriding overheating protection. 
     * 
     * @param protectionOverridden whether the shooter overrides protection. 
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

    /**
     * Write the settings of the shooter SPARK MAX motors. 
     */
    public void burnFlash() {
        masterShooterMotor.burnFlash(); 
        followerShooterMotor.burnFlash(); 
    }
    
    
}
