package rms.dominio;

import java.util.Date;

public class Analyze extends EntidadeDominio {
	private String msg;
	private Date dtInicial;
	private Date dtFinal;
	public String title;
	public String tooltip;
	public String legend;
	public String[] xAxis;
	public String[] yAxis;
	
	public class Series{
		public String name;
		public String type;
		public String[] data;
	}
	
	public Series series = new Series();

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// métodos de acesso
	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}
}
