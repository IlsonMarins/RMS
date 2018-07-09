
package rms.core.impl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rms.dominio.EntidadeDominio;
import rms.dominio.Indice;
import rms.dominio.Analyze;
import rms.dominio.AgendamentoReorg;
import rms.dominio.ExecucaoReorg;
import rms.dominio.Tabela;

public class AnalyzeDAO extends AbstractJdbcDAO {
	
	// construtor
	public AnalyzeDAO() {
		super("SOLICITACAO", "SOL_ID");
	}
	
	@SuppressWarnings("resource")
	public void salvar(EntidadeDominio entidade) {
		// inicialização dos objetos necessários para a alteração
		openConnection();
		PreparedStatement pst = null;
		//ResultSet rs = null;
		StringBuilder sql = null;
		ExecucaoReorg execucaoReorg = (ExecucaoReorg)entidade;
		List<AgendamentoReorg> solicitacoes = execucaoReorg.getSolicitacoes();
		//ArrayList<EntidadeDominio> edsRetorno = new ArrayList<EntidadeDominio>();
		try {
			// validar os índices
			for (AgendamentoReorg s : solicitacoes){ // varre as solicitações
				for (Tabela t : s.getTabelas()){ // varre as tabelas da solicitação
					for (Indice i : t.getIndices()){ // varre os índices da tabela
						pst = null;
						sql = new StringBuilder();
						sql.append("alter index ");
						sql.append(i.getOwnerIndice().getNomeOwner());
						sql.append(".");
						sql.append(i.getNomeIndice());
						sql.append(" rebuild");
						pst = connection.prepareStatement(sql.toString());
						pst.executeUpdate();
						//connection.commit();
					}
				}
			}
			// fazer a coleta de estatística
			for (int i = 0; i < solicitacoes.size(); i++){ // varrer as solicitacoes
				for (int j = 0; j < solicitacoes.get(i).getTabelas().size(); j++){ // varrer as tabelas da solicitacao
					// fazer a coleta de estatística das tabelas reorganizadas
					pst = null;
					sql = new StringBuilder();
					sql.append("ANALYZE TABLE ");
					sql.append(solicitacoes.get(i).getTabelas().get(j).getOwner().getNomeOwner());
					sql.append(".");
					sql.append(solicitacoes.get(i).getTabelas().get(j).getNomeTabela());
					sql.append(" COMPUTE STATISTICS");
					pst = connection.prepareStatement(sql.toString());
					pst.executeUpdate();
				} // fim-for_varrer_tabelas
				// atualizar o status após a reorganização e a coleta de estatística
				solicitacoes.get(i).setStatus("CONCLUIDO");
				pst = null;
				sql = new StringBuilder();
				sql.append("UPDATE SOLICITACAO SET SOL_DT_FIM_REORG = SYSTIMESTAMP, SOL_STATUS = ? WHERE SOL_ID = ?");
				pst = connection.prepareStatement(sql.toString());
				pst.setString(1, solicitacoes.get(i).getStatus());
				pst.setInt(2, solicitacoes.get(i).getId());
				pst.executeUpdate();
				connection.commit();
			} // fim-for_varrer_solicitacoes
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();			
		}finally{
			try {
				pst.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	} //fim-salvar()
	
	@Override
	public void alterar(EntidadeDominio entidade) {
		// inicialização dos objetos necessários para a alteração
		openConnection();
		PreparedStatement pst = null;
		//ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		ExecucaoReorg execucaoReorg = (ExecucaoReorg)entidade;
		List<AgendamentoReorg> solicitacoes = execucaoReorg.getSolicitacoes();
		//ArrayList<EntidadeDominio> edsRetorno = new ArrayList<EntidadeDominio>();
		try {
			/*
			 *  executar o reorg em cada uma das tabelas de cada uma das solicitacoes pendentes
			 */
			for (int i = 0; i < solicitacoes.size(); i++){ // varre todas as solicitações
				//if(!solicitacoes.get(i).getStatus().equals("EXECUTAR")) // solicitacao não está marcada para executar?
				//	continue;
				/*
				 * atualizar o status antes de iniciar a reorganização
				 */
				solicitacoes.get(i).setStatus("EXECUTANDO");
				pst = null;
				sql = new StringBuilder();
				sql.append("UPDATE SOLICITACAO SET SOL_DT_INICIO_REORG = SYSTIMESTAMP, SOL_STATUS = ? WHERE SOL_ID = ?");
				pst = connection.prepareStatement(sql.toString());
				pst.setString(1, solicitacoes.get(i).getStatus());
				pst.setInt(2, solicitacoes.get(i).getId());
				pst.executeUpdate();
				connection.commit();
				/*
				 * varrer todas as tabelas de cada solicitação
				 */
				for (int j = 0; j < solicitacoes.get(i).getTabelas().size(); j++){
					// fazer o reorg em cada tabela
					pst = null;
					sql = new StringBuilder();
					sql.append("alter table ");
					sql.append(solicitacoes.get(i).getTabelas().get(j).getOwner().getNomeOwner());
					sql.append(".");
					sql.append(solicitacoes.get(i).getTabelas().get(j).getNomeTabela());
					sql.append(" move tablespace ");
					sql.append(solicitacoes.get(i).getTabelas().get(j).getMetadado().getNomeTablespace());
					pst = connection.prepareStatement(sql.toString());
					System.out.print("\nDAO - Owner: " + solicitacoes.get(i).getTabelas().get(j).getOwner().getNomeOwner());
					System.out.print("\nDAO - Tabela: " + solicitacoes.get(i).getTabelas().get(j).getNomeTabela());
					System.out.print("\nDAO - Tablespace: " + solicitacoes.get(i).getTabelas().get(j).getMetadado().getNomeTablespace());
					pst.executeUpdate();
				}
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();			
		}finally{
			try {
				pst.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	} //fim-alterar()
	
	public List<EntidadeDominio> consultar(EntidadeDominio entidade){
		List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
		Analyze analyze = (Analyze)entidade;
		// consultar os reorgs de período específico?
		if (analyze.getDtInicial() != null
				&& analyze.getDtFinal() != null){
			java.sql.Date dtInicial = new java.sql.Date(analyze.getDtInicial().getTime());
			java.sql.Date dtFinal = new java.sql.Date(analyze.getDtFinal().getTime());
			openConnection();
			PreparedStatement pst = null;
			ResultSet rs = null;
			StringBuilder sql = new StringBuilder();
			List<String> datas = new ArrayList<String>();
			List<String> valores = new ArrayList<String>();
			try{
				connection.setAutoCommit(false);
				sql.append("select to_char(s.sol_dt_solicitacao, 'dd/mm') datas, count(s.sol_id) valores "
						+ "from solicitacao s "
						+ "where s.sol_dt_solicitacao between ? and  ? + 1 "
						+ "and s.sol_status = 'CONCLUIDO' "
						+ "group by to_char(s.sol_dt_solicitacao, 'dd/mm') "
						+ "order by to_date(datas, 'dd/mm')");
				pst = connection.prepareStatement(sql.toString());
				pst.setDate(1, dtInicial);
				pst.setDate(2, dtFinal);
				rs = pst.executeQuery();
				//Date dataAtual = new Date();
				//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
				//System.out.print("\nDAO - Data Atual: " + dataAtual); // Resultado: Thu May 04 10:43:27 BRT 2017
				//System.out.print("\nDAO - Data Atual formatada: " + sdf.format(dataAtual)); // Resultado: 04/05/17 10:43
				while (rs.next()) {
					datas.add(rs.getString(1));
					valores.add(rs.getString(2));
				}
			}catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();			
			}finally{
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			String[] xCliente = datas.toArray(new String[datas.size()]);
			String[] series = valores.toArray(new String[valores.size()]);
			analyze.title = "REORGS POR PERÍODO";
			analyze.legend = "REORGS POR PERÍODO";
			analyze.xAxis = xCliente;
			analyze.series.name = "Total Geral";
			analyze.series.type = "line";
			analyze.series.data = series;
			entidades.add(analyze);
			
			// consultar os reorgs por cliente desse período
			pst = null;
			rs = null;
			sql = new StringBuilder();
			List<String> chaves = new ArrayList<String>();
			valores = new ArrayList<String>();
			try{
				connection.setAutoCommit(false);
				sql.append("select count(distinct s.sol_id) valores, c.cli_nome_cliente chaves "
						+ "from solicitacao s, solicitacao_tabela m, tabela t, owner o, banco b, cliente c "
						+ "where s.sol_dt_solicitacao between ? and ? + 1 "
						+ "and s.sol_id=m.stb_sol_id "
						+ "and m.stb_tab_id=t.tab_id and t.tab_own_id=o.own_id "
						+ "and o.own_ban_id=b.ban_id and b.ban_cli_id=c.cli_id "
						+ "and s.sol_status = 'CONCLUIDO' "
						+ "group by c.cli_nome_cliente "
						+ "order by valores, chaves");
				pst = connection.prepareStatement(sql.toString());
				pst.setDate(1, (java.sql.Date)dtInicial);
				pst.setDate(2, (java.sql.Date)dtFinal);
				rs = pst.executeQuery();
				//Date dataAtual = new Date();
				//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
				//System.out.print("\nDAO - Data Atual: " + dataAtual); // Resultado: Thu May 04 10:43:27 BRT 2017
				//System.out.print("\nDAO - Data Atual formatada: " + sdf.format(dataAtual)); // Resultado: 04/05/17 10:43
				while (rs.next()) {
					chaves.add(rs.getString("chaves"));
					valores.add(rs.getString("valores"));
				}
			}catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();			
			}finally{
				try {
					pst.close();
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			analyze = new Analyze();
			xCliente = chaves.toArray(new String[chaves.size()]);
			series = valores.toArray(new String[valores.size()]);
			analyze.title = "REORGS POR CLIENTE NO PERÍODO";
			analyze.legend = "REORGS POR CLIENTE NO PERÍODO";
			analyze.xAxis = xCliente;
			analyze.series.name = "Total Geral";
			analyze.series.type = "bar";
			analyze.series.data = series;
			entidades.add(analyze);
		}else{ // consultar os reorgs dos últimos 30 dias
			entidades.add(getReorgsMes());
			entidades.add(getReorgsMesPorCliente());
		}
		return entidades;
	} //fim-consultar
	
	public Analyze getReorgsMesPorCliente(){
		// inicialização dos objetos necessários para a visualização
		openConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		List<String> chaves = new ArrayList<String>();
		List<String> valores = new ArrayList<String>();
		try{
			connection.setAutoCommit(false);
			sql.append("select count(distinct s.sol_id) valores, c.cli_nome_cliente chaves "
					+ "from solicitacao s, solicitacao_tabela m, tabela t, owner o, banco b, cliente c "
					+ "where s.sol_dt_solicitacao between sysdate - 30 and sysdate and s.sol_id=m.stb_sol_id "
					+ "and m.stb_tab_id=t.tab_id and t.tab_own_id=o.own_id "
					+ "and o.own_ban_id=b.ban_id and b.ban_cli_id=c.cli_id "
					+ "and s.sol_status = 'CONCLUIDO'"
					+ "group by c.cli_nome_cliente "
					+ "order by valores, chaves");
			pst = connection.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			//Date dataAtual = new Date();
			//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
			//System.out.print("\nDAO - Data Atual: " + dataAtual); // Resultado: Thu May 04 10:43:27 BRT 2017
			//System.out.print("\nDAO - Data Atual formatada: " + sdf.format(dataAtual)); // Resultado: 04/05/17 10:43
			while (rs.next()) {
				chaves.add(rs.getString("chaves"));
				valores.add(rs.getString("valores"));
			}
		}catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();			
		}finally{
			try {
				pst.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Analyze analyze = new Analyze();
		String[] xCliente = chaves.toArray(new String[chaves.size()]);
		String[] series = valores.toArray(new String[valores.size()]);
		analyze.title = "REORGS POR CLIENTE NO PERÍODO (ÚLTIMOS 30 DIAS)";
		analyze.legend = "REORGS POR CLIENTE NO PERÍODO (ÚLTIMOS 30 DIAS)";
		analyze.xAxis = xCliente;
		analyze.series.name = "Total Geral";
		analyze.series.type = "bar";
		analyze.series.data = series;
		return analyze;
	} // fim-getReorgsMesPorCliente()
	
	public Analyze getReorgsMes(){
		// inicialização dos objetos necessários para a visualização
		openConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		List<String> datas = new ArrayList<String>();
		List<String> valores = new ArrayList<String>();
		Analyze analyze = new Analyze();
		try{
			connection.setAutoCommit(false);
			sql.append("select to_char(s.sol_dt_solicitacao, 'dd/mm') datas, count(s.sol_id) valores "
					+ "from solicitacao s "
					+ "where s.sol_dt_solicitacao between sysdate - 30 and sysdate "
					+ "and s.sol_status = 'CONCLUIDO'"
					+ "group by to_char(s.sol_dt_solicitacao, 'dd/mm') "
					+ "order by to_date(datas, 'dd/mm')");
			pst = connection.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			//Date dataAtual = new Date();
			//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
			//System.out.print("\nDAO - Data Atual: " + dataAtual); // Resultado: Thu May 04 10:43:27 BRT 2017
			//System.out.print("\nDAO - Data Atual formatada: " + sdf.format(dataAtual)); // Resultado: 04/05/17 10:43
			while (rs.next()) {
				datas.add(rs.getString(1));
				valores.add(rs.getString(2));
			}
		}catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();			
		}finally{
			try {
				pst.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		String[] xCliente = datas.toArray(new String[datas.size()]);
		String[] series = valores.toArray(new String[valores.size()]);
		analyze.title = "REORGS POR PERÍODO (ÚLTIMOS 30 DIAS)";
		analyze.legend = "REORGS POR PERÍODO (ÚLTIMOS 30 DIAS)";
		analyze.xAxis = xCliente;
		analyze.series.name = "Total Geral";
		analyze.series.type = "line";
		analyze.series.data = series;
		return analyze;
	} // fim-getReorgsMes()
	
	@Override
	public List<EntidadeDominio> visualizar(EntidadeDominio entidade) {
		return null;
	} //fim-consultar()

} //fim-classe
