package br.sma.jadecloudsim.behaviour;

import java.util.Map;

import br.sma.jadecloudsim.agent.BrokerAgent;
import br.sma.jadecloudsim.ui.PrincipalUI;
import br.sma.jadecloudsim.util.CloudServiceData;
import br.sma.jadecloudsim.util.CloudServiceStandards;
import br.sma.jadecloudsim.util.MonitorSuporte;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class BrokerMonitorBehaviour extends CyclicBehaviour {


	private static final long serialVersionUID = -3602026932236913554L;

	
	MessageTemplate mt = MessageTemplate.and(
			MessageTemplate.MatchConversationId("cloudmonitor-info"),
			MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)
	);
	
	public BrokerMonitorBehaviour(Agent agent) {
		super(agent);
	}
	
	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(mt);
		if (msg!=null) {
			PrincipalUI.getInstance().addLog("BrokerMonitor: rebendo mensagem enviada por CloudMonitor");
			System.out.println("BrokerMonitor: rebendo mensagem enviada por CloudMonitor");
			if(msg.getPerformative() == ACLMessage.INFORM){
				if(msg.getContent() != null){
					try {
					//System.out.println("BrokerMonitor sending message to be trated");
					//Map.Entry<Integer, CloudServiceData> entry = (Map.Entry<Integer, CloudServiceData>) msg.getContentObject();
					//tratarInfosEnviadas(entry);
						CloudServiceData cloudServiceData = (CloudServiceData) msg.getContentObject();
						int tipoMsg = cloudServiceData.getTipoMensagem();
						switch (tipoMsg) {
							case MonitorSuporte.SEND_OVER_ALLOWED_OUTAGE:
								PrincipalUI.getInstance().addLog("Informação recebida de CloudMonitor = " + msg.getSender().getName());
								System.out.println("Informação recebida de CloudMonitor = " + msg.getSender().getName());
								((BrokerAgent)getAgent()).deliberarMudancaDataCenter(((BrokerAgent)getAgent()).getMonitorToDataCenterMap().get(msg.getSender().getName()));
							break;
	
							case MonitorSuporte.SEND_DATACENTER_RATING_UPDATE:
								PrincipalUI.getInstance().addLog("\nBrokerMonitor: Atualização de rating recebida");
								System.out.println("BrokerMonitor: Atualização de rating recebida");
								PrincipalUI.getInstance().addLog("DataCenterId = " + cloudServiceData.getDataCenterId() + " / Rating = " + cloudServiceData.getRating());
								System.out.println("DataCenterId = " + cloudServiceData.getDataCenterId() + " / Rating = " + cloudServiceData.getRating());
								((BrokerAgent)getAgent()).getBaseConhecimentoCloudService().getDataCenterRatings().put(cloudServiceData.getDataCenterId(), cloudServiceData.getRating());
							break;
								default:
									//Rating
							
									break;
						}
						
						if(((BrokerAgent)getAgent()).getBaseConhecimentoCloudService().getDataCenterRatings().get(cloudServiceData.getDataCenterId()) > 
								CloudServiceStandards.DEFAULT_MAX_RATING_FOR_BLACKLIST){
							
							/** Deliberar se deve colocar na blacklist */
							//System.out.println(CloudSim.getEntity(idDataCenter).getName());
							if(!((BrokerAgent)getAgent()).getBaseConhecimentoCloudService().getDataCenterBlackList().contains(cloudServiceData.getDataCenterId())){
								((BrokerAgent)getAgent()).getBaseConhecimentoCloudService().getDataCenterBlackList().add(cloudServiceData.getDataCenterId()); 
							}
							
						}
					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/*if(Integer.valueOf(msg.getContent()) == MonitorSuporte.SEND_OVER_ALLOWED_OUTAGE){
						//Iniciar outra rodada de preços.
						//Map.Entry<Integer, CloudServiceData> entry = (Map.Entry<Integer, CloudServiceData>) msg.getContentObject();
						//Integer entry = (Integer) msg.getContentObject();
					}*/
				}
			}
		}
	}
	
	private void tratarInfosEnviadas(Map.Entry<Integer, CloudServiceData> entry) {
		if(((BrokerAgent)myAgent).getHistoricoCloudService().containsKey(entry.getKey())){
			CloudServiceData cloudServiceData = ((BrokerAgent)myAgent).getHistoricoCloudService().get(entry.getKey());
			
			printHistoricoCloudService(cloudServiceData);
		}else{
			((BrokerAgent)myAgent).getHistoricoCloudService().put(entry.getKey(), entry.getValue());
			
			printHistoricoCloudService(entry.getValue());
		}	
	}
		
	private void printHistoricoCloudService(CloudServiceData cloudServiceData) {
		if(cloudServiceData != null){
			System.out.println("Events performance: " + cloudServiceData.getQtdEventosPerformance());
			System.out.println("Events indisponibilidade: " + cloudServiceData.getQtdEventosIndisponibilidades());
		}
	}

}
