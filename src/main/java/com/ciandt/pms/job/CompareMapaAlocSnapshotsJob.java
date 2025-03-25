package com.ciandt.pms.job;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IMapaAlocacaoFotoService;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.vo.AlocacaoFotoRow;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.JobUtil;
import com.google.common.base.Throwables;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * 
 * A classe CompareMapaAlocSnapshotsJob proporciona as funcionalidades de
 * comparação das fotos dos Mapas e envia e-mail para os followers.
 * 
 * @since 08/03/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public class CompareMapaAlocSnapshotsJob {

    private Logger logger = LoggerFactory.getLogger(CompareMapaAlocSnapshotsJob.class);

    @Autowired
    private JobUtil jobUtil;

    /** Instância do MapaAlocacaoService. */
    @Autowired
    private IMapaAlocacaoService mapaAlocService;

    /** Instância do MapaAlocacaoFotoService. */
    @Autowired
    private IMapaAlocacaoFotoService mapaAlocFotoService;
    
    /** Instância do ModuloService. */
    @Autowired
    private IModuloService moduloService;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Método que faz comparação das fotos dos Mapas e envia e-mails para os
     * followers.
     */
    public void compareMapaAlocSnapshots() {

        if (!jobUtil.isJobActive(Constants.JOB_MAPA_ALOC_SNAPSHOTS_FOLLOW_ACTIVE)) {
            logger.warn("JOB", "Job is not active on config.properties. Set the "
                    .concat(Constants.JOB_MAPA_ALOC_SNAPSHOTS_FOLLOW_ACTIVE)
                    .concat(" property to 'true' for active it."));
            return;
        }

        try {
            logger.info("JOB", "Starting Job compareMapaAlocSnapshots - " + new Date());

            // passo 1 - às 0 hrs faz a cópia do MapaAlocacao e grava nas
            // tabelas "Foto" com o status Current (CR) busca os mapas com o
            // range de AlocacaoPeriodo: startDate até infinito, onde
            // starDate na gravação das snaps, deve ser Sysdate - 6
            logger.info("JOB", "Job compareMapaAlocSnapshots - 1st step - " + new Date());
            Date startDateSnaps = DateUtils.addMonths(DateUtil .getDate(new Date()), -6);
            List<MapaAlocacao> mapaAlocList = mapaAlocService
                    .findMapaAlocAllPBByRangeMonths(startDateSnaps);
            mapaAlocFotoService.createMapaAlocFotoList(mapaAlocList);

            // a data a ser utilizada como referência é:
            // Maior de (mês fechado + 1; mês atual (sysdate) -1)
            Date nextClosingDate = DateUtils.addMonths(moduloService
                    .getClosingDateMapaAlocacao(), 1);
            Date previousCurrentDate = DateUtils.addMonths(DateUtil
                    .getDate(new Date()), -1);
            Date startDate = DateUtil.getMaxDate(nextClosingDate,
                    previousCurrentDate);

            // passo 2 - compara as fotos com o status Previous (PV) e
            // Current (CR) retornando as diferenças e faz a lógica para
            // interpretar
            // o resultado e prepara a matriz para ser enviada via e-mail
            logger.info("JOB", "Job compareMapaAlocSnapshots - 2nd step - " + new Date());
            Map<Long, List<AlocacaoFotoRow>> mapaAlocSnapshotsMatrixClob = mapaAlocFotoService
                    .prepareMapaAlocSnapshotsMatrix(startDate, Boolean.TRUE);
            Map<Long, List<AlocacaoFotoRow>> mapaAlocSnapshotsMatrixPeople = mapaAlocFotoService
                    .prepareMapaAlocSnapshotsMatrix(startDate, Boolean.FALSE);

            // passo 3 - monta e envia e-mails para os followers
            logger.info("JOB", "Job compareMapaAlocSnapshots - 3rd step - " + new Date());
            mapaAlocFotoService.sendMapaAlocSnapshotsMail(
                    mapaAlocSnapshotsMatrixClob, startDate, Boolean.TRUE);
            mapaAlocFotoService.sendMapaAlocSnapshotsMail(
                    mapaAlocSnapshotsMatrixPeople, startDate, Boolean.TRUE);

            // passo 4 - remove a foto com status Previous (PV) e altera a
            // Current para Previous
            logger.info("JOB", "Job compareMapaAlocSnapshots - 4th step - " + new Date());
            mapaAlocFotoService.updateStatusFotos();

            logger.info("JOB", "End of Job compareMapaAlocSnapshots - " + new Date());
        } catch (Exception e) {
            // se der algum erro no Job, envia um email informando o erro
            logger.error("JOB", Throwables.getStackTraceAsString(e));

            StringBuffer stackTrace = new StringBuffer();

            // trace da exceção
            StackTraceElement[] stElemList = e.getStackTrace();
            for (StackTraceElement stElem : stElemList) {
                stackTrace.append(stElem.toString() + "<br />");
            }

            mapaAlocFotoService.sendSnapshotsErrorMail(e.toString(),
                    stackTrace.toString());
        }
    }
}