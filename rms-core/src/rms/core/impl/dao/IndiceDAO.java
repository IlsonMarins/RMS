package rms.core.impl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rms.dominio.EntidadeDominio;
import rms.dominio.Indice;
import rms.dominio.Owner;
import rms.dominio.Tabela;

public class IndiceDAO extends AbstractJdbcDAO {

	public IndiceDAO() {
		super("INDICE", "IND_ID");
	}

	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void alterar(EntidadeDominio entidade) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<EntidadeDominio> visualizar(EntidadeDominio entidade) throws SQLException {
		
		return null;
		
	} // fim-visualizar()

	@Override
	public List<EntidadeDominio> consultar(EntidadeDominio entidade) throws SQLException {
		openConnection();
		// entidade de retorno
		List<EntidadeDominio> eds = new ArrayList<EntidadeDominio>();
		PreparedStatement pst = null;
		Tabela tabela = (Tabela)entidade;
		Indice indice = null;
		List<Indice> indices = new ArrayList<Indice>();
		try {
			connection.setAutoCommit(false);
			
			// buscar o(s) índice(s) dessa tabela
			StringBuilder sql = new StringBuilder();
			sql.append("select seq_id.nextval, d.owner, d.index_name, d.compression, d.tablespace_name,"
					+ "d.ini_trans, d.logging, d.status, d.last_analyzed, d.degree, d.partitioned "
					+ "from dba_indexes d "
					+ "where d.table_owner = ? and d.table_name = ?");
			pst = connection.prepareStatement(sql.toString());
			pst.setString(1, tabela.getOwner().getNomeOwner());
			pst.setString(2, tabela.getNomeTabela());
			ResultSet rs = pst.executeQuery();
			int cont = 1;
			while (rs.next()) {
				indice = new Indice();
				indice.setOwnerIndice(new Owner());
				indice.setId(rs.getInt(cont++));
				indice.getOwnerIndice().setNomeOwner(rs.getString(cont++));
				indice.setNomeIndice(rs.getString(cont++));
				indice.setCompressionIndice(rs.getString(cont++));
				indice.setTablespaceIndice(rs.getString(cont++));
				indice.setIniTransIndice(rs.getDouble(cont++));
				indice.setLoggingIndice(rs.getString(cont++));
				indice.setStatusIndice(rs.getString(cont++));
				indice.setLastAnalyzedIndice(rs.getDate(cont++));
				indice.setParallelIndice(rs.getString(cont++));
				indice.setPartitionedIndice(rs.getString(cont++));
				indices.add(indice);
				cont = 1;
			}
			tabela.setIndices(indices);
			eds.add(tabela);
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
		
		return eds;
		
	} // fim-consultar()

}
