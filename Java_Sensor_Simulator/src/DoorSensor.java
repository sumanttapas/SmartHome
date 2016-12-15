
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class DoorSensor extends Thread
{
	static List<String> door_floor_new = new ArrayList<String>();
	static List<String> door_status_new = new ArrayList<String>();
	static int flag = 1,equalFlag=0,statusChangedFlag=0;

	public static void getDoorSensorStatus()
	{
		try
		{
			HelperFunctions help = new HelperFunctions();

			ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
			nameValuePairs1.add(new BasicNameValuePair("userID",String.valueOf(Thermo_main.userID)));
			String httpPostString = "http://"+Thermo_main.ip_addr_server+"/getDoorSensor_java.php";

			String result = help.getResult(nameValuePairs1,httpPostString);
			JSONArray arrayDoor = new JSONArray(result);

			for(int n =0; n< arrayDoor.length(); n++)
			{
				JSONObject objDoor = arrayDoor.getJSONObject(n);
				door_floor_new.add(objDoor.getString("floor")); 
				door_status_new.add(objDoor.getString("status")); 
			}			
		}catch(Exception e){}
	}
	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.sleep(1000);
				if(flag == 1)
				{
					door_floor_new.clear();
					door_status_new.clear();
					getDoorSensorStatus();
					flag++;
					equalFlag++;
				}
				for(int i=0;i< Thermo_main.door_status.size();i++)
				{ 
					String temp1 = Thermo_main.door_status.get(i);
					String temp2 = door_status_new.get(i);
					if( temp1.equals(temp2))
					{
						equalFlag = 1;
						door_floor_new.clear();
						door_status_new.clear();
						getDoorSensorStatus();
					}
					else
					{
						statusChangedFlag = 1;
						Thermo_main.door_status.set(i, door_status_new.get(i));
						equalFlag = 0;
					}
					StringBuilder sb = new StringBuilder();
					sb.append("door:");
					if(statusChangedFlag == 1)
					{
						for(int j=0;j<door_floor_new.size();j++)
						{
							if(j == door_floor_new.size()-1)
								sb.append("1001 " + door_floor_new.get(j) +" "+ door_status_new.get(j));
							else
								sb.append("1001 " + door_floor_new.get(j) +" "+ door_status_new.get(j)+":");
							//System.out.println("status = " + door_status_new.get(i)+" floor = "+ door_floor_new.get(j));	
						}
						System.out.println(sb.toString());
						Thermo_main.help.createSocketConnAndSendString(sb);
						
						statusChangedFlag = 0;
					}
				}	
			}catch(Exception e){}
		}
	}
}
