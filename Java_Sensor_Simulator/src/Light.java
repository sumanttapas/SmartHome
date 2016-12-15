
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;


public class Light extends Thread
{
	static List<String> light_status_new = new ArrayList<String>();
	static List<String> dimmer_status_new = new ArrayList<String>();
	static List<Integer> room_new = new ArrayList<Integer>();
	static int flag = 1,equalFlag=0,statusChangedFlag=0;
	
	public static void getLightStatus()
	{
		//HelperFunctions help = new HelperFunctions();
		try
		{
			ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
			nameValuePairs1.add(new BasicNameValuePair("userID",String.valueOf(Thermo_main.userID)));
			String httpPoststring = "http://"+Thermo_main.ip_addr_server+"/getLightStatus_java.php";
			
			//String httpPostSetString = "http://"+Thermo_main.ip_addr_server+"/setEnergy.php";
			//Thermo_main.help.setResult(nameValuePairs1, httpPostSetString);
			
			String result = Thermo_main.help.getResult(nameValuePairs1,httpPoststring);
			JSONArray array = new JSONArray(result);

			for(int n =0; n< array.length(); n++)
			{
				JSONObject objLight = array.getJSONObject(n);
				room_new.add(objLight.getInt("room")); 
				light_status_new.add(objLight.getString("light_status"));
				dimmer_status_new.add(objLight.getString("dimmer_status"));
			}
		}catch(Exception e) {}
	}

	public void run() 
	{
		while(true)
		{
			try
			{
				Thread.sleep(1000);
				
				ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
				nameValuePairs1.add(new BasicNameValuePair("userID",String.valueOf(Thermo_main.userID)));
				String httpPostString = "http://"+Thermo_main.ip_addr_server+"/gettempTime_java.php";
				
				String result = Thermo_main.help.getResult(nameValuePairs1,httpPostString);
				
				JSONObject objEnergy = new JSONObject(result);
				int Energyflag = objEnergy.getInt("flag");
				
				//System.out.println("result : " + result);
				
				if(Energyflag == 1)
				{
					Thermo_main.e1.getEnergyConsumedValue(result);
					//Energyflag =0;
				}
				
				if(flag == 1)
				{
					light_status_new.clear();
					dimmer_status_new.clear();
					room_new.clear();
					getLightStatus();
					flag++;
					equalFlag++;
				}
				for(int i=0;i< Thermo_main.light_status.size();i++)
				{ 
					String temp1 = Thermo_main.light_status.get(i);
					String temp2 = light_status_new.get(i);
					String dimmer1 = Thermo_main.dimmer_status.get(i);
					String dimmer2 = dimmer_status_new.get(i);
					if( temp1.equals(temp2))
					{
						equalFlag = 1;
						//Thermo_main.dimmer_status = dimmer_status_new;
						//Thermo_main.dimmer_status.add(i, dimmer_status_new.get(i));
						light_status_new.clear();
						dimmer_status_new.clear();
						
						
						room_new.clear();
						getLightStatus();
					}
					else
					{
						statusChangedFlag = 1;
						Thermo_main.light_status.set(i, light_status_new.get(i));
						equalFlag = 0;
					}
					StringBuilder sb = new StringBuilder();
					sb.append("light:");
					if(statusChangedFlag == 1)
					{
						for(int j=0;j<light_status_new.size();j++)
						{
							//System.out.println("room = " + room_new.get(i)+" Light = "+ light_status_new.get(j) + " dimmer status  = " + dimmer_status_new.get(j));
							if(j== light_status_new.size()-1)
								sb.append("1001 " + room_new.get(j) +" " +light_status_new.get(j) + " "+dimmer_status_new.get(j));
							else
								sb.append("1001 " + room_new.get(j) +" " +light_status_new.get(j) + " "+dimmer_status_new.get(j) + ":");
						}
						System.out.println(sb.toString());
						statusChangedFlag = 0;

						Thermo_main.help.createSocketConnAndSendString(sb);
					}
				}
			}catch(Exception e){}
		}
	}
}