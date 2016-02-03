package br.sma.jadecloudsim.behaviour;

import java.util.Vector;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetInitiator;

public class VmAllocationBehaviour extends ContractNetInitiator{


	private static final long serialVersionUID = -8979788616871197071L;

	public static MessageTemplate mt = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
	

	public VmAllocationBehaviour(Agent a, ACLMessage cfp) {
		super(a, cfp);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void handlePropose(ACLMessage propose, Vector acceptances) {
		// TODO Auto-generated method stub
		//super.handlePropose(propose, acceptances);
		System.out.println("Agent "+propose.getSender().getName()+" proposed "+propose.getContent());
	}
	
	@Override
	protected void handleRefuse(ACLMessage refuse) {
		// TODO Auto-generated method stub
		//super.handleRefuse(refuse);
		System.out.println("Agent "+refuse.getSender().getName()+" refused");
	}
	
	@Override
	protected void handleFailure(ACLMessage failure) {
		// TODO Auto-generated method stub
		//super.handleFailure(failure);
		
		if (failure.getSender().equals(myAgent.getAMS())) {
			// FAILURE notification from the JADE runtime: the receiver
			// does not exist
			System.out.println("Responder does not exist");
		}
		else {
			System.out.println("Agent "+failure.getSender().getName()+" failed");
		}
	}
	
	@Override
	protected void handleAllResponses(Vector responses, Vector acceptances) {
		// TODO Auto-generated method stub
		//super.handleAllResponses(responses, acceptances);
		System.out.println("Response evaluation");
	}
	
	@Override
	protected void handleInform(ACLMessage inform) {
		// TODO Auto-generated method stub
		//super.handleInform(inform);
		
		System.out.println("Agent "+inform.getSender().getName()+" successfully performed the requested action");
	}
}
