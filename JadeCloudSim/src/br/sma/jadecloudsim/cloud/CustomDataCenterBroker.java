package br.sma.jadecloudsim.cloud;

import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;

import br.sma.jadecloudsim.agent.BrokerAgent;
import br.sma.jadecloudsim.agent.CloudMonitorAgent;


public class CustomDataCenterBroker extends DatacenterBroker {

	private final int CUSTOM_TAG = 1044;
	
	private ContainerController containerController;
	
	public CustomDataCenterBroker(String name) throws Exception {
		super(name);
	}

	@Override
	public void processEvent(SimEvent ev) {
		//Log.printLine(CloudSim.clock()+"[Broker]: event received:"+ev.getTag());
		switch (ev.getTag()){
			// Resource characteristics request
			case CloudSimTags.RESOURCE_CHARACTERISTICS_REQUEST:
				processResourceCharacteristicsRequest(ev);
				break;
			// Resource characteristics answer
	        case CloudSimTags.RESOURCE_CHARACTERISTICS:
	        	processResourceCharacteristics(ev);
	            break;
	        // VM Creation answer
	        case CloudSimTags.VM_CREATE_ACK:
	           	processVmCreate(ev);
	           	break;
	        //A finished cloudlet returned
	        case CloudSimTags.CLOUDLET_RETURN:
	        	processCloudletReturn(ev);
	            break;
	        // if the simulation finishes
	        case CloudSimTags.END_OF_SIMULATION:
	        	shutdownEntity();
	            break;
	        case CUSTOM_TAG:
	        	continueExecution();
	        	break;
            // other unknown tags are processed by this method
	        default:
	            processOtherEvent(ev);
	            break;
		}
	}
	
	protected void continueExecution() {
		CloudSim.send(this.getId(), this.getId(), 0, CUSTOM_TAG, null);
	}
	
	protected void customProcessResourceCharacteristicsRequest(SimEvent ev) {
		try {
			AgentController acBroker = getContainerController().createNewAgent("broker", 
					BrokerAgent.class.getName(), new Object[]{this.getId()});
			
			acBroker.start();
			
			CloudSim.send(this.getId(), this.getId(), 0, CUSTOM_TAG, null);
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void processResourceCharacteristicsRequest(SimEvent ev) {
		//customProcessResourceCharacteristicsRequest(ev);
		
		setDatacenterIdsList(CloudSim.getCloudResourceList());
		setDatacenterCharacteristicsList(new HashMap<Integer, DatacenterCharacteristics>());

		Log.printLine(CloudSim.clock()+": "+getName()+ ": Cloud Resource List received with "+getDatacenterIdsList().size()+" resource(s)");

		if(CloudElements.getMapVmDatacenter() != null && !CloudElements.getMapVmDatacenter().isEmpty()){
			//submitVmList(new ArrayList<Vm>(CloudElements.getMapVmDatacenter().keySet()));
			
			for(Vm vm : getVmList()){
				if(vm != null){
					createVmsInDatacenter(CloudElements.getMapVmDatacenter().get(vm.getId()).getId());
				}
			}
			
		}
	}
	
	@Override
	protected void processResourceCharacteristics(SimEvent ev) {
		
		DatacenterCharacteristics characteristics = (DatacenterCharacteristics) ev.getData();
		getDatacenterCharacteristicsList().put(CloudSim.getEntityId(characteristics.getResourceName()), characteristics);

		
		if (getDatacenterCharacteristicsList().size() == getDatacenterIdsList().size()) {
			setDatacenterRequestedIdsList(new ArrayList<Integer>());
			createVmsInDatacenter(getDatacenterIdsList().get(0));
		}
	}
	
	@Override
	protected void createVmsInDatacenter(int datacenterId) {
		// send as much vms as possible for this datacenter before trying the next one
		int requestedVms = 0;
		String datacenterName = CloudSim.getEntityName(datacenterId);
		for (Vm vm : getVmList()) {
			if(vm != null){
				if (!getVmsToDatacentersMap().containsKey(vm.getId())) {
					Log.printLine(CloudSim.clock() + ": " + getName() + ": Trying to Create VM #" + vm.getId() + " in " + datacenterName);
					sendNow(datacenterId, CloudSimTags.VM_CREATE_ACK, vm);
					requestedVms++;
				}
			}
		}

		getDatacenterRequestedIdsList().add(datacenterId);

		setVmsRequested(requestedVms);
		setVmsAcks(0);
	}
		
	@Override
	public void startEntity() {
		super.startEntity();
	}

	private ContainerController getContainerController() {
		return containerController;
	}

	public void setContainerController(ContainerController containerController) {
		this.containerController = containerController;
	}
	
	@Override
	public void shutdownEntity() {
		
		super.shutdownEntity();
	}
}
