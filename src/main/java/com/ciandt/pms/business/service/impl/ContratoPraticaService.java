/**
 * Classe ContratoPraticaService - Responsavel por implementar os metodos
 * da camada de servico referentes a entidade ContratoPratica
 */
package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICentroLucroService;
import com.ciandt.pms.business.service.IContratoPraticaCentroLucroService;
import com.ciandt.pms.business.service.IContratoPraticaGrupoCustoService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IConvergenciaService;
import com.ciandt.pms.business.service.ICpraticaDfiscalService;
import com.ciandt.pms.business.service.IDealFiscalService;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.business.service.IParametroService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IReceitaService;
import com.ciandt.pms.control.jsf.ContratoPraticaController;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.csv.util.CsvUtil;
import com.ciandt.pms.enums.ContratoPraticaStatus;
import com.ciandt.pms.enums.NaturezaCentroLucroSigla;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.exception.MoreThanOneActiveEntityException;
import com.ciandt.pms.exception.NullNotSupportedException;
import com.ciandt.pms.message.ContractLobMessage;
import com.ciandt.pms.message.dto.ContractLobDTO;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaAud;
import com.ciandt.pms.model.ContratoPraticaCentroLucro;
import com.ciandt.pms.model.ContratoPraticaGrupoCusto;
import com.ciandt.pms.model.Convergencia;
import com.ciandt.pms.model.CpraticaDfiscal;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.Parametro;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.model.UploadManagers;
import com.ciandt.pms.model.vo.MapaProspectComAlocacaoResultsetVO;
import com.ciandt.pms.persistence.dao.IContratoPraticaCentroLucroDao;
import com.ciandt.pms.persistence.dao.IContratoPraticaDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.MailSenderUtil;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * A classe ContratoPraticaService proporciona as funcionalidades de servi�o
 * para a entidade Contrato Pratica.
 *
 * @since 13/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 */
@Service
public class ContratoPraticaService implements IContratoPraticaService {

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private IContratoPraticaDao contratoPraticaDao;

    /**
     * Instancia do DAO da entidade ContratoPraticaCentroLucroService.
     */
    @Autowired
    private IContratoPraticaCentroLucroDao cpclDao;

    /**
     * Instancia do Servico da entidade NaturezaCentroLucro.
     */
    @Autowired
    private INaturezaCentroLucroService naturezaCentroLucroService;

    /**
     * Instancia do servico da entidade ContratoPraticaCentroLucro.
     */
    @Autowired
    private IContratoPraticaCentroLucroService cpclService;

    /**
     * Instancia do servico da entidade CpraticaDfiscal.
     */
    @Autowired
    private ICpraticaDfiscalService cpraticaDfiscalService;

    /**
     * Instancia do servico da entidade CpraticaDfiscal.
     */
    @Autowired
    private IConvergenciaService convergenciaService;

    /**
     * Arquivo de configuracoes.
     */
    @Autowired
    private Properties systemProperties;

    /**
     * Instancia do servico da entidade {@link CentroLucro}.
     */
    @Autowired
    private ICentroLucroService centroLucroService;

    /**
     * Instancia do servico da entidade {@link ContratoPraticaGrupoCusto}.
     */
    @Autowired
    private IContratoPraticaGrupoCustoService contratoPraticaGrupoCustoService;

    /**
     * Instancia do servico da entidade mapaAlocacaoService.
     */
    @Autowired
    private IMapaAlocacaoService mapaAlocacaoService;

    @Autowired
    private IDealFiscalService dealFiscalService;

    @Autowired
    private IReceitaService receitaService;

    @Autowired
    private IParametroService parametroService;

    @Autowired
    private ContractLobMessage message;

    /**
     * Instancia do servico Pessoa.
     */
    @Autowired
    private IPessoaService pessoaService;

    /**
     * outcome tela de configure da entidade.
     */
    private static final String PADRAO_ARQUIVO_CSV = "Padrão OpenOffice";

    /*********** OUTROS **************************/

    /**
     * Intancia de mailSender.
     */
    private MailSenderUtil mailSender;

    public static final String MSG_ERROR_CREATE_PROFIT_CENTER = "_nls.contrato_pratica.msg.error.create.profit_center";

    public static final String MSG_ERROR_SAVE_BUSINESS_MANAGER_UPDATE = "_nls.contrato_pratica.msg.error.update.business_manager";

	/**
	 * Retorna todas as entidades.
	 *
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<ContratoPratica> findContratoPraticaAll() {
		return contratoPraticaDao.findAll();
	}

    public List<ContratoPratica> findContratoPraticaAllForComboBox() {
        return contratoPraticaDao.findAllForComboBox();
    }

    public List<ContratoPratica> findAllCompleteForCombobox() {
        return contratoPraticaDao.findAllCompleteForCombobox();
    }

    /**
     * Retorna todas as entidades com estado igual a 'Complete'.
     *
     * @return retorna uma lista com todas as entidades.
     */
    public List<ContratoPratica> findContratoPraticaAllComplete() {
        return contratoPraticaDao.findAllComplete();
    }

