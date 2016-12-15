
import java.net.URL;
import java.util.*;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.NameValuePair;

public class Thermo_main 
{
	static double currInsidetmp1,currInsidetmp2;
	static double currOutsidetemp;//=35;
	static String ac_mode1,ac_mode2,fan1,fan2;
	static int floor1,floor2,floor;
	static double setTemp1,setTemp2;
	static int userID = 1001;
	
	static int ApplianceID;
	static String time1,time2;
	static String room;

	static List<Integer> roomLock = new ArrayList<Integer>();
	static List<String> lock_status = new ArrayList<String>();
	static List<String> light_status = new ArrayList<String>();
	static List<String> dimmer_status = new ArrayList<String>();
	static String security_status;
	static List<String> door_floor = new ArrayList<String>();
	static List<String> door_status = new ArrayList<String>();

	static List<String> motion_floor = new ArrayList<String>();
	static List<String> motion_status = new ArrayList<String>();

	static List<String> garage_door_status = new ArrayList<String>();
	static List<String> garage_door_type = new ArrayList<String>();

	static String ip_addr_server = null;//"192.168.1.111";
	static Integer SocketNo = 9501;
	static Integer DiscoverySocketNo = 9580;
	static String pi_ip_addr = null;
	

	static HelperFunctions help = new HelperFunctions();	
	static EnergyConsumption e1 = new EnergyConsumption();
	static ArrayList<String> ip_addr_pi = new ArrayList<String>();

