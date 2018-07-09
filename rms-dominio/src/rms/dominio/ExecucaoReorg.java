package rms.dominio;

import java.util.ArrayList;
import java.util.List;

public class ExecucaoReorg extends EntidadeDominio{
	
	// atributos privados
	private List<AgendamentoReorg> solicitacoes = new ArrayList<AgendamentoReorg>();
	
	// métodos de acesso
	public List<AgendamentoReorg> getSolicitacoes() {
		return solicitacoes;
	}

	public void setSolicitacoes(List<AgendamentoReorg> solicitacoes) {
		this.solicitacoes = solicitacoes;
	}
	
} // fim-classe
