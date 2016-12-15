import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class EnergyConsumption 
{
	static String timePeriod  = "";
	static int ApplianceID;
	static String time1 = "2016-10-25 21:42:03",time2 = "2016-10-25 21:42:40";
	//	static String time1 = "2016-10-26 01:08:52",time2 = "2016-10-26 01:57:09";
	static int userID = 1001;
	static int floor = 1;
	static int room = 0;
	static double Ackw = 2;
	static double bkw = 0.8;
	

	static String ipAddr = "192.168.1.111";

	static EnergyConsumption e = new EnergyConsumption();
	
	public void getEnergyConsumedValue(String result)
	{
		try
		{
			JSONObject objEnergy = new JSONObject(result);
			
			Thermo_main.time1 = objEnergy.getString("time1");
			Thermo_main.time2 = objEnergy.getString("time2");
			ApplianceID = objEnergy.getInt("applianceID");
			
			/*nameValuePairs1.add(new BasicNameValuePair("ApplianceID",String.valueOf(ApplianceID)));
			nameValuePairs1.add(new BasicNameValuePair("time1",time1));
			nameValuePairs1.add(new BasicNameValuePair("time2",time2));
			nameValuePairs1.add(new BasicNameValuePair("floor",String.valueOf(floor)));
			nameValuePairs1.add(new BasicNameValuePair("room",String.valueOf(room)));*/
			double finalResult=0;

			if(ApplianceID == 1)
			{
				//finalResult = getEnergyCalcAc("2016-10-25 21:42:03","2016-10-25 21:42:40",result);
				//finalResult =0;
				finalResult = getEnergyCalcAc(Thermo_main.time1,Thermo_main.time2);
				DecimalFormat df = new DecimalFormat("##.#####");
				
				ArrayList<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>();
				nameValuePairs2.add(new BasicNameValuePair("userID",String.valueOf(userID)));
				nameValuePairs2.add(new BasicNameValuePair("ApplianceID",String.valueOf(ApplianceID)));
				nameValuePairs2.add(new BasicNameValuePair("energy",String.valueOf(df.format(finalResult))));
				
				String httpPostString1 = "http://"+Thermo_main.ip_addr_server+"/setEnergy.php";
				
				Thermo_main.help.setResult(nameValuePairs2, httpPostString1);
				
				ArrayList<NameValuePair> nameValuePairs3 = new ArrayList<NameValuePair>();
				nameValuePairs3.add(new BasicNameValuePair("userID",String.valueOf(Thermo_main.userID)));
				nameValuePairs3.add(new BasicNameValuePair("time1",String.valueOf(time1)));
				nameValuePairs3.add(new BasicNameValuePair("time2",String.valueOf(time2)));
				nameValuePairs3.add(new BasicNameValuePair("ApplianceID",String.valueOf(ApplianceID)));
				nameValuePairs3.add(new BasicNameValuePair("flag","0"));
				
				String httpPostString2 = "http://"+Thermo_main.ip_addr_server+"/setEnergyFlag.php";
				Thermo_main.help.setResult(nameValuePairs2, httpPostString2);
			}
			else if(ApplianceID == 3)
			{
				//finalResult = getEnergyCalcLight("2016-10-26 01:08:52","2016-10-26 01:57:09",result);
				//finalResult =0;tempTime
				finalResult = getEnergyCalcLight(Thermo_main.time1,Thermo_main.time2);
				DecimalFormat df = new DecimalFormat("##.#####");
				
				String energyValue = df.format(finalResult);
				
				ArrayList<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>();
				nameValuePairs2.add(new BasicNameValuePair("userID",String.valueOf(userID)));
				nameValuePairs2.add(new BasicNameValuePair("ApplianceID",String.valueOf(ApplianceID)));
				nameValuePairs2.add(new BasicNameValuePair("energy",energyValue));
				
				String httpPostString1 = "http://"+Thermo_main.ip_addr_server+"/setEnergy.php";
				
				Thermo_main.help.setResult(nameValuePairs2, httpPostString1);
				
				ArrayList<NameValuePair> nameValuePairs3 = new ArrayList<NameValuePair>();
				nameValuePairs3.add(new BasicNameValuePair("userID",String.valueOf(Thermo_main.userID)));
				nameValuePairs3.add(new BasicNameValuePair("time1",String.valueOf(time1)));
				nameValuePairs3.add(new BasicNameValuePair("time2",String.valueOf(time2)));
				nameValuePairs3.add(new BasicNameValuePair("ApplianceID",String.valueOf(ApplianceID)));
				nameValuePairs3.add(new BasicNameValuePair("flag","0"));
				
				String httpPostString2 = "http://"+Thermo_main.ip_addr_server+"/setEnergyFlag.php";
				Thermo_main.help.setResult(nameValuePairs2, httpPostString2);
			}

			
			
			String halt = "ruk ja oo dil Diwane";
		}
		catch(Exception e){}
	}
	
	public double getEnergyCalcAc(String  time1, String time2)
	{
		double energyConsumption=0;
		try
		{

			ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
			nameValuePairs1.add(new BasicNameValuePair("userID",String.valueOf(userID)));
			nameValuePairs1.add(new BasicNameValuePair("ApplianceID",String.valueOf(ApplianceID)));
			nameValuePairs1.add(new BasicNameValuePair("time1",String.valueOf(time1)));
			nameValuePairs1.add(new BasicNameValuePair("time2",String.valueOf(time2)));
			
			String httpPostString = "http://"+Thermo_main.ip_addr_server+"/getEnergyForAc_java.php";
			
			String result = Thermo_main.help.getResult(nameValuePairs1, httpPostString);
			
			//System.out.println(result);
			
			JSONArray array = new JSONArray(result);

			List<String> time = new ArrayList<String>();
			List<String> Appliance_status = new ArrayList<String>();
			List<String> timeStamp = new ArrayList<String>();

			int insertFlag = 0;
			for(int n =0; n< array.length(); n++)
			{
				JSONObject objEner = array.getJSONObject(n);

				time.add(objEner.getString("curr_time")); 
				Appliance_status.add(objEner.getString("Appliance_status"));
			}
			for(int i=0;i<Appliance_status.size();i++)
			{
				if((!Appliance_status.get(i).equals("off")) && insertFlag==0)
				{
					timeStamp.add(time.get(i));
					insertFlag=1;
				}
				else if(Appliance_status.get(i).equals("off") && insertFlag ==1)
				{
					timeStamp.add(time.get(i));
					insertFlag = 0;
				}
			}
			timeStamp.add(time.get(Appliance_status.size()-1));
			
			DecimalFormat df = new DecimalFormat("##.#####");
			long totalTime = getTotalTime(timeStamp);
			energyConsumption = (totalTime*Ackw)/3600;
			
			System.out.println("Energy Consumed by AC : " + df.format(energyConsumption));
			
		}catch(Exception e)
		{}

		return energyConsumption;
	}
	
	public double getEnergyCalcLight(String  time1, String time2)
	{
		double energyConsumption=0;
		try
		{
			
			ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
			nameValuePairs1.add(new BasicNameValuePair("userID",String.valueOf(userID)));
			nameValuePairs1.add(new BasicNameValuePair("ApplianceID",String.valueOf(ApplianceID)));
			nameValuePairs1.add(new BasicNameValuePair("time1",String.valueOf(time1)));
			nameValuePairs1.add(new BasicNameValuePair("time2",String.valueOf(time2)));
			
			String httpPostString = "http://"+Thermo_main.ip_addr_server+"/getEnergyForLight_java.php";
			
			String result = Thermo_main.help.getResult(nameValuePairs1, httpPostString);
			//System.out.println(result);
			
			JSONArray array = new JSONArray(result);
			
			List<String> timeRoom1 = new ArrayList<String>();
			List<String> timeRoom2 = new ArrayList<String>();
			List<String> timeRoom3 = new ArrayList<String>();
			List<String> Appliance_status1 = new ArrayList<String>();
			List<String> Appliance_status2 = new ArrayList<String>();
			List<String> Appliance_status3 = new ArrayList<String>();
			

			for(int n =0; n< array.length(); n++)
			{
				JSONObject objEner = array.getJSONObject(n);
				if(objEner.getInt("room") == 1)
				{
					timeRoom1.add(objEner.getString("curr_time")); 
					Appliance_status1.add(objEner.getString("Appliance_status"));
					
					continue;
				}
				if(objEner.getInt("room") == 2)
				{
					timeRoom2.add(objEner.getString("curr_time")); 
					Appliance_status2.add(objEner.getString("Appliance_status"));
					continue;
				}	
				if(objEner.getInt("room") == 3)
				{
					timeRoom3.add(objEner.getString("curr_time")); 
					Appliance_status3.add(objEner.getString("Appliance_status"));
					continue;
				}	
			}

			long OnTimeRoom1 = getTotalTimePerRoom(timeRoom1,Appliance_status1);
			long OnTimeRoom2 = getTotalTimePerRoom(timeRoom2,Appliance_status2);
			long OnTimeRoom3 = getTotalTimePerRoom(timeRoom3,Appliance_status3);

			long totalTime = OnTimeRoom1 + OnTimeRoom2 + OnTimeRoom3;
			
			//double dimValpercent = totalDimmValue/100;

			energyConsumption = (totalTime*bkw)/3600;
			
			DecimalFormat df = new DecimalFormat("##.#####");
			//System.out.println("Dimmer status values : " + dimm);
			System.out.println("total on Time : " + totalTime+"sec");
			System.out.println("Energy Consumed by Light : " + df.format(energyConsumption));
			
		}catch(Exception e)
		{}
		return energyConsumption;
	}
	
	public long getTotalTimePerRoom(List<String> timeRoom, List<String>Appliance_status)
	{
		List<String> timeStamp = new ArrayList<String>();
		List<Integer> dimm = new ArrayList<Integer>();
		int offFlag = 0;
		int insertFlag = 0;
		for(int i=0;i<Appliance_status.size();i++)
		{
			/*if((Appliance_status1.get(i).equals("on")))
			{
				dimm.add(DimmerStatus1.get(i));
				//timeStampTemp.add(time.get(i));
				offFlag=1;
			}
			else if(offFlag == 1)
			{
				dimm.add(DimmerStatus.get(i));
				//timeStampTemp.add(time.get(i));
				offFlag =0;
			}*/
			if((Appliance_status.get(i).equals("on")) && insertFlag==0)
			{
				timeStamp.add(timeRoom.get(i));
				insertFlag=1;
			}
			else if(Appliance_status.get(i).equals("off") && insertFlag ==1)
			{
				timeStamp.add(timeRoom.get(i));
				insertFlag = 0;
			}
		}
		timeStamp.add(Thermo_main.time2);

		if((timeStamp.size() & 1) == 1)
		{
			timeStamp.remove(timeStamp.size()-1);
		}
		/*double totalDimmValue = 0;
		for(int i=0;i<dimm.size();i++)
		{
			totalDimmValue = totalDimmValue + dimm.get(i);
		}

		totalDimmValue = (totalDimmValue/(dimm.size()));

		System.out.println(totalDimmValue);*/
		long returnTime = getTotalTime(timeStamp);
		return returnTime;
	}
	
	public long getTotalTime(List<String> timeStamp)
	{
		long totalTime = 0;

		for(int i=0;i<timeStamp.size();)
		{	try
		{	
			String t1 = timeStamp.get(i);
			String t2 = timeStamp.get(i+1);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(format.parse(t1));

			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(format.parse(t2));

			Long diff = cal2.getTimeInMillis() - cal1.getTimeInMillis();
			totalTime = totalTime + diff;
			i = i+2;

		}catch(ParseException  e){} 
		catch (java.text.ParseException e) {}
		}
		return (totalTime/1000);
	}
}
