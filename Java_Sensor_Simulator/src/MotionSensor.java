
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class MotionSensor extends Thread
{
	static List<String> motion_floor_new = new ArrayList<String>();
	static List<String> motion_status_new = new ArrayList<String>();
	static int flag =1,equalFlag=0,statusChangedFlag=0;

	public static void getMotionSensorStatus()
	{
		try
		{
			HelperFunctions help = new HelperFunctions();
			ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
			nameValuePairs1.add(new BasicNameValuePair("userID",String.valueOf(Thermo_main.userID)));
			String httpPostString = "http://"+Thermo_main.ip_addr_server+"/getMotionSensors_java.php";

			String result = help.getResult(nameValuePairs1,httpPostString);
			JSONArray arrayLock = new JSONArray(result);

			for(int n =0; n< arrayLock.length(); n++)
			{
				JSONObject objLock = arrayLock.getJSONObject(n);
				motion_floor_new.add(objLock.getString("floor")); 
				motion_status_new.add(objLock.getString("status"));
			}

		}catch(Exception e){}

	}
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.sleep(1000);
				if(flag == 1)
				{
					motion_floor_new.clear();
					motion_status_new.clear();
					getMotionSensorStatus();
					flag++;
				}

				for(int i=0;i< Thermo_main.motion_status.size();i++)
				{
					String temp1 = Thermo_main.motion_status.get(i);
					String temp2 = motion_status_new.get(i);

					if( temp1.equals(temp2))
					{
						equalFlag = 1;
						motion_floor_new.clear();
						motion_status_new.clear();
						getMotionSensorStatus();
					}
					else
					{
						statusChangedFlag = 1;
						Thermo_main.motion_status.set(i, motion_status_new.get(i));
						equalFlag = 0;
					}
					StringBuilder sb = new StringBuilder();
					sb.append("motion:");
					if(statusChangedFlag == 1)
					{
						for(int j=0;j<motion_status_new.size();j++)
						{
							//System.out.println("status = " + motion_status_new.get(i)+" floor = "+ motion_floor_new.get(j));
							if(j==motion_status_new.size()-1)
								sb.append("1001 " + motion_floor_new.get(j) +" "+ motion_status_new.get(j));
							else
								sb.append("1001 " + motion_floor_new.get(j) +" "+ motion_status_new.get(j) +":");
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
