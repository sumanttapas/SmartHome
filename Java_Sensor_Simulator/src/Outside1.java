import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

@SuppressWarnings("deprecation")
public class Outside1 extends Thread
{
	
	double insideTemp;
	static double outsideTemp;
	
	Outside1() {}
	
	Outside1(double inTemp, double outTemp)
	{
		Thermo_main.currInsidetmp1 = inTemp;
		outsideTemp = outTemp;
	}
	
	public static void getConnection()
	{
		HelperFunctions help = new HelperFunctions();
		InputStream inputStream = null;
		String result = "";
		ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
		//String httpPostString = "http://"+Thermo_main.ip_addr_server+"/setEnergy.php";
				
		nameValuePairs1.add(new BasicNameValuePair("userID",String.valueOf(Thermo_main.userID)));
		nameValuePairs1.add(new BasicNameValuePair("insideTemp",String.valueOf(Thermo_main.currInsidetmp1)));
		nameValuePairs1.add(new BasicNameValuePair("outsideTemp",String.valueOf(outsideTemp)));
		nameValuePairs1.add(new BasicNameValuePair("setTemp",String.valueOf(Thermo_main.setTemp1)));
		nameValuePairs1.add(new BasicNameValuePair("ac_mode",Thermo_main.ac_mode1));
		nameValuePairs1.add(new BasicNameValuePair("floor",String.valueOf(Thermo_main.floor1)));
		nameValuePairs1.add(new BasicNameValuePair("fan",Thermo_main.fan1));
		nameValuePairs1.add(new BasicNameValuePair("fan",Thermo_main.fan1));
		
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://"+Thermo_main.ip_addr_server+"/setTemp.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
			((BufferedReader) httpclient).close();
			
			//Thermo_main.help.getResult(nameValuePairs1, httpPostString);
		}
		catch(Exception e){}
		
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			inputStream.close();
			result=sb.toString();
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
				Thread.sleep(5000);

				StringBuilder sb = new StringBuilder();
				sb.append("ac:");
				DecimalFormat df = new DecimalFormat("##.##");
				
				if(Thermo_main.currInsidetmp1 < outsideTemp) 
				{
					Thermo_main.currInsidetmp1 = Thermo_main.currInsidetmp1 + 0.1;
					sb.append("1001 1 " +df.format(Thermo_main.currInsidetmp1)+ " "+outsideTemp + " "+df.format(Thermo_main.setTemp1)+" "+Thermo_main.ac_mode1+" "+Thermo_main.fan1);
					System.out.println(sb.toString());
					
					Thermo_main.help.createSocketConnAndSendString(sb);
				}
				else if(Thermo_main.currInsidetmp1 > outsideTemp)
				{
					Thermo_main.currInsidetmp1 = Thermo_main.currInsidetmp1 - 0.1;
					sb.append("1001 1 " +df.format(Thermo_main.currInsidetmp1)+ " "+outsideTemp + " "+Thermo_main.setTemp1+" "+Thermo_main.ac_mode1+" "+Thermo_main.fan1);
					System.out.println(sb.toString());
					
					Thermo_main.help.createSocketConnAndSendString(sb);
				}
				
				getConnection();
			}
			catch(Exception ex)
			{
				Thread.currentThread().interrupt();
			}
		}
		
	}
	

}