    /**
     * Busca uma entidade pelo id.
     *
     * @param id da entidade.
     * @return entidade com o id passado por parametro.
     */
    public ContratoPratica findContratoPraticaById(final Long id) {
        return contratoPraticaDao.findById(id);
    }

    /**
     * Insere uma entidade.
     *
     * @param entity entidade a ser inserida.
     * @throws IntegrityConstraintException - Excecao indicando violacao de Constraint
     */
    @Override
    public void createContratoPratica(final ContratoPratica entity)
            throws IntegrityConstraintException {
        if (!this.existContratoPratica(entity)) {
            entity.setIndicadorStatus(Constants.STATUS_INCOMPLETE);
            entity.setDataInicial(new Date());
            entity.setDataPrivacy(systemProperties.getProperty(Constants.DATA_PRIVACY_INITIAL));
            contratoPraticaDao.create(entity);
        } else {
            throw new IntegrityConstraintException();
        }
    }

    /*
     * @see com.ciandt.pms.business.service.IContratoPraticaService#
     * createContratoPraticaWithConvergencia
     * (com.ciandt.pms.model.ContratoPratica, com.ciandt.pms.model.GrupoCusto)
     */
    @Override
    public void createContratoPraticaWithConvergencia(
            final ContratoPratica entity, GrupoCusto grupoCusto, List<ContratoPraticaCentroLucro> contratoPraticaCentroLucro)
            throws IntegrityConstraintException {
        createContratoPratica(entity);

        Date mesCorrenteTruncado = DateUtil.getDate(new Date());
        contratoPraticaGrupoCustoService.create(entity, grupoCusto, mesCorrenteTruncado);
        Convergencia convergencia = toConvergencia(entity, grupoCusto);

        try {
            for (ContratoPraticaCentroLucro cpcl : contratoPraticaCentroLucro) {
                cpcl.setContratoPratica(entity);
                cpclService.createCPCL(cpcl);
            }
        } catch (MoreThanOneActiveEntityException e) {
            throw new IntegrityConstraintException(MSG_ERROR_CREATE_PROFIT_CENTER);
        }


        if (entity.getIndicadorAtivo().equals(Constants.ACTIVE)) {
            sendEmailToControladoria(
                    BundleUtil.getBundle(Constants.EMAIL_ASSUNTO_CRIACAO_CONTRATO_PRATICA),
                    BundleUtil.getBundle(Constants.EMAIL_MSG_CRIACAO_CONTRATO_PRATICA, convergencia.getContratoPratica().getNomeContratoPratica()));
        }
    }

    private Convergencia toConvergencia(final ContratoPratica entity,
                                        GrupoCusto grupoCusto) {
        Convergencia convergencia = new Convergencia();
        convergencia.setContratoPratica(entity);
        convergencia.setGrupoCusto(grupoCusto);
        convergencia.setSiglaTipo("CL");
        convergencia.setNomeProjetoMega(entity.getNomeContratoPratica());
        convergencia.setCodigoCentroCustoMega(grupoCusto.getErpCentroCustoPadrao());
        convergenciaService.create(convergencia);
        return convergencia;
    }

    /**
     * @param subject
     * @param message
     */
    public void sendEmailToControladoria(String subject, String message) {
        String mail = systemProperties.getProperty("mail.address.controladoria.to");
        mailSender.sendHtmlMail(mail, subject, message);
    }

