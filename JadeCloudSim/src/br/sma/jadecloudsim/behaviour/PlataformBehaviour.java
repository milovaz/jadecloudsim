package br.sma.jadecloudsim.behaviour;

import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;

import br.sma.jadecloudsim.cloud.CloudElements;
import br.sma.jadecloudsim.cloud.CloudMonitorSimElement;
import br.sma.jadecloudsim.util.SuporteUtil;

import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PlataformBehaviour extends SimpleBehaviour{


	private static final long serialVersionUID = 1091584654816220785L;
	
	MessageTemplate mt = MessageTemplate.and(
			MessageTemplate.MatchConversationId("start-plataform"),
			MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)
	);
	
	public PlataformBehaviour() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		System.out.println("\n PlataformAgent received the message");
		ACLMessage msg = myAgent.receive(mt);
		if(msg == null) { 
			block();
		} else {
			// realiza ação
			ACLMessage reply = msg.createReply();
			if(msg.getPerformative() == ACLMessage.REQUEST) {
				/*reply.setPerformative(ACLMessage.AGREE);
				reply.setContent("Agreeing to the request");
				myAgent.send(reply);*/
				
				System.out.println("\n PlataformAgent execute action");
				
				execute();
				//executeWithFailureTeste();
				
				/*reply.setPerformative(ACLMessage.INFORM);
				reply.setContent("AK");
				myAgent.send(reply);*/
			} else {
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
				reply.setContent("NOK");
				myAgent.send(reply);
			}
		}
	}

	public void execute() {
		
		
		CloudSim.startSimulation();
		
		
		List<Cloudlet> newList = CloudElements.getDataCenterBroker().getCloudletReceivedList();

		CloudSim.stopSimulation();
		
		Log.enable();

    	SuporteUtil.printCloudletList(newList);

		//Print the debt of each user to each datacenter
    	for(int i = 0; i < CloudElements.getDataCenterList().size(); i++){
    		CloudElements.getDataCenterList().get(i).printDebts();
    	}

		Log.printLine("CloudSimExample4 finished!");
	}
	
	public void executeWithFailureTeste() {
		
		CloudMonitorSimElement cloudMonitorSimElement = new CloudMonitorSimElement("monitorSimEntity");
		
		CloudSim.addEntity(cloudMonitorSimElement);
		
		CloudSim.startSimulation();
		
		List<Cloudlet> newList = CloudElements.getDataCenterBroker().getCloudletReceivedList();

		CloudSim.stopSimulation();

    	SuporteUtil.printCloudletList(newList);

		//Print the debt of each user to each datacenter
    	for(int i = 0; i < CloudElements.getDataCenterList().size(); i++){
    		CloudElements.getDataCenterList().get(i).printDebts();
    	}

		Log.printLine("CloudSimExample4 finished!");
	}
	
	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
