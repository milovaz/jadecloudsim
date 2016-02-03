package br.sma.jadecloudsim.behaviour;


import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import br.sma.jadecloudsim.agent.BrokerAgent;
import br.sma.jadecloudsim.agent.CloudMonitorAgent;
import br.sma.jadecloudsim.cloud.CustomDataCenterBroker;
import br.sma.jadecloudsim.ui.PrincipalUI;
import br.sma.jadecloudsim.util.BrokerUserNeeds;
import br.sma.jadecloudsim.util.CloudServiceData;
import br.sma.jadecloudsim.util.DataCenterProposal;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class BrokerBehaviour extends ContractNetInitiator{


	private static final long serialVersionUID = 4824054367699676602L;

	private BrokerUserNeeds brokerUserNeeds;
	
	
	public BrokerBehaviour(Agent a, ACLMessage cfp) {
		super(a, cfp);
	}
	
	
	@Override
	protected void handlePropose(ACLMessage propose, Vector acceptances) {
		// TODO Auto-generated method stub
		//super.handlePropose(propose, acceptances);
		System.out.println("Agent "+propose.getSender().getName()+" proposed "+propose.getContent());
		try {
			DataCenterProposal proposal = (DataCenterProposal) propose.getContentObject();
			
			
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
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
		System.out.println("\nIniting response evaluation...");
		
		try {
			BrokerUserNeeds brokerUserNeeds = ((BrokerAgent)getAgent()).getBrokerUserNeeds();
			acceptances.addAll(avaliarPropostas(responses, brokerUserNeeds));
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*BrokerUserNeeds brokerUserNeeds = ((BrokerAgent)getAgent()).getBrokerUserNeeds();
		
		AID bestProposer = null;
		ACLMessage accept = null;
		List<ACLMessage> listMem = new ArrayList<>();
		List<ACLMessage> listStg = new ArrayList<>();
		double bestProposal = -1;
			
		Enumeration e = responses.elements();
		while (e.hasMoreElements()) {
			ACLMessage msg = (ACLMessage) e.nextElement();
			if (msg.getPerformative() == ACLMessage.PROPOSE) {
				listMem.add(msg);
				listStg.add(msg);
				try {
					double costMem = ((DataCenterProposal)msg.getContentObject()).getCostPerMem();
					System.out.println(msg.getSender().getLocalName() + " Proposed CosPerMer = " + costMem);
				} catch (UnreadableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		SuporteUtil.ordenarPorMem(listMem);
		SuporteUtil.ordenarPorStorage(listStg);
		
		ACLMessage reply = listMem.get(0).createReply();
		reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
		acceptances.addElement(reply);
		
		for(int i = 1; i < listMem.size(); i++){
			reply = listMem.get(i).createReply();
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
			acceptances.addElement(reply);
		}
		
		
		bestProposer = listMem.get(0).getSender();
		accept = reply;
		
		//int proposal = Integer.parseInt(msg.getContent());
		try {
			bestProposal = ((DataCenterProposal) listMem.get(0).getContentObject()).getCostPerMem();
		} catch (UnreadableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (accept != null) {
			System.out.println("Accepting proposal "+bestProposal+" from responder "+bestProposer.getName());
			//accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
		}*/
	}
	
	private List<ACLMessage> ordenarPorMem(List<ACLMessage> msgs) {
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
	
	
	/**
	 * Avalia as propostas enviadas pelos agentes de DataCenter
	 * 
	 * @param responses
	 * @param brokerUserNeeds
	 * @return acceptances
	 * @throws UnreadableException
	 */
	private Vector avaliarPropostas(Vector responses, BrokerUserNeeds brokerUserNeeds) throws UnreadableException {
		
		Vector acceptances = new Vector<>();
		
		double bestCostProposal = -1;
		AID bestProposer = null;
		ACLMessage accept = null;
		
		
		CustomDataCenterBroker customDataCenterBroker = (CustomDataCenterBroker) CloudSim.getEntity(((BrokerAgent)getAgent()).getDataCenterBrokerId());
		List<Vm> vms = customDataCenterBroker.getVmList();
		
		Enumeration e = responses.elements();
		while (e.hasMoreElements()) {
			ACLMessage msg = (ACLMessage) e.nextElement();
			if (msg.getPerformative() == ACLMessage.PROPOSE) {
				
				ACLMessage reply = msg.createReply();
				reply = msg.createReply();
				reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
				acceptances.addElement(reply);
				
				DataCenterProposal dataCenterProposal = (DataCenterProposal)msg.getContentObject();
				double totalCost = calcularCustoTotalPorVms(dataCenterProposal, vms); 
				
				PrincipalUI.getInstance().addLog("Proposta recebida de " + msg.getSender().getLocalName() + " = " + totalCost);
				Double rating = ((BrokerAgent)getAgent()).getBaseConhecimentoCloudService().getDataCenterRatings().get(dataCenterProposal.getDataCenterId());
				
				PrincipalUI.getInstance().addLog("Rating " + msg.getSender().getLocalName() + " = " + ((rating == null) ? 0: rating));
				
				totalCost = ajustarCustoPorHistorico(totalCost, dataCenterProposal);
				
				if(totalCost == Double.MAX_VALUE){
					PrincipalUI.getInstance().addLog(msg.getSender().getLocalName() + "está na blacklist e foi eliminado da concorrencia");
				}
				
				if(bestCostProposal < 0){
					bestCostProposal = totalCost;
					bestProposer = msg.getSender();
					accept = reply;
				}else{
					if(totalCost < bestCostProposal){
						bestCostProposal = totalCost;
						bestProposer = msg.getSender();
						accept = reply;
					}
				}
			}
		}
		
		if(accept != null){
			accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			String msgContent = "" + vms.get(0).getId();
			for(int i = 1; i < vms.size(); i++){
				msgContent = msgContent + "," + vms.get(i).getId();
			}
			
			accept.setContent(msgContent);
			
			PrincipalUI.getInstance().addLog("\n" + bestProposer.getLocalName() + " selecionado \n");
		}
		
		return acceptances;
	}
	
	private double ajustarCustoPorHistorico(double custoAtual, DataCenterProposal dataCenterProposal) {
		boolean flagBlackList = ((BrokerAgent)getAgent()).getBaseConhecimentoCloudService().getDataCenterBlackList()
										.contains(dataCenterProposal.getDataCenterId());
		
		Double rating = ((BrokerAgent)getAgent()).getBaseConhecimentoCloudService().getDataCenterRatings().get(dataCenterProposal.getDataCenterId());
		
		if(flagBlackList){
			custoAtual = Double.MAX_VALUE;
		}else if(rating != null){
			custoAtual = custoAtual + (custoAtual * (rating/10.0));
		}
		
		return custoAtual;
	}
	
	private double calcularCustoTotalPorVms(DataCenterProposal proposal, List<Vm> vms) {
		
		double total = 0;
		if(vms != null && vms.size() > 0){
			for(Vm vm : vms){
				total += vm.getRam() * proposal.getCostPerMem() + vm.getSize() * proposal.getCostPerStorage() + proposal.getCostPerMi() + proposal.getCostPerBw();
			}
		}
		
		return total;
	}
	
	@Override
	protected void handleInform(ACLMessage inform) {
		// TODO Auto-generated method stub
		//super.handleInform(inform);
		
		System.out.println("Agent "+inform.getSender().getName() + 
				" successfully performed the requested action - Content = " + inform.getContent());
		
		AgentController acCloudMonitor;
		try {
			
			acCloudMonitor = getAgent().getContainerController().getAgent("cloudMonitor" + inform.getContent());

		} catch (ControllerException e) {
			//O monitor não existe
			
			try {
				Double dataCenterRating = null;
				if( (dataCenterRating = ((BrokerAgent)getAgent()).getBaseConhecimentoCloudService().getDataCenterRatings().get(Integer.valueOf(inform.getContent()))) == null){
					dataCenterRating = 0.0;
				}
				
				acCloudMonitor = getAgent().getContainerController().createNewAgent("cloudMonitor" + inform.getContent(), 
						CloudMonitorAgent.class.getName(), 
						new Object[]{((BrokerAgent)getAgent()).getLocalName(), Integer.valueOf(inform.getContent()), ((BrokerAgent)getAgent()).getCloudServiceStandards(), dataCenterRating});
				
				acCloudMonitor.start();
				
				((BrokerAgent)getAgent()).getMonitorToDataCenterMap().put(acCloudMonitor.getName(), Integer.valueOf(inform.getContent()));
				if(!((BrokerAgent)getAgent()).getHistoricoCloudService().containsKey(Integer.valueOf(inform.getContent()))){
					((BrokerAgent)getAgent()).getHistoricoCloudService().put(Integer.valueOf(inform.getContent()), new CloudServiceData());
				}
				
				
			} catch (StaleProxyException e1) {
				e1.printStackTrace();
			}
			
		}
		
		ACLMessage goMsg = new ACLMessage(ACLMessage.REQUEST);
		goMsg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		goMsg.setConversationId("start-plataform");
		goMsg.setContent("GO");
		goMsg.addReceiver(new AID("PlataformAgent", AID.ISLOCALNAME));
		myAgent.send(goMsg);
		
		myAgent.removeBehaviour(this);
	}
	
}
