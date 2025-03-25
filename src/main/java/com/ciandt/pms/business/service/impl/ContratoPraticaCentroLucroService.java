package com.ciandt.pms.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IContratoPraticaCentroLucroService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.MoreThanOneActiveEntityException;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaCentroLucro;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.vo.NaturezaContratoPraticaCLRow;
import com.ciandt.pms.persistence.dao.IContratoPraticaCentroLucroDao;

/**
 * 
 * A classe ContratoPraticaCentroLucroService proporciona as funcionalidades da
 * camada de servico referente a entidade ContratoPraticaCentroLucro.
 * 
 * @since 18/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class ContratoPraticaCentroLucroService implements
		IContratoPraticaCentroLucroService {

	/** Instancia do dao da entidade. */
	@Autowired
	private IContratoPraticaCentroLucroDao dao;

	/** Instancia do servico da entidade NaturezaCentroLucro. */
	@Autowired
	private INaturezaCentroLucroService naturezaCentroLucroService;

	/** Instancia do servico da entidade ContratoPratica. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/** Instancia do Logger. */
	private static Logger log = LogManager
			.getLogger(ContratoPraticaCentroLucro.class);

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * 
	 * @return true se salvo com sucesso, false caso contrario
	 */
	public Boolean createCPCL(final ContratoPraticaCentroLucro entity) throws MoreThanOneActiveEntityException {

		ContratoPraticaCentroLucro cpcl = dao
				.findMaxStartDateByContratoPraticaAndNatureza(entity
						.getContratoPratica(), entity.getCentroLucro().getNaturezaCentroLucro());

		contratoPraticaService.verifyContratoPraticaComplete(entity.getContratoPratica());

		// verifica se a data de vigencia é a maior
		if (cpcl != null) {
			if (entity.getDataInicioVigencia().after(
					cpcl.getDataInicioVigencia())) {
				// se maior realiza um update da data fim da vigencia
				cpcl.setDataFimVigencia(DateUtils.addMonths(
						entity.getDataInicioVigencia(), -1));

				dao.update(cpcl);
				// senao erro, pois a data deve ser posterior a maior data de
				// inicio existente
			} else {
				Messages.showError("createCPCL",
						Constants.MSG_ERROR_INVALID_CPCL_START_DATE);
				return Boolean.FALSE;
			}
		}



		dao.create(entity);

		return Boolean.TRUE;
	}

	/**
	 * Busca uma lista de entidades pelo ContratoPratica.
	 * 
	 * @param cp
	 *            entidade populada com os valores do ContratoPratica.
	 * 
	 * @return lista de entidades que atendem ao criterio do ContratoPratica.
	 */
	public List<ContratoPraticaCentroLucro> findCPCLByContratoPratica(
			final ContratoPratica cp) {
		return dao.findByContratoPratica(cp);
	}

	/**
	 * Busca uma lista de entidades pelo ContratoPratica e NaturezaCentroLucro
	 * opcionais (diferente de obrigatorias).
	 * 
	 * @param cp
	 *            entidade populada com os valores do ContratoPratica.
	 * 
	 * @return lista de entidades que atendem ao criterio do ContratoPratica.
	 */
	public List<ContratoPraticaCentroLucro> findCPCLByContratoPraticaAndNaturezaOptional(
			final ContratoPratica cp) {
		return dao.findByContratoPraticaAndNaturezaOptional(cp);
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public ContratoPraticaCentroLucro findCPCLById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que ser� apagada.
	 * 
	 *            clientes
	 * @return true se removisdo com sucesso, caso contrario false.
	 */
	public Boolean removeCPCL(final ContratoPraticaCentroLucro entity) {

		ContratoPraticaCentroLucro maxClcp = dao
				.findMaxStartDateByContratoPraticaAndNatureza(entity
						.getContratoPratica(), entity.getCentroLucro()
						.getNaturezaCentroLucro());

		if (maxClcp == null
				|| maxClcp.getCodigoContratoPraticaCl().compareTo(
						entity.getCodigoContratoPraticaCl()) == 0) {

			dao.remove(entity);

			maxClcp = dao.findMaxStartDateByContratoPraticaAndNatureza(entity
					.getContratoPratica(), entity.getCentroLucro()
					.getNaturezaCentroLucro());
			if (maxClcp != null) {
				maxClcp.setDataFimVigencia(null);
				dao.update(maxClcp);
			}

			return true;

		} else {
			Messages.showError("removeCPCL",
					Constants.MSG_ERROR_REMOVE_CENTRO_LUCRO);
			return false;
		}

	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que ser� atualizada.
	 * 
	 */
	public void updateCPCL(final ContratoPraticaCentroLucro entity) {
		dao.update(entity);
	}

	/**
	 * Busca pelo ContratoPratica e pela Natureza.
	 * 
	 * @param cp
	 *            - ContratoPratica utilizado na busca.
	 * 
	 * @param natureza
	 *            - NaturezaCentroLucro utilizado na busca.
	 * 
	 * @return lista com todas as entidades ContratoPraticaCentroLucro
	 *         relacionadas com o ContratoPratica e Natureza
	 */
	public List<ContratoPraticaCentroLucro> findCPCLByContratoPraticaAndNatureza(
			final ContratoPratica cp, final NaturezaCentroLucro natureza) {
		return dao.findByContratoPraticaAndNatureza(cp, natureza);
	}

	/**
	 * Cria uma Lista de NaturezaContratoPraticaCLRow utilizada no view do
	 * ContratoPratica, referente a listagem dos ContratoPraticaCentroLucro.
	 * 
	 * @param cp
	 *            - ContratoPratica utilizado para montar a lista
	 * 
	 * @return lista de NaturezaContratoPraticaCLRow referente ao
	 *         contrato-pratica passado por parametro
	 */
	public List<NaturezaContratoPraticaCLRow> prepareNaturezaContratoPraticaCLList(
			final ContratoPratica cp) {

		// Lista utiliza para o retorno do metodo
		List<NaturezaContratoPraticaCLRow> ret = new ArrayList<NaturezaContratoPraticaCLRow>();
		// Map utilizado para verificar se uma natureza j� foi inserida na lista
		Map<Long, NaturezaContratoPraticaCLRow> naturezaMap = new HashMap<Long, NaturezaContratoPraticaCLRow>();
		// Lista com todas os CPCL associados ao Contrato-Pratica
		List<ContratoPraticaCentroLucro> cpclList = dao
				.findByContratoPratica(cp);
		// Variavel utilizada para cada elemento da lista
		// 'contratoPraticaCLList'
		NaturezaContratoPraticaCLRow naturezaCPCLRow = null;
		// Lista utilizada para cada naturezaCPCLRow
		List<ContratoPraticaCentroLucro> contratoPraticaCLList = null;

		// itera��o na lista de ContratoPraticaCentroLucro
		for (ContratoPraticaCentroLucro cpcl : cpclList) {
			// pega a natureza
			NaturezaCentroLucro naturezaCentroLucro = cpcl.getCentroLucro()
					.getNaturezaCentroLucro();

			// verifica se a natureza j� foi inserida na lista
			if (!naturezaMap.containsKey(naturezaCentroLucro
					.getCodigoNatureza())) {
				// cria a row
				naturezaCPCLRow = new NaturezaContratoPraticaCLRow();
				// cria a lista da row
				contratoPraticaCLList = new ArrayList<ContratoPraticaCentroLucro>();
				// Adiciona o ContratoPraticaCentroLucro na lista do row
				contratoPraticaCLList.add(cpcl);
				// seta a Lista de ContratoPraticaCentroLucro na row
				naturezaCPCLRow.setContratoPraticaCLList(contratoPraticaCLList);
				// seta a Natureza na row
				naturezaCPCLRow.setNatureza(naturezaCentroLucro);

				naturezaMap.put(naturezaCentroLucro.getCodigoNatureza(),
						naturezaCPCLRow);
				ret.add(naturezaCPCLRow);
				// se j� existe na lista insere o ContratoPraticaCentroLucro na
				// lista do row
			} else {
				naturezaCPCLRow = naturezaMap.get(naturezaCentroLucro
						.getCodigoNatureza());
				naturezaCPCLRow.getContratoPraticaCLList().add(cpcl);
			}

		}

		return ret;
	}

	/**
	 * Cria uma Lista de NaturezaContratoPraticaCLRow utilizada na aba Profit
	 * Center do ContratoPratica, referente a listagem dos
	 * ContratoPraticaCentroLucro. <br>
	 * Gera 3 listas: <br>
	 * 1 - lista de NaturezaContratoPraticaCLRow com as NaturezaCentroLucro
	 * Obrigatorias <br>
	 * 2 - lista de NaturezaCentroLucro nao obrigatorias (opcionais) <br>
	 * 3 - lista de ContratoPraticaCentroLucro das NaturezaCentroLucro nao
	 * obrigatorias
	 * 
	 * @param cp
	 *            - ContratoPratica utilizado para montar a lista
	 * 
	 * @return mapa com as 3 listas referente ao contrato-pratica passado por
	 *         parametro
	 */
	public Map<Integer, List> prepareNaturezaCPCLListMandatory(
			final ContratoPratica cp) {

		ContratoPratica contratoPratica = contratoPraticaService
				.findContratoPraticaById(cp.getCodigoContratoPratica());

		// Lista utilizadas para o retorno do metodo
		List<NaturezaContratoPraticaCLRow> naturezaCPCLRowMandatoryList = new ArrayList<NaturezaContratoPraticaCLRow>();
		List<NaturezaCentroLucro> naturezaCentroLucroOptionalList = new ArrayList<NaturezaCentroLucro>();
		List<ContratoPraticaCentroLucro> contratoPraticaCLOptionalList = new ArrayList<ContratoPraticaCentroLucro>();

		// Map utilizado para verificar se uma natureza já foi inserida na lista
		Map<Long, NaturezaContratoPraticaCLRow> naturezaCPCLRowMap = new HashMap<Long, NaturezaContratoPraticaCLRow>();

		// Variavel utilizada para cada elemento da lista
		// 'contratoPraticaCLList'
		NaturezaContratoPraticaCLRow naturezaCPCLRow = null;
		// Variavel (Lista) ContratoPraticaCentroLucro utilizada para cada
		// NaturezaContratoPraticaCLRow
		List<ContratoPraticaCentroLucro> contratoPraticaCLList = null;

		// Lista com todas os CPCL associados ao Contrato-Pratica
		List<ContratoPraticaCentroLucro> cpclList = dao
				.findByContratoPratica(contratoPratica);

		// itera��o na lista de ContratoPraticaCentroLucro
		for (ContratoPraticaCentroLucro cpcl : cpclList) {
			// pega a natureza
			NaturezaCentroLucro naturezaCentroLucro = cpcl.getCentroLucro()
					.getNaturezaCentroLucro();

			// verifica se a natureza já foi inserida na lista
			if (!naturezaCPCLRowMap.containsKey(naturezaCentroLucro
					.getCodigoNatureza())) {
				// se NaturezaCentroLucro for obrigatoria, insere na lista
				// naturezaCPCLRowList
				if (Constants.NATUREZA_TYPE_MANDATORY
						.equals(naturezaCentroLucro.getIndicadorTipo())) {
					// cria a lista da row
					contratoPraticaCLList = new ArrayList<ContratoPraticaCentroLucro>();
					// Adiciona o ContratoPraticaCentroLucro na lista do row
					contratoPraticaCLList.add(cpcl);
					// cria a row
					naturezaCPCLRow = new NaturezaContratoPraticaCLRow();
					// seta a Lista de ContratoPraticaCentroLucro na row
					naturezaCPCLRow
							.setContratoPraticaCLList(contratoPraticaCLList);
					// seta a Natureza na row
					naturezaCPCLRow.setNatureza(naturezaCentroLucro);

					naturezaCPCLRowMap.put(
							naturezaCentroLucro.getCodigoNatureza(),
							naturezaCPCLRow);
					naturezaCPCLRowMandatoryList.add(naturezaCPCLRow);
					// senao, insere na naturezaCentroLucroOptionalList e os
					// CPCL na contratoPraticaCLOptionalList
				} else {
					naturezaCPCLRowMap.put(
							naturezaCentroLucro.getCodigoNatureza(), null);
					naturezaCentroLucroOptionalList.add(naturezaCentroLucro);
					contratoPraticaCLOptionalList.add(cpcl);
				}
				// se j� existe na lista insere o ContratoPraticaCentroLucro na
				// lista do row
			} else {
				// se NaturezaCentroLucro for obrigatoria, insere na lista
				// contratoPraticaCLList
				if (Constants.NATUREZA_TYPE_MANDATORY
						.equals(naturezaCentroLucro.getIndicadorTipo())) {
					naturezaCPCLRow = naturezaCPCLRowMap
							.get(naturezaCentroLucro.getCodigoNatureza());
					naturezaCPCLRow.getContratoPraticaCLList().add(cpcl);
					// senao, insere os
					// CPCL na contratoPraticaCLOptionalList
				} else {
					contratoPraticaCLOptionalList.add(cpcl);
				}
			}
		}

		// ate aqui, as listas (NaturezaCentroLucro) foram carregadas com base
		// nos CPCL registrados. Busca todas as NaturezaCentroLucro, verifica se
		// j� existe no map, caso n�o existir, adiciona nas listas
		List<NaturezaCentroLucro> naturezaCentroLucroAllList = naturezaCentroLucroService
				.findNaturezaCentroLucroAll();
		for (NaturezaCentroLucro ncl : naturezaCentroLucroAllList) {
			if (!naturezaCPCLRowMap.containsKey(ncl.getCodigoNatureza())) {
				if ((Constants.NATUREZA_TYPE_MANDATORY).equals(ncl
						.getIndicadorTipo())) {
					// cria a row
					naturezaCPCLRow = new NaturezaContratoPraticaCLRow();
					// seta a Lista de ContratoPraticaCentroLucro na row
					naturezaCPCLRow
							.setContratoPraticaCLList(new ArrayList<ContratoPraticaCentroLucro>());
					// seta a Natureza na row
					naturezaCPCLRow.setNatureza(ncl);
					naturezaCPCLRowMandatoryList.add(naturezaCPCLRow);
				} else {
					naturezaCentroLucroOptionalList.add(ncl);
				}
			}
		}

		Map<Integer, List> naturezaCPCLListsMap = new HashMap<Integer, List>();
		Integer cont = Integer.valueOf(0);
		naturezaCPCLListsMap.put(++cont, naturezaCPCLRowMandatoryList);
		naturezaCPCLListsMap.put(++cont, naturezaCentroLucroOptionalList);
		naturezaCPCLListsMap.put(++cont, contratoPraticaCLOptionalList);

		return naturezaCPCLListsMap;
	}

	/**
	 * Busca pelo ContratoPratica e pela Natureza e pela Data de vigencia.
	 * 
	 * @param cp
	 *            - ContratoPratica utilizado na busca.
	 * 
	 * @param natureza
	 *            - NaturezaCentroLucro utilizado na busca.
	 * 
	 * @param dataMes
	 *            - data da vigencia.
	 * 
	 * @return a entidade que atende aos criterios do filtro
	 */
	@Override
	public ContratoPraticaCentroLucro findCPCLByContPratAndNatAndDate(
			final ContratoPratica cp, final NaturezaCentroLucro natureza,
			final Date dataMes) {
		return dao.findByContPratAndNatAndDate(cp, natureza, dataMes);
	}

	/**
	 * Checa se um {@link ContratoPratica} possui mais do que um CentroLucro
	 * vigente.
	 * 
	 * @param contratoPratica
	 *            {@link ContratoPratica}
	 * @param natureza
	 *            {@link NaturezaCentroLucro}
	 * @throws MoreThanOneActiveEntityException
	 */
	@Override
	public void checkMoreThanOneCentroLucroPresent(
			final ContratoPratica contratoPratica,
			final NaturezaCentroLucro natureza) {
		List<ContratoPraticaCentroLucro> contratoPraticaCentroLucros = this
				.findCPCLByContratoPraticaAndNatureza(contratoPratica, natureza);

		int count = 0;
		for (ContratoPraticaCentroLucro contratoPraticaCentroLucro : contratoPraticaCentroLucros) {
			if (contratoPraticaCentroLucro.getDataFimVigencia() == null) {
				count++;
			}
			if (count > 1) {
				String error = new StringBuilder(
						"More than one Centro de Lucro is ative to CLOB: ")
						.append(contratoPratica.getNomeContratoPratica())
						.append(" in Natureza: ")
						.append(natureza.getNomeNatureza()).toString();
				log.warn(error);
			}
		}
	}

	/**
	 * Busca o {@link ContratoPraticaCentroLucro} vigente pelo ContratoPratica e
	 * pela Natureza.
	 * 
	 * @param contratoPratica
	 *            - ContratoPratica utilizado na busca.
	 * 
	 * @param natureza
	 *            - NaturezaCentroLucro utilizado na busca.
	 * 
	 * @return ContratoPraticaCentroLucro vigente relacionadas com o
	 *         ContratoPratica e Natureza
	 * @throws MoreThanOneActiveEntityException
	 */
	@Override
	public ContratoPraticaCentroLucro findPresentByContratoPraticaAndNatureza(
			final ContratoPratica contratoPratica,
			final NaturezaCentroLucro natureza) {

		ContratoPraticaCentroLucro contratoPraticaCentroLucroVigente = new ContratoPraticaCentroLucro();

		// lanca um erro caso exista mais de um Centro de Lucro vigente para a
		// mesma natureza de um CLOB
		checkMoreThanOneCentroLucroPresent(contratoPratica, natureza);

		List<ContratoPraticaCentroLucro> contratoPraticaCentroLucros = this
				.findCPCLByContratoPraticaAndNatureza(contratoPratica, natureza);

		for (ContratoPraticaCentroLucro contratoPraticaCentroLucro : contratoPraticaCentroLucros) {
			if (contratoPraticaCentroLucro.getDataFimVigencia() == null) {
				contratoPraticaCentroLucroVigente = contratoPraticaCentroLucro;
			}
		}

		return contratoPraticaCentroLucroVigente;
	}

}