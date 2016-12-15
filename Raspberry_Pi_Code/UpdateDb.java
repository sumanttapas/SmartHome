package home_simulator;

import java.net.*;
import java.io.*;
import java.util.*;
import java.sql.*;

public class UpdateDb implements Runnable{
	Socket s;
	Connection con;
	String sss;
	public UpdateDb(String sss)
	{
		this.sss = sss;
	}
	public UpdateDb(Socket s, Connection con) {
		this.s = s;
		this.con = con;
	}

	@Override
	public void run() {
		try {
			Scanner sc = new Scanner(s.getInputStream());
			int k = 0;
			while(k != 1)
			{
			        String data = sc.nextLine();
				String[] da = data.split(":");
				Statement stmt = con.createStatement();
				switch(da[0])
				{
					case "garage":
						System.out.println("Updating garage Doors");
						for(int i =1; i < da.length; i++)
						{
							String[] da1 = da[i].split(" ");
							String query = "update garage-doors set door_status='"+da1[3]+
								"' where userID="+da1[0]+" AND door_type = '"+da1[1]+" "+da1[2]+"'";
							System.out.println(query);
						        stmt.execute(query);
						}
						break;
					case "lock":
						System.out.println("Updating lock_status");
						for(int i =1; i < da.length; i++)
						{
							String[] da1 = da[i].split(" ");
							String query = "update lock_status set lock_status ='"+da1[2]+
									"' where userID="+da1[0]+" AND room = "+da1[1];
							System.out.println(query);
						        stmt.execute(query);
						}
						break;
					case "light":
						System.out.println("Updating light status");
						for (int i = 1; i < da.length; i++) 
						{
							String[] da1 = da[i].split(" ");
							String query = "update light_status set light_status = '"+da1[2]+"', "+
							"dimmer_status = "+da1[3]+" where userID = "+da1[0]+" AND room = "+da1[1];
							System.out.println(query);
							stmt.execute(query);							
						}
						break;
					case "motion":
						System.out.println("Updating motion sensors");
						for (int i = 1; i < da.length; i++) 
						{
							String[] da1 = da[i].split(" ");
							String query = "update motion_sensors set status = '"+da1[2]+"' "+
							" where userID = "+da1[0]+" AND floor = '"+da1[1];
							System.out.println(query);
							stmt.execute(query);
							
						}
						break;
					case "security":
						System.out.println("Updating security system");
						for (int i = 1; i < da.length; i++) 
						{
							String[] da1 = da[i].split(" ");
							String status = "";
							if(da1.length == 3)
								status = da1[1]+" "+da1[2];
							else
								status = da1[1];
							String query = "update security_system set security_status = '"+
									status+"' where userID = "+da1[0];
							System.out.println(query);
							stmt.execute(query);							
						}
						break;
					case "door":
						System.out.println("updating door status");
						for (int i = 1; i < da.length; i++) 
						{
							String[] da1 = da[i].split(" ");
							String query = "update door_sensors set status = '"+da1[2]+"' "+
									" where userID = "+da1[0]+" AND floor = '"+da1[1];
							System.out.println(query);
							stmt.execute(query);							
						}
						break;
					case "ac":
						System.out.println("updating thermostat");
						for (int i = 1; i < da.length; i++) 
						{
							String[] da1  = da[i].split(" ");
							String query = "update AC_Data set insideTemp = "+da1[2]+", outsideTemp = "+
							da1[3]+", setTemp = "+da1[4]+", ac_mode = '"+da1[5]+"', fan = '"+da1[6]+"'"+
									" where userID = "+da1[0]+" AND floor = "+da1[1];
							System.out.println(query);
							stmt.execute(query);
						}
						break;	
				}
				k=1;
			}
			
		} catch (Exception e) {
			System.out.println("Exiting");
			try {
				return;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return;
		}
		finally{
			try {
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
