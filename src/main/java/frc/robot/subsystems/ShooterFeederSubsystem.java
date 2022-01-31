package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ShooterFeederSubsystem extends SubsystemBase {
    
    private final CANSparkMax rollerMotor;
    private final ColorSensorV3 colorSensor; 

    private double rollSpeed = 0; 
    private boolean rollUpwards = true; 

    public ShooterFeederSubsystem(int rollerMotor) {
        this.rollerMotor = new CANSparkMax(rollerMotor, MotorType.kBrushless); 
        this.rollerMotor.setIdleMode(IdleMode.kBrake); 

        this.colorSensor = new ColorSensorV3(I2C.Port.kOnboard); 
        this.rollSpeed = Constants.ROLL_SPEED; 
    }

    /**
     * Get the color sensor used by the shooter feeder subsystem. 
     * 
     * @return the color sensor used by the shooter feeder subsystem. 
     */
    public ColorSensorV3 getColorSensor() {
        return this.colorSensor; 
    }

    public Color getColorDetected() {
        return this.colorSensor.getColor();
    }

    /**
     * Start rolling the flat belts to move balls upwards. 
     * 
     * @param rollSpeed speed of the roller motors. 
     */
    public void startRoller() {
        if (rollUpwards) {
            rollerMotor.set(this.rollSpeed); 
        } else {
            rollerMotor.set(-this.rollSpeed); 
        }
    }

    /**
     * Stops rolling the flat belt motors. 
     */
    public void stopRoller() {
        rollerMotor.stopMotor();
    }

    /**
     * Get the speed that the belts are rolling at. 
     * 
     * @return the speed of the roller motor. 
     */
    public double getRollSpeed() {
        return rollUpwards ? rollSpeed : -rollSpeed; 
    }

    /**
     * Set the speed that the belts are rolling at. 
     * 
     * @param rollSpeed the desired speed of the roller motor. 
     */
    public void setRollSpeed(double rollSpeed) {
        this.rollSpeed = rollSpeed; 
    }

    /**
     * Get the direction the belts are rolling. 
     * 
     * @return the direction of the roller belts. 
     */
    public boolean getRollDirection() {
        return this.rollUpwards; 
    }

    /**
     * Set whether the belts should roll upwards or downwards. 
     * 
     * @param rollUpwards whether the belts should roll upwards.
     */
    public void setRollDirection(boolean rollUpwards) {
        this.rollUpwards = rollUpwards; 
    }

    /**
     * Toggle between rotating the belts upwards and downwards. 
     */
    public void toggleRollDirection() {
        this.rollUpwards = !this.rollUpwards;
    }

}
