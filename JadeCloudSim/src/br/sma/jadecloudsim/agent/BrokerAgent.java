package br.sma.jadecloudsim.agent;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import br.sma.jadecloudsim.behaviour.BrokerBehaviour;
import br.sma.jadecloudsim.behaviour.BrokerMonitorBehaviour;
import br.sma.jadecloudsim.cloud.CloudElements;
import br.sma.jadecloudsim.cloud.CustomDataCenterBroker;
import br.sma.jadecloudsim.main.Principal;
import br.sma.jadecloudsim.ui.PrincipalUI;
import br.sma.jadecloudsim.util.BaseConhecimentoCloudService;
import br.sma.jadecloudsim.util.BrokerUserNeeds;
import br.sma.jadecloudsim.util.CloudServiceData;
import br.sma.jadecloudsim.util.CloudServiceStandards;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;


/**
 * Classe que implementa o agente Broker extendendo as funcionalidades 
 * da classe Agent do JADE.
 * 
 * O BrokerAgent utiliza dois behaviours:
 * 
 * - BrokerBehaviour - para realizar a interação ContractNet com os DataCenterAgents
 * - BrokerMonitorBehaviour - para receber as notificações do CloudMonitorBehaviour
 * 
 * 
 * @author thiago
 *
 */
public class BrokerAgent extends Agent {


	private static final long serialVersionUID = -7204715239024088584L;

	
	private int dataCenterBrokerId;
	private BrokerUserNeeds brokerUserNeeds;
	private Map<Integer, CloudServiceData> historicoCloudService;
	private BaseConhecimentoCloudService baseConhecimentoCloudService;
	private CloudServiceStandards cloudServiceStandards;
	private Map<String, Integer> monitorToDataCenterMap;
	
	
	BrokerBehaviour brokerBehaviour;
	
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		
		System.out.println("\n Setting up broker agent \n");
		
		Object[] args = getArguments();
		if(args != null && args.length > 0){
			System.out.println("Argument received by Broker = " + args[0]);
			dataCenterBrokerId = (int) args[0];
		}
		
		
		/** Cria a CFP inicial **/
		ACLMessage msg = new ACLMessage(ACLMessage.CFP);
		
		AID[] searchResult = searchDF("datacenteragent");
		
