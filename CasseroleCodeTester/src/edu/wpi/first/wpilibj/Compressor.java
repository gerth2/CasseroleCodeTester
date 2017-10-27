package edu.wpi.first.wpilibj;

import java.util.Timer;
import java.util.TimerTask;

import org.usfirst.frc.team1736.robotTester.GlobalState;

public class Compressor {
	
	compressorPlant plant;
	
	public Compressor(){
		//Kick off plant model for compressor
		plant = new compressorPlant();
		
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(plant, 0, 100); //100ms plant update rate
	}
	
	//Mock implementations of WPILIB functions
	public double getCompressorCurrent(){
		return plant.getCurrent();
	}
	public boolean getPressureSwitchValue(){
		return plant.pressureSwActive;
	}
	
	
	//Simple plant model for compressor
	//Has a running/not-running command
	//When running, has some current draw and fills to a max psi
	//Once at the max PSI turns off until pressure goes below MIN_ACTIVE_PSI
	public class compressorPlant extends TimerTask {
		
		final double FILL_RATE_PSI_PER_LOOP = 0.5;
		final double MAX_PSI = 125;
		final double MIN_ACTIVE_PSI = 100;
		final double ACTIVE_CURRENT_A = 12;
		
		boolean running;
		boolean running_cmd;
		boolean pressureSwActive;
		
		double pressure_psi;
		double pressure_psi_prev;
		
		public compressorPlant(){
			running = true;
			running_cmd = true;
			pressureSwActive = false;
			pressure_psi = 0;
			pressure_psi_prev = 0;
		}
		
		public void turnOn(){
			running_cmd = true;
		}
		
		public void turnOff(){
			running_cmd = false;
		}
		
		public boolean getRunning(){
			return running;
		}
		
		//Returns the present current
		public double getCurrent(){
			if(running){
				return ACTIVE_CURRENT_A;
			} else {
				return 0.0;
			}
		}
		
		//Returns present pressure in PSI
		public double getPressurePSI(){
			return pressure_psi;
		}
		
		//Called by other plants when a cylinder pulls air from the system
		public void reducePressure(double psi){
			pressure_psi_prev = pressure_psi;
			pressure_psi -= psi;
			
			//Limit to prevent vacuum (not modeled)
			if(pressure_psi < 0){
				pressure_psi = 0;
			}
		}
		
		//Periodic update function
	    @Override
	    public void run() {
	    	
	    	//Pressure switch with hysteresis
	    	if(pressure_psi <= MIN_ACTIVE_PSI && pressure_psi_prev > MIN_ACTIVE_PSI ){
	    		pressureSwActive = false;
	    	} else if(pressure_psi >= MAX_PSI && pressure_psi_prev < MAX_PSI) {
	    		pressureSwActive = true;
	    	}
	    	
	    	//When we are commanded to run, and the pressure switch isn't on, we should run.
	    	if(running_cmd == true && pressureSwActive==false){
	    		running = true;
	    	} else {
	    		running = false;
	    	}

	    	//Save off previous state
	    	pressure_psi_prev = pressure_psi;
	    	
	    	//When running, the pressure will go up
	    	if(running == true){
	    		pressure_psi += FILL_RATE_PSI_PER_LOOP;
	    	}

	    	//Update global state for other plant models
	    	GlobalState.SystemPressurePSI = pressure_psi;
	    	
	    
	    	
	    }
	}

}
