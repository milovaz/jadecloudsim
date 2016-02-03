package br.sma.jadecloudsim.behaviour;

import java.io.IOException;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;

import br.sma.jadecloudsim.agent.DataCenterAgent;
import br.sma.jadecloudsim.cloud.CloudElements;
import br.sma.jadecloudsim.util.DataCenterProposal;
import br.sma.jadecloudsim.util.SuporteUtil;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;


public class DataCenterBehaviour extends ContractNetResponder{

	
	private static final long serialVersionUID = 3779374021122634272L;

	private int dataCenterId;
	
	public static MessageTemplate mt = MessageTemplate.MatchProtocol(
			FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
	
	public DataCenterBehaviour(Agent agent, MessageTemplate mt, int dataCenterId) {
		super(agent, mt);
		this.dataCenterId = dataCenterId;
	}
	
	@Override
	protected ACLMessage prepareResponse(ACLMessage cfp)
			throws NotUnderstoodException, RefuseException {
		// TODO Auto-generated method stub
		//return super.prepareResponse(cfp);
		//String proposal = "";
		//int proposal = (int) (Math.random() * 10);
		DataCenterProposal proposal = new DataCenterProposal();
		//Datacenter datacenter = (Datacenter)CloudSim.getEntity(((DataCenterAgent)getAgent()).getDataCenterId());
		Datacenter datacenter = (Datacenter)CloudSim.getEntity(this.dataCenterId);
		DatacenterCharacteristics characteristics = SuporteUtil.getDatacenterCharacteristicsMap().get(datacenter.getId());
		/*CloudSim.send(datacenter.getId(), datacenter.getId(), 0, CloudSimTags.RESOURCE_CHARACTERISTICS, datacenter.getId());
		SimEvent evn = datacenter.getNextEvent();
		System.out.println(evn.getData());*/
		
		//proposal.setDataCenterId(((DataCenterAgent)getAgent()).getDataCenterId());
		proposal.setDataCenterId(this.dataCenterId);
		proposal.setCostPerBw(characteristics.getCostPerBw());
		proposal.setCostPerMem(characteristics.getCostPerMem());
		proposal.setCostPerMi(characteristics.getCostPerMi());
		proposal.setCostPerStorage(characteristics.getCostPerStorage());
		
		ACLMessage propose = cfp.createReply();
		propose.setPerformative(ACLMessage.PROPOSE);
		//propose.setContent(String.valueOf(proposal));
		try {
			propose.setContentObject(proposal);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return propose;
	}
	
	protected boolean sendVmCreation(ACLMessage cfp) {
		String [] vmIds = cfp.getContent().split(",");
		for(String vmId : vmIds){
			//CloudElements.addLigacaoVmDatacenter(Integer.valueOf(vmId), ((DataCenterAgent)getAgent()).getDataCenterId());
			CloudElements.addLigacaoVmDatacenter(Integer.valueOf(vmId), this.dataCenterId);
		}
		
		return true;
	}
	
	@Override
	protected ACLMessage prepareResultNotification(ACLMessage cfp,
			ACLMessage propose, ACLMessage accept) throws FailureException {
		// TODO Auto-generated method stub
		//return super.prepareResultNotification(cfp, propose, accept);
		System.out.println("Agent "+cfp.getSender().getLocalName()+": Proposta aceita");
		//System.out.println("Message: " + cfp.getContent());
		
		if (performAction(cfp.getContent())) {
			System.out.println("Agent "+cfp.getSender().getLocalName()+": Ação executada com sucesso");
			ACLMessage inform = accept.createReply();
			inform.setPerformative(ACLMessage.INFORM);
			//inform.setContent(String.valueOf(((DataCenterAgent)getAgent()).getDataCenterId()));
			inform.setContent(String.valueOf(this.dataCenterId));
			return inform;
		}
		else {
			System.out.println("Agent "+cfp.getSender().getLocalName()	+": Execução da ação falhou");
			throw new FailureException("unexpected-error");
		}	
	}
	 
	@Override
	protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose,
			ACLMessage reject) {
		// TODO Auto-generated method stub
		//super.handleRejectProposal(cfp, propose, reject);
		System.out.println("Agent " + cfp.getSender().getLocalName() + ": Proposta rejeitada");
	}
	
	private boolean performAction(String content) {
		
		if(content != null && !content.isEmpty()){
			// Simulate action execution by generating a random number
			//CloudElements.getDatacenterBrokerSelection().put(getAgent().getLocalName(), 
			//		(Datacenter)CloudSim.getEntity(((DataCenterAgent)getAgent()).getDataCenterId()));
			CloudElements.getDatacenterBrokerSelection().put(getAgent().getLocalName(), (Datacenter)CloudSim.getEntity(this.dataCenterId));
			
			String [] vmIds = content.split(",");
			
			for(String vmId : vmIds){
				CloudElements.addLigacaoVmDatacenter(Integer.valueOf(vmId), this.dataCenterId);
			}
			
		}
		
		return true;
	}
}
