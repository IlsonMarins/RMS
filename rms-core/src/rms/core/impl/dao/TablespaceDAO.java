
package rms.core.impl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rms.dominio.EntidadeDominio;
import rms.dominio.Indice;
import rms.dominio.Metadado;
import rms.dominio.Owner;
import rms.dominio.AgendamentoReorg;
import rms.dominio.ExecucaoReorg;
import rms.dominio.Tabela;

public class TablespaceDAO extends AbstractJdbcDAO {
	
	// construtor
	public TablespaceDAO() {
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
				sql.append("update solicitacao "
						+ "set sol_dt_fim_reorg = systimestamp, sol_status = ? "
						+ "where sol_id = ?");
				pst = connection.prepareStatement(sql.toString());
				pst.setString(1, solicitacoes.get(i).getStatus());
				pst.setInt(2, solicitacoes.get(i).getId());
				pst.executeUpdate();
				connection.commit();
				pst.close();
				// atualizar a tabela de console
				String msg = "ExecucaoReorg nº " 
						+ solicitacoes.get(i).getId() 
						+ " concluído com sucesso! ";
				pst = null;
				sql = new StringBuilder();
				sql.append("insert into console (con_dt_registro, con_mensagem) "
						+ "values (?, ?) ");
				pst = connection.prepareStatement(sql.toString());
				pst.setTimestamp(1, new Timestamp(new Date().getTime()));
				pst.setString(2, msg);
				pst.executeUpdate();
				connection.commit();
				pst.close();
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
				sql.append("update solicitacao "
						+ "set sol_dt_inicio_reorg = systimestamp, sol_status = ? "
						+ "where sol_id = ?");
				pst = connection.prepareStatement(sql.toString());
				pst.setString(1, solicitacoes.get(i).getStatus());
				pst.setInt(2, solicitacoes.get(i).getId());
				pst.executeUpdate();
				connection.commit();
				pst.close();
				// atualizar a tabela de console
				String msg = "Executando ExecucaoReorg nº " 
						+ solicitacoes.get(i).getId() 
						+ "... ";
				pst = null;
				sql = new StringBuilder();
				sql.append("insert into console (con_dt_registro, con_mensagem) "
						+ "values (?, ?) ");
				pst = connection.prepareStatement(sql.toString());
				pst.setTimestamp(1, new Timestamp(new Date().getTime()));
				pst.setString(2, msg);
				pst.executeUpdate();
				connection.commit();
				pst.close();
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
					System.out.print("\nReorgDAO - Owner: " + solicitacoes.get(i).getTabelas().get(j).getOwner().getNomeOwner());
					System.out.print("\nReorgDAO - Tabela: " + solicitacoes.get(i).getTabelas().get(j).getNomeTabela());
					System.out.print("\nReorgDAO - Tablespace: " + solicitacoes.get(i).getTabelas().get(j).getMetadado().getNomeTablespace());
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
		// inicialização dos objetos necessários para as consultas
		openConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		ExecucaoReorg execucaoReorg = (ExecucaoReorg)entidade;
		AgendamentoReorg agendamentoReorg = null;
		List<AgendamentoReorg> solicitacoes = execucaoReorg.getSolicitacoes();
		List<EntidadeDominio> edsRetorno = new ArrayList<EntidadeDominio>();
		String msg = null;
		try {
			// buscar as solicitações pendentes que estejam na hora de serem executadas
			// e preenchê-las no array de solicitações
			connection.setAutoCommit(false);
			sql.append("select * "
					+ "from solicitacao "
					+ "where SOL_STATUS = 'PENDENTE' and "
					+ "to_char(SOL_DT_AGENDADA, 'yyyy-mm-dd hh24:mi:ss') "
					+ "<= to_char(sysdate, 'yyyy-mm-dd hh24:mi:ss')");
			pst = connection.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			//Date dataAtual = new Date();
			//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
			//System.out.print("\nDAO - Data Atual: " + dataAtual); // Resultado: Thu May 04 10:43:27 BRT 2017
			//System.out.print("\nDAO - Data Atual formatada: " + sdf.format(dataAtual)); // Resultado: 04/05/17 10:43
			while (rs.next()) {
				agendamentoReorg = new AgendamentoReorg();
				agendamentoReorg.setId(rs.getInt(1));
				agendamentoReorg.setDtAgendamento(rs.getTimestamp(2));
				agendamentoReorg.setDtCadastro(rs.getTimestamp(3));
				//if (solicitacao.getDtAgendada().compareTo(dataAtual) <= 0){ // data agendada é igual ou anterior à data atual?
				//	solicitacao.setStatus("EXECUTAR"); // marcar esta solicitacao para executar, pois chegou a hora
				agendamentoReorg.setStatus("AGUARDANDO");
				pst = null;
				sql = new StringBuilder();
				sql.append("update solicitacao "
						+ "set sol_status = ? "
						+ "where sol_id = ?");
				pst = connection.prepareStatement(sql.toString());
				pst.setString(1, agendamentoReorg.getStatus());
				pst.setInt(2, agendamentoReorg.getId());
				pst.executeUpdate();
				connection.commit();
				pst.close();
				solicitacoes.add(agendamentoReorg);
				// atualizar a tabela de console
				msg = "ExecucaoReorg nº " 
						+ agendamentoReorg.getId() 
						+ " marcado para iniciar. ";
				pst = null;
				sql = new StringBuilder();
				sql.append("insert into console (con_dt_registro, con_mensagem) "
						+ "values (?, ?) ");
				pst = connection.prepareStatement(sql.toString());
				pst.setTimestamp(1, new Timestamp(new Date().getTime()));
				pst.setString(2, msg);
				pst.executeUpdate();
				connection.commit();
				pst.close();
			}
			// existe reorg a ser executado no momento?
			if (!solicitacoes.isEmpty()){
				// buscar os metadados de cada solicitacao
				// varre cada solicitacao
				for (int i = 0; i < solicitacoes.size(); i++){
					sql = new StringBuilder();
					sql.append("select t.TAB_ID, t.TAB_NOME_TABELA, o.OWN_NOME_OWNER, s.STB_PARALLEL, ");
					sql.append("s.STB_INI_TRANS, s.STB_COMPRESS_TABLE, s.STB_CRITERIO_COLUNA, ");
					sql.append("s.STB_PCT_ESTATISTICA, s.STB_TABLESPACE_DADOS, s.STB_TAMANHO_TABELA ");
					sql.append("from solicitacao_tabela s, tabela t, owner o ");
					sql.append("where s.STB_TAB_ID = t.TAB_ID and t.TAB_OWN_ID = o.OWN_ID and s.STB_SOL_ID = ?");
					pst = null;
					pst = connection.prepareStatement(sql.toString());
					pst.setInt(1, solicitacoes.get(i).getId());
					rs = null;
					rs = pst.executeQuery();
					ArrayList<Tabela> tbs = new ArrayList<Tabela>();
					int cont = 1;
					// varre cada linha do resultado da query
					while (rs.next()) {
						Tabela t = new Tabela();
						t.setId(rs.getInt(cont++));
						t.setNomeTabela(rs.getString(cont++));
						Owner o = new Owner();
						o.setNomeOwner(rs.getString(cont++));
						t.setOwner(o);
						Metadado m = new Metadado();
						m.setParallel(rs.getString(cont++));
						m.setIniTrans(rs.getDouble(cont++));
						m.setCompressTable(rs.getString(cont++));
						m.setCriterioColuna(rs.getString(cont++));
						m.setPctEstatistica(rs.getDouble(cont++));
						m.setNomeTablespace(rs.getString(cont++));
						m.setTamanho(rs.getDouble(cont++));
						t.setMetadado(m);
						tbs.add(t);
						cont = 1;
					}
					solicitacoes.get(i).setTabelas(tbs);
				}
				// buscar os índices de cada tabela
				// varre cada solicitacao
				for (AgendamentoReorg s : solicitacoes){
					// varre cada tabela da solicitacao
					for (Tabela t : s.getTabelas()){
						pst = null;
						rs = null;
						sql = new StringBuilder();
						sql.append("SELECT i.IND_ID, i.IND_OWNER_INDICE, i.IND_NOME_INDICE, ");
						sql.append("i.IND_COMPRESSION, i.IND_NOME_TABLESPACE, i.IND_INI_TRANS, i.IND_LOGGING, ");
						sql.append("i.IND_STATUS, i.IND_LAST_ANALYZED, i.IND_PARALLEL, i.IND_PARTITIONED ");
						sql.append("FROM indice i, tabela t ");
						sql.append("WHERE t.tab_id = i.ind_tab_id AND t.tab_id = ?");
						pst = connection.prepareStatement(sql.toString());
						pst.setInt(1, t.getId());
						rs = pst.executeQuery();
						int cont = 1;
						List<Indice> indices = new ArrayList<Indice>();
						// varre cada linha do resultado da query
						while (rs.next()) {
							Indice indice = new Indice();
							Owner owner = new Owner();
							indice.setId(rs.getInt(cont++));
							owner.setNomeOwner(rs.getString(cont++));
							indice.setOwnerIndice(owner);
							indice.setNomeIndice(rs.getString(cont++));
							indice.setCompressionIndice(rs.getString(cont++));
							indice.setTablespaceIndice(rs.getString(cont++));
							indice.setIniTransIndice(rs.getDouble(cont++));
							indice.setLoggingIndice(rs.getString(cont++));
							indice.setStatusIndice(rs.getString(cont++));
							indice.setLastAnalyzedIndice(rs.getDate(cont++));
							indice.setParallelIndice(rs.getString(cont++));
							indice.setPartitionedIndice(rs.getString(cont++));
							cont = 1;
							indices.add(indice);
						}
						t.setIndices(indices);
					}
				}
				rs.close();
			}else{
				// atualizar a tabela de console
				msg = "Nenhum ExecucaoReorg encontrado para execução no momento!";
				pst = null;
				sql = new StringBuilder();
				sql.append("insert into console (con_dt_registro, con_mensagem) "
						+ "values (?, ?) ");
				pst = connection.prepareStatement(sql.toString());
				pst.setTimestamp(1, new Timestamp(new Date().getTime()));
				pst.setString(2, msg);
				pst.executeUpdate();
				connection.commit();
				return null;
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
		edsRetorno.addAll(solicitacoes);
		return edsRetorno;
	} //fim-consultar
	
	@SuppressWarnings("resource")
	@Override
	public List<EntidadeDominio> visualizar(EntidadeDominio entidade) {
		List<EntidadeDominio> eds = new ArrayList<EntidadeDominio>();
		if (entidade instanceof ExecucaoReorg){ // executar o REORG?
			openConnection();
			PreparedStatement pst = null;
			try {
				connection.setAutoCommit(false);
				
				// buscar as solicitações pendentes que estejam na hora de serem executadas
				// e preenchê-las no array de solicitações
				StringBuilder sql = new StringBuilder();
				sql.append("select * "
						+ "from solicitacao "
						+ "where SOL_STATUS = 'PENDENTE' "
						+ "and to_char(SOL_DT_AGENDADA, 'yyyy-mm-dd hh24:mi:ss') "
						+ "<= to_char(sysdate, 'yyyy-mm-dd hh24:mi:ss')");
				pst = connection.prepareStatement(sql.toString());
				
				ResultSet rs = pst.executeQuery();
				
				AgendamentoReorg agendamentoReorg = new AgendamentoReorg();
				ArrayList<AgendamentoReorg> solicitacoes = new ArrayList<AgendamentoReorg>();
				//Date dataAtual = new Date();
				//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
				//System.out.print("\nDAO - Data Atual: " + dataAtual); // Resultado: Thu May 04 10:43:27 BRT 2017
				//System.out.print("\nDAO - Data Atual formatada: " + sdf.format(dataAtual)); // Resultado: 04/05/17 10:43
				while (rs.next()) {
					agendamentoReorg = new AgendamentoReorg();
					agendamentoReorg.setId(rs.getInt(1));
					agendamentoReorg.setDtAgendamento(rs.getTimestamp(2));
					agendamentoReorg.setDtCadastro(rs.getTimestamp(3));
					//if (solicitacao.getDtAgendada().compareTo(dataAtual) <= 0){ // data agendada é igual ou anterior à data atual?
					//	solicitacao.setStatus("EXECUTAR"); // marcar esta solicitacao para executar, pois chegou a hora
					agendamentoReorg.setStatus("AGUARDANDO");
					pst = null;
					sql = new StringBuilder();
					sql.append("UPDATE SOLICITACAO SET SOL_STATUS = ? WHERE SOL_ID = ?");
					
					pst = connection.prepareStatement(sql.toString());
					
					pst.setString(1, agendamentoReorg.getStatus());
					pst.setInt(2, agendamentoReorg.getId());
					
					pst.executeUpdate();
					connection.commit();
					solicitacoes.add(agendamentoReorg);
				}
				
				if (solicitacoes.isEmpty()) //não existe reorg a ser executado?
					return null;
				
				// preencher com informações detalhadas cada solicitação pendente que já chegou a hora
				for (int i = 0; i < solicitacoes.size(); i++){
					//if(!solicitacoes.get(i).getStatus().equals("EXECUTAR")) // solicitacao não está marcada para executar?
					//	continue;
					sql = new StringBuilder();
					sql.append("select t.TAB_ID, t.TAB_NOME_TABELA, o.OWN_NOME_OWNER, s.STB_PARALLEL, ");
					sql.append("s.STB_INI_TRANS, s.STB_COMPRESS_TABLE, s.STB_CRITERIO_COLUNA, ");
					sql.append("s.STB_PCT_ESTATISTICA, s.STB_TABLESPACE_DADOS, s.STB_TAMANHO_TABELA ");
					sql.append("from solicitacao_tabela s, tabela t, owner o ");
					sql.append("where s.STB_TAB_ID = t.TAB_ID and t.TAB_OWN_ID = o.OWN_ID and s.STB_SOL_ID = ?");
					
					pst = null;
					pst = connection.prepareStatement(sql.toString());
					pst.setInt(1, solicitacoes.get(i).getId());
					
					rs = null;
					rs = pst.executeQuery();
					ArrayList<Tabela> tbs = new ArrayList<Tabela>();
					int cont = 1;
					while (rs.next()) {
						Tabela t = new Tabela();
						t.setId(rs.getInt(cont++));
						
						t.setNomeTabela(rs.getString(cont++));
						
						Owner o = new Owner();
						o.setNomeOwner(rs.getString(cont++));
						t.setOwner(o);
						
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
						tbs.add(t);
					} //fim-while(next)
					solicitacoes.get(i).setTabelas(tbs);
				} //fim-for(solicitacoes.size())
				
				// executar o reorg em cada uma das tabelas de cada uma das solicitacoes pendentes
				for (int i = 0; i < solicitacoes.size(); i++){ // varre todas as solicitações
					//if(!solicitacoes.get(i).getStatus().equals("EXECUTAR")) // solicitacao não está marcada para executar?
					//	continue;
					
					// atualizar o status antes de iniciar a reorganização
					solicitacoes.get(i).setStatus("EXECUTANDO");
					pst = null;
					sql = new StringBuilder();
					sql.append("UPDATE SOLICITACAO SET SOL_STATUS = ? WHERE SOL_ID = ?");
					
					pst = connection.prepareStatement(sql.toString());
					
					pst.setString(1, solicitacoes.get(i).getStatus());
					pst.setInt(2, solicitacoes.get(i).getId());
					
					pst.executeUpdate();
					connection.commit();
					
					// varrer todas as tabelas de cada solicitação
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
						
						// validar os índices
						//pst = null;
						//sql = new StringBuilder();
						//sql.append("alter index ?.? rebuild");
						//pst = connection.prepareStatement(sql.toString());
						//pst.setString(1, solicitacoes.get(i).getOwner().getNomeOwner());
						//pst.setString(2, solicitacoes.get(i).getTabelas().get(j).getNomeTabela());
						//pst.setString(3, solicitacoes.get(i).getTabelas().get(j).getMetadado().getTablespaceDados());
						//pst.executeUpdate();
						
						// fazer a coleta de estatística da tabela reorganizada
						pst = null;
						sql = new StringBuilder();
						sql.append("ANALYZE TABLE ");
						sql.append(solicitacoes.get(i).getTabelas().get(j).getOwner().getNomeOwner());
						sql.append(".");
						sql.append(solicitacoes.get(i).getTabelas().get(j).getNomeTabela());
						sql.append(" COMPUTE STATISTICS");
						pst = connection.prepareStatement(sql.toString());
						pst.executeUpdate();
					} //fim-for(tabelas.size())
					
					// atualizar o status após a reorganização
					solicitacoes.get(i).setStatus("CONCLUIDO");
					pst = null;
					sql = new StringBuilder();
					sql.append("UPDATE SOLICITACAO SET SOL_STATUS = ? WHERE SOL_ID = ?");
					
					pst = connection.prepareStatement(sql.toString());
					
					pst.setString(1, solicitacoes.get(i).getStatus());
					pst.setInt(2, solicitacoes.get(i).getId());
					
					pst.executeUpdate();
					connection.commit();
				} //fim-for(solicitacoes.size())
				
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
			return null;
		} // fim-executar_reorg?
		return eds;
		
	} //fim-consultar()
	
	
	

} //fim-classe
