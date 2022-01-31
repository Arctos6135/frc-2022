package frc.robot.util;

import com.revrobotics.CANPIDController;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * A CANPIDController that implements Sendable. Appears as a PID Controller on Shuffleboard. 
 * 
 * Allows for tuning values in the PIDF controller and PID setpoint values to be modified 
 * via the Shuffleboard. 
 */
public class SendableCANPIDController implements Sendable {
    
    private CANPIDController controller;
    private double setpoint = 0; 
    private boolean enabled = false; 

    public SendableCANPIDController(CANPIDController controller) {
        this.controller = controller; 
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("PIDController");
        
        builder.addDoubleProperty("p", controller::getP, controller::setP);
        builder.addDoubleProperty("i", controller::getI, controller::setI); 
        builder.addDoubleProperty("d", controller::getD, controller::setD);
        builder.addDoubleProperty("f", controller::getFF, controller::setFF); 
        
        builder.addDoubleProperty("setpoint", () -> {
            return this.setpoint; 
        }, (setpoint) -> {
            this.setpoint = setpoint; 

            if (enabled) {
                this.controller.setReference(setpoint, ControlType.kVelocity); 
            }
        });

        builder.addBooleanProperty("enabled", () -> {
            return this.enabled; 
        }, (enabled) -> {
            this.enabled = enabled; 

            if (enabled) {
                this.controller.setReference(setpoint, ControlType.kVelocity); 
            } else {
                controller.setReference(0, ControlType.kVelocity);
            }
        }); 
    } 
}
