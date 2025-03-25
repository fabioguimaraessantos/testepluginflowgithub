package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IDealFiscalService;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IMsaContratoCnpjService;
import com.ciandt.pms.business.service.IMsaContratoFilialService;
import com.ciandt.pms.business.service.IMsaContratoService;
import com.ciandt.pms.business.service.IMsaContratoTipoServicoService;
import com.ciandt.pms.business.service.IMsaSaldoMoedaService;
import com.ciandt.pms.business.service.IMsaService;
import com.ciandt.pms.business.service.IMsaStatusContratoService;
import com.ciandt.pms.business.service.IMsaTipoContratoService;
import com.ciandt.pms.business.service.IMsaTipoMontanteDespesaService;
import com.ciandt.pms.business.service.ITaxaImpostoService;
import com.ciandt.pms.business.service.ITipoServicoService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.csv.util.CsvUtil;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.MsaContrato;
import com.ciandt.pms.model.MsaContratoCnpj;
import com.ciandt.pms.model.MsaContratoFilial;
import com.ciandt.pms.model.MsaContratoTipoServico;
import com.ciandt.pms.model.MsaSaldoMoeda;
import com.ciandt.pms.model.MsaStatusContrato;
import com.ciandt.pms.model.MsaTipoContrato;
import com.ciandt.pms.model.MsaTipoMontanteDespesa;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.TaxaImposto;
import com.ciandt.pms.model.TipoServico;
import com.ciandt.pms.model.UploadMsaLegalDoc;
import com.ciandt.pms.model.vo.MsaSaldoMoedaRow;
import com.ciandt.pms.persistence.dao.IMsaDao;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.MailSenderUtil;
import com.ciandt.pms.util.NumberUtil;
import com.google.common.base.Preconditions;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.deleteWhitespace;

