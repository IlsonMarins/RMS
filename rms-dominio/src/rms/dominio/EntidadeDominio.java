package rms.dominio;


import java.util.Date;

public class EntidadeDominio implements IEntidade{
	
	// atributos privados
	private Integer id;
	private Date dtCadastro;

	// métodos de acesso
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getDtCadastro() {
		return dtCadastro;
	}
	public void setDtCadastro(Date dtCadastro) {
		this.dtCadastro = dtCadastro;
	}
	
	
	

}
