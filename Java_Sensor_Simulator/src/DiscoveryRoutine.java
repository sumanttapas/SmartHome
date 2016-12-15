import java.net.URL;


public class DiscoveryRoutine extends Thread
{
	String known_ip_addr = "255.255.255.255";
	Integer runOtherThreadsFlag = 0;
	
	Outside1 outside1 = new Outside1(Thermo_main.currInsidetmp1,Thermo_main.currOutsidetemp);
	Outside2 outside2 = new Outside2(Thermo_main.currInsidetmp2,Thermo_main.currOutsidetemp);
	Inside1 inside1 = new Inside1(Thermo_main.setTemp1,Thermo_main.currInsidetmp1,Thermo_main.ac_mode1);   
	Inside2 inside2 = new Inside2(Thermo_main.setTemp2,Thermo_main.currInsidetmp2,Thermo_main.ac_mode2); 
	Lock lock = new Lock();
	Light light = new Light();
	SecuritySystem securitySystem = new SecuritySystem();
	Garage_door garage = new Garage_door();
	DoorSensor door = new DoorSensor();
	MotionSensor motion = new MotionSensor();
	
	int onlyOnceFlag=0;

	public void run()
	{
		
		try{

			Thread.sleep(1000);
			while(true)
			{
				//if(onlyOnceFlag == 0)
				//{
					Thermo_main.ip_addr_pi = Thermo_main.help.getIpAddress();
					
					System.out.println("Discovery Phase Successful");
					onlyOnceFlag = 1;
			//	}

				for(int j=0;j<Thermo_main.ip_addr_pi.size();j++)
				{
					Thermo_main.pi_ip_addr = Thermo_main.ip_addr_pi.get(j);
					String IpOfServer = Thermo_main.ip_addr_server;

					Thermo_main.help.createSocketConnAndSendString(IpOfServer,Thermo_main.pi_ip_addr,Thermo_main.DiscoverySocketNo);
					known_ip_addr = Thermo_main.pi_ip_addr;
					runOtherThreadsFlag =0;
				}

				if((known_ip_addr.equals(Thermo_main.pi_ip_addr)))//when both are same start all other threads
				{
					runOtherThreadsFlag =1;
				}
				/*else	// when known_ip is different from discovered ip create new socket
				{
					String IpOfServer = Thermo_main.ip_addr_server;

					Thermo_main.help.createSocketConnAndSendString(IpOfServer,Thermo_main.DiscoverySocketNo);
					known_ip_addr = Thermo_main.pi_ip_addr;
					runOtherThreadsFlag =0;
				}*/

				if(runOtherThreadsFlag == 1)
				{

					inside1.start();
					//outside1.start();
					inside2.start();
					//outside2.start();
					lock.start();
					light.start();
					securitySystem.start();
					door.start();
					motion.start();
					garage.start();
					//outside1.join();
					inside1.join();
				}
			}
		}catch(Exception e){}
	}
}


