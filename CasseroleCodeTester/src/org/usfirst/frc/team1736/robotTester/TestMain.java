package org.usfirst.frc.team1736.robotTester;

import java.util.Timer;
import java.util.TimerTask;

import org.usfirst.frc.team1736.robot.Robot;

public class TestMain {
	
	static Robot robotUnderTest;
	static RunState cur_state;
	static RunState prev_state;
	
	public static void main(String [ ] args)
	{
		//Declare the under-test robot
		robotUnderTest = new Robot();
		
		//Init internal states
		prev_state = RunState.DISABLED;
		cur_state = RunState.TELEOP;
		
		//Init the under-test robot
		robotUnderTest.robotInit();
		
		//Kick off the main execution task
		TimerTask timerTask = new MainExecutionTask();
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, 20);
		
		//Let it run forever
		while(true);

	}
	
	
	public static class MainExecutionTask extends TimerTask {
	    @Override
	    public void run() {
	    	//Main test execution loop
	    	
	    	//Handle transitions between states with init
	    	if(cur_state != prev_state){
	    		if(cur_state == RunState.DISABLED)
	    			robotUnderTest.disabledInit();
	    		else if (cur_state == RunState.TELEOP)
	    			robotUnderTest.teleopInit();
	    		else if (cur_state == RunState.AUTO)
	    			robotUnderTest.autonomousInit();
	    	}
	    	
	    	//Run periodic function
    		if(cur_state == RunState.DISABLED)
    			robotUnderTest.disabledPeriodic();
    		else if (cur_state == RunState.TELEOP)
    			robotUnderTest.teleopPeriodic();
    		else if (cur_state == RunState.AUTO)
    			robotUnderTest.autonomousPeriodic();
	    	
	    	
	    	//Update state variable
	    	prev_state = cur_state;
	    }        
	}
	
	
	//Describes the state of the program running
	enum RunState { DISABLED, TELEOP, AUTO };

}
