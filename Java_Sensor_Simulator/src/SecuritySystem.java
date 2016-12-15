
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class SecuritySystem extends Thread
{
	static String security_status_new;
	static int flag = 1,equalFlag=0,statusChangedFlag=0;

	public static void getSecurityStatus()
	{
		try
		{
			HelperFunctions help = new HelperFunctions();
			ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
			nameValuePairs1.add(new BasicNameValuePair("userID",String.valueOf(Thermo_main.userID)));

			String httpPostString = "http://"+Thermo_main.ip_addr_server+"/getSecurityStatus_java.php";
			String result = help.getResult(nameValuePairs1,httpPostString);

			JSONObject objSecurity = new JSONObject(result);
			security_status_new = objSecurity.getString("security_status");
		}
		catch(Exception e){}	
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
					security_status_new="";
					getSecurityStatus();
					flag++;
					equalFlag++;
				} 
				String temp1 = Thermo_main.security_status;
				String temp2 = security_status_new;
				if( temp1.equals(temp2))
				{
					equalFlag = 1;
					security_status_new="";
					getSecurityStatus();
				}
				else
				{
					statusChangedFlag = 1;
					Thermo_main.security_status = security_status_new;
					equalFlag = 0;
				}
				StringBuilder sb = new StringBuilder();
				if(statusChangedFlag == 1)
				{
					//System.out.println("changed Security Status = "+ security_status_new);
					sb.append("security:1001 " + security_status_new);
					statusChangedFlag = 0;
					System.out.println(sb.toString());

					Thermo_main.help.createSocketConnAndSendString(sb);
				}
			}catch(Exception e){}
		}

	}

}
