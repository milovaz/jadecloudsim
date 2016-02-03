package br.sma.jadecloudsim.main;

import java.util.ArrayList;
import java.util.Calendar;
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
import br.sma.jadecloudsim.ui.PrincipalUI;
import br.sma.jadecloudsim.util.SuporteUtil;
import br.sma.jadecloudsim.util.UserServiceNeeds;
import jade.Boot;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class Principal {

	private static Principal instance = null; 
	
	private ContainerController containerController;
	
	private static List<Vm> vmlist;
	
	private int qtdDataCenters; 
	private int qtdCloudLets;
	
	protected Principal() {
	}
	
	public static Principal getInstance() {
		if(instance == null){
			instance = new Principal();
		}
		
		return instance;
	}
	
	public void initJade() {
		Boot.main(new String [] {"-gui"});
		Runtime rt = Runtime.instance();
		Profile p = new ProfileImpl();
		ContainerController cc = rt.createAgentContainer(p);
		setContainerController(cc);
		rt.setCloseVM(false);
	}
	
	public void reinitSimulation() {
		Profile p = new ProfileImpl();
		Runtime rt = Runtime.instance();
		rt.startUp(p);
		ContainerController cc = rt.createAgentContainer(p);
		setContainerController(cc);
	}
	
	public void stopSimulation() {
		
		if(CloudSim.running()){
			CloudSim.stopSimulation();	
		}
		
		try {
			getContainerController().kill();
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	public void reinitCloudSim() {
		int num_user = 1;   // number of cloud users
		Calendar calendar = Calendar.getInstance();
		boolean trace_flag = false;  // mean trace events
		
		CloudSim.init(num_user, calendar, trace_flag);
		
		Log.disable();
		
		List<Datacenter> dataCenterList = new ArrayList<>();
		
		Iterator<DatacenterCharacteristics> iter = SuporteUtil.getDatacenterCharacteristicsMap().values().iterator();
		for(int i = 0; i < qtdDataCenters; i++){
			String dataCenterName = "Datacenter_" + i;
			Datacenter datacenter = SuporteUtil.createDatacenter(dataCenterName, iter.next());
			dataCenterList.add(datacenter);
		}
		
		CloudElements.setDataCenterList(dataCenterList);
		
		CustomDataCenterBroker broker = SuporteUtil.createBroker();
		broker.setContainerController(getContainerController());
		int brokerId = broker.getId();
		
		DatacenterBroker dataCenterBroker = broker;
		
		vmlist = new ArrayList<Vm>();

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

		
		int id = 0;
		long length = 40000;
		long fileSize = 300;
		long outputSize = 300;
		UtilizationModel utilizationModel = new UtilizationModelFull();

		
		for(int i = 0; i < qtdCloudLets; i++){
			Cloudlet cloudlet = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet.setUserId(brokerId);
			
			cloudletList.add(cloudlet);
			
			id++;
		}
		
		UserServiceNeeds userServiceNeeds = new UserServiceNeeds();
		userServiceNeeds.setCloudlets(cloudletList);
		
		//submit cloudlet list to the broker
		broker.submitCloudletList(cloudletList);

		CloudElements.setDataCenterBroker(broker);
	}
	
	public void initCloudSim(int qtdDataCenters, int qtdCloudLets){
		try {
			
			this.qtdDataCenters = qtdDataCenters;
			this.qtdCloudLets = qtdCloudLets;
			
			// First step: Initialize the CloudSim package. It should be called
			// before creating any entities.
			int num_user = 1;   // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false;  // mean trace events
			
			AgentController acPlataform = getContainerController().createNewAgent("PlataformAgent", 
					PlataformAgent.class.getName(), null);
			acPlataform.start();

			// Initialize the GridSim library
			CloudSim.init(num_user, calendar, trace_flag);
			
			Log.disable();
			
			// Second step: Create Datacenters
			//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
			List<Datacenter> dataCenterList = new ArrayList<>();
			
			Object[][] data = new Object[qtdDataCenters][];
			
			for(int i = 0; i < qtdDataCenters; i++){
				String dataCenterName = "Datacenter_" + i;
				Datacenter datacenter = SuporteUtil.createDatacenter(dataCenterName);
				dataCenterList.add(datacenter);
				
				data[i] = new Object[]{datacenter.getId(), dataCenterName, ""};
				
				AgentController acDataCenter = getContainerController().createNewAgent(dataCenterName, 
						DataCenterAgent.class.getName(), new Object[]{datacenter.getId()});
				acDataCenter.start();
				
				while(acDataCenter.getState().getCode() != Agent.AP_IDLE){
					Thread.sleep(100);
				}
			}
			
			PrincipalUI.getInstance().carregarTabelaDataCenters(data);

			
			CloudElements.setDataCenterList(dataCenterList);
			
			CustomDataCenterBroker broker = SuporteUtil.createBroker();
			broker.setContainerController(getContainerController());
			int brokerId = broker.getId();
			
			DatacenterBroker dataCenterBroker = broker;
			
			vmlist = new ArrayList<Vm>();

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

			
			int id = 0;
			long length = 40000;
			long fileSize = 300;
			long outputSize = 300;
			UtilizationModel utilizationModel = new UtilizationModelFull();

			
			for(int i = 0; i < qtdCloudLets; i++){
				Cloudlet cloudlet = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
				cloudlet.setUserId(brokerId);
				
				cloudletList.add(cloudlet);
				
				id++;
			}
			
			UserServiceNeeds userServiceNeeds = new UserServiceNeeds();
			userServiceNeeds.setCloudlets(cloudletList);
			
			//submit cloudlet list to the broker
			broker.submitCloudletList(cloudletList);

			CloudElements.setDataCenterBroker(broker);
			
			AgentController acBroker = getContainerController().createNewAgent("broker", 
					BrokerAgent.class.getName(), new Object[]{broker.getId()});
			
			acBroker.start();
			
		}catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
	}
	
	
	public ContainerController getContainerController() {
		return containerController;
	}

	public void setContainerController(ContainerController containerController) {
		this.containerController = containerController;
	}

	public static void main(String[] args) {
		Principal main = new Principal();
	}

}
	
	
	
	
	
	
