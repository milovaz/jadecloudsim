package br.sma.jadecloudsim.util;

import java.io.Serializable;

public class CloudServiceData implements Serializable{

	
	private static final long serialVersionUID = 1752991860037207912L;

	
	private int qtdEventosPerformance;
	private int qtdEventosIndisponibilidades;
	private Double [] hoursOfOutagePerMonth;
	private double rating;
	private int dataCenterId;
	private int tipoMensagem;
	
	
	public CloudServiceData() {
		this.qtdEventosPerformance = 0;
		this.qtdEventosIndisponibilidades = 0;
		this.hoursOfOutagePerMonth = new Double[12];
	}
	
	public int getQtdEventosPerformance() {
		return qtdEventosPerformance;
	}
	public void setQtdEventosPerformance(int qtdEventosPerformance) {
		this.qtdEventosPerformance = qtdEventosPerformance;
	}
	public int getQtdEventosIndisponibilidades() {
		return qtdEventosIndisponibilidades;
	}
	public void setQtdEventosIndisponibilidades(int qtdEventosIndisponibilidades) {
		this.qtdEventosIndisponibilidades = qtdEventosIndisponibilidades;
	}
	public Double[] getHoursOfOutagePerMonth() {
		return hoursOfOutagePerMonth;
	}
	public void setHoursOfOutagePerMonth(Double[] hoursOfOutagePerMonth) {
		this.hoursOfOutagePerMonth = hoursOfOutagePerMonth;
	}
	public void addHourOfOutage(double hours, int month) {
		if(this.hoursOfOutagePerMonth[month] == null){
			this.hoursOfOutagePerMonth[month] = hours;
		}else{
			this.hoursOfOutagePerMonth[month] = this.hoursOfOutagePerMonth[month] + hours;
		}
	}
	public void resetHoursOfOutage(int month) {
		if(this.hoursOfOutagePerMonth[month] != null){
			this.hoursOfOutagePerMonth[month] = 0.0;
		}
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getDataCenterId() {
		return dataCenterId;
	}

	public void setDataCenterId(int dataCenterId) {
		this.dataCenterId = dataCenterId;
	}

	public int getTipoMensagem() {
		return tipoMensagem;
	}

	public void setTipoMensagem(int tipoMensagem) {
		this.tipoMensagem = tipoMensagem;
	}
	
}
