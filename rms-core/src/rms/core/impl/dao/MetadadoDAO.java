package rms.core.impl.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rms.dominio.EntidadeDominio;
import rms.dominio.Metadado;
import rms.dominio.Owner;
import rms.dominio.Tabela;

public class MetadadoDAO extends AbstractJdbcDAO {

	public MetadadoDAO() {
		super("FAKE_TABELA", "FAKE_TAB_ID");
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
		PreparedStatement pst = null;
		List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
		Tabela tabela = (Tabela)entidade;
		try {
			connection.setAutoCommit(false);
			// buscar o owner dessa tabela
			StringBuilder sql = new StringBuilder();
			sql.append("select o.own_nome_owner "
					+ "from owner o, tabela t "
					+ "where t.tab_own_id=o.own_id "
					+ "and t.tab_id = ? ");
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, tabela.getId());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				tabela.setOwner(new Owner());
				tabela.getOwner().setNomeOwner(rs.getString(1));
			}
			// buscar os dados necessários para buscar os metadados nas views
			// do dicionário de dados
			sql = new StringBuilder();
			sql.append("SELECT TAB_OWN_ID, TAB_ID, TAB_NOME_TABELA "
					+ "FROM TABELA "
					+ "WHERE TAB_ID = ?");
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, tabela.getId());
			rs = null;
			rs = pst.executeQuery();
			if (rs.next()) {
				tabela.getOwner().setId(rs.getInt("TAB_OWN_ID"));
				tabela.setId(rs.getInt("TAB_ID"));
				tabela.setNomeTabela(rs.getString("TAB_NOME_TABELA"));
				// buscar os metadados efetivamente
				sql = new StringBuilder();
				sql.append("SELECT D.DEGREE, D.INI_TRANS, DECODE(D.COMPRESSION, 'DISABLED', ");
				sql.append("D.COMPRESSION, D.COMPRESSION || ' FOR ' || D.COMPRESS_FOR), 'FALSO', ");
				sql.append("DECODE(D.SAMPLE_SIZE, 0, D.SAMPLE_SIZE, (D.NUM_ROWS / D.SAMPLE_SIZE * 100)), ");
				sql.append("D.TABLESPACE_NAME, S.BYTES/1024, D.LAST_ANALYZED ");
				sql.append("FROM DBA_TABLES D, DBA_SEGMENTS S, OWNER O ");
				sql.append("WHERE D.TABLE_NAME = '");
				sql.append(tabela.getNomeTabela() + "' AND S.SEGMENT_NAME = '");
				sql.append(tabela.getNomeTabela() + "' AND D.OWNER = O.OWN_NOME_OWNER AND O.OWN_ID = ");
				sql.append(tabela.getOwner().getId());
				pst = connection.prepareStatement(sql.toString());
				rs = pst.executeQuery();
				int cont = 1;
				if (rs.next()) {
					tabela.setMetadado(new Metadado());
					tabela.getMetadado().setParallel(rs.getString(cont++));
					tabela.getMetadado().setIniTrans(rs.getDouble(cont++));
					tabela.getMetadado().setCompressTable(rs.getString(cont++));
					tabela.getMetadado().setCriterioColuna(rs.getString(cont++));
					tabela.getMetadado().setPctEstatistica(rs.getDouble(cont++));
					tabela.getMetadado().setNomeTablespace(rs.getString(cont++));
					tabela.getMetadado().setTamanho(rs.getDouble(cont++));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"); // formato do BD
					Date date = sdf.parse(rs.getString(cont++)); // faz o parse para o tipo Date
					tabela.getMetadado().setLastAnalyzed(date); // insere no atributo o tipo Date corretamente
//					System.out.println("\nData inserida: " + t.getMetadado().getLastAnalyzed());
					cont = 1;
				}
			}
			rs.close();	
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();			
		} catch (ParseException e) {
			System.out.println("\nProblema no parse do tipo Date!");
			e.printStackTrace();
		}finally{
			try {
				pst.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		entidades.add(tabela);
		return entidades;
	} // fim-consultar()
}
