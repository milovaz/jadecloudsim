package br.sma.jadecloudsim.util;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import br.sma.jadecloudsim.cloud.CustomDataCenterBroker;

public class SuporteUtil {

	private static Map<Integer, DatacenterCharacteristics> datacenterCharacteristicsMap;
	
	
	//We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
	//to the specific rules of the simulated scenario
	public static CustomDataCenterBroker createBroker(){

		CustomDataCenterBroker broker = null;
		try {
			broker = new CustomDataCenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}
	
	public static Datacenter createDatacenter(String name, DatacenterCharacteristics datacenterCharacteristics){
		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store
		//    our machine
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores.
		// In this example, it will have only one core.
		List<Pe> peList = new ArrayList<Pe>();

		if(datacenterCharacteristics == null && datacenterCharacteristicsMap == null){
			datacenterCharacteristicsMap = new HashMap<Integer, DatacenterCharacteristics>();
		}
		
		//int mips = 1000;
		int mips = 10000;


		// 3. Create PEs and add these into a list.
		peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

		//4. Create Host with its id and list of PEs and add them to the list of machines
		int hostId=0;
		int ram = 2048; //host memory (MB)
		long storage = 1000000; //host storage
		int bw = 10000;


		//in this example, the VMAllocatonPolicy in use is SpaceShared. It means that only one VM
		//is allowed to run on each Pe. As each Host has only one Pe, only one VM can run on each Host.
		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				//new VmSchedulerSpaceShared(peList)
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our first machine

		// 5. Create a DatacenterCharacteristics object that stores the
		//    properties of a data center: architecture, OS, list of
		//    Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).
		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0 * (1 + Math.random() * 10);              // the cost of using processing in this resource
		double costPerMem = 0.05 * (1 + Math.random() * 10);		// the cost of using memory in this resource
		double costPerStorage = 0.001 * (1 + Math.random() * 10);	// the cost of using storage in this resource
		double costPerBw = 0.0;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

	    DatacenterCharacteristics characteristics = datacenterCharacteristics;

	    if(characteristics == null){
	    	characteristics = new DatacenterCharacteristics(
	                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);
	    }
	    
	       
		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
			
			
			if(datacenterCharacteristics == null){
				datacenterCharacteristicsMap.put(datacenter.getId(), characteristics);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return datacenter;
		
	}
	
	public static Datacenter createDatacenter(String name){

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store
		//    our machine
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores.
		// In this example, it will have only one core.
		List<Pe> peList = new ArrayList<Pe>();

		if(datacenterCharacteristicsMap == null){
			datacenterCharacteristicsMap = new HashMap<Integer, DatacenterCharacteristics>();
		}
		
		//int mips = 1000;
		int mips = 10000;


		// 3. Create PEs and add these into a list.
		peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

		//4. Create Host with its id and list of PEs and add them to the list of machines
		int hostId=0;
		int ram = 2048; //host memory (MB)
		long storage = 1000000; //host storage
		int bw = 10000;


		//in this example, the VMAllocatonPolicy in use is SpaceShared. It means that only one VM
		//is allowed to run on each Pe. As each Host has only one Pe, only one VM can run on each Host.
		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				//new VmSchedulerSpaceShared(peList)
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our first machine

		// 5. Create a DatacenterCharacteristics object that stores the
		//    properties of a data center: architecture, OS, list of
		//    Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).
		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0 * (1 + Math.random() * 10);              // the cost of using processing in this resource
		double costPerMem = 0.05 * (1 + Math.random() * 10);		// the cost of using memory in this resource
		double costPerStorage = 0.001 * (1 + Math.random() * 10);	// the cost of using storage in this resource
		double costPerBw = 0.0;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

	       DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
	                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

	       
		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
			
			datacenterCharacteristicsMap.put(datacenter.getId(), characteristics);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return datacenter;
	}	
	
	/**
	 * Prints the Cloudlet objects
	 * @param list  list of Cloudlets
	 */
	public static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center Name" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				Log.print("SUCCESS");
				
				Log.printLine( indent + indent + cloudlet.getResourceName(cloudlet.getResourceId()) + indent + indent + indent + cloudlet.getVmId() +
						indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
						indent + indent + dft.format(cloudlet.getFinishTime()));
			}
		}

	}
	
	public static List<ACLMessage> ordenarPorMem(List<ACLMessage> msgs) {
		Collections.sort(msgs, new Comparator<ACLMessage>() {
			public int compare(ACLMessage o1, ACLMessage o2) {
				double memCost1;
				try {
					memCost1 = ((DataCenterProposal) o1.getContentObject()).getCostPerMem();
					double memCost2 = ((DataCenterProposal) o2.getContentObject()).getCostPerMem();
					return new Double(memCost1).compareTo(new Double(memCost2));
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				
				return -1;
			};	
		});
		
		return msgs;
	}
	
	public static List<ACLMessage> ordenarPorStorage(List<ACLMessage> msgs) {
		Collections.sort(msgs, new Comparator<ACLMessage>() {
			public int compare(ACLMessage o1, ACLMessage o2) {
				double stgCost1;
				try {
					stgCost1 = ((DataCenterProposal) o1.getContentObject()).getCostPerStorage();
					double stgCost2 = ((DataCenterProposal) o2.getContentObject()).getCostPerStorage();
					return new Double(stgCost1).compareTo(new Double(stgCost2));
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				
				return -1;
			};	
		});
		
		return msgs;
	}
	
	public static List<ACLMessage> ordenarPorBw(List<ACLMessage> msgs) {
		Collections.sort(msgs, new Comparator<ACLMessage>() {
			public int compare(ACLMessage o1, ACLMessage o2) {
				double cost1;
				try {
					cost1 = ((DataCenterProposal) o1.getContentObject()).getCostPerBw();
					double cost2 = ((DataCenterProposal) o2.getContentObject()).getCostPerBw();
					return new Double(cost1).compareTo(new Double(cost2));
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				
				return -1;
			};	
		});
		
		return msgs;
	}
	
	public static List<ACLMessage> ordenarPorMi(List<ACLMessage> msgs) {
		Collections.sort(msgs, new Comparator<ACLMessage>() {
			public int compare(ACLMessage o1, ACLMessage o2) {
				double cost1;
				try {
					cost1 = ((DataCenterProposal) o1.getContentObject()).getCostPerMi();
					double cost2 = ((DataCenterProposal) o2.getContentObject()).getCostPerMi();
					return new Double(cost1).compareTo(new Double(cost2));
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				
				return -1;
			};	
		});
		
		return msgs;
	}
	
	public static void runCloudSimulation(int dataCenterBrokerId) {
		DatacenterBroker dataCenterBroker = (DatacenterBroker)CloudSim.getEntity(dataCenterBrokerId);
		
		CloudSim.startSimulation();
		
		List<Cloudlet> newList = dataCenterBroker.getCloudletReceivedList();

		CloudSim.stopSimulation();

    	SuporteUtil.printCloudletList(newList);

		//Print the debt of each user to each datacenter
    	//dataCenterList.get(0).printDebts();
    	//dataCenterList.get(1).printDebts();

		Log.printLine("CloudSimExample4 finished!");
	}

	public static Map<Integer, DatacenterCharacteristics> getDatacenterCharacteristicsMap() {
		return datacenterCharacteristicsMap;
	}

	public static void setDatacenterCharacteristicsMap(
			Map<Integer, DatacenterCharacteristics> datacenterCharacteristicsMap) {
		SuporteUtil.datacenterCharacteristicsMap = datacenterCharacteristicsMap;
	}
	
	
}
