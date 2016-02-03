package br.sma.jadecloudsim.util;

import java.util.Date;
import java.util.Random;

import br.sma.jadecloudsim.ui.PrincipalUI;

public class MonitorSuporte {
	
	public static final int SEND_OVER_ALLOWED_OUTAGE = 1001;
	public static final int SEND_DATACENTER_RATING_UPDATE = 1002;

	public boolean isServiceAvailable() {
		Random random = new Random();
		int val = random.nextInt(10);
		//System.out.println(val);
		if (val > 5){
			return false;
		}
		
		return true;
	}
	
	public long countHoursOfOutage(Date initTime, int idDataCenter) {
		
		int c = 0;

		if(!isServiceAvailable()){
			c++;
		}
		
		/*if(c > 0){
			return new Date().getTime() - initTime.getTime();
		}
		
		return 0;*/
		return c;
	}
	
}
