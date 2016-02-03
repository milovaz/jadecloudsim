package br.sma.jadecloudsim.main;

import jade.wrapper.AgentController;
import jade.wrapper.State;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import br.sma.jadecloudsim.agent.BrokerAgent;
import br.sma.jadecloudsim.agent.DataCenterAgent;
import br.sma.jadecloudsim.agent.PlataformAgent;
import br.sma.jadecloudsim.cloud.CloudElements;
import br.sma.jadecloudsim.cloud.CustomDataCenterBroker;
import br.sma.jadecloudsim.util.SuporteUtil;

public class CloudSimulationSuporte {

	private static int flagSimulacao = 1;
	
	public static void setupSimulacao() {
		int num_user = 1;   // number of cloud users
		Calendar calendar = Calendar.getInstance();
		boolean trace_flag = false;  // mean trace events

		// Inicializar CloudSim
		CloudSim.init(num_user, calendar, trace_flag);
		
		Log.setDisabled(true);
		
		if(flagSimulacao == 1){
			List<Datacenter> dataCenterList = new ArrayList<>();
			
			Iterator<DatacenterCharacteristics> iter = SuporteUtil.getDatacenterCharacteristicsMap().values().iterator();
			
			Datacenter datacenter0 = SuporteUtil.createDatacenter("Datacenter_0", iter.next());
			Datacenter datacenter1 = SuporteUtil.createDatacenter("Datacenter_1", iter.next());
			Datacenter datacenter2 = SuporteUtil.createDatacenter("Datacenter_2", iter.next());
			Datacenter datacenter3 = SuporteUtil.createDatacenter("Datacenter_3", iter.next());
			
			dataCenterList.add(datacenter0);
			dataCenterList.add(datacenter1);
			dataCenterList.add(datacenter2);
			dataCenterList.add(datacenter3);
			
			CloudElements.setDataCenterList(dataCenterList);
		}
				
		//Third step: Create Broker
		CustomDataCenterBroker broker = SuporteUtil.createBroker();
		
		int brokerId = broker.getId();
		
		DatacenterBroker dataCenterBroker = broker;
		
		//Fourth step: Create one virtual machine
		List<Vm> vmlist = new ArrayList<Vm>();

		CloudElements.setCreatedVms(new HashMap<Integer, Vm>());
		//VM description
		int vmid = 0;
		int mips = 250;
		long size = 10000; //image size (MB)
		int ram = 512; //vm memory (MB)
		long bw = 1000;
		int pesNumber = 1; //number of cpus
		String vmm = "Xen"; //VMM name

		//create two VMs
		Vm vm1 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());

		//the second VM will have twice the priority of VM1 and so will receive twice CPU time
		vmid++;
		Vm vm2 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());

		//add the VMs to the vmList
		vmlist.add(vm1);
		CloudElements.addCreatedVm(vm1);
		vmlist.add(vm2);
		CloudElements.addCreatedVm(vm2);
		
		

		//submit vm list to the broker
		broker.submitVmList(vmlist);


		//Fifth step: Create two Cloudlets
		List<Cloudlet> cloudletList = new ArrayList<Cloudlet>();
		CloudElements.setCloudlets(cloudletList);

		//Cloudlet properties
		int id = 0;
		long length = 40000;
		long fileSize = 300;
		long outputSize = 300;
		UtilizationModel utilizationModel = new UtilizationModelFull();

		Cloudlet cloudlet1 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
		cloudlet1.setUserId(brokerId);

		id++;
		Cloudlet cloudlet2 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
		cloudlet2.setUserId(brokerId);

		//add the cloudlets to the list
		cloudletList.add(cloudlet1);
		cloudletList.add(cloudlet2);

		//submit cloudlet list to the broker
		broker.submitCloudletList(cloudletList);

		CloudElements.setDataCenterBroker(broker);
		
		
		/*AgentController acCloudMonitor = getContainerController().createNewAgent("cloudMonitor", 
				CloudMonitorAgent.class.getName(), new Object[]{broker.getId()});
		
		acCloudMonitor.start();*/
		
		//bind the cloudlets to the vms. This way, the broker
		//// will submit the bound cloudlets only to the specific VM
		//broker.bindCloudletToVm(cloudlet1.getCloudletId(),vm1.getId());
		//broker.bindCloudletToVm(cloudlet2.getCloudletId(),vm2.getId());

		// Sixth step: Starts the simulation
		/*CloudSim.startSimulation();

		
		List<Cloudlet> newList = dataCenterBroker.getCloudletReceivedList();

		CloudSim.stopSimulation();

    	SuporteUtil.printCloudletList(newList);

		//Print the debt of each user to each datacenter
    	dataCenterList.get(0).printDebts();
    	dataCenterList.get(1).printDebts();

		Log.printLine("CloudSimExample4 finished!");*/
	}
	
}