/**
 * 
 * A classe MsaService proporciona as funcionalidades da camada de serviço
 * referente a entidade Msa.
 * 
 * @since 01/10/2012
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class MsaService implements IMsaService {

	/** instancia do DAO da entidade Msa. */
	@Autowired
	private IMsaDao msaDao;

	/** Service da entidade Moeda. */
	@Autowired
	private IMoedaService moedaService;
	/** Service da entidade MontanteDespesaService**/
	@Autowired
	private IMsaTipoMontanteDespesaService msaTipoMontanteDespesaService;

	/** Service da entidade IMsaStatusContratoService**/
	@Autowired
	private IMsaStatusContratoService msaStatusContratoService;

	@Autowired
	private IMsaContratoCnpjService msaContratoCnpjService;

	/** Service da entidade MsaSaldoMoeda. */
	@Autowired
	private IMsaSaldoMoedaService msaSalMoeService;

	/** Intancia de mailSender. */
	private MailSenderUtil mailSender;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** Servico de DealFiscal. */
	@Autowired
	private IDealFiscalService dealFiscalService;

	/** Servico de TaxaImposto. */
	@Autowired
	private ITaxaImpostoService taxaImpostoService;
	/** Servico de MsaTipoContrato. */
	@Autowired
	private IMsaTipoContratoService msaTipoContratoService;

	@Autowired
	private IMsaContratoService msaContratoService;

	@Autowired
	private ITipoServicoService tipoServicoService;

	@Autowired
	private IEmpresaService empresaService;

	@Autowired
	private IMsaContratoFilialService msaContratoFilialService;

	@Autowired
	private IMsaContratoTipoServicoService msaContratoTipoServicoService;

	@Autowired
	private IContratoPraticaService contratoPraticaService;



	/**
	 * outcome tela de configure da entidade.
	 */
	private static final String PADRAO_ARQUIVO_CSV = "Padrão OpenOffice";

	private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(Constants.DEFAULT_RESOURCE_BUNDLE);

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
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida
	 */
	public boolean createMsa(final Msa entity) {
		if (this.existsMsa(entity)) {
			return false;
		} 
			msaDao.create(entity);
			this.sendMsaMail(entity);
				
		return true;
	}

	public boolean existsMsa(final Msa msa) {

		Msa msaConsult = msaDao.findAllByName(msa);
		if (msaConsult != null) {
			return true;
		}

		return false;
	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * @throws IntegrityConstraintException
	 *             lança exceção caso o Msa possua ContratoPratica associados e
	 *             tente inativá-la
	 */
	public void updateMsa(final Msa entity) throws IntegrityConstraintException {
		// busca a entidade do banco por causa da sessão de conexão
		// (hibernate)
		Msa msa = findMsaById(entity.getCodigoMsa());

		// verifica se a entidade da tela está sendo inativada
		if (entity.getIndicadorStatus().equals(Constants.INACTIVE)) {

			// se sim, usa a entidade do banco para verificar se existem filhos,
			// e lança exceção
			if (msa.getContratoPraticas().size() > 0) {
				throw new IntegrityConstraintException(
						Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_INACTIVE);
			}
		}

		// condicao para conseguir editar um Msa sem pessoa aprovadora.
		if ((entity.getPessoa() != null)
				&& (entity.getPessoa().getCodigoPessoa() == null)) {
			entity.setPessoa(null);
		}
		
		msaDao.update(entity);
		contratoPraticaService.atualizaNomesContratoPratica(msa.getContratoPraticas());
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 * @throws IntegrityConstraintException
	 *             lança exceção caso o Msa possua ContratoPratica associados e
	 *             tente inativá-lo
	 */
	public void removeMsa(final Msa entity) throws IntegrityConstraintException {
		// verifica se existem entidades relacionadas, se sim lança exceção
		if (entity.getContratoPraticas().size() > 0) {
			throw new IntegrityConstraintException(
					Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
					Constants.ENTITY_NAME_CONTRATO_PRATICA);
		}
		if (entity.getDealFiscals().size() > 0) {
			throw new IntegrityConstraintException(
					Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
					Constants.ENTITY_NAME_DEAL);
		}
		if (entity.getTabelaPrecos().size() > 0) {
			throw new IntegrityConstraintException(
					Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
					Constants.ENTITY_NAME_TABELA_PRECO);
		}
		if (entity.getPerfilVendidos().size() > 0) {
			throw new IntegrityConstraintException(
					Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
					Constants.ENTITY_NAME_PERFIL_VENDIDO);
		}
		msaDao.remove(entity);
	}

	/**
	 * Executa um update da Tab Budget.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * @param listMsaSalMoeRow
	 *            listMsaSalMoeRow
	 * 
	 * @throws IntegrityConstraintException
	 *             lança exceção caso o Msa possua ContratoPratica associados e
	 *             tente inativá-lo
	 */
	public void updateMsaTabBudget(final Msa entity,
			final List<MsaSaldoMoedaRow> listMsaSalMoeRow)
			throws IntegrityConstraintException {
		List<MsaSaldoMoeda> listMsaSalMoeAdd = new ArrayList<MsaSaldoMoeda>();
		List<MsaSaldoMoeda> listMsaSalMoeUpdate = new ArrayList<MsaSaldoMoeda>();

		for (MsaSaldoMoedaRow row : listMsaSalMoeRow) {
			if (row.getIsCheckBoxSelected()) {
				row.getMsaSalMoe().setIndicadorAtivo(Constants.ACTIVE);
				listMsaSalMoeAdd.add(row.getMsaSalMoe());
			} else if (row.getMsaSalMoe().getCodigoMsaSaldo() != null) {
				listMsaSalMoeUpdate.add(row.getMsaSalMoe());
			}
		}

		entity.setMsaSaldoMoedas(listMsaSalMoeAdd);

		MsaSaldoMoeda msaSalMoeUpdate;
		for (MsaSaldoMoeda msaSalMoe : listMsaSalMoeUpdate) {
			msaSalMoeUpdate = msaSalMoeService.findMsaSalMoeById(msaSalMoe
					.getCodigoMsaSaldo());
			msaSalMoeUpdate.setIndicadorAtivo(Constants.INACTIVE);

			msaSalMoeService.updateMsaSalMoe(msaSalMoeUpdate);
		}

		try {
			this.updateMsa(entity);
		} catch (IntegrityConstraintException e) {
			throw e;
		}
	}

	/**
	 * Busca todas as entidades.
	 * 
	 * @return retorna todas as entidades.
	 */
	public List<Msa> findMsaAll() {
		return msaDao.findAll();
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades mas apenas o codigo e
	 *         nome estao preenchidos.
	 */
	@Override
	public List<Msa> findAllReturnCodigoAndNomeMsa() {
		return msaDao.findAllReturnCodigoAndNomeMsa();
	}


	/**
	 * Busca uma lista de entidades por filtro.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio do filtro
	 */
	public List<Msa> findMsaByFilter(final Msa filter) {
		return msaDao.findByFilter(filter);
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public Msa findMsaById(final Long id) {
		return msaDao.findById(id);
	}

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param cliente
	 *            - Cliente que se deseja buscar os Msa
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<Msa> findMsaByCliente(final Cliente cliente) {
		return msaDao.findByCliente(cliente);
	}

	@Override
	public List<Msa> findMsaByBmDn(Long bmdn) throws BusinessException{

		List<Msa> result = msaDao.findByBmDn(bmdn);
		if(result == null || result.isEmpty())
			throw new BusinessException("MSAs not found");

		return result;
	}

	@Override
	public List<Msa> findMsaByIndustryType(Long industryType) throws BusinessException{

		List<Msa> result = msaDao.findByIndustryType(industryType);
		if(result == null || result.isEmpty())
			throw new BusinessException("MSAs not found");

		return result;
	}

	/**
	 * Busca todas as Moedas ativas e marcadas como selecionadas na aba Budget
	 * do MSA.
	 * 
	 * @param msa
	 *            msa
	 * @return retorna uma lista com as entidades.
	 */
	public List<MsaSaldoMoedaRow> getListMsaSaldoMoedaRow(final Msa msa) {
		// Busca os MsaSaldoMoeda do MSA e guarda em um map.
		Map<Long, MsaSaldoMoeda> mapMsaSalMoe = new HashMap<Long, MsaSaldoMoeda>();
		List<MsaSaldoMoeda> listMsaSalMoe = msaSalMoeService
				.findMsaSalMoeByMsa(msa);
		for (MsaSaldoMoeda msaSalMoe : listMsaSalMoe) {
			mapMsaSalMoe.put(msaSalMoe.getMoeda().getCodigoMoeda(), msaSalMoe);
		}

		List<MsaSaldoMoedaRow> listMsaSalMoeRow = new ArrayList<MsaSaldoMoedaRow>();

		// Busca todas as Moedas disponíveis e analisa as que o MSA já tem
		// associado. Se sim, mostrará a Moeda checkada e o saldo. Se não,
		// apenas mostra a Moeda sem check e sem saldo.
		List<Moeda> listMoeda = moedaService.findMoedaAll();
		for (Moeda moeda : listMoeda) {
			MsaSaldoMoedaRow msaSalMoeRow = new MsaSaldoMoedaRow();

			msaSalMoeRow.setIsCheckBoxSelected(Boolean.valueOf(false));

			// Verifica se o MSA tem a Moeda corrente associada
			Long codigoMoeda = moeda.getCodigoMoeda();
			if (mapMsaSalMoe.containsKey(codigoMoeda)) {
				MsaSaldoMoeda msaSalMoe = mapMsaSalMoe.get(codigoMoeda);

				msaSalMoeRow.setIsSelected(Boolean.valueOf(true));
				if (msaSalMoe.getIndicadorAtivo().equals(Constants.ACTIVE)) {
					msaSalMoeRow.setIsCheckBoxSelected(Boolean.valueOf(true));
				} else {
					msaSalMoeRow.setIsCheckBoxSelected(Boolean.valueOf(false));
				}
				msaSalMoeRow.setMsaSalMoe(msaSalMoe);

				listMsaSalMoeRow.add(msaSalMoeRow);
				// se não, cria um objeto com atributos vazios para ser
				// usado na atualização do MSA
			} else {
				MsaSaldoMoeda msaSalMoe = new MsaSaldoMoeda();
				msaSalMoe.setMsa(msa);
				msaSalMoe.setMoeda(moeda);

				msaSalMoeRow.setMsaSalMoe(msaSalMoe);

				listMsaSalMoeRow.add(msaSalMoeRow);
			}
		}

		return listMsaSalMoeRow;
	}

	/**
	 * Retorna todas as entidades no estado completo.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<Msa> findMsaAllComplete() {
		return msaDao.findAllComplete();
	}

	/**
	 * Cria e envia uma mensagem de email de um Contrato Pratica.
	 * 
	 * @param msa
	 *            - entidade do tipo ContratoPratica.
	 */
	private void sendMsaMail(final Msa msa) {

		Map<String, Object> dataSource = new HashMap<String, Object>();
		dataSource.put("msa", msa);
		dataSource.put("author", LoginUtil.getLoggedUsername());

		String message = mailSender
				.getTemplateMailMessage("msa.vm", dataSource);

		String subject = BundleUtil.getBundle("_nls.msa.mail.subject",
				msa.getNomeMsa());

		String to = systemProperties
				.getProperty(Constants.EMAIL_ADDRESS_ADM_COMERCIAL_KEY);

		mailSender.sendHtmlMail(to, subject, message);
	}

	/**
	 * Checa se o MSA esta com status completo. Caso esteja retorna TRUE.
	 * 
	 * @param codigoMsa
	 *            - codigo do MSA a ser pesquisado
	 * 
	 * @return retorna true caso o MSA em questao esteja completo
	 */
	@Override
	public boolean isMsaCompleted(final Long codigoMsa) {
		Preconditions.checkNotNull(codigoMsa, "codigoMsa can not be null!");
		Msa msa = this.findMsaById(codigoMsa);
		return Constants.STATUS_COMPLETE.equals(msa.getIndicadorStatus());
	}

	/**
	 * Calcula o valor do Forecast Sales Tax: media de impostos dos Fiscals
	 * Deals associados ao Contract-LOB, sendo que o imposto do Fiscal Deal eh a
	 * media dos impostos de seus Service Types associados.
	 * 
	 * @param msa
	 *            - ContratoPratica que será usado para calcular o Forecast
	 *            Sales Tax
	 * 
	 * @return valor do Forecast Sales Tax
	 */
	public BigDecimal calculateForecastSalesTax(final Msa msa) {

		// variaveis
		BigDecimal totalMsa = BigDecimal.valueOf(0);
		int contDealFiscal = 0;

		// recupera a lista de DealFiscal do ContratoPratica corrente
		List<DealFiscal> dealFiscalList = dealFiscalService
				.findDealFiscalByMsaAndActiveAndNotLogicalDelete(msa);

		// itera sobre a lista de DealFiscal, para cada um, verifica as
		// associacoes dos TipoServico e verifica se tem TaxaImposto cadastrada
		// para a Empresa (do DealFiscal corrente) e TipoServico
		Iterator<DealFiscal> itDealFiscal = dealFiscalList.iterator();
		while (itDealFiscal.hasNext()) {
			DealFiscal dealFiscal = itDealFiscal.next();

			// testa se o DealFiscal é ativo, se nao desconsidera o DealFiscal
			// corrente e vai para o próximo
			if (!Constants.ACTIVE.equals(dealFiscal.getIndicadorStatus())) {
				continue;
			}

			// variaveis
			BigDecimal totalDealFiscal = BigDecimal.valueOf(0);
			int contTipoServico = 0;

			// recupera a Empresa (do DealFiscal corrente)
			Empresa empresa = dealFiscal.getEmpresa();

			// recupera a lista de TipoServico (do DealFiscal corrente)
			List<TipoServico> tipoServicoList = dealFiscal.getTipoServicos();

			// itera sobre a lista de TipoServico e verifica a TaxaImposto (para
			// o TipoServico e Empresa correntes)
			for (TipoServico tipoServico : tipoServicoList) {
				// cria a variavel com as informacoes para serem filtradas
				TaxaImposto filter = new TaxaImposto(tipoServico, empresa);
				// busca a TaxaImposto conforme o filtro (filter)
				TaxaImposto taxaImposto = taxaImpostoService
						.findTaxaMaxStartDateByEmpTipServ(filter);

				// se existir TaxaImposto cadastrada para o filtro, soma no
				// total do DealFiscal
				if (taxaImposto != null) {
					/*
					 * totalDealFiscal = totalDealFiscal.add(taxaImposto
					 * .getValorTaxa());
					 */
					BigDecimal taxa = new BigDecimal(0);
					if (taxaImposto.getValorTaxaMunicipal() != null) {
						taxa = taxa.add(taxaImposto.getValorTaxaMunicipal());
					}
					if (taxaImposto.getValorTaxaFederal() != null) {
						taxa = taxa.add(taxaImposto.getValorTaxaFederal());
					}
					totalDealFiscal = totalDealFiscal.add(taxa);

					contTipoServico++;
				}
			}

			// caso quantidade de TipoServico calculados seja maior que zero, ou
			// seja, caso
			// o total do DealFiscal seja maior do que zero, calcula a
			// media dos totais dos DealFiscal e soma ao total do
			// ContratoPratica
			// - total DealFiscal div qtde TipoServico
			if (contTipoServico > 0) {
				BigDecimal mediaDealFiscal = totalDealFiscal.divide(
						BigDecimal.valueOf(contTipoServico), 2,
						BigDecimal.ROUND_HALF_UP);
				totalMsa = totalMsa.add(mediaDealFiscal);
				contDealFiscal++;
			}
		}

		// caso a quantidade de DealFiscal calculados seja maior do que zero,
		// pega o somatorio de todas as medias dos DealFiscal e calcula a media
		// do ContratoPratica
		// - total ContratoPratica div qtde DealFiscal
		if (contDealFiscal > 0) {
			BigDecimal mediaContratoPratica = totalMsa.divide(
					BigDecimal.valueOf(contDealFiscal), 2,
					BigDecimal.ROUND_HALF_UP);

			return mediaContratoPratica;
		} else {
			return BigDecimal.valueOf(0);
		}
	}

	/**
	 * Retorna uma busca rapida por parte do nome do msa.(AutoComplete)
	 * 
	 * @param name
	 *            - nome do msa
	 * @return listResult
	 */
	public List<Msa> findMsabyNameQuick(final String name) {
		return msaDao.findbyNameQuick(name);
	}

	/**
	 * Retorna uma busca nome do msa.
	 * 
	 * @param name
	 *            - nome do msa
	 * @return listResult
	 */
	public Msa findMsaByName(String name) {
		return msaDao.findByName(name);
	}


	/**
	 * Retorna um Msa relacionado a {@link CentroLucro}.
	 *
	 * @param centroLucro
	 * @return {@link Msa}
	 */
	@Override
	public List<Msa> findMsaByCentroLucro(CentroLucro centroLucro) {
		return msaDao.findByCentroLucro(centroLucro);
	}

	/**
	 * Metodo que realiza o upload da planilha de legal doc.
	 * @param uploadItem      - contem o valor do upload
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, List<String>> uploadLegalDoc(UploadItem uploadItem) throws Exception {

		PadraoArquivo padraoArq = new PadraoArquivo(1L, PADRAO_ARQUIVO_CSV);
		padraoArq.setTextoDelimitadorColuna(',');
		padraoArq.setTextoDelimitadorString('"');

		// Obtendo os dados do arquivo como um array de bytes
		byte[] data = uploadItem.getData();

		String csvData = new String(data, StandardCharsets.UTF_8);

		List<UploadMsaLegalDoc> uploadMsaLegalDocList = CsvUtil.getElementList(csvData, UploadMsaLegalDoc.class, CsvUtil.getCsvConfig(padraoArq));

		Map<String, List<String>> map = mapErrosUploadFile(uploadMsaLegalDocList);

		if (!hasKeyWithValue(map)) {
			saveMsaLegalDocData(uploadMsaLegalDocList);
		} else {
			return map;
		}
		return null;
	}

	@Override
	public Map<String, List<String>> mapErrosUploadFile(List<UploadMsaLegalDoc> uploadMsaLegalDocs) throws Exception {
		Map<String, List<String>> errorMap = new HashMap<>();

		for (String key : Constants.ERROR_KEYS) {
			errorMap.put(key, new ArrayList<String>());
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
		dateFormat.setLenient(false);

		validateUniqueCombinationsInCsv(uploadMsaLegalDocs, errorMap);
		for (UploadMsaLegalDoc uploadMsaLegalDoc : uploadMsaLegalDocs) {
			boolean isValid = true;
			validateUploadDoc(uploadMsaLegalDoc, errorMap, dateFormat, isValid);
		}

		return errorMap;
	}

	/**
	 * Validates the uploaded MSA legal document.
	 *
	 * @param doc        the uploaded MSA legal document to be validated
	 * @param errorMap   a map to store validation errors
	 * @param dateFormat the date format to be used for date validation
	 * @param isValid
	 */
	private void validateUploadDoc(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap, SimpleDateFormat dateFormat, boolean isValid) {
		validateProjectDescription(doc, errorMap);
		isValid = validateJiraCp(doc, errorMap) && isValid;
		validateType(doc, errorMap);
		validateFte(doc, errorMap);
		validateHasLimit(doc, errorMap);
		validateTotalContractAmount(doc, errorMap);
		validateTypeAmountExpenses(doc, errorMap);
		validateTotalAmountExpenses(doc, errorMap);
		validateStatus(doc, errorMap);
		isValid = validateStartDate(doc, errorMap, dateFormat) && isValid;
		isValid = validateEndDate(doc, errorMap, dateFormat) && isValid;
		validateTextoMesIpca(doc, errorMap);
		validateTipoServico(doc, errorMap);
		validateBase(doc, errorMap);
		validateCnpj(doc, errorMap);
		validateMsa(doc, errorMap, isValid);
	}

	private void validateUniqueCombinationsInCsv(List<UploadMsaLegalDoc> docs, Map<String, List<String>> errorMap) {
		Set<String> uniqueCombinations = new HashSet<>();
		final String DELIMITER = " | ";

		for (UploadMsaLegalDoc doc : docs) {

			String combination = doc.getMsa() +
					DELIMITER +
					doc.getJiraCp() +
					DELIMITER +
					doc.getJiraLegal() +
					DELIMITER +
					doc.getSow() +
					DELIMITER +
					doc.getPo() +
					DELIMITER +
					doc.getStartDate();

			if (uniqueCombinations.contains(combination)) {
				addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_UPLOAD_ALREADY_EXIST_ERROR),
						"Duplicate combination in CSV (Must be unique):  " + combination);
			} else {
				uniqueCombinations.add(combination);
			}
		}
	}


	/**
	 * Validates the project description of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 */
	private void validateProjectDescription(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if (isNullOrEmpty(doc.getProjectDescription())) {
			addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_PROJECT_DESCRIPTION_UPLOAD_ERROR), Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR);
		}
	}

	/**
	 * Validates the Jira CP field of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 * @return true if the Jira CP is valid, false otherwise
	 */
	private boolean validateJiraCp(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if (isNullOrEmpty(doc.getJiraCp())) {
			addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_JIRA_CP_UPLOAD_ERROR), Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR);
			return false;
		}
		return true;
	}

	/**
	 * Validates the type of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 */
	private void validateType(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if (isNullOrEmpty(doc.getType()) || msaTipoContratoService.findActiveByAcronimo(doc.getType()) == null) {
			addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_TYPE_UPLOAD_ERROR),
					isNullOrEmpty(doc.getType()) ? Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR : doc.getType());
		}
	}

	/**
	 * Validates the currency of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 */
	private void validateCurrency(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if (isNullOrEmpty(doc.getCurrency()) || moedaService.findMoedaByNameMsa(doc.getCurrency(), doc.getMsa()) == null) {
				addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_CURRENCY_UPLOAD_ERROR),
						isNullOrEmpty(doc.getCurrency()) ? Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR : doc.getCurrency());
			}

	}

	/**
	 * Validates the FTE (Full-Time Equivalent) of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 */
	private void validateFte(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if (isNullOrEmpty(doc.getFte()) || (!doc.getFte().equalsIgnoreCase("Y") && !doc.getFte().equalsIgnoreCase("N"))) {
			addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_FTE_UPLOAD_ERROR),
					isNullOrEmpty(doc.getFte()) ? Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR : doc.getFte());
		}
	}

	/**
	 * Validates the "Has Limit" field of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 */
	private void validateHasLimit(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if ("Y".equalsIgnoreCase(doc.getFte())){
            if (isNullOrEmpty(doc.getHasLimit()) || (!doc.getHasLimit().equalsIgnoreCase("Y") && !doc.getHasLimit().equalsIgnoreCase("N"))) {
                addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_HAS_LIMIT_UPLOAD_ERROR),
                        isNullOrEmpty(doc.getHasLimit()) ? Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR : doc.getHasLimit());
            }
		}
	}

	private void validateTotalContractAmount(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if (!isNullOrEmpty(doc.getHasLimit()) && !isNullOrEmpty(doc.getFte())) {
			if (doc.getHasLimit().equalsIgnoreCase("Y") || doc.getFte().equalsIgnoreCase("N")) {
				if (isNullOrEmpty(doc.getTotalContractAmount()) || !NumberUtil.isValidAmount(doc.getTotalContractAmount())) {
					addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_TOTAL_CONTRACT_AMOUNT_UPLOAD_ERROR),
							isNullOrEmpty(doc.getTotalContractAmount()) ? Constants.MSA_LEGAL_DOC_UPLOAD_VALUE_ERROR : doc.getTotalContractAmount());
				}
			}
		}
	}

	/**
	 * Validates the type of amount expenses of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 */
	private void validateTypeAmountExpenses(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if (isNullOrEmpty(doc.getTypeAmountExpenses()) ||
				msaTipoMontanteDespesaService.findByName(doc.getTypeAmountExpenses()) == null) {
			addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_TYPE_AMOUNT_EXPENSES_UPLOAD_ERROR),
					isNullOrEmpty(doc.getTypeAmountExpenses()) ? Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR : doc.getTypeAmountExpenses());
		}
	}

	/**
	 * Validates the total amount expenses of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 */
	private void validateTotalAmountExpenses(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		String typeAmountExpenses = doc.getTypeAmountExpenses();

		if (typeAmountExpenses != null) {
			if (isPercentageType(typeAmountExpenses)) {
				validatePercentageExpenses(doc, errorMap);
			} else if (isAmountType(typeAmountExpenses)) {
				validateAmountExpenses(doc, errorMap);
			}
		}
	}

	/**
	 * Checks if the given type represents a percentage type.
	 *
	 * @param type the type to be checked
	 * @return true if the type is a percentage type, false otherwise
	 */
	private boolean isPercentageType(String type) {
		return type.equalsIgnoreCase("NF %") || type.equalsIgnoreCase("ND %");
	}

	/**
	 * Checks if the given type represents an amount type.
	 *
	 * @param type the type to be checked
	 * @return true if the type is an amount type, false otherwise
	 */
	private boolean isAmountType(String type) {
		return type.equalsIgnoreCase("ND $") || type.equalsIgnoreCase("NF $");
	}

	/**
	 * Validates the percentage expenses of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 */
	private void validatePercentageExpenses(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if (isNullOrEmpty(doc.getPercentExpenses())) {
			addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_PERCENT_EXPENSES_UPLOAD_ERROR), Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR);
		} else if (!NumberUtil.isValidPercentage(doc.getPercentExpenses())) {
			addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_PERCENT_EXPENSES_UPLOAD_ERROR), doc.getPercentExpenses());
		}
	}

	/**
	 * Validates the amount expenses of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 */
	private void validateAmountExpenses(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if (isNullOrEmpty(doc.getTotalAmountExpenses()) || !NumberUtil.isValidAmount(doc.getTotalAmountExpenses())) {
			addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_TOTAL_AMOUNT_EXPENSES_UPLOAD_ERROR),
					isNullOrEmpty(doc.getTotalAmountExpenses()) ? Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR : doc.getTotalAmountExpenses());
		}
	}

	/**
	 * Validates the status of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 */
	private void validateStatus(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if (isNullOrEmpty(doc.getStatus()) || msaStatusContratoService.findByName(doc.getStatus()) == null) {
			addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_STATUS_UPLOAD_ERROR),
					isNullOrEmpty(doc.getStatus()) ? Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR : doc.getStatus());
		}
	}

	/**
	 * Validates the start date of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 * @param dateFormat the date format to be used for date validation
	 * @return true if the start date is valid, false otherwise
	 */
	private boolean validateStartDate(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap, SimpleDateFormat dateFormat) {
		if (isNullOrEmpty(doc.getStartDate())) {
			addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_START_DATE_UPLOAD_ERROR), Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR);
			return false;
		} else {
			try {
				doc.setStartDateConverted(dateFormat.parse(doc.getStartDate()));
			} catch (ParseException e) {
				addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_START_DATE_UPLOAD_ERROR), doc.getStartDate());
				return false;
			}
		}
		return true;
	}

	/**
	 * Validates the end date of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 * @param dateFormat the date format to be used for date validation
	 * @return true if the end date is valid, false otherwise
	 */
	private boolean validateEndDate(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap, SimpleDateFormat dateFormat) {
		if (!isNullOrEmpty(doc.getIndeterminate())){
			if (isNullOrEmpty(doc.getEndDate()) && doc.getIndeterminate().equals("N")) {
				addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_END_DATE_UPLOAD_ERROR), Constants.MSA_LEGAL_DOC_UPLOAD_NOT_INDETERMINATE_ERROR);
				return false;
			} else if (!isNullOrEmpty(doc.getEndDate()) && doc.getIndeterminate().equals("Y")) {
				addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_END_DATE_UPLOAD_ERROR), Constants.MSA_LEGAL_DOC_UPLOAD_INDETERMINATE_DATE_ERROR);
				return false;
			}
		}
		 if (!isNullOrEmpty(doc.getEndDate()) && doc.getStartDateConverted() != null) {
			try {
				Date endDate = dateFormat.parse(doc.getEndDate());
				if (endDate.before(doc.getStartDateConverted())) {
					addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_END_DATE_UPLOAD_ERROR), doc.getEndDate());
					return false;
				} else {
					doc.setEndDateConverted(endDate);
				}
			} catch (ParseException e) {
				addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_END_DATE_UPLOAD_ERROR), doc.getEndDate());
				return false;
			}
		}
		return true;
	}

	/**
	 * Validates the "Texto Mes IPCA" field of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 */
	private void validateTextoMesIpca(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if (isNullOrEmpty(doc.getTextoMesIpca()) || !getMonthsList().contains(doc.getTextoMesIpca().toUpperCase().trim()) && !doc.getTextoMesIpca().toUpperCase().trim().equals("N/A")) {
			addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_TEXTO_MES_IPCA_UPLOAD_ERROR),
					isNullOrEmpty(doc.getTextoMesIpca()) ? Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR : doc.getTextoMesIpca());
		}
	}

	/**
	 * Validates the MSA (Master Service Agreement) of the uploaded MSA legal document.
	 * Check if the registered currency is valid for the MSA and if it is a contract that respects the unique record in the data table.
	 *
	 * @param doc      the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 * @param isValid
	 */
	private void validateMsa(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap, boolean isValid) {
		if (isNullOrEmpty(doc.getMsa())) {
			addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_MSA_UPLOAD_ERROR), Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR);
			isValid = false;
		} else {
			Msa msa = findMsaByName(doc.getMsa());
			if (msa == null) {
				addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_MSA_UPLOAD_ERROR), doc.getMsa());
				isValid = false;
			} else if (isValid){
				validateCurrency(doc, errorMap);
				// Verificação de contrato único
				MsaContrato msaContrato = new MsaContrato();
				msaContrato.setMsa(msa);
				msaContrato.setJiraCp(deleteWhitespace(doc.getJiraCp()));
				msaContrato.setJiraLegal(deleteWhitespace(doc.getJiraLegal()));
				msaContrato.setSow(doc.getSow());
				msaContrato.setPo(doc.getPo());
				msaContrato.setDataInicial(doc.getStartDateConverted());
				msaContrato.setDataFinal(doc.getEndDateConverted());
				msaContrato.setDataIndeterminada("Y".equalsIgnoreCase(doc.getIndeterminate()));
				msaContrato.setDeleteLogico(false);

				if (Boolean.FALSE.equals(msaContratoService.validateUniqueContract(msaContrato, msa.getCodigoMsa()))) {
					addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_UPLOAD_ALREADY_EXIST_ERROR),
							 "Record already exists in the database: " + msa.getNomeMsa() + " | " + doc.getJiraCp() + " | " + doc.getJiraLegal() + " | " + doc.getStartDate());
				}
			}
		}
	}

	/**
	 * Validates the "Tipo Servico" field of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 */
	private void validateTipoServico(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if (doc.getTipoServico() == null || doc.getTipoServico().isEmpty()) {
			addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_TIPO_SERVICO_UPLOAD_ERROR), Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR);
		} else {
			for (String tipoServico : doc.getTipoServico()) {
				TipoServico servico = tipoServicoService.findTipoServicoByName(tipoServico.trim());
				if (servico == null) {
					addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_TIPO_SERVICO_UPLOAD_ERROR), tipoServico);
				} else {
					if (doc.getTipoServicoIds() == null) {
						doc.setTipoServicoIds(new HashSet<>());
					}
					doc.getTipoServicoIds().add(servico.getCodigoTipoServico());
				}
			}
		}
	}

	/**
	 * Validates the "Base" field of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 */
	private void validateBase(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if (doc.getBase() == null || doc.getBase().isEmpty()) {
			addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_BASE_UPLOAD_ERROR), Constants.MSA_LEGAL_DOC_UPLOAD_IS_REQUIRED_ERROR);
		} else {
			for (String base : doc.getBase()) {
				Empresa empresa = empresaService.findEmpresaByName(base.trim());
				if (empresa == null) {
					addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_BASE_UPLOAD_ERROR), base);
				} else {
					if (doc.getBaseIds() == null) {
						doc.setBaseIds(new HashSet<>());
					}
					doc.getBaseIds().add(empresa.getCodigoEmpresa());
				}
			}
		}
	}

	/**
	 * Validates the CNPJ (Cadastro Nacional da Pessoa Jurídica) of the uploaded MSA legal document.
	 *
	 * @param doc the uploaded MSA legal document to be validated
	 * @param errorMap a map to store validation errors
	 */
	private void validateCnpj(UploadMsaLegalDoc doc, Map<String, List<String>> errorMap) {
		if (doc.getCnpj() != null) {
			for (String cnpj : doc.getCnpj()) {
				if (!NumberUtil.isValidCnpj(cnpj.trim())) {
					addUnique(errorMap.get(Constants.MSA_LEGAL_DOC_CNPJ_UPLOAD_ERROR), cnpj);
				}
			}
		}
	}

	/**
	 * Checks if the given value is null or empty.
	 *
	 * @param value the value to be checked
	 * @return true if the value is null or empty, false otherwise
	 */
	private boolean isNullOrEmpty(String value) {
		return value == null || value.isEmpty();
	}

	/** Lista com String dos meses
	 *
	 * @return
	 */
	public static String getMonthsList() {
		return resourceBundle.getString(Constants.BUNDLE_KEY_MONTHS);
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

	@Override
	public void saveMsaLegalDocData(List<UploadMsaLegalDoc> uploadMsaLegalDocs){

		String loggedUser = LoginUtil.getLoggedUsername();

		for (UploadMsaLegalDoc uploadMsaLegalDoc : uploadMsaLegalDocs) {
			MsaContrato msaContrato = new MsaContrato();

			Msa msa = this.findMsaByName(uploadMsaLegalDoc.getMsa());
			msaContrato.setMsa(msa);

			msaContrato.setJiraCp(deleteWhitespace(uploadMsaLegalDoc.getJiraCp()));
			msaContrato.setJiraLegal(deleteWhitespace(uploadMsaLegalDoc.getJiraLegal()));
			msaContrato.setDescricao(uploadMsaLegalDoc.getProjectDescription());
			msaContrato.setSow(uploadMsaLegalDoc.getSow());
			msaContrato.setPo(uploadMsaLegalDoc.getPo());
			msaContrato.setComentarios(uploadMsaLegalDoc.getNotes());
			MsaTipoContrato tipoContrato = msaTipoContratoService.findActiveByAcronimo(uploadMsaLegalDoc.getType());
			msaContrato.setTipoContrato(tipoContrato);

			msaContrato.setMonthOfReadjustment(uploadMsaLegalDoc.getTextoMesIpca().toUpperCase().trim());

			Moeda moeda = moedaService.findMoedaByNameMsa(uploadMsaLegalDoc.getCurrency(), uploadMsaLegalDoc.getMsa());
			msaContrato.setMoeda(moeda);
			msaContrato.setDataInicial(uploadMsaLegalDoc.getStartDateConverted());

			if(uploadMsaLegalDoc.getEndDateConverted() != null){
			msaContrato.setDataFinal(uploadMsaLegalDoc.getEndDateConverted());}

			msaContrato.setDataIndeterminada("Y".equalsIgnoreCase(uploadMsaLegalDoc.getIndeterminate()));
			msaContrato.setFte("Y".equalsIgnoreCase(uploadMsaLegalDoc.getFte()));

			//If FTE false, has limit is false and save totalContractAmount
			if (Boolean.FALSE.equals(msaContrato.getFte())) {
				msaContrato.setHasLimit(Boolean.FALSE);
				msaContrato.setTotalContrato(Double.parseDouble(uploadMsaLegalDoc.getTotalContractAmount()));
			}else{ //if FTE True, check limit, if true, set totalContractAmount
				if(uploadMsaLegalDoc.getHasLimit().equalsIgnoreCase("Y")){
					msaContrato.setHasLimit(Boolean.TRUE);
					msaContrato.setTotalContrato(Double.parseDouble(uploadMsaLegalDoc.getTotalContractAmount()));
				}else{
					msaContrato.setHasLimit(Boolean.FALSE);
				}
			}

			MsaTipoMontanteDespesa tipoMontanteDespesa = msaTipoMontanteDespesaService.findByName(uploadMsaLegalDoc.getTypeAmountExpenses());
			msaContrato.setTipoDespesa(tipoMontanteDespesa);

			if (uploadMsaLegalDoc.getTotalAmountExpenses() != null) {
				msaContrato.setValorDespesa(Double.parseDouble(uploadMsaLegalDoc.getTotalAmountExpenses()));
			}
			if (uploadMsaLegalDoc.getPercentExpenses() != null) {
				msaContrato.setPorcentagemDespesa(Double.parseDouble(uploadMsaLegalDoc.getPercentExpenses()));
			}

			MsaStatusContrato statusContrato = msaStatusContratoService.findByName(uploadMsaLegalDoc.getStatus());
			msaContrato.setStatus(statusContrato);

			msaContrato.setLoginAtualizacao(loggedUser);
			msaContrato.setDataAtualizacao(new Date());
			msaContrato.setDeleteLogico(Boolean.FALSE);

			MsaContrato msaContratoPersisted = msaContratoService.save(msaContrato);

			for (Long tipoServicoId : uploadMsaLegalDoc.getTipoServicoIds()) {
				saveTipoServico(tipoServicoId, msaContratoPersisted.getCodigo());
			}

			for (Long baseId : uploadMsaLegalDoc.getBaseIds()) {
				saveBaseFilial(baseId, msaContratoPersisted.getCodigo());
			}

			if (uploadMsaLegalDoc.getCnpj() != null && !uploadMsaLegalDoc.getCnpj().isEmpty()) {
				for (String cnpj : uploadMsaLegalDoc.getCnpj()) {
					saveMsaContratoCnpj(cnpj.trim(), msaContratoPersisted);
				}
			}
		}

	}

	private void saveBaseFilial(Long baseId, Long msaContratoCode) {
		MsaContratoFilial filial = new MsaContratoFilial();
		filial.setCodigoMsaContrato(msaContratoCode);
		filial.setFilial(empresaService.findEmpresaById(baseId));
		msaContratoFilialService.create(filial);
	}

	private void saveTipoServico(Long tipoServico, Long msaContratoCode) {
		MsaContratoTipoServico msaContratoTipoServico = new MsaContratoTipoServico();
		msaContratoTipoServico.setCodigoMsaContrato(msaContratoCode);
		msaContratoTipoServico.setTipoServico(tipoServicoService.findById(tipoServico));
		msaContratoTipoServicoService.create(msaContratoTipoServico);
	}
	private void saveMsaContratoCnpj(String cnpj, MsaContrato msaContrato) {

			MsaContratoCnpj novoCnpj = new MsaContratoCnpj();
			novoCnpj.setMsaContrato(msaContrato);
			novoCnpj.setMsaContratoCnpj(cnpj);
			msaContratoCnpjService.save(novoCnpj);

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