		while(searchResult == null){
			try {
				Thread.sleep(2000);
				searchResult = searchDF("datacenteragent");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for(AID dataCenterAgent : searchResult) {
			msg.addReceiver(dataCenterAgent);
		}
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
		
		CustomDataCenterBroker customDataCenterBroker = (CustomDataCenterBroker) CloudSim.getEntity(this.getDataCenterBrokerId());
		List<Vm> vms = customDataCenterBroker.getVmList();
		String msgContent = "" + vms.get(0).getId();
		for(int i = 1; i < vms.size(); i++){
			msgContent = msgContent + "," + vms.get(i).getId();
		}
		msg.setContent(msgContent);
		
		System.out.println("\n Sending message \n");
		
		
		/** Inicializa as classe auxiliares **/
		setHistoricoCloudService(new HashMap<Integer, CloudServiceData>());
		setCloudServiceStandards(new CloudServiceStandards());
		setMonitorToDataCenterMap(new HashMap<String, Integer>());
		setBaseConhecimentoCloudService(new BaseConhecimentoCloudService());
		
		
		brokerBehaviour = new BrokerBehaviour(this, msg); 
		addBehaviour(brokerBehaviour);
		addBehaviour(new BrokerMonitorBehaviour(this));
	}

	public void enviarNovaCFP() {
		ACLMessage msg = new ACLMessage(ACLMessage.CFP);
		AID[] searchResult = searchDF("datacenteragent");
		
		while(searchResult == null){
			try {
				Thread.sleep(2000);
				searchResult = searchDF("datacenteragent");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for(AID dataCenterAgent : searchResult) {
			msg.addReceiver(dataCenterAgent);
		}
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		// We want to receive a reply in 10 secs
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
		
		//CustomDataCenterBroker customDataCenterBroker = (CustomDataCenterBroker) CloudSim.getEntity(this.getDataCenterBrokerId());
		//List<Vm> vms = customDataCenterBroker.getVmList();
		//List<Integer> vms = new ArrayList<>(CloudElements.getMapVmDatacenter().keySet());
		CustomDataCenterBroker customDataCenterBroker = (CustomDataCenterBroker) CloudSim.getEntity(this.getDataCenterBrokerId());
		List<Vm> vms = customDataCenterBroker.getVmList();
		String msgContent = "" + vms.get(0).getId();
		for(int i = 1; i < vms.size(); i++){
			msgContent = msgContent + "," + vms.get(i).getId();
		}
		msg.setContent(msgContent);
		
		Vector<ACLMessage> msgs = new Vector<>();
		msgs.add(msg);
		
		//this.removeBehaviour(brokerBehaviour);
		
		brokerBehaviour = new BrokerBehaviour(this, msg);
		this.addBehaviour(brokerBehaviour);
		
		System.out.println("\n Sending message \n");
	}
	
	/**
	 * Metodo que implementa o mecanismo de deliberação acerca da solicitação de mudança de DataCenter
	 * 
	 * @param idDataCenter
	 */
	public void deliberarMudancaDataCenter(Integer idDataCenter) {
		PrincipalUI.getInstance().addLog(this.getLocalName() + ": Analisando mudanca de datacenter");
		System.out.println(this.getLocalName() + ": Analisando mudanca de datacenter");
		/** Deliberar sobre mudança de datacenter **/
		if(historicoCloudService.containsKey(idDataCenter)){
			//historicoCloudService.get(entrada.getKey()).setQtdEventosIndisponibilidades(historicoCloudService.get(entrada.getKey()).getQtdEventosIndisponibilidades() + 1);
			
			//CloudSimulationSuporte.setupSimulacao();
			Principal.getInstance().reinitCloudSim();
			
			PrincipalUI.getInstance().addLog(this.getLocalName() + ": Iniciando uma nova rodada de prospostas");
			System.out.println(this.getLocalName() + ": Iniciando uma nova rodada de prospostas");
			
			CustomDataCenterBroker customDataCenterBroker = (CustomDataCenterBroker) CloudSim.getEntity(this.getDataCenterBrokerId());
			List<Vm> vms = customDataCenterBroker.getVmList();
			for(Vm vm : vms){
				CloudElements.removeLigacaoVmDatacenter(vm.getId());
			}
			
			try {
				this.getContainerController().getAgent("cloudMonitor" + idDataCenter).kill();
			} catch (ControllerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			enviarNovaCFP();
		}
	}
	
	/**
	 *  Método que realiza a busca por serviços registrados nas "YellowPages"
	 * 
	 * @param service
	 * @return array AID's encontrados
	 */
	protected AID [] searchDF( String service ) {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType( service );
		dfd.addServices(sd);
		
		SearchConstraints ALL = new SearchConstraints();
		ALL.setMaxResults(new Long(-1));
	
		try
		{
			DFAgentDescription[] result = DFService.search(this, dfd, ALL);
			if(result.length > 0) {
				AID[] agents = new AID[result.length];
				for (int i=0; i<result.length; i++) 
					agents[i] = result[i].getName() ;
				
				return agents;
			}
	
		}
	    catch (FIPAException fe) { fe.printStackTrace(); }
	    
	  	return null;
	}

	public BrokerUserNeeds getBrokerUserNeeds() {
		return brokerUserNeeds;
	}

	public void setBrokerUserNeeds(BrokerUserNeeds brokerUserNeeds) {
		this.brokerUserNeeds = brokerUserNeeds;
	}

	public int getDataCenterBrokerId() {
		return dataCenterBrokerId;
	}

	public void setDataCenterBrokerId(int dataCenterBrokerId) {
		this.dataCenterBrokerId = dataCenterBrokerId;
	}

	public Map<Integer, CloudServiceData> getHistoricoCloudService() {
		return historicoCloudService;
	}

	public void setHistoricoCloudService(
			Map<Integer, CloudServiceData> historicoCloudService) {
		this.historicoCloudService = historicoCloudService;
	}

	public BaseConhecimentoCloudService getBaseConhecimentoCloudService() {
		return baseConhecimentoCloudService;
	}

	public void setBaseConhecimentoCloudService(
			BaseConhecimentoCloudService baseConhecimentoCloudService) {
		this.baseConhecimentoCloudService = baseConhecimentoCloudService;
	}

	public CloudServiceStandards getCloudServiceStandards() {
		return cloudServiceStandards;
	}

	public void setCloudServiceStandards(CloudServiceStandards cloudServiceStandards) {
		this.cloudServiceStandards = cloudServiceStandards;
	}

	public Map<String, Integer> getMonitorToDataCenterMap() {
		return monitorToDataCenterMap;
	}

	public void setMonitorToDataCenterMap(
			Map<String, Integer> monitorToDataCenterMap) {
		this.monitorToDataCenterMap = monitorToDataCenterMap;
	}
	
}
