package rms.core.impl.negocio;

import java.util.List;

import rms.core.IStrategy;
import rms.core.impl.dao.ReorgDAO;
import rms.dominio.EntidadeDominio;
import rms.dominio.ExecucaoReorg;
import rms.dominio.AgendamentoReorg;

public class ComplSolicitacoesPendentes implements IStrategy {
	@Override
	public String processar(EntidadeDominio entidade) {
		ReorgDAO reorgDAO = new ReorgDAO();
		List<? extends EntidadeDominio> solicitacoes = null;
		if (entidade != null){
			ExecucaoReorg execucaoReorg = (ExecucaoReorg)entidade;
			solicitacoes = (List<? extends EntidadeDominio>) reorgDAO.consultar(entidade);
			if (solicitacoes != null){
				execucaoReorg.setSolicitacoes((List<AgendamentoReorg>) solicitacoes);
			}else{
				return "\ncSolPendentes - Nenhum Reorg encontrado para execução no momento!\n";
			}
		}else{
			return "Entidade: " + entidade.getClass().getCanonicalName() + " nula!";
		}
		
		return null;
		
	} // fim-processar
	
} // fim-classe
