package edu.wpi.first.wpilibj;

import org.usfirst.frc.team1736.robotTester.GlobalState;

public class AnalogInput {
	int user_idx;
	
	//Init's a new analog input
	public AnalogInput(int index){
		user_idx = index;
	}
	
	public double getVoltage(){
		//Switch what is returned based on which sensor this is
		if(user_idx == 0){
			//0 is presumed to be a very special pressure sensor, 1 volt per PSI
			return GlobalState.SystemPressurePSI;
		} else {
			return 0.0;
		}
	}
}
