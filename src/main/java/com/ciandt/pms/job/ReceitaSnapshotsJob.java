package com.ciandt.pms.job;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IReceitaResultadoService;
import com.ciandt.pms.business.service.IVwPmsReceitaResultadoService;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ReceitaResultado;
import com.ciandt.pms.model.VwPmsReceitaResultado;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.JobUtil;
import com.google.common.base.Throwables;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


/**
 * 
 * A classe ReceitaSnapshotsJob executa um job que realiza fotos da receita para
 * a tabela {@link ReceitaResultado}.
 * 
 * @since 18/01/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
public class ReceitaSnapshotsJob {

    private Logger logger = LoggerFactory.getLogger(ReceitaSnapshotsJob.class);

    @Autowired
    private JobUtil jobUtil;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /** Instancia do serviço. */
    @Autowired
    private IVwPmsReceitaResultadoService vwPmsReceitaResultadoService;

    /** Instancia do serviço. */
    @Autowired
    private IReceitaResultadoService receitaResultadoService;

    /** Instancia do serviço. */
    @Autowired
    private IContratoPraticaService contratoPraticaService;

    /**
     * Executa as fotos da tabela receita para a receita realizada.
     */
    public void snapshotReceita() {

        if (!jobUtil.isJobActive(Constants.JOB_RECEITA_SNAPSHOTS_ACTIVE)) {
            logger.warn("JOB", "Job is not active on config.properties. Set the "
                    .concat(Constants.JOB_RECEITA_SNAPSHOTS_ACTIVE)
                    .concat(" property to 'true' for active it."));
            return;
        }

        try {
            logger.info("JOB", "Starting Job snapshotReceita - " + new Date());

            // Setta o periodo de busca
            Date dataMes = DateUtils.addMonths(DateUtil.getDate(new Date()), -1);

            // Obtem os registros da view para a foto
            List<VwPmsReceitaResultado> snapshotList =
                    vwPmsReceitaResultadoService
                            .findVwPmsReceitaResultadoByDataMes(dataMes);

            // Encerra a rotina caso nao exista registros para foto
            if (snapshotList == null || snapshotList.isEmpty()) {
                logger.info("JOB", "End of Job snapshotReceita - no records to snapshot - " + new Date());
                return;
            } else {

                // Converte a lista de VwPmsReceitaResultado da view em
                // uma
                // lista de ReceitaResultado
                List<ReceitaResultado> receitaResultadoList =
                        new ArrayList<ReceitaResultado>();

                for (VwPmsReceitaResultado vw : snapshotList) {
                    ReceitaResultado rs = new ReceitaResultado();

                    ContratoPratica contratoPratica =
                            contratoPraticaService
                                    .findContratoPraticaById(vw.getId()
                                            .getCodigoContratoPratica());

                    rs.setContratoPratica(contratoPratica);
                    rs.setDataMes(vw.getId().getDataAlocacaoPeriodo());
                    rs.setMotivoResultado(null);
                    rs.setNumeroFtePlanejado(vw.getId().getFte());
                    rs
                            .setValorReceitaPlanejada(vw.getId()
                                    .getReceita());
                    rs.setValorReceitaRealizada(BigDecimal.valueOf(0));
                    rs.setDataFoto(new Date());

                    // Compoe a lista de foto
                    receitaResultadoList.add(rs);
                }

                // cria as fotos
                receitaResultadoService
                        .snapshotReceitaResultado(receitaResultadoList);

                logger.info("JOB", "End of Job snapshotReceita - " + new Date());
            }
        } catch (Exception e) {
            logger.error("JOB", Throwables.getStackTraceAsString(e));
        }

    }
}