package br.sma.jadecloudsim.behaviour;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Vm;

import br.sma.jadecloudsim.agent.CloudMonitorAgent;
import br.sma.jadecloudsim.cloud.CloudElements;
import br.sma.jadecloudsim.util.BrokerUserNeeds;
import br.sma.jadecloudsim.util.UserServiceNeeds;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class CloudDimensioningBehaviour extends CyclicBehaviour{

	
	private static final long serialVersionUID = -4440396536109794387L;

	MessageTemplate mt = MessageTemplate.and(
			MessageTemplate.MatchConversationId("clouddimensioning-request"),
			MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)
	);
	
	
	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(mt);
		if (msg!=null) {
			if(msg.getPerformative() == ACLMessage.REQUEST){
				UserServiceNeeds userServiceNeeds;
				try {
					userServiceNeeds = (UserServiceNeeds) msg.getContentObject();
					List<BrokerUserNeeds> listaBrokerUserNeeds = dimensionarVms(userServiceNeeds);
					
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					
					myAgent.send(reply);
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	
	public List<BrokerUserNeeds> dimensionarVms(UserServiceNeeds userServiceNeeds) {
		//VM description
		int vmid = 0;
		int mips = 250;
		long size = 10000; //image size (MB)
		int ram = 512; //vm memory (MB)
		long bw = 1000;
		int pesNumber = 1; //number of cpus
		String vmm = "Xen"; //VMM name

		List<BrokerUserNeeds> listaBrokerUserNeeds = new ArrayList<>();
		
		for(Cloudlet cloudlet : userServiceNeeds.getCloudlets()){
			BrokerUserNeeds brokerUserNeeds = new BrokerUserNeeds();
			brokerUserNeeds.setBw(bw);
			brokerUserNeeds.setMips(mips);
			brokerUserNeeds.setPesNumber(pesNumber);
			brokerUserNeeds.setRam(ram);
			brokerUserNeeds.setSize(size);
			brokerUserNeeds.setBrokerId(userServiceNeeds.getBrokerId());
			
			listaBrokerUserNeeds.add(brokerUserNeeds);
		}
		
		return listaBrokerUserNeeds;
				
	}

}
