package br.sma.jadecloudsim.behaviour;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map.Entry;

import br.sma.jadecloudsim.agent.CloudMonitorAgent;
import br.sma.jadecloudsim.cloud.CloudElements;
import br.sma.jadecloudsim.ui.PrincipalUI;
import br.sma.jadecloudsim.util.CloudServiceData;
import br.sma.jadecloudsim.util.CloudServiceStandards;
import br.sma.jadecloudsim.util.MonitorSuporte;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class CloudMonitorBehaviour extends TickerBehaviour{


	private static final long serialVersionUID = 3248563737784385617L;

	private MonitorSuporte monitorSuporte;
	
	
	MessageTemplate mt = MessageTemplate.and(
			MessageTemplate.MatchConversationId("cloudmonitor-info"),
			MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)
	);
	
	public CloudMonitorBehaviour(Agent a, long period) {
		super(a, period);
		monitorSuporte = new MonitorSuporte();
	}
	
	@Override
	protected void onTick() {
		
		int result = 0;
		if((result = monitorarCloudService()) > 0) {			
			ACLMessage infoMsg;
			try {
				infoMsg = criarMensagem(result);
				System.out.println("Enviando mensagem para BrokerAgent");
				myAgent.send(infoMsg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}

	private ACLMessage criarMensagem(int tipo) throws IOException {
		System.out.println("Criando mensagem de MonitorAgent");
		ACLMessage infoMsg = new ACLMessage(ACLMessage.INFORM);
		infoMsg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		infoMsg.setConversationId("cloudmonitor-info");
		infoMsg.addReceiver(new AID(((CloudMonitorAgent)getAgent()).getBrokerName(), AID.ISLOCALNAME));
		
		switch (tipo) {
			case MonitorSuporte.SEND_OVER_ALLOWED_OUTAGE:
				//infoMsg.setContent(String.valueOf(MonitorSuporte.SEND_OVER_ALLOWED_OUTAGE));
				((CloudMonitorAgent)getAgent()).getCloudServiceData().setTipoMensagem(MonitorSuporte.SEND_OVER_ALLOWED_OUTAGE);
				break;
	
			case MonitorSuporte.SEND_DATACENTER_RATING_UPDATE:
				//infoMsg.setContent(String.valueOf(MonitorSuporte.SEND_DATACENTER_RATING_UPDATE));
				((CloudMonitorAgent)getAgent()).getCloudServiceData().setTipoMensagem(MonitorSuporte.SEND_DATACENTER_RATING_UPDATE);
				break;	
			default:
				break;
		}
		
		infoMsg.setContentObject(((CloudMonitorAgent)getAgent()).getCloudServiceData());
		
		return infoMsg;
	}
	
	private boolean hasRelevantEvent() {
		if((Math.random() + Math.random()) * 10 > 5){
			return true;
		}
		
		return false;
	}
	
	private int monitorarCloudService() {
		Date now = new Date();
		
		long hoursOfOutage = monitorSuporte.countHoursOfOutage(now, ((CloudMonitorAgent)getAgent()).getCloudServiceData().getDataCenterId());
		((CloudMonitorAgent)getAgent()).getCloudServiceData().addHourOfOutage(hoursOfOutage, now.getMonth());
		
		//System.out.println(((CloudMonitorAgent)getAgent()).getCloudServiceData().getHoursOfOutagePerMonth()[now.getMonth()]);
		
		PrincipalUI.getInstance().alterarStatusTableDataCenter(((CloudMonitorAgent)getAgent()).getCloudServiceData().getDataCenterId(), "");
		
		if(((CloudMonitorAgent)getAgent()).getCloudServiceData().getHoursOfOutagePerMonth()[now.getMonth()] >=  
				((CloudMonitorAgent)getAgent()).getCloudServiceStandards().getAllowedHoursOfOutagePerMonth() || CloudElements.isFalhaIndisponilidadeTeste()){
			
			PrincipalUI.getInstance().addLog("Indisponibilidade excedeu limite mensal");
			CloudElements.setFalhaIndisponilidadeTeste(false);
			
			((CloudMonitorAgent)getAgent()).getCloudServiceData().resetHoursOfOutage(now.getMonth());
			
			((CloudMonitorAgent)getAgent()).getCloudServiceData().setQtdEventosIndisponibilidades(
					((CloudMonitorAgent)getAgent()).getCloudServiceData().getQtdEventosIndisponibilidades() + 1);
			
			if(((CloudMonitorAgent)getAgent()).getCloudServiceData().getQtdEventosIndisponibilidades() >= 
				((CloudMonitorAgent)getAgent()).getCloudServiceStandards().getAcceptableIndisEvents()){
				
				((CloudMonitorAgent)getAgent()).getCloudServiceData().getHoursOfOutagePerMonth()[now.getMonth()] = 0.0;
				((CloudMonitorAgent)getAgent()).getCloudServiceData().setQtdEventosIndisponibilidades(0);
				
				PrincipalUI.getInstance().addLog(myAgent.getLocalName() + ": Num. de eventos de indisponibilidade excedeu o limite aceitável");
				System.out.println(myAgent.getLocalName() + ": Num. de eventos de indisponibilidade excedeu o limite aceitável");
				
				PrincipalUI.getInstance().addLog(myAgent.getLocalName() + ": Enviando solicitacao de mudança de provedor");
				System.out.println(myAgent.getLocalName() + ": Enviando solicitacao de mudança de provedor");
				
				//Enviar mensagem de alerta para Broker
				return MonitorSuporte.SEND_OVER_ALLOWED_OUTAGE;
			}
			
			((CloudMonitorAgent)getAgent()).getCloudServiceData().setRating(((CloudMonitorAgent)getAgent()).getCloudServiceData().getRating() + 1);
			
			return MonitorSuporte.SEND_DATACENTER_RATING_UPDATE;
		}
		
		return 0;
	}

}
