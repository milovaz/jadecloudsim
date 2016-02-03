package br.sma.jadecloudsim.ui;

public class LogOutput {

	private static LogOutput instance = null;
	
	private String data;
	
	protected LogOutput() {
		
	}
	
	public static LogOutput getInstance() {
		if(instance == null) {
			instance = new LogOutput();
			instance.data = new String();
		}
		
		return instance;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public void addData(String newData) {
		this.data = this.data + newData; 
	}
}
