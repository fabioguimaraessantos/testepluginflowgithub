package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IFaturaService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.util.MailSenderUtil;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.audit.listener.AuditoriaJpaListener;
import com.ciandt.pms.business.service.IHistoricoFaturaService;
import com.ciandt.pms.business.service.IItemFaturaService;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.persistence.dao.IItemFaturaDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * A classe ItemFaturaService proporciona as funcionalidades de serviço para a
 * entidade ItemFatura.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class ItemFaturaService implements IItemFaturaService {

	/**
	 * Instancia do DAO da entidade.
	 */
	@Autowired
	private IItemFaturaDao itemFaturaDao;

	private MailSenderUtil mailSender;

	@Autowired
	private IHistoricoFaturaService historicoFaturaService;

	@Autowired
	private IFaturaService faturaService;

	/** Intancia que realiza a auditoria (Log). */
	private AuditoriaJpaListener auditoria = new AuditoriaJpaListener();

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	public void createItemFatura(final ItemFatura entity) {
		entity.setCodigoItemFatura(null);

		itemFaturaDao.create(entity);

		historicoFaturaService.createHistoricoFatura(entity.getFatura());

		auditoria.postPersist(entity);
	}

	/**
	 * Atualiza a entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 */
	public void updateItemFatura(final ItemFatura entity) {

		itemFaturaDao.update(entity);

		auditoria.preUpdate(entity);
	}

	/**
	 * Remove a entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será removida.
	 * @param log
	 *            true deve logar e false nao.
	 * 
	 */
	public void removeItemFatura(final ItemFatura entity, final Boolean log) {

		if (log) {
			auditoria.postRemove(entity);
		}

		itemFaturaDao.remove(entity);
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public ItemFatura findItemFaturaById(final Long id) {
		return itemFaturaDao.findById(id);
	}

	/**
	 * Busca uma lista de ItemFatura associado a uma Fatura passada por
	 * parametro.
	 * 
	 * @param fatura
	 *            do tipo Fatura para realizar a busca
	 * @return lista de ItemFatura que estao associados a Fatura
	 */
	public List<ItemFatura> findItemFaturaByFatura(final Fatura fatura) {
		return itemFaturaDao.findByFatura(fatura);
	}

	/**
	 * Pega o total associado pela fatura.
	 * 
	 * @param fatura
	 *            - Fatura em questão
	 * 
	 * @return retorna um double com o valor total associado da fatura
	 */
	public BigDecimal getItemFaturaTotalByFatura(final Fatura fatura) {
		return itemFaturaDao.getTotalByFatura(fatura);
	}

	/**
	 * Busca pelos items da fatura que atendem o criterio do filtro.
	 * 
	 * @param startDate
	 *            - data inicio
	 * @param endDate
	 *            - data fim
	 * @param f
	 *            - fatura
	 * @param item
	 *            - item fatura
	 * @param c
	 *            - cliente
	 * @param e
	 *            - empresa
	 * @param onlyNotPaid
	 *            - flag que indica se somente pagas
	 * @param msa
	 *            - the {@link Msa}
	 * @param dataPagamentoDe
	 *            - data de pagamento "de" para filtrar por range
	 * @param dataPagamentoAte
	 *            - data de pagamento "ate" para filtrar por range
	 * 
	 * @return retorna a lista com os resultados.
	 */
	public List<ItemFatura> findItemFaturaByFilter(final Date startDate,
			final Date endDate, final Fatura f, final ItemFatura item,
			final Cliente c, final Empresa e, final Boolean onlyNotPaid,
			final Boolean filtroData, final Msa msa,
			final Date dataPagamentoDe, final Date dataPagamentoAte) {

		List<ItemFatura> itensFatura = itemFaturaDao.findByFilter(startDate,
				endDate, f, item, c, e, onlyNotPaid, filtroData, msa,
				dataPagamentoDe, dataPagamentoAte);

		for (ItemFatura itemFatura : itensFatura) {
			Hibernate.initialize(itemFatura.getFatura());
			Hibernate.initialize(itemFatura.getFatura().getDealFiscal());
			Hibernate.initialize(itemFatura.getFatura().getDealFiscal()
					.getMoeda());
			Hibernate.initialize(itemFatura.getFatura().getDealFiscal()
					.getMsa());
		}

		return itensFatura;
	}
}