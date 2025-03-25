package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICentroLucroService;
import com.ciandt.pms.business.service.IConvergenciaService;
import com.ciandt.pms.business.service.IGrupoCustoAreaOrcamentariaService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.business.service.IParametroService;
import com.ciandt.pms.business.service.IVwPmsGrupoContaContabilService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.message.CostCenterMessage;
import com.ciandt.pms.message.dto.CostCenterDTO;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Convergencia;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.GrupoCustoAud;
import com.ciandt.pms.model.GrupoCustoCentroLucro;
import com.ciandt.pms.model.GrupoCustoPeriodo;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.Parametro;
import com.ciandt.pms.model.PessoaGrupoCusto;
import com.ciandt.pms.model.VwMegaCCusto;
import com.ciandt.pms.model.VwPmsGrupoContaContabil;
import com.ciandt.pms.model.vo.NaturezaRow;
import com.ciandt.pms.persistence.dao.IGrupoCustoDao;
import com.ciandt.pms.persistence.dao.IGrupoCustoPeriodoDao;
import com.ciandt.pms.persistence.dao.jpa.GrupoCustoAreaOrcamentaria;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.MailSenderUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * A classe GrupoCustoService proporciona as funcionalidades da camada de
 * servi�o referente a entidade GrupoCusto.
 * 
 * @since 15/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class GrupoCustoService implements IGrupoCustoService {

    /** Instancia do GrupoCustoDao. */
    @Autowired
    private IGrupoCustoDao grupoCustoDao;

    /** Instancia do servico. */
    @Autowired
    private INaturezaCentroLucroService naturezaService;

    /** Instancia do servico CentroLucro. */
    @Autowired
    private ICentroLucroService centroLucroService;

	/** Instancia do servico. */
	@Autowired
	private IConvergenciaService convergenciaService;

	/** Instancia do GrupoCustoPeriodoDao. */
	@Autowired
	private IGrupoCustoPeriodoDao grupoCustoPeriodoDao;


	@Autowired
	private IParametroService parametroService;

	private MailSenderUtil mailSender;

	@Autowired
	private IGrupoCustoAreaOrcamentariaService grupoCustoAreaOrcamentariaService;

	@Autowired
	private IVwPmsGrupoContaContabilService pmsGrupoCOntabilService;

	@Autowired
	private CostCenterMessage costCenterMessage;

	@Autowired
	private GrupoCustoStatusService grupoCustoStatusService;

	/**
	 * Prepara os dados para a cria��o de um grupo de custo.
	 * 
	 * @return retorna uma lista com as naturezas.
	 */
	public List<NaturezaRow> prepareCreateGrupoCusto() {
		Map<String, Long> centroLucroMap;
		List<String> centroLucroList;
		NaturezaRow naturezaRow;
		List<NaturezaRow> resultList = new ArrayList<>();

        List<NaturezaCentroLucro> naturezaList = naturezaService
                .findNaturezaCentroLucroAll();

        for (NaturezaCentroLucro natureza : naturezaList) {
            centroLucroMap = new HashMap<>();
            centroLucroList = new ArrayList<>();
            naturezaRow = new NaturezaRow();

            for (CentroLucro centroLucro : natureza.getCentroLucros()) {
                centroLucroMap.put(centroLucro.getNomeCentroLucro(),
                        centroLucro.getCodigoCentroLucro());
                centroLucroList.add(centroLucro.getNomeCentroLucro());
            }

            naturezaRow.setNatureza(natureza);
            naturezaRow.setCentroLucroList(centroLucroList);
            naturezaRow.setCentroLucroMap(centroLucroMap);

            resultList.add(naturezaRow);
        }

        return resultList;
    }

    /**
	 * Cria um {@link GrupoCusto}.
	 *
	 */
	@Transactional
	public void create(final GrupoCusto grupoCusto) {
		grupoCustoDao.create(grupoCusto);
	}

	/**
	 * Cria um CostCenter ({@link GrupoCusto}) juntamente com um registro de
	 * {@link Convergencia}.
	 * 
	 * @param centroCustoMega
	 *            {@link VwMegaCCusto}
	 * @return grupo de custo criado
	 */
	@Override
	@Transactional
	public GrupoCusto createCostCenter(final VwMegaCCusto centroCustoMega) {
		GrupoCusto grupoCusto = centroCustoMega.toGrupoCusto();
		grupoCusto.setEmpresa(centroCustoMega.getEmpresa());
		grupoCusto.setIndicadorAtivo(Constants.ACTIVE);
		grupoCustoDao.create(grupoCusto);

		Convergencia convergencia = centroCustoMega.toConvergencia();
		convergencia.setGrupoCusto(grupoCusto);
		convergenciaService.create(convergencia);

		return grupoCusto;
	}

	public void sendEmailToControladoria(String subject, String message) {
		Parametro controllershipEmails = parametroService
				.findParametroByNomeParametro(Constants.CONTROLLERSHIP_EMAILS_PARAMETER);
		String mail = controllershipEmails.getTextoParametro();
		mailSender.sendHtmlMail(mail, subject, message);
	}

	private void sendEmailToControladoriaAndCopyMail(String subject, String message, String ccMail) {
		Parametro controllershipEmails = parametroService.findParametroByNomeParametro(Constants.CONTROLLERSHIP_EMAILS_PARAMETER);
		String mail = controllershipEmails.getTextoParametro();

		mailSender.sendMailAttachment(mail, subject, mail, ccMail, message, true, null,null);
	}

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * @param naturezaRowList
     *            lista com as naturezas
     * @param startDate
     *            data inicio da vigencia
     */
    public void createGrupoCusto(final GrupoCusto entity,
            final List<NaturezaRow> naturezaRowList, final Date startDate) {

        GrupoCustoCentroLucro gccl;
        CentroLucro cl;
        List<GrupoCustoCentroLucro> associatoinList = new ArrayList<>(
                0);

        GrupoCustoPeriodo periodo = new GrupoCustoPeriodo();
        periodo.setDataInicio(startDate);
        periodo.setGrupoCustoCentroLucros(associatoinList);
        periodo.setGrupoCusto(entity);

        for (NaturezaRow naturezaRow : naturezaRowList) {
            gccl = new GrupoCustoCentroLucro();
            cl = null;

            Long codCentroLucro = naturezaRow.getCentroLucroMap().get(
                    naturezaRow.getCentroLucroName());

            if (codCentroLucro != null) {
                cl = centroLucroService.findCentroLucroById(codCentroLucro);
            }

            gccl.setNaturezaCentroLucro(naturezaRow.getNatureza());
            gccl.setCentroLucro(cl);
            gccl.setGrupoCustoPeriodo(periodo);
            associatoinList.add(gccl);
        }

        List<GrupoCustoPeriodo> periodoList = new ArrayList<>();
        periodoList.add(periodo);

        entity.setGrupoCustoPeriodos(periodoList);
        entity.setIndicadorAtivo(Constants.ACTIVE);

        grupoCustoDao.create(entity);
    }

	/**
	 * Prepara os dados para a edicao de um GrupoCusto.
	 * 
	 * @param grupoCusto
	 *            - GrupoCusto que sera atualizado
	 */
	public void prepareUpdateGrupoCusto(final GrupoCusto grupoCusto) {
		// recupera a lista de GrupoCustoPeriodo do GrupoCusto corrente
		List<GrupoCustoPeriodo> grupoCustoPeriodoList = grupoCusto.getGrupoCustoPeriodos();

		// itera a lista de GrupoCustoPeriodo
		for (GrupoCustoPeriodo grupoCustoPeriodo : grupoCustoPeriodoList) {
			// recupera a lista de GrupoCustoCentroLucro do GrupoCustoPeriodo
			// corrente
			List<GrupoCustoCentroLucro> grupoCustoCentroLucroList = grupoCustoPeriodo
					.getGrupoCustoCentroLucros();

			// itera a lista de GrupoCustoCentroLucro
			for (GrupoCustoCentroLucro grupoCustoCentroLucro : grupoCustoCentroLucroList) {
				// recupera a NaturezaCentroLucro do GrupoCustoCentroLucro
				// corrente
				NaturezaCentroLucro naturezaCentroLucro = grupoCustoCentroLucro.getNaturezaCentroLucro();

				// recupera a lista de CentroLucro da NaturezaCentroLucro
				// corrente
				List<CentroLucro> centroLucroList = naturezaCentroLucro.getCentroLucros();

				// para cada CentroLucro da lista, atribui o nomeCentroLucro na
				// lista fake (transient)
				List<String> nomeCentroLucroList = grupoCustoCentroLucro.getNomeCentroLucroList();

				for (CentroLucro centroLucro : centroLucroList) {
					if(centroLucro.getIndicadorAtivo().equalsIgnoreCase(Constants.ACTIVE)) {
						nomeCentroLucroList.add(centroLucro.getNomeCentroLucro());
					}
				}

				// recupera o CentroLucro do GrupoCustoPeriodo corrente e
				// atribui o nomeCentroLucro (transient)
				CentroLucro centroLucro = grupoCustoCentroLucro
						.getCentroLucro();
				if (centroLucro != null) {
					grupoCustoCentroLucro.setNomeCentroLucro(centroLucro
							.getNomeCentroLucro());
				}
			}

			List<NaturezaCentroLucro> naturezaList = naturezaService
					.findNaturezaAllNotInGrupoCusto(grupoCustoPeriodo
							.getCodigoGcPeriodo());
			// itera a lista de NaturezaCentroLucro restantes que nao estao
			// relacionada com o GrupoCustoCentroLucro

			for (NaturezaCentroLucro natureza : naturezaList) {

				boolean naturezaJaNaLista = false;
				for (GrupoCustoCentroLucro gccl : grupoCustoCentroLucroList) {
					if (gccl.getNaturezaCentroLucro().equals(natureza)) {
						naturezaJaNaLista = true;
						break;
					}
				}

				if (!naturezaJaNaLista) {
					GrupoCustoCentroLucro gccl = new GrupoCustoCentroLucro();
					gccl.setGrupoCustoPeriodo(grupoCustoPeriodo);
					gccl.setNaturezaCentroLucro(natureza);

					// recupera a lista de CentroLucro da NaturezaCentroLucro
					// corrente
					List<CentroLucro> centroLucroList = natureza.getCentroLucros();

					// para cada CentroLucro da lista, atribui o nomeCentroLucro na
					// lista fake (transient)
					List<String> nomeCentroLucroList = gccl.getNomeCentroLucroList();

					for (CentroLucro centroLucro : centroLucroList) {
						if (centroLucro.getIndicadorAtivo().equalsIgnoreCase(Constants.ACTIVE)) {
							nomeCentroLucroList.add(centroLucro.getNomeCentroLucro());
						}
					}

					// adiciona o GrupoCustoCentroLucro correnta na lista
					grupoCustoCentroLucroList.add(gccl);
				}
			}
		}
	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param grupoCusto
	 *            que ser� atualizada.
	 * @throws IntegrityConstraintException
	 *             lan�a exce��o caso o GrupoCusto possua GrupoCustoPeriodo
	 *             associados e tente inativ�-lo
	 * 
	 */
	@Override
	public void updateGrupoCusto(final GrupoCusto grupoCusto) throws IntegrityConstraintException {

		checkGrupoCustoAtivo(grupoCusto);

		updateProfitCenterFromPeriod(grupoCusto);

		updateForRequestIntegrationJob(grupoCusto);

		sendEmailWhenStatusRequestInactivation(grupoCusto);

		grupoCustoDao.update(grupoCusto);

	}

	private void sendEmailWhenStatusRequestInactivation(GrupoCusto grupoCusto) {
		if (grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.REQUEST_INACTIVATION)) {
			this.sendEmailToControladoriaAndCopyMail(
			BundleUtil.getBundle(Constants.EMAIL_MSG_ASSUNTO_REQ_INATIVACAO_GRUPO_CUSTO),
			BundleUtil.getBundle(Constants.EMAIL_MSG_REQ_INATIVACAO_GRUPO_CUSTO,
					grupoCusto.getNomeGrupoCusto(), LoginUtil.getLoggedUser().getCodigoLogin()),
					grupoCusto.getGerenteAprovador().getTextoEmail());
       }
	}

	private void updateProfitCenterFromPeriod(GrupoCusto grupoCusto) {
		// itera as entidades GrupoCustoPeriodo e GrupoCustoCentroLucro para
		// atribuir o CentroLucro corretamente
		List<GrupoCustoPeriodo> grupoCustoPeriodoList = grupoCusto.getGrupoCustoPeriodos();

		for (GrupoCustoPeriodo gcPeriodo : grupoCustoPeriodoList) {
			List<GrupoCustoCentroLucro> gcclList = gcPeriodo.getGrupoCustoCentroLucros();

			for (GrupoCustoCentroLucro gccl : gcclList) {
				// recupera o nome do CentroLucro que vem da tela (campo
				// transient)
				Map<String, CentroLucro> cLucroMap = indexaMapLucroNatureza(gccl.getNaturezaCentroLucro());
				String nomeCentroLucro = gccl.getNomeCentroLucro();
				// se o nome nao for vazio, atribui o CentroLucro ao objeto
				if (!StringUtils.isEmpty(nomeCentroLucro)) {
					CentroLucro centroLucro = cLucroMap.get(nomeCentroLucro);
					gccl.setCentroLucro(centroLucro);
				} else {
					gccl.setCentroLucro(null);
				}

				// limpa propriedade transient
				gccl.getNomeCentroLucroList().clear();
				gccl.setNomeCentroLucro(null);
			}
		}
	}

	private void updateForRequestIntegrationJob(GrupoCusto grupoCusto) {
		grupoCusto.setRequestIntegration(Constants.SIM);

		if (grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.NEW_INACTIVE) && grupoCusto.getDataInativacao().after(new Date())) {
			grupoCusto.setGrupoCustoStatus(grupoCustoStatusService.findBySiglaStatusGrupoCusto(Constants.INTEGRATING_INACTIVATION));
		}
	}

	public List<CostCenterDTO> findAllWithExternalRestRequest() {
		return costCenterMessage.getForSelection();
	}

	private Map<String, CentroLucro> indexaMapLucroNatureza(NaturezaCentroLucro ncl) {
		// popula um Map auxiliar com os CentroLucro
		Map<String, CentroLucro> centroLucroMap = new HashMap<>();
		List<CentroLucro> centroLucroList = centroLucroService
				.findCentroLucroByNatureza(ncl);
		for (CentroLucro centroLucro : centroLucroList) {
			if (centroLucro.getIndicadorAtivo().equalsIgnoreCase(Constants.ACTIVE)) {
				centroLucroMap.put(centroLucro.getNomeCentroLucro(), centroLucro);
			}
		}
		return centroLucroMap;
	}

	public void checkGrupoCustoAreaOrcamentaria(GrupoCusto entity) {

		Date today = new Date();
		List<GrupoCustoAreaOrcamentaria> gcaoList = grupoCustoAreaOrcamentariaService
				.findByGrupoCustoId(entity.getCodigoGrupoCusto());
		if(gcaoList != null){
			for (GrupoCustoAreaOrcamentaria grupoCustoAreaOrcamentaria : gcaoList) {
				if (grupoCustoAreaOrcamentaria.getDataFim() == null || grupoCustoAreaOrcamentaria.getDataFim().after(today)) {
					entity.setHasBudgetArea(Boolean.TRUE);
					entity.setErrorMessageBudgetAreaConstraintContentFirstText(Constants.DEFAULT_MSG_ERROR_INTEGRITY_BUDGET_AREA_FIRST_TEXT);
					entity.setErrorMessageBudgetAreaConstraintContentSecondText(Constants.DEFAULT_MSG_ERROR_INTEGRITY_BUDGET_AREA_SECOND_TEXT);
					entity.setErrorMessageBudgetAreaConstraintContentThirdText(Constants.DEFAULT_MSG_ERROR_INTEGRITY_BUDGET_AREA_THIRD_TEXT);
					entity.setErrorMessageBudgetAreaConstraint(Boolean.TRUE);
				}
			}
		}
	}


	private void checkGrupoCustoAtivo(final GrupoCusto entity)
			throws IntegrityConstraintException {
		// verifica se a entidade da tela est� sendo inativada
		if (entity.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.REQUEST_INACTIVATION) ||
				entity.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.INTEGRATING_INACTIVATION)) {

			// busca a entidade do banco por causa da sess�o de conex�o
			// (hibernate)
			GrupoCusto grupoCusto = findGrupoCustoById(entity
					.getCodigoGrupoCusto());

			// se sim, usa a entidade do banco para verificar se existem filhos,
			// se sim lan�a exce��o
			Set<PessoaGrupoCusto> pessoaGrupoCustos = grupoCusto.getPessoaGrupoCustos();
			for (PessoaGrupoCusto pessoaGrupoCusto : pessoaGrupoCustos) {
				if (pessoaGrupoCusto.getDataFim() == null
						&& pessoaGrupoCusto.getGrupoCusto()
								.getCodigoGrupoCusto()
								.equals(entity.getCodigoGrupoCusto())
						&& pessoaGrupoCusto.getPessoa().getDataRescisao() == null) {
					throw new IntegrityConstraintException(
							Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_INACTIVE);	
				}
			}
		}
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param gc
	 *            que ser� apagada.
	 */
	public void removeGrupoCusto(final GrupoCusto gc)
			throws IntegrityConstraintException {
		String clobNames = getCLobAssociated(gc);

		// verifica se existem Clobs relacionados, se sim lanca
		// excecao
		if (clobNames.length() > 0) {
			throw new IntegrityConstraintException(clobNames);
		} else {
			Convergencia cv = convergenciaService.findByCostGroupId(gc
					.getCodigoGrupoCusto());
			convergenciaService.delete(cv);
			List<GrupoCustoPeriodo> gcpList = gc.getGrupoCustoPeriodos();
			for (GrupoCustoPeriodo gcp : gcpList) {
				grupoCustoPeriodoDao.remove(gcp);
			}
			gc.setIndicadorDeletado(Constants.SIM);
			grupoCustoDao.remove(gc);
		}
	}

	/**
	 * Verifica se existe algum Clob associado
	 * 
	 * @param gc
	 *            - grupo de custo
	 * @return nomes do c-lobs associados separados por virgula
	 */
	private String getCLobAssociated(GrupoCusto gc) {
		StringBuilder bd = new StringBuilder();
		List<Convergencia> list = convergenciaService.findByCLCostGroupId(gc
				.getCodigoGrupoCusto());
		if (!list.isEmpty()) {
			for (Convergencia convergencia : list) {
				String nomeContratoPratica = convergencia.getContratoPratica()
						.getNomeContratoPratica();
				bd.append(nomeContratoPratica).append(", ");
			}
			int length = bd.length();
			bd.delete(length - 2, length).append(" CLob");
		}
		return bd.toString();
	}

	/**
	 * Marca a entidade como deletada logicamente.
	 * 
	 * @param gc
	 *            que sera marcada como deletada logicamente.
	 * @throws IntegrityConstraintException
	 *             - tratamento erro de referencia de integridade
	 * 
	 */
	public void deleteLogico(final GrupoCusto gc)
			throws IntegrityConstraintException {
		GrupoCusto costCenter = this.findGrupoCustoById(gc.getCodigoGrupoCusto());
		String clobNames = getCLobAssociated(costCenter);
		// verifica se existem Clobs relacionados, se sim lanca
		// excecao
		if (clobNames.length() > 0) {
			throw new IntegrityConstraintException(clobNames);
		} else {
			Convergencia cv = convergenciaService.findByCostGroupId(costCenter
					.getCodigoGrupoCusto());
			convergenciaService.delete(cv);

			costCenter.setIndicadorDeletado(Constants.SIM);
			grupoCustoDao.update(costCenter);
		}
	}

    /**
     * Busca e entidade pelo id.
     * 
     * @param id
     *            - id da entidade.
     * 
     * @return retorna a entidade com o id passado por parametro. Caso n�o
     *         exista retorna null.
     */
    public GrupoCusto findGrupoCustoById(final Long id) {
        return grupoCustoDao.findById(id);
    }

    /**
     * Busca e entidade pelo acronym.
     * 
     * @param siglaGrupoCusto
     *            sigla do GrupoCusto
     * 
     * @return retorna a entidade
     */
    public GrupoCusto findGrupoCustoByAcronym(final String siglaGrupoCusto) {
        return grupoCustoDao.findByAcronym(siglaGrupoCusto);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<GrupoCusto> findGrupoCustoByFilter(final GrupoCusto filter) {
        return grupoCustoDao.findByFilter(filter);
    }

	  public List<GrupoCusto> findByAproverRescinded() {
		  return grupoCustoDao.findByAproverRescinded();
	  }

    /**
     * Busca por todos os GrupoCusto.
     * 
     * @return uma lista com todos os ContratoPraticas.
     */
    public List<GrupoCusto> findGrupoCustoAllActive() {
        return grupoCustoDao.findAllActive();
    }

	public List<GrupoCusto> findGrupoCustoActiveProdCom() {
		return grupoCustoDao.findActiveTypeProdCom();
	}

	public Long findTipoAreaByCentroCusto(Long grupoCustoId) {
		VwPmsGrupoContaContabil grupoCusto = pmsGrupoCOntabilService.findByCodigoGrupoCusto(grupoCustoId);
		if(grupoCusto != null){
			return grupoCusto.getCodigoTipoArea();
		}
		return null;
	}

	/**
	 * Busca por todos os GrupoCusto.
	 *
	 * @return uma lista com todos os GrupoCusto.
	 */
	public List<GrupoCusto> findGrupoCustoAll() {
		return grupoCustoDao.findAll();
	}


	/**
	 * Busca por todos os GrupoCusto ativos.
	 *
	 * @return uma lista com todos os GrupoCusto ativos mas s� o codigo e nome
	 *         est�o preenchidos.
	 */
    @Override
    public List<GrupoCusto> findAllActiveReturnCodigoAndNomeGrupoCusto() {
		return grupoCustoDao.findAllActiveReturnCodigoAndNomeGrupoCusto();
	}

	@Override
	public List<GrupoCustoAud> findHistoryByCodigo(Long codigoGrupoCusto) {
		return grupoCustoDao.findHistoryByCodigo(codigoGrupoCusto);
	}

    /**
     * Ordena uma lista de GrupoCustoPeriodo.
     *
     * @param grupoCustoPeriodoList
     *            - lista de GrupoCustoPeriodo
     */
    public void orderGrupoCustoPeriodoList(
            final List<GrupoCustoPeriodo> grupoCustoPeriodoList) {
        Collections.sort(grupoCustoPeriodoList,
                new Comparator<GrupoCustoPeriodo>() {
                    public int compare(final GrupoCustoPeriodo gc1,
                            final GrupoCustoPeriodo gc2) {
                        return gc1.getDataInicio().compareTo(
                                gc2.getDataInicio());
                    }
                });
    }

    /**
     * Ordena uma lista de GrupoCustoCentroLucro.
     *
     * @param grupoCustoCentroLucroList
     *            - lista de GrupoCustoCentroLucro
     */
    public void orderGrupoCustoCentroLucroList(
            final List<GrupoCustoCentroLucro> grupoCustoCentroLucroList) {
        Collections.sort(grupoCustoCentroLucroList,
                new Comparator<GrupoCustoCentroLucro>() {
                    public int compare(final GrupoCustoCentroLucro gccl1,
                            final GrupoCustoCentroLucro gccl2) {
                        return gccl1.getNaturezaCentroLucro().getNomeNatureza()
                                .compareTo(
                                        gccl2.getNaturezaCentroLucro()
                                                .getNomeNatureza());
                    }
                });
    }

	/**
	 * Ordena a hierarquia de entidades (listas GrupoCustoPeriodo e suas
	 * respectivas listas GrupoCustoCentroLucro) de um GrupoCusto.
	 *
	 * @param grupoCusto
	 *            - GrupoCusto com as listas a serem ordenadas
	 */
	public void orderGrupoCustoHierarchyList(final GrupoCusto grupoCusto) {
		List<GrupoCustoPeriodo> grupoCustoPeriodoList = grupoCusto
				.getGrupoCustoPeriodos();
		this.orderGrupoCustoPeriodoList(grupoCustoPeriodoList);
		for (GrupoCustoPeriodo grupoCustoPeriodo : grupoCustoPeriodoList) {
			this.orderGrupoCustoCentroLucroList(grupoCustoPeriodo
					.getGrupoCustoCentroLucros());
		}
	}

	/*
	 * @see com.ciandt.pms.business.service.IGrupoCustoService#
	 * findGrupoCustoByIdWithPeriodos(java.lang.Long)
	 */

	@Override
	public GrupoCusto findGrupoCustoByIdWithPeriodos(Long codigoGrupoCusto) {
		return grupoCustoDao.findGrupoCustoByIdWithPeriodos(codigoGrupoCusto);
	}
	/*
	 * @see com.ciandt.pms.business.service.IGrupoCustoService#findGrupoCustoAllActiveAndEstrOrgan(java.lang.String)
	 */

	@Override
	public List<GrupoCusto> findGrupoCustoAllActiveAndEstrOrgan(
			String sgEstruturaOrganizacional) {
		return grupoCustoDao.findGrupoCustoAllActiveAndEstrOrgan(sgEstruturaOrganizacional);
	}
	public void setMailSender(final MailSenderUtil mailSender) {
		this.mailSender = mailSender;
	}

	public MailSenderUtil getMailSender() {
		return mailSender;
	}

	@Override
	public List<GrupoCusto> findCostCentersByCostCenterHierarchy(final Long code) throws BusinessException {
		List<GrupoCusto> costCenters = grupoCustoDao.findByCostCenterHierarchy(code);

		if(costCenters == null || costCenters.isEmpty())
			throw new BusinessException("Cost Centers not found.");

		return costCenters;
	}
}