    /*
     * @see com.ciandt.pms.business.service.IContratoPraticaService#
     * updateContratoPraticaWithConvergencia
     * (com.ciandt.pms.model.ContratoPratica, com.ciandt.pms.model.GrupoCusto)
     */
    @Override
    public void updateContratoPraticaWithConvergencia(ContratoPratica cp,
                                                      GrupoCusto grupoCusto) throws MoreThanOneActiveEntityException {

        MapaAlocacao mapaAlocacao = mapaAlocacaoService
                .findMapaAlocacaoByContratoPratica(cp);
        if (mapaAlocacao != null) {
            mapaAlocacao.setContratoPratica(cp);
            mapaAlocacaoService.updateMapaAlocacao(mapaAlocacao);
        }

        // busca convergencia salva
        Convergencia convergencia = convergenciaService
                .findByContratoPraticaId(cp.getCodigoContratoPratica());

        if (convergencia.getCodigoConvergencia() == null) {
            // cria uma nova convergencia
            convergencia = toConvergencia(cp, grupoCusto);
        } else if (grupoCusto != null) {
            // atualiza uma nova convergencia salva
            convergencia.setGrupoCusto(grupoCusto);
        }
        // convergencia.setNomeProjetoMega(cp.getNomeContratoPratica());
        convergenciaService.update(convergencia);

        ContratoPratica cpSaved = this.findContratoPraticaById(cp.getCodigoContratoPratica());
        Boolean fromProspectToActive = (cpSaved.getIndicadorAtivo().equals(Constants.PROSPECT) || cpSaved.getIndicadorAtivo().equals(Constants.EXPAND)) && cp.getIndicadorAtivo().equals(Constants.ACTIVE);
        Boolean fromActiveOrProspectToInactive = !cpSaved.getIndicadorAtivo().equals(Constants.INACTIVE) && cp.getIndicadorAtivo().equals(Constants.INACTIVE);
        Boolean fromRequestInactivationToInactive = !cpSaved.getIndicadorAtivo().equals(Constants.REQUEST_INACTIVATION) && cp.getIndicadorAtivo().equals(Constants.REQUEST_INACTIVATION);

		this.updateContratoPratica(cp);
		this.verifyContratoPraticaComplete(cp);

        if (fromProspectToActive) {
            this.sendEmailToControladoria(
                    BundleUtil.getBundle(Constants.EMAIL_ASSUNTO_CRIACAO_CONTRATO_PRATICA),
                    BundleUtil.getBundle(
                            Constants.EMAIL_MSG_CRIACAO_CONTRATO_PRATICA,
                            convergencia.getContratoPratica().getNomeContratoPratica()));
        }

        if (fromActiveOrProspectToInactive) {
            this.sendEmailToControladoria(
                    BundleUtil.getBundle(Constants.EMAIL_ASSUNTO_INATIVACAO_CONTRATO_PRATICA),
                    BundleUtil.getBundle(
                            Constants.EMAIL_MSG_INATIVACAO_CONTRATO_PRATICA,
                            convergencia.getContratoPratica().getNomeContratoPratica()));
        }

        if (fromRequestInactivationToInactive) {
            this.sendRequestInactivationEmail(
                    this.findActualBusinessManagerContratoPratica(cp),
                    BundleUtil.getBundle(Constants.EMAIL_ASSUNTO_REQUISICAO_INATIVACAO_CONTRATO_PRATICA),
                    BundleUtil.getBundle(
                            Constants.EMAIL_MSG_REQUISICAO_INATIVACAO_CONTRATO_PRATICA,
                            convergencia.getContratoPratica().getNomeContratoPratica(), LoginUtil.getLoggedUser().getCodigoLogin()));
        }
    }

    /**
     * Retorna um booleano indicando se o nome da Pratica do
     * {@link ContratoPratica} eh igual ao nome do {@link CentroLucro}. Obs.:
     * Case ignorado. "xpto" = "XPTO"
     *
     * @param contratoPratica {@link ContratoPratica}
     * @param natureza        {@link NaturezaCentroLucro}
     * @return boolean
     * @throws MoreThanOneActiveEntityException
     */
    public boolean isSameNamePraticaAndCentroLucro(
            ContratoPratica contratoPratica, NaturezaCentroLucro natureza) {
        contratoPratica = this.findContratoPraticaById(contratoPratica.getCodigoContratoPratica());

        ContratoPraticaCentroLucro cpcl = cpclService
                .findPresentByContratoPraticaAndNatureza(contratoPratica,
                        natureza);

        // caso nao tenha centro de lucro cadastrado nao completa
        if (cpcl == null || cpcl.getCodigoContratoPraticaCl() == null) {
            return Boolean.FALSE;
        }

        CentroLucro centroLucro = centroLucroService.findCentroLucroById(cpcl
                .getCentroLucro().getCodigoCentroLucro());

        String nomeCentroLucro = centroLucro.getNomeCentroLucro();
        String nomePratica = contratoPratica.getPratica().getNomePratica();

        if ("Pacific".equalsIgnoreCase(nomeCentroLucro)
                || "Pacific".equalsIgnoreCase(nomePratica)) {
            return Boolean.TRUE;
        }

        return nomePratica.equalsIgnoreCase(nomeCentroLucro);
    }

    /**
     * Executa um update na entidade passado por parametro.
     *
     * @param entity que sera atualizada.
     * @throws MoreThanOneActiveEntityException
     */
    @Override
    public void updateContratoPratica(final ContratoPratica entity) {
        if (Constants.INACTIVE.equals(entity.getIndicadorAtivo())) {
            entity.setDataFinal(new Date());
        } else {
            entity.setDataFinal(null);
        }
        // Caso o nome da Pratica do ContratoPratica seja diferente do nome do
        // Centro de Lucro, alterar o status do ContratoPratica para Incompleto
        NaturezaCentroLucro natureza = naturezaCentroLucroService
                .findBySigla(NaturezaCentroLucroSigla.LOB);
        if (!isSameNamePraticaAndCentroLucro(entity, natureza)) {
            entity.setIndicadorStatus(ContratoPraticaStatus.INCOMPLETE
                    .getIndicadorStatus());
        }
        contratoPraticaDao.update(entity);
    }

    /**
     * Busca o ContratoPratica por Contrato e Pratica.
     *
     * @param idPratica id da entidade pratica
     * @param idMsa     id do entidade contrato
     * @return o ContratoPratica caso exista
     */
    public ContratoPratica findContratoPraticaByPraticaAndContrato(
            final Long idPratica, final Long idMsa) {
        return contratoPraticaDao.findByPraticaAndMsa(idPratica, idMsa);
    }

