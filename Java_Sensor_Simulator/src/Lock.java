
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class Lock extends Thread
{
	static List<Integer> roomLock_new = new ArrayList<Integer>();
	static List<String> lock_status_new = new ArrayList<String>();
	static int flag = 1,equalFlag=0,statusChangedFlag=0;

	public static void getLockStatus()
	{
		HelperFunctions help = new HelperFunctions();
		try
		{
			ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
			nameValuePairs1.add(new BasicNameValuePair("userID",String.valueOf(Thermo_main.userID)));
			String httpPostString = "http://"+Thermo_main.ip_addr_server+"/getLock_java.php";
			
			String result = help.getResult(nameValuePairs1,httpPostString);
			JSONArray arrayLock = new JSONArray(result);

			for(int n =0; n< arrayLock.length(); n++)
			{
				JSONObject objLock = arrayLock.getJSONObject(n);
				roomLock_new.add(objLock.getInt("room")); 
				lock_status_new.add(objLock.getString("lock_status"));
			}
		}
		catch(Exception e)
		{ }
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
					lock_status_new.clear();
					roomLock_new.clear();
					getLockStatus();
					flag++;
					equalFlag++;
				}

				for(int i=0;i< Thermo_main.lock_status.size();i++)
				{ 
					String temp1 = Thermo_main.lock_status.get(i);
					String temp2 = lock_status_new.get(i);
					if( temp1.equals(temp2))
					{
						equalFlag = 1;
						lock_status_new.clear();
						roomLock_new.clear();
						getLockStatus();		
					}
					else
					{
						statusChangedFlag = 1;
						Thermo_main.lock_status.set(i, lock_status_new.get(i));
						equalFlag = 0;
					}
					StringBuilder sb = new StringBuilder();
					if(statusChangedFlag == 1)
					{
						//System.out.print("lock: ");
						sb.append("lock:");
						for(int j=0;j<lock_status_new.size();j++)
						{
							if(j== lock_status_new.size() - 1)
							{
								//System.out.print("1001 " + roomLock_new.get(j) +" "+ lock_status_new.get(j));
								sb.append("1001 " + roomLock_new.get(j) +" "+ lock_status_new.get(j));
							}
							else
							{
								//System.out.print("1001 " + roomLock_new.get(j) +" "+ lock_status_new.get(j) + ":");
								sb.append("1001 " + roomLock_new.get(j) +" "+ lock_status_new.get(j) + ":");
							}
						}
						System.out.println(sb.toString());
						
						Thermo_main.help.createSocketConnAndSendString(sb);
						statusChangedFlag = 0;
					}

				}
			} 
			 catch (Exception e) {}
		}
	}
}



