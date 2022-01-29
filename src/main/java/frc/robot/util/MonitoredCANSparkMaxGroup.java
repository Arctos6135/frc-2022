package frc.robot.util;

import java.util.function.BiConsumer;

import com.revrobotics.CANSparkMax;

import frc.robot.RobotContainer;

public class MonitoredCANSparkMaxGroup {
   
	private final double SHUTOFF_TEMPERATURE;
	private final double WARNING_TEMPERATURE;
   
	private CANSparkMax[] motors;
	private final short[] motorFaults;
   
	private String name;
   
	private boolean overheatShutoff = false;
	private boolean overheatWarning = false;
	private BiConsumer<CANSparkMax, Double> overheatShutoffCallback;
	private BiConsumer<CANSparkMax, Double> overheatWarningCallback;
	private Runnable normalTempCallback;
   
	public MonitoredCANSparkMaxGroup(String name, double warningTemp,
			double shutoffTemp, CANSparkMax... motors) {
		this.name = name;
		this.WARNING_TEMPERATURE = warningTemp;
		this.SHUTOFF_TEMPERATURE = shutoffTemp;
		this.motors = motors;
		this.motorFaults = new short[motors.length];
	}

	public boolean getOverheatShutoff() {
		return overheatShutoff;
	}

	public boolean getOverheatWarning() {
		return overheatWarning;
	}
   
	// BiConsumer has args overheating motor (CANSparkMax), temperature (double)
	// callback gives overheating motor and its temperature.

	public void setOverheatShutoffCallback(BiConsumer<CANSparkMax, Double> callback) {
		this.overheatShutoffCallback = callback;
	}

	public void setOverheatWarningCallback(BiConsumer<CANSparkMax, Double> callback) {
		this.overheatWarningCallback = callback;
	}

	// Set the callback for when the motor temperatures are normal again.
	// callback a callback, Runnable, can be run within thread
	public void setNormalTempCallback(Runnable callback) {
		this.normalTempCallback = callback;
	}

	public void monitorOnce() {
		boolean shutoff = false;
		boolean warning = false;
	   
		// Check each motor.
		for (var i = 0; i < motors.length; i++) {
			CANSparkMax motor = motors[i];

			// Check each motor for faults.
			short faults = motor.getFaults();

			if (faults != motorFaults[i] && faults != 0) {
				RobotContainer.getLogger().logError(name + " motor " + motor.getDeviceId() + " had faults " + faults);
			}

			motorFaults[i] = faults;

			// Check all motors for temperature.
			double temp = motor.getMotorTemperature();

			if (temp >= SHUTOFF_TEMPERATURE) {
				// call if the motor was not overheating
				if (!overheatShutoff && overheatShutoffCallback != null) {
					overheatShutoffCallback.accept(motor, temp);
				}
				shutoff = true;
			} else if (temp >= WARNING_TEMPERATURE) {
				// call if the motor was not overheating
				if (!overheatWarning && overheatWarningCallback != null) {
					overheatWarningCallback.accept(motor, temp);
				}
				warning = true;
			}
		}
	   
		// run on return to normal temp.
		if ((overheatShutoff || overheatWarning) && (!shutoff && !warning) && normalTempCallback != null) {
			normalTempCallback.run();
		}
	   
		overheatShutoff = shutoff;
		overheatWarning = warning;
	}
}