    /**
     * Busca o ContratoPratica por Contrato e Pratica.
     *
     * @param idPratica id da entidade pratica
     * @return o ContratoPratica caso exista
     */
    public List<ContratoPratica> findContratoPraticaByPratica(
            final Long idPratica) {
        if (idPratica == null) {
            throw new NullNotSupportedException("Parametro: idPratica.");
        }
        return contratoPraticaDao.findByPratica(idPratica);
    }

    public void atualizaNomesContratoPratica(List<ContratoPratica> contratoPratica) {
        for (ContratoPratica contPrat: contratoPratica) {
            atualizaContratoPratica(contPrat);
        }
    }

    public void atualizaContratoPratica(ContratoPratica contratoPratica) {
        String nomeContratoPraticaAtt = ContratoPraticaController.composesClobName(contratoPratica.getMsa().getNomeMsa(),
                contratoPratica.getNomeCompound(), contratoPratica.getPratica().getSiglaPratica());
        contratoPratica.setNomeContratoPratica(nomeContratoPraticaAtt);

        updateContratoPratica(contratoPratica);
        mapaAlocacaoService.atualizaSufixosMapaAlocacao(contratoPratica);
    }

    /**
	 * Busca uma lista de entidades pelo filtro.
	 *
	 * @param filter		- entidade populada com os valores do filtro.
	 * @param cli			- entidade Cliente
	 * @param natureza		- entidade NaturezaCentroLucro
	 * @param cl			- entidade CentroLucro
	 * @param grupoCusto	- entidade CentroLucro
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<ContratoPratica> findContratoPraticaByFilterFetch(
			final ContratoPratica filter,
			final Cliente cli,
			final NaturezaCentroLucro natureza,
			final CentroLucro cl,
			final GrupoCusto grupoCusto,
			final List<String> indicadorWorkAtRiskList) {

        Msa c = filter.getMsa();
        if (c.getNomeMsa() != null && !"".equals(c.getNomeMsa())
                && c.getCodigoMsa() == null) {
            c.setCodigoMsa(-1L);
        }

        Pratica p = filter.getPratica();
        if (p.getNomePratica() != null && !"".equals(p.getNomePratica())
                && p.getCodigoPratica() == null) {
            p.setCodigoPratica(-1L);
        }

		return contratoPraticaDao.findByFilterFetch(filter, cli, natureza, cl, grupoCusto, indicadorWorkAtRiskList);
	}

    /**
     * Retorna todas as entidades relacionadas com o Cliente passado por
     * parametro.
     *
     * @param cliente - Cliente que se deseja buscar os ContratoPratica
     * @return retorna uma lista com todas as entidades.
     */
    public List<ContratoPratica> findContratoPraticaByCliente(
            final Cliente cliente) {
        return contratoPraticaDao.findByCliente(cliente);
    }

    /**
     * Retorna todas as entidades sem um fiscal deal associado.
     *
     * @return retorna uma lista com todas as entidades.
     */
    public List<ContratoPratica> findContratoPraticaSemFiscalDeal() {
        return contratoPraticaDao.findSemFiscalDeal();
    }

    @Transactional
    public Boolean removeContratoPratica(final ContratoPratica entity) {

        if (!entity.getMapaAlocacaos().isEmpty()) {
            Messages.showError("removeContratoPratica",
                    Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
                    new Object[]{Constants.ENTITY_NAME_CONTRATO_PRATICA,
                            Constants.ENTITY_NAME_MAPA_ALOCACAO});
            return Boolean.FALSE;
        }

        for (ContratoPraticaCentroLucro contratoPraticaCentroLucro : cpclDao.findByContratoPratica(entity)) {
            cpclDao.remove(contratoPraticaCentroLucro);
        }
        cpclDao.flush();

        Convergencia convergencia = convergenciaService.findByContratoPraticaId(entity.getCodigoContratoPratica());
        convergenciaService.delete(convergencia);

        entity.getImpostos().clear();

        contratoPraticaDao.remove(entity);
        contratoPraticaDao.flush();

        return Boolean.TRUE;
    }

