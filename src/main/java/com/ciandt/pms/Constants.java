package com.ciandt.pms;

import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.FichaReajuste;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Classe que contem todas as constantes do sistema.
 */
public final class Constants {

    /**
     * Lista com os dias do mes.
     */
    public static final List<String> MONTH_DAY_LIST = Arrays.asList("01", "02",
            "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
    /**
     * Lista com os dias do mes.
     */
    public static final List<String> BILLABILITY_LIST = Arrays.asList("Billable", "Fixed");
    /**
     * Nome da chave do session map que retorna o login do usu�rio logado
     */
    public static final String SPRING_SECURITY_LAST_USERNAME = "SPRING_SECURITY_LAST_USERNAME";
    /**
     * Caminho dos arquivos de upload de plantao por valor
     */
    public static final String UPLOAD_PLANTAO_POR_VALOR_DESTINATION_PAHT = "upload.plantao_valor.destination.path";
    /**
     * Caminho dos arquivos de upload de hora extra por valor
     */
    public static final String UPLOAD_HORA_EXTRA_POR_VALOR_DESTINATION_PAHT = "upload.hora_extra_valor.destination.path";
    /**
     * Identificador de saida para pagina de erro generico.
     */
    public static final String DEFAULT_ERROR_OUTCOME = "genericError";

    /** Indicadores/identificadores. */

    /** Identificador de saida para pagina de erro de acesso negado. */
    public static final String DEFAULT_ACCESS_DENIED_ERROR_OUTCOME = "accessDeniedError";

    /** Identificador para bundle. */
    public static final String DEFAULT_RESOURCE_BUNDLE = "resourceBundle";

    /** Identificador para incio de chaves de bundle. */
    public static final String DEFAULT_BUNDLES_KEY_BEGIN = "_nls";

    /** Bundle do locale defult da aplica��o. */
    public static final String LOCALE_DEFAULT_BUNDLE = "locale.en_us";

    /** Bundle do formato padrao de datas - simplificado (m�s/ano) (MMM/yyyy). */
    public static final String DEFAULT_DATE_PATTERN_SIMPLE = "_nls.date.pattern.simple";

    public static final String DEFAULT_DATE_PATTERN_COMPLETE = "_nls.date.pattern.complete";

    public static final String GENERIC_ERROR = "_nls.msg.error.genericError";
    /**
     * Bundle do formato padrao de datas - calend�rio (m�s dia, ano) (MMM d,
     * yyyy).
     */
    public static final String DEFAULT_CALENDAR_PATTERN = "_nls.calendar.pattern";

    /**
     * Bundle do formato padrao de datas - simplificado (dia/m�s/ano
     * 24hora:min:seg) (dd/MM/yyyy HH:mm:ss).
     */
    public static final String DEFAULT_DATE_PATTERN_FULL = "_nls.date.pattern.full";

    /** Bundle do formato data (MM/yyyy) */
    public static final String DEFAULT_DATE_PATTERN_MONTH_YEAR = "_nls.date.pattern.month.year";

    /** Bundle do Locale padr�o (en/US). */
    public static final String DEFAULT_CALENDAR_LOCALE = "_nls.calendar.locale";

    /** Identificador para incio de chaves de bundle. */
    public static final String NOT_AVAILABLE_VALUE = "_nls.not_available.label";
    /*
     * Mensagem de erro após atingir máxxmio de tentaivas
     */
    public static final String MAX_LOGIN_ATTEMPTS_ERROR_MESSAGE = "_nls.max.login.attempts.reached";

    /*
     * Atributo do JSESSIONID para identificar a quantidade de tentativas de login
     */
    public static final String LOGIN_ATTEMPTS = "LOGIN_ATTEMPTS";
    public static final String WAIT_ATTEMPT_TIME = "WAIT_ATTEMPT_TIME";

    /** Indicador para valor Success - "S". */
    public static final String SUCCESS = "S";

    /** Indicador para valor Aborted - "A". */
    public static final String ABORTED = "A";

    /** Indicador para valor Error - "E". */
    public static final String ERROR = "E";

    /** Indicador para valor "todos" (ALL). */
    public static final String ALL = "ALL";

    /** Indicador para valor Ativo - "A". */
    public static final String ACTIVE = "A";

    public static final String INACTIVE = "I";

    /** Indicador para o novo valor Ativo - "ACTI". */
    public static final String NEW_ACTIVE = "ACTI";

    /** Indicador para o novo valor Inativo - "INAC". */
    public static final String NEW_INACTIVE = "INAC";

    /** Indicador para valor Prospect - "P". */
    public static final String PROSPECT = "P";

    public static final String EXPAND = "X";

	/** Indicador para valor Request Inactivation - "REIN". */
	public static final String REQUEST_INACTIVATION = "REIN";
	public static final String REQUEST_INACTIVATION_OLD = "R";

    /** Indicador para valor Request Creation - "RECR". */
    public static final String REQUEST_CREATION = "RECR";

    /** Indicador para valor Integrating Creation - "INCR". */
    public static final String INTEGRATING_CREATION = "INCR";

    /** Indicador para valor Integrating Inactivation - "ININ". */
    public static final String INTEGRATING_INACTIVATION = "ININ";

    /** Indicador para valor Closed - "CLOS". */
    public static final String CLOSED = "CLOS";

    /** Indicador para o valor "todos" (Ativo("A") e Inativo("I")). */
    public static final String ACTIVE_INACTIVE = "AI";

    /** Indicador para valor Yes - "Y". */
    public static final String YES = "Y";

    /** Indicador para valor No - "N". */
    public static final String NO = "N";

    /** Indicador para valor SIM - "S". */
    public static final String SIM = "S";

    /** Indicador para valor Reembolsavel - "RB". */
    public static final String REEMBOLSAVEL = "RB";

    /** Indicador para valor No - "N". */
    public static final String NOT_REEMBOLSAVEL = "NM";

    /** Indicador para status Previous - "PV". */
    public static final String STATUS_PREVIOUS = "PV";

    /** Indicador para status Current - "CR". */
    public static final String STATUS_CURRENT = "CR";

    public static final Long EXPENSE_REEMB_REVENUE_SOURCE = 2L;
    public static final Long CONTRACTOR_REVENUE_SOURCE = 3L;

    /** Lista com os indicadores Ativo("A") e Inativo("I"). */
    public static final List<String> ACTIVE_INACTIVE_VALUES = Arrays.asList(
            ACTIVE_INACTIVE, ACTIVE, INACTIVE, PROSPECT);
    /**
     * Lista com os indicadores Ativo("A") e Inativo("I") e Ambos(AI).
     */
    public static final List<String> ALL_ACTIVE_INACTIVE_VALUES = Arrays.asList(
            ACTIVE_INACTIVE, ACTIVE, INACTIVE);

    /** Lista com os indicadores Ativo("A") e Inativo("I"). */
    public static final List<String> ACTIVE_INACTIVE_VALUES_ONLY = Arrays.asList(
            ACTIVE, INACTIVE);
    /**
     * Indicador da vers�o DRAFT do mapa de aloca��o.
     */
    public static final String VERSION_DRAFT = "DF";
    /**
     * Indicador da vers�o PUBLISHED do mapa de aloca��o.
     */
    public static final String VERSION_PUBLISHED = "PB";
    /**
     * Indicador da vers�o VALIDATE do mapa de aloca��o.
     */
    public static final String VERSION_VALIDATE = "VD";
    /**
     * Indicador da vers�o Unavailable.
     */
    public static final String VERSION_UNAVAILABLE = "NA";
    /**
     * Indicador da vers�o WORKING do HorasFaturadasDeal.
     */
    public static final String VERSION_HORAS_FAT_DEAL_WORKING = "WK";
    /**
     * Indicador da vers�o PUBLISHED do HorasFaturadasDeal.
     */
    public static final String VERSION_HORAS_FAT_DEAL_PUBLISHED = "PB";
    /**
     * Indicador da versao WORKING do Receita.
     */
    public static final String VERSION_RECEITA_WORKING = "WK";
    /**
     * Indicador da versao PUBLISHED do Receita.
     */
    public static final String VERSION_RECEITA_PUBLISHED = "PB";
    /**
     * Indicador da versao PENDING do Receita.
     */
    public static final String VERSION_RECEITA_PENDING = "PD";
    /**
     * Indicador da versao INTEGRATED do Receita.
     */
    public static final String VERSION_RECEITA_INTEGRATED = "IN";
    /**
     * Indicador da versao DRAFT do Receita.
     */
    public static final String VERSION_RECEITA_DRAFT = "DF";
    /**
     * Indicador da versao FORECAST do Receita.
     */
    public static final String VERSION_RECEITA_FORECAST = "FC";
    /**
     * Indicador do tipo da comissao fatura.
     */
    public static final String COMISSAO_TYPE_INVOICE = "IN";
    /**
     * Indicador do tipo da comissao fatura.
     */
    public static final String COMISSAO_TYPE_ACCELERATOR = "AC";
    /**
     * Mensagem de sucesso ao integrar as receitas.
     */
    public static final String INTEGRACAO_RECEITA_MSG_WARNING_MORE_THAN_ONE_SERVICE_TYPE = "_nls.receita_integracao.msg.some_deal_fiscal_more_service_type";
    /**
     * Mensagem de warning na cria��o de receitas de licen�a.
     */
    public static final String RECEITA_LICENCA_WARNING_DEAL_FISCAL_TYPE = "_nls.receita_parcelada.msg.warning.fiscal_deal_type";
    /**
     * Mensagem de sucesso na dele��o de receitas de licen�a.
     */
    public static final String RECEITA_LICENCA_SUCCESS_DELETE = "_nls.receita_licenca.msg.success.revenue_value";
    /**
     * Mensagem de erro tipo licen�a inv�lido.
     */
    public static final String RECEITA_MSG_ERROR_TIPO_INVALIDO = "_nls.receita_integracao.msg.error.revenue.type.invalid";
    public static final String RECEITA_MSG_ERROR_EXPORT_REPORT = "_nls.receita_relatorio.msg.error.export_report";
    /**
     * Indicador do tipo de receita
     */
    public static final String RECEITA_TYPE_ABBREVIATION_LICENCE = "LI";
    /**
     * Indicador do tipo de receita
     */
    public static final String RECEITA_TYPE_ABBREVIATION_SERVICE = "SE";
    /**
     * Indicador do tipo de receita
     */
    public static final String RECEITA_TYPE_LICENCE = "License";
    /**
     * Indicador do tipo de receita
     */
    public static final String RECEITA_TYPE_SERVICE = "Service";
    public static final String REVENUE_MSG_CREATED_SUCCESSFULLY = "_nls.receita_parcelada.msg.success.created";
    /**
     * Mensagem de erro contrato pratica inativo.
     */
    public static final String RECEITA_MSG_ERROR_CONTRATO_PRATICA_INATIVO = "_nls.receita_integracao.msg.error.revenue.clob.inactive";
    /**
     * Mensagem de erro centro custo inativo.
     */
    public static final String RECEITA_MSG_ERROR_CENTRO_CUSTO_INATIVO = "_nls.receita_integracao.msg.error.revenue.centro.custo.inactive";
    /**
     * Mensagem de erro projeto inativo.
     */
    public static final String RECEITA_MSG_ERROR_PROJETO_INATIVO = "_nls.receita_integracao.msg.error.revenue.projeto.inactive";

    /**
     * AWS mail config
     */
    public static final String AWS_MAIL_QUEUE_URL = "aws.mail.queueUrl";
    public static final String AWS_MAIL_KEY = "aws.mail.key";
    public static final String AWS_MAIL_SECRET_KEY = "aws.mail.secretKey";
    public static final String AWS_MAIL_REGION = "aws.mail.awsRegion";
    /**
     * Indicador do est�gio ABERTO da comiss�o.
     */
    public static final String COMISSAO_STATUS_OPEN = "OP";

    /**
     * Status da comiss�o.
     */
    /**
     * indicador de status open para comissao de item fatura.
     */
    public static final String COMISSAO_STATUS_ITEM_OPEN = "Open";
    /**
     * Bundle do est�gio ABERTO da comiss�o.
     */
    public static final String COMISSAO_BUNDLE_STATUS_OPEN = "_nls.comissao_acelerador.status.open";
    /**
     * Indicador do est�gio REQUISITADO APROVACAO da comiss�o.
     */
    public static final String COMISSAO_STATUS_REQUEST = "RQ";
    /**
     * Bundle do est�gio ABERTO da comiss�o.
     */
    public static final String COMISSAO_BUNDLE_STATUS_REQUEST = "_nls.comissao_acelerador.status.requested";
    /**
     * Indicador do est�gio REVISAO da comiss�o.
     */
    public static final String COMISSAO_STATUS_REVIEW = "RV";
    /**
     * Bundle do est�gio REVISAO da comiss�o.
     */
    public static final String COMISSAO_BUNDLE_STATUS_REVIEW = "_nls.comissao_acelerador.status.review";
    /**
     * Indicador do est�gio APROVADA da comiss�o.
     */
    public static final String COMISSAO_STATUS_APPROVED = "AP";
    /**
     * Bundle do est�gio APROVADA da comiss�o.
     */
    public static final String COMISSAO_ALREADY_EXISTS = "_nls.comissao.already.exists";
    /**
     * Bundle do est�gio APROVADA da comiss�o.
     */
    public static final String COMISSAO_BUNDLE_STATUS_APPROVED = "_nls.comissao_acelerador.status.approved";
    /**
     * Indicador do est�gio EMCAMINHADA da comiss�o.
     */
    public static final String COMISSAO_STATUS_FORWARDED = "FW";
    /**
     * Bundle do est�gio EMCAMINHADA da comiss�o.
     */
    public static final String COMISSAO_BUNDLE_STATUS_FORWARDED = "_nls.comissao_acelerador.status.forwarded";
    /**
     * Indicador do est�gio EMCAMINHADA da comiss�o.
     */
    public static final String COMISSAO_STATUS_COMISSIONED = "CM";
    /**
     * Bundle do est�gio EMCAMINHADA da comiss�o.
     */
    public static final String COMISSAO_BUNDLE_STATUS_COMISSIONED = "_nls.comissao_acelerador.status.comissioned";
    /**
     * Indicador para valor "todos" vers�es do MapaAlocacao (DRAFT, PUBLISH e
     * VALIDATE).
     */
    public static final String VERSION_ALL = "ALL";
    /**
     * Lista com os indicadores (DRAFT, PUBLISHED e VALIDATE).
     */
    public static final List<String> VERSION_ALL_VALUES = Arrays.asList(
            VERSION_ALL, VERSION_DRAFT, VERSION_PUBLISHED, VERSION_VALIDATE);
    /**
     * Indicador do est�gio COMMITED da aloca��o.
     */
    public static final String STAGE_COMMITED = "CM";
    /**
     * Indicador do est�gio RESERVED da aloca��o.
     */
    public static final String STAGE_RESERVED = "RV";
    /**
     * Indicador do est�gio PROSPECTED_LOW da aloca��o.
     */
    public static final String STAGE_PROSPECTED_LOW = "PL";
    /**
     * Indicador do est�gio PROSPECTED_HIGH da aloca��o.
     */
    public static final String STAGE_PROSPECTED_HIGH = "PH";
    /**
     * Indicador do est�gio PROSPECTED da aloca��o.
     */
    public static final String STAGE_PROSPECTED = "PR";
    /**
     * Indicador do tipo de Recurso - PapelAlocacao.
     */
    public static final String RESOURCE_TYPE_PA = "PA";
    /**
     * Indicador do tipo de Recurso - Pessoa.
     */
    public static final String RESOURCE_TYPE_PE = "PE";
    /**
     * Indicador do status COMPLETE do ContratoPratica.
     */
    public static final String STATUS_COMPLETE = "C";
    /**
     * Indicador do status INCOMPLETE do ContratoPratica.
     */
    public static final String STATUS_INCOMPLETE = "I";
    /**
     * Indicador do tipo de faturamento 'Time Material'.
     */
    public static final String TIPO_FATURAMENTO_TM = "TM";
    /**
     * Indicador do tipo de faturamento 'Fixed Price'.
     */
    public static final String TIPO_FATURAMENTO_FP = "FP";
    /**
     * Indicador do tipo de entrega 'Nacional'.
     */
    public static final String TIPO_ENTREGA_NACIONAL = "NA";
    /**
     * Indicador do tipo de entrega 'Internacional'.
     */
    public static final String TIPO_ENTREGA_INTERNACIONAL = "IN";
    /**
     * Indicador do status OPEN da Fatura.
     */
    public static final String FATURA_STATUS_OPEN = "OP";
    /**
     * Indicador do status APPROVED da Fatura.
     */
    public static final String FATURA_STATUS_APPROVED = "AP";
    /**
     * Indicador do status SUBMITTED da Fatura.
     */
    public static final String FATURA_STATUS_SUBMITTED = "SB";
    /**
     * Indicador do status INTEGRATED da Fatura.
     */
    public static final String FATURA_STATUS_INTEGRATED = "IN";
    /**
     * Indicador do status INTEGRATION ERROR da Fatura.
     */
    public static final String FATURA_STATUS_INTEGRATION_ERROR = "ER";
    /**
     * Indicador do status CANCELED da Fatura.
     */
    public static final String FATURA_STATUS_CANCELED = "CA";
    /**
     * Indicador do status PENDING da Fatura.
     */
    public static final String FATURA_STATUS_PENDING = "PD";
    /**
     * Indicador do status PENDING da Fatura.
     */
    public static final String FATURA_STATUS_PROCESSING = "PR";
    /**
     * Indicador do status ADDED do MapaAlocacaoFoto.
     */
    public static final String MAPA_FOTO_STATUS_ADDED = "A";
    /**
     * Indicador do status UPDATED do MapaAlocacaoFoto.
     */
    public static final String MAPA_FOTO_STATUS_UPDATED = "U";
    /**
     * Indicador do status DELETED do MapaAlocacaoFoto.
     */
    public static final String MAPA_FOTO_STATUS_DELETED = "D";
    /**
     * Indicador do status REAL da CotacaoMoeda.
     */
    public static final String COTACAO_MOEDA_TIPO_REAL = "R";
    public static final String COTACAO_MOEDA_REAL = "Real";

    /**
     * Indicador do status PREVISTO da CotacaoMoeda.
     */
    public static final String COTACAO_MOEDA_TIPO_PREVISTO = "P";
    /**
     * Sigla Moeda Real
     */
    public static final String SIGLA_MOEDA_REAL = "BRL";
    /**
     * Sigla Moeda Real
     */
    public static final String SIGLA_MOEDA_YEN = "JPY";
    /**
     * Sigla Moeda Real
     */
    public static final String SIGLA_MOEDA_YUAN = "CNY";
    /**
     * Indicador do tipo NACIONAL do Cliente.
     */
    public static final String CLIENTE_TYPE_NATIONAL = "NAC";
    /**
     * Indicador do tipo INTERNACIONAL do Cliente.
     */
    public static final String CLIENTE_TYPE_INTERNATIONAL = "INT";
    /**
     * Indicador do tipo MANDATORY (Obrigatorio) da NaturezaCentroLucro.
     */
    public static final String NATUREZA_TYPE_MANDATORY = "O";
    /**
     * Indicador do tipo OPTIONAL (Facultativo) da NaturezaCentroLucro.
     */
    public static final String NATUREZA_TYPE_OPTIONAL = "F";
    /**
     * Indicador do tipo MANUAL da sincronizacao.
     */
    public static final String TYPE_MANUAL = "MN";
    /**
     * Indicador do tipo SYNCHRONIZATION da sincronizacao.
     */
    public static final String TYPE_SYNC = "SY";
    /**
     * Indicador do tipo de TiRecurso Contract Service.
     */
    public static final String TI_RECURSO_TYPE_CONTRCT_SERVICE = "CS";
    /**
     * Indicador do tipo de TiRecurso Software User.
     */
    public static final String TI_RECURSO_TYPE_SOFTWARE_USER = "SU";
    public static final String TI_RECURSO_TYPE_SOFTWARE_PROJECT = "SOFTWARE_PROJECT";
    public static final String LICENSES_MSG_ERROR_EXPORT = "_nls.receita_relatorio.msg.error.export_report";
    /**
     * Indicador para valor "todos" do tipo de faturamento Deal (Time Material,
     * Fixed Price).
     */
    public static final String TIPO_FATURAMENTO_ALL = "ALL";
    /**
     * Indicador para valor "todos" status do ContratoPratica (COMPLETE,
     * INCOMPLETE).
     */
    public static final String STATUS_ALL = "ALL";
    /**
     * Indicador para valor "todos" status do MSA (Prospected, Validated,
     * Active, Inactive).
     */
    public static final String STATUS_MSA_ALL = "ALL";
    /**
     * Bundle Extra
     */
    public static final String BUNDLE_EXTRA_LABEL = "_nls.extra.label";
    /**
     * Indicador do tipo de CustoRealizado - Ferias.
     */
    public static final String CUSTO_REALIZADO_TYPE_FR = "FR";
    /**
     * Indicador do tipo de CustoRealizado - Normal.
     */
    public static final String CUSTO_REALIZADO_TYPE_NM = "NM";
    /**
     * Indicador do tipo de CustoRealizado - Overhad.
     */
    public static final String CUSTO_REALIZADO_TYPE_OH = "OH";
    /**
     * Indicador do status PLANNED da AlocacaoOverhead (planejada).
     */
    public static final String ALOCACAO_OH_STATUS_PLANNED = "P";
    /**
     * Indicador do status PERFORMED da AlocacaoOverhead (tirada).
     */
    public static final String ALOCACAO_OH_STATUS_PERFORMED = "T";
    /**
     * Indicador Horas Extras.
     */
    public static final String CUSTO_PESSOA_MES_ORIGEM_HORAS_EXTRAS = "HE";
    /**
     * Indicador Plantao.
     */
    public static final String CUSTO_PESSOA_MES_ORIGEM_PLANTAO = "PL";
    /**
     * Mes de ajuste ja existente.
     */
    public static final String MSG_ERROR_AJUSTMENT_MES_EXIST = "_nls.ajuse_receita.error.mes_existente";
    /**
     * Mensagem de sucesso update.
     */
    public static final String MSG_SUCCESS_UPDATE = "_nls.msg.success.update_adjust";
    /**
     * Mensagem de sucesso validacao.
     */
    public static final String MSG_SUCCESS_VALIDATE = "_nls.msg.success.validate";
    /**
     * Bundle para valor "todos" (ALL).
     */
    public static final String BUNDLE_KEY_ALL = "_nls.all.label";

    /** Bundles diversos. */
    /**
     * Bundle para valor "Sem tag".
     */
    public static final String BUNDLE_KEY_WITHOUT_TAG = "_nls.without_tag.label";
    /**
     * Bundle para a lista de meses.
     */
    public static final String BUNDLE_KEY_MONTHS = "_nls.months.list";
    /**
     * Bundle para faturamento do tipo 'Time Material'.
     */
    public static final String BUNDLE_KEY_TIPO_FATURAMENTO_TM = "_nls.deal.indicador_tipo.tm.label";
    /**
     * Bundle para faturamento do tipo 'Fixed Price'.
     */
    public static final String BUNDLE_KEY_TIPO_FATURAMENTO_FP = "_nls.deal.indicador_tipo.fp.label";
    /**
     * Bundle para valor "todos" (Time Material, Fixed Price).
     */
    public static final String BUNDLE_KEY_TIPO_FATURAMENTO_ALL = "_nls.deal.indicador_tipo.all.label";
    /**
     * Bundle para entrega do tipo 'Nacional'.
     */
    public static final String BUNDLE_KEY_TIPO_ENTREGA_NACIONAL = "_nls.deal.indicador_entrega.nacional.label";
    /**
     * Bundle para entrega do tipo 'Internacional'.
     */
    public static final String BUNDLE_KEY_TIPO_ENTREGA_INTERNACIONAL = "_nls.deal.indicador_entrega.internacional.label";
    /**
     * Bundle para valor Yes.
     */
    public static final String BUNDLE_KEY_YES = "_nls.yes.label";
    /**
     * Bundle para valor No.
     */
    public static final String BUNDLE_KEY_NO = "_nls.no.label";
    /**
     * Bundle para valor Ativo.
     */
    public static final String BUNDLE_KEY_ACTIVE = "_nls.active.label";
    /**
     * Bundle para valor Inativo.
     */
    public static final String BUNDLE_KEY_INACTIVE = "_nls.inactive.label";
    /**
     * Bundle para valor Prospect.
     */
    public static final String BUNDLE_KEY_PROSPECT = "_nls.prospect.label";

    public static final String BUNDLE_KEY_CONTRACT_RENEWAL = "_nls.contract_renewal.label";

    public static final String BUNDLE_KEY_EXPAND = "_nls.expand.label";

    /** Bundle para valor Request Inactivation. */
    public static final String BUNDLE_KEY_REQUEST_INACTIVATION = "_nls.request_inactivation.label";

    /** Bundle para valor Request CREATION. */
    public static final String BUNDLE_KEY_REQUEST_CREATION = "_nls.request_creation.label";

    /** Bundle para valor INTEGRATING CREATION. */
    public static final String BUNDLE_KEY_INTEGRATING_CREATION = "_nls.integrating_creation.label";

    /** Bundle para valor INTEGRATING INACTIVATION. */
    public static final String BUNDLE_KEY_INTEGRATING_INACTIVATION = "_nls.integrating_inactivation.label";

    /** Bundle para valor CLOSED. */
    public static final String BUNDLE_KEY_CLOSED = "_nls.closed.label";

    /** Bundle para valor "todos" (Ativo("A") e Inativo("I")).. */
    public static final String BUNDLE_KEY_ACTIVE_INACTIVE = "_nls.active_inactive.label";

    /** Bundle para a vers�o DRAFT do mapa de aloca��o. */
    public static final String BUNDLE_KEY_VERSION_DRAFT = "_nls.mapa_alocacao.indicador_versao.draft.label";

    /** Bundle para a vers�o DRAFT do mapa de aloca��o. */
    public static final String MAPA_ALOCACAO_ERROR_PERSON_WITHOUT_CITY = "_nls.mapa_alocacao.msg.error.cant_add_person_without_site";

    /** Bundle para a vers�o PUBLISH do mapa de aloca��o. */
    public static final String BUNDLE_KEY_VERSION_PUBLISHED = "_nls.mapa_alocacao.indicador_versao.published.label";

    /** Bundle para a vers�o VALIDATE do mapa de aloca��o. */
    public static final String BUNDLE_KEY_VERSION_VALIDATED = "_nls.mapa_alocacao.indicador_versao.validated.label";

    /** Bundle para a vers�o UNAVAILABLE. */
    public static final String BUNDLE_KEY_VERSION_UNAVAILABLE = "_nls.mapa_alocacao.indicador_versao.unavailable.label";

    /** Bundle para valor "todos" (DRAFT, PUBLISH e VALIDATE). */
    public static final String BUNDLE_KEY_VERSION_ALL = "_nls.mapa_alocacao.indicador_versao.all.label";

    /** Bundle para o est�gio COMMITED da aloca��o. */
    public static final String BUNDLE_KEY_STAGE_COMMITED = "_nls.alocacao.indicador_estagio.commited.label";

    /** Bundle para o est�gio RESERVED da aloca��o. */
    public static final String BUNDLE_KEY_STAGE_RESERVED = "_nls.alocacao.indicador_estagio.reserved.label";

    /** Bundle para o est�gio PROSPECTED_LOW da aloca��o. */
    public static final String BUNDLE_KEY_STAGE_PROSPECTED_LOW = "_nls.alocacao.indicador_estagio.prospected_low.label";

    /** Bundle para o est�gio PROSPECTED_HIGH da aloca��o. */
    public static final String BUNDLE_KEY_STAGE_PROSPECTED_HIGH = "_nls.alocacao.indicador_estagio.prospected_high.label";

    /** Bundle para o status COMPLETE do ContratoPratica. */
    public static final String BUNDLE_KEY_STATUS_COMPLETE = "_nls.contrato_pratica.indicador_status.complete.label";

    /** Bundle para o status INCOMPLETE do ContratoPratica. */
    public static final String BUNDLE_KEY_STATUS_INCOMPLETE = "_nls.contrato_pratica.indicador_status.incomplete.label";

    /** Bundle para o est�gio PROSPECTED da MSA. */
    public static final String BUNDLE_KEY_STAGE_PROSPECTED = "_nls.msa.indicador_estagio.prospected.label";

    /** Bundle para o est�gio PROSPECTED da MSA. */
    public static final String BUNDLE_KEY_STAGE_EXISTING = "_nls.msa.indicador_estagio.existing.label";

    /** Bundle para a completude Completo do MSA. */
    public static final String BUNDLE_KEY_STATUS_MSA_COMPLETE = "_nls.msa.status.completo";

    /** Bundle para a completude incompleto do MSA. */
    public static final String BUNDLE_KEY_STATUS_MSA_INCOMPLETE = "_nls.msa.status.incompleto";

    /** Bundle para o est�gio INTEGRADO da integra��o. */
    public static final String INTEGRACAO_BUNDLE_STATUS_INTEGRADO = "_nls.receita_integracao.status.integrado";

    /** Bundle para o est�gio PENDENTE da integra��o. */
    public static final String INTEGRACAO_BUNDLE_STATUS_PENDENTE = "_nls.receita_integracao.status.pendente";

    /** Bundle para o est�gio INTEGRADO da integra��o. */
    public static final String INTEGRACAO_BUNDLE_STATUS_ORACLE = "_nls.receita_integracao.status.oracle";

    /** Bundle para o est�gio NAO INTEGRADO da integra��o. */
    public static final String INTEGRACAO_BUNDLE_STATUS_NAO_INTEGRADO = "_nls.receita_integracao.status.nao_integrado";

    public static final String INTEGRACAO_BUNDLE_COTACAO_MOEDA_NOF_FOUND = "_nls.receita_integracao.cotacao_moeda.nof_found";

    public static final String LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_AGUARDANDO_APROVACAO = "_nls.ti_recurso_integracao.status.aguardando_aprovacao";

    public static final String LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_APROVADO = "_nls.ti_recurso_integracao.status.aprovado";

    /** Bundle para o est�gio NAO INTEGRADO da integra��o. */
    public static final String LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_PENDENTE = "_nls.ti_recurso_integracao.status.pendente";

    /** Bundle para o est�gio NAO INTEGRADO da integra��o. */
    public static final String LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_INTEGRADO = "_nls.ti_recurso_integracao.status.integrado";

    public static final String LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_ERRO = "_nls.ti_recurso_integracao.status.erro";

    public static final String LICENSE_SW_INTEGRACAO_ACCOUNTING_NOT_FOUND = "_nls.ti_recurso_integracao.accouting_notfound";

    public static final String LICENSE_SW_INTEGRACAO_PROJECT_COST_CENTER_CLOB_INACTIVE = "_nls.ti_recurso_integracao.project.cost_center_clob_inactive";
    public static final String LICENSE_SW_INTEGRACAO_USER_COST_CENTER_CLOB_INACTIVE = "_nls.ti_recurso_integracao.user.cost_center_clob_inactive";

    public static final String LICENSE_SW_APROVACAO_ERROR_GENERIC = "_nls.ti_recurso_integracao.approve_error";

    public static final String LICENSE_SW_INTEGRACAO_ERROR_GENERIC = "_nls.ti_recurso_integracao.integrate_error";

    /** Bundle para o est�gio PENDENTE da apropria��o. */
    public static final String APROPRIACAO_FATURA_BUNDLE_STATUS_PENDENTE = "_nls.apropriacao_fatura.status.pendente";

    /** Bundle para o est�gio COMPLETO da apropria��o. */
    public static final String APROPRIACAO_FATURA_BUNDLE_STATUS_COMPLETO = "_nls.apropriacao_fatura.status.completo";

    /** Bundle para o status faturavel do PessoGrupoCusto. */
    public static final String PESSOA_GRUPO_CUSTO_BUNDLE_STATUS_FATURAVEL = "_nls.pessoa_grupo_custo.status.faturavel";

    /** Bundle para o status n�o faturavel do PessoGrupoCusto. */
    public static final String PESSOA_GRUPO_CUSTO_BUNDLE_STATUS_NAO_FATURAVEL = "_nls.pessoa_grupo_custo.status.nao_faturavel";

    /** Bundle para o status disponivel do PessoGrupoCusto. */
    public static final String PESSOA_GRUPO_CUSTO_BUNDLE_STATUS_DISPONIVEL = "_nls.pessoa_grupo_custo.status.disponivel";

    /** Bundle para o status planejado da AlocacaoOverhead. */
    public static final String ALOCACAO_OH_BUNDLE_STATUS_PLANNED = "_nls.alocacao_overhead.indicador_status.planned.label";

    /** Bundle para o status tirado da AlocacaoOverhead. */
    public static final String ALOCACAO_OH_BUNDLE_STATUS_PERFORMED = "_nls.alocacao_overhead.indicador_status.performed.label";

    /** status faturavel da PessoGrupoCusto. */
    public static final String PESSOA_GRUPO_CUSTO_STATUS_FATURAVEL = "BL";

    /** status n�o faturavel da PessoGrupoCusto. */
    public static final String PESSOA_GRUPO_CUSTO_STATUS_NAO_FATURAVEL = "NB";

    /** status disponivel da PessoGrupoCusto. */
    public static final String PESSOA_GRUPO_CUSTO_STATUS_DISPONIVEL = "AV";

    /** Est�gio INTEGRADO da integra��o. */
    public static final String APROPRIACAO_FATURA_STATUS_PENDENTE = "PD";

    /** Est�gio INTEGRADO da integra��o. */
    public static final String APROPRIACAO_FATURA_STATUS_COMPLETO = "CP";

    /** Bundle para o est�gio ERROR da integra��o. */
    public static final String INTEGRACAO_BUNDLE_STATUS_ERROR = "_nls.receita_integracao.status.erro";

    public static final String INTEGRACAO_BUNDLE_STATUS_NA_FILA = "_nls.receita_integracao.status.na_fila";

    /** Est�gio INTEGRADO da integra��o. */
    public static final String INTEGRACAO_STATUS_INTEGRADO = "I";

    /** Est�gio N�O INTEGRADO da integra��o. */
    public static final String INTEGRACAO_STATUS_NAO_INTEGRADO = "W";

    /** Estágio pendente. */
    public static final String INTEGRACAO_STATUS_PENDENTE = "P";

    /** Est�gio oracle. */
    public static final String INTEGRACAO_STATUS_ORACLE = "O";

    /** Est�gio ERROR da integra��o. */
    public static final String INTEGRACAO_STATUS_ERROR = "E";

    public static final String INTEGRACAO_STATUS_NA_FILA = "Q";

    public static final String LICENSE_SW_INTEGRACAO_STATUS_AGUARDANDO_APROVACAO = "AGUARDANDO_APROVACAO";

    public static final String LICENSE_SW_INTEGRACAO_STATUS_APROVADO = "APROVADO";

    public static final String LICENSE_SW_INTEGRACAO_STATUS_PENDENTE = "PENDENTE";

    public static final String LICENSE_SW_INTEGRACAO_STATUS_PENDENTE_ORACLE = "PENDENTE ORACLE";

    public static final String LICENSE_SW_INTEGRACAO_STATUS_INTEGRADO = "INTEGRADO";

    public static final String LICENSE_SW_INTEGRACAO_STATUS_ERRO = "ERRO";
    public static final String LICENSE_SW_TYPE_USER = "U";
    public static final String LICENSE_SW_TYPE_PROJECT = "P";

    /**
     * Indicador do status PENDING da Licença.
     */
    public static final String STATUS_LICENSE_PENDING = "Pending";
    public static final String LICENSE_FINANCIAL_ACCOUNT_PROD = "4106010004";
    public static final String LICENSE_FINANCIAL_ACCOUNT_COMERCIAL = "5106010004";
    public static final String LICENSE_FINANCIAL_ACCOUNT_ADM = "5206010004";
    public static final String LICENSE_FINANCIAL_ACCOUNT_PANDD = "5306010004";
    public static final String LICENSE_FINANCIAL_ORACLE_CREDIT_ACCOUNT = "1103030007";

    /**
     * Indicador de uma despesa do tipo credito.
     */
    public static final String DESPESA_INDICADOR_CREDITO = "C";
    /**
     * Indicador de uma despesa do tipo debito.
     */
    public static final String DESPESA_INDICADOR_DEBITO = "D";
    /**
     * Indicador de uma despesa do tipo credito.
     */
    public static final String DESPESA_INDICADOR_UNIDADE_VALOR_ABSOLUTO = "A";
    /**
     * Indicador de uma despesa do tipo debito.
     */
    public static final String DESPESA_INDICADOR_UNIDADE_PERCENTUAL = "P";
    /**
     * Bundle para valor "todos" status do ContratoPratica (COMPLETE,
     * INCOMPLETE).
     */
    public static final String BUNDLE_KEY_STATUS_ALL = "_nls.contrato_pratica.indicador_status.all.label";
    /**
     * Bundle para valor "todos" status do MSA (Prospected, Validated, Active,
     * Inactive).
     */
    public static final String BUNDLE_KEY_STAGE_ALL = "_nls.msa.status_all.label";
    /**
     * Bundle para o status OPEN da Fatura.
     */
    public static final String BUNDLE_KEY_FATURA_STATUS_OPEN = "_nls.fatura.indicador_status.open.label";
    /**
     * Bundle para o status APPROVED da Fatura.
     */
    public static final String BUNDLE_KEY_FATURA_STATUS_APPROVED = "_nls.fatura.indicador_status.approved.label";
    /**
     * Bundle para o status SUBMITTED da Fatura.
     */
    public static final String BUNDLE_KEY_FATURA_STATUS_SUBMITTED = "_nls.fatura.indicador_status.submitted.label";
    /**
     * Bundle para o status INTEGRATED da Fatura.
     */
    public static final String BUNDLE_KEY_FATURA_STATUS_INTEGRATED = "_nls.fatura.indicador_status.integrated.label";
    /**
     * Bundle para o status INTEGRATION ERROR da Fatura.
     */
    public static final String BUNDLE_KEY_FATURA_STATUS_INTEGRATION_ERROR = "_nls.fatura.indicador_status.integration_error.label";
    /**
     * Bundle para o status CANCELED da Fatura.
     */
    public static final String BUNDLE_KEY_FATURA_STATUS_CANCELED = "_nls.fatura.indicador_status.canceled.label";
    /**
     * Bundle para o status PENDING da Fatura.
     */
    public static final String BUNDLE_KEY_FATURA_STATUS_PENDING = "_nls.fatura.indicador_status.pending.label";
    /**
     * Bundle para o status PROCESSING da Fatura.
     */
    public static final String BUNDLE_KEY_FATURA_STATUS_PROCESSING = "_nls.fatura.indicador_status.processing.label";
    /**
     * Bundle para o status ADDED do MapaAlocacaoFoto.
     */
    public static final String BUNDLE_KEY_MAPA_FOTO_STATUS_ADDED = "_nls.mapa_foto.indicador_status.added.label";
    /**
     * Bundle para o status UPDATED do MapaAlocacaoFoto.
     */
    public static final String BUNDLE_KEY_MAPA_FOTO_STATUS_UPDATED = "_nls.mapa_foto.indicador_status.updated.label";
    /**
     * Bundle para o status DELETED do MapaAlocacaoFoto.
     */
    public static final String BUNDLE_KEY_MAPA_FOTO_STATUS_DELETED = "_nls.mapa_foto.indicador_status.deleted.label";
    /**
     * Bundle para o status REAL da CotacaoMoeda.
     */
    public static final String BUNDLE_KEY_COTACAO_MOEDA_TIPO_REAL = "_nls.cotacao_moeda.indicador_tipo.real.label";
    /**
     * Bundle para o status PREVISTO da CotacaoMoeda.
     */
    public static final String BUNDLE_KEY_COTACAO_MOEDA_TIPO_PREVISTO = "_nls.cotacao_moeda.indicador_tipo.previsto.label";
    /**
     * Bundle para o tipo NACIONAL do Cliente.
     */
    public static final String BUNDLE_KEY_CLIENTE_TYPE_NATIONAL = "_nls.cliente.indicador_tipo.nacional.label";
    /**
     * Bundle para o tipo INTERNACIONAL do Cliente.
     */
    public static final String BUNDLE_KEY_CLIENTE_TYPE_INTERNATIONAL = "_nls.cliente.indicador_tipo.internacional.label";
    /**
     * Bundle para o tipo MANDATORY (Obrigatorio) da NaturezaCentroLucro.
     */
    public static final String BUNDLE_KEY_NATUREZA_TYPE_MANDATORY = "_nls.natureza_centro_lucro.indicador_tipo.mandatory.label";
    /**
     * Bundle para o tipo OPTIONAL (Facultativo) da NaturezaCentroLucro.
     */
    public static final String BUNDLE_KEY_NATUREZA_TYPE_OPTIONAL = "_nls.natureza_centro_lucro.indicador_tipo.optional.label";
    /**
     * Bundle para o tipo MANUAL da ComposicaoTce.
     */
    public static final String BUNDLE_KEY_COMP_TCE_TYPE_MANUAL = "_nls.composicao_tce.indicador_tipo.manual.label";
    /**
     * Bundle para o tipo SYNCHRONIZATION da ComposicaoTce.
     */
    public static final String BUNDLE_KEY_COMP_TCE_TYPE_SYNC = "_nls.composicao_tce.indicador_tipo.sync.label";
    /**
     * Bundle para o tipo MANUAL da Chargeback.
     */
    public static final String BUNDLE_KEY_CHARGEBACK_TYPE_MANUAL = "_nls.chargeback.indicador_tipo.manual.label";
    /**
     * Bundle para o tipo SYNCHRONIZATION da Chargeback.
     */
    public static final String BUNDLE_KEY_CHARGEBACK_TYPE_SYNC = "_nls.chargeback.indicador_tipo.sync.label";
    /**
     * Bundle para o tipo de TiRecurso CS.
     */
    public static final String TI_RECURSO_BUNDLE_KEY_TIPO_CS = "_nls.ti_recurso.tipo_recurso.cs";
    /**
     * Bundle para o tipo de TiRecurso SU.
     */
    public static final String TI_RECURSO_BUNDLE_KEY_TIPO_SU = "_nls.ti_recurso.tipo_recurso.su";
    public static final String TI_RECURSO_BUNDLE_KEY_TIPO_SP = "_nls.ti_recurso.tipo_recurso.sp";
    /**
     * Bundle para estilo css italico.
     */
    public static final String BUNDLE_KEY_LABEL_STYLE_ITALIC = "_nls.label.style.italic";
    /**
     * Bundle para estilo css cor inativa (cor pastel).
     */
    public static final String BUNDLE_KEY_LABEL_STYLE_INACTIVE_COLOR = "_nls.label.style.inactive_color";
    /**
     * Bundle para estilo css cor vermelha.
     */
    public static final String BUNDLE_KEY_LABEL_STYLE_COLOR_RED = "_nls.label.style.color_red";
    /**
     * Bundle para estilo css cor verde.
     */
    public static final String BUNDLE_KEY_LABEL_STYLE_COLOR_GREEN = "_nls.label.style.color_green";
    /**
     * Assunto do e-mail para followers de MapaAlocacao.
     */
    public static final String BUNDLE_KEY_MAAL_PESS_MAIL_FOLLOW_SUBJECT = "_nls.mapa_aloc_pessoa.mail.follow.subject";
    /**
     * Descri��o do e-mail para followers de MapaAlocacao.
     */
    public static final String BUNDLE_KEY_MAAL_PESS_MAIL_FOLLOW_DESCRIPTION = "_nls.mapa_aloc_pessoa.mail.follow.description";
    /**
     * Descri��o 2 do e-mail para followers de MapaAlocacao.
     */
    public static final String BUNDLE_KEY_MAAL_PESS_MAIL_FOLLOW_DESCRIPTION2 = "_nls.mapa_aloc_pessoa.mail.follow.description2";
    /**
     * Assunto do e-mail para followers de Pessoa.
     */
    public static final String BUNDLE_KEY_PESS_PESS_MAIL_FOLLOW_SUBJECT = "_nls.pessoa_pessoa.mail.follow.subject";
    /**
     * Descri��o do e-mail para followers de Pessoa.
     */
    public static final String BUNDLE_KEY_PESS_PESS_MAIL_FOLLOW_DESCRIPTION = "_nls.pessoa_pessoa.mail.follow.description";
    /**
     * Descri��o 2 do e-mail para followers de Pessoa.
     */
    public static final String BUNDLE_KEY_PESS_PESS_MAIL_FOLLOW_DESCRIPTION2 = "_nls.pessoa_pessoa.mail.follow.description2";
    /**
     * Assunto do e-mail BlackBerry para followers de MapaAlocacao.
     */
    public static final String BUNDLE_KEY_MAAL_PESS_MAIL_BBERRY_FOLLOW_SUBJECT = "_nls.mapa_aloc_pessoa.mail_bberry.follow.subject";
    /**
     * Descri��o do e-mail BlackBerry para followers de MapaAlocacao.
     */
    public static final String BUNDLE_KEY_MAAL_PESS_MAIL_BBERRY_FOLLOW_DESCRIPTION = "_nls.mapa_aloc_pessoa.mail_bberry.follow.description";
    /**
     * Descri��o 2 do e-mail BlackBerry para followers de MapaAlocacao.
     */
    public static final String BUNDLE_KEY_MAAL_PESS_MAIL_BBERRY_FOLLOW_DESCRIPTION2 = "_nls.mapa_aloc_pessoa.mail_bberry.follow.description2";
    /**
     * Assunto do e-mail BlackBerry para followers de Pessoa.
     */
    public static final String BUNDLE_KEY_PESS_PESS_MAIL_BBERRY_FOLLOW_SUBJECT = "_nls.pessoa_pessoa.mail_bberry.follow.subject";
    /**
     * Descri��o do e-mail BlackBerry para followers de Pessoa.
     */
    public static final String BUNDLE_KEY_PESS_PESS_MAIL_BBERRY_FOLLOW_DESCRIPTION = "_nls.pessoa_pessoa.mail_bberry.follow.description";
    /**
     * Descri��o 2 do e-mail BlackBerry para followers de Pessoa.
     */
    public static final String BUNDLE_KEY_PESS_PESS_MAIL_BBERRY_FOLLOW_DESCRIPTION2 = "_nls.pessoa_pessoa.mail_bberry.follow.description2";
    /**
     * Descri��o do e-mail de erro para followers de Pessoa.
     */
    public static final String BUNDLE_KEY_PESS_PESS_MAIL_FOLLOW_ERROR = "_nls.pessoa_pessoa.mail.follow.error";
    /**
     * Assunto do e-mail de erro no Follow.
     */
    public static final String BUNDLE_KEY_MAPA_FOTO_MAIL_FOLLOW_ERROR_SUBJECT = "_nls.mapa_foto.mail.follow.error.subject";
    /**
     * Assunto do e-mail de erro gen�rico no Follow.
     */
    public static final String BUNDLE_KEY_MAPA_FOTO_MAIL_FOLLOW_ERROR_GENERIC_SUBJECT = "_nls.mapa_foto.mail.follow.error_generic.subject";
    /**
     * Template de e-mail Follow C-Lob.
     */
    public static final String BUNDLE_KEY_MAPA_FOTO_TEMPLATE_MAIL_CLOB = "_nls.mapa_foto.template_mail.clob";
    /**
     * Template de e-mail Follow People.
     */
    public static final String BUNDLE_KEY_MAPA_FOTO_TEMPLATE_MAIL_PEOPLE = "_nls.mapa_foto.template_mail.people";
    /**
     * Template de e-mail Follow Error.
     */
    public static final String BUNDLE_KEY_MAPA_FOTO_TEMPLATE_MAIL_ERROR = "_nls.mapa_foto.template_mail.error";
    /**
     * Template de e-mail Follow Error Generic.
     */
    public static final String BUNDLE_KEY_MAPA_FOTO_TMPL_MAIL_ERROR_GENERIC = "_nls.mapa_foto.tmpl_mail.error_generic";
    /**
     * Mensagem generica de sucess de remove generica.
     */
    public static final String GENEREC_MSG_SUCCESS_REMOVE = "_nls.msg.generic.success.remove";

    /** Mensagens gen�ricas. */
    /**
     * Mensagem generica de sucess de update generica.
     */
    public static final String GENEREC_MSG_SUCCESS_UPDATE = "_nls.msg.generic.success.update";
    /**
     * Mensagem de sucess de requisição de inativação.
     */
    public static final String MSG_SUCCESS_REQUEST_INACTIVATION = "_nls.msg.success.request_inactivation";
    /**
     * Mensagem generica de sucess de update generica.
     */
    public static final String GENEREC_MSG_SUCCESS_ADD = "_nls.msg.generic.success.add";
    public static final String GENERIC_MSG_SUCESS_CLONE = "_nls.msg.success.clone";
    /**
     * Mensagem generica de sucess de create generica.
     */
    public static final String DEFAULT_MSG_SUCCESS_SAVE = "_nls.msg.success.save";
    /**
     * Mensagem generica de ativacao de create generica.
     */
    public static final String DEFAULT_MSG_SUCCESS_ACTIVATE = "_nls.msg.success.active";
    public static final String DEFAULT_MSG_SUCCESS_REACTIVATE = "_nls.msg.success.reactive";
    /**
     * Mensagem generica de sucess de saved generica COM ARGUMENTO.
     */
    public static final String RECEITA_MSG_JUSTIFICATIVA_SUCCESS_SAVE = "_nls.receita.msg.justificativa.success.save";
    /**
     * Mensagem de warning informando que nao existe nenhum item para ser
     * publicado.
     */
    public static final String RECEITA_MSG_WARNING_NO_ITEM_TO_PUBLISH = "_nls.receita.msg.warning.no_items_to_published";
    /**
     * Mensagem de erro publica��o Revenue sem Taxa Imposto.
     */
    public static final String REVENUE_MSG_ERROR_NO_TAX_TO_PUBLISH = "_nls.receita.msg.error.no_tax_to_publish";
    /**
     * Mensagem de erro publica��o Revenue sem Taxa Imposto mensagem composta.
     */
    public static final String REVENUE_MSG_ERROR_NO_TAX_TO_PUBLISH_2 = "_nls.receita.msg.error.no_tax_to_publish2";
    /**
     * Mensagem de erro nenhuma Taxa Imposto encontrata.
     */
    public static final String RECEITA_DEAL_FISCAL_MSG_ERROR_NO_TAX_FOUND = "_nls.receita.msg.error.no_tax_found";
    /**
     * Mensagem de erro nenhum Mapa Alocacao encontrata.
     */
    public static final String MSG_ERROR_NO_ALOCATION_MAP_FOUND = "_nls.receita.msg.error.alocation_map_not_found";
    /**
     * Mensagem de erro Sale Profile não pode ser editado pois existem Alocações a ele associadas.
     */

    /**
     * Mensagem de erro Sale Profile não pode ser inativado, pois existem Alocações a ele associadas.
     */
    public static final String MSG_ERROR_THERE_ARE_RESOURCE_ALLOCATIONS_WITHIN_CURRENT_MONTH_YOU_CAN_NOT_INACTIVATE_IT = "_nls.sale_profile.msg.error.resource_allocations_within_current_month_you_can_not_inactivate_it";
    /**
     * Mensagem de erro Sale Profile não pode ser editado pois existem Alocações a ele associadas.
     */
    public static final String MSG_ERROR_THERE_ARE_RESOURCE_ALLOCATIONS_FOR_THIS_SALE_PROFILE = "_nls.sale_profile.msg.error.resource_allocations_sale_profile";
    /**
     * Mensagem de erro Sale Profile não pode ser inativado, pois existem Alocações a ele associadas.
     */
    public static final String MSG_ERROR_THERE_ARE_RESOURCE_ALLOCATIONS_FOR_THIS_SALE_PROFILE_YOU_CAN_NOT_INACTIVATE_IT = "_nls.sale_profile.msg.error.resource_allocations_sale_profile_you_can_not_inactivate_it";
    /**
     * Mensagem de erro Sale Profile não pode ser editado pois há associação com Tabelas de Preços de diferentes moedas.
     */
    public static final String MSG_ERROR_THERE_ARE_PRICE_TABLE_WITH_A_DIFFERENT_CURRENCY = "_nls.sale_profile.msg.error.price_table.different_currency";
    /**
     * Mensagem de erro ao adicionar um login que não possui o perfil de Commercial Partner ou Production Business na lista de Price Table Editors
     */
    public static final String MSM_ERROR_ADD_USER_WITH_NO_PROFILE_ON_PRICE_TABLE_EDITOR = "_nls.msa.configuracao.price_table_editor.add.error";
    /**
     * Mensagem de erro ao adicionar o seu próprio login na lista de Price Table Editors
     */
    public static final String MSM_ERROR_ADD_YOUR_OWN_LOGIN_IN_EDITOR = "_nls.msa.configuracao.price_table_editor.add_your_own_login.error";

    /**
     * Mensagem de erro ao adicionar o seu próprio login na lista de Price Table Approvers
     */
    public static final String MSM_ERROR_ADD_YOUR_OWN_LOGIN_IN_APPROVER = "_nls.msa.configuracao.price_table_approver.add_your_own_login.error";

    /**
     * Mensagem de erro ao adicionar login como Editor e Approver da Price Table ao mesmo tempo
     */
    public static final String MSM_ERROR_ADD_LOGIN_EXISTING_IN_EDITOR_OR_APPROVER = "_nls.msa.configuracao.price_table.login_existing_in_editor_or_approver";

    /**
     * Mensagem de erro ao adicionar um aprovador com alterações em andamento em tabelas do MSA
     */
    public static final String MSG_ERROR_ADD_LOGIN_WITH_CHANGES_IN_PROGRESS = "_nls.msa.configuracao.price_table_approver.add.changes_in_progress";

    /** GrupoCusto Data de Inativacao. */
    public static final String MSG_DATA_INACTIVATION_GRUPO_CUSTO = "_nls.msg.error.cost_center.data_inactivation_is_mandatory";

    public static final String MSG_PARENT_COST_CENTER_REQUIRED = "_nls.msg.error.cost_center.parent_cost_center_required";
    public static final String MSG_PARENT_OVERHEAD_PROJECT_REQUIRED = "_nls.msg.error.cost_center.parent_overhead_project_required";

    /** GrupoCusto Data de Inativacao atual ou futura. */
    public static final String MSG_DATA_INACTIVATION_ATUAL_FUTURA_GRUPO_CUSTO = "_nls.msg.error.cost_center.data_inactivation_today_future";

    public static final String MSG_APPROVE_PRICE_TABLE_START_BEFORE_CLOSING = "_nls.msa.configuracao.price_table.approve.start_before_closing";

    public static final String PRICE_TABLE_ITEM_EDITED_BG_COLOR = "_nls.item_tabela_preco.edited.background";

    public static final String PRICE_TABLE_ITEM_REMOVED_BG_COLOR = "_nls.item_tabela_preco.removed.background";

    public static final String PRICE_TABLE_ITEM_DEFAULT_BG_COLOR = "_nls.item_tabela_preco.default.background";

    /**
     * Mensagem generica de sucess de create generica.
     */
    public static final String DEFAULT_MSG_SUCCESS_CREATE = "_nls.msg.success.create";
    /**
     * Mensagem generica de sucess de create generica.
     */
    public static final String DEFAULT_MSG_UPDATE_SUCCESS = "_nls.msg.success.update";
    /**
     * Mensagem generica de sucess de add generica.
     */
    public static final String DEFAULT_MSG_SUCCESS_ADD = "_nls.msg.generic.success.add";
    /**
     * Mensagem generica de sucess de update generica.
     */
    public static final String DEFAULT_MSG_SUCCESS_UPDATE = "_nls.msg.generic.success.update";
    /**
     * Mensagem sucess para inativacao da delegacao de recursos.
     */
    public static final String MSG_SUCCESS_DELEGATE_OFF = "_nls.msg.my_profile.success.delegate_off";
    /**
     * Mensagem sucess para ativacao da delegacao de recursos.
     */
    public static final String MSG_SUCCESS_DELEGATE_ON = "_nls.msg.my_profile.success.delegate_on";
    /**
     * Mensagem generica de sucess de remove generica.
     */
    public static final String DEFAULT_MSG_SUCCESS_REMOVE = "_nls.msg.success.remove";
    /**
     * Mensagem generica de sucess de cancel generica.
     */
    public static final String DEFAULT_MSG_SUCCESS_CANCEL = "_nls.msg.success.cancel";
    /**
     * Mensagem generica de sucesso para uma troca de abas.
     */
    public static final String DEFAULT_MSG_SUCCESS_NEXT_STEP = "_nls.msg.success.next_step";
    /**
     * Mensagem generica de warning de search generica.
     */
    public static final String DEFAULT_MSG_WARNG_NO_RESULT = "_nls.msg.warn.search.no_result";
    /**
     * Mensagem generica de error para valor n�o encontrado.
     */
    public static final String DEFAULT_MSG_ERROR_NOT_FOUND = "_nls.msg.error.not_found";
    /**
     * Mensagem generica de error data atual maior que data inicial.
     */
    public static final String DEFAULT_MSG_ERROR_DATE_CURRENT_GT_DATE_BEG = "_nls.msg.error.date_current_gt_date_beg";
    /**
     * Mensagem de erro para Balance do MSA, Moedas selecionadas, valores
     * precisam ser preenchidos.
     */
    public static final String MSA_SALDO_MOEDA_MSG_ERROR_MUST_BE_FILLED = "_nls.msa_saldo_moeda.msg.error.must_be_filled";
    /**
     * Mensagem de warning para msa incompleto.
     */
    public static final String MSA_INCOMPLETO_WARNING = "_nls.msa.warning.incompleto";
    /**
     * Mensagem de warning para msa incompleto.
     */
    public static final String MSA_INCOMPLETO_WARNING_UPDATE = "_nls.msa.warning.incompleto_update";
    public static final String MSA_SALE_PROFILE_HAS_ALLOCATION = "_nls.msa.hasAllocation.error";
    /**
     * Mensagem de warning para remoção de CNPJ associado ao msa contrato
     */

    public static final String  MSA_CONTRATO_CNPJ_WARNING_REMOVE = "_nls_msa.documento_legal.msg.warning.delete.cnpj";

    /**
     * Mensagem de warning para msa incompleto - deve ser passado o nome do MSA
     * como parametro.
     */
    public static final String MSA_STATUS_INCOMPLETO_WARNING = "_nls.msa.warning.incomplete_status";

    /** Mensagem de sucesso para msa completo. */
    public static final String MSA_COMPLETO_SUCCESS = "_nls.msa.success.complete";

    /**
     * Mensagem de erro quando n�o � permitido alterar a data inicial do mapa de
     * aloca��o.
     */
    public static final String MAPA_ALOCACAO_MSG_ERROR_CHANGE_DATE_BEG = "_nls.mapa_alocacao.msg.error.cant_change_date_beg";

    /** Mensagem generica de error data inicial maior que data final. */
    public static final String DEFAULT_MSG_ERROR_DATE_BEG_GT_DATE_END = "_nls.msg.error.date_beg_gt_date_end";

    /**
     * Mensagem generica de error data de vencimento maior que data de previsao.
     */
    public static final String DEFAULT_MSG_ERROR_DATE_GT_EXP_DATE = "_nls.msg.error.date_gt_exp_date";

    /** Mensagem generica de error data atual maior que data final. */
    public static final String DEFAULT_MSG_ERROR_CURRENT_DATE_GT_END_DATE = "_nls.msg.error.current_date_gt_end_date";

    /** Mensagem generica de error data inicial nova menor que data ini antiga. */
    public static final String DEFAULT_MSG_ERROR_NEW_DATE_BEG_LT_OLD_DATE_BEG = "_nls.msg.error.new_date_beg_lt_old_date_beg";

    /** Mensagem generica de error data final nova menor que data final antiga. */
    public static final String DEFAULT_MSG_ERROR_NEW_DATE_END_LT_OLD_DATE_END = "_nls.msg.error.new_date_end_lt_old_date_end";

    /** Mensagem generica de error para restri��o de integridade - remove. */
    public static final String DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE = "_nls.msg.error.integrity_constraint_remove";

    /** Mensagem generica de error para restri��o de integridade - remove. */
    public static final String GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE = "_nls.msg.error.generic.integrity_constraint_remove";

    public static final String DEFAULT_MSG_ERROR_LOAD_PAYMENT_TERMS = "_nls.cliente.paymentcondition.conectivity.error.label";

    public static final String MSG_PAYMENT_TERMS_NOT_FOUND = "_nls.cliente.paymentcondition.not_found.label";

    /** Mensagem generica de error para restri��o de integridade - inactive. */
    public static final String DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_INACTIVE = "_nls.msg.error.integrity_constraint_inactive";

    /** Mensagem generica de error para restri��o de integridade relacionada a existencia de GrupoCustoAreaOrcamentaria */
    public static final String DEFAULT_MSG_ERROR_INTEGRITY_BUDGET_AREA_FIRST_TEXT = "_nls.msg.error.integrity_budget_area_first_text";

    /** Mensagem generica de error para restri��o de integridade relacionada a existencia de GrupoCustoAreaOrcamentaria */
    public static final String DEFAULT_MSG_ERROR_INTEGRITY_BUDGET_AREA_SECOND_TEXT = "_nls.msg.error.integrity_budget_area_second_text";

    /** Mensagem generica de error para restri��o de integridade relacionada a existencia de GrupoCustoAreaOrcamentaria */
    public static final String DEFAULT_MSG_ERROR_INTEGRITY_BUDGET_AREA_THIRD_TEXT = "_nls.msg.error.integrity_budget_area_third_text";

    /** Mensagem generica de error para uma entidade que j� existe. */
    public static final String DEFAULT_MSG_ERROR_ALREADY_EXISTS = "_nls.msg.error.already_exists";

    /** Mensagem generica de indicacao de modo de edicao. */
    public static final String MSG_EDIT_MODE = "_nls.msg.edit_mode";

    /** Mensagem generica de error para um nome j� existe. */
    public static final String DEFAULT_MSG_ERROR_NAME_ALREADY_EXISTS = "_nls.msg.error.name.already_exists";

    /** Mensagem generica de error para uma data inv�lida. */
    public static final String DEFAULT_MSG_ERROR_INVALID_DATE = "_nls.msg.error.invalid.date";
    /**
     * Mensagem generica de error uma vigencia de intercompany que ja existe.
     */
    public static final String DEFAULT_MSG_ERROR_VIGENCY_INTERCOMPANY_ALREADY_EXISTS = "_nls.msg.error.vigency_intercompany_already_exists";
    /**
     * Mensagem generica de error uma vigencia de licenca de usuario que ja existe.
     */
    public static final String DEFAULT_MSG_ERROR_VIGENCY_LICENSE_USER_ALREADY_EXISTS = "_nls.msg.error.license_user_already_exists";

    /**
     * Mensagem generica de error para uma Price Table Status inv�lida.
     */
    public static final String DEFAULT_MSG_ERROR_INVALID_PRICE_TABLE_STATUS = "_nls.msg.error.invalid.price_table_status";

    /**
     * Mensagem generica de aviso para uma Price Table sem Histórico.
     */
    public static final String DEFAULT_MSG_WARN_NO_PRICE_TABLE_HISTORY_FOUND = "_nls.msg.warn.no.price_table_history_found";

    /**
     * Mensagem generica de aviso para uma Price Table que tem receitas, que os valores alterados não foram refletidos.
     */
    public static final String MSG_WARN_PRICE_TABLE_VALUES_NOT_REFLECT_REVENUES = "_nls.msa.configuracao.price_table_revenues.update.values";

    /**
     * Mensagem generica de aviso para uma Revenue que tem em seu MSA Price Tables não aprovadas.
     */
    public static final String MSG_WARN_REVENUE_UPDATE_VALUES_PRICE_TABLE_NOT_APPROVED = "_nls.revenue.price_table_not_approved.update.values";

    /**
     * Mensagem generica de erro para uma Price Table que contém valores zerados em seus itens.
     */
    public static final String MSG_ERROR_PRICE_TABLE_RATES_NOT_FILLED = "_nls.msa.configuracao.price_table_rates.filled";

    /**
     * Mensagem de error nenhum item selecionado.
     */
    public static final String DEFAULT_MSG_ERROR_SELECT_ITEM = "_nls.msg.error.select.item";
    /**
     * Mensagem de error para campo obrigat�rio.
     */
    public static final String DEFAULT_MSG_ERROR_VALUE_REQUIRED = "javax.faces.component.UIInput.REQUIRED";
    public static final String DEFAULT_MSG_ERROR_GREATER_THAN = "javax.faces.component.value_validation.greater_than";
    public static final String DEFAULT_MSG_ERROR_LOWER_THAN = "javax.faces.component.value_validation.lower_than";
    /**
     * Mensagem generica de error para cnpj invalido.
     */
    public static final String DEFAULT_MSG_ERROR_INVALID_CNPJ = "_nls.msg.error.invalid.cnpj";
    public static final String DEFAULT_MSG_ERROR_HAS_INVALID_AGENT = "_nls.msg.error.has.invalid.agent";

    /**
     * Mensagens de erro para Oracle Taxpayer
     */
    public static final String DEFAULT_MSG_ERROR_HAS_INVALID_ORACLE_TAXPAYER = "_nls.msg.error.invalid.oracle_taxpayer";
    public static final String DEFAULT_MSG_ERROR_HAS_EXISTING_ORACLE_TAXPAYER_CUSTOMER = "_nls.msg.error.existing.oracle_taxpayer_customer";
    public static final String DEFAULT_MSG_ERROR_HAS_EXISTING_ORACLE_TAXPAYER_COMPANY = "_nls.msg.error.existing.oracle_taxpayer_company";

    public static final String DEFAULT_MSG_ERROR_COMPANY_ERP_HAS_MORE_THAN_ONE_ACTIVE = "_nls.msg.error.company_erp.has_more_than_one_active";

    /**
     * Mensagem de erro de DN n�o encontrado.
     */
    public static final String COMISSAO_MSG_ERROR_DN_NOT_FOUND = "_nls.comissao.dn.not_found";

    /** Mensagens. */
    /**
     * Mensagem de erro de DN n�o encontrado.
     */
    public static final String COMISSAO_MSG_ERROR_DEAL_FISCAL_NOT_FOUND = "_nls.comissao_acelerador.fiscal_deal.not_found";
    /**
     * Mensagem de sucesso de inativa��o.
     */
    public static final String PESSOA_MSG_SUCCESS_INACITIVATE = "_nls.pessoa.msg.success.inactivate";
    /**
     * Mensagem de sucesso de ativa��o.
     */
    public static final String PESSOA_MSG_SUCCESS_ACITIVATE = "_nls.pessoa.msg.success.activate";
    /**
     * Indicador de origem manual.
     */
    public static final String INPUT_INDICATOR_MANUAL = "_nls.indicador.entrada.manual";
    /**
     * Indicador de origem arquivo.
     */
    public static final String INPUT_INDICATOR_IMPORTADO = "_nls.indicador.entrada.importado";
    /**
     * Mensagem de sucesso no upload do arquivo.
     */
    public static final String UPLOAD_MSG_SUCCESS_UPLOAD = "_nls.msg.success.upload";
    /**
     * Mensagem de upload vazio.
     */
    public static final String UPLOAD_MSG_EMPTY_UPLOAD = "_nls.despesa.msg.sem_registros";
    /**
     * Mensagem de erro do padr�o do arquivo.
     */
    public static final String MSG_ERROR_UPLOAD_PADRAO_ARQUIVO = "_nls.msg.error.upload.padrao_arquivo";
    /**
     * Mensagem de erro no upload.
     */
    public static final String MSG_ERROR_UPLOAD = "_nls.msg.error.upload";
    /**
     * Mensagem de warning no upload.
     */
    public static final String MSG_WARNING_UPLOAD = "_nls.msg.warning.upload";
    /**
     * Mensagem de erro no upload registro j� existente.
     */
    public static final String UPLOAD_MSG_ERROR_ALREAD_EXISTS = "_nls.msg.error.upload.already_exists";
    /**
     * Mensagem de erro no upload de data j� fechada.
     */
    public static final String UPLOAD_MSG_ERROR_BEFORE_CLOSING_DATE = "_nls.msg.error.upload.before_closing_date";
    /**
     * Mensagem de erro no upload de data est� em um formato inv�lido.
     */
    public static final String UPLOAD_MSG_ERROR_INVALID_DATE_PATTERN = "_nls.msg.error.upload.invalid_date_pattern";
    /**
     * Mensagem de erro no upload erro na leitura do registro.
     */
    public static final String UPLOAD_MSG_ERROR_READING = "_nls.msg.error.upload.reading";
    /**
     * Mensagem de erro no upload erro na leitura do registro.
     */
    public static final String UPLOAD_ERROR_INVALID_LOGIN = "_nls.msg.error.upload.invalid_login";
    /**
     * Mensagem de erro no upload erro na leitura do registro.
     */
    public static final String UPLOAD_ERROR_INVALID_ACTIVITY = "_nls.msg.error.upload.atividade_invalida";
    /**
     * Mensagem de erro no upload erro na leitura do registro.
     */
    public static final String UPLOAD_ERROR_INVALID_CLOB = "_nls.msg.error.upload.contrato_pratica_invalida";
    /**
     * Mensagem de erro no upload erro na leitura do registro.
     */
    public static final String UPLOAD_ERROR_INVALID_CLOB_AND_COST_GROUP = "_nls.msg.error.upload.contrato_pratica_grupo_custo_invalida";
    /**
     * Mensagem de erro no upload erro na leitura do registro.
     */
    public static final String UPLOAD_ERROR_INVALID_COST_GROUP = "_nls.msg.error.upload.invalid_cost_group";
    /**
     * Mensagem de erro no upload erro na leitura do registro.
     */
    public static final String UPLOAD_ERROR_INVALID_CATEGORY = "_nls.msg.error.upload.invalid_category";
    /**
     * Mensagem de erro no upload erro na leitura do registro.
     */
    public static final String UPLOAD_ERROR_INVALID_CURRENCY = "_nls.msg.error.upload.invalid_currency";
    /**
     * Mensagem de erro no upload erro na leitura do registro.
     */
    public static final String UPLOAD_ERROR_INVALID_C_LOB = "_nls.msg.error.upload.invalid_contrato_pratica";
    /**
     * Mensagem de erro na sigla da moeda no upload de plantao por valor.
     */
    public static final String UPLOAD_PLANTAO_HE_MSG_ERRO_SIGLA_MOEDA = "_nls.apropriacao.plantao_horaExtra.msg.error.sigla_moeda_invalido";
    /**
     * Mensagem de erro registro j� existente.
     */
    public static final String UPLOAD_PLANTAO_HE_MSG_ERRO_REGISTRO_JA_EXISTENTE = "_nls.apropriacao.plantao_horaExtra.msg.error.registro_existente";
    /**
     * Mensagem de erro registro duplicado no arquivo.
     */
    public static final String UPLOAD_PLANTAO_HE_MSG_ERRO_REGISTRO_DUPLICADO_NO_ARQUIVO = "_nls.apropriacao.plantao_horaExtra.msg.error.registro_duplicado_no_arquivo";
    /**
     * Mensagem de erro formato data MM/dd/yyyy.
     */
    public static final String UPLOAD_PLANTAO_HE_MSG_ERRO_FORMATO_DATA = "_nls.apropriacao.plantao_horaExtra.msg.error.formato_data";
    /**
     * Mensagem de erro formato do valor.
     */
    public static final String UPLOAD_PLANTAO_HE_MSG_ERRO_FORMATO_VALOR = "_nls.apropriacao.plantao_horaExtra.msg.error.valor";
    /**
     * Mensagem de sucesso para remover registro de apropria��o plant�o.
     */
    public static final String MSG_SUCCESS_APROPRIACAO_PLANTAO_REMOVE = "_nls.msg.success.apropriacao.plantao.remove";
    /**
     * Mensagem de sucesso para remover registro de apropria��o hora extra.
     */
    public static final String MSG_SUCCESS_APROPRIACAO_HORA_EXTRA_REMOVE = "_nls.msg.success.apropriacao.hora_extra.remove";
    /**
     * Mensagem de erro ao inserir um CustoInfraBase com mesma data e
     * CidadeBase.
     */
    public static final String MSG_ERROR_ADD_CUSTO_INFRA_BASE_PERIOD = "_nls.custo_infra_base.error.vigencia";
    /**
     * Mensagem de sucesso ao validar o DreLogFechamento.
     */
    public static final String MSG_SUCCESS_VALIDATE_DRE_LOG_FECHAMENTO = "_nls.dre_log_fechamento.msg.success.validate";
    /**
     * Mensagem de sucesso ao validar o DreProcessoByLogin.
     */
    public static final String MSG_ERROR_VALIDATE_DRE_BY_LOGIN = "_nls.dre_processos_fechamento.msg.error.empty.list";
    /**
     * Mensagem de erro ao validar o inser��o login.
     */
    public static final String MSG_ERROR_VALIDATE_DRE_LOGIN_DUPLICATED = "_nls.menu.dre_processos_fechamento_by_login.msg.login.duplicado";
    /**
     * Mensagem de sucesso ao validar o DreLogFechamento.
     */
    public static final String MSG_SUCCESS_CONSOLIDATE_DRE_LOG_FECHAMENTO = "_nls.dre_log_fechamento.msg.success.consolidate";
    /**
     * Mensagem de erro para exceptions do consolidar receitas e faturamento
     */
    public static final String MSG_ERROR_DRE_PROCESS_CONSOLIDATE = "_nls.dre_processos_fechamento.msg.error.consolidate";
    /**
     * Mensagem de erro para exceptions do registro de plant�o e horas extras
     */
    public static final String MSG_ERROR_DRE_PROCESS_REGISTER = "_nls.dre_processos_fechamento.msg.error.register";
    /**
     * Mensagem de erro para Apropriacao Plantao e Horas Extras - Codigo de moeda n�o existe
     */
    public static final String REGISTER_PL_HE_ERROR_VALIDATE_CODE_CURRENCY = "_nls.dre_processos_fechamento.msg.error.validate_code_currency";
    /**
     * Mensagem de completado com sucesso
     */
    public static final String MSG_SUCCESS_SUCCESFULLY_COMPLETED = "_nls.dre_processos_fechamento.msg.success.successfully_completed";
    /**
     * Mensagem de warning para verificacao de status do processo antes da
     * execucao do mesmo
     */
    public static final String MSG_WARNING_DRE_PROCESS_STATUS = "_nls.dre_processos_fechamento.msg.warning.status";
    /**
     * Mensagem de warning para verificacao de status do processo no mes/ano
     * antes da execucao do mesmo
     */
    public static final String MSG_WARNING_DRE_PROCESS_NOT_COMPLETED = "_nls.dre_processos_fechamento.msg.warning.not_completed";
    /**
     * Mensagem de erro para valida��o de moeda
     */
    public static final String MSG_ERROR_DRE_PROCESS_VALIDATE_CURRENCY = "_nls.dre_processos_fechamento.msg.error.validate_currency";
    /**
     * Mensagem de erro para valida��o de grupo de custo por pessoa
     */
    public static final String MSG_ERROR_DRE_PROCESS_VALIDATE_COST_GROUP = "_nls.dre_processos_fechamento.msg.error.validate_grupo_custo";
    /**
     * Mensagem de erro para valida��o de grupo de custo por pessoa
     */
    public static final String MSG_ERROR_DRE_PROCESS_VALIDATE_ALLOCATION_MAP = "_nls.dre_processos_fechamento.msg.error.validate_mapa_alocacao";
    /**
     * Mensagem de erro para valida��o de custo TCE por pessoa
     */
    public static final String MSG_ERROR_DRE_PROCESS_VALIDATE_TCE = "_nls.dre_processos_fechamento.msg.error.validate_custo_tce";
    /**
     * Mensagem para identificar a valida��o de grupo de custo e custo TCE por
     * pessoa
     */
    public static final String MSG_DRE_PROCESS_VALIDATE_COST_GROUP_AND_COST_TCE = "_nls.dre_processos_fechamento.msg.cost_group.and.tce_cost";
    /**
     * Mensagem para identificar a valida��o de grupo de custo e custo TCE por
     * pessoa
     */
    public static final String MSG_DRE_PROCESS_VALIDATE_PROSPECT_ALLOCATION_MAP = "_nls.dre_processos_fechamento.msg.prospect_allocation_map";
    /**
     * Mensagem de erro para consolidacao DRE.
     */
    public static final String MSG_ERROR_CONSOLIDATE_DRE = "_nls.dre_fechamento.error.consolidate";
    /**
     * Mensagem de sucesso ao apropriar PL e HE no Fechamento da DRE.
     */
    public static final String MSG_SUCCESS_REGISTER_PL_HE_COSTS_DRE = "_nls.dre_log_fechamento.msg.success.register_pl_he_costs";
    /**
     * Mensagem de erro ao validar o DreLogFechamento.
     */
    public static final String MSG_ERROR_VALIDATE_DRE_LOG_FECHAMENTO = "_nls.dre_log_fechamento.msg.error.validate";
    /**
     * Constante para id do Messages do Duty Hours and Overtime
     */
    public static final String REGISTER_PL_AND_HE_COSTS_ID = "registerPLAndHECosts";
    /**
     * Constante para id do Messages do Set Exchange Rates
     */
    public static final String CONSOLIDATE_ID = "consolidateId";
    /**
     * Mensagem de sucesso ao integrar as Fatura.
     */
    public static final String INTEGRACAO_FATURA_MSG_SUCCESS = "_nls.fatura_integracao.msg.success";
    /**
     * Mensagem de operacao completada ao integrar as Fatura.
     */
    public static final String INTEGRACAO_FATURA_MSG_COMPLETE = "_nls.fatura_integracao.msg.complete";
    /**
     * Mensagem de operacao completada ao integrar as Fatura.
     */
    public static final String INTEGRACAO_FATURA_MSG_ERROR = "_nls.fatura_integracao.msg.error";
    /**
     * Mensagem de operacao completada ao reintegrar a receita-deal.
     */
    public static final String INTEGRACAO_FATURA_MSG_ERROR_NOT_CANCELED = "_nls.fatura_integracao.msg.error.not_canceled";
    /**
     * Mensagem de sucesso ao reintegrar as receitas.
     */
    public static final String INTEGRACAO_FATURA_REINTEGRATE_MSG_SUCCESS = "_nls.fatura_integracao.msg.success.reintegrado";
    /**
     * Mensagem de erro ao tentar integrar a fatura com status n�o permitido.
     */
    public static final String INTEGRACAO_FATURA_INTEGRATE_MSG_ERROR_STATUS = "_nls.fatura.msg.error.integracao.status";
    /**
     * Mensagem de erro ao tentar integrar a fatura sem data de previsao ou data
     * de vencimento preenchidas.
     */
    public static final String INTEGRACAO_FATURA_INTEGRATE_MSG_ERROR_DATES = "_nls.fatura.msg.error.integracao.dates";
    /**
     * Mensagem de erro ao tentar integrar a fatura com valor total zerado
     */
    public static final String INTEGRACAO_FATURA_INTEGRATE_MSG_ERROR_ZERO_VALUE_TOTAL = "_nls.fatura.msg.error.integracao.zero.value.total";
    /**
     * Mensagem de erro ao salvar a fatura e alterar o status de Integrate para
     * Submitted e a data de vencimento e nula.
     */
    public static final String FATURA_DUE_DATE_NULL = "_nls.fatura.msg.error.due_date_null";
    /**
     * Mensagem de erro ao tentar cancelar a fatura com status n�o permitido.
     */
    public static final String INTEGRACAO_FATURA_CANCEL_MSG_ERROR_STATUS = "_nls.fatura.msg.error.cancel.status";
    /**
     * Mensagem de sucesso ao integrar as receitas.
     */
    public static final String INTEGRACAO_RECEITA_MSG_SUCCESS = "_nls.receita_integracao.msg.success";
    public static final String INTEGRACAO_LICENCA_SOFTWARE_MSG_SUCCESS = "_nls.licenca_software_integracao.msg.success";
    public static final String INTEGRACAO_LICENCA_SOFTWARE_MSG_APPROVE_NOTAFISCAL_MISSING = "_nls.licenca_software_integracao.msg.approve.notafiscal.missing";
    public static final String INTEGRACAO_LICENCA_SOFTWARE_MSG_APPROVE_SUCCESS = "_nls.licenca_software_integracao.msg.approve.success";
    public static final String INTEGRACAO_LICENCA_SOFTWARE_MSG_MAIL_SENT_SUCCESS = "_nls.licenca_software_mail.sent.msg.success";
    /**
     * Mensagem de sucesso ao integrar as receitas.
     */
    public static final String INTEGRACAO_RECEITA_MSG_WARNING_MORE_THAN_ONE_INTERCOMPANY = "_nls.receita_integracao.msg.some_revenues_was_not_integrated";
    /**
     * Mensagem de sucesso ao integrar as receitas.
     */
    public static final String INTEGRACAO_RECEITA_MSG_WARNING_NO_INTERCOMPANY_RESOURCE = "_nls.receita_integracao.msg.no_intercompany_resource";
    /**
     * Mensagem de sucesso ao reintegrar as receitas.
     */
    public static final String INTEGRACAO_RECEITA_REINTEGRATE_MSG_SUCCESS = "_nls.receita_integracao.msg.success.reintegrado";
    /**
     * Mensagem de erro ao conectar com a base dados do mega.
     */
    public static final String INTEGRACAO_RECEITA_DATABASE_ACCESS_ERP_ERROR = "_nls.receita_integracao.msg.error.database_access_erp";
    /**
     * Mensagem de erro ao conectar com a fila do Rabbit de integração do mega.
     */
    public static final String INTEGRACAO_RECEITA_INTEGRATOR_ACCESS_ERP_ERROR = "_nls.receita_integracao.msg.error.integrator_access_erp";
    /**
     * Mensagem de erro ao tentar integrar uma fatura, que n�o possui 'numero do
     * item' e 'numero servico' cadastrado.
     */
    public static final String INTEGRACAO_FATURA_PARAM_NOT_FIND = "_nls.fatura_integracao.msg.error.param_not_find";
    /**
     * Mensagem de operacao completada ao integrar as receitas.
     */
    public static final String INTEGRACAO_RECEITA_MSG_COMPLETE = "_nls.receita_integracao.msg.complete";
    public static final String INTEGRACAO_LICENCA_SOFTWARE_MSG_COMPLETE = "_nls.licenca_software_integracao.msg.complete";
    public static final String INTEGRACAO_LICENCA_SOFTWARE_MSG_NOT_ALL_APPROVED = "_nls.licenca_software_integracao.msg.not_all_approved";
    public static final String INTEGRACAO_LICENCA_SOFTWARE_MSG_NOT_ALL_WAITING_APPROVAL = "_nls.licenca_software_integracao.msg.not_all_waiting_approval";
    /**
     * Mensagem de operacao completada ao integrar as receitas.
     */
    public static final String INTEGRACAO_RECEITA_MSG_ERROR = "_nls.receita_integracao.msg.error";
    public static final String INTEGRACAO_LICENCA_SOFTWARE_MSG_ERROR = "_nls.licenca_software_integracao.msg.error";
    public static final String INTEGRACAO_LICENCA_SOFTWARE_WITHOUT_VALUE = "_nls.licenca_software_integracao.amount.without.value";
    /**
     * Mensagem de operacao completada ao reintegrar a receita-deal.
     */
    public static final String INTEGRACAO_RECEITA_MSG_ERROR_NOT_CANCELED = "_nls.receita_integracao.msg.error.not_canceled";
    /**
     * Mensagem de operacao completada ao integrar as receitas.
     */
    public static final String INTEGRACAO_RECEITA_MSG_INFO_NULL = "_nls.receita_integracao.msg.info_null";
    public static final String RECEITA_MSG_AVISO_PENDENTE = "_nls.receita.msg.aviso_pendente";
    /**
     * Mensagem de operacao completada ao integrar as licencas de software.
     */
    public static final String INTEGRACAO_LICENCA_SOFTWARE_MSG_INFO_NULL = "_nls.licenca_software_integracao.msg.info_null";
    /**
     * Mensagem de sucesso ao copiar Fatura.
     */
    public static final String FATURA_MSG_SUCCESS_COPY = "_nls.fatura.msg.copy.success";
    /**
     * Mensagem de sucesso ao copiar Alocacao.
     */
    public static final String ALOCACAO_MSG_SUCCESS_COPY = "_nls.alocacao.msg.copy.success";
    /**
     * Mensagem de sucesso ao copiar ComposicaoTce.
     */
    public static final String COMPOSICAO_TCE_MSG_SUCCESS_COPY = "_nls.composicao_tce.msg.copy.success";
    /**
     * Mensagem de erro ao copiar ComposicaoTce.
     */
    public static final String COMPOSICAO_TCE_MSG_ERROR_COPY = "_nls.composicao_tce.msg.copy.error";
    /**
     * Mensagem de sucesso Full Synchronization do ComposicaoTce.
     */
    public static final String COMPOSICAO_TCE_MSG_SUCCESS_SYNC_FULL = "_nls.composicao_tce.msg.sync_full.success";
    /**
     * Mensagem de sucesso Partial Synchronization do ComposicaoTce.
     */
    public static final String COMPOSICAO_TCE_MSG_SUCCESS_SYNC_PARTIAL = "_nls.composicao_tce.msg.sync_partial.success";
    /**
     * Mensagem de erro na sincroniza��o do ComposicaoTce.
     */
    public static final String COMPOSICAO_TCE_MSG_ERROR_SYNC = "_nls.composicao_tce.msg.sync.error";
    /**
     * Mensagem de warning na sincroniza��o do ComposicaoTce.
     */
    public static final String COMPOSICAO_TCE_MSG_WARNG_SYNC = "_nls.composicao_tce.msg.sync.warng";
    /**
     * Mensagem de erro na sincroniza��o do ComposicaoTce - registros
     * duplicados.
     */
    public static final String COMPOSICAO_TCE_MSG_LOGIN_ERROR_SYNC = "_nls.composicao_tce.msg.sync.login_error";
    /**
     * Mensagem de sucesso ao copiar ChargebackPessoa.
     */
    public static final String CHBACK_PESS_MSG_SUCCESS_COPY = "_nls.chback_pess.msg.copy.success";
    /**
     * Mensagem de erro ao copiar ChargebackPessoa.
     */
    public static final String CHBACK_PESS_MSG_ERROR_COPY = "_nls.chback_pess.msg.copy.error";
    /**
     * Mensagem de sucesso Full Synchronization do ChargebackPessoa.
     */
    public static final String CHBACK_PESS_MSG_SUCCESS_SYNC_FULL = "_nls.chback_pess.msg.sync_full.success";
    /**
     * Mensagem de sucesso Partial Synchronization do ChargebackPessoa.
     */
    public static final String CHBACK_PESS_MSG_SUCCESS_SYNC_PARTIAL = "_nls.chback_pess.msg.sync_partial.success";
    /**
     * Mensagem de erro na sincroniza��o do ChargebackPessoa.
     */
    public static final String CHBACK_PESS_MSG_ERROR_SYNC = "_nls.chback_pess.msg.sync.error";
    /**
     * Mensagem de warning na sincroniza��o do ChargebackPessoa.
     */
    public static final String CHBACK_PESS_MSG_WARNG_SYNC = "_nls.chback_pess.msg.sync.warng";
    public static final String CHBACK_PESS_MSG_WARNG_SEARCH_REQUIRED = "_nls.chback_pess.search.required";
    public static final String CHBACK_PESS_MSG_WARNG_INVALID_DATE = "_nls.chback_pess.tooltip.alert.date";
    /**
     * Mensagem de erro na sincroniza��o do ChargebackPessoa - registros
     * duplicados.
     */
    public static final String CHBACK_PESS_MSG_ERROR_SYNC_LOGIN_DUPLIC = "_nls.chback_pess.msg.sync.login_error_duplicated";
    /**
     * Mensagem de erro na sincroniza��o do ChargebackPessoa - registros de
     * Pessoa nao cadastrado na base.
     */
    public static final String CHBACK_PESS_MSG_ERROR_SYNC_LOGIN_NOT_EXIST = "_nls.chback_pess.msg.sync.login_error_not_exist";
    /**
     * Mensagem de erro no cadastro e remoção de Custo de TI Recurso para o mês determinado
     * TI Recurso já aprovado para o mês.
     */
    public static final String MSG_ERROR_ADD_CUSTO_TI_RECURSO_SELECTED_DATE_APPROVED = "_nls.msg.error.add.custo.ti_recurso.selected_date.approved";
    public static final String MSG_ERROR_REMOVE_CUSTO_TI_RECURSO_SELECTED_DATE_APPROVED = "_nls.msg.error.remove.custo.ti_recurso.selected_date.approved";
    /**
     * Mensagem de erro na sincroniza��o do ChargebackPessoa - registros de
     * TiRecurso nao cadastrado na base.
     */
    public static final String CHBACK_PESS_MSG_ERROR_SYNC_TI_RECURSO_NOT_EXIST = "_nls.chback_pess.msg.sync.ti_recurso_error_not_exist";
    /**
     * Mensagem de sucesso na sincroniza��o do ChargebackPessoa - quantidade de
     * registros sincronizados.
     */
    public static final String CHBACK_PESS_MSG_SUCCESS_SYNC_QTDE_REG = "_nls.chback_pess.msg.sync.success_qtde_reg";
    /**
     * Mensagem de erro de campo obrigatorio - BEGIN MONTH
     */
    public static final String CHBACK_PESS_MSG_ERROR_BEGIN_MONTH_REQUIRED = "_nls.chback_pess.msg.error.required.begin_month";
    /**
     * Mensagem de erro de campo obrigatorio - BEGIN YEAR
     */
    public static final String CHBACK_PESS_MSG_ERROR_BEGIN_YEAR_REQUIRED = "_nls.chback_pess.msg.error.required.begin_year";
    /**
     * Mensagem de erro de campo obrigatorio - IT RESOURCE
     */
    public static final String CHBACK_PESS_MSG_ERROR_IT_RESOURCE_REQUIRED = "_nls.chback_pess.msg.error.required.it_resource";
    /**
     * Mensagem de erro de campo obrigatorio - TICKET ID
     */
    public static final String CHBACK_PESS_MSG_ERROR_TICKET_ID_REQUIRED = "_nls.chback_pess.msg.error.required.ticket_id";
    /**
     * Mensagem de erro de campo obrigatorio - DESCRIPTION
     */
    public static final String CHBACK_PESS_MSG_ERROR_DESCRIPTION_REQUIRED = "_nls.chback_pess.msg.error.required.description";
    /**
     * Mensagem de erro de campo obrigatorio - INSTALMENTS
     */
    public static final String CHBACK_PESS_MSG_ERROR_INSTALMENTS_REQUIRED = "_nls.chback_pess.msg.error.required.instalments";
    /**
     * Mensagem de erro de campo obrigatorio - TYPE
     */
    public static final String CHBACK_PESS_MSG_ERROR_TYPE_REQUIRED = "_nls.chback_pess.msg.error.required.type";
    /**
     * Mensagem de erro de campo obrigatorio - CLIENT
     */
    public static final String CHBACK_PESS_MSG_ERROR_CLIENT_REQUIRED = "_nls.chback_pess.msg.error.required.client";
    /**
     * Mensagem de erro de campo obrigatorio - C-LOB / COST CENTER
     */
    public static final String CHBACK_PESS_MSG_ERROR_CLOB_COSTCENTER_REQUIRED = "_nls.chback_pess.msg.error.required.clob_costcenter";
    /**
     * Mensagem de erro de campo obrigatorio - C-LOB / COST CENTER
     */
    public static final String CHBACK_PESS_MSG_ERROR_APPROVED_NEW_CHARGEBACK = "_nls.chback_pess.msg.error.approved.new_chargeback";
    public static final String CHBACK_PESS_MSG_ERROR_EDIT_INTEGRATED_INSTALLMENTS = "_nls.chback_pess.msg.error.edit.integrated_installments";
    public static final String CHBACK_PESS_MSG_ERROR_REACTIVATE_CHBACK_PESS = "_nls.chback_pess.msg.error.reactivate";

    public static final String CHBACK_PESS_MSG_ERROR_LOGINS = "_nls.chback_pess.msg.error.required.login";
    /**
     * Mensagem de erro ao aprivar uma fatura diferente do status open.
     */
    public static final String FATURA_MSG_ERROR_APPROVED = "_nls.fatura.msg.error.approved.cant_be_approved";
    /**
     * Mensagem de erro ao remover fatura apropriada.
     */
    public static final String FATURA_MSG_ERROR_REMOVE_APROPRIADA = "_nls.fatura.msg.error.remove.fatura_apropriada";
    /**
     * Mensagem de erro DealFiscal - Cliente inv�lido.
     */
    public static final String FATURA_MSG_ERROR_DF_INVALID_CLIENT_ENTITY = "_nls.fatura.msg.error.df.invalid_client_entity";
    /**
     * Mensagem de erro DealFiscal - Empresa inv�lida.
     */
    public static final String FATURA_MSG_ERROR_DF_INVALID_COMPANY = "_nls.fatura.msg.error.df.invalid_company";
    /**
     * Mensagem de erro DealFiscal - "It's not possible to create an intercompany between the same company"
     */
    public static final String MSG_ERROR_INTERCOMP_BETWEEN_SAME_COMPANY = "_nls.deal.msg.error.invalid_intercomp_same_company";
    /**
     * Mensagem de erro DealFiscal - Par�metros Intercompany inv�lidos.
     */
    public static final String FATURA_MSG_ERROR_DF_INVALID_INTERCOMP_PARAMS = "_nls.fatura.msg.error.df.invalid_intercomp_params";
    public static final String MSG_ERROR_INTERCOMP_CONFIG = "_nls.deal.msg.error.invalid_intercomp_config";
    public static final String MSG_ERROR_NOT_POSSIBLE_REMOVE_INTERCOMP_BECAUSE_THERE_ARE_ACTIVES_ALLOCATIONS =
            "_nls.deal.msg.error.not_possible_remove_intercomp_because_there_are_actives_allocations";
    public static final String MSG_ERROR_NOT_POSSIBLE_REMOVE_INTERCOMP_BECAUSE_THERE_ARE_REVENUES_NOT_INTEGRATED =
            "_nls.deal.msg.error.not_possible_remove_intercomp_because_there_are_revenues_not_itegrated";
    /**
     * Mensagem de erro DealFiscal - TipoServico inv�lidos.
     */
    public static final String FATURA_MSG_ERROR_DF_INVALID_SERVICE_TYPE = "_nls.fatura.msg.error.df.invalid_service_type";
    /**
     * Mensagem de erro DealFiscal - Novo TipoServico inv�lidos.
     */
    public static final String FATURA_MSG_ERROR_NDF_INVALID_SERVICE_TYPE = "_nls.fatura.msg.error.ndf.invalid_service_type";
    /**
     * Mensagem de erro TipoServico inv�lido.
     */
    public static final String FATURA_MSG_ERROR_INVALID_SERVICE_TYPE = "_nls.fatura.msg.error.tipo_servico_invalido";
    /**
     * Mensagem de sucesso ao remover Fatura.
     */
    public static final String FATURA_MSG_ERROR_REMOVE_SUBMITTED = "_nls.fatura.msg.error.remove.fatura_submitted";
    /**
     * Mensagem de sucesso ao remover Fatura depois da data de fechamento.
     */
    public static final String FATURA_MSG_ERROR_REMOVE_BEFORE_CLOSING_DATE = "_nls.fatura.msg.error.remove.fatura.before_closing_date";
    /**
     * Mensagem de error para restri��o de integridade - cancel da Fatura.
     */
    public static final String FATURA_MSG_ERROR_INTEGRITY_CONSTRAINT_CANCEL = "_nls.fatura.msg.error.integrity_constraint_cancel";
    /**
     * Mensagem de warning para Fatura (Opened ou Approved) expirada (data menor
     * sysdate).
     */
    public static final String FATURA_MSG_WARNG_OUTDATED = "_nls.fatura.msg.warn.outdated";
    /**
     * Mensagem de warning para Fatura - deu date menor que invoice date.
     */
    public static final String FATURA_MSG_WARNG_INVALID_DUE_DATE = "_nls.fatura.msg.warn.due_date_invalid";
    /**
     * Mensagem de warning para Fatura - payment date menor que invoice date.
     */
    public static final String FATURA_MSG_WARNG_INVALID_PAYMENT_DATE = "_nls.fatura.msg.warn.payment_date_invalid";
    /**
     * Mensagem de erro ao criar uma fatura com a data previs�o, anterior a data
     * de fechamento.
     */
    public static final String INTEGRACAO_FATURA_MSG_ERROR_BEFORE_CLOSING_DATE = "_nls.fatura.msg.error.data_previsao_before_closing_date";
    /**
     * Mensagem de error de update quando tentar atribuir um ClientePai para um
     * ClientePai.
     */
    public static final String MSG_ERROR_UPDATE_CLIENTE_PAI = "_nls.cliente.msg.error.update_cliente_pai";
    /**
     * Mensagem de error de update quando tentar atribuir uma EmpresaPai para
     * uma EmpresaPai.
     */
    public static final String MSG_ERROR_UPDATE_EMPRESA_PAI = "_nls.empresa.msg.error.update_empresa_pai";

    /**
     * Mensagem de error de cria��o de Empresa - EmpresaPai não existe.
     */
    public static final String MSG_ERROR_EMPRESA_PAI_NOT_FOUND = "_nls.empresa.msg.error.empresa_pai_not_found";
    /**
     * Mensagem de error de update quando tentar atualizar um registro de
     * uma CidadeBase.
     */
    public static final String MSG_ERROR_UPDATE_CIDADE_BASE = "_nls.cidade_base.msg.error.update_cidade_base";
    /**
     * Mensagem de error de cria��o e atualiza��o de Empresa - Acronym
     * duplicado.
     */
    public static final String MSG_ERROR_ALREADY_EXISTS_ACRONYM = "_nls.empresa.msg.error.already_exists_acronym";
    /**
     * Msg de error para percent alocacao invalido para Recurso tipo Pessoa.
     */
    public static final String MSG_ERROR_INVAL_PERCENT_TYPE_PE = "_nls.msg.error.inval_percent_type_pe";
    /**
     * Msg de error percent alocacao invalido p Recurso tipo PapelAlocacao.
     */
    public static final String MSG_ERROR_INVAL_PERCENT_TYPE_PA = "_nls.msg.error.inval_percent_type_pa";
    /**
     * Mensagem de error para percentual alocacao invalido, step quebrado.
     */
    public static final String MSG_ERROR_INVAL_PERCENT_TYPE = "_nls.msg.error.inval_percent_type";
    /**
     * Mensagem de error para utilization rate invalido.
     */
    public static final String MSG_ERROR_INVAL_UTILIZATION_RATE = "_nls.msg.error.inval_utilization_rate";
    /**
     * Mensagem de error para Alocacao invalida, preenchimento incompleto.
     */
    public static final String MSG_ERROR_INVAL_ALLOCATION = "_nls.msg.error.inval_allocation";
    /**
     * Mensagem de warning para busca dos ItemHrsFatDeal (Deal e mes/ano) n�o
     * encontrados.
     */
    public static final String MSG_WARNG_ITEM_HRS_NO_RESULT = "_nls.item_hrs_fat_deal.msg.warn.search.no_result";
    /**
     * Mensagem de warning para busca do Ajuste da Receita.
     */
    public static final String MSG_WARN_REVENUE_ADJUSTMENT_DATE_SELECT = "_nls.ajuste_receita.msg.date_select";
    /**
     * Mensagem de warning para busca do Ajuste da Receita.
     */
    public static final String MSG_WARN_ADJUST_TYPE_NULL = "_nls.ajuste_receita.msg.tipo_ajuste_nulo";
    /**
     * Mensagem de warning para busca do Ajuste da Receita ano ajuste nulo.
     */
    public static final String MSG_WARN_REVENUE_ADJUSTMENT_YEAR_ADJUST_NULL = "_nls.ajuste_receita.msg.ano_ajuste_nulo";
    /**
     * Mensagem de warning para busca do Ajuste da Receita mes ajuste nulo.
     */
    public static final String MSG_WARN_REVENUE_ADJUSTMENT_MONTH_ADJUST_NULL = "_nls.ajuste_receita.msg.mes_ajuste_nulo";
    /**
     * Constante para tipo de ajuste "Adjsutment".
     */
    public static final String TIPO_AJUSTE_ADJUSTMENT = "Adjustment";
    /**
     * Constante para tipo de ajuste "Loss".
     */
    public static final String TIPO_AJUSTE_LOSS = "Loss";
    /**
     * Constante para tipo de ajuste "Reproval".
     */
    public static final String TIPO_AJUSTE_REPROVAL = "Reproval";
    /**
     * Mensagem de erro para dealFiscal inexsitente ou inativo.
     */
    public static final String MSG_ERROR_DEALFISCAL_INATIVO = "_nls.deal_fiscal.inativo";
    /**
     * Mensagem de erro para clientEntity inexsitente ou inativo.
     */
    public static final String MSG_ERROR_CLIENTENTITY_INATIVO = "_nls.client_entity.inativo";
    /**
     * Mensagem de erro para clientEntity inexsitente ou inativo.
     */
    public static final String MSG_ERROR_DELIVERYPLACE_EMPTY = "_nls.delivery_place.vazio";
    /**
     * Mensagem de warning para busca do Ajuste da Receita ano receita nulo.
     */
    public static final String MSG_WARN_REVENUE_ADJUSTMENT_YEAR_REVENUE_NULL = "_nls.ajuste_receita.msg.ano_receita_nulo";
    /**
     * Mensagem de warning para busca do Ajuste da Receita mes receita nulo.
     */
    public static final String MSG_WARN_REVENUE_ADJUSTMENT_MONTH_REVENUE_NULL = "_nls.ajuste_receita.msg.mes_receita_nulo";
    /**
     * Mensagem de warning para Ajuste da Receita login nulo.
     */
    public static final String MSG_WARN_REVENUE_ADJUSTMENT_REPORTER_LOGIN_NULL = "_nls.ajuste_receita.msg.reporter_login_nulo";
    /**
     * Mensagem de warning para MSA sem receita.
     */
    public static final String MSG_WARN_REVENUE_NULL = "_nls.ajuste_receita.warning.receita_nulo";
    /**
     * Mensagem de warning para MSA sem ajuste.
     */
    public static final String MSG_WARN_NOT_REVENUE_AJUSTMENT = "_nls.ajuste_receita.warning.warning.not_ajuste_receita";
    /**
     * Mensagem de erro para closing date de Revenue.
     */
    public static final String MSG_ERROR_REVENUE_CLOSING_DATE = "_nls.receita.review_ur.msg.error.closing_date";
    /**
     * Mensagem de erro para International closing date de Revenue.
     */
    public static final String MSG_ERROR_INTERNATIONAL_REVENUE_CLOSING_DATE = "_nls.receita.review_ur.msg.error.international_closing_date";

    public static final String MSG_NOT_FOUND_CIDADE_BASE_FILIAL = "_nls.cidade_base_filial.msg.not_found";

    public static final String MSG_ALREADY_EXISTS_CIDADE_BASE_FILIAL = "_nls.cidade_base_filial.msg.already_exists";

    /**
     * Mensagem de erro de criacao e atualizacao de Cidade Base - Acronym
     * duplicado.
     */
    public static final String MSG_ERROR_ALREADY_EXISTS_CIDADE_BASE_ACRONYM = "_nls.cidade_base.msg.error.already_exists_acronym";
    /**
     * Mensagem de erro de criacao e atualizacao de Cidade Base - Name
     * duplicado.
     */
    public static final String MSG_ERROR_ALREADY_EXISTS_CIDADE_BASE_NAME = "_nls.cidade_base.msg.error.already_exists_name";
    /**
     * Mensagem de erro de deleção de Cidade Base - Possui
     * dependência.
     */
    public static final String MSG_ERROR_CIDADE_BASE_HAS_DEPENDENCY = "_nls.cidade_base.msg.error.has_dependency";
    /**
     * Mensagem de erro de inativação de Cidade Base.
     */
    public static final String MSG_ERROR_NOT_EXISTS_CIDADE_BASE = "_nls.cidade_base.msg.error.not_exists";
    /**
     * Mensagem de warning para busca dos ItemReceita (ContratoPratica e
     * mes/ano) n�o encontrados.
     */
    public static final String MSG_WARNG_ITEM_RECEITA_NO_RESULT = "_nls.item_receita.msg.warn.search.no_result";
    public static final String MSG_WARNG_ITEM_RECEITA_INTERCOMPANY = "_nls.item_receita.msg.warn.intercompany.resource";
    /**
     * Mensagem de sucesso para a criacao de uma EstratificacaoUr.
     */
    public static final String MSG_SUCCESS_CREATE_REVIEW = "_nls.receita.review_ur.msg.create.success";
    /**
     * Mensagem de sucesso para a edicao de uma EstratificacaoUr.
     */
    public static final String MSG_SUCCESS_UPDATE_REVIEW = "_nls.receita.review_ur.msg.update.success";
    /**
     * Mensagem de sucesso para a delecao de uma EstratificacaoUr.
     */
    public static final String MSG_SUCCESS_DELETE_REVIEW = "_nls.receita.review_ur.msg.delete.success";
    /**
     * Mensagem de error para ItemHrsFatDeal invalida, preenchimento incompleto,
     * os campos de valores devem ser maior do que zero.
     */
    public static final String MSG_ERROR_INVAL_ITEM_HRS_FAT_DEAL_VALUES = "_nls.msg.error.inval_item_hrs_fat_deal_values";
    /**
     * Mensagem de error para ItemReceita invalida, preenchimento incompleto, os
     * campos de valores devem ser maior do que zero.
     */
    public static final String MSG_ERROR_INVAL_ITEM_RECEITA_VALUES = "_nls.msg.error.inval_item_receita_values";
    public static final String MSG_ERROR_INVAL_ALLOCATION_ZERO_VALUES = "_nls.msg.error.inval_allocation_zero_values";
    /**
     * Mensagem de error para lista de ItemHrsFatDeal invalida/vazia.
     */
    public static final String MSG_ERROR_INVAL_ITEM_HRS_FAT_DEAL_LIST = "_nls.msg.error.inval_item_hrs_fat_deal_list";
    /**
     * Mensagem de error para lista de ItemReceita invalida/vazia.
     */
    public static final String MSG_ERROR_INVAL_ITEM_RECEITA_LIST = "_nls.msg.error.inval_item_receita_list";
    /**
     * Mensagem de warning para quando o total de receita for diferente do total
     * dos deals.
     */
    public static final String MSG_ERROR_RECEITA_TOTAL_NOT_MATCH = "_nls.receita.msg.error.total_not_match";
    /**
     * Mensagem de warning para quando o total de receita for diferente do total
     * dos deals.
     */
    public static final String MSG_SUCCESS_RECEITA_TOTAL_NOT_MATCH = "_nls.receita.msg.success.total_not_match";
    /**
     * Mensagem de warning para quando o total de receita for diferente do total
     * dos deals.
     */
    public static final String MSG_SUCCESS_RECEITA_MANY_TOTAL_NOT_MATCH = "_nls.receita.msg.success.many_total_not_match";
    /**
     * Mensagem de warning para quando n�o existir deal relacionado com um
     * contrato-pratica.
     */
    public static final String MSG_ERROR_RECEITA_NO_DEAL = "_nls.receita.msg.error.no_deal";
    /**
     * Mensagem de error para ItemHrsFatDeal invalida, preenchimento incompleto,
     * o campo pre�o deve ser maior do que zero (ItemTabelaPreco).
     */
    public static final String MSG_ERROR_INVAL_ITEM_HRS_FAT_DEAL_PRICE = "_nls.msg.error.inval_item_hrs_fat_deal_price";
    /**
     * Mensagem de error para ItemReceita invalida, preenchimento incompleto, o
     * campo pre�o deve ser maior do que zero (ItemTabelaPreco).
     */
    public static final String MSG_ERROR_INVAL_ITEM_RECEITA_PRICE = "_nls.msg.error.inval_item_receita_price";
    /**
     * Mensagem de error para ItemReceita, quando o C-LOB se faz obrigat�rio.
     */
    public static final String MSG_ERROR_ITEM_RECEITA_REQUIRED_CLOB = "_nls.msg.error.item_receita.required_contract_lob";
    /**
     * Mensagem de error para ItemFatura quando valor negativo for inputado.
     */
    public static final String MSG_ERROR_ITEM_FATURA_NEGATIVE_VALUE = "_nls.msg.error.item_fatura.negative_value";
    /**
     * Mensagem de error para AlocacaoPeriodo invalida, preenchimento
     * incompleto, pelo menos 1 com valor maior do que zero.
     */
    public static final String MSG_ERROR_INVAL_ALLOCATION_PERCENTAGE = "_nls.msg.error.inval_allocation_percentage";
    /**
     * Mensagem de error para lista de Alocacao invalida/vazia.
     */
    public static final String MSG_ERROR_INVAL_ALLOCATION_LIST = "_nls.msg.error.inval_allocation_list";
    /**
     * Mensagem de error para Mapa de Alocacao invalido (mesmo ContratoPratica x
     * Vers�o).
     */
    public static final String MSG_ERROR_INVAL_ALLOCATION_MAP = "_nls.msg.error.inval_allocation_map";
    /**
     * Mensagem de error para quando aplicar Etiqueta (tag) é uma Alocacao
     * invalida (não salva).
     */
    public static final String MSG_ERROR_INVAL_ALLOCATION_TAG = "_nls.msg.error.inval_allocation_tag";
    /**
     * Mensagem de error para quando copiar uma Alocacao inválida (não salva).
     */
    public static final String MSG_ERROR_INVAL_ALLOCATION_COPY = "_nls.msg.error.inval_allocation_copy";
    /**
     * Mensagem de error para quando copiar uma Alocacao inv�lida (sem
     * AlocacaoPeriodo no mes anterior).
     */
    public static final String MSG_ERROR_INVAL_ALLOCATION_COPY_NO_PERCENTAGE = "_nls.msg.error.inval_allocation_copy_no_percentage";
    /**
     * Mensagem de error para exclusao de MapaAlocacao.
     */
    public static final String MSG_ERROR_REMOVE_ALLOCATION_MAP = "_nls.mapa_alocacao.msg.error.remove";
    /**
     * Mensagem de recurso inativo no mapa de alocacao.
     */
    public static final String MSG_WARN_RECURSO_INATIVO = "_nls.mapa_alocacao.msg.warning.recurso_inativo";
    /**
     * Mensagem de erro para a vigencia da tabela de preco.
     */
    public static final String MSG_ERROR_ADD_TABELA_PRECO_PERIOD = "_nls.tabela_preco.error.vigencia";
    /**
     * Mensagem de erro para a vigencia do GrupoCustoPeriodo.
     */
    public static final String MSG_ERROR_ADD_GRUPO_CUSTO_PERIODO = "_nls.grupo_custo_periodo.error.vigencia";
    /**
     * Mensagem de erro para a vigencia do TcePapelAlocacao.
     */
    public static final String MSG_ERROR_ADD_TCE_PAPEL_ALOCACAO_PERIOD = "_nls.tce_papel_alocacao.error.vigencia";
    /**
     * Mensagem de erro para a vigencia do CustoBasePapelAlocacao.
     */
    public static final String MSG_ERROR_ADD_CUSTO_BASE_PAPEL_ALOCACAO_PERIOD = "_nls.custo_base_papel_alocacao.error.vigencia";
    /**
     * Mensagem de erro para o Total TCE igual a '0'.
     */
    public static final String MSG_ERROR_ADD_CUSTO_BASE_PAPEL_ALOCACAO_TCE_EQUALS_ZERO = "_nls.custo_base_papel_alocacao.error.tce.equals.zero";
    public static final String MSG_ERROR_ADD_CUSTO_BASE_PAPEL_ALOCACAO_SALARY_GREATER_THAN_ZERO = "_nls.msg.error.base_papel_alocacao.salary.greater_than_zero";
    public static final String MSG_ERROR_ADD_CUSTO_BASE_PAPEL_ALOCACAO_BENEFIT_NEGATIVE = "_nls.msg.error.base_papel_alocacao.benefit.negative";
    public static final String MSG_ERROR_ADD_CUSTO_BASE_PAPEL_ALOCACAO_TAX_NEGATIVE = "_nls.msg.error.base_papel_alocacao.tax.negative";
    public static final String MSG_ERROR_ADD_CUSTO_BASE_PAPEL_ALOCACAO_PROVISIONING_NEGATIVE = "_nls.msg.error.base_papel_alocacao.provisioning.negative";
    public static final String MSG_ERROR_ADD_CUSTO_BASE_PAPEL_ALOCACAO_TCE_NEGATIVE = "_nls.msg.error.base_papel_alocacao.tce.negative";
    public static final String MSG_ERROR_ADD_CUSTO_BASE_PAPEL_ALOCACAO_ALREADY_EXISTS = "_nls.custo_base_papel_alocacao.error.tce_cost.already_exists";
    /**
     * Mensagem de erro para o Start Date posterior a Data atual.
     */
    public static final String MSG_ERROR_ADD_CUSTO_BASE_PAPEL_ALOCACAO_START_DATE_DATA_ATUAL = "_nls.custo_base_papel_alocacao.error.start_date.data_atual";
    /**
     * Mensagem de erro para a ja existencia do TaxaImposto.
     */
    public static final String MSG_ERROR_ADD_EDIT_TAXA_IMPOSTO = "_nls.taxa_imposto.error.add";
    /**
     * Mensagem de erro para contrato pratica incompleto.
     */
    public static final String MSG_ERROR_CONTRATO_PRATICA_INCOMPLETE = "_nls.contrato_pratica.msg.error.status_incomplete";
    /**
     * Mensagem de sucesso para contrato pratica completo.
     */
    public static final String MSG_SUCCESS_CONTRATO_PRATICA_COMPLETE = "_nls.contrato_pratica.msg.success.status_complete";
    /**
     * Mensagem de error para cadastro PerfilVendido.
     */
    public static final String MSG_ERROR_CREATE_LEAST_ONE = "_nls.msg.error.create.least_one";
    /**
     * Mensagem de sucesso para contrato pr�tica 'Save As'.
     */
    public static final String MSG_SUCCESS_CONTRATO_PRATICA_SAVE_AS = "_nls.mapa_alocacao.msg.success.save_as";
    /**
     * Mensagem de warning para contrato pr�tica 'Save As' quando h�
     * estratifica��o relacionada ao MapaAlocacao republicado.
     */
    public static final String MSG_WARNG_MAPA_ALOCACAO_SAVE_AS_STRATIFICATION = "_nls.mapa_alocacao.msg.success.save_as.stratification";
    /**
     * Mensagem de sucesso para HorasFaturasDeal 'Save As'.
     */
    public static final String MSG_SUCCESS_HORAS_HORAS_DEAL_SAVE_AS = "_nls.horas_faturadas_deal.msg.success.save_as";
    /**
     * Mensagem de sucesso para Receita 'Save As'.
     */
    public static final String MSG_SUCCESS_RECEITA_SAVE_AS = "_nls.receita.msg.success.save_as";
    /**
     * Bundle para a vers�o WORKING do HorasFaturadasDeal.
     */
    public static final String BUNDLE_KEY_HORAS_FAT_DEAL_VERSION_WORKING = "_nls.horas_faturadas_deal.indicador_versao.working.label";
    /**
     * Bundle para a vers�o PUBLISHED do HorasFaturadasDeal.
     */
    public static final String BUNDLE_KEY_HORAS_FAT_DEAL_VERSION_PUBLISHED = "_nls.horas_faturadas_deal.indicador_versao.published.label";
    /**
     * Bundle para a vers�o WORKING da Receita.
     */
    public static final String BUNDLE_KEY_RECEITA_VERSION_WORKING = "_nls.receita.indicador_versao.working.label";
    /**
     * Bundle para a vers�o PUBLISHED do Receita.
     */
    public static final String BUNDLE_KEY_RECEITA_VERSION_PUBLISHED = "_nls.receita.indicador_versao.published.label";
    /**
     * Bundle para a vers�o INTEGRATED do Receita.
     */
    public static final String BUNDLE_KEY_RECEITA_VERSION_INTEGRATED = "_nls.receita.indicador_versao.integrated.label";
    /**
     * Bundle para a vers�o PENDING do Receita.
     */
    public static final String BUNDLE_KEY_RECEITA_VERSION_PENDING = "_nls.receita.indicador_versao.pending.label";
    /**
     * Mensagem de erro para data inicial invalida do dashboard do mapa.
     */
    public static final String MAPA_ALOCACAO_DASHBOARD_MSG_ERROR_START_DATE = "_nls.mapa_alocacao.dashboard.msg.error.start_date";
    /**
     * Mensagem de erro para data final invalida do dashboard do mapa.
     */
    public static final String MAPA_ALOCACAO_DASHBOARD_MSG_ERROR_END_DATE = "_nls.mapa_alocacao.dashboard.msg.error.end_date";
    /**
     * Mensagem de aviso para HorasFaturasDeal para quando existir papel
     * aloca��o no MapaAloca��o.
     */
    public static final String MSG_ERROR_HORAS_HORAS_DEAL_EXISTE_PAPEL = "_nls.horas_faturadas_deal.msg.error.existe.papel";
    /**
     * Mensagem de aviso para Receita para quando existir papel aloca��o no
     * MapaAloca��o.
     */
    public static final String MSG_ERROR_RECEITA_EXISTE_PAPEL = "_nls.receita.msg.error.existe.papel";
    /**
     * Mensagem de aviso quando um perfil vendido n�o possui valor/hora
     * cadastrado.
     */
    public static final String PERFIL_VENDIDO_MSG_WARNING_NO_PRICE = "_nls.perfil_vendido.msg.warning.no_price";
    /**
     * Mensagem de aviso quando um perfil vendido n�o possui valor/hora
     * cadastrado.
     */
    public static final String PERFIL_VENDIDO_MSG_WARNING_NO_PRICE_FOR_ALOCATION = "_nls.perfil_vendido.msg.warning.no_price_for_alocation";
    /**
     * Mensagem de error para exclusao de uma associa��o de CentroLucro.
     */
    public static final String MSG_ERROR_REMOVE_CENTRO_LUCRO = "_nls.cpcl.error.remove";
    /**
     * Mensagem de error para exclusao de uma associa��o de CentroLucro.
     */
    public static final String MSG_ERROR_INVALID_CPCL_START_DATE = "_nls.cpcl.error.vigencia";
    /**
     * Mensagem de erro do login (usuario e password).
     */
    public static final String MSG_ERRO_LOGIN_INVALID_USER_PASS = "_nls.login.msg.error.invalid.user_pass";
    /**
     * Mensagem de erro do login (usuario n�o existe).
     */
    public static final String MSG_ERRO_LOGIN_INVALID_USER = "_nls.login.msg.error.invalid.user";
    /**
     * Mensagem de erro para a vigencia da aliquota.
     */
    public static final String MSG_ERROR_ADD_ALIQUOTA_PERIOD = "_nls.aliquota.error.vigencia";
    /**
     * Mensagem de sucesso para o lock do mapa.
     */
    public static final String MSG_SUCCESS_MAPA_ALOCACAO_LOCK = "_nls.mapa_alocacao.msg.success.lock";
    /**
     * Mensagem de sucesso para o unlock do mapa.
     */
    public static final String MSG_SUCCESS_MAPA_ALOCACAO_UNLOCK = "_nls.mapa_alocacao.msg.success.unlock";
    /**
     * Mensagem de erro para o lock/unlock do mapa.
     */
    public static final String MSG_ERROR_MAPA_ALOCACAO_LOCK_UNLOCK = "_nls.mapa_alocacao.msg.error.lock_unlock";
    /**
     * Mensagem de erro para o lock/unlock do mapa.
     */
    public static final String MSG_SUCCESS_MAPA_ALOCACAO_LOCK_EDIT = "_nls.mapa_alocacao.msg.success.lock.edit";
    /**
     * Mensagem de error para quando aplicar Etiqueta (tag) � um ItemHrsFatDeal
     * inv�lido (n�o salva).
     */
    public static final String MSG_ERROR_INVAL_ITEM_HRS_FAT_DEAL_TAG = "_nls.msg.error.inval_item_hrs_fat_deal_tag";
    /**
     * Mensagem de erro para TabelaPreco ao remover uma tabela que n�o � a
     * �ltima.
     */
    public static final String MSG_ERROR_TABELA_PRECO_REMOVE = "_nls.tabela_preco.error.remove";
    /**
     * Mensagem de erro para TabelaPreco ao remover uma tabela de preco.
     */
    public static final String MSG_ERROR_TABELA_PRECO_DELETE = "_nls.tabela_preco.error.delete";
    /**
     * Mensagem de erro para GrupoCustoPeriodo ao remover um periodo que n�o � o
     * �ltimo.
     */
    public static final String MSG_ERROR_GRUPO_CUSTO_PERIODO_REMOVE = "_nls.grupo_custo_periodo.error.remove";
    /**
     * Mensagem de erro ao remover um TcePapelAlocacao que n�o � o �ltima.
     */
    public static final String MSG_ERROR_TCE_PAPEL_ALOCACAO_REMOVE = "_nls.tce_papel_alocacao.error.remove";
    /**
     * Mensagem de erro ao remover um CustoBasePapelAlocacao que n�o � o �ltimo.
     */
    public static final String MSG_ERROR_CUSTO_BASE_PAPEL_ALOCACAO_REMOVE = "_nls.custo_base_papel_alocacao.error.remove";
    public static final String MSG_ERROR_CUSTO_BASE_PAPEL_ALOCACAO_UPDATE = "_nls.custo_base_papel_alocacao.error.update";
    /**
     * Mensagem de erro ao tentar remover um TcePapelAlocacao. Somente poder�
     * ser deletado se DataInicio > closingDate do MapaAlocacao.
     */
    public static final String MSG_ERROR_TCE_PAPEL_ALOCACAO_REMOVE_CLOS_DATE = "_nls.tce_papel_alocacao.error.remove_clos_date";
    /**
     * Mensagem de erro ao tentar adicionar um ajusteReceita anterior a Closing
     * Date da Revenue.
     */
    public static final String MSG_ERROR_AJUSTE_RECEITA_ADD_CLOS_DATE = "_nls.ajuste_receita.error.add_clos_date";
    /**
     * Mensagem de erro ao tentar adicionar um ajusteReceita anterior a Closing
     * Date da Revenue.
     */
    public static final String MSG_ERROR_RECEITA_INTERNACIONAL_ADD_CLOS_DATE = "_nls.receita.msg.error.int_rev_closing_date";
    /**
     * Mensagem de erro ao tentar remover um CustoBasePapelAlocacao. Somente
     * poder� ser deletado se DataInicio > closingDate do History Lock.
     */
    public static final String MSG_ERROR_CUSTO_BASE_PAPEL_ALOCACAO_REMOVE_CLOS_DATE = "_nls.custo_base_papel_alocacao.error.remove_clos_date";
    /**
     * Mensagem de erro de receita n�o publicada ou integrada.
     */
    public static final String MSG_ERROR_RECEITA_NAO_PUBLICADA_INTEGRADA = "_nls.receita.review_ur.error.receita_nao_publicada_integrada";
    /**
     * Mensagem de erro ao tentar adicionar uma Receita internacional anterior a Closing Date da Revenue.
     */
    public static final String MSG_ERROR_RECEITA_INTERNACIONAL_ADD_CLOSING_DATE = "";
    /**
     * Mensagem de erro para valor invalido de associa��o.
     */
    public static final String APROPRIACAO_FATURA_MSG_ERROR_ASSOCIATE_VALUE = "_nls.apropriacao_fatura.msg.error.associate_value";

    /**
     * Mensagem de erro para Apropriacao fatura.
     */
    /**
     * Mensagem de erro para valor invalido de associa��o.
     */
    public static final String APROPRIACAO_FATURA_WARNING_NO_INVOICE = "_nls.apropriacao_fatura.msg.warning.no_invoice";
    /**
     * Mensagem de erro para Imposto ao remover uma tabela que n�o � a �ltima.
     */
    public static final String MSG_ERROR_ALIQUOTA_REMOVE = "_nls.aliquota.error.remove";
    /**
     * Mensagem de erro para adicionar Alioquota em Contrato Pratica.
     */
    public static final String MSG_EXIST_ALIQUOTA = "_nls.aliquota.error.exist_rate";
    /**
     * Mensagem de error para HorasFaturadasDeal invalido (mesmo Deal, mes/ano x
     * Vers�o).
     */
    public static final String MSG_ERROR_INVAL_HORAS_FATURADAS_DEAL = "_nls.msg.error.inval_horas_faturadas_deal";
    /**
     * Mensagem de error para Receita invalida (mesmo ContratoPratica, mes/ano x
     * Vers�o).
     */
    public static final String MSG_ERROR_INVAL_RECEITA = "_nls.msg.error.inval_receita";
    /**
     * Mensagem de error para ajuste na Receita.
     */
    public static final String MSG_ERROR_INVAL_RECEITA_AJUSTE = "_nls.msg.error.inval_receita_ajuste";
    public static final String MSG_ERROR_INVAL_RECEITA_PUBLISH = "_nls.msg.error.inval_receita_publish";
    /**
     * Mensagem de warning para Receita sem CotacaoMoeda relacionada.
     */
    public static final String MSG_WARNG_RECEITA_NO_EXCHANGE_RATE = "_nls.msg.warn.receita_no_exchange_rate";
    /**
     * Mensagem de error para HorasFaturadasDeal invalido (mes/ano deve ser
     * menor do que a data atual).
     */
    public static final String MSG_ERROR_INVAL_HORAS_FATURADAS_DEAL_MONTH = "_nls.msg.error.inval_horas_faturadas_deal_month";
    /**
     * Mensagem de error para Receita invalido (mes/ano deve ser menor do que a
     * data atual).
     */
    public static final String MSG_ERROR_INVAL_RECEITA_MONTH = "_nls.msg.error.inval_receita_month";
    public static final String MSG_ERROR_INVAL_CITY_BASE_DEAL_FISCAL = "_nls.msg.error.inval_receita_deal_fiscal_city_base";
    /**
     * Mensagem de sucesso para ValidacaoPessoa.
     */
    public static final String VALIDATE_PERSON_SUCCESS_VALIDATE = "_nls.validacao_pessoa.msg.success.validate";
    /**
     * Mensagem de sucesso para ValidacaoPessoa.
     */
    public static final String VALIDATE_PERSON_SUCCESS_ALL_RESOURCES_VALIDATED = "_nls.validacao_pessoa.msg.success.all_resources_validated";
    /**
     * Mensagem de warng para ValidacaoPessoa.
     */
    public static final String VALIDATE_PERSON_WARNG_NO_VALIDATED_RESOURCES = "_nls.validacao_pessoa.msg.warng.no_validated_resources";
    /**
     * Mensagem de erro para ValidacaoPessoa.
     */
    public static final String VALIDATE_PERSON_ERROR_VALIDATE = "_nls.validacao_pessoa.msg.error.validate";
    /**
     * Mensagem de erro para ValidacaoPessoa - sem associacao de GrupoCusto.
     */
    public static final String VALIDATE_PERSON_ERROR_VALIDATE_COST_GROUP = "_nls.validacao_pessoa.msg.error.validate_grupo_custo";
    /**
     * Mensagem de erro para ValidacaoPessoa - sem custo TCE cadastrado.
     */
    public static final String VALIDATE_PERSON_ERROR_VALIDATE_TCE_COST = "_nls.validacao_pessoa.msg.error.validate_custo_tce";
    /**
     * Mensagem de erro para ValidacaoPessoa - valida somente se mes anterior
     * estiver validado.
     */
    public static final String VALIDATE_PERSON_ERROR_NO_PREV_VALIDATED_MONTH = "_nls.validacao_pessoa.msg.error.no_prev_validated_month";
    /**
     * Mensagem de erro para ValidacaoPessoa - remove validacao somente se for o
     * ultimo mes.
     */
    public static final String VALIDATE_PERSON_ERROR_REMOVE_NO_LAST_VALID_MONTH = "_nls.validacao_pessoa.msg.error.remove_no_last_valid_month";
    /**
     * Mensagem de erro para ValidacaoPessoa - re-validacao somente se for o
     * ultimo mes ou proximo.
     */
    public static final String VALIDATE_PERSON_ERROR_NO_LAST_VALID_MONTH = "_nls.validacao_pessoa.msg.error.no_last_valid_month";
    /**
     * Mensagem de warning para Auto Complete Pessoa quando o total alocado
     * (allocated) est� igual o total alocavel (avaiable).
     */
    public static final String MSG_WARNG_AUTO_COMPLETE_PESSOA_TOTAL_OK = "_nls.msg.warn.auto_complete_pessoa.total_ok";
    /**
     * Mensagem de warning para Auto Complete Pessoa quando n�o existe Alocacao
     * no m�s anterior ao ClosingDate.
     */
    public static final String MSG_WARNG_AUTO_COMPLETE_PESSOA_NO_RESULT_PREV_MONTH = "_nls.msg.warn.auto_complete_pessoa.no_result_prev_month";
    /**
     * Msg de erro para PessoaBancoHoras j� existente - mesma Pessoa, data e
     * valorFator.
     */
    public static final String MSG_ERROR_PESS_BCO_HRS_ALREADY_EXISTS = "_nls.msg.error.pess_bco_hrs.already_exists";
    /**
     * Msg de erro para RegistroAtividade j� existente - mesmo ContratoPratica,
     * Pessoa, Atividade e data.
     */
    public static final String MSG_ERROR_REGISTRO_ATIVIDADE_ALREADY_EXISTS = "_nls.msg.error.registro_atividade.already_exists";
    /**
     * Msg de erro para CLob e GrupoCusto nulos.
     */
    public static final String MSG_ERROR_REGISTRO_ATIVIDADE_CONTRATO_PRATICA_AND_GRUPO_CUSTO_NULL = "_nls.msg.error.registro_atividade.contrato_pratica_and_grupo_custo_null";
    /**
     * Mensagem de erro para Apropriacao Plantao e Horas Extras - sem associacao
     * de PessoaSalario.
     */
    public static final String REGISTER_PL_HE_ERROR_VALIDATE_RESOURCE_SALARY = "_nls.custo_pessoa_mes.msg.error.validate_resource_salary";
    /**
     * Mensagem de erro para Apropriacao Plantao e Horas Extras - sem associacao
     * de PessoaGrupoCusto.
     */
    public static final String REGISTER_PL_HE_ERROR_VALIDATE_RESOURCE_COST_GROUP = "_nls.custo_pessoa_mes.msg.error.validate_resource_cost_group";
    /**
     * Mensagem de erro para Apropriacao Plantao e Horas Extras - sem associacao
     * de PessoaTipoContrato.
     */
    public static final String REGISTER_PL_HE_ERROR_VALIDATE_RESOURCE_CONTRACT_TYPE = "_nls.custo_pessoa_mes.msg.error.validate_resource_contract_type";
    /**
     * Mensagem da valida��o do History Lock.
     */
    public static final String MSG_ERROR_MODULO_VERIFY_HISTORY_LOCK = "_nls.msg.error.modulo.verify_history_lock";
    public static final String MSG_ERROR_MODULO_VERIFY_HISTORY_LOCK_CURRENT_MONTH = "_nls.msg.error.modulo.verify_history_lock.current_month";
    public static final String MSG_ERROR_MODULO_VERIFY_HISTORY_LOCK_REMOVE_BUDGET_AREA = "_nls.msg.error.modulo.verify_history_lock_remove_budget_area";
    /**
     * Mensagem da valida��o do Chargeback.
     */
    public static final String MSG_ERROR_MODULO_CHARGEBACK = "_nls.msg.error.modulo.chargeback";
    /**
     * Mensagem da valida��o do Chargeback.
     */
    public static final String MSG_ERROR_APPROVED_CHARGEBACK = "_nls.msg.error.modulo.chargeback";
    /**
     * Mensagem de erro para atualizacao de M�dia Cota��o Moeda
     */
    public static final String MSG_ERROR_MEDIA_COTACAO_MOEDA_UPDATE = "_nls.media_cotacao_moeda.error.update";
    /**
     * Mensagem de erro para cria��o de M�dia Cota��o Moeda
     */
    public static final String MSG_ERROR_MEDIA_COTACAO_MOEDA_ADD = "_nls.media_cotacao_moeda.error.add";
    /**
     * Mensagem de erro para cria��o de M�dia Cota��o Moeda j� existente
     */
    public static final String MSG_ERROR_MEDIA_COTACAO_MOEDA_ALREADY_EXISTS = "_nls.media_cotacao_moeda.error.already_exists";
    public static final String MSG_ERROR_PESSOA_BILLABILTY_PERIOD_EXISTS = "_nls.pessoa.pessoa_billability.validation.period_exists";
    public static final String MSG_ERROR_PESSOA_BILLABILTY_EXISTS = "_nls.pessoa.pessoa_billability.validation.billability_exists";
    /**
     * MediaCotacaoMoeda.
     */
    public static final String ENTITY_NAME_MEDIA_COTACAO_MOEDA = "_nls.media_cotacao_moeda.entity_name";

    /** Nomes das Entidades. */
    /**
     * Pessoa.
     */
    public static final String ENTITY_NAME_PESSOA = "_nls.pessoa.entity_name";
    /**
     * Ajuste Receita.
     */
    public static final String ENTITY_NAME_AJUSTE_RECEITA = "_nls.ajuste_receita.entity_name";
    /**
     * Entidade Perfil Sistema.
     */
    public static final String ENTITY_NAME_PERFIL_SISTEMA = "_nls.perfil_sistema.entity_name";
    /**
     * Entidade Perfil Sistema.
     */
    public static final String ENTITY_NAME_AREA_ORCAMENTARIA = "_nls.menu.budget_area";
    public static final String ENTITY_NAME_EMPRESA_ORCAMENTARIA = "_nls.menu.budget_empresa";
    /**
     * ComissaoAcelerador.
     */
    public static final String ENTITY_NAME_COMISSAO_ACELERADOR = "_nls.comissao_acelerador.entity_name";
    /**
     * ComissaoFatura.
     */
    public static final String ENTITY_NAME_COMISSAO_FATURA = "_nls.comissao_fatura.entity_name";
    /**
     * Comissao.
     */
    public static final String ENTITY_NAME_COMISSAO = "_nls.comissao.entity_name";
    /**
     * GrupoCusto.
     */
    public static final String ENTITY_NAME_GRUPO_CUSTO = "_nls.grupo_custo.entity_name";
    /**
     * CidadeBase.
     */
    public static final String ENTITY_NAME_CIDADE_BASE = "_nls.cidade_base.entity_name";

    public static final String ENTITY_NAME_CIDADE_BASE_FILIAL = "_nls.cidade_base_filial.entity_name";

    /**
     * PessoaCidadeBase.
     */
    public static final String ENTITY_NAME_PESSOA_CIDADE_BASE = "_nls.pessoa_cidade_base.entity_name";
    /**
     * TipoContrato.
     */
    public static final String ENTITY_NAME_TIPO_CONTRATO = "_nls.tipo_contrato.entity_name";
    /**
     * TipoContrato.
     */
    public static final String ENTITY_NAME_BILLABILITY = "_nls.pessoa_billability.entity_name";
    /**
     * TipoContrato.
     */
    public static final String ENTITY_NAME_TIPO_CONTRATO_ENCARGO = "_nls.encargo.entity_name";
    /**
     * PessoaSalario.
     */
    public static final String ENTITY_NAME_PESSOA_SALARIO = "_nls.pessoa_salario.entity_name";
    /**
     * GrupoCustoPeriodo.
     */
    public static final String ENTITY_NAME_GRUPO_CUSTO_PERIODO = "_nls.grupo_custo_periodo.entity_name";
    /**
     * Fator Reajuste.
     */
    public static final String ENTITY_NAME_FATOR_REAJUSTE = "_nls.fator_reajuste.entity_name";
    /**
     * Hedge.
     */
    public static final String ENTITY_NAME_HEDGE = "_nls.hedge.entity_name";
    /**
     * NaturezaCentroLucro.
     */
    public static final String ENTITY_NAME_NATUREZA_CENTRO_LUCRO = "_nls.natureza_centro_lucro.entity_name";
    /**
     * CentroLucro.
     */
    public static final String ENTITY_NAME_CENTRO_LUCRO = "_nls.centro_lucro.entity_name";
    /**
     * Cliente.
     */
    public static final String ENTITY_NAME_CLIENTE = "_nls.cliente.entity_name";
    /**
     * Empresa.
     */
    public static final String ENTITY_NAME_EMPRESA = "_nls.empresa.entity_name";

    /**
     * BMDN.
     */
    public static final String ENTITY_NAME_BMDN = "_nls.bmdn.entity_name";
    public static final String BMDN_ERROR_ASSOCIATED_MSAS = "_nls.bmdn.msg.error.associated_msas";

    /**
     * ClientePai.
     */
    public static final String ENTITY_NAME_CLIENTE_PAI = "_nls.cliente_pai.entity_name";
    /**
     * MapaAlocacao.
     */
    public static final String ENTITY_NAME_MAPA_ALOCACAO = "_nls.mapa_alocacao.entity_name";
    /**
     * Pratica.
     */
    public static final String ENTITY_NAME_PRATICA = "_nls.pratica.entity_name";
    /**
     * MSA.
     */
    public static final String ENTITY_NAME_MSA = "_nls.msa.entity_name";
    public static final String ENTITY_NAME_MSA_SETTINGS = "_nls.msa.configuracao.entity_name";
    /**
     * ContratoPratica.
     */
    public static final String ENTITY_NAME_CONTRATO_PRATICA = "_nls.contrato_pratica.entity_name";

    /**
     * ContratoPratica.
     */
    public static final String CONTRATO_PRATICA_SAME_NAME = "_nls.contrato_pratica.same_name";

    /**
     * PerfilVendido.
     */
    public static final String ENTITY_NAME_PERFIL_VENDIDO = "_nls.perfil_vendido.entity_name";
    /**
     * TabelaPreco.
     */
    public static final String ENTITY_NAME_TABELA_PRECO = "_nls.tabela_preco.entity_name";
    /**
     * ItemTabelaPreco.
     */
    public static final String ENTITY_NAME_ITEM_TABELA_PRECO = "_nls.item_tabela_preco.entity_name";
    /**
     * E-mail notificacao
     */
    public static final String EMAIL_SUBJECT_PRICE_TABLE_STATUS = "_nls.email.subject.price_table_status";
    public static final String EMAIL_MSG_PRICE_TABLE_STATUS = "_nls.email.msg.price_table_status";
    public static final String CPS_EMAILS_PARAMETER = "CPS_EMAILS";
    /**
     * Deal.
     */
    public static final String ENTITY_NAME_DEAL = "_nls.deal.entity_name";
    /**
     * Deal.
     */
    public static final String ENTITY_NAME_INTERCOMPANY = "_nls.deal.nome_emp_intercomp.label";
    /**
     * Projeto.
     */
    public static final String ENTITY_NAME_PROJETO = "_nls.projeto.entity_name";
    /**
     * HorasFaturadasDeal.
     */
    public static final String ENTITY_NAME_HORAS_FATURADAS_DEAL = "_nls.horas_faturadas_deal.entity_name";
    /**
     * Receita.
     */
    public static final String ENTITY_NAME_RECEITA = "_nls.receita.entity_name";
    /**
     * ContratoPraticaCentroLucro.
     */
    public static final String ENTITY_NAME_CONTRATO_PRATICA_CL = "_nls.cpcl.entity_name";
    /**
     * CostCenterHierarchy.
     */
    public static final String ENTITY_NAME_COST_CENTER_HIERARCHY = "_nls.menu.cost_center_hierarchy";

    public static final String MSG_COST_CENTER_HIERARCHY_ORACLE_CODE_IS_INVALID = "_nls.msg.error.cost_center_hierarchy.invalid_oracle_code";

    public static final String MSG_COST_CENTER_HIERARCHY_ALREADY_EXISTS_WITH_NAME_ORACLE_CODE =
            "_nls.msg.error.cost_center_hierarchy.already_exists";
    /**
     * E-mail notificacao ContratoPraticaCentroLucro
     */
    public static final String COST_CENTER_INTEGRATE_ERROR = "_nls.email.subject.cost_center_integrate_error";
    public static final String EMAIL_SUBJECT_COST_CENTER_INTEGRATE_ERROR = "_nls.email.subject.cost_center_integrate_error";
    public static final String EMAIL_MESSAGE_COST_CENTER_INTEGRATE_ERROR = "_nls.email.message.cost_center_integrate_error";
    public static final String CONTRACT_LOB_INTEGRATE_ERROR = "_nls.email.subject.contract_lob_integrate_error";
    public static final String EMAIL_SUBJECT_CONTRACT_LOB_INTEGRATE_ERROR = "_nls.email.subject.contract_lob_integrate_error";
    public static final String EMAIL_MESSAGE_CONTRACT_LOB_INTEGRATE_ERROR = "_nls.email.message.contract_lob_integrate_error";
    public static final String SUPPORT_EMAILS_PARAMETER = "SUPPORT_EMAILS";

    public static final String EMAIL_SUBJECT_COST_CENTER_INTEGRATE_SUCCESS = "_nls.email.subject.cost_center_integrate_success";
    public static final String EMAIL_MESSAGE_COST_CENTER_INTEGRATE_SUCCESS = "_nls.email.message.cost_center_integrate_success";


    public static final String LIST_COST_CENTER_LOAD_INTEGRATE_ERROR = "_nls.list.cost_center_load_integrate_error";
    public static final String LIST_PROJECT_LOAD_INTEGRATE_ERROR = "_nls.list.project_load_integrate_error";

    public static final String COST_CENTER_HIERARCHY_WARN_INACTIVE_SELECTED = "_nls.cost_center_hierarchy.status.warn_inactive_selected";

    /**
     * Alocacao.
     */
    public static final String ENTITY_NAME_ALOCACAO = "_nls.alocacao.entity_name";
    /**
     * Etiqueta.
     */
    public static final String ENTITY_NAME_ETIQUETA = "_nls.etiqueta.entity_name";
    /**
     * Fatura.
     */
    public static final String ENTITY_NAME_FATURA = "_nls.fatura.entity_name";
    /**
     * Cotacao Moeda.
     */
    public static final String ENTITY_NAME_COTACAO_MOEDA = "_nls.cotacao_moeda.entity_name";
    /**
     * ItemFatura.
     */
    public static final String ENTITY_NAME_ITEM_FATURA = "_nls.item_fatura.entity_name";
    /**
     * Modulo.
     */
    public static final String ENTITY_NAME_MODULO = "_nls.modulo.entity_name";
    /**
     * Moeda.
     */
    public static final String ENTITY_NAME_MOEDA = "_nls.moeda.entity_name";
    /**
     * ItemHrsFatDeal.
     */
    public static final String ENTITY_NAME_ITEM_HRS_FAT_DEAL = "_nls.item_hrs_fat_deal.entity_name";
    /**
     * ItemHrsFatDeal.
     */
    public static final String ENTITY_NAME_ITEM_RECEITA = "_nls.item_receita.entity_name";
    /**
     * PapelAlocacao.
     */
    public static final String ENTITY_NAME_PAPEL_ALOCACAO = "_nls.papel_alocacao.entity_name";
    /**
     * BasePapelAlocacao.
     */
    public static final String ENTITY_NAME_BASE_PAPEL_ALOCACAO = "_nls.base_papel_alocacao.entity_name";
    /**
     * CustoBasePapelAlocacao.
     */
    public static final String ENTITY_NAME_CUSTO_BASE_PAPEL_ALOCACAO = "_nls.custo_base_papel_alocacao.entity_name";
    /**
     * TcePapelAlocacao.
     */
    public static final String ENTITY_NAME_TCE_PAPEL_ALOCACAO = "_nls.tce_papel_alocacao.entity_name";
    /**
     * IntegFaturaParametro.
     */
    public static final String ENTITY_NAME_INTEG_FATURA_PARAMETRO = "_nls.integ_fatura_parametro.entity_name";
    /**
     * IntegFaturaParametro.
     */
    public static final String ENTITY_NAME_MAPA_DESPESA = "_nls.mapa_despesa.entity_name";
    /**
     * IntegFaturaParametro.
     */
    public static final String EMPRESA_DESPESA_REQUIRED = "_nls.error.mapa_despesa.empresa.required";
    /**
     * IntegFaturaParametro.
     */
    public static final String ENTITY_NAME_CUSTO_INFRA_BASE = "_nls.custo_infra_base.entity_name";
    /**
     * DreLogFechamento.
     */
    public static final String ENTITY_NAME_DRE_LOG_FECHAMENTO = "_nls.dre_log_fechamento.entity_name";
    /**
     * TiRecurso.
     */
    public static final String ENTITY_NAME_TI_RECURSO = "_nls.ti_recurso.entity_name";
    /**
     * Entidade CustoTiRecurso.
     */
    public static final String ENTITY_NAME_CUSTO_TI_RECURSO = "_nls.custo_ti_recurso.entity_name";
    /**
     * AlocacaoOverhead.
     */
    public static final String ENTITY_NAME_ALOCACAO_OVERHEAD = "_nls.alocacao_overhead.entity_name";
    /**
     * PessoaBancoHoras.
     */
    public static final String ENTITY_NAME_PESS_BCO_HRS = "_nls.pessoa_banco_horas.entity_name";
    /**
     * RegistroAtividade.
     */
    public static final String ENTITY_NAME_REGISTRO_ATIVIDADE = "_nls.registro_atividade.entity_name";
    /**
     * Apropriacao Plantao.
     */
    public static final String ENTITY_NAME_APROPRIACAO_PANTAO = "_nls.apropriacao_plantao.entity_name";
    /**
     * Apropriacao Hora Extra.
     */
    public static final String ENTITY_NAME_APROPRIACAO_HORA_EXTRA = "_nls.apropriacao_hora_extra.entity_name";
    /**
     * RegistroAtividade.
     */
    public static final String ENTITY_NAME_DESPESA = "_nls.despesa.entity_name";
    /**
     * ComposicaoTce.
     */
    public static final String ENTITY_NAME_COMPOSICAO_TCE = "_nls.composicao_tce.entity_name";
    /**
     * My Profile.
     */
    public static final String MY_PROFILE = "_nls.my_profile";
    /**
     * ComposicaoTce.
     */
    public static final String ENTITY_NAME_IT_CHARGEBACK = "_nls.chargeback.entity_name";
    public static final String ENTITY_NAME_LICENSE = "_nls.license.entity_name";
    /**
     * Receita Resultado.
     */
    public static final String ENTITY_NAME_RECEITA_RESULTADO = "_nls.receita_resultado.entity_name";

    /**
     * Industry Tyoe.
     */
    public static final String ENTITY_INDUSTRY_TYPE = "_nls.industry.type.entity_name";

    /**
     * Vigencia.
     */
    public static final String DEFAULT_LABEL_VALIDITY_DATE_MAPA_ALOCACAO = "_nls.mapa_alocacao.vigencia.label";

    /** Labels. */
    /**
     * Label do redistribution value - HorasFaturadasDeal.
     */
    public static final String ITEM_HRS_FAT_DEAL_REDISTRIBUTION_LABEL = "_nls.item_hrs_fat_deal.redistribution_value.label";
    /**
     * Label do redistribution value - Receita.
     */
    public static final String ITEM_RECEITA_REDISTRIBUTION_LABEL = "_nls.item_receita.redistribution_value.label";
    /**
     * Label do redistribution value - Receita.
     */
    public static final String ITEM_RECEITA_REDISTRIBUTION_VALUE_BIGGER_THAN_TOTAL_REVENUE_VALUE = "_nls.item_receita.redistribution_value_bigger_than_total_revenue_value";
    /**
     * Label do update U.R.
     */
    public static final String LABEL_ALOCACAO_UPDATE_UR = "_nls.alocacao.update_ur.label";
    /**
     * Label do update FTE.
     */
    public static final String LABEL_ALOCACAO_UPDATE_FTE = "_nls.alocacao.update_fte.label";
    /**
     * Label do numero FTE.
     */
    public static final String LABEL_RECEITA_NUMERO_FTE = "_nls.item_receita.numero_fte.label";
    /**
     * Label do numero Horas.
     */
    public static final String LABEL_RECEITA_NUMERO_HOURS = "_nls.item_receita.numero_horas.label";
    /**
     * Label do update FTE.
     */
    public static final String LABEL_HRS_FAT_DEAL_UPDATE_HOURS = "_nls.item_hrs_fat_deal.numero_horas.label";
    /**
     * Label do periodo GrupoCustoPeriodo.
     */
    public static final String LABEL_GRUPO_CUSTO_PERIODO_PERIOD = "_nls.grupo_custo_periodo.vigencia.label";
    /**
     * UnSubscribe.
     */
    public static final String LABEL_UNSUBSCRIBE = "_nls.unsubscribe.label";
    /**
     * Click Here.
     */
    public static final String LABEL_CLICK_HERE = "_nls.click_here.label";
    /**
     * to Unfollow the C-Lob.
     */
    public static final String LABEL_TO_UNFOLLOW_CLOB = "_nls.mapa_foto.follow.unsubscribe.to_unfollow_clob.label";
    /**
     * to Unfollow the Person.
     */
    public static final String LABEL_TO_UNFOLLOW_PERSON = "_nls.mapa_foto.follow.unsubscribe.to_unfollow_person.label";
    /**
     * to Unfollow all my managed.
     */
    public static final String LABEL_TO_UNFOLLOW_ALL_YOU_MANAGE = "_nls.mapa_foto.follow.unsubscribe.to_unfollow_all_you_manage.label";
    /**
     * All my managed.
     */
    public static final String LABEL_ALL_YOU_MANAGE = "_nls.pessoa.people_follow.all_you_manage.label";
    /**
     * TCE Synchronization.
     */
    public static final String LABEL_TCE_SYNCHRONIZATION = "_nls.composicao_tce.tce_synchronization.label";
    /**
     * ChargebackPessoa Synchronization.
     */
    public static final String LABEL_CHBACK_PESS_SYNC = "_nls.chargeback.chback_pess_sync.label";
    /**
     * Propriedade da aplica��o. Utilizado para identificar o nome jndi. �
     * criado a constate para usar em contextos est�ticos (static) pois nos jobs
     * n�o funciona a inst�ncia do arquivo Properties.
     */
    public static final String APPLICATION_JNDI_NAME = "jdbc/pmsDS";

    /** Propriedades/configuracoes diversas. */
    /**
     * Ano inicial para combobox.
     */
    public static final String DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN = "research.combobox.year.begin";
    /**
     * Range para combobox de ano da vigencia.
     */
    public static final String DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE = "research.combobox.year.range";
    /**
     * Range para combobox da tela Average Annual Rate.
     */
    public static final String MEDIA_COTACAO_MOEDA_PROPERTY_COMBOBOX_YEAR_RANGE = "media_cotacao_moeda.combobox.year.range";
    /**
     * Step para percentual de alocacao.
     */
    public static final String DEFAULT_PROPERTY_ALLOCATION_PERIOD_STEP = "alocacao_periodo.step";
    /**
     * Tamanho da String das Etiqueta (tags).
     */
    public static final String DEFAULT_PROPERTY_ETIQUETA_LENGTH = "etiqueta.length";
    /**
     * Padr�o para exibi��o de valores de moeda. Concatenar sigla da moeda.
     * Exemplo: Constants.DEFAULT_PROPERTY_PATTERN_CURRENCY + Moeda.siglaMoeda.
     */
    public static final String DEFAULT_PROPERTY_PATTERN_CURRENCY = "pattern.currency.";
    /**
     * Padr�o para exibi��o de valores de moeda - Brasil.
     */
    public static final String DEFAULT_PROPERTY_PATTERN_CURRENCY_BRL = "pattern.currency.brl";
    /**
     * Codigo da Moeda padr�o.
     */
    public static final String DEFAULT_PROPERTY_CURRENCY_CODE = "default.currency.code";
    /**
     * Codigo de Reembolso de Despesas no TipoServico.
     */
    public static final String EXPENSE_REIMBURSEMENT_CODE = "tipo.servico.reembolso.despesa";
    /**
     * Codigo da Moeda Real (Brasil).
     */
    public static final String PROPERTY_CURRENCY_BRL_CODE = "currency.brl.code";
    /**
     * Quantidade padr�o de horas que representa 1 m�s.
     */
    public static final String DEFAULT_PROPERTY_NUMBER_HOURS_MONTH = "number.hours.month";
    /**
     * Escala padr�o.
     */
    public static final String DEFAULT_NUMBER_SCALE = "number.scale.value";
    /**
     * Sigla padr�o para o nome do mapa de aloca��o.
     */
    public static final String MAPA_ALOCACAO_DEFAULT_AMOUNT_VIEW_CLOSING_DATE = "mapa_alocacao.qtd.view.meses_fechados";
    /**
     * Sigla padr�o para o nome do mapa de aloca��o.
     */
    public static final String MAPA_ALOCACAO_DEFAULT_ACRONYM_NAME = "mapa_alocacao.name.acronym";
    /**
     * Quantidade de meses exibidos no mapa de alocacao.
     */
    public static final String MAPA_ALOCACAO_DEFAULT_WINDOW_SIZE = "mapa_alocacao.tamanho.janela.meses";
    /**
     * Quantidade de meses do range para copia (snapshots).
     */
    public static final String MAPA_ALOCACAO_RANGE_MONTHS_SNAPSHOTS = "mapa_alocacao.range.months.snapshots";
    /**
     * Sigla padr�o para o nome do deal fiscal.
     */
    public static final String DEAL_FISCAL_DEFAULT_ACRONYM_NAME = "deal_fiscal.name.acronym";
    /**
     * C�digo do Modulo do ChargeBack.
     */
    public static final String MODULO_CHARGEBACK_CODE = "modulo.chargeback.code";
    /**
     * C�digo do Modulo de Horas Extras e Plant�o.
     */
    public static final String MODULO_LEAVES_OVERTIME_CODE = "modulo.leaves_overtime.code";
    /**
     * C�digo do Modulo do MapaAlocacao.
     */
    public static final String MODULO_ALOCATION_MAP_CODE = "modulo.alocation_map.code";
    /**
     * C�digo do Modulo History Lock.
     */
    public static final String MODULO_HISTORY_LOCK_CODE = "modulo.history_lock.code";
    /**
     * Nome do Modulo History Lock.
     */
    public static final String MODULO_HISTORY_LOCK_NAME = "modulo.history_lock.name";
    /**
     * Nome do Modulo Expense.
     */
    public static final String MODULO_EXPENSE_NAME = "modulo.expense.name";
    /**
     * C�digo do Modulo Expense.
     */
    public static final String MODULO_EXPENSE_CODE = "modulo.expense.code";
    /**
     * Nome do Modulo do MapaAlocacao.
     */
    public static final String MODULO_ALOCATION_MAP_NAME = "modulo.alocation_map.name";
    /**
     * Nome do Modulo da Receita.
     */
    public static final String MODULO_RECEITA_NAME = "modulo.revenue.name";
    /**
     * C�digo do Modulo da Receita.
     */
    public static final String MODULO_RECEITA_CODE = "modulo.revenue.code";
    /**
     * Nome do Modulo da Receita.
     */
    public static final String MODULO_VALIDACAO_PESSOA_NAME = "modulo.people_validation.name";
    /**
     * Nome do Modulo da Fatura.
     */
    public static final String MODULO_FATURA_NAME = "modulo.invoice.name";
    /**
     * C�digo do Modulo da Receita.
     */
    public static final String MODULO_FATURA_CODE = "modulo.invoice.code";
    /**
     * C�digo do Modulo da International Revenue.
     */
    public static final String MODULO_EXCHANGE_CODE = "modulo.exchange.code";
    /**
     * Nome do Modulo da International Revenue.
     */
    public static final String MODULO_INT_REVENUE_NAME = "modulo.international.revenue.name";
    /**
     * C�digo do Modulo da Receita.
     */
    public static final String MODULO_INT_REVENUE_CODE = "modulo.international.revenue.code";
    /**
     * Configura��o da unidade da despesa.
     */
    public static final String DESPESA_UNIDADE_TIPO_DESP = "tipo.despesa.unidade.id_";
    /**
     * Configura��o default unidade da despesa.
     */
    public static final String DESPESA_UNIDADE_DEFAULT = "tipo.despesa.default.unidade";
    /**
     * Nao remover position.
     */
    public static final String CAN_NOT_REMOVED_POSITION = "_nls.cargo.msgm.remove";
    /**
     * Chave para o bundle de valor min. menor que o permitido.
     */
    public static final String BUNDLE_VALUE_IS_LESS_THAN_ALLOWABLE_MINIMUM = "_nls.msg.value_is_less_than_allowable_minimum";
    public static final String BUNDLE_IS_CLOB_NOT_REIMBURSABLE = "_nls.msg.value_is_clob_not_reimbursable";
    public static final String BUNDLE_IS_CLOB_REIMBURSABLE_MISSING = "_nls.msg.value_is_clob_reimbursable_missing";
    /**
     * Chave da propriedade de sistema setado em produ��o. Utilizado para
     * identificar o servidor
     */
    public static final String SERVER_ENVIRONMENT_KEY = "server.environment";
    /**
     * Propriedade de sistema setado em produ��o. Utilizado para identificar o
     * servidor
     */
    public static final String SERVER_ENVIRONMENT_PROD = "production";
    /**
     * Tipo de permiss�o Business Manager.
     */
    public static final String AUTHORITY_TYPE_BM = "PMS_BUSINESS_MANAGER";
    /**
     * Tipo de permiss�o AE.
     */
    public static final String AUTHORITY_TYPE_AE = "PMS_AE";
    /**
     * Endere�o de email do DP.
     */
    public static final String EMAIL_ADDRESS_DP_KEY = "mail.address.dp.to";
    /**
     * Endere�o de email do Comercial.
     */
    public static final String EMAIL_ADDRESS_ADM_COMERCIAL_KEY = "mail.address.adm_comercial.to";
    /**
     * Endere�o de email de destino de testes.
     */
    public static final String EMAIL_ADDRESS_TEST_KEY = "mail.sender.to_test";
    /**
     * Endere�o de email de destino de erros.
     */
    public static final String EMAIL_ADDRESS_ERROR_KEY = "mail.sender.to_error";
    /**
     * Link do Jira Web.
     */
    public static final String JIRA_WEB_LINK = "jira_web.link";
    /**
     * Host environment em produ��o.
     */
    public static final String HOST_ENVIRONMENT = "host.environment";
    /**
     * Url UnSubscribe Follow.
     */
    public static final String URL_FOLLOW_UNSUBSCRIBE = "/pages/public/mapaAlocFollowUnSubscribe.jsf?";
    /**
     * Tipo de Follow - C-Lob.
     */
    public static final String FOLLOW_TYPE_CLOB = "FLWCLOB";
    /**
     * Tipo de Follow - People.
     */
    public static final String FOLLOW_TYPE_PEOPLE = "FLWPEOP";
    /**
     * Dia inicial do range CHP.
     */
    public static final String CHP_RANGE_INITIAL_DAY = "chp.range.initial.day";
    /**
     * Papel de admin.
     */
    public static final String ROLE_ADMIN = "ROLE_PMS_ADMIN";

    /** ROLES. */
    /**
     * Papel de pd.
     */
    public static final String ROLE_PD = "ROLE_PMS_PD";
    /**
     * Papel de people manager.
     */
    public static final String ROLE_PEOPLE_MANAGER = "ROLE_PMS_PEOPLE_MANAGER";
    /**
     * Papel de contract.
     */
    public static final String ROLE_CONTRACT = "ROLE_PMS_CONTRACT";
    /**
     * Papel de Financial.
     */
    public static final String ROLE_FINANCIAL = "ROLE_PMS_FINANCIAL";
    /**
     * Papel de Hoshin Office.
     */
    public static final String ROLE_HOSHIN_OFFICE = "ROLE_PMS_HOSHIN_OFFICE";
    /**
     * Status geral da fatura Parcial.
     */
    public static final String FATURA_STATUS_PARTLYPAG = "Partly";

    /** Papel de Controladoria. */
    public static final String ROLE_PMS_CONTROLLERSHIP = "ROLE_PMS_CONTROLLERSHIP";

    /** STATUS FATURA. */

    /** Status geral da fatura Pago. */
    public static final String FATURA_STATUS_PAID = "Paid";

    /** Status geral da fatura n�o pago. */
    public static final String FATURA_STATUS_NOTPAID = "Not paid";

    /** ANEXO TABELA PRECO. */

    /** Arquivo vazio. */
    public static final String ANEXO_EMPTY = "_nls.anexo.msgm.vazio";


    /** ANEXO TABELA PRECO. */
    /**
     * Warning de nome ja existente.
     */
    public static final String TRAVEL_BUDGET_ALREADY_EXISTS = "_nls.travel.budget.warn.existe";

    /** TRAVEL BUDGET */
    /**
     * Mensagem de desativado com sucesso.
     */
    public static final String TRAVEL_BUDGET_DESATIVADO_SUCESSO = "_nls.travel.budget.msgm.desativado_sucesso";
    /**
     * Mensagem de nome ja existe na allowlist.
     */
    public static final String ALLOW_LIST_ALREADY_EXISTS = "_nls.travel.budget.warn.name.existe";
    /**
     * Email com mensagem para cria��o de TravelBudget de Cliente.
     */
    public static final String EMAIL_MSG_CREATE_TRAVEL_BUDGET_CL = "_nls.travel.budget.cl.email.msgm.create";
    /**
     * Email com assunto para cria��o de TravelBudget de Cliente.
     */
    public static final String EMAIL_ASSUNTO_CREATE_TRAVEL_BUDGET_CL = "_nls.travel.budget.cl.email.assunto.create";
    /**
     * Email com assunto para desabilitar travel budget de Cliente.
     */
    public static final String EMAIL_ASSUNTO_DISABLE_TRAVEL_BUDGET_CL = "_nls.travel.budget.cl.email.assunto.disable";
    /**
     * Email com mensagem para desabilitar travel budget de Cliente.
     */
    public static final String EMAIL_MSG_DISABLE_TRAVEL_BUDGET_CL = "_nls.travel.budget.cl.email.msgm.disable";
    /**
     * Email com assunto para delete travel budget de Cliente.
     */
    public static final String EMAIL_ASSUNTO_DELETE_TRAVEL_BUDGET_CL = "_nls.travel.budget.cl.email.assunto.delete";
    /**
     * Email com mensagem para delete travel budget de Cliente.
     */
    public static final String EMAIL_MSG_DELETE_TRAVEL_BUDGET_CL = "_nls.travel.budget.cl.email.msgm.delete";
    /**
     * Email com mensagem para cria��o de TravelBudget de Grupo de Custo.
     */
    public static final String EMAIL_MSG_CREATE_TRAVEL_BUDGET_GC = "_nls.travel.budget.gc.email.msgm.create";
    /**
     * Email com assunto para cria��o de TravelBudget de Grupo de Custo.
     */
    public static final String EMAIL_ASSUNTO_CREATE_TRAVEL_BUDGET_GC = "_nls.travel.budget.gc.email.assunto.create";
    /**
     * Email com assunto para desabilitar travel budget de Grupo de Custo.
     */
    public static final String EMAIL_ASSUNTO_DISABLE_TRAVEL_BUDGET_GC = "_nls.travel.budget.gc.email.assunto.disable";
    /**
     * Email com mensagem para desabilitar travel budget de Grupo de Custo.
     */
    public static final String EMAIL_MSG_DISABLE_TRAVEL_BUDGET_GC = "_nls.travel.budget.gc.email.msgm.disable";
    /**
     * Email com assunto para delete travel budget de Grupo de Custo.
     */
    public static final String EMAIL_ASSUNTO_DELETE_TRAVEL_BUDGET_GC = "_nls.travel.budget.gc.email.assunto.delete";
    /**
     * Email com mensagem para delete travel budget de Grupo de Custo.
     */
    public static final String EMAIL_MSG_DELETE_TRAVEL_BUDGET_GC = "_nls.travel.budget.gc.email.msgm.delete";
    /**
     * Travel Budget Reembolsavel pelo cliente.
     */
    public static final String ORCAMENTO_DESPESA_REEMBOLSAVEL = "RB";
    /**
     * Travel Budget nao Reembolsavel pelo cliente.
     */
    public static final String ORCAMENTO_DESPESA_NAO_REEMBOLSAVEL = "NR";
    /**
     * Label tipo de fatura
     */
    public static final String ORCAMENTO_DESPESA_TIPO_FATURA = "_nls.travel.budget.label.tipo_fatura";
    public static final String ORCAMENT_DESPESA_CLOBS = "_nls.travel.budget.label.clobs";
    public static final String ORCAMENTO_DESPESA_DATE = "_nls.travel_budget.label.date";
    public static final String ORCAMENTO_DESPESA_UNTIL = "_nls.travel.budget.label.until";
    /**
     * Label nota fiscal
     */
    public static final String ORCAMENTO_DESPESA_NOTA_FISCAL = "_nls.travel.budget.label.nota_fiscal";
    /**
     * Valor nota fiscal
     */
    public static final String ORCAMENTO_DESPESA_NOTA_FISCAL_VALOR = "_nls.travel.budget.label.nota_fiscal_val";
    /**
     * Label nota debito
     */
    public static final String ORCAMENTO_DESPESA_NOTA_DEBITO = "_nls.travel.budget.label.nota_debito";
    /**
     * Valor nota debito
     */
    public static final String ORCAMENTO_DESPESA_NOTA_DEBITO_VALOR = "_nls.travel.budget.label.nota_debito_val";
    /**
     * Mensagem para valor de orcamento maior que o planejado para viagens.
     */
    public static final String ORCAMENTO_DESPESA_ORCAMENTO_VIAGEM_LIMITE = "_nls.travel.budget.msgm.valor_maior_viagens";
    /**
     * Mensagen de erro usada na valida��o de nome do OrcamentoDespesa.
     */
    public static final String ORCAMENTO_DESPESA_CHARACTER_NOT_ALLOWED = "_nls.travel.budget.msg.error.character_not_allowed";
    /**
     * CONTRATO_PRATICA JOB
     **/

    public static final String EMAIL_ASSUNTO_CONTRATO_PRATICA_DEAL_FISCAL = "_nls.job.contrato_pratica.subject";
    public static final String EMAIL_MSG_PRINCIPAL_CONTRATO_PRATICA_DEAL_FISCAL = "_nls.job._contrato_pratica.princial.message";
    public static final String EMAIL_MSG_ITENS_CONTRATO_PRATICA_DEAL_FISCAL = "_nls.job.contrato_pratica.itens.message";
    /**
     * CONTRATO_PRATICA JOB
     **/

    public static final String EMAIL_ASSUNTO_MAPA_PROSPECT_COM_ALOCACAO = "_nls.job.prospect_map.subject";
    public static final String EMAIL_MGR_MSG_PRINCIPAL_MAPA_PROSPECT_COM_ALOCACAO = "_nls.job.prospect_map.manager.principal.message";
    public static final String EMAIL_CTRL_MSG_PRINCIPAL_MAPA_PROSPECT_COM_ALOCACAO = "_nls.job.prospect_map.controllership.principal.message";
    public static final String EMAIL_MSG_ITENS_MAPA_PROSPECT_COM_ALOCACAO = "_nls.job.prospect_map.itens.message";
    public static final String EMAIL_CONTROLLERSHIP_ALIAS_TO = "mail.address.controladoria.alias.to";
    /**
     * Email com assunto para emissao de voucher.
     */
    public static final String EMAIL_ASSUNTO_EMISSAO_VOUCHER = "_nls.voucher.email.assunto.emissao";

    /** VOUCHER */
    /**
     * Email com mensagem para emissao de voucher.
     */
    public static final String EMAIL_MSG_EMISSAO_VOUCHER = "_nls.voucher.email.msgm.emissao";
    /**
     * Email com assunto para alteracao de voucher.
     */
    public static final String EMAIL_ASSUNTO_ALTERACAO_VOUCHER = "_nls.voucher.email.assunto.alteracao";
    /**
     * Email com mensagem para alteracao de voucher.
     */
    public static final String EMAIL_MSG_ALTERACAO_VOUCHER = "_nls.voucher.email.msgm.alteracao";
    /**
     * Email com assunto para delecao de vouhcer.
     */
    public static final String EMAIL_ASSUNTO_DELECAO_VOUCHER = "_nls.voucher.email.assunto.delecao";
    /**
     * Email com mensagem para delecao de voucher.
     */
    public static final String EMAIL_MSG_DELECAO_VOUCHER = "_nls.voucher.email.msgm.delecao";
    /**
     * Email com assunto para redfalg de vouhcer.
     */
    public static final String EMAIL_ASSUNTO_REDFLAG_VOUCHER = "_nls.voucher.email.assunto.red_flag";
    /**
     * Email com mensagem para redflag de voucher.
     */
    public static final String EMAIL_MSG_REDFLAG_VOUCHER = "_nls.voucher.email.msgm.red_flag";
    /**
     * Email com mensagem para voucher aberto por v�rios dias.
     */
    public static final String EMAIL_ASSUNTO_VOUCHER_OPEN_VARIOUS_DAYS = "_nls.voucher.email.assunto.old.open";
    /**
     * Email com mensagem para voucher aberto por v�rios dias.
     */
    public static final String EMAIL_MSG_VOUCHER_OPEN_VARIOUS_DAYS = "_nls.voucher.email.assunto.old.open";
    /**
     * Email com mensagem para voucher aberto por v�rios dias.
     */
    public static final String EMAIL_MSG_VOUCHER_OPEN_ORC_DESP = "_nls.voucher.email.assunto.voucher.open.orc.desp";
    public static final String EMAIL_LICENSE_PROEJCT_MANAGER_NOTIF_BODY_MSG = "_nls.license.project.manager.email.notification.body_message";
    public static final String COMPANY_MAIL_DOMAIN = "@ciandt.com";
    public static final String MAIL_ACCOUNTING = "mail.address.accounting.alias.to";
    public static final String MAIL_COMMERCIAL_PARTNERS = "mail.address.commercial_partners.alias.to";
    /**
     * Status do voucher Open.
     */
    public static final String STATUS_VOUCHER_OPEN = "OP";
    /**
     * Status do voucher Cancel.
     */
    public static final String STATUS_VOUCHER_CANCEL = "CA";
    /**
     * Status do voucher Closed.
     */
    public static final String STATUS_VOUCHER_CLOSED = "CL";
    /**
     * Status do voucher Used.
     */
    public static final String STATUS_VOUCHER_USED = "US";
    /**
     * Status do voucher Expired.
     */
    public static final String STATUS_VOUCHER_EXPIRED = "EX";
    /**
     * Flag Yes.
     */
    public static final String FLAG_YES = "Y";
    /**
     * Flag No.
     */
    public static final String FLAG_NO = "N";
    /**
     * Tipo de Fonte de Receita Service.
     */
    public static final String TIPO_FONTE_RECEITA_SERVICE = "SV";
    /**
     * Tipo de Fonte de Receita Other.
     */
    public static final String TIPO_FONTE_RECEITA_OTHER = "OT";
    /**
     * Bundle de Revenue do Dasboard do Map.
     */
    public static final String BUNDLE_MAP_DASHBOARD_REVENUE = "_nls.mapa_alocacao.dashboard.revenue.label";
    /**
     * Bundle de Deleted do delete logico.
     */
    public static final String BUNDLE_LOGICAL_DELETE = "_nls.delete_logico.delete";
    /**
     * Bundle de Deleted do not delete logico.
     */
    public static final String BUNDLE_NOT_LOGICAL_DELETE = "_nls.delete_logico.not_delete";
    /**
     * Revenue calculation rule (tela de Financials) - 0 Projetada.
     */
    public static final int REVENUE_CALCULATION_RULE_PROJECTED = 0;
    /**
     * Revenue calculation rule (tela de Financials) - 1 Receitada.
     */
    public static final int REVENUE_CALCULATION_RULE_RECEITED = 1;
    /**
     * Revenue calculation rule (tela de Financials) - 2 None.
     */
    public static final int REVENUE_CALCULATION_RULE_NONE = 2;
    /**
     * Bundle para nome de MSA ja existente.
     */
    public static final String NAME_OF_MSA_ALREADY_EXISTS = "_nls.msa.error.name_already_exists";
    /**
     * Tipo de orcamento de despesa para Cliente.
     */
    public static final String TRAVEL_BUDGET_CLIENT_TYPE = "CL";
    /**
     * Tipo de orcamento de despesa para Cost Group.
     */
    public static final String TRAVEL_BUDGET_COST_GROUP_TYPE = "GC";
    /**
     * Mensagem para periodo de pagamento em branco na tela de criacao de
     * fatura.
     */
    public static final String INVOICE_NO_PAYMENT_PERIOD = "_nls.fatura.msg.warning.invalid_payment_period";

    public static final int INVOICE_ITEM_DESCRIPTION_MAX_LENGTH = 240;

    /**
     * Tipo cliente para TB.
     */
    public static final String TRAVEL_BUDGET_TYPE_CLIENT = "_nls.cliente_pai.entity_name";
    /**
     * Tipo cost group para TB.
     */
    public static final String TRAVEL_BUDGET_TYPE_COST_GROUP = "_nls.grupo_custo.entity_name";
    /**
     * Anexo documentoLegal ja existe.
     */
    public static final String LEGAL_DOC_ANEXO_ALREADY_EXISTS = "_nls.msa.documento_legal.msg.anexo.already_exists";
    /**
     * Jira CP Number já existe.
     */
    public static final String MSA_CONTRATO_JIRA_CP_ALREADY_EXISTS = "_nls.msa.documento_legal.msg.error.jira_cp_already_exists";
    public static final String MSA_CONTRATO_UNIQUE_KEY_ALREADY_EXISTS = "_nls.msa.documento_legal.msg.error.unique_key_exists";
    /**
     * Jira Legal Number já existe.
     */
    public static final String MSA_CONTRATO_JIRA_LEGAL_ALREADY_EXISTS = "_nls.msa.documento_legal.msg.error.jira_legal_already_exists";
    public static final String MSA_CONTRATO_DEFAULT_JIRA_CP = "SSI-0000";
    public static final String MSA_SETTINGS_GENERIC_ERROR = "_nls.msa.configuracao.error.update.generic";
    /**
     * mensagem para budget extra maior que tres meses.
     */
    public static final String TRAVEL_BUDGET_THREE_MONTHS = "_nls.travel.budget.msgm.data_maior_tres_meses";
    /**
     * Mensagem para campo frequencia invalida.
     */
    public static final String FICHA_REAJUSTE_INVALID_FREQUENCY = "_nls.ficha.reajuste.msgm.invalid.frequency";
    /**
     * Sigla Renova��o autom�tica "Yes"
     */
    public static final String DOCUMENTO_LEGAL_AUTO_RENEWAL_YES = "Y";
    /**
     * Sigla Renova��o autom�tica "No"
     */
    public static final String DOCUMENTO_LEGAL_AUTO_RENEWAL_NO = "N";
    /**
     * Status Documento Legal closed
     */
    public static final String DOCUMENTO_LEGAL_STATUS_ACTIVE = "A";
    /**
     * Status Documento Legal closed
     */
    public static final String DOCUMENTO_LEGAL_STATUS_CLOSED = "C";
    /**
     * Bundle
     */
    public static final String AUTO_RENEWAL_ERROR_MESSAGE = "_nls.msa.documento_legal.msg.auto_renewal.error";
    /**
     * Mensagem documento legal que n�o preve reajuste j� possui ficha vinculada
     */
    public static final String MSG_ERROR_DOCUMENTO_LEGAL_POSSUI_FICHA = "_nls.msa.documento_legal.msg.warning.ja_possui_ficha";
    /**
     * Sigla Status {@link FichaReajuste} ativo
     */
    public static final String FICHA_REAJUSTE_STATUS_SG_ACTIVE = "AC";
    /** CONTROL RESCINDED APROVERS JOB **/

    public static final String EMAIL_ASSUNTO_CONTROL_RESCINDED_APROVERS = "_nls.job.control_rescindend.aprovers_subject";

    public static final String EMAIL_MSG_CONTROL_RESCINDED_APROVERS_COST_CENTER  = "_nls.job.control_rescindend.aprovers.message";

    public static final String EMAIL_MSG_CONTROL_RESCINDED_APROVERS_APROVER = "_nls.job.control_rescindend.aprovers.message_aprover";

    public static final String EMAIL_MSG_ITENS_CONTROL_RESCINDED_COST_CENTER = "_nls.job.control_rescindend.aprovers.itens.message";


    /**
     * Gest�o de Reajuste - Ficha de Reajuste
     */
    /**
     * Sigla Status {@link FichaReajuste} inativo
     */
    public static final String FICHA_REAJUSTE_STATUS_SG_INACTIVE = "IN";
    /**
     * Label para nova ficha reajuste
     */
    public static final String FICHA_REAJUSTE_NEW = "New Adjustment";
    /**
     * Sigla Status {@link ControleReajuste} Open
     */
    public static final String CONTROLE_REAJUSTE_STATUS_SG_OPEN = "OP";

    /**
     * Gest�o de Reajuste - Controle de Reajuste
     */
    /**
     * Sigla Status {@link ControleReajuste} Rescheduled
     */
    public static final String CONTROLE_REAJUSTE_STATUS_SG_RESCHEDULED = "RS";
    /**
     * Sigla Status {@link ControleReajuste} Negotiating
     */
    public static final String CONTROLE_REAJUSTE_STATUS_SG_NEGOTIATING = "NG";
    /**
     * Sigla Status {@link ControleReajuste} Not Executed
     */
    public static final String CONTROLE_REAJUSTE_STATUS_SG_NOT_EXECUTED = "NE";
    /**
     * Sigla Status {@link ControleReajuste} Executed
     */
    public static final String CONTROLE_REAJUSTE_STATUS_SG_EXECUTED = "EX";
    /**
     * Sigla Status {@link ControleReajuste} Cancelled
     */
    public static final String CONTROLE_REAJUSTE_STATUS_SG_CANCELLED = "CA";
    /**
     * Sigla Status {@link ControleReajuste} Renovation
     */
    public static final String CONTROLE_REAJUSTE_STATUS_SG_RENOVATION = "RE";
    /**
     * Bundle para mensagem de campos obrigat�rios.
     */
    public static final String BUNDLE_ALL_FIELDS_REQUIRED = "_nls.controle.reajuste.mensagem.campos.obrigatorios";
    /**
     * Bundle para mensagem de campo obrigat�rio.
     */
    public static final String BUNDLE_OBS_FIELD_REQUIRED = "_nls.controle.reajuste.mensagem.obs.obrigatorio";
    /**
     * Bundle para mensagem de campos obrigat�rios.
     */
    public static final String DOCUMENTO_LEGAL_SUCCESFULLY_INACTIVATED = "_nls.msa.documento_legal.msg.successfully.inactivated";
    /**
     * Nome do parametro para data de corte na cria��o de {@link ControleReajuste}
     */
    public static final String CONTROLE_REAJUSTE_DATA_CORTE = "DATA_CORTE_CONTROLE_REAJUSTE";
    /**
     * Chave que configura a quantidade de meses anteriores a data prevista de
     * reajuste que dispar� o e-mail com lembrete de reajuste
     */
    public static final String CONTROLE_REAJUSTE_QT_MESES_INICIO_LEMBRETE = "controle.reajuste.mail.qt.meses.inicio.lembrete";

    //////////////////////////////////////
    // INICIO Gest�o de Reajuste - email
    /////////////////////////////////////
    /**
     * Configura a quantidade de dias entre e-mails do controle de reajuste, para evitar o envio mais de
     * uma vez por intervalo
     */
    public static final String CONTROLE_REAJUSTE_QT_DIAS_ENTRE_LEMBRETES = "controle.reajuste.mail.qt.dias.entre.lembretes";
    /**
     * Chave que configura a quantidade de meses anteriores a data de encerramento do
     * documento legal que dispar� o e-mail com lembrete de renova��o
     */
    public static final String DOCUMENTO_LEGAL_QT_MESES_INICIO_LEMBRETE = "documento.legal.mail.qt.meses.inicio.lembrete";
    /**
     * Configura a quantidade de dias entre e-mails do documento legal, para evitar o envio mais de
     * uma vez por intervalo
     */
    public static final String DOCUMENTO_LEGAL_QT_DIAS_ENTRE_LEMBRETES = "documento.legal.mail.qt.dias.entre.lembretes";
    /**
     * Configura a quantidade de meses anteriores a data atual que valida se um Contrato Pratica possui receita
     */
    public static final String CONTROLE_REAJUSTE_QT_MESES_RECEITAS = "controle.reajuste.mail.qt.meses.receitas";
    /**
     * # Quantidade de dias adicionados ao periodo do Documento Legal para cria��o e visualiza��o de controles de reajuste
     */
    public static final String DOCUMENTO_LEGAL_ADICAO_PERIODO = "documento.legal.adicao.dias.periodo";
    /**
     * Chave para o email do CS
     */
    public static final String CONTROLE_REAJUSTE_EMAIL_CS = "mail.address.cs_gestao_reajuste.to";
    /**
     * Bundle para o assunto do email do Controle de Reajuste a vencer
     */
    public static final String CONTROLE_REAJUSTE_EMAIL_ASSUNTO_A_VENCER = "_nls.controle.reajuste.email.assunto.a_vencer";
    /**
     * Bundle para o assunto do email do Controle de Reajuste vencido
     */
    public static final String CONTROLE_REAJUSTE_EMAIL_ASSUNTO_VENCIDO = "_nls.controle.reajuste.email.assunto.vencido";
    /**
     * Bundle para o assunto do email do Documento Legal a vencer
     */
    public static final String DOCUMENTO_LEGAL_EMAIL_ASSUNTO_A_VENCER = "_nls.legal.document.email.assunto.a_vencer";
    /**
     * Bundle para o assunto do email do Documento Legal vencido
     */
    public static final String DOCUMENTO_LEGAL_EMAIL_ASSUNTO_VENCIDO = "_nls.legal.document.email.assunto.vencido";
    /**
     * Numero de dias para antecedencia de email de legal doc.
     */
    public static final String ANTECEDENCIA_EMAIL_LEGAL_DOC = "dias.antecedencia.emails.documento.legal";
    /**
     * Bundle para o assunto do email a vencido
     */
    public static final String FICHA_REAJUSTE_MSG_INATIVO = "_nls.ficha.reajuste.msg.inactive";
    /**
     * mensagem sem itens no mapa para criar receita
     */
    public static final String MENSAGEM_NO_ITENS_MAPA_ALOACAO_RECEITA = "_nls.receita.no_itens_map";

    //////////////////////////////////////
    // FIM Gest�o de Reajuste - email
    /////////////////////////////////////


    /** mensagem para msa sem invoice type. */
    public static final String MSA_WITHOUT_INVOICE_TYPE = "_nls.travel.budget.msgm.no_invoice_type";

    /** mensagem para msa com mais de um invoice type. */
    public static final String MSA_WITH_MORE_THAN_ONE_INVOICE_TYPE = "_nls.travel.budget.msgm.more_than_one_invoice_type";

    public static final String TIPO_FATURAMENTO_MSA_TB_DEBITO_VL = "_nls.msa.nota_debito_val";
    public static final String TIPO_FATURAMENTO_MSA_TB_NOTA_FISCAL_VL = "_nls.msa.nota_fiscal_val";
    public static final String TIPO_FATURAMENTO_MSA_TB_DEBITO = "_nls.msa.nota_debito";
    public static final String TIPO_FATURAMENTO_MSA_TB_NOTA_FISCAL = "_nls.msa.nota_fiscal";

    //////////////////////////////////////
    // Fiscal Balance
    //////////////////////////////////////

    /** Opcao da tela de Fiscal Balance CFP Cut Date. */
    public static final String FISCAL_BALANCE_CFP_CUT_DATE_OPTION = "CFP";

    /** Opcao da tela de Fiscal Balance Last day of month. */
    public static final String FISCAL_BALANCE_LAST_DAY_OF_MONTH = "LDM";

    /** Quantidade de dias no m�s seguinte que ser� considerado quando selecionado a opcao CFP */
    public static final String FISCAL_BALANCE_DAYS_CFP_CUT_DATE = "fiscal_balance.cfp_cut_date";

    /** Mensagem de aviso que Cliente ou MSA � obrigat�rio */
    public static final String MSG_WARNING_FISCAL_BALANCE_MSA_OR_CLIENT_REQUIRED = "_nls.fiscal_balance.msg.warning.client_or_msa.required";

    /** Indica Balan�o Acumulado A Faturar  */
    public static final String FISCAL_BALANCE_ACCUMULATED_A_FATURAR = "F";

    /** Indica Balan�o Acumulado A Receitar  */
    public static final String FISCAL_BALANCE_ACCUMULATED_A_RECEITAR = "R";

    /** Indica Balan�o Acumulado igual a zero  */
    public static final String FISCAL_BALANCE_ACCUMULATED_ZERO = "E";

    /** Indica tipo de item da entidade VwFiscalBalanceAcumulado */
    public static final String FISCAL_BALANCE_FONTE_INVOICE = "INV";

    /** Indica tipo de item da entidade VwFiscalBalanceAcumulado */
    public static final String FISCAL_BALANCE_FONTE_REVENUE = "REC";

    public static final String EMAIL_ASSUNTO_CRIACAO_CONTRATO_PRATICA = "_nls.email.subject.contrato_pratica.create";

    public static final String EMAIL_MSG_CRIACAO_CONTRATO_PRATICA = "_nls.email.msg.contrato_pratica.create";

    public static final String EMAIL_ASSUNTO_CRIACAO_GRUPO_CUSTO = "_nls.email.subject.grupo_custo.create";

    public static final String STATUS_INFO_MESSAGE = "_nls.grupo_custo.tooltip.statusInfoMessage";

    public static final String EMAIL_MSG_CRIACAO_GRUPO_CUSTO = "_nls.email.msg.grupo_custo.create";

    public static final String CONTROLLERSHIP_EMAILS_PARAMETER = "CONTROLLERSHIP_EMAILS";

    public static final String EMAIL_ASSUNTO_INATIVACAO_CONTRATO_PRATICA = "_nls.email.subject.contrato_pratica.inactivate";

    public static final String EMAIL_ASSUNTO_REQUISICAO_INATIVACAO_CONTRATO_PRATICA = "_nls.email.subject.contrato_pratica.request_inactivation";

    public static final String EMAIL_MSG_INATIVACAO_CONTRATO_PRATICA = "_nls.email.msg.contrato_pratica.inactivate";

    public static final String EMAIL_MSG_REQUISICAO_INATIVACAO_CONTRATO_PRATICA = "_nls.email.msg.contrato_pratica.request_inactivation";

    public static final String EMAIL_MSG_ASSUNTO_REQ_INATIVACAO_GRUPO_CUSTO= "_nls.email.subject.grupo_custo.request_inactivation";

    public static final String EMAIL_MSG_REQ_INATIVACAO_GRUPO_CUSTO = "_nls.email.msg.grupo_custo.request_inactivation";

    /** Project Associate */
    public static final String ACTIVATION_PROJECT_ERROR_REQUIRED_CLOB = "_nls.menu.activation.project.error.required.c_lob";

    public static final String ACTIVATION_PROJECT_ERROR_ALREADY_USED = "_nls.menu.activation.project.error.already_used";

    public static final String ACTIVATION_PROJECT_ASSOCIATE = "_nls.project.associate";

    public static final String ACTIVATION_PROJECT_NOT_ASSOCIATE = "_nls.project.not_associate";

    public static final String ACTIVATION_PROJECT_ALL = "_nls.project.all";

    public static final String ACTIVATION_ASSOCIATE_SG = "AS";

    public static final String ACTIVATION_NOT_ASSOCIATE_SG = "NA";

    public static final String ACTIVATION_ALL = "ALL";

    /** ContratoPraticaGrupoCusto */
    public static final String CPGC_VIGENCIA_INVALIDA = "_nls.cpgc.error.vigencia";

    /** PaymentConditionDealFiscal */
    public static final String DFCP_SAME_CONDITION = "_nls.dfcp.same.condition";

    public static final String CLIENT_WITHOUT_MEGA_AGENT_CODE = "_nls.client.without.mega.agent.code";

    public static final String PAYMENT_CONDITION_NAME_IS_REQUIRED = "_nls.payment.condition.name.is.required";

    /** Configuracao endpoint Gateway */

    public static final String MGMT_URI_GATEWAY_API = "MGMT_URI_GATEWAY_API";
    public static final String URI_GATEWAY_API_VERSION = "uri.gateway.api.version";

    /** Configuracao endpoint RevenueMS */
    public static final String URI_AUTHORIZATION_TOKEN = "uri.authorization.token";

    public static final String REVENUE_PRODUCER = "REV";
    public static final String INVOICE_PRODUCER = "INV";
    public static final String SOFTWARE_LICENSE_PRODUCER = "SWL";
    public static final String PROJECT_PRODUCER = "PJT";

    public static final String INVOICE_DELETE = "DELETE";
    public static final String INVOICE_CANCEL = "CANCEL";

    /**
     //////////////////////////////////////
     // FIM Gest�o de Reajuste - email
     /////////////////////////////////////
     /**
     * mensagem para msa sem invoice type.
     */
    /**
     * JOBs
     */
    public static final String JOB_MAIL_DOCUMENTO_LEGAL_SEND_MAIL_ACTIVE = "job.mail_documento_legal.active";
    public static final String JOB_MAPA_ALOC_SNAPSHOTS_FOLLOW_ACTIVE = "job.mapa_aloc_snapshots.follow.active";
    public static final String JOB_SEND_INVOICE_MAIL_DELAYED_ACTIVE = "job.send_invoice_mail_delayed.active";
    public static final String JOB_SEND_INVOICE_MAIL_TO_BE_SUBMITTED_ACTIVE = "job.send_invoice_mail_to_be_submitted.active";
    public static final String JOB_SEND_VOUCHER_MAIL = "job.send_voucher_mail.active";
    public static final String JOB_SEND_OLD_OPEN_VOUCHER_MAIL = "job.send_old_open_voucher_mail.active";
    public static final String JOB_CONTROLE_REAJUSTE_CREATE_ACTIVE = "job.controle_reajuste_create.active";
    public static final String JOB_CONTROLE_REAJUSTE_SEND_MAIL_ACTIVE = "job.controle_reajuste.active";
    public static final String JOB_RECEITA_SNAPSHOTS_ACTIVE = "job.receita.foto.active";
    public static final String JOB_SOFTWARE_LICENSE_ACTIVE = "job.software_license.active";
    public static final String JOB_COTACAO_MOEDA_MEDIA_ACTIVE = "job.cotacao_moeda_media.active";
    public static final String JOB_CALCULATE_ALL_CURRENCY_AVERAGES_ACTIVE = "job.calculate_all_currency_averages.active";
    public static final String JOB_CONTRATO_PRATICA_DEAL_FISCAL_ACTIVE = "job.contrato_pratica_deal_fiscal.active";
    public static final String JOB_MAPA_PROSPECT_COM_ALOCACAO_ACTIVE = "job.mapa_prospect_com_alocacao.active";
    public static final String JOB_CONTROL_RESCINDED_APROVERS_ACTIVE = "job.control_rescinded_aprovers.active";

    public static final Long CD_EMPRESA_JPY = 112L;
    public static final Long CD_EMPRESA_CNY = 113L;
    public static final Long CD_EMPRESA_INC = 10L;
    public static final Long CD_EMPRESA_UK = 115L;
    public static final Long CD_EMPRESA_CAN = 119L;
    public static final Long CD_EMPRESA_AUS = 125L;
    public static final Long CD_EMPRESA_PT = 123L;
    public static final Long CD_EMPRESA_COL = 167L;
    public static final Long CD_EMPRESA_CINQ_INC = 128L;
    public static final Long CD_EMPRESA_CINQ_US = 131L;
    public static final Long CD_EMPRESA_DEXTRA_INC = 130L;
    public static final Long CD_EMPRESA_SOMO_GLOBAL_LTD_UK = 155L;
    public static final Long CD_EMPRESA_SOMO_CUSTOM_LTD_UK = 157L;
    public static final Long CD_EMPRESA_SOMO_GLOBAL_SAS_CO = 159L;
    public static final Long CD_EMPRESA_SOMO_GLOBAL_INC_US = 161L;
    public static final Long CD_EMPRESA_SOMO_LTD_UK = 163L;
    public static final Long CD_EMPRESA_OCEANIA = 171L;
    public static final Long CD_EMPRESA_NTERSOL = 177L;
    public static final Long CD_EMPRESA_COREIP = 179L;
    public static final Long CD_EMPRESA_BR_MATRIZ = 1L;
    public static final Long CD_EMPRESA_BR_CPS = 4L;
    public static final Long CD_EMPRESA_BR_SP = 5L;
    public static final Long CD_EMPRESA_BR_BH = 6L;
    public static final Long CD_EMPRESA_BR_RJ = 11L;

    public static final String LEGACY_ERP_BRAZIL = "MEGA";

    public static final List<Long> COMPANIES_WITH_XERO_INTEGRATION_AND_ALLOWED_TO_SUMMARIZE_INVOICE = Collections.unmodifiableList(Arrays.asList(
            Constants.CD_EMPRESA_SOMO_CUSTOM_LTD_UK,
            Constants.CD_EMPRESA_SOMO_GLOBAL_SAS_CO,
            Constants.CD_EMPRESA_SOMO_GLOBAL_INC_US,
            Constants.CD_EMPRESA_OCEANIA
    ));
    public static final List<Long> REVENUE_SOURCES_THAT_CAN_BE_SUMMARIZED = Collections.singletonList(Constants.CONTRACTOR_REVENUE_SOURCE);

    public static final String LICENCA_SW_PROJETO_STATUS_WAITING_APPROVAL = "AGUARDANDO_APROVACAO";
    public static final String LICENCA_SW_PROJETO_STATUS_APPROVED = "APROVADO";
    public static final String LICENCA_SW_PROJETO_STATUS_PENDENTE = "PENDENTE";
    public static final String LICENCA_SW_PROJETO_STATUS_INTEGRADO = "INTEGRADO";
    public static final String LICENCA_SW_PROJETO_STATUS_ERRO = "ERRO";
    public static final String LICENCA_SW_PROJETO_TIPO_PARCELA_LP = "LONGO";
    /**
     * RabbitMQ
     */
    public static final String RABBITMQ_HOST = "rabbitmq.host";
    public static final String RABBITMQ_PORT = "rabbitmq.port";
    public static final String RABBITMQ_USERNAME = "rabbitmq.username";
    public static final String RABBITMQ_LOG_CONNECTION_FAIL_LAYOUT = "====================RABBITMQ CONNECTION FAIL==================================";
    public static final String RABBITMQ_LOG_CONNECTION_FAIL_TEXT = "No connection to RabbitMQ";
    public static final String RABBITMQ_PASSWORD = "rabbitmq.password";
    public static final String RABBITMQ_REVENUE_EXCHANGE_INCOMING_NAME = "rabbitmq.revenueExchange.incomingRevenue.name";
    public static final String RABBITMQ_REVENUE_EXCHANGE_QUEUE_ACTIVE = "rabbitmq.revenueExchange.pendingQueue.active";
    public static final String RABBITMQ_REVENUE_EXCHANGE_INCOMING_QUEUE_NAME = "rabbitmq.revenueExchange.incomingRevenue.pendingQueue.name";
    public static final String RABBITMQ_REVENUE_EXCHANGE_OUTGOING_NAME = "rabbitmq.revenueExchange.outgoingRevenue.name";
    public static final String RABBITMQ_REVENUE_EXCHANGE_OUTGOING_NAME_EXCEPTION = "rabbitmq.revenueExchange.outgoingRevenue.pendingQueue.exception";
    public static final String RABBITMQ_REVENUE_EXCHANGE_OUTGOING_QUEUE_NAME = "rabbitmq.revenueExchange.outgoingRevenue.pendingQueue.name";
    public static final String RABBITMQ_INVOICE_EXCHANGE_INCOMING_NAME = "rabbitmq.invoiceExchange.incomingInvoice.name";
    public static final String RABBITMQ_INVOICE_EXCHANGE_QUEUE_ACTIVE = "rabbitmq.invoiceExchange.pendingQueue.active";
    public static final String RABBITMQ_INVOICE_EXCHANGE_INCOMING_QUEUE_NAME = "rabbitmq.invoiceExchange.incomingInvoice.pendingQueue.name";
    public static final String RABBITMQ_INVOICE_EXCHANGE_OUTGOING_NAME = "rabbitmq.invoiceExchange.outgoingInvoice.name";
    public static final String RABBITMQ_INVOICE_EXCHANGE_OUTGOING_NAME_EXCEPTION = "rabbitmq.invoiceExchange.outgoingInvoice.pendingQueue.exception";
    public static final String RABBITMQ_INVOICE_EXCHANGE_OUTGOING_QUEUE_NAME = "rabbitmq.invoiceExchange.outgoingInvoice.pendingQueue.name";
    public static final String RABBITMQ_SOFTWARE_LICENSE_EXCHANGE_INCOMING_NAME = "rabbitmq.softwareLicenseExchange.incomingSoftwareLicense.name";
    public static final String RABBITMQ_SOFTWARE_LICENSE_EXCHANGE_QUEUE_ACTIVE = "rabbitmq.softwareLicenseExchange.pendingQueue.active";
    public static final String RABBITMQ_SOFTWARE_LICENSE_EXCHANGE_INCOMING_QUEUE_NAME = "rabbitmq.softwareLicenseExchange.incomingSoftwareLicense.pendingQueue.name";
    public static final String RABBITMQ_SOFTWARE_LICENSE_EXCHANGE_OUTGOING_NAME = "rabbitmq.softwareLicenseExchange.outgoingSoftwareLicense.name";
    public static final String RABBITMQ_SOFTWARE_LICENSE_EXCHANGE_OUTGOING_NAME_EXCEPTION = "rabbitmq.softwareLicenseExchange.outgoingSoftwareLicense.pendingQueue.exception";
    public static final String RABBITMQ_SOFTWARE_LICENSE_EXCHANGE_OUTGOING_QUEUE_NAME = "rabbitmq.softwareLicenseExchange.outgoingSoftwareLicense.pendingQueue.name";
    public static final String RABBITMQ_DELETE_INVOICE_ACTIVE = "rabbitmq.deleteInvoiceExchange.active";
    public static final String RABBITMQ_DELETE_INVOICE_EXCHANGE = "rabbitmq.deleteInvoiceExchange";
    public static final String RABBITMQ_QUEUE_NOT_FOUND = "Queue not found on RabbitMQ. Please create the Queue on RabbitMQ.";
    public static final String RABBIT_QUEUE_MEGA_MIDDLEWARE_DOACTION = "rabbitmq.megamiddleware.doActionQueue.name";
    public static final String RABBITMQ_LICENSE_TO_QB_QUEUE_NAME = "rabbitmq.licenseToQuickbooks.pendingQueue.name";
    public static final String RABBITMQ_LICENSE_TO_ORACLE_QUEUE_NAME = "rabbitmq.licenseToOracleErp.incomingQueue.name";
    public static final String RABBITMQ_LICENSE_TO_QB_QUEUE_ACTIVE = "rabbitmq.licenseToQuickbooks.pendingQueue.active";
    public static final String RABBITMQ_PROJECT_EXCHANGE_QUICKBOOKS_QUEUE_NAME = "rabbitmq.projectExchange.integrationPendingQuickbooks.pendingQueue.name";
    public static final String RABBITMQ_CANCEL_INVOICE_ACTIVE = "rabbitmq.cancelInvoice.active";
    public static final String RABBITMQ_CANCEL_INVOICE_EXCHANGE = "rabbitmq.cancelInvoice.exchange";
    public static final String RABBITMQ_RECALCULATE_MAP_QUEUE_NAME = "rabbitmq.recalculateMap.name";

    /**
     * License Sw Integration
     */
    public static final String LICENSE_SW_INTEGRATION_ACTION_CODE = "license.integration.mega.actioncode";
    public static final String ENTITY_NAME_SUB_BUDGET_AREA = "_nls.sub_area_orcamentaria.entity_name";
    public static final String SUB_BUDGET_AREA_REQUIRED_NAME = "_nls.sub_area_orcamentaria.msg.error.required.name";
    public static final String SUB_BUDGET_AREA_REQUIRED_BUDGET_AREA = "_nls.sub_area_orcamentaria.msg.error.required.area_orcamentaria";
    public static final String SUB_BUDGET_AREA_INVALID_BUDGET_AREA = "_nls.sub_area_orcamentaria.msg.error.invalid.area_orcamentaria";
    public static final String SUB_BUDGET_AREA_ERROR_ASSOCIATED_COST_CENTERS = "_nls.sub_area_orcamentaria.msg.error.associated_cost_centers";
    public static final List<String> SUB_AREA_ORCAMENTARIA_ACTIVE_INACTIVE_VALUES = Arrays.asList(
            ACTIVE_INACTIVE, ACTIVE, INACTIVE);
    /**
     * Bundle para o TB Purpose GENERAL do Travel Budget.
     */
    public static final String BUNDLE_KEY_TB_PURPOSE_GENERAL = "_nls.travel.budget.tb_purpose.general";
    /**
     * Bundle para o TB Purpose BENEFIT do Travel Budget.
     */
    public static final String BUNDLE_KEY_TB_PURPOSE_BENEFIT = "_nls.travel.budget.tb_purpose.benefit";
    /**
     * Indicador do TB Purpose .
     */
    public static final String TB_PURPOSE_GENERAL = "GE";
    /**
     * Indicador do TB Purpose Benefit
     */
    public static final String TB_PURPOSE_BENEFIT = "BE";
    /* Indicador para TB EPD */
    public static final String TB_PURPOSE_EDP = "_EDP";
    /**
     * RabbitMQ
     */
    public static final String RECEITA_ADJUSTMENT_INVALID_DATE = "_nls.msg.error.inval_date_receita_adjustment";
    /**
     * License Sw Integration
     */
    public static final String LEGAL_DOC_DUPLICATED_CNPJ = "_nls.msg.error.duplicated_cnpj";
    public static final String LEGAL_DOC_INVALID_CNPJ = "_nls.msg.error.invalid_cnpj";
    public static final String MSA_SETTINGS_DUPLICATED_LOGIN = "_nls.msg.error.msa_settings_duplicated_login";

    /**
     * Mensagem de erro da leitura de planilha de múltiplos logins na adição de recurso para licença
     */
    public static final String MSG_ERROR_MODULO_CHARGEBACK_UPLOAD_FILE_LOGINS = "_nls.msg.error.modulo.chargeback.upload.file.logins";
    public static final String MSG_ERROR_MODULO_CHARGEBACK_FILE_HAS_LOGINS_INCORRECT = "_nls.msg.error.modulo.chargeback.file.has.logins.incorrect";

    /**
     * Mensagens para Industries Types
     **/
    public static final String DEFAULT_MSG_INDUSTRY_TYPE_ALREADY_EXISTS = "_nls.msg.industry.type.already.exists";

    /**
     * Indicador para valor Draft - "D".
     */
    public static final String DRAFT = "D";
    public static final String PRICE_TABLE_STATUS_DRAFT = "Draft";

    /**
     * Indicador para valor READY FOR APPROVAL - "REAP".
     */
    public static final String READY_FOR_APPROVAL = "REAP";
    public static final String PRICE_TABLE_STATUS_READY_FOR_APPROVAL = "Ready for Approval";

    /**
     * Indicador para valor ON_APPROVAL - "ONAP".
     */
    public static final String ON_APPROVAL = "ONAP";
    public static final String PRICE_TABLE_STATUS_ON_APPROVAL = "On Approval";


    /**
     * Indicador para valor APPROVED - "APP".
     */
    public static final String APPROVED = "APP";
    public static final String PRICE_TABLE_STATUS_APPROVED = "Approved";

    /**
     * Indicador para valor NOT APPROVED - "NAPP".
     */
    public static final String NOT_APPROVED = "NAPP";
    public static final String PRICE_TABLE_STATUS_NOT_APPROVED = "Not Approved";

    /**
     * Indicador para valor NOT APPROVED - "Not Approved".
     */
    public static final String NOT_APPROVED_DESCRIPTION = "Not Approved";

    /**
     * Indicador para valor DELETED - "DEL".
     */
    public static final String DELETED = "DEL";
    public static final String PRICE_TABLE_STATUS_DELETED = "Deleted";

    public static final String NOT_APPROVE_REASONS_DESCRIPTION = "_nls.msa.configuracao.price_table.not_approve_reasons_description";

    /**
     * outcome tela view.
     */
    public static final String OUTCOME_ITEM_PRICE_TABLE_VIEW = "itemPriceTable_view";

    /**
     * outcome tela view dsabled.
     */
    public static final String OUTCOME_ITEM_PRICE_TABLE_VIEW_DISABLED = "itemPriceTable_view_disabled";

    public static final String MSG_APPROVE_PRICE_TABLE_SELECT_ALL_APPROVE_COLUMN = "_nls.msa.configuracao.price_table.approve.select_all_approve_columns";
    public static final String MSG_APPROVE_PRICE_TABLE_SELECT_AT_LEAST_ONE_APPROVE_COLUMN = "_nls.msa.configuracao.price_table.approve.select_at_least_one_approve_columns";

    /**
     * Audit Date for Price Table
     */
    public static final String PRICE_TABLE_DATE_AUDIT = "2022-10-01";

    public static final String MSG_ERRO_INPUT_INVALID = "_nls.input.msg.error.invalid";

    public static final String MSG_NO_EXCHANGE_RATE_TO_COPY = "_nls.media_cotacao_moeda.msg.copy.no_exchange";
    public static final String MSG_EXCHANGE_COPY_SAME_DATE = "_nls.media_cotacao_moeda.msg.copy.same_date";
    public static final String MSG_EXCHANGE_COPY_CLOSED_MONTH = "_nls.media_cotacao_moeda.msg.copy.closed_month";
    public static final String MSG_EXCHANGE_COPY_SUCCESS = "_nls.media_cotacao_moeda.msg.copy.success";
    public static final String MSG_EXCHANGE_DELETE_SUCCESS = "_nls.media_cotacao_moeda.msg.delete.success";
    public static final String MSG_EXCHANGE_AUTH = "_nls.media_cotacao_moeda.msg.copy.auth";
    public static final String MSG_EXCHANGE_TICKET= "_nls.media_cotacao_moeda.msg.copy.ticket";
    public static final String PWD_EXCHANGE_PASSWORD_1 = "_nls.media_cotacao_moeda.pwd.password_1";
    public static final String PWD_EXCHANGE_PASSWORD_2 = "_nls.media_cotacao_moeda.pwd.password_2";
    public static final int PARAM_DIAS_VALIDACAO_PASSWORD = 5;
    public static final String LABEL_REPLACE = "R";
    public static final String LABEL_DELETE = "D";

    /**
     * Parametro da configuração da data de fechamento para reter as Revenues de Intercompany no Mega-Middleware
     */
    public static final String MEGA_MIDDLEWARE_DATE_INTERCOMPANY_REVENUE_INTEGRATION = "MEGA_MIDDLEWARE_DATE_INTERCOMPANY_REVENUE_INTEGRATION";
    public static final String MEGA_MIDDLEWARE_DOACTION_KEY_CLEAR_CACHE = "MEGA_MIDDLEWARE_DOACTION_KEY_CLEAR_CACHE?key=";
    public static final String MEGA_MIDDLEWARE_DOACTION_KEY_START_INTEGRATION = "MEGA_MIDDLEWARE_DOACTION_KEY_START_INTEGRATION";

    /**
     * Parametro para envio de receita com APL diferentes
     */
    public static final List<Long> SERVICES_OTHERS_APL_CODE = Arrays.asList(103L, 104L, 105L, 106L);
    public static final Long APL_SERVICE_CODE = 57L;
    public static final Long TPD_SERVICE_CODE = 159L;
    public static final Long INTERNATIONAL_APL_CODE = 24L;

    public static final String REMOVE_PESSOA_GRUPO_CUSTO_TICKET_REQUIRED_ERROR = "_nls.pessoa.pessoa_grupo_custo.delete.ticket_required.error";

    public static final String COST_CENTER_ACCOUNT_ANALITYCAL = "ANALYTICAL";
    public static final String COST_CENTER_ACCOUNT_SYNTHETIC = "SYNTHETIC";

    /**
     * Parametros para update de business manager via upload
     */

    public static final String CONTRATO_PRATICA_CLOB_NOT_FOUND = "C-LOB not found or does not exist:";
    public static final String CONTRATO_PRATICA_LOGIN_NOT_FOUND = "Login not found or does not exist:";
    public static final String CONTRATO_PRATICA_BUSINESS_MANAGER_NOT_REGISTRED = "Login not registered as Business Manager:";
    public static final String CONTRATO_PRATICA_INVALID_DATE = "Invalid date or date earlier than the current date:";
    public static final String CONTRATO_PRATICA_UPDATE_MANAGER_COPY_MAIL="silvio.xavier";
    public static final String CONTRATO_PRATICA_UPDATE_MANAGER_MAIL_SUBJECT="C-LOB Update";
    public static final String CONTRATO_PRATICA_MANAGER_NOT_REGISTRED = "Login not registered as Manager:";
    public static final String CONTRATO_PRATICA_MANAGER_NULL = "_nls.contrato_pratica.aprrover.null";
    public static final String CONTRATO_PRATICA_APPROVER_MANAGER_NULL = "_nls.contrato_pratica.aprrover_manager.null";

    /**
     * Parametros para insert de legal docs via upload
     */
    public static final String MSA_LEGAL_DOC_PROJECT_DESCRIPTION_UPLOAD_ERROR = "Project/Description cannot be null or empty";
    public static final String MSA_LEGAL_DOC_JIRA_CP_UPLOAD_ERROR = "Jira CP cannot be null or empty";
    public static final String MSA_LEGAL_DOC_TYPE_UPLOAD_ERROR = "Type not found or invalid:";
    public static final String MSA_LEGAL_DOC_CURRENCY_UPLOAD_ERROR = "Currencies are not found in the MSA or are invalid:";
    public static final String MSA_LEGAL_DOC_FTE_UPLOAD_ERROR = "The FTE field must be provided and can only be 'Y' or 'N':";
    public static final String MSA_LEGAL_DOC_TYPE_AMOUNT_EXPENSES_UPLOAD_ERROR = "Type Amount Expenses not found or invalid:";
    public static final String MSA_LEGAL_DOC_TOTAL_AMOUNT_EXPENSES_UPLOAD_ERROR = "Total amount Expenses not found or invalid:";
    public static final String MSA_LEGAL_DOC_PERCENT_EXPENSES_UPLOAD_ERROR = "Percent Expenses not found or invalid:";
    public static final String MSA_LEGAL_DOC_STATUS_UPLOAD_ERROR = "Status not found or invalid:";
    public static final String MSA_LEGAL_DOC_START_DATE_UPLOAD_ERROR = "Invalid start date:";
    public static final String MSA_LEGAL_DOC_END_DATE_UPLOAD_ERROR = "Invalid end date:";
    public static final String MSA_LEGAL_DOC_MSA_UPLOAD_ERROR = "MSA not found or invalid:";
    public static final String MSA_LEGAL_DOC_HAS_LIMIT_UPLOAD_ERROR = "Has Limit can only be 'Y' or 'N':";
    public static final String MSA_LEGAL_DOC_TEXTO_MES_IPCA_UPLOAD_ERROR = "Texto Mes IPCA not found or invalid";
    public static final String MSA_LEGAL_DOC_TIPO_SERVICO_UPLOAD_ERROR = "Service Type not found or invalid:";
    public static final String MSA_LEGAL_DOC_BASE_UPLOAD_ERROR = "Bases not found or invalid:";
    public static final String MSA_LEGAL_DOC_CNPJ_UPLOAD_ERROR = "Invalid CNPJ:";
    public static final String MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR = "Mandatory field is empty or null";
    public static final String MSA_LEGAL_DOC_UPLOAD_ALREADY_EXIST_ERROR = "Already exist: ";
    public static final String MSA_LEGAL_DOC_UPLOAD_INDETERMINATE_DATE_ERROR ="Record(s) marked as Indeterminate but with the end date field filled.";
    public static final String MSA_LEGAL_DOC_UPLOAD_NOT_INDETERMINATE_ERROR ="If not Indeterminate, end Date is required";
    public static final String MSA_LEGAL_DOC_TOTAL_CONTRACT_AMOUNT_UPLOAD_ERROR = "Total Contract Amount not found or invalid:";
    public static final String MSA_LEGAL_DOC_UPLOAD_VALUE_ERROR = "Value is mandatory and must be greater than 0";

    public static final String[] ERROR_KEYS = {
            Constants.MSA_LEGAL_DOC_PROJECT_DESCRIPTION_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_JIRA_CP_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_TYPE_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_CURRENCY_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_FTE_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_TYPE_AMOUNT_EXPENSES_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_STATUS_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_START_DATE_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_END_DATE_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_MSA_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_HAS_LIMIT_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_TEXTO_MES_IPCA_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_TIPO_SERVICO_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_BASE_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_CNPJ_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_UPLOAD_ALREADY_EXIST_ERROR,
            Constants.MSA_LEGAL_DOC_PERCENT_EXPENSES_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_TOTAL_AMOUNT_EXPENSES_UPLOAD_ERROR,
            Constants.MSA_LEGAL_DOC_TOTAL_CONTRACT_AMOUNT_UPLOAD_ERROR
    };

    /**
     * Constantes de configuração do payload para o OracleERP
     */
    public static final String ORACLE_USER_SOURCE_NAME = "PMS";
    public static final String ORACLE_USER_CATEGORY_NAME = "LICENSES";
    public static final String ORACLE_JOURNAL_NAME = "Integrate Licenses";
    public static final String COMPANY_INC_CURRENCY_CODE = "USD";


    /**
     * Service Account File
     */
    public static final String SERVICE_ACCOUNT_FILE = "sa.json";

    /**
     * DATA PRIVACY
     */
    public static final String DATA_PRIVACY_LINK = "data.privacy.link";
    public static final String DATA_PRIVACY_INITIAL = "data.privacy.initial";

    /**
     * Construtor da Classe.
     */
    private Constants() {
    }
}
