// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drivetrain extends SubsystemBase {

  // Motor Controllers 
  private final CANSparkMax rightMotor;
  private final CANSparkMax leftMotor;
  private final CANSparkMax rightFollowerMotor;
  private final CANSparkMax leftFollowerMotor;
  
  // Encoders 
  private final CANEncoder rightEncoder;
  private final CANEncoder leftEncoder;
  
  // TODO: may need a subsystem or utility to monitor the motor controllers 

  // Adjusts speeds.
  private double speedMultiplier = 1.0;
  
  /**
   * Set the speed multiplier. 
   * 
   * <p>
   * Speeds passed to the motors are multiplied by this.
   * </p> 
   * 
   * @param speedMultiplier the speed multiplier.
   */
  public void setSpeedMultiplier(double speedMultiplier) {
    this.speedMultiplier = speedMultiplier;
  }
  
  /**
   * Get the speed multiplier. 
   * 
   * @return the speed multiplier.
   */
  public double getSpeedMultiplier() {
    return this.speedMultiplier; 
  }


  public void setMotors(double left, double right) {
    
  }

  /**
   * Creates a new drivetrain.
   * 
   * @param rightMaster   the corresponding PDP port for the right motor
   *                      controller.
   * @param leftMaster    the corresponding PDP port for the left motor
   *                      controller.
   * @param rightFollower the corresponding PDP port for the right follower motor
   *                      controller.
   * @param leftFollower  the corresponding PDP port for the left follower motor
   *                      controller.
   */
  public Drivetrain(int rightMaster, int leftMaster, int rightFollower, int leftFollower) {
    // Motor Instantiation
    rightMotor = new CANSparkMax(rightMaster, MotorType.kBrushless);
    leftMotor = new CANSparkMax(leftMaster, MotorType.kBrushless);
    rightFollowerMotor = new CANSparkMax(rightFollower, MotorType.kBrushless);
    leftFollowerMotor = new CANSparkMax(leftFollower, MotorType.kBrushless);
    
    // Encoder Instantiation 
    rightEncoder = rightMotor.getEncoder(EncoderType.kHallSensor, Constants.COUNTS_PER_REVOLUTION);
    leftEncoder = leftMotor.getEncoder(EncoderType.kHallSensor, Constants.COUNTS_PER_REVOLUTION);

    rightFollowerMotor.follow(rightMotor);
    leftFollowerMotor.follow(leftMotor);
    
    rightMotor.stopMotor();
    leftMotor.stopMotor();
    
    // Invert master motors to drive in the correct direction. 
    rightMotor.setInverted(false);
    leftMotor.setInverted(true);
    
    rightEncoder.setPositionConversionFactor(Constants.POSITION_CONVERSION_FACTOR);
    leftEncoder.setPositionConversionFactor(Constants.POSITION_CONVERSION_FACTOR);
    rightEncoder.setVelocityConversionFactor(Constants.VELOCITY_CONVERSION_FACTOR);
    leftEncoder.setVelocityConversionFactor(Constants.VELOCITY_CONVERSION_FACTOR);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