    /**
     * Verifica se o contratoPratica esta completamente preenchido. E seta o
     * contrato-pratica como completo ou incompleto.
     *
     * @param contratoPratica para realizar a verificacao
     * @return se completo retorna true senao retorna false
     * @throws MoreThanOneActiveEntityException
     */
    public ContratoPratica verifyContratoPraticaComplete(ContratoPratica contratoPratica) {

        // contratoPratica = findContratoPraticaById(contratoPratica.getCodigoContratoPratica());

        // verifica se o contrato pratica possui centro lucro.
        NaturezaCentroLucro filter = new NaturezaCentroLucro();
        filter.setIndicadorTipo(Constants.NATUREZA_TYPE_MANDATORY);
        List<NaturezaCentroLucro> naturezaCentroLucroList = naturezaCentroLucroService
                .findNaturezaCentroLucroByFilter(filter);
        for (NaturezaCentroLucro ncl : naturezaCentroLucroList) {
            List<ContratoPraticaCentroLucro> cpclList = cpclService
                    .findCPCLByContratoPraticaAndNatureza(contratoPratica, ncl);
            if (cpclList == null || cpclList.isEmpty()) {
                contratoPratica.setIndicadorStatus(ContratoPraticaStatus.INCOMPLETE
                        .getIndicadorStatus());
                this.updateContratoPratica(contratoPratica);

                return contratoPratica;
            }
        }

        Boolean naoEhProspect = !(contratoPratica.getIndicadorAtivo().equals(Constants.PROSPECT) || contratoPratica.getIndicadorAtivo().equals(Constants.EXPAND));
        if (Boolean.TRUE.equals(naoEhProspect)) {
            Convergencia convergencia = convergenciaService.findByContratoPraticaId(contratoPratica.getCodigoContratoPratica());
            if (convergencia == null || convergencia.getCodigoProjetoMega() == null) {
                contratoPratica.setIndicadorStatus(ContratoPraticaStatus.INCOMPLETE
                        .getIndicadorStatus());
                this.updateContratoPratica(contratoPratica);

                return contratoPratica;
            }
        }

        contratoPratica.setIndicadorStatus(ContratoPraticaStatus.COMPLETE
                .getIndicadorStatus());
        this.updateContratoPratica(contratoPratica);

        return contratoPratica;
    }

    /**
     * Retorna todas as entidades relacionadas com o Cliente passado por
     * parametro.
     *
     * @param cl - CentroLucro que se deseja buscar os ContratoPraticas
     * @return retorna uma lista com todas as entidades.
     */
    public List<ContratoPratica> findContratoPraticaByCentroLucro(
            final CentroLucro cl) {
        return contratoPraticaDao.findByCentroLucro(cl);
    }

    /**
     * Retorna todas as entidades relacionadas com o Cliente passado por
     * parametro.
     *
     * @param natureza - NaturezaCentroLucro que se deseja buscar os ContratoPraticas
     * @return retorna uma lista com todas as entidades.
     */
    public List<ContratoPratica> findContratoPraticaByNatureza(
            final NaturezaCentroLucro natureza) {
        return contratoPraticaDao.findByNatureza(natureza);
    }

    /**
     * Retorna todas as entidades relacionadas com o Contrato passado por
     * parametro.
     *
     * @param msa - MSA que se deseja buscar os ContratoPraticas
     * @return retorna uma lista com todas as entidades.
     */
    public List<ContratoPratica> findContratoPraticaByContrato(final Msa msa) {
        return contratoPraticaDao.findByMsa(msa);
    }

    /**
     * @param mailSender the mailSender to set
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
     * Busca uma lista de entidades que possuem MapaAlocacao.
     *
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<ContratoPratica> findContPratAllWithMapaAlocacao() {
        return contratoPraticaDao.findAllWithMapaAlocacao();
    }

    /**
     * Retorna todos os ContratoPratica relacionados com o chargeback e dentro
     * de um periodo.
     *
     * @param tiRecurso - TiRecurso que se deseja buscar os ContratoPraticas
     * @param startDate - Data inicio do periodo.
     * @param endDate   - Data fim do periodo
     * @return retorna uma lista com todas as entidades.
     */
    public List<ContratoPratica> findContratoPraticaByTiRecursoAndPeriod(
            final TiRecurso tiRecurso, final Date startDate, final Date endDate) {
        return contratoPraticaDao.findByTiRecursoAndPeriod(tiRecurso,
                startDate, endDate);
    }

    /**
     * Busca um contrato pratica pelo nome.
     *
     * @param name the {@link ContratoPratica}'s name
     * @return {@link ContratoPratica}
     */
    @Override
    public ContratoPratica findContratoPraticaByName(final String name) {
        return contratoPraticaDao.findByName(name);
    }

    /**
     * Checa se um {@link ContratoPratica} j� existe no banco utilizando as
     * regras de constraints do banco.
     *
     * @param contratoPratica the {@link ContratoPratica}
     * @return {@link Boolean}
     */
    @Override
    public Boolean existContratoPratica(final ContratoPratica contratoPratica) {
        return this.findContratoPraticaByName(contratoPratica
                .getNomeContratoPratica()) != null;
    }

    /**
     * Add todas as {@link DealFiscal}s associando ao {@link ContratoPratica}.
     *
     * @param contratoPratica  the {@link ContratoPratica}
     * @param cpraticaDfiscals the {@link CpraticaDfiscal}
     */
    @Override
    public void addAllCpraticaDfiscal(final ContratoPratica contratoPratica,
                                      final List<CpraticaDfiscal> cpraticaDfiscals) {
        for (CpraticaDfiscal cpDealFiscal : cpraticaDfiscals) {
            cpraticaDfiscalService.createCpraticaDfiscal(cpDealFiscal);
        }
    }

