package com.ciandt.pms.audit.listener;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.*;
import com.ciandt.pms.persistence.dao.IFaturaDao;
import com.ciandt.pms.persistence.dao.IItemFaturaDao;
import com.ciandt.pms.persistence.dao.ILogAuditoriaDao;
import com.ciandt.pms.persistence.dao.IReceitaDao;
import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import javax.faces.context.FacesContext;
import javax.persistence.PostPersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 
 * A classe AuditoriaListener proporciona as funcionalidades de Log para as
 * mudanças que ocorrem nas entidades do banco de dados.
 * 
 * @since 22/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public class AuditoriaJpaListener {

    /**
     * Instancia do Dao do LogMapaAlocacao.
     */
    private ILogAuditoriaDao logAuditoriaDao;

    /** Constante de descrição do log na criação. */
    private static final String DESCRIPTION_PERSIST = "CREATE";

    /** Constante de descrição do log no update. */
    private static final String DESCRIPTION_PRE_UPDATE = "PRE-UPDATE";

    /** Constante de descrição do log no depois do delete. */
    private static final String DESCRIPTION_DELETE = "DELETE";

    /** Instancia do contesto da aplicação (Spring). */
    private ApplicationContext applicationContext;

    /**
     * Realiza a auditoria (log) da entidade após inseção.
     * 
     * @param entidade
     *            a ser auditada.
     */
    @PostPersist
    public void postPersist(final Object entidade) {
        if (entidade instanceof MapaAlocacao) {
            this.createLogMapaAlocacao((MapaAlocacao) entidade,
                    DESCRIPTION_PERSIST, Constants.MODULO_ALOCATION_MAP_NAME);
        } else if (entidade instanceof Receita) {

            this.createLogReceita((Receita) entidade, DESCRIPTION_PERSIST,
                    Constants.MODULO_RECEITA_NAME);

        } else if (entidade instanceof ItemReceita) {
            ItemReceita itemReceita = (ItemReceita) entidade;

            this.createLogReceita(itemReceita.getReceitaMoeda().getReceita(),
                    DESCRIPTION_PERSIST, Constants.MODULO_RECEITA_NAME);

        } else if (entidade instanceof ReceitaDealFiscal) {
            ReceitaDealFiscal rdf = (ReceitaDealFiscal) entidade;

            this.createLogReceita(rdf.getReceitaMoeda().getReceita(),
                    DESCRIPTION_PERSIST, Constants.MODULO_RECEITA_NAME);

        } else if (entidade instanceof AlocacaoPeriodo) {
            AlocacaoPeriodo ap = (AlocacaoPeriodo) entidade;

            this.createLogValidacao(ap, DESCRIPTION_PERSIST,
                    Constants.MODULO_VALIDACAO_PESSOA_NAME);
        } else if (entidade instanceof Fatura) {
            Fatura fatura = (Fatura) entidade;

            this.createLogFatura(fatura, DESCRIPTION_PERSIST,
                    Constants.MODULO_FATURA_NAME);
        } else if (entidade instanceof ItemFatura) {
            ItemFatura itemFatura = (ItemFatura) entidade;

            this.createLogFatura(itemFatura.getFatura(), DESCRIPTION_PERSIST,
                    Constants.MODULO_FATURA_NAME);
        } else {
            this.createDefaultLog(entidade, DESCRIPTION_PERSIST);
        }
    }

    /**
     * Realiza a auditoria(log) da entidade antes do update.
     * 
     * @param entidade
     *            a ser auditada.
     */
    @PreUpdate
    public void preUpdate(final Object entidade) {

        if (entidade instanceof MapaAlocacao) {
            this.createLogMapaAlocacao((MapaAlocacao) entidade,
                    DESCRIPTION_PRE_UPDATE, Constants.MODULO_ALOCATION_MAP_NAME);

        } else if (entidade instanceof Receita) {
            this.createLogReceita((Receita) entidade, DESCRIPTION_PRE_UPDATE,
                    Constants.MODULO_RECEITA_NAME);

        } else if (entidade instanceof ItemReceita) {
            ItemReceita itemReceita = (ItemReceita) entidade;
            /*
             * this.createLogReceita(itemReceita.getReceita(),
             * DESCRIPTION_PRE_UPDATE, Constants.MODULO_RECEITA_NAME);
             */
        } else if (entidade instanceof ReceitaDealFiscal) {
            ReceitaDealFiscal rdf = (ReceitaDealFiscal) entidade;
            /*
             * this.createLogReceita(rdf.getReceita(), DESCRIPTION_PRE_UPDATE,
             * Constants.MODULO_RECEITA_NAME);
             */
        } else if (entidade instanceof AlocacaoPeriodo) {
            AlocacaoPeriodo ap = (AlocacaoPeriodo) entidade;

            this.createLogValidacao(ap, DESCRIPTION_PRE_UPDATE,
                    Constants.MODULO_VALIDACAO_PESSOA_NAME);
        } else if (entidade instanceof Fatura) {
            Fatura fatura = (Fatura) entidade;

            this.createLogFatura(fatura, DESCRIPTION_PRE_UPDATE,
                    Constants.MODULO_FATURA_NAME);
        } else if (entidade instanceof ItemFatura) {
            ItemFatura itemFatura = (ItemFatura) entidade;

            this.createLogFatura(itemFatura.getFatura(),
                    DESCRIPTION_PRE_UPDATE, Constants.MODULO_FATURA_NAME);
        } else {
            this.createDefaultLog(entidade, DESCRIPTION_PRE_UPDATE);
        }
    }

    /**
     * Realiza a auditoria(log) da entidade após o update.
     * 
     * @param entidade
     *            a ser auditada.
     * 
     @PostUpdate public void postUpdate(final Object entidade) { if (entidade
     *             instanceof MapaAlocacao) {
     *             this.createLogMapaAlocacao((MapaAlocacao) entidade,
     *             DESCRIPTION_POS_UPDATE); } }
     */

    /**
     * Realiza a auditoria (log) da entidade após remoção.
     * 
     * @param entidade
     *            a ser auditada.
     */
    @PreRemove
    public void postRemove(final Object entidade) {
        if (entidade instanceof MapaAlocacao) {
            this.createLogMapaAlocacao((MapaAlocacao) entidade,
                    DESCRIPTION_DELETE, Constants.MODULO_ALOCATION_MAP_NAME);
        } else if (entidade instanceof Receita) {
            this.createLogReceita((Receita) entidade, DESCRIPTION_DELETE,
                    Constants.MODULO_RECEITA_NAME);

        } else if (entidade instanceof ItemReceita) {
            ItemReceita itemReceita = (ItemReceita) entidade;
            this.createLogReceita(itemReceita.getReceitaMoeda().getReceita(),
                    DESCRIPTION_DELETE, Constants.MODULO_RECEITA_NAME);

        } else if (entidade instanceof ReceitaDealFiscal) {
            ReceitaDealFiscal rdf = (ReceitaDealFiscal) entidade;
            this.createLogReceita(rdf.getReceitaMoeda().getReceita(),
                    DESCRIPTION_DELETE, Constants.MODULO_RECEITA_NAME);

        } else if (entidade instanceof AlocacaoPeriodo) {
            AlocacaoPeriodo ap = (AlocacaoPeriodo) entidade;

            this.createLogValidacao(ap, DESCRIPTION_DELETE,
                    Constants.MODULO_VALIDACAO_PESSOA_NAME);
        } else if (entidade instanceof Fatura) {
            Fatura fatura = (Fatura) entidade;

            this.createLogFatura(fatura, DESCRIPTION_DELETE,
                    Constants.MODULO_FATURA_NAME);
        } else if (entidade instanceof ItemFatura) {
            FacesContext context = FacesContext.getCurrentInstance();
            applicationContext =
                    FacesContextUtils.getWebApplicationContext(context);

            ItemFatura itemFatura =
                    ((IItemFaturaDao) applicationContext
                            .getBean("itemFaturaDao"))
                            .findById(((ItemFatura) entidade)
                                    .getCodigoItemFatura());

            this.createLogFatura(itemFatura.getFatura(), DESCRIPTION_DELETE,
                    Constants.MODULO_FATURA_NAME);
        } else {
            this.createDefaultLog(entidade, DESCRIPTION_DELETE);
        }
    }

    /**
     * Log do Persiste do MapaAlocacao.
     * 
     * @param mapaAlocacao
     *            para realizar a auditoria.
     * @param description
     *            - Descrição do log do MapaAlocacao.
     * @param moduloName
     *            - Nome do modulo.
     */
    private void createLogMapaAlocacao(final MapaAlocacao mapaAlocacao,
            final String description, final String moduloName) {
        FacesContext context = FacesContext.getCurrentInstance();
        // Essa condição é necessária para os testes unitarios não falharem.
        // Pois no testes unitarios não existe contexto.
        if (context != null) {
            applicationContext =
                    FacesContextUtils.getWebApplicationContext(context);
            // Pega o login do usuario logado no sistema
            String username =
                    (String) context.getExternalContext().getSessionMap()
                            .get("SPRING_SECURITY_LAST_USERNAME");

            Properties systemProperties =
                    (Properties) applicationContext.getBean("systemProperties");
            LogAuditoria log = new LogAuditoria();

            log.setCodigoAutor(username);
            log.setCodigoEntidade(mapaAlocacao.getCodigoMapaAlocacao());
            log.setDataAcao(new Date());

            String mapaStr = mapaAlocacao.toString();
            Integer maxLength =
                    Integer.parseInt((String) systemProperties
                            .get("string.max.length"));
            if (mapaStr.length() > maxLength) {
                mapaStr = mapaStr.substring(0, maxLength);
            }
            log.setTextoConteudo(mapaStr);
            log.setTextoAcao(description);
            log.setTextoModulo(systemProperties.getProperty(moduloName));

            logAuditoriaDao =
                    (ILogAuditoriaDao) applicationContext
                            .getBean("logAuditoriaDao");

            logAuditoriaDao.create(log);
        }
    }

    /**
     * Log do Persiste Validacao.
     * 
     * @param ap
     *            para realizar a auditoria.
     * @param description
     *            - Descrição do log do MapaAlocacao.
     * @param moduloName
     *            - Nome do modulo.
     */
    private void createLogValidacao(final AlocacaoPeriodo ap,
            final String description, final String moduloName) {
        FacesContext context = FacesContext.getCurrentInstance();
        // Essa condição é necessária para os testes unitarios não falharem.
        // Pois no testes unitarios não existe contexto.
        if (context != null) {
            applicationContext =
                    FacesContextUtils.getWebApplicationContext(context);
            // Pega o login do usuario logado no sistema
            String username =
                    (String) context.getExternalContext().getSessionMap()
                            .get("SPRING_SECURITY_LAST_USERNAME");

            Properties systemProperties =
                    (Properties) applicationContext.getBean("systemProperties");

            logAuditoriaDao =
                    (ILogAuditoriaDao) applicationContext
                            .getBean("logAuditoriaDao");

            LogAuditoria log = new LogAuditoria();

            log.setTextoConteudo("login: "
                    + ap.getAlocacao().getRecurso().getCodigoMnemonico()
                    + ", date: "
                    + ap.getDataAlocacaoPeriodo()
                    + ", alocation(%): "
                    + ap.getPercentualAlocacaoPeriodo()
                    + ", contract-lob: "
                    + ap.getAlocacao().getMapaAlocacao().getContratoPratica()
                            .getNomeContratoPratica());

            log.setCodigoAutor(username);
            log.setCodigoEntidade(ap.getCodigoAlocacaoPeriodo());
            log.setDataAcao(new Date());
            log.setTextoAcao(description);
            log.setTextoModulo(systemProperties.getProperty(moduloName));

            logAuditoriaDao.create(log);
        }
    }

    /**
     * Log do Persiste do Receita.
     * 
     * @param receita
     *            para realizar a auditoria.
     * @param description
     *            - Descrição do log do MapaAlocacao.
     * @param moduloName
     *            - Nome do modulo.
     */
    private void createLogReceita(final Receita receita,
            final String description, final String moduloName) {
        FacesContext context = FacesContext.getCurrentInstance();
        // Essa condição é necessária para os testes unitarios não falharem.
        // Pois no testes unitarios não existe contexto.
        if (context != null) {
            applicationContext =
                    FacesContextUtils.getWebApplicationContext(context);
            // Pega o login do usuario logado no sistema
            String username =
                    (String) context.getExternalContext().getSessionMap()
                            .get("SPRING_SECURITY_LAST_USERNAME");

            Properties systemProperties =
                    (Properties) applicationContext.getBean("systemProperties");

            IReceitaDao receitaDao =
                    (IReceitaDao) applicationContext.getBean("receitaDao");

            Receita rec = receitaDao.findById(receita.getCodigoReceita());

            LogAuditoria log = new LogAuditoria();

            log.setTextoConteudo(rec.getCodigoReceita() + ", "
                    + rec.getContratoPratica().getNomeContratoPratica() + ", "
                    + rec.getDataMes());

            log.setCodigoAutor(username);
            log.setCodigoEntidade(rec.getCodigoReceita());
            log.setDataAcao(new Date());
            log.setTextoAcao(description);
            log.setTextoModulo(systemProperties.getProperty(moduloName));

            logAuditoriaDao =
                    (ILogAuditoriaDao) applicationContext
                            .getBean("logAuditoriaDao");

            logAuditoriaDao.create(log);
        }
    }

    /**
     * Log do Persiste do Receita.
     * 
     * @param fatura
     *            para realizar a auditoria.
     * @param description
     *            - Descrição do log do MapaAlocacao.
     * @param moduloName
     *            - Nome do modulo.
     */
    private void createLogFatura(final Fatura fatura, final String description,
            final String moduloName) {
        FacesContext context = FacesContext.getCurrentInstance();
        // Essa condição é necessária para os testes unitarios não falharem.
        // Pois no testes unitarios não existe contexto.
        if (context != null) {
            applicationContext =
                    FacesContextUtils.getWebApplicationContext(context);
            // Pega o login do usuario logado no sistema
            String username =
                    (String) context.getExternalContext().getSessionMap()
                            .get("SPRING_SECURITY_LAST_USERNAME");

            LogAuditoria log = new LogAuditoria();

            IFaturaDao faturaDao =
                    (IFaturaDao) applicationContext.getBean("faturaDao");
            IItemFaturaDao itemfaturaDao =
                    (IItemFaturaDao) applicationContext
                            .getBean("itemFaturaDao");

            Properties systemProperties =
                    (Properties) applicationContext.getBean("systemProperties");

            Fatura fat = faturaDao.findById(fatura.getCodigoFatura());

            DealFiscal dealFiscal = fat.getDealFiscal();

            StringBuilder sb = new StringBuilder();
            sb.append("Fatura");
            sb.append("\nMSA:").append(dealFiscal.getMsa().getNomeMsa());
            sb.append("\nAE:").append(fat.getCodigoLoginAe());
            sb.append("\nData Previsao:").append(fat.getDataPrevisao());
            sb.append("\nStatus:").append(fat.getIndicadorStatus());
            sb.append("\nPedido ERP:").append(fat.getCodigoErpPedido());
            sb.append("\nData Cancelamento:").append(fat.getDataCancelamento());
            sb.append("\nId Deal:").append(
                    fat.getDealFiscal().getCodigoDealFiscal());
            sb.append("\nMoeda:").append(fat.getMoeda().getNomeMoeda());

            List<ItemFatura> itemFaturas = itemfaturaDao.findByFatura(fat);
            sb.append("\n\n--- Itens:");
            for (ItemFatura itemFatura : itemFaturas) {
                sb.append("\nCod.Item:").append(
                        itemFatura.getCodigoItemFatura());
                sb.append("\nData Pagto.:").append(
                        itemFatura.getDataPagamento());
                sb.append("\nNum Doc.:").append(
                        itemFatura.getNumeroNotaFiscal());
                sb.append("\nNum Pedido:").append(itemFatura.getNumeroPedido());
                sb.append("\nFonte Receita:").append(
                        itemFatura.getFonteReceita().getNomeFonteReceita());
                sb.append("\nTipo Servico:").append(
                        itemFatura.getTipoServico().getNomeTipoServico());
                sb.append("\nValor:").append(itemFatura.getValorItem());
                sb.append("\n");
            }
            sb.append("\n==Fim==");

            String faturaStr = sb.toString();
            Integer maxLength =
                    Integer.parseInt((String) systemProperties
                            .get("string.max.length"));
            if (faturaStr.length() > maxLength) {
                faturaStr = faturaStr.substring(0, maxLength);
            }

            log.setTextoConteudo(faturaStr);
            log.setCodigoAutor(username);
            log.setCodigoEntidade(fatura.getCodigoFatura());
            log.setDataAcao(new Date());
            log.setTextoAcao(description);
            log.setTextoModulo(systemProperties.getProperty(moduloName));

            logAuditoriaDao =
                    (ILogAuditoriaDao) applicationContext
                            .getBean("logAuditoriaDao");

            logAuditoriaDao.create(log);
        }
    }

    /**
     * Cria um log default.
     * 
     * @param obj
     *            - Objeto não definido no log
     * 
     * @param description
     *            descrição da ação.
     */
    private void createDefaultLog(final Object obj, final String description) {
        FacesContext context = FacesContext.getCurrentInstance();
        // Essa condição é necessária para os testes unitarios não falharem.
        // Pois no testes unitarios não existe contexto.
        if (context != null) {
            applicationContext =
                    FacesContextUtils.getWebApplicationContext(context);
            // Pega o login do usuario logado no sistema
            String username =
                    (String) context.getExternalContext().getSessionMap()
                            .get("SPRING_SECURITY_LAST_USERNAME");

            LogAuditoria log = new LogAuditoria();

            log.setTextoConteudo(obj.toString());
            log.setCodigoAutor(username);
            log.setCodigoEntidade(0L);
            log.setDataAcao(new Date());
            log.setTextoAcao(description);
            log.setTextoModulo("Módulo desconhecido");

            logAuditoriaDao =
                    (ILogAuditoriaDao) applicationContext
                            .getBean("logAuditoriaDao");

            logAuditoriaDao.create(log);
        }
    }

    /**
     * @return the logMapaAlocaoDao
     */
    public ILogAuditoriaDao getLogAuditoriaDao() {
        return logAuditoriaDao;
    }

    /**
     * @param logAuditoriaDao
     *            the LogAuditoriaDao to set.
     */
    public void setLogAuditoriaDao(final ILogAuditoriaDao logAuditoriaDao) {
        this.logAuditoriaDao = logAuditoriaDao;
    }

}