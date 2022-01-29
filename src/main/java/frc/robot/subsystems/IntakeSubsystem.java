package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * The intake subsystem.
 */
public class IntakeSubsystem extends SubsystemBase {
    // No Pneumatics

    // Motors to Spin Mecanum Wheels
    private final CANSparkMax leftIntakeMotor;
    private final CANSparkMax rightIntakeMotor;
    private IdleMode idleMode;

    /**
     * Set the speed of the intake motors.
     *
     * @param scale the speed of the motors.
     */
    public void setIntakeMotors(double scale) {
        // TODO: may need to monitor these motors
        leftIntakeMotor.set(scale);
        rightIntakeMotor.set(scale);
    }

    /**
     * Get the Idle Mode of the Intake motors (brake/coast).
     *
     * @return the intake motor idle mode.
     */
    public IdleMode getIntakeIdleMode() {
        return this.idleMode;
    }
   
    /**
     * Set the Idle Mode of the Intake motors (brake/coast).
     *
     * @param idleMode the idle mode.
     */
    public void setIntakeMotorMode(IdleMode idleMode) {
        this.idleMode = idleMode;
        leftIntakeMotor.setIdleMode(idleMode);
        rightIntakeMotor.setIdleMode(idleMode);
    }
   
    /**
     * Create a new intake subsystem.
     *
     * @param leftIntake the CAN ID of the left intake motor controller.
     * @param rightIntake the CAN ID of the right intake motor controller.
     */
    public IntakeSubsystem(int leftIntake, int rightIntake) {
        this.leftIntakeMotor = new CANSparkMax(leftIntake, MotorType.kBrushless);
        this.rightIntakeMotor = new CANSparkMax(rightIntake, MotorType.kBrushless);
       
        // Invert the left motor to spin in the same direction.
        leftIntakeMotor.setInverted(true);
        rightIntakeMotor.setInverted(false);

        leftIntakeMotor.stopMotor();
        rightIntakeMotor.stopMotor();
    }
}