    /**
     * Remove todas as {@link DealFiscal}s associadas a um
     * {@link ContratoPratica}.
     *
     * @param entity the {@link ContratoPratica}
     */
    @Override
    public void removeAllCpraticaDfiscal(final ContratoPratica entity) {

        if (entity.getCpraticaDfiscals().isEmpty()) {
            return;
        }

        for (CpraticaDfiscal cpraticaDfiscal : entity.getCpraticaDfiscals()) {
            cpraticaDfiscalService.removeCpraticaDfiscal(cpraticaDfiscal);
        }
    }

    /**
     * Atualiza a lista de {@link CpraticaDfiscal}s do {@link ContratoPratica}.
     *
     * @param contratoPratica  the {@link ContratoPratica}
     * @param cpraticaDfiscals a list of {@link CpraticaDfiscal}
     */
    @Override
    public void updateCpraticaDfiscal(final ContratoPratica contratoPratica,
                                      final List<CpraticaDfiscal> cpraticaDfiscals) {

        List<CpraticaDfiscal> existingCpraticaDfiscals = cpraticaDfiscalService.findByContratoPratica(contratoPratica);
        if (existingCpraticaDfiscals == null || existingCpraticaDfiscals.size() <= 0) {

            List<DealFiscal> newDealFiscals = new ArrayList<DealFiscal>();
            for (CpraticaDfiscal cpraticaDfiscal : cpraticaDfiscals) {
                newDealFiscals.add(dealFiscalService.findDealFiscalById(cpraticaDfiscal.getDealFiscal().getCodigoDealFiscal()));
            }
            receitaService.updateAllReceitaForecastDealFiscalByContratoPratica(newDealFiscals, contratoPratica.getCodigoContratoPratica());
        }

        // remove todos os ContratoPraticaMoeda existentes
        this.removeAllCpraticaDfiscal(contratoPratica);

        // aticiona os novos ContratoPraticaMoeda
        this.addAllCpraticaDfiscal(contratoPratica, cpraticaDfiscals);
    }

    /**
     * Busca todos os clobs comepletos e ativos.
     *
     * @return listResult
     */
    public List<ContratoPratica> findContratoPraticaAllCompleteAndActive() {
        return contratoPraticaDao.findAllCompleteAndActive();
    }


    /**
     * Busca todos os clobs comepletos e ativos.
     *
     * @return listResult
     */
    public List<ContratoPratica> findAllContratoPraticaActive() {
        return contratoPraticaDao.findAllContratoPraticaActive();
    }

    public List<ContratoPratica> findByAproverRescinded() {
        return contratoPraticaDao.findByAproverRescinded();
    }

    @Override
    public Boolean isContratoPraticaActive(Long codigoContratoPratica) {
        ContratoPratica cp = this.findContratoPraticaById(codigoContratoPratica);

        if (cp != null) {
            return this.isContratoPraticaActive(cp);
        }

        return null;
    }

