import java.io.PrintWriter;
import java.net.Socket;
import java.text.DecimalFormat;



public class Inside2 extends Thread{

	double setTemp,insideTemp;
	String mode;
	int changeFlag = 0;
	Thermo_main getTemp = new Thermo_main();
	Inside2(){}

	Inside2(double tempSet, double inTemp, String mode1)
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
				//if(Thermo_main.fan2 == "on" || Thermo_main.fan2 == "auto")
				//{
				Thermo_main.help.getTempData();
					switch(Thermo_main.ac_mode2)
					{

					case "Cool": 
						if(Thermo_main.setTemp2 < Thermo_main.currInsidetmp2)
						{
							Thermo_main.currInsidetmp2 = Thermo_main.currInsidetmp2 - 0.1;
							changeFlag = 1;
						}
						break;
					case "cool":
						if(Thermo_main.setTemp2 < Thermo_main.currInsidetmp2)
						{
							Thermo_main.currInsidetmp2 = Thermo_main.currInsidetmp2 - 0.1;
							changeFlag = 1;
						}

						break;
					case "heat":
						if(Thermo_main.setTemp2 > Thermo_main.currInsidetmp2)
						{
							Thermo_main.currInsidetmp2 = Thermo_main.currInsidetmp2 + 0.1;
							changeFlag = 1;
						}
						break;
					case "Heat":
						if(Thermo_main.setTemp2 > Thermo_main.currInsidetmp2)
						{
							Thermo_main.currInsidetmp2 = Thermo_main.currInsidetmp2 + 0.1;
							changeFlag = 1;
						}
						break;
					case "off":
						break;
					default:
						break;
					}
				//}
				StringBuilder sb = new StringBuilder();
				DecimalFormat df = new DecimalFormat("##.##");

				if(changeFlag == 1)
				{
					changeFlag = 0;
					sb.append("ac:1001 2 " +df.format(Thermo_main.currInsidetmp2)+" "+Thermo_main.currOutsidetemp 
							+" "+ Thermo_main.setTemp2 +" "+ Thermo_main.ac_mode2 +" "+ Thermo_main.fan2);
					
					System.out.println(sb.toString());
					Thermo_main.help.createSocketConnAndSendString(sb);
				}
				//Outside2.getConnection();
				Thermo_main.help.updateTempInDb(1,2);
			} catch (Exception e) {}
		}
	}
}