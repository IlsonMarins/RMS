
package rms.core.impl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import rms.dominio.*;

public class SolicitacaoDAO extends AbstractJdbcDAO {
	
	// construtor
	public SolicitacaoDAO() {
		super("SOLICITACAO", "SOL_ID");
	}
	
	@Override
	public void salvar(EntidadeDominio entidade) {
		openConnection();
		PreparedStatement pst = null;
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		try {
			connection.setAutoCommit(false);
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO SOLICITACAO (SOL_ID, SOL_DT_AGENDADA, SOL_DT_SOLICITACAO, SOL_STATUS) ");
			sql.append("VALUES (?, ?, ?, ?)");
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, agendamentoReorg.getId());
			Timestamp dtAgendada = new Timestamp(agendamentoReorg.getDtAgendamento().getTime());
			pst.setTimestamp(2, dtAgendada);
			Timestamp dtSolicitacao = new Timestamp(agendamentoReorg.getDtCadastro().getTime());
			pst.setTimestamp(3, dtSolicitacao);
			pst.setString(4, agendamentoReorg.getStatus());
			pst.executeUpdate();
			sql = new StringBuilder();
			sql.append("INSERT INTO SOLICITACAO_TABELA (STB_SOL_ID, STB_TAB_ID, ");
			sql.append("STB_PARALLEL, STB_INI_TRANS, STB_COMPRESS_TABLE, STB_CRITERIO_COLUNA, ");
		    sql.append("STB_PCT_ESTATISTICA, STB_TABLESPACE_DADOS, STB_TAMANHO_TABELA) ");
			sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pst.close();
			pst = null;
			pst = connection.prepareStatement(sql.toString());
			int cont = 1;
			pst.setInt(cont++, agendamentoReorg.getId());
			// varre todas as tabelas
			for (int i = 0; i < agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().size(); i++){
				pst.setInt(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getId());
				pst.setString(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getMetadado().getParallel());
				pst.setDouble(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getMetadado().getIniTrans());
				pst.setString(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getMetadado().getCompressTable());
				pst.setString(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getMetadado().getCriterioColuna());
				pst.setDouble(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getMetadado().getPctEstatistica());
				pst.setString(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getMetadado().getNomeTablespace());
				pst.setDouble(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getMetadado().getTamanho());
				pst.executeUpdate();
				cont = 2;
			}
			sql = new StringBuilder();
			sql.append("INSERT INTO INDICE (IND_TAB_ID, IND_ID, IND_OWNER_INDICE, IND_NOME_INDICE, "
					+ "IND_COMPRESSION, IND_NOME_TABLESPACE, IND_INI_TRANS, IND_LOGGING, "
					+ "IND_STATUS, IND_LAST_ANALYZED, IND_PARALLEL, IND_PARTITIONED) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pst = null;
			pst = connection.prepareStatement(sql.toString());
			cont = 1;
			// varre cada tabela
			for (Tabela tabela : agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas()){
				for (Indice indice : tabela.getIndices()){ // varre todos os índices da tabela
					pst.setInt(cont++, tabela.getId());
					pst.setInt(cont++, indice.getId());
					pst.setString(cont++, indice.getOwnerIndice().getNomeOwner());
					pst.setString(cont++, indice.getNomeIndice());
					pst.setString(cont++, indice.getCompressionIndice());
					pst.setString(cont++, indice.getTablespaceIndice());
					pst.setDouble(cont++, indice.getIniTransIndice());
					pst.setString(cont++, indice.getLoggingIndice());
					pst.setString(cont++, indice.getStatusIndice());
					pst.setDate(cont++, (java.sql.Date)indice.getLastAnalyzedIndice());
					pst.setString(cont++, indice.getParallelIndice());
					pst.setString(cont++, indice.getPartitionedIndice());
					pst.executeUpdate();
					cont = 1;
				}
			}
		}catch (SQLException e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();			
		}finally{
			try {
				connection.commit();
				pst.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	} //fim-salvar()
	
	@Override
	public void alterar(EntidadeDominio entidade) {
		openConnection();
		PreparedStatement pst = null;
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		StringBuilder sql = new StringBuilder();
		Integer solId = agendamentoReorg.getId();
		String solStatus = agendamentoReorg.getStatus();
		String entBusca = agendamentoReorg.getEntidadeBusca();
		// é alteração de fato?
		if (solId != null && solStatus != null){
			// é cancelamento ou travamento de solicitação pendente?
			if (solStatus.equals("CANCELADO")
					|| solStatus.equals("TRAVADO")
					|| entBusca.equals("VOLTAR")){
				try {
					connection.setAutoCommit(false);
					sql.append("update solicitacao "
							+ "set sol_status = ? "
							+ "where sol_id = ?");		
					pst = connection.prepareStatement(sql.toString());
					pst.setString(1, agendamentoReorg.getStatus());
					pst.setInt(2, agendamentoReorg.getId());
					pst.executeUpdate();
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
				return;
			}
			// alterar toda a solicitacao
			else{
				try {
					connection.setAutoCommit(false);
					sql.append("update solicitacao "
							+ "set sol_dt_agendada = ?, sol_dt_solicitacao = ?, sol_status = ? "
							+ "where sol_id = ?");		
					pst = connection.prepareStatement(sql.toString());
					Timestamp dtAgendamento = new Timestamp(agendamentoReorg.getDtAgendamento().getTime());
					pst.setTimestamp(1, dtAgendamento);
					Timestamp dtCadastro = new Timestamp(agendamentoReorg.getDtCadastro().getTime());
					pst.setTimestamp(2, dtCadastro);
					pst.setString(3, agendamentoReorg.getStatus());
					pst.setInt(4, agendamentoReorg.getId());
					pst.executeUpdate();
					pst.close();
					// atualizar as tabelas de metadados deletando os dados antigos e reinserindo-os
					sql = new StringBuilder();
					sql.append("delete from solicitacao_tabela "
							+ "where stb_sol_id = ?");
					pst = null;
					pst = connection.prepareStatement(sql.toString());
					pst.setInt(1, agendamentoReorg.getId());
					pst.executeUpdate();
					sql = new StringBuilder();
					sql.append("insert into solicitacao_tabela (stb_sol_id, stb_tab_id, ");
					sql.append("stb_parallel, stb_ini_trans, stb_compress_table, stb_criterio_coluna, ");
				    sql.append("stb_pct_estatistica, stb_tablespace_dados, stb_tamanho_tabela) ");
					sql.append("values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
					pst.close();
					pst = null;
					pst = connection.prepareStatement(sql.toString());
					Integer cont = 1;
					pst.setInt(cont++, agendamentoReorg.getId());
					// varre cada tabela
					for (int i = 0; i < agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().size(); i++){
						pst.setInt(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getId());
						pst.setString(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getMetadado().getParallel());
						pst.setDouble(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getMetadado().getIniTrans());
						pst.setString(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getMetadado().getCompressTable());
						pst.setString(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getMetadado().getCriterioColuna());
						pst.setDouble(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getMetadado().getPctEstatistica());
						pst.setString(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getMetadado().getNomeTablespace());
						pst.setDouble(cont++, agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas().get(i).getMetadado().getTamanho());
						pst.executeUpdate();
						cont = 2;
					}
					sql = new StringBuilder();
					sql.append("delete from indice "
							+ "where ind_tab_id = ?");
					pst.close();
					pst = null;
					pst = connection.prepareStatement(sql.toString());
					// varre cada tabela
					cont = 1;
					for (Tabela tabela : agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas()){
						pst.setInt(cont++, tabela.getId());
						pst.executeUpdate();
						cont = 1;
					}
					sql = new StringBuilder();
					sql.append("insert into indice (ind_tab_id, ind_id, ind_owner_indice, ind_nome_indice, "
							+ "ind_compression, ind_nome_tablespace, ind_ini_trans, ind_logging, "
							+ "ind_status, ind_last_analyzed, ind_parallel, ind_partitioned) "
							+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					pst.close();
					pst = null;
					pst = connection.prepareStatement(sql.toString());
					cont = 1;
					// varre cada tabela
					for (Tabela tabela : agendamentoReorg.getCliente().getBancos().get(0).getOwners().get(0).getTabelas()){
						// varre cada índice da tabela
						for (Indice indice : tabela.getIndices()){
							pst.setInt(cont++, tabela.getId());
							pst.setInt(cont++, indice.getId());
							pst.setString(cont++, indice.getOwnerIndice().getNomeOwner());
							pst.setString(cont++, indice.getNomeIndice());
							pst.setString(cont++, indice.getCompressionIndice());
							pst.setString(cont++, indice.getTablespaceIndice());
							pst.setDouble(cont++, indice.getIniTransIndice());
							pst.setString(cont++, indice.getLoggingIndice());
							pst.setString(cont++, indice.getStatusIndice());
							pst.setDate(cont++, (java.sql.Date)indice.getLastAnalyzedIndice());
							pst.setString(cont++, indice.getParallelIndice());
							pst.setString(cont++, indice.getPartitionedIndice());
							pst.executeUpdate();
							cont = 1;
						}
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
				return;
			}
		} // fim-alteração-de-fato
		return;
	} // fim-alterar()
	
	public List<EntidadeDominio> consultar(EntidadeDominio entidade){
		// entidade recebida
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		// entidade de retorno
		List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
		// buscar um novo ID para a solicitação?
		if (agendamentoReorg.getId() == null && agendamentoReorg.getEntidadeBusca().equals("CARREGAR")){
			// desmembrar em um método separado a fim de ganhar legibilidade no Consultar:
			consultarProximoIdNovaSolicitacao(agendamentoReorg); // retornará por referência
			entidades.add(agendamentoReorg);
			return entidades;
		}
		// buscar as informações de uma solicitação específica?
		else if ((agendamentoReorg.getId() != null
				&& agendamentoReorg.getEntidadeBusca() != null)
				&& (agendamentoReorg.getEntidadeBusca().equals("VISUALIZAR")
						|| agendamentoReorg.getEntidadeBusca().equals("DETALHES")
						|| agendamentoReorg.getEntidadeBusca().equals("CANCELAR"))){
			openConnection();
			PreparedStatement pst = null;
			try {
				connection.setAutoCommit(false);
				StringBuilder sql = new StringBuilder();
				Integer solId = agendamentoReorg.getId();
				if (solId != null){
					sql.append("select s.sol_id, s.sol_dt_agendada, "
							+ "s.sol_dt_solicitacao, s.sol_status, "
							+ "s.sol_dt_inicio_reorg, s.sol_dt_fim_reorg "
							+ "from solicitacao s "
							+ "where SOL_ID = ?");
					pst = connection.prepareStatement(sql.toString());
					pst.setInt(1, solId);
					ResultSet rs = pst.executeQuery();
					if (rs.next()) {
						agendamentoReorg.setId(rs.getInt("sol_id"));
						agendamentoReorg.setDtAgendamento(rs.getTimestamp("sol_dt_agendada"));
						agendamentoReorg.setDtCadastro(rs.getTimestamp("sol_dt_solicitacao"));
						agendamentoReorg.setStatus(rs.getString("sol_status"));
						agendamentoReorg.setDtInicioReorg(rs.getTimestamp("sol_dt_inicio_reorg"));
						agendamentoReorg.setDtFimReorg(rs.getTimestamp("sol_dt_fim_reorg"));
						entidades.add(agendamentoReorg);
					}
					rs.close();
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
					return entidades;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		// buscar todas as tabelas de uma solicitação com seus respectivos metadados?
		/*else if (solicitacao.getEntidadeBusca() != null && solicitacao.getEntidadeBusca().equals("DETALHES")){
			// buscar a(s) tabela(s) da solicitação, incluindo
			// seu respectivo cliente, banco e owner
			consultarTabelasSolicitacao(solicitacao);
			// buscar o(s) índice(s) de cada tabela
			for (Tabela t : solicitacao.getBanco().getOwners().get(0).getTabelas()){
				consultarIndicesTabela(t);
			}
			// buscar os metadados de cada tabela
			for (Tabela t : solicitacao.getBanco().getOwners().get(0).getTabelas()){
				consultarMetadadosTabela(t);
			}
			entidades.add(solicitacao);
			return entidades;
		}*/
		// verificar se é para preencher as combos inicialmente
//		if (solicitacao.getEntidadeBusca() == null){ // preencher todas as combos?
//			consultarClientes(solicitacao); // retorno por referência
//			entidades.add(solicitacao);
			//solicitacao = consultarBancos(solicitacao); // função desativada pois se deve carregar somente bancos específicos
			//solicitacao = consultarOwners(solicitacao); // função desativada pois se deve carregar somente owners específicos
			//solicitacao = consultarTabelas(solicitacao); // função desativada pois se deve carregar somente tabelas específicas
//		}else{ // preencher combo específica
//			if (solicitacao.getEntidadeBusca().equals("BANCO")){ // preencher_combo_de_banco
//				solicitacao = consultarBancos(solicitacao);
//				entidades.add(solicitacao);
//			}else if (solicitacao.getEntidadeBusca().equals("OWNER")){ // preencher_combo_de_owner
//				solicitacao = consultarOwners(solicitacao);
//				entidades.add(solicitacao);
//			}else if (solicitacao.getEntidadeBusca().equals("TABELA")){ // preencher_combo_de_tabela
//				solicitacao = consultarTabelas(solicitacao);
//				entidades.add(solicitacao);
//			}else if (solicitacao.getEntidadeBusca().equals("METADADO")){ // incluir_linha_na_tabela_de_metadado
//				//solicitacao = consultarMetadadosTabela(solicitacao);
//				entidades.add(solicitacao);
//			}
//		}
		entidades.add(agendamentoReorg);
		return entidades;
	} //fim-consultar

	@Override
	public List<EntidadeDominio> visualizar(EntidadeDominio entidade) {
		openConnection();
		PreparedStatement pst = null;
		AgendamentoReorg agendamentoReorg = (AgendamentoReorg)entidade;
		List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
		try {
			connection.setAutoCommit(false);
			StringBuilder sql = new StringBuilder();
			Integer solId = agendamentoReorg.getId();
			if (solId != null){
				sql.append("select * "
						+ "from solicitacao "
						+ "where SOL_ID = ?");
				pst = connection.prepareStatement(sql.toString());
				pst.setInt(1, solId);
				ResultSet rs = pst.executeQuery();
				Integer cont = 1;
				if (rs.next()) {
					agendamentoReorg.setId(rs.getInt(cont++));
					agendamentoReorg.setDtAgendamento(rs.getTimestamp(cont++));
					agendamentoReorg.setDtCadastro(rs.getTimestamp(cont++));
					agendamentoReorg.setStatus(rs.getString(cont++));
					agendamentoReorg.setDtInicioReorg(rs.getTimestamp(cont++));
					agendamentoReorg.setDtFimReorg(rs.getTimestamp(cont++));
					cont = 1;
					entidades.add(agendamentoReorg);
				}
				rs.close();
			}else{
				sql.append("select * "
						+ "from solicitacao "
						+ "order by 1 desc");
				pst = connection.prepareStatement(sql.toString());
				ResultSet rs = pst.executeQuery();
				Integer cont = 1;
				while (rs.next()) {
					agendamentoReorg = new AgendamentoReorg();
					agendamentoReorg.setId(rs.getInt(cont++));
					agendamentoReorg.setDtAgendamento(rs.getTimestamp(cont++));
					agendamentoReorg.setDtCadastro(rs.getTimestamp(cont++));
					agendamentoReorg.setStatus(rs.getString(cont++));
					agendamentoReorg.setDtInicioReorg(rs.getTimestamp(cont++));
					agendamentoReorg.setDtFimReorg(rs.getTimestamp(cont++));
					cont = 1;
					entidades.add(agendamentoReorg);
				}
				rs.close();
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
		return entidades;
	} // fim-visualizar()
	
	private void consultarProximoIdNovaSolicitacao(AgendamentoReorg agendamentoReorg) {
		if (agendamentoReorg.getEntidadeBusca() != null && agendamentoReorg.getEntidadeBusca().equals("METADADO"))
			return;
		openConnection();
		PreparedStatement pst = null;
		try {
			connection.setAutoCommit(false);
			StringBuilder sql = new StringBuilder();
			sql.append("select max(sol_id) "
					+ "from solicitacao");
			pst = connection.prepareStatement(sql.toString());
			ResultSet rs = pst.executeQuery();
			String strId = "";
			if (rs.next()) {
				strId = strId + rs.getInt(1);
			}
			agendamentoReorg.setId(Integer.parseInt(strId) + 1);
			rs.close();
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
		return;
	} //fim-gerarProximoId()
	
//	private AgendamentoReorg consultarClientes(AgendamentoReorg solicitacao) {
//		openConnection();
//		PreparedStatement pst = null;
//		try {
//			connection.setAutoCommit(false);
//			
//			// preencher o objeto dos clientes
//			StringBuilder sql = new StringBuilder();
//			sql.append("select CLI_ID, CLI_NOME_CLIENTE from cliente order by 2");
//					
//			pst = connection.prepareStatement(sql.toString());
//			
//			ResultSet rs = pst.executeQuery();
//			
//			ArrayList<Cliente> clientes = new ArrayList<Cliente>();
//			while (rs.next()) {
//				Cliente c = new Cliente();
//				c.setId(rs.getInt(1));
//				c.setNomeCliente(rs.getString(2));
//				clientes.add(c);
//			}
//			
//			solicitacao.setClientes(clientes);
//			
//			rs.close();	
//		} catch (SQLException e) {
//			try {
//				connection.rollback();
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//			e.printStackTrace();			
//		}finally{
//			try {
//				pst.close();
//				connection.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return solicitacao;
//	} //fim-consultarClientes()
//	
//	private AgendamentoReorg consultarBancos(AgendamentoReorg solicitacao) {
//		openConnection();
//		PreparedStatement pst = null;
//		if (solicitacao.getCliente() != null){ //buscar_banco_especifico?
//			Cliente c = solicitacao.getCliente();
//			Integer id = c.getId();
//			ArrayList<Banco> bcs = new ArrayList<Banco>();
//			try {
//				connection.setAutoCommit(false);
//				// preencher o objeto dos bancos
//				StringBuilder sql = new StringBuilder();
//				sql.append("SELECT b.ban_id, b.ban_nome_banco FROM banco b, cliente c WHERE b.ban_cli_id = c.cli_id AND c.cli_id = ");
//				sql.append(id);
//			    sql.append(" ORDER BY 2");
//				
//				pst = connection.prepareStatement(sql.toString());
//				
//				ResultSet rs = pst.executeQuery();
//				
//				while (rs.next()) {
//					Banco b = new Banco();
//					b.setId(rs.getInt(1));
//					b.setNomeBanco(rs.getString(2));
//					bcs.add(b);
//				}
//				
//				solicitacao.setBancos(bcs);
//				
//				rs.close();	
//			} catch (SQLException e) {
//				try {
//					connection.rollback();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
//				e.printStackTrace();			
//			}finally{
//				try {
//					pst.close();
//					connection.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		} //fim-buscar_banco_especifico?
//		else{ //buscar_todos_os_bancos
//			try {
//				connection.setAutoCommit(false);
//				
//				// preencher o objeto dos bancos
//				StringBuilder sql = new StringBuilder();
//				sql.append("select BAN_CLI_ID, BAN_ID, BAN_NOME_BANCO from banco order by 3");
//						
//				pst = connection.prepareStatement(sql.toString());
//				
//				ResultSet rs = pst.executeQuery();
//				
//				ArrayList<Banco> bancos = new ArrayList<Banco>();
//				Cliente c = new Cliente();
//				while (rs.next()) {
//					Banco b = new Banco();
//					if (c.getId() == null || c.getId() != rs.getInt(1)){
//						c = new Cliente();
//						c.setId(rs.getInt(1));
//					}
//					b.setCliente(c);
//					b.setId(rs.getInt(2));
//					b.setNomeBanco(rs.getString(3));
//					bancos.add(b);
//				}
//				
//				solicitacao.setBancos(bancos);
//				
//				rs.close();	
//			} catch (SQLException e) {
//				try {
//					connection.rollback();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
//				e.printStackTrace();			
//			}finally{
//				try {
//					pst.close();
//					connection.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		} //fim-buscar_todos_os_bancos
//		return solicitacao;
//	} //fim-consultarBancos()
//	
//	private AgendamentoReorg consultarOwners(AgendamentoReorg solicitacao){
//		openConnection();
//		PreparedStatement pst = null;
//		if (solicitacao.getBanco() != null){ //buscar_owner_especifico?
//			Banco b = solicitacao.getBanco();
//			Integer id = b.getId();
//			ArrayList<Owner> ows = new ArrayList<Owner>();
//			try {
//				connection.setAutoCommit(false);
//				// preencher o objeto dos owners
//				StringBuilder sql = new StringBuilder();
//				sql.append("SELECT o.own_id, o.own_nome_owner FROM banco b, owner o WHERE o.own_ban_id = b.ban_id AND b.ban_id = ");
//				sql.append(id);
//			    sql.append(" ORDER BY 2");
//				
//				pst = connection.prepareStatement(sql.toString());
//				
//				ResultSet rs = pst.executeQuery();
//				
//				while (rs.next()) {
//					Owner o = new Owner();
//					o.setId(rs.getInt(1));
//					o.setNomeOwner(rs.getString(2));
//					ows.add(o);
//				}
//				
//				solicitacao.setOwners(ows);
//				
//				rs.close();
//			} catch (SQLException e) {
//				try {
//					connection.rollback();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
//				e.printStackTrace();			
//			}finally{
//				try {
//					pst.close();
//					connection.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		} //fim-buscar_owner_especifico?
//		else{ //buscar_todos_os_owners
//			try {
//				connection.setAutoCommit(false);
//				
//				// preencher o objeto dos bancos
//				StringBuilder sql = new StringBuilder();
//				sql.append("select OWN_BAN_ID, OWN_ID, OWN_NOME_OWNER from owner order by 3");
//						
//				pst = connection.prepareStatement(sql.toString());
//				
//				ResultSet rs = pst.executeQuery();
//				
//				ArrayList<Owner> owners = new ArrayList<Owner>();
//				Banco b = new Banco();
//				while (rs.next()) {
//					Owner o = new Owner();
//					if (b.getId() == null || b.getId() != rs.getInt(1)){
//						b = new Banco();
//						b.setId(rs.getInt(1));
//					}
//					o.setBanco(b);
//					o.setId(rs.getInt(2));
//					o.setNomeOwner(rs.getString(3));
//					owners.add(o);
//				}
//				
//				solicitacao.setOwners(owners);
//				
//				rs.close();	
//			} catch (SQLException e) {
//				try {
//					connection.rollback();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
//				e.printStackTrace();			
//			}finally{
//				try {
//					pst.close();
//					connection.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		} //fim-buscar_todos_os_owners
//		return solicitacao;
//	} //fim-consultarOwners()
//	
//	private AgendamentoReorg consultarTabelas(AgendamentoReorg solicitacao) {
//		openConnection();
//		PreparedStatement pst = null;
//		if (solicitacao.getOwner() != null){ //buscar_tabela_especifica?
//			Owner o = solicitacao.getOwner();
//			Integer id = o.getId();
//			ArrayList<Tabela> tbs = new ArrayList<Tabela>();
//			try {
//				connection.setAutoCommit(false);
//				// preencher o objeto das tabelas
//				StringBuilder sql = new StringBuilder();
//				sql.append("SELECT t.tab_id, t.tab_nome_tabela FROM banco b, owner o,"
//						+ "tabela t WHERE b.ban_id = o.own_ban_id AND o.own_id = t.tab_own_id AND o.own_id = ");
//				sql.append(id);
//			    sql.append(" ORDER BY 2");
//				
//				pst = connection.prepareStatement(sql.toString());
//				
//				ResultSet rs = pst.executeQuery();
//				
//				while (rs.next()) {
//					Tabela t = new Tabela();
//					t.setId(rs.getInt(1));
//					t.setNomeTabela(rs.getString(2));
//					tbs.add(t);
//				}
//				
//				solicitacao.setTabelas(tbs);
//				
//				rs.close();
//			} catch (SQLException e) {
//				try {
//					connection.rollback();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
//				e.printStackTrace();			
//			}finally{
//				try {
//					pst.close();
//					connection.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}else{ // senao, buscar_todas_as_tabelas
//			try {
//				connection.setAutoCommit(false);
//				
//				// preencher o objeto das tabelas
//				StringBuilder sql = new StringBuilder();
//				sql.append("select TAB_OWN_ID, TAB_ID, TAB_NOME_TABELA from tabela order by 3");
//						
//				pst = connection.prepareStatement(sql.toString());
//				
//				ResultSet rs = pst.executeQuery();
//				
//				ArrayList<Tabela> tabelas = new ArrayList<Tabela>();
//				Owner o = new Owner();
//				while (rs.next()) {
//					Tabela t = new Tabela();
//					if (o.getId() == null || o.getId() != rs.getInt(1)){
//						o = new Owner();
//						o.setId(rs.getInt(1));
//					}
//					t.setOwner(o);
//					t.setId(rs.getInt(2));
//					t.setNomeTabela(rs.getString(3));
//					tabelas.add(t);
//				}
//				
//				solicitacao.setTabelas(tabelas);
//				
//				rs.close();	
//			} catch (SQLException e) {
//				try {
//					connection.rollback();
//				} catch (SQLException e1) {
//					e1.printStackTrace();
//				}
//				e.printStackTrace();			
//			}finally{
//				try {
//					pst.close();
//					connection.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		} //fim-buscar_todas_as_tabelas
//		return solicitacao;
//	} //fim-consultarTabelas()

	private void consultarTabelasSolicitacao(AgendamentoReorg agendamentoReorg) {
		openConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuilder sql = null;
		try {
			connection.setAutoCommit(false);
			// buscar todas as tabelas da solicitação na base da aplicação
			// com seu cliente, banco e owner
			sql = new StringBuilder();
			sql.append("select c.CLI_ID, c.CLI_NOME_CLIENTE, b.BAN_ID, b.BAN_NOME_BANCO, o.OWN_ID, "
					+ "o.OWN_NOME_OWNER, t.TAB_ID, t.TAB_NOME_TABELA "
					+ "from solicitacao s, solicitacao_tabela m, tabela t, owner o, banco b, cliente c "
					+ "where s.sol_id=m.stb_sol_id and m.stb_tab_id=t.tab_id and t.TAB_OWN_ID=o.OWN_ID and "
					+ "o.OWN_BAN_ID=b.BAN_ID and b.BAN_CLI_ID=c.CLI_ID and "
					+ "s.sol_id = ");
			sql.append(agendamentoReorg.getId());
			pst = connection.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			//Integer cont = 1;
			Integer i = 0;
			while (rs.next()) {
				// verificar se é a primeira iteração do while:
				if (agendamentoReorg.getBanco() == null){
					agendamentoReorg.setBanco(new Banco());
					agendamentoReorg.getBanco().setCliente(new Cliente());
					agendamentoReorg.getBanco().getCliente().setId(rs.getInt("CLI_ID"));
					agendamentoReorg.getBanco().getCliente().setNomeCliente(rs.getString("CLI_NOME_CLIENTE"));
					agendamentoReorg.getBanco().setId(rs.getInt("BAN_ID"));
					agendamentoReorg.getBanco().setNomeBanco(rs.getString("BAN_NOME_BANCO"));
					agendamentoReorg.getBanco().setOwners(new ArrayList<Owner>());
					agendamentoReorg.getBanco().getOwners().add(new Owner());
					agendamentoReorg.getBanco().getOwners().get(0).setId(rs.getInt("OWN_ID"));
					agendamentoReorg.getBanco().getOwners().get(0).setNomeOwner(rs.getString("OWN_NOME_OWNER"));
					agendamentoReorg.getBanco().getOwners().get(0).setTabelas(new ArrayList<Tabela>());
				}
				// adicionar os dados da tabela, seja em qual iteração estiver:
				agendamentoReorg.getBanco().getOwners().get(0).getTabelas().add(new Tabela());
				agendamentoReorg.getBanco().getOwners().get(0).getTabelas().get(i).setId(rs.getInt("TAB_ID"));
				agendamentoReorg.getBanco().getOwners().get(0).getTabelas().get(i++).setNomeTabela(rs.getString("TAB_NOME_TABELA"));
				//cont = 1;
			}
			rs.close();
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
		return;
	} //fim-consultarTabelasSolicitacao()
	
	private void consultarIndicesTabela(Tabela t) {
		openConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuilder sql = null;
		try {
			connection.setAutoCommit(false);
			// buscar todas as tabelas da solicitação na base da aplicação
			// com seu cliente, banco e owner
			sql = new StringBuilder();
			sql.append("select distinct i.IND_OWNER_INDICE, i.IND_NOME_INDICE, i.IND_COMPRESSION, "
					+ "i.IND_NOME_TABLESPACE, i.IND_INI_TRANS, i.IND_LOGGING, i.IND_STATUS, "
					+ "i.IND_LAST_ANALYZED, i.IND_PARALLEL, i.IND_PARTITIONED "
					+ "from indice i, tabela t "
					+ "where i.IND_TAB_ID=t.tab_id and "
					+ "t.tab_id = ");
			sql.append(t.getId());
			pst = connection.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			t.setIndices(new ArrayList<Indice>());
			Integer cont = 1;
			while (rs.next()) {
				Indice ind = new Indice();
				ind.setOwnerIndice(new Owner());
				ind.getOwnerIndice().setNomeOwner(rs.getString(cont++));
				ind.setNomeIndice(rs.getString(cont++));
				ind.setCompressionIndice(rs.getString(cont++));
				ind.setTablespaceIndice(rs.getString(cont++));
				ind.setIniTransIndice(rs.getDouble(cont++));
				ind.setLoggingIndice(rs.getString(cont++));
				ind.setStatusIndice(rs.getString(cont++));
				ind.setLastAnalyzedIndice(rs.getTimestamp(cont++));
				ind.setParallelIndice(rs.getString(cont++));
				ind.setPartitionedIndice(rs.getString(cont++));
				t.getIndices().add(ind);
				cont = 1;
			}
			rs.close();
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
		return;
	} //fim-consultarIndicesTabela()
	
	private void consultarMetadadosTabela(Tabela t) {
		openConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuilder sql = null;
		try {
			connection.setAutoCommit(false);
			sql = new StringBuilder();
			sql.append("select m.STB_PARALLEL, m.STB_INI_TRANS, m.STB_COMPRESS_TABLE, m.STB_CRITERIO_COLUNA, "
					+ "m.STB_PCT_ESTATISTICA, m.STB_TABLESPACE_DADOS, m.STB_TAMANHO_TABELA "
					+ "from solicitacao_tabela m, tabela t "
					+ "where t.tab_id=m.stb_tab_id and "
					+ "t.tab_id = ");
			sql.append(t.getId());
			pst = connection.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			// preencher os objetos com o resultado da query
			Integer cont = 1;
			while (rs.next()) {
				Metadado m = new Metadado();
				m.setParallel(rs.getString(cont++));
				m.setIniTrans(rs.getDouble(cont++));
				m.setCompressTable(rs.getString(cont++));
				m.setCriterioColuna(rs.getString(cont++));
				m.setPctEstatistica(rs.getDouble(cont++));
				m.setNomeTablespace(rs.getString(cont++));
				m.setTamanho(rs.getDouble(cont++));
				t.setMetadado(m);
				cont = 1;
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
		return;
	} //fim-consultarMetadadosTabela()

} //fim-classe
