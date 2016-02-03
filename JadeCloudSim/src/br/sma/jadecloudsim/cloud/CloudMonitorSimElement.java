package br.sma.jadecloudsim.cloud;

import java.util.Map;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;

public class CloudMonitorSimElement extends SimEntity{

	public CloudMonitorSimElement(String name) {
		super(name);
	}

	@Override
	public void processEvent(SimEvent ev) {
		switch (ev.getTag()) {
			// Pauses a previously submitted Cloudlet, but the sender
			// asks for an acknowledgement
			case CloudSimTags.CLOUDLET_PAUSE_ACK:
				processCloudletPauseAck(ev, CloudSimTags.CLOUDLET_PAUSE_ACK);
				break;

		default:
			break;
		}
	}
	
	public void processCloudletPauseAck(SimEvent ev, int tag) {
		System.out.println("Cloudlet PAUSE ACK received");
	}

	@Override
	public void shutdownEntity() {
		Log.printLine(getName() + " is shutting down...");
	}

	@Override
	public void startEntity() {
		Log.printLine(getName() + " is starting...");
		
		System.out.println("Teste DC ID = " + CloudElements.getMapVmDatacenter().get(0).getId());
		
		int [] data = {0, 6, 0};
		
		Map<Integer, Datacenter> m = CloudElements.getMapVmDatacenter();
		
		schedule(CloudElements.getMapVmDatacenter().get(0).getId(), 160, CloudSimTags.CLOUDLET_PAUSE, CloudElements.getCloudlets().get(0));
   	}

}