	public static void setGlobalVaribles()
	{

		String result = "";
		String httpPostString = "";

		ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
		nameValuePairs1.add(new BasicNameValuePair("userID",String.valueOf(Thermo_main.userID)));

		try
		{
			/*****************1.Thermostat Result Get*********************/

			httpPostString = "http://"+Thermo_main.ip_addr_server+"/getTemp_java.php";

			result = help.getResult(nameValuePairs1,httpPostString);
			JSONArray arrayTemp = new JSONArray(result);

			List<Integer> floor = new ArrayList<Integer>();
			List<Float> currInsidetmp = new ArrayList<Float>();
			List<Float> setTemp = new ArrayList<Float>();
			List<String> ac_mode = new ArrayList<String>();
			List<String> fan = new ArrayList<String>();
			List<Float> currOutsidetmp = new ArrayList<Float>();

			for(int n =0; n< arrayTemp.length(); n++)
			{
				JSONObject objTemp = arrayTemp.getJSONObject(n);
				floor.add(objTemp.getInt("floor")); 
				currInsidetmp.add(Float.parseFloat(objTemp.getString("insideTemp")));
				currOutsidetmp.add(Float.parseFloat(objTemp.getString("outsideTemp")));
				setTemp.add(Float.parseFloat(objTemp.getString("setTemp")));
				ac_mode.add(objTemp.getString("ac_mode"));
				fan.add(objTemp.getString("fan"));
				currOutsidetmp.add(Float.parseFloat(objTemp.getString("outsideTemp")));
			}

			for(int i =0;i<floor.size();i++)
			{
				if(floor.get(i) == 1)
				{
					floor1 = floor.get(i);
					currInsidetmp1 = currInsidetmp.get(i);
					setTemp1 = setTemp.get(i);
					ac_mode1 = ac_mode.get(i);
					fan1 = fan.get(i);
					currOutsidetemp = currOutsidetmp.get(i);
				}
				else if(floor.get(i) == 2)
				{
					floor2 = floor.get(i);
					currInsidetmp2 = currInsidetmp.get(i);
					setTemp2 = setTemp.get(i);
					ac_mode2 = ac_mode.get(i);
					fan2 = fan.get(i);
					currOutsidetemp = currOutsidetmp.get(i);
				}
			}
			/*****************Thermostat get result end*********************/

			/*****************2.Light Get Result Start*********************/

			httpPostString = "http://"+Thermo_main.ip_addr_server+"/getLightStatus_java.php";
			result = help.getResult(nameValuePairs1,httpPostString);
			JSONArray array = new JSONArray(result);
			List<Integer> room = new ArrayList<Integer>();
			

			for(int n =0; n< array.length(); n++)
			{
				JSONObject objLight = array.getJSONObject(n);
				room.add(objLight.getInt("room")); 
				light_status.add(objLight.getString("light_status"));
				dimmer_status.add(objLight.getString("dimmer_status"));
			}
			/*****************Light get result end*********************/

			/*****************3.Door Get Result Start*********************/

			httpPostString = "http://"+Thermo_main.ip_addr_server+"/getDoorSensor_java.php";
			result = help.getResult(nameValuePairs1,httpPostString);	
			JSONArray arrayDoor = new JSONArray(result);

			for(int n =0; n< arrayDoor.length(); n++)
			{
				JSONObject objDoor = arrayDoor.getJSONObject(n);
				//String doorTypeValue = objDoor.getString("floor");
				door_floor.add(objDoor.getString("floor")); 
				door_status.add(objDoor.getString("status")); 
			}
			/*****************Door get result end*********************/

			/*****************4.Motion Sensor Get result start*********************/
			nameValuePairs1.add(new BasicNameValuePair("userID",String.valueOf(Thermo_main.userID)));
			httpPostString = "http://"+Thermo_main.ip_addr_server+"/getMotionSensors_java.php";

			result = help.getResult(nameValuePairs1,httpPostString);
			JSONArray arrayMotion = new JSONArray(result);

			for(int n =0; n< arrayMotion.length(); n++)
			{
				JSONObject objLock = arrayMotion.getJSONObject(n);
				motion_floor.add(objLock.getString("floor")); 
				motion_status.add(objLock.getString("status"));
			}

			/*****************Motion Sensor result end*********************/

			/*****************5.Lock Sensor get result start*********************/

			httpPostString = "http://"+Thermo_main.ip_addr_server+"/getLock_java.php";
			result = help.getResult(nameValuePairs1,httpPostString);
			JSONArray arrayLock = new JSONArray(result);

			for(int n =0; n< arrayLock.length(); n++)
			{
				JSONObject objLock = arrayLock.getJSONObject(n);
				roomLock.add(objLock.getInt("room")); 
				lock_status.add(objLock.getString("lock_status"));	
			}
			/*****************Lock Sensor get result end*********************/

			/*****************6.Security Sensor Get Result Start*********************/
			httpPostString = "http://"+Thermo_main.ip_addr_server+"/getSecurityStatus_java.php";
			result = help.getResult(nameValuePairs1,httpPostString);	
			JSONObject objSecurity = new JSONObject(result);
			security_status = objSecurity.getString("security_status");
			/*****************Security Sensor get result end*********************/

			/*****************7.Garage Door Sensor Get Result Start*************************/
			httpPostString = "http://"+Thermo_main.ip_addr_server+"/getGarageDoor_java.php";
			result = help.getResult(nameValuePairs1,httpPostString);	
			JSONArray arrayGarage = new JSONArray(result);

			for(int n=0;n<arrayGarage.length();n++)
			{
				JSONObject objGarage = arrayGarage.getJSONObject(n);
				garage_door_status.add(objGarage.getString("door_status"));
				garage_door_type.add(objGarage.getString("door_type"));
			}
			/*****************Garage Door Sensor Get result end*********************/
		}
		catch(Exception e){}
	}
	
	public static void main(String[] args) 
	{
		try
		{
			ip_addr_server = help.GetServerIp();
			String username="root",password="1234";
			String up= "username=" + username + "&password=" + password ;
			URL url=new URL("http://"+ip_addr_server+"/login.php?");//+up);
			java.net.URLConnection  urlc=  url.openConnection();
			urlc.connect();
			
			setGlobalVaribles();
			DiscoveryRoutine discover = new DiscoveryRoutine();
			
			//while(true)
			//{	
				discover.start();
				while(true){;}
				//discover.join();
				
			//}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}