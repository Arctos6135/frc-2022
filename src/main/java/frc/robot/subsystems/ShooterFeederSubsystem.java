package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterFeederSubsystem extends SubsystemBase {
    
    private final CANSparkMax rollerMotor;
    private final ColorSensorV3 colorSensor; 

    public ShooterFeederSubsystem(int rollerMotor) {
        this.rollerMotor = new CANSparkMax(rollerMotor, MotorType.kBrushless); 
        this.rollerMotor.setIdleMode(IdleMode.kBrake); 

        this.colorSensor = new ColorSensorV3(I2C.Port.kOnboard); 
    }

    /**
     * Get the color sensor used by the shooter feeder subsystem. 
     * 
     * @return the color sensor used by the shooter feeder subsystem. 
     */
    public ColorSensorV3 getColorSensor() {
        return this.colorSensor; 
    }

    /**
     * Start rolling the flat belts to move balls upwards. 
     * 
     * @param rollSpeed speed of the roller motors. 
     */
    public void startRollers(int rollSpeed) {
        rollerMotor.set(rollSpeed); 
    }

    /**
     * Stops rolling the flat belt motors. 
     */
    public void stopRollers() {
        rollerMotor.stopMotor();
    }
}