    @Override
    public Boolean isContratoPraticaActive(ContratoPratica contratoPratica) {

        if (contratoPratica.getIndicadorDeleteLogico().equals(Constants.NO)
                && contratoPratica.getIndicadorAtivo().equals(Constants.ACTIVE)) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public List<ContratoPraticaAud> findHistoryByCodigo(Long codigoContratoPratica) {
        return contratoPraticaDao.findHistoryByCodigo(codigoContratoPratica);
    }

    @Override
    public List<String> findManagerOfContratoPratricaWithouDealFiscal() {
        return contratoPraticaDao.findManagerOfContratoPraticaWithoutDealFiscal();
    }

    public List<Map<String, String>> findContratoPraticaWithoutDealFiscal(String loginManager) {
        return contratoPraticaDao.findContratoPraticaWithoutDealFiscal(loginManager);
    }

    /**
     * Busca todos os Mapas de Alocação com o status "Prospect" que possuem alocacao
     * de algum recurso no mês corrente.
     *
     * @return {@link List} of {@link MapaProspectComAlocacaoResultsetVO}
     */
    @Override
    public List<MapaProspectComAlocacaoResultsetVO> findProspectMapWithAllocation() {
        return contratoPraticaDao.findProspectMapWithAllocation();
    }

    @Override
    public List<ContractLobDTO> findAllWithExternalRestRequest() {
        List<ContractLobDTO> filtered = new ArrayList<>();
        List<ContractLobDTO> all = message.getForSelection();

        for (ContractLobDTO clob : all) {
            if (!clob.getIdeCode().equals("1")) { // ide 1 é para clientes, então para o projeto overhead nao é necessario.
                filtered.add(clob);
            }
        }

        return filtered;
    }

    public void sendRequestInactivationEmail(String to, String subject, String message) {
        String mail = systemProperties.getProperty("mail.address.controladoria.to");

        if (to != null) {
            mail = mail + "," + to;
        }
        mailSender.sendHtmlMail(mail, subject, message);
    }

    public String findActualBusinessManagerContratoPratica(ContratoPratica contratoPratica) {
        return contratoPraticaDao.findActualBusinessManagerContratoPratica(contratoPratica.getCodigoContratoPratica());
    }

    /**
     * verifica se o arquivo enviado para upload contem erros.
     *
     * @param uploadItem      - contem o valor do upload
     * @param centroLucroList
     *                     upload
     */
    @Override
    public Map<String, List<String>> uploadContratoPratica(final UploadItem uploadItem, List<String> centroLucroList) throws Exception {


        PadraoArquivo padraoArq = new PadraoArquivo(1l, PADRAO_ARQUIVO_CSV);
        padraoArq.setTextoDelimitadorColuna(',');
        padraoArq.setTextoDelimitadorString('"');

        List<UploadManagers> uploadManagersList =
                CsvUtil.getElementList(new String(uploadItem.getData()),
                        UploadManagers.class, CsvUtil.getCsvConfig(padraoArq));

        Map<String, List<String>> map =  mapErrosUploadFile(uploadManagersList, centroLucroList);

        if (!hasKeyWithValue(map)) {
            saveContratoPraticaData(uploadManagersList);
        }else{
            return map;
        }
        return null;
    }

    @Override
    public void saveContratoPraticaData(List<UploadManagers> uploadManagersList) throws Exception {
        List<String> emailConfirmationList = new ArrayList<String>();
        StringBuilder message = new StringBuilder("C-LOB:\n");

        for (UploadManagers manager : uploadManagersList) {
            ContratoPratica contratoPratica = findContratoPraticaById(manager.getCodigoContratoPratica());
            if(manager.getPesoaAprovador() != null){
                Pessoa pessoaArovador = pessoaService.findPessoaByLogin(manager.getPesoaAprovador());
                contratoPratica.setAprovador(pessoaArovador);
            }
            if(manager.getPessoaGerente()!= null){
                Pessoa pessoaGerente = pessoaService.findPessoaByLogin(manager.getPessoaGerente());
                contratoPratica.setGerenteAprovador(pessoaGerente);
            }
            if(manager.getBusinessManager() != null) {
                saveBusinessManager(contratoPratica, manager.getBusinessManager(), manager.getStartDate());
            }
            contratoPraticaDao.update(contratoPratica);
            message.append("\n").append(contratoPratica.getCodigoContratoPratica()+" - ").append(contratoPratica.getNomeContratoPratica()).append(" atualizado com sucesso.\n");
        }
        String subject = Constants.CONTRATO_PRATICA_UPDATE_MANAGER_MAIL_SUBJECT;

        Parametro cpsEmails = parametroService
                .findParametroByNomeParametro(Constants.CPS_EMAILS_PARAMETER);
        String textoParametro = cpsEmails.getTextoParametro();
        String textoParametroAntesVirgula = textoParametro.split(",")[0];
        String mail = textoParametroAntesVirgula + "," + Constants.CONTRATO_PRATICA_UPDATE_MANAGER_COPY_MAIL+Constants.COMPANY_MAIL_DOMAIN;
        mailSender.sendHtmlMail(mail, subject, message.toString());
    }

    public void saveBusinessManager(ContratoPratica contratoPratica, String businessManager, String startDate) throws Exception {
        NaturezaCentroLucro naturezaFilter = new NaturezaCentroLucro();
        naturezaFilter.setIndicadorAtivo(Constants.ACTIVE);
        naturezaFilter.setCodigoNatureza(3L);
        List<ContratoPraticaCentroLucro> contratoPraticaCentroLucros = cpclService.findCPCLByContratoPraticaAndNatureza(contratoPratica, naturezaFilter
        );

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        Date startDateParsed = null;
        try {
            if (startDate == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                startDateParsed = calendar.getTime();
            } else {
                startDateParsed = dateFormat.parse(startDate);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDateParsed);
            calendar.add(Calendar.MONTH, -1);
            Date previousMonthDate = calendar.getTime();
            if(contratoPraticaCentroLucros != null && !contratoPraticaCentroLucros.isEmpty()) {
                for (ContratoPraticaCentroLucro cpcl : contratoPraticaCentroLucros) {
                    if (cpcl.getDataFimVigencia() == null) {
                        cpcl.setDataFimVigencia(previousMonthDate);
                        cpclDao.update(cpcl);
                    }
                }
            }

            ContratoPraticaCentroLucro cpcl = new ContratoPraticaCentroLucro();
            cpcl.setContratoPratica(contratoPratica);
            List<CentroLucro> centroLucroList = centroLucroService.findCentroLucroBusinessManagerByName(naturezaFilter,businessManager);
            cpcl.setCentroLucro(centroLucroList.get(0));
            cpcl.setDataInicioVigencia(startDateParsed);
            cpclDao.create(cpcl);


        } catch (Exception e) {
            throw new IntegrityConstraintException(MSG_ERROR_SAVE_BUSINESS_MANAGER_UPDATE);
        }
    }

    /**
     * verifica se o arquivo enviado para upload contem erros.
     *
     * @param centroLucroList
     *                     upload
     */
    @Override
    public Map<String, List<String>> mapErrosUploadFile(final List<UploadManagers> uploadManagersList, List<String> centroLucroList) throws Exception {
        List<Pessoa> gerentes = pessoaService.findAllManagerForComboBox();
        Map<String, List<String>> errorMap = new HashMap<>();
        List<String> codigoContratoPraticaErrorList = new ArrayList<>();
        List<String> loginErrorList = new ArrayList<>();
        List<String> managerErrorList = new ArrayList<>();
        List<String> startDateErrorList = new ArrayList<>();
        List<String> managerApproverErrorList = new ArrayList<>();
        int count = 1;
        String strError = "";
        for (UploadManagers uploadManagers : uploadManagersList) {
            count++;

            if(uploadManagers.getCodigoContratoPratica() != null){
                Long clobIdError = validadeteCodigoContratoPratica(uploadManagers.getCodigoContratoPratica());
                if (clobIdError != null) {
                    addUnique(codigoContratoPraticaErrorList, clobIdError.toString());
                }
            }else{
                addUnique(codigoContratoPraticaErrorList, "null");
            }


            if (uploadManagers.getPesoaAprovador() != null) {
                String userPessoaAprovador = validadeteUser(uploadManagers.getPesoaAprovador());
                if (userPessoaAprovador != null) {
                    addUnique(loginErrorList, userPessoaAprovador);
                }
            }

            if (uploadManagers.getPessoaGerente() != null) {
                Pessoa pessoa = pessoaService.findPessoaByLogin(uploadManagers.getPessoaGerente());
                if (pessoa == null) {
                    addUnique(loginErrorList, uploadManagers.getPessoaGerente());
                } else {
                    if (!gerentes.contains(pessoa)) {
                        addUnique(managerApproverErrorList, uploadManagers.getPessoaGerente());
                    }
                }
            }

            if (uploadManagers.getBusinessManager() != null) {
                {
                    String userBusinessManager = validadeteUser(uploadManagers.getBusinessManager());
                    if (userBusinessManager != null) {
                        addUnique(loginErrorList, userBusinessManager);
                    } else {
                        if (!centroLucroList.contains(uploadManagers.getBusinessManager())) {
                            addUnique(managerErrorList, uploadManagers.getBusinessManager());
                        }
                    }
                }

            }

            if (uploadManagers.getStartDate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
                dateFormat.setLenient(false);
                try {
                    Date date = dateFormat.parse(uploadManagers.getStartDate());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int inputMonth = cal.get(Calendar.MONTH);
                    int inputYear = cal.get(Calendar.YEAR);

                    Calendar currentCal = Calendar.getInstance();
                    int currentMonth = currentCal.get(Calendar.MONTH);
                    int currentYear = currentCal.get(Calendar.YEAR);

                    if (inputYear < currentYear || (inputYear == currentYear && inputMonth < currentMonth)) {
                        addUnique(startDateErrorList, uploadManagers.getStartDate());
                    }
                } catch (ParseException e) {
                    addUnique(startDateErrorList, uploadManagers.getStartDate());
                }
            }
        }


        errorMap.put(Constants.CONTRATO_PRATICA_CLOB_NOT_FOUND, codigoContratoPraticaErrorList);
        errorMap.put(Constants.CONTRATO_PRATICA_LOGIN_NOT_FOUND, loginErrorList);
        errorMap.put(Constants.CONTRATO_PRATICA_BUSINESS_MANAGER_NOT_REGISTRED, managerErrorList);
        errorMap.put(Constants.CONTRATO_PRATICA_INVALID_DATE, startDateErrorList);
        errorMap.put(Constants.CONTRATO_PRATICA_MANAGER_NOT_REGISTRED, managerApproverErrorList);

        return errorMap;
    }

    /**
     * Verifica se o valor a ser inserido na lista é unico
     *
     */
    private static void addUnique(List<String> list, String value) {
        if (!list.contains(value)) {
            list.add(value);
        }
    }

    /**
     * Valida o CLOB id.
     *
     * @return retorna o id que contem erro.
     * @par
     */
    private Long validadeteCodigoContratoPratica(final Long codigoContratoPratica) {
        ContratoPratica contratoPratica = null;
        contratoPratica = findContratoPraticaById(codigoContratoPratica);
        if (contratoPratica == null) {
            return codigoContratoPratica;
        }
        return null;
    }


    /**
     * Valida o Usuarios cadastrados.
     *
     * @return retorna a string que contem erro.
     * @par
     */
    private String validadeteUser(final String user) {
        Pessoa pessoa = pessoaService.findPessoaByLogin(user);
        if (pessoa == null) {
            return user;
        }
        return null;
    }

    /**
     * Verifica se o mapa contem erro.
     *
     * @return retorna se o mapa contem erro.
     * @param map
     */
    @Override
    public boolean hasKeyWithValue(Map<String, List<String>> map) {
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            List<String> errorList = (List<String>) entry.getValue();
            if (entry.getValue() != null && errorList.size() > 0) {
                return true;
            }
        }
        return false;
    }

}
