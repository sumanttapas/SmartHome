
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class HelperFunctions 
{
	public String getResult(ArrayList<NameValuePair> nameValuePairs1, String httpPostString)
	{
		String result = "";
		try
		{
			InputStream inputStreamLock = null;
			DefaultHttpClient httpclient = new DefaultHttpClient();

			HttpPost httppostLock = new HttpPost(httpPostString);
			httppostLock.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
			HttpResponse responseLock = httpclient.execute(httppostLock);
			HttpEntity entityLock = responseLock.getEntity();
			inputStreamLock = entityLock.getContent();

			BufferedReader readerLock = new BufferedReader(new InputStreamReader(inputStreamLock,"iso-8859-1"),8);
			StringBuilder sbLock = new StringBuilder();
			String lineLock = null;

			while ((lineLock = readerLock.readLine()) != null) {
				sbLock.append(lineLock + "\n");
			}
			inputStreamLock.close();

			result=sbLock.toString();
			httpclient.close();
		}catch(Exception e){}
		return result;
	}
	
	public void setResult(ArrayList<NameValuePair> nameValuePairs1, String httpPostString)
	{
		try
		{
			//InputStream inputStreamLock = null;
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppostLock = new HttpPost(httpPostString);
			httppostLock.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
			HttpResponse responseLock = httpclient.execute(httppostLock);
			HttpEntity entityLock = responseLock.getEntity();
			httpclient.close();
		}catch(Exception e){}
	}

	public void UpdateDbWithNewTempValue(ArrayList<NameValuePair> nameValuePairs1)
	{
		try
		{
			//InputStream inputStream = null;
			//String result = "";
			HttpClient httpclient = new DefaultHttpClient();			
			HttpPost httppost = new HttpPost("http://"+Thermo_main.ip_addr_server+"/setTemp.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			//inputStream = entity.getContent();
			((BufferedReader) httpclient).close();
		}
		catch(Exception e){}
	}

	public ArrayList<String> getIpAddress()
	{
		ArrayList<String> RaspberryIp = new ArrayList<String>();

		try
		{
			Runtime rt = Runtime.getRuntime();
			String[] commands = {"/home/harshad/eclipse_workspace/Sensor_Simulator/src/GetIpAddrRaspberry.sh"};
			Process proc = rt.exec(commands);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

			// read the output from the command
			String s = null;
			while ((s = stdInput.readLine()) != null) 
			{
				RaspberryIp.add(s);
				//System.out.println(s);
			}

			//In case there are any errors
			while ((s = stdError.readLine()) != null) 
			{
				System.out.println(s);
			}
		}catch(Exception e){}

		return RaspberryIp;
	}

	public String GetServerIp()
	{
		String ip = null;
		ArrayList<String> buff = new ArrayList<String>();
		try
		{
			Runtime run = Runtime.getRuntime();
			String[] cmd = {"/home/harshad/eclipse_workspace/Sensor_Simulator/src/GetServerIpAddr.sh"};
			Process p1 = run.exec(cmd);

			BufferedReader stdin = new BufferedReader( new InputStreamReader(p1.getInputStream()));
			while((ip = stdin.readLine())!= null)
			{
				buff.add(ip);
				//System.out.println(ip);
			}
			ip = buff.get(0);
		}catch(Exception e){}
		return ip;
	}

	public void createSocketConnAndSendString(StringBuilder sb) 
	{
		Socket s = null;
		try
		{
			for(int i=0;i<Thermo_main.ip_addr_pi.size();i++)
			{
				s = new Socket(Thermo_main.ip_addr_pi.get(i), Thermo_main.SocketNo);
				PrintWriter p = new PrintWriter(s.getOutputStream());

				p.println(sb.toString());
				p.close();
				s.close();
			}
		}catch(Exception e){}
		//return s;
	}

	public void createSocketConnAndSendString(String IpOfServer,String IpOfPi, Integer socketNo) 
	{
		Socket s = null;
		try
		{
			s = new Socket(IpOfPi, socketNo);
			PrintWriter p = new PrintWriter(s.getOutputStream());
			p.println(IpOfServer);
			p.flush();
			p.close();
			s.close();
		}catch(Exception e){}
		//return s;
	}
	
	public void getTempData()
	{
		String result = "";
		String httpPostString = "";

		ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
		nameValuePairs1.add(new BasicNameValuePair("userID",String.valueOf(Thermo_main.userID)));

		try
		{
			/*****************1.Thermostat Result Get*********************/

			httpPostString = "http://"+Thermo_main.ip_addr_server+"/getTemp_java.php";

			result = getResult(nameValuePairs1,httpPostString);
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
					Thermo_main.floor1 = floor.get(i);
					Thermo_main.currInsidetmp1 = currInsidetmp.get(i);
					Thermo_main.setTemp1 = setTemp.get(i);
					Thermo_main.ac_mode1 = ac_mode.get(i);
					Thermo_main.fan1 = fan.get(i);
					Thermo_main.currOutsidetemp = currOutsidetmp.get(i);
				}
				else if(floor.get(i) == 2)
				{
					Thermo_main.floor2 = floor.get(i);
					Thermo_main.currInsidetmp2 = currInsidetmp.get(i);
					Thermo_main.setTemp2 = setTemp.get(i);
					Thermo_main.ac_mode2 = ac_mode.get(i);
					Thermo_main.fan2 = fan.get(i);
					Thermo_main.currOutsidetemp = currOutsidetmp.get(i);
				}
			}
		}
		catch(Exception e){}
			/*****************Thermostat get result end*********************/
	}
	
	public void updateTempInDb(int applicanceID, int floor)
	{
		HelperFunctions help = new HelperFunctions();
		InputStream inputStream = null;
		String result = "";
		ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
		//String httpPostString = "http://"+Thermo_main.ip_addr_server+"/setEnergy_.php";
		String httpPostStringTemp = "http://"+Thermo_main.ip_addr_server+"/setTemp.php";
		Thermo_main.ApplianceID = applicanceID;
		
		nameValuePairs1.add(new BasicNameValuePair("userID",String.valueOf(Thermo_main.userID)));
		
		if(floor == 1)
		{
			nameValuePairs1.add(new BasicNameValuePair("insideTemp",String.valueOf(Thermo_main.currInsidetmp1)));
			nameValuePairs1.add(new BasicNameValuePair("outsideTemp",String.valueOf(Thermo_main.currOutsidetemp)));
			nameValuePairs1.add(new BasicNameValuePair("setTemp",String.valueOf(Thermo_main.setTemp1)));
			nameValuePairs1.add(new BasicNameValuePair("ac_mode",Thermo_main.ac_mode1));
			nameValuePairs1.add(new BasicNameValuePair("floor",String.valueOf(Thermo_main.floor1)));
			nameValuePairs1.add(new BasicNameValuePair("fan",Thermo_main.fan1));
			nameValuePairs1.add(new BasicNameValuePair("ApplianceID",String.valueOf(applicanceID)));
		}
		else if(floor == 2)
		{
			nameValuePairs1.add(new BasicNameValuePair("insideTemp",String.valueOf(Thermo_main.currInsidetmp2)));
			nameValuePairs1.add(new BasicNameValuePair("outsideTemp",String.valueOf(Thermo_main.currOutsidetemp)));
			nameValuePairs1.add(new BasicNameValuePair("setTemp",String.valueOf(Thermo_main.setTemp2)));
			nameValuePairs1.add(new BasicNameValuePair("ac_mode",Thermo_main.ac_mode2));
			nameValuePairs1.add(new BasicNameValuePair("floor",String.valueOf(Thermo_main.floor2)));
			nameValuePairs1.add(new BasicNameValuePair("fan",Thermo_main.fan2));
			nameValuePairs1.add(new BasicNameValuePair("ApplianceID",String.valueOf(applicanceID)));
		}
		
		try{
			
			Thermo_main.help.setResult(nameValuePairs1, httpPostStringTemp);
			//Thermo_main.help.setResult(nameValuePairs1, httpPostString);
		}
		catch(Exception e){}
	}
}
