package rms.dominio;

import java.util.Date;
import java.util.List;

public class AgendamentoReorg extends EntidadeDominio{
	// atributos privados
	private Cliente cliente;
	private Banco banco;
	private Owner owner;
	private List<Tabela> tabelas;
	private List<Cliente> clientes;
	private List<Banco> bancos;
	private List<Owner> owners;
	private String status;
	private Date dtAgendamento;
	private Date dtInicioReorg;
	private Date dtFimReorg;
	private Registro registro;
	private String entidadeBusca;
	
	// métodos de acesso
	public List<Tabela> getTabelas() {
		return tabelas;
	}

	public void setTabelas(List<Tabela> tbs) {
		this.tabelas = tbs;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDtAgendamento() {
		return dtAgendamento;
	}

	public void setDtAgendamento(Date dtAgendada) {
		this.dtAgendamento = dtAgendada;
	}

	public Date getDtInicioReorg() {
		return dtInicioReorg;
	}

	public void setDtInicioReorg(Date dtInicioReorg) {
		this.dtInicioReorg = dtInicioReorg;
	}

	public Date getDtFimReorg() {
		return dtFimReorg;
	}

	public void setDtFimReorg(Date dtFimReorg) {
		this.dtFimReorg = dtFimReorg;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public Registro getRegistro() {
		return registro;
	}

	public void setRegistro(Registro registro) {
		this.registro = registro;
	}

	public List<Banco> getBancos() {
		return bancos;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}

	public List<Owner> getOwners() {
		return owners;
	}

	public void setOwners(List<Owner> owners) {
		this.owners = owners;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getEntidadeBusca() {
		return entidadeBusca;
	}

	public void setEntidadeBusca(String entidadeBusca) {
		this.entidadeBusca = entidadeBusca;
	}
	
}
