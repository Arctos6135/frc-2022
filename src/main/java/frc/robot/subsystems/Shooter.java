package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private final CANSparkMax shooterMotor;
    
    public Shooter(int shooterMotor) {
        this.shooterMotor = new CANSparkMax(shooterMotor, MotorType.kBrushless);
    }
    
    public void setShooterSpeed(double shooterSpeed) {
        this.shooterMotor.set(shooterSpeed);
    }
}