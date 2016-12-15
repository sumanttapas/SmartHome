
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;




public class Garage_door extends Thread
{
	static List<String> garage_door_type_new = new ArrayList<String>();
	static List<String> garage_door_status_new = new ArrayList<String>();
	static int flag = 1,equalFlag=0,statusChangedFlag=0;

	public static void getGarageDoorStatus()
	{
		try
		{
			String result="";
			HelperFunctions help = new HelperFunctions();

			ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
			nameValuePairs1.add(new BasicNameValuePair("userID",String.valueOf(Thermo_main.userID)));
			String httpPostString = "http://"+Thermo_main.ip_addr_server+"/getGarageDoor_java.php";

			result = help.getResult(nameValuePairs1,httpPostString);
			JSONArray arrayDoor = new JSONArray(result);

			for(int n =0; n< arrayDoor.length(); n++)
			{
				JSONObject objDoor = arrayDoor.getJSONObject(n);
				garage_door_type_new.add(objDoor.getString("door_type")); 
				garage_door_status_new.add(objDoor.getString("door_status")); 
			}
			/*****************Door get result end*********************/

		}catch(Exception e)
		{}
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
					garage_door_type_new.clear();
					garage_door_status_new.clear();
					getGarageDoorStatus();
					flag++;
					equalFlag++;
				}
				for(int i=0;i< Thermo_main.garage_door_status.size();i++)
				{ 
					String temp1 = Thermo_main.garage_door_status.get(i);
					String temp2 = garage_door_status_new.get(i);
					if( temp1.equals(temp2))
					{
						equalFlag = 1;
						garage_door_type_new.clear();
						garage_door_status_new.clear();
						getGarageDoorStatus();
					}
					else
					{
						statusChangedFlag = 1;
						Thermo_main.garage_door_status.set(i, garage_door_status_new.get(i));
						equalFlag = 0;
					}
					StringBuilder sb = new StringBuilder();
					sb.append("garage:");
					if(statusChangedFlag == 1)
					{
						for(int j=0;j<garage_door_status_new.size();j++)
						{
							//System.out.println("garage door_status = " + garage_door_status_new.get(j)+" garage door Type = "+ garage_door_type_new.get(j));
							if(j == garage_door_status_new.size()-1)
								sb.append("1001 "+garage_door_type_new.get(j) +" "+garage_door_status_new.get(j));
							else
								sb.append("1001 "+garage_door_type_new.get(j) +" "+garage_door_status_new.get(j)+":");
								
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

