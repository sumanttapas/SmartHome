
import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class ServerSock {
	String garage_door;
	String lock;
	String light;
	String motion;
	String security;
	String door;
	String ac;
	
	
	public ServerSock() {
		this.garage_door = "";
		this.lock = "lock:1001 5 locked:1001 4 unlocked:1001 6 locked";
		this.light = "light:1001 1 on 57:1001 2 off 71:1001 3 off 0";
		this.motion = "";
		this.security = "security:1001 armed away";
		this.door = "";
		this.ac = "";
	}
	
	public static void main(String args[]) throws IOException, ClassNotFoundException
	{
		ServerSocket discovery = new ServerSocket(9580);
		Socket sd = discovery.accept();
		Scanner scd = new Scanner (sd.getInputStream());
		String serverIp = scd.nextLine();
		System.out.println("Ip address of server is "+ serverIp);
		scd.close();
		sd.close();
		discovery.close();
		ServerSocket ss = new ServerSocket(9501);
		Connection con = null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/phaseTwo","root","1234");
			
		}catch(Exception e)
		{
			System.out.println("Error in connection Object ");
			 e.printStackTrace();
			System.exit(-1);
		}
		while(true)
		{
			Socket s = ss.accept();
			System.out.println("Socket Accepted");
			new Thread(new UpdateDb(s,con)).start();
		}
	}
}
