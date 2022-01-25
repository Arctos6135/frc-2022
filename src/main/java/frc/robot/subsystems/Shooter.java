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

    private final MonitoredCANSparkMaxGroup monitorGroup; 

    private static final double kP = 0, kI = 0, kD = 0, kF = 0;

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

    
    
}
