package rms.core.impl.negocio;

import java.util.List;

import rms.core.IStrategy;
import rms.core.impl.dao.ReorgDAO;
import rms.dominio.EntidadeDominio;
import rms.dominio.ExecucaoReorg;
import rms.dominio.AgendamentoReorg;

public class ExecutarReorg implements IStrategy {
	@Override
	public String processar(EntidadeDominio entidade) {
		ReorgDAO reorgDAO = new ReorgDAO();
		if (entidade != null){
			ExecucaoReorg execucaoReorg = (ExecucaoReorg)entidade;
			List<AgendamentoReorg> solicitacoes = execucaoReorg.getSolicitacoes();
			if (solicitacoes != null && solicitacoes.size() > 0){
				reorgDAO.alterar(execucaoReorg);
			}else{
				return "\nexeReorg - Nenhum Reorg encontrado para execução no momento!\n";
			}
		}else{
			return "Entidade: " + entidade.getClass().getCanonicalName() + " nula!";
		}
		
		return null;
		
	} // fim-processar
	
} // fim-classe
