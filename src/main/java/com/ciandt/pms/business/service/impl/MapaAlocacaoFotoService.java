/**
 * Classe MapaAlocacaoFotoService - Implementação do serviço do MapaAlocacaoFoto.
 */
package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAlocacaoFotoService;
import com.ciandt.pms.business.service.IAlocacaoPeriodoFotoService;
import com.ciandt.pms.business.service.IMapaAlocacaoFotoService;
import com.ciandt.pms.business.service.IMapaAlocacaoPessoaService;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IRecursoService;
import com.ciandt.pms.business.service.IVwAuditFollowService;
import com.ciandt.pms.business.service.IVwDifMapaSnapshotsService;
import com.ciandt.pms.control.jsf.converters.StatusMapaAlocFotoConverter;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.Alocacao;
import com.ciandt.pms.model.AlocacaoFoto;
import com.ciandt.pms.model.AlocacaoPeriodo;
import com.ciandt.pms.model.AlocacaoPeriodoFoto;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.MapaAlocacaoFoto;
import com.ciandt.pms.model.MapaAlocacaoPessoa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.VwDifMapaSnapshots;
import com.ciandt.pms.model.VwDifMapaSnapshotsId;
import com.ciandt.pms.model.vo.AlocacaoFotoCell;
import com.ciandt.pms.model.vo.AlocacaoFotoRow;
import com.ciandt.pms.persistence.dao.IMapaAlocacaoFotoDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.FacesUtil;
import com.ciandt.pms.util.MailSenderUtil;
import com.ciandt.pms.util.PMSUtil;


