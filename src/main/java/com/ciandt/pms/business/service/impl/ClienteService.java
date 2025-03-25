package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IClienteService;
import com.ciandt.pms.business.service.IOrcDespesaClService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.OrcDespesaCl;
import com.ciandt.pms.persistence.dao.IClienteDao;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.MailSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Define o Service da entidade.
 * 
 * @since 31/07/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class ClienteService implements IClienteService {

	/**
	 * Instancia do DAO da entidade.
	 */
	@Autowired
	private IClienteDao dao;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** Intancia de mailSender. */
	private MailSenderUtil mailSender;

	/** Instancia do servico orcamentoDespesa. */
	@Autowired
	private IOrcDespesaClService orcDespClService;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	public void createCliente(final Cliente entity) {
		dao.create(entity);
	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * @throws IntegrityConstraintException
	 *             lança uma exceção caso este cliente seja pai e tenha cliente
	 *             pai atribuido ou caso o cliente seja pai de outros clientes e
	 *             tente inativá-lo
	 * 
	 */
	public void updateCliente(final Cliente entity)
			throws IntegrityConstraintException {
		// busca a entidade do banco por causa da sessão de conexão (hibernate)
		Cliente cliente = findClienteById(entity.getCodigoCliente());

		// usa a entidade do banco para verificar se existem filhos
		if (cliente.getClientes().size() > 0) {
			// se sim verifica na entidade da tela se foi atribuído um
			// ClientePai, se sim um ClientePai não pode ter ClientePai e lança
			// exceção
			if (entity.getCliente() != null) {
				throw new IntegrityConstraintException(
						Constants.MSG_ERROR_UPDATE_CLIENTE_PAI);
			}
		}

		// verifica se a entidade da tela está sendo inativada
		if (entity.getIndicadorAtivo().equals(Constants.INACTIVE)) {
			// se sim, usa a entidade do banco para verificar se existem filhos,
			// se sim lança exceção
			if (cliente.getClientes().size() > 0) {
				throw new IntegrityConstraintException(
						Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_INACTIVE);
			}
		}

		dao.update(entity);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * @throws IntegrityConstraintException
	 *             lança uma exception caso este cliente seja pai de outros
	 *             clientes
	 * 
	 */
	public void removeCliente(final Cliente entity)
			throws IntegrityConstraintException {
		// verifica se existem ClienteFilho relacionado, se sim lança exceção
		if (entity.getClientes().size() > 0) {
			throw new IntegrityConstraintException(
					Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE);
		} else if (entity.getMsas().size() > 0) {
			throw new IntegrityConstraintException(
					Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE);
		}

		// Busca Orcamentos Despesa associados
		List<OrcDespesaCl> listOrcDespCl = orcDespClService
				.findOrcDespesaClByCliente(entity);

		// Remove Orcamentos Despesa associados
		for (OrcDespesaCl orcDesp : listOrcDespCl) {
			orcDespClService.removeOrcDespesaCl(orcDesp);
		}

		dao.remove(entity);
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public Cliente findClienteById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Busca uma lista de entidades pelo filtro - somente ClientePai.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<Cliente> findClientePaiByFilter(final Cliente filter) {
		return dao.findByFilterClientePai(filter);
	}

	/**
	 * Busca uma lista de entidades pelo filtro - somente ClienteFilho.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<Cliente> findClienteFilhoByFilter(final Cliente filter) {
		return dao.findByFilterClienteFilho(filter);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<Cliente> findClienteAll() {

		return dao.findAll();
	}

	/**
	 * Retorna todas os clientes pai.
	 * 
	 * @return retorna uma lista com todos os clientes pai.
	 */
	public List<Cliente> findClienteAllClientePai() {

		return dao.findAllClientePai();
	}

	public List<Cliente> findClienteAllClientePaiForComboBox() {

		return dao.findAllClientePaiForComboBox();
	}

	/**
	 * Retorna todas os clientes filho.
	 * 
	 * @return retorna uma lista com todos os clientes filho.
	 */
	public List<Cliente> findClienteAllClienteFilho() {

		return dao.findAllClienteFilho();
	}

	/**
	 * Busca uma lista de entidades pelo cliente pai.
	 * 
	 * @param clientePai
	 *            entidade do tipo Cliente pai de outras entidades.
	 * 
	 * @return lista de entidades filhas, referente ao pai passado por
	 *         parametro.
	 */
	public List<Cliente> findClienteByClientePai(final Cliente clientePai) {
		return dao.findByClientePai(clientePai);
	}

	/**
	 * Cria e envia uma mensagem de email de um Cliente.
	 * 
	 * @param cliente
	 *            - entidade do tipo Cliente.
	 */
	public void sendClienteMail(final Cliente cliente) {

		Map<String, Object> dataSource = new HashMap<String, Object>();
		dataSource.put("cliente", cliente);
		dataSource.put("author", LoginUtil.getLoggedUsername());

		String message = mailSender.getTemplateMailMessage("cliente.vm",
				dataSource);

		String subject = BundleUtil.getBundle("_nls.cliente.mail.subject",
				cliente.getNomeCliente());

		String to = systemProperties
				.getProperty(Constants.EMAIL_ADDRESS_ADM_COMERCIAL_KEY);

		mailSender.sendHtmlMail(to, subject, message);
	}

	/**
	 * @param mailSender
	 *            the mailSender to set
	 */
	public void setMailSender(final MailSenderUtil mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * @return the mailSender
	 */
	public MailSenderUtil getMailSender() {
		return mailSender;
	}

    /**
     * Retorna um {@link Cliente} relacionado a {@code centroLucro}.
     *
     * @param centroLucro
     * @return {@link Cliente}
     */
    @Override
    public List<Cliente> findClienteByCentroLucro(final CentroLucro centroLucro) {
    	return dao.findByCentroLucro(centroLucro);
    }

	/**
	 * Esse método retorna o código da empresa da sensedia se a string 'sensedia' existir no nome do cliente.
	 * Estamos fazendo assim pois hoje no PMS não há o
	 * @param clientName
	 * @return
	 */
	@Override
	public Long getCompanyCodeByClientName(String clientName) {
		return clientName.toUpperCase().contains("SENSEDIA") ? 3L : 1L;
	}

	/**
	 * Retorna um {@link Cliente} relacionado a {@code codigoOracleTaxpayerCustomer}.
	 * @param codigoOracleTaxpayerCustomer
	 * @return
	 */
	@Override
	public List<Cliente> findClienteByCodigoOracleTaxpayerCustomer(String codigoOracleTaxpayerCustomer) {
		return dao.findByCodigoOracleTaxpayerCustomer(codigoOracleTaxpayerCustomer);
	}
}