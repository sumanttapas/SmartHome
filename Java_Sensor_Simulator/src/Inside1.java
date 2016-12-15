
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DecimalFormat;



public class Inside1 extends Thread
{

	double setTemp,insideTemp;
	String mode;
	int changeFlag = 0;
	
	Thermo_main getTemp = new Thermo_main();

	Inside1(){}

	Inside1(double tempSet, double inTemp, String mode1)
	{
		setTemp = tempSet;
		insideTemp = inTemp;
		mode = mode1;
	}

	@Override
	public void run() 
	{
		while(true)
		{
			try 
			{
				Thread.sleep(1000);
				Thermo_main.help.getTempData();
				
				//if(Thermo_main.fan1 == "on" || Thermo_main.fan1 == "auto")
				//{
					switch(Thermo_main.ac_mode1)
					{

					case "Cool": 
						if(Thermo_main.setTemp1 < Thermo_main.currInsidetmp1)
						{
							Thermo_main.currInsidetmp1 = Thermo_main.currInsidetmp1 - 0.1;
							changeFlag = 1;
						}

						break;
					case "cool": 
						if(Thermo_main.setTemp1 < Thermo_main.currInsidetmp1)
						{
							Thermo_main.currInsidetmp1 = Thermo_main.currInsidetmp1 - 0.1;
							changeFlag = 1;
						}

						break;
					case "Heat":
						if(Thermo_main.setTemp1 > Thermo_main.currInsidetmp1)
						{
							Thermo_main.currInsidetmp1 = Thermo_main.currInsidetmp1 + 0.1;
							changeFlag = 1;
						}
						break;
					case "heat":
						if(Thermo_main.setTemp1 > Thermo_main.currInsidetmp1)
						{
							Thermo_main.currInsidetmp1 = Thermo_main.currInsidetmp1 + 0.1;
							changeFlag = 1;
						}
						break;
					case "off":
						break;
					default:
						break;
					}
					StringBuilder sb = new StringBuilder();
					DecimalFormat df = new DecimalFormat("##.##");
					
					if(changeFlag == 1)
					{
						changeFlag = 0;
						sb.append("ac:1001 1 " +df.format(Thermo_main.currInsidetmp1)+" "+Thermo_main.currOutsidetemp 
								+" "+ Thermo_main.setTemp1 +" "+ Thermo_main.ac_mode1 +" "+ Thermo_main.fan1);
						System.out.println(sb.toString());
						
						Thermo_main.help.createSocketConnAndSendString(sb);

					}
					Thermo_main.help.updateTempInDb(1,1);
					//Outside1.getConnection();
				//}
			} catch (Exception e) {} 
		}
	}
}