/**
 * A classe MapaAlocacaoFotoService proporciona as funcionalidades de serviço
 * para a entidade MapaAlocacaoFoto.
 * 
 * @since 18/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class MapaAlocacaoFotoService implements IMapaAlocacaoFotoService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IMapaAlocacaoFotoDao mapaAlocFotoDao;

    /** Instancia do Service de AlocacaoFoto. */
    @Autowired
    private IAlocacaoFotoService alocacaoFotoService;

    /** Instancia do Service de AlocacaoPeriodoFoto. */
    @Autowired
    private IAlocacaoPeriodoFotoService alocacaoPeriodoFotoService;

    /** Instância do Service de VwDifMapaSnapshots. */
    @Autowired
    private IVwDifMapaSnapshotsService vwDifMapaSnapshotsService;

    /** Instancia do Service de MapaAlocacao. */
    @Autowired
    private IMapaAlocacaoService mapaAlocService;

    /** Instancia do Service de MapaAlocacaoPessoa. */
    @Autowired
    private IMapaAlocacaoPessoaService mapaAlocPessService;

    /** Instancia do Service de Pessoa. */
    @Autowired
    private IPessoaService pessoaService;

    /** Instancia do Service de Recurso. */
    @Autowired
    private IRecursoService recursoService;

    /** Instancia do Service de VwAuditFollow. */
    @Autowired
    private IVwAuditFollowService vwAuFlService;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /** Intancia de mailSender. */
    private MailSenderUtil mailSender;

    /**
     * @return the mailSender
     */
    public MailSenderUtil getMailSender() {
        return mailSender;
    }

    /**
     * @param mailSender
     *            the mailSender to set
     */
    public void setMailSender(final MailSenderUtil mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createMapaAlocacaoFoto(final MapaAlocacaoFoto entity) {
        mapaAlocFotoDao.create(entity);
    }

    /**
     * Insere uma lista de entidades.
     * 
     * @param mapaAlocList
     *            - lista de MapaAlocacao (snapshots) a serem gravados como
     *            foto.
     */
    public void createMapaAlocFotoList(final List<MapaAlocacao> mapaAlocList) {
        for (MapaAlocacao ma : mapaAlocList) {
            MapaAlocacaoFoto entity = new MapaAlocacaoFoto();
            entity.setIndicadorStatusFoto(Constants.STATUS_CURRENT);
            entity.setCodigoMapaAlocacao(ma.getCodigoMapaAlocacao());
            entity.setCodigoContratoPratica(ma.getContratoPratica()
                    .getCodigoContratoPratica());
            // entity.setTextoTitulo(ma.getTextoTitulo());
            // entity.setIndicadorVersao(ma.getIndicadorVersao());
            // entity.setLoginTrava(ma.getLoginTrava());
            // entity.setDataTrava(ma.getDataTrava());
            // entity.setDataInicio(ma.getDataInicio());
            // entity.setDataFim(ma.getDataFim());
            // entity.setDataInicioJanela(ma.getDataInicioJanela());

            List<Alocacao> alocacaoList = ma.getAlocacaos();
            List<AlocacaoFoto> entityAlocFotoList = new ArrayList<AlocacaoFoto>();
            for (Alocacao a : alocacaoList) {
                AlocacaoFoto entityAlocFoto = new AlocacaoFoto();
                entityAlocFoto.setMapaAlocacaoFoto(entity);
                entityAlocFoto.setIndicadorStatusFoto(Constants.STATUS_CURRENT);
                entityAlocFoto.setCodigoAlocacao(a.getCodigoAlocacao());
                entityAlocFoto
                        .setCodigoMapaAlocacao(ma.getCodigoMapaAlocacao());
                entityAlocFoto.setCodigoPerfilVendido(a.getPerfilVendido()
                        .getCodigoPerfilVendido());
                entityAlocFoto.setCodigoRecurso(a.getRecurso()
                        .getCodigoRecurso());
                // entityAlocFoto.setCodigoCidadeBase(a.getCidadeBase().getCodigoCidadeBase());
                entityAlocFoto.setValorUr(a.getValorUr());
                entityAlocFoto.setIndicadorEstagio(a.getIndicadorEstagio());
                // entityAlocFoto.setDataInicio(a.getDataInicio());
                // entityAlocFoto.setDataFim(a.getDataFim());

                entityAlocFotoList.add(entityAlocFoto);

                Set<AlocacaoPeriodo> alocacaoPeriodoList = a
                        .getAlocacaoPeriodos();
                List<AlocacaoPeriodoFoto> entityAlocPerFotoList = new ArrayList<AlocacaoPeriodoFoto>();
                for (AlocacaoPeriodo ap : alocacaoPeriodoList) {
                    AlocacaoPeriodoFoto entityAlocPerFoto = new AlocacaoPeriodoFoto();
                    entityAlocPerFoto.setAlocacaoFoto(entityAlocFoto);
                    entityAlocPerFoto
                            .setIndicadorStatusFoto(Constants.STATUS_CURRENT);
                    entityAlocPerFoto.setCodigoAlocacaoPeriodo(ap
                            .getCodigoAlocacaoPeriodo());
                    entityAlocPerFoto.setCodigoAlocacao(a.getCodigoAlocacao());
                    entityAlocPerFoto.setDataAlocacaoPeriodo(ap
                            .getDataAlocacaoPeriodo());
                    entityAlocPerFoto.setPercentualAlocacaoPeriodo(ap
                            .getPercentualAlocacaoPeriodo());
                    // entityAlocPerFoto.setPercentualUr(ap.getPercentualUr());

                    entityAlocPerFotoList.add(entityAlocPerFoto);
                }

                entityAlocFoto.setAlocacaoPeriodoFotos(entityAlocPerFotoList);
            }
            entity.setAlocacaoFotos(entityAlocFotoList);

            this.createMapaAlocacaoFoto(entity);
        }
    }

    /**
     * Atualiza a entidade.
     * 
     * @param entity
     *            entidade a ser atualizada.
     */
    public void updateMapaAlocacaoFoto(final MapaAlocacaoFoto entity) {
        mapaAlocFotoDao.update(entity);
    }

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     */
    public void removeMapaAlocacaoFoto(final MapaAlocacaoFoto entity) {

        MapaAlocacaoFoto mapa = this.findMapaAlocacaoFotoById(entity
                .getCodigoMapaAlocFoto());

        // itera a lista de AlocacaoFoto e por sua vez a lista de
        // AlocacaoPeriodoFoto de
        // cada AlocacaoFoto e exclui cada entidade, uma a uma
        List<AlocacaoFoto> alocacaoFotoList = mapa.getAlocacaoFotos();
        for (AlocacaoFoto alocacaoFoto : alocacaoFotoList) {
            List<AlocacaoPeriodoFoto> alocacaoPeriodoFotoList = alocacaoFoto
                    .getAlocacaoPeriodoFotos();
            for (AlocacaoPeriodoFoto alocacaoPeriodoFoto : alocacaoPeriodoFotoList) {
                // remove AlocacaoPeriodoFoto
                alocacaoPeriodoFotoService
                        .removeAlocacaoPeriodoFoto(alocacaoPeriodoFoto);
            }

            // remove AlocacaoFoto
            alocacaoFotoService.removeAlocacaoFoto(alocacaoFotoService
                    .findAlocacaoFotoById(alocacaoFoto.getCodigoAlocFoto()));
        }

        // remove MapaAlocacao
        mapaAlocFotoDao.remove(mapa);
    }

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public MapaAlocacaoFoto findMapaAlocacaoFotoById(final Long entityId) {
        return mapaAlocFotoDao.findById(entityId);
    }

    /**
     * Prepara uma matriz de alocações e percentuais Previous e Current para
     * serem exibidos nos e-mails para os followers.
     * 
     * @param startDate
     *            - data de início do range do follow
     * @param isClobFollow
     *            - indicador se é follow de C-lob ou não (People)
     * 
     * @return uma matriz de alocações e percentuais Previous e Current
     */
    public Map<Long, List<AlocacaoFotoRow>> prepareMapaAlocSnapshotsMatrix(
            final Date startDate, final Boolean isClobFollow) {
        StatusMapaAlocFotoConverter converter = new StatusMapaAlocFotoConverter();

        List<VwDifMapaSnapshots> difList = vwDifMapaSnapshotsService
                .findAllByRangeMonths(startDate, isClobFollow);

        Map<Long, AlocacaoFotoRow> alocFotoRowMap = new HashMap<Long, AlocacaoFotoRow>();
        Map<Date, AlocacaoFotoCell> alocFotoCellMap = null;
        Map<Long, Map<Date, AlocacaoFotoCell>> alocFotoCellListMap = 
            new HashMap<Long, Map<Date, AlocacaoFotoCell>>();

        List<Date> validityDateAuxList = DateUtil.getValidityDateList(
                startDate, new Integer((String) systemProperties
                        .get(Constants.MAPA_ALOCACAO_RANGE_MONTHS_SNAPSHOTS)));

        for (VwDifMapaSnapshots dif : difList) {
            VwDifMapaSnapshotsId id = dif.getId();
            Long codigoAlocacao = id.getCodigoAlocacao();
            Date dataAloc = DateUtil.getDate(id.getDataAloc());

            if (!alocFotoRowMap.containsKey(codigoAlocacao)) {
                alocFotoCellMap = new HashMap<Date, AlocacaoFotoCell>();
                AlocacaoFotoRow row = new AlocacaoFotoRow();

                if (isClobFollow) {
                    row.setCurrentFollowedName(id.getNomeContratoPratica());
                    row.setListedName(id.getNomeRecurso());
                } else {
                    row.setCurrentFollowedName(id.getNomeRecurso());
                    row.setListedName(id.getNomeContratoPratica());
                }
                row.setCodigoMapaAlocacao(id.getCodigoMapaAlocacao());
                row.setNomeContratoPratica(id.getNomeContratoPratica());
                row.setIndicadorTipoOp(converter.getAsString(dif
                        .getIndicadorTipoOp()));
                row.setCodigoAlocacao(codigoAlocacao);
                row.setCodigoRecurso(id.getCodigoRecurso());
                row.setNomeRecurso(id.getNomeRecurso());
                row.setNomePerfilVendidoPv(dif.getNomePerfilVendidoPv());
                row.setNomePerfilVendidoCr(dif.getNomePerfilVendidoCr());
                row.setIndicadorEstagioPv(dif.getIndicadorEstagioPv());
                row.setIndicadorEstagioCr(dif.getIndicadorEstagioCr());
                row.setValorUrPv(dif.getValorUrPv());
                row.setValorUrCr(dif.getValorUrCr());
                alocFotoRowMap.put(codigoAlocacao, row);

                for (Date dateAux : validityDateAuxList) {
                    AlocacaoFotoCell cellAux = new AlocacaoFotoCell();
                    cellAux.setDataAloc(dateAux);
                    cellAux.setPercentualAlocPv(BigDecimal.valueOf(0));
                    cellAux.setPercentualAlocCr(BigDecimal.valueOf(0));
                    alocFotoCellMap.put(dateAux, cellAux);
                }
            }

            AlocacaoFotoCell cell = new AlocacaoFotoCell();
            cell.setDataAloc(dataAloc);
            cell.setPercentualAlocPv(dif.getPercentualAlocPv());
            cell.setPercentualAlocCr(dif.getPercentualAlocCr());
            alocFotoCellMap.put(dataAloc, cell);

            alocFotoCellListMap.put(codigoAlocacao, alocFotoCellMap);
        }

        List<AlocacaoFotoRow> alocFotoRowList = new ArrayList<AlocacaoFotoRow>();
        List<AlocacaoFotoCell> alocFotoCellList = null;
        Iterator<Long> itRow = alocFotoRowMap.keySet().iterator();
        while (itRow.hasNext()) {
            Long nextCodigoAlocacao = itRow.next();
            AlocacaoFotoRow row = alocFotoRowMap.get(nextCodigoAlocacao);

            alocFotoCellList = new ArrayList<AlocacaoFotoCell>();
            Map<Date, AlocacaoFotoCell> cellMapAux = alocFotoCellListMap
                    .get(nextCodigoAlocacao);
            Iterator<Date> itCell = cellMapAux.keySet().iterator();
            while (itCell.hasNext()) {
                Date nextDate = itCell.next();
                AlocacaoFotoCell cell = cellMapAux.get(nextDate);
                alocFotoCellList.add(cell);
            }

            orderAlocacaoFotoCellList(alocFotoCellList);
            row.setAlocacaoFotoCellList(alocFotoCellList);

            alocFotoRowList.add(row);
        }

        return this.getMapMatrix(alocFotoRowList, isClobFollow);
    }

    /**
     * Retorna um Map (matriz) das snapshots dos MapaAlocacao. Se for Follow
     * C-Lob, "trava" o código do MapaAlocacao, caso contrário, trava código do
     * Recurso
     * 
     * @param alocFotoRowList
     *            - lista de registros (AlocacaoFotoRow) para serem listadas nos
     *            e-mails
     * @param isClobFollow
     *            - indicador se é Follow de C-Lob ou não (People)
     * 
     * @return um Map (matriz) das snapshots dos MapaAlocacao.
     */
    private Map<Long, List<AlocacaoFotoRow>> getMapMatrix(
            final List<AlocacaoFotoRow> alocFotoRowList,
            final Boolean isClobFollow) {
        Map<Long, List<AlocacaoFotoRow>> mapaAlocSnapshotsMatrix = new HashMap<Long, List<AlocacaoFotoRow>>();
        Long codigo = null;

        for (AlocacaoFotoRow row : alocFotoRowList) {
            if (isClobFollow) {
                codigo = row.getCodigoMapaAlocacao();
            } else {
                codigo = row.getCodigoRecurso();
            }

            if (mapaAlocSnapshotsMatrix.containsKey(codigo)) {
                List<AlocacaoFotoRow> rowListAux = mapaAlocSnapshotsMatrix
                        .get(codigo);
                rowListAux.add(row);
            } else {
                List<AlocacaoFotoRow> rowListAux = new ArrayList<AlocacaoFotoRow>();
                rowListAux.add(row);
                mapaAlocSnapshotsMatrix.put(codigo, rowListAux);
            }
        }

        // ordena a lista de alocações se for Follow de C-Lob, ordena pelo
        // nomeRecurso, se Follow de People, ordena pelo nomeContratoPratica
        Iterator<Long> itAux = mapaAlocSnapshotsMatrix.keySet().iterator();
        while (itAux.hasNext()) {
            Long nextCodAux = itAux.next();
            List<AlocacaoFotoRow> alocFotoRowListAux = mapaAlocSnapshotsMatrix
                    .get(nextCodAux);
            orderAlocacaoFotoRowList(alocFotoRowListAux, isClobFollow);
        }

        return mapaAlocSnapshotsMatrix;
    }

    /**
     * Ordena a lista de AlocacaoFotoCell de cada Alocacao da matriz.
     * 
     * @param alocFotoCellList
     *            - lista de AlocacaoPeriodoFoto da AlocacaoFoto corrente
     */
    private void orderAlocacaoFotoCellList(
            final List<AlocacaoFotoCell> alocFotoCellList) {
        Collections.sort(alocFotoCellList, new Comparator<AlocacaoFotoCell>() {
            public int compare(final AlocacaoFotoCell afc1,
                    final AlocacaoFotoCell afc2) {
                return afc1.getDataAloc().compareTo(afc2.getDataAloc());
            }
        });
    }

    /**
     * Ordena a lista de AlocacaoFotoRow de cada MapaAlocacao da matriz.
     * 
     * @param alocFotoRowList
     *            - lista de AlocacaoFoto do MapaAlocacaoFoto corrente
     * @param isClobFollow
     *            - indicador se é follow de C-lob ou não (People)
     */
    private void orderAlocacaoFotoRowList(
            final List<AlocacaoFotoRow> alocFotoRowList,
            final Boolean isClobFollow) {
        if (isClobFollow) {
            Collections.sort(alocFotoRowList,
                    new Comparator<AlocacaoFotoRow>() {
                        public int compare(final AlocacaoFotoRow afr1,
                                final AlocacaoFotoRow afr2) {
                            return afr1.getNomeRecurso().compareTo(
                                    afr2.getNomeRecurso());
                        }
                    });
        } else {
            Collections.sort(alocFotoRowList,
                    new Comparator<AlocacaoFotoRow>() {
                        public int compare(final AlocacaoFotoRow afr1,
                                final AlocacaoFotoRow afr2) {
                            return afr1.getNomeContratoPratica().compareTo(
                                    afr2.getNomeContratoPratica());
                        }
                    });
        }
    }

    /**
     * Envia o e-mail para os followers do MapaAlocacao e Pessoa.
     * 
     * @param mapaAlocSnapshotsMatrix
     *            - matriz com os dados da comparação
     * @param startDate
     *            - data de início do range do follow
     * @param isClobFollow
     *            - indicador se é follow de C-lob ou não (People)
     */
    public void sendMapaAlocSnapshotsMail(
            final Map<Long, List<AlocacaoFotoRow>> mapaAlocSnapshotsMatrix,
            final Date startDate, final Boolean isClobFollow) {
        // dados comuns entre Follow C-Lob e People
        String[] commonStrings = new String[7];
        // 0 subject (html)
        // 1 description (html)
        // 2 subjectBB
        // 3 descriptionBB
        // 4 template e-mail

        // 5 description 2 (html)
        // 6 descriptionBB 2

        if (isClobFollow) {
            commonStrings[0] = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_MAAL_PESS_MAIL_FOLLOW_SUBJECT);
            commonStrings[1] = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_MAAL_PESS_MAIL_FOLLOW_DESCRIPTION);

            commonStrings[2] = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_MAAL_PESS_MAIL_BBERRY_FOLLOW_SUBJECT);
            commonStrings[3] = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_MAAL_PESS_MAIL_BBERRY_FOLLOW_DESCRIPTION);

            commonStrings[4] = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_MAPA_FOTO_TEMPLATE_MAIL_CLOB);

            commonStrings[5] = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_MAAL_PESS_MAIL_FOLLOW_DESCRIPTION2);
            commonStrings[6] = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_MAAL_PESS_MAIL_BBERRY_FOLLOW_DESCRIPTION2);
        } else {
            commonStrings[0] = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_PESS_PESS_MAIL_FOLLOW_SUBJECT);
            commonStrings[1] = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_PESS_PESS_MAIL_FOLLOW_DESCRIPTION);

            commonStrings[2] = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_PESS_PESS_MAIL_BBERRY_FOLLOW_SUBJECT);
            commonStrings[3] = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_PESS_PESS_MAIL_BBERRY_FOLLOW_DESCRIPTION);

            commonStrings[4] = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_MAPA_FOTO_TEMPLATE_MAIL_PEOPLE);

            commonStrings[5] = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_PESS_PESS_MAIL_FOLLOW_DESCRIPTION2);
            commonStrings[6] = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_PESS_PESS_MAIL_BBERRY_FOLLOW_DESCRIPTION2);
        }

        String subject = commonStrings[0];
        String defaultDescription = commonStrings[1];
        String subjectBB = commonStrings[2];
        String defaultDescriptionBB = commonStrings[3];
        String template = commonStrings[4];

        String defaultDescription2 = commonStrings[5];
        String defaultDescriptionBB2 = commonStrings[6];

        // formato e-mail comum - html
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", new Locale(
                "en/US"));
        List<String> validityDateStringList = DateUtil
                .getValidityDateStringList(
                        startDate,
                        new Integer(
                                (String) systemProperties
                                        .get(Constants.MAPA_ALOCACAO_RANGE_MONTHS_SNAPSHOTS)),
                        sdf);

        // formato e-mail para BlackBerry
        sdf = new SimpleDateFormat("MMM", new Locale("en/US"));
        List<String> valDateStrListBB = DateUtil.getValidityDateStringList(
                startDate, new Integer((String) systemProperties
                        .get(Constants.MAPA_ALOCACAO_RANGE_MONTHS_SNAPSHOTS)),
                sdf);
        
        StringBuffer strBufInvalidLogin = new StringBuffer();

        Iterator<Long> it = mapaAlocSnapshotsMatrix.keySet().iterator();
        while (it.hasNext()) {
            Long nextCodigo = it.next();
            List<AlocacaoFotoRow> alocFotoRowList = mapaAlocSnapshotsMatrix
                    .get(nextCodigo);
            Map<String, Object> dataSourceMap = new HashMap<String, Object>();
            String currentFollowedName = alocFotoRowList.get(0)
                    .getCurrentFollowedName();
            String description = defaultDescription.replace("{0}",
                    currentFollowedName);
            String description2 = defaultDescription2;

            // a revisao a ser recuperada eh a do dia anterior (o processo
            // roda 00:00 horas de D (dia), portanto preciso das revisoes de
            // D - 1 (dia - 1))
            Date revDate = DateUtils.addDays(DateUtils.truncate(new Date(),
                    Calendar.DAY_OF_MONTH), -1);
            List<String> authorsList = vwAuFlService.findAuthorsByCodAndDate(
                    nextCodigo, revDate, isClobFollow);
            String authorsString = vwAuFlService.getAuthorsString(authorsList);
            description2 += " " + authorsString;

            dataSourceMap.put("currentFollowedName", currentFollowedName);
            dataSourceMap.put("description", description);
            dataSourceMap.put("description2", description2);
            dataSourceMap.put("alocFotoRowList", alocFotoRowList);
            dataSourceMap.put("validityDateList", validityDateStringList);
            dataSourceMap.put("unSubscribeLabel", BundleUtil
                    .getBundle(Constants.LABEL_UNSUBSCRIBE));
            dataSourceMap.put("clickHereLabel", BundleUtil
                    .getBundle(Constants.LABEL_CLICK_HERE));
            dataSourceMap.put("toUnfollow", BundleUtil
                    .getBundle(isClobFollow ? Constants.LABEL_TO_UNFOLLOW_CLOB
                            : Constants.LABEL_TO_UNFOLLOW_ALL_YOU_MANAGE));

            // arquivo vm que sera convertido em mensagem de email
            // formato e-mail comum - html
            String message = mailSender.getTemplateMailMessage(template,
                    dataSourceMap);

            // formato e-mail para BlackBerry
            String descriptionBB = defaultDescriptionBB + " "
                    + currentFollowedName;
            String descriptionBB2 = defaultDescriptionBB2 + " " + authorsString;
            StringBuffer strb = this.getBlackBerryMailFormat(descriptionBB,
                    descriptionBB2, alocFotoRowList, valDateStrListBB,
                    isClobFollow);

            if (isClobFollow) {
                MapaAlocacao ma = mapaAlocService
                        .findMapaAlocacaoById(nextCodigo);
                if (ma == null) {
                    continue;
                }
                List<MapaAlocacaoPessoa> mapaAlocPessoaList = mapaAlocPessService
                        .findMapaAlocPessoaByMapa(ma.getCodigoMapaAlocacao());

                // se a retornou MapaAlocacaoPessoa, manda o email para as
                // Pessoas
                for (MapaAlocacaoPessoa mapaAlocPess : mapaAlocPessoaList) {
                    // pesquisa a pessoa para não dar problema de Lazy
                    Pessoa pessFlwer = pessoaService
                            .findFlwerByMapaAlocacaoPessoa(mapaAlocPess
                                    .getCodigoMapaAlocPessoa());
                    // formato e-mail comum - html
                    mailSender.sendHtmlMail(pessFlwer.getTextoEmail(), subject,
                            message.replace("unSubscribeLink",
                                    getURLUnSubscribeLink(mapaAlocPess
                                            .getCodigoMD5(),
                                            Constants.FOLLOW_TYPE_CLOB)));
                }
            } else {
                // Pessoa que está sendo seguida
                Pessoa pess = pessoaService.findPessoaByRecurso(recursoService
                        .findRecursoById(nextCodigo));
                // se não encontrou Pessoa vai para o próximo (casos quando o
                // Recurso é PapelAlocacao ou quando existe recurso mas não
                // Pessoa)
                if (pess == null) {
                    continue;
                }

                // busca o gerente da Pessoa corrente (que tem modificacoes)
                String codigoLoginMgr = pess.getCodigoLoginMgr();
                Pessoa pessMgrFlwer = pessoaService.findPessoaByLogin(codigoLoginMgr);
				// se não encontrou o Follower (Manager) vai para o próximo
				// (casos quando o gerente está inconsistente, digitação errada
				// por exemplo), guarda os logins inválidos em um String e envia e-mail de erro para o admin
                if (pessMgrFlwer == null) {
                	strBufInvalidLogin.append(codigoLoginMgr + ", ");
                	continue;
                }
                
                // se o gerente está seguindo seus gerenciados (flag
                // Pessoa.indicadorFollowOn = 'Y') envia e-mail para ele
                if (Constants.YES.equals(pessMgrFlwer.getIndicadorFollowOn())) {
                    // formato e-mail comum - html
                    mailSender.sendHtmlMail(pessMgrFlwer.getTextoEmail(), subject,
                            message.replace("unSubscribeLink",
                                    getURLUnSubscribeLink(pessMgrFlwer
                                            .getCodigoMD5(),
                                            Constants.FOLLOW_TYPE_PEOPLE)));
                }

                /*List<PessoaPessoa> pessPessList = pessPessService.findPessPessByPessoa(pess.getCodigoPessoa());

                // se a retornou PessoaPessoa, manda o email para as Pessoas
                // Followers
                for (PessoaPessoa pessPess : pessPessList) {
                    // pesquisa a pessoa para não dar problema de Lazy
                    Pessoa pessFlwer = pessoaService
                            .findFlwerByPessoaPessoa(pessPess
                                    .getCodigoPessoaPessoa());
                    // formato e-mail comum - html
                    mailSender.sendHtmlMail(pessFlwer.getTextoEmail(), subject,
                            message.replace("unSubscribeLink",
                                    getURLUnSubscribeLink(pessPess
                                            .getCodigoMD5(),
                                            Constants.FOLLOW_TYPE_PEOPLE)));

                    // formato e-mail para BlackBerry
                    mailSender.sendHtmlMail(pessFlwer.getTextoEmail(),
                            subjectBB, strb.toString());
                }*/
            }
        }
        
		// envia e-mail de erro para o admin com os logins inválidos do People
		// Follow 
		int strBufLength = strBufInvalidLogin.length();
		if (strBufLength > 0) {
			this.sendErrorMailPeopleFlw(strBufInvalidLogin.substring(0,
					strBufLength - 2));
		}
    }

    /**
     * Gera um template de e-mail formatado para BlackBerry.
     * 
     * @param descriptionBB
     *            - descrição cabeçalho do e-mail
     * @param descriptionBB2
     *            - descrição 2 cabeçalho do e-mail
     * @param alocFotoRowList
     *            - lista de alocações que contêm diferenças a serem exibidas
     * @param valDateStrListBB
     *            - lista / range de datas
     * @param isClobFollow
     *            - indicador se é follow de C-lob ou não (People)
     * 
     * @return um StringBuffer com o template do e-mail formatado
     */
    private StringBuffer getBlackBerryMailFormat(final String descriptionBB,
            final String descriptionBB2,
            final List<AlocacaoFotoRow> alocFotoRowList,
            final List<String> valDateStrListBB, final Boolean isClobFollow) {
        // cabeçalho do e-mail
        StringBuffer strb = new StringBuffer();
        strb
                .append("<html><body><p style='font-family: monospace, sans-serif;'>");
        strb.append("BlackBerry Format<br /><br />");
        strb.append(descriptionBB + "<br />");
        strb.append(descriptionBB2 + "<br /><br />");

        for (AlocacaoFotoRow row : alocFotoRowList) {
            // cabeçalho dos dados
            strb.append((isClobFollow ? "Resource: " : "C-Lob: ")
                    + row.getListedName() + "<br />");
            strb.append("Profile: " + row.getNomePerfilVendidoPv() + " >> "
                    + row.getNomePerfilVendidoCr() + "<br />");
            strb.append("Stage: " + row.getIndicadorEstagioPv() + " >> "
                    + row.getIndicadorEstagioCr() + "<br />");
            strb.append("UR%: "
                    + row.getValorUrPv().setScale(1, BigDecimal.ROUND_HALF_UP)
                    + " >> "
                    + row.getValorUrCr().setScale(1, BigDecimal.ROUND_HALF_UP)
                    + "<br />");
            strb.append("Allocation:<br />");

            // cabeçalho das alocações
            strb.append("|Stat|");
            for (String dt : valDateStrListBB) {
                strb.append(" " + dt + " |");
            }
            strb.append("<br />");

            // percentuais das alocações - Previous
            strb.append("|Prev|");
            List<AlocacaoFotoCell> alocFotoCellList = row
                    .getAlocacaoFotoCellList();
            for (AlocacaoFotoCell cell : alocFotoCellList) {
                strb.append(" "
                        + cell.getPercentualAlocPv().setScale(1,
                                BigDecimal.ROUND_HALF_UP) + " |");
            }
            strb.append("<br />");

            // percentuais das alocações - Current
            strb.append("|Curr|");
            for (AlocacaoFotoCell cell : alocFotoCellList) {
                strb.append(" "
                        + cell.getPercentualAlocCr().setScale(1,
                                BigDecimal.ROUND_HALF_UP) + " |");
            }
            strb.append("<br />");
            strb.append("___________________________________<br /><br />");
        }
        strb.append("</p></body></html>");

        return strb;
    }

    /**
     * Atualiza o status dos registros as tabelas Foto.
     * 
     * @return retorna o status da execução da atualização. false caso erro,
     *         true caso sucesso.
     */
    public Boolean updateStatusFotos() {
        Integer status = mapaAlocFotoDao.updateStatusFotos();

        // caso erro na atualização
        if (status == 0) {
            return Boolean.valueOf(false);
        }

        // sucesso na atualização
        return Boolean.valueOf(true);
    }

    /**
     * Envia um email informando algum erro no Job.
     * 
     * @param errorMsg
     *            - mensagem do erro
     * @param stackTrace
     *            - stackTrace do erro
     */
    public void sendSnapshotsErrorMail(final String errorMsg,
            final String stackTrace) {
        Map<String, Object> dataSource = new HashMap<String, Object>();
        dataSource.put("time", new Date());
        dataSource.put("message", errorMsg);
        dataSource.put("stackTrace", stackTrace);

        String message = mailSender.getTemplateMailMessage(BundleUtil
                .getBundle(Constants.BUNDLE_KEY_MAPA_FOTO_TEMPLATE_MAIL_ERROR),
                dataSource);

        String subject = BundleUtil
                .getBundle(Constants.BUNDLE_KEY_MAPA_FOTO_MAIL_FOLLOW_ERROR_SUBJECT);

        mailSender.sendHtmlMail(systemProperties
                .getProperty(Constants.EMAIL_ADDRESS_ERROR_KEY), subject,
                message);
    }
    
    /**
     * Envia um email quando há inconsitência no Follow de Pessoa.
     * 
     * @param errorMsg
     *            - mensagem do erro
     * @param stackTrace
     *            - stackTrace do erro
     */
	private void sendErrorMailPeopleFlw(final String loginError) {
		Map<String, Object> dataSource = new HashMap<String, Object>();
		dataSource.put("time", new Date());
		String errorMsg = BundleUtil
				.getBundle(Constants.BUNDLE_KEY_PESS_PESS_MAIL_FOLLOW_ERROR);
		dataSource.put("message", errorMsg.replace("{0}", loginError));

		String message = mailSender
				.getTemplateMailMessage(
						BundleUtil
								.getBundle(Constants.BUNDLE_KEY_MAPA_FOTO_TMPL_MAIL_ERROR_GENERIC),
						dataSource);

		String subject = BundleUtil
				.getBundle(Constants.BUNDLE_KEY_MAPA_FOTO_MAIL_FOLLOW_ERROR_GENERIC_SUBJECT);

		mailSender
				.sendHtmlMail(systemProperties
						.getProperty(Constants.EMAIL_ADDRESS_ERROR_KEY),
						subject, message);
	}

    /**
     * Retorna a URL do link de UnSubscribe do Follow por Email.
     * 
     * @param encriptedKey
     *            - código md5 do registro do Follow
     * @param flwType
     *            - tipo do Follow
     * 
     * @return retorna um String que representa a URL do link de UnSubscribe
     */
    private String getURLUnSubscribeLink(final String encriptedKey,
            final String flwType) {
        String url = this.getAppServerPathProduction()
                + Constants.URL_FOLLOW_UNSUBSCRIBE + "encriptedKey="
                + encriptedKey + "&flwType=" + flwType;

        return url;
    }

    /**
     * Retorna o url da aplicação no servidor verificando se é ambiente de
     * produção.
     * 
     * @return retornauma string que representa a url
     */
    private String getAppServerPathProduction() {
        String url;
        // se ambiente de produção
        if (PMSUtil.isProduction()) {
            url = systemProperties.getProperty(Constants.HOST_ENVIRONMENT);
            // caso não seja produção
        } else {
            try {
                url = FacesUtil.getAppServerPath();
            } catch (NullPointerException e) {
                // se for um JOB, não terá o AppServerPath
                url = "";
            }
        }
        return url;
    }

}