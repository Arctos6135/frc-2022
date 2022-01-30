package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterFeederSubsystem extends SubsystemBase {
    
    private final CANSparkMax bottomRollerMotor; 
    private final CANSparkMax topRollerMotor; 

    public ShooterFeederSubsystem(int bottomRollerMotor, int topRollerMotor) {
        this.bottomRollerMotor = new CANSparkMax(bottomRollerMotor, MotorType.kBrushless); 
        this.topRollerMotor = new CANSparkMax(topRollerMotor, MotorType.kBrushless); 

        this.bottomRollerMotor.follow(this.topRollerMotor); 

        this.topRollerMotor.setIdleMode(IdleMode.kBrake); 
        this.bottomRollerMotor.setIdleMode(IdleMode.kBrake); 
    }

    /**
     * Start rolling the flat belts to move balls upwards. 
     * 
     * @param rollSpeed speed of the roller motors. 
     */
    public void startRollers(int rollSpeed) {
        topRollerMotor.set(rollSpeed); 
    }

    /**
     * Stops rolling the flat belt motors. 
     */
    public void stopRollers() {
        topRollerMotor.stopMotor();
    }
}
