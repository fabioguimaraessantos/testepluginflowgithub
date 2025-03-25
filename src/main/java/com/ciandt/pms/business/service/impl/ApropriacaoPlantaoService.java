package com.ciandt.pms.business.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.time.DateUtils;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IApropriacaoPlantaoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IUploadArquivoService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.ApropriacaoPlantao;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.persistence.dao.IApropriacaoPlantaoDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.UploadUtil;

/**
 * A classe ApropriacaoPlantaoService proporciona as funcionalidades da camada
 * de negócio referente a entidade {@link ApropriacaoPlantao}.
 * 
 * @since 22/10/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 */
@Service
public class ApropriacaoPlantaoService implements IApropriacaoPlantaoService {

	/** Instancia do Dao da entidade ApropriacaoPlantao. */
	@Autowired
	private IApropriacaoPlantaoDao apropriacaoPlantaoDao;

	/** Instancia do servico PessoaService. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instancia do servico Modulo. */
	@Autowired
	private IModuloService moduloService;

	/** Instancia do servico Moeda. */
	@Autowired
	private IMoedaService moedaService;

	/** Instancia do sevico {@link UploadArquivo}. */
	@Autowired
	private IUploadArquivoService uploadArquivoService;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	private static final String SEPARADOR = "; ";

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Override
	@Transactional
	public boolean createApropriacaoPlantao(final ApropriacaoPlantao entity) {

		boolean sucess = true;

		entity.setDataAtualizacao(new Date());

		entity.setDataMes(DateUtils.truncate(entity.getDataMes(),
				Calendar.MONTH));

		Date closingDate = moduloService.getClosingDateBancoHoras();
		if (DateUtil.before(entity.getDataMes(), closingDate)
				|| DateUtil.isSameDate(entity.getDataMes(), closingDate,
						Calendar.MONTH)) {
			// Se a data do plantão for anterior a data do closing date do
			// Módulo de horas extras e plantão, mostra mensagem de erro
			Messages.showError("createDutyHours",
					Constants.UPLOAD_MSG_ERROR_BEFORE_CLOSING_DATE);
			sucess = false;
		}

		// Consulta plantão por pessoa e MM/yyyy
		List<ApropriacaoPlantao> plantoes = apropriacaoPlantaoDao
				.findApropriacaoPlantaoByDataAndCdPessoa(entity.getDataMes(),
						entity.getPessoa());
		if (plantoes.size() > 0) {
			// Não pode inserir valor com mesmo login e MM/yyyy. Se existir
			// registro para pessoa e MM/yyyy, mostra mensagem de erro.
			Messages.showError("createDutyHours",
					Constants.UPLOAD_PLANTAO_HE_MSG_ERRO_REGISTRO_JA_EXISTENTE);
			sucess = false;
		}

		if (sucess) {

			apropriacaoPlantaoDao.create(entity);

			Messages.showSucess("createDutyHours",
					Constants.DEFAULT_MSG_SUCCESS_SAVE,
					Constants.ENTITY_NAME_APROPRIACAO_PANTAO);
		}
		return sucess;
	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que sera atualizada.
	 * 
	 */
	@Override
	@Transactional
	public void updateApropriacaoPlantao(final ApropriacaoPlantao entity) {
		apropriacaoPlantaoDao.update(entity);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que sera apagada.
	 */
	@Override
	@Transactional
	public void removeApropriacaoPlantao(final ApropriacaoPlantao entity) {
		apropriacaoPlantaoDao.remove(entity);
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	@Override
	public ApropriacaoPlantao findApropriacaoPlantaoById(final Long id) {
		return apropriacaoPlantaoDao.findById(id);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	public List<ApropriacaoPlantao> findApropriacaoPlantaoAllActive() {
		return apropriacaoPlantaoDao.findAll();
	}

	/**
	 * Checa se o objeto {@link ApropriacaoPlantao} contem erro (objeto obtido
	 * via upload).
	 * 
	 * @param apropriacaoPlantao
	 *            {@link ApropriacaoPlantao}
	 * @param mapLogins Map com logins
	 * @return String erro
	 */
	private String validateApropriacaoPlantaoUploadedItem(
			ApropriacaoPlantao apropriacaoPlantao,
			final Map<String, Date> mapLogins) {

		String login = apropriacaoPlantao.getLogin();
		StringBuilder srtErro = new StringBuilder("");

		if (apropriacaoPlantao.getDataMes() != null) {

			if (mapLogins.get(apropriacaoPlantao.getLogin()) != null
					&& mapLogins.get(apropriacaoPlantao.getLogin()).equals(
							DateUtils.truncate(apropriacaoPlantao.getDataMes(),
									Calendar.MONTH))) {
				srtErro.append(BundleUtil
						.getBundle(Constants.UPLOAD_PLANTAO_HE_MSG_ERRO_REGISTRO_DUPLICADO_NO_ARQUIVO));
				srtErro.append(SEPARADOR);
			}

			// Valida login
			if (login != null) {
				Pessoa pessoa = pessoaService.findPessoaByLogin(login);
				if (pessoa != null) {
					// Verifica se não existe registro para o login e data
					apropriacaoPlantao.setPessoa(pessoa);
					if (this.existsApropriacaoPlantao(apropriacaoPlantao)) {
						srtErro.append(BundleUtil
								.getBundle(Constants.UPLOAD_PLANTAO_HE_MSG_ERRO_REGISTRO_JA_EXISTENTE));
						srtErro.append(SEPARADOR);
					}
				} else {
					srtErro.append(BundleUtil
							.getBundle(Constants.UPLOAD_ERROR_INVALID_LOGIN));
					srtErro.append(SEPARADOR);
				}
			} else {
				srtErro.append(BundleUtil
						.getBundle(Constants.UPLOAD_MSG_ERROR_READING));
				srtErro.append(SEPARADOR);
			}

			// Valida se a data com a data de fechamento do banco de
			// horas/plantão
			if (!DateUtil.after(apropriacaoPlantao.getDataMes(),
					moduloService.getClosingDateBancoHoras())) {
				srtErro.append(BundleUtil
						.getBundle(Constants.UPLOAD_MSG_ERROR_BEFORE_CLOSING_DATE));
				srtErro.append(SEPARADOR);
			}
		} else {
			// Valida formato da data
			srtErro.append(BundleUtil
					.getBundle(Constants.UPLOAD_PLANTAO_HE_MSG_ERRO_FORMATO_DATA));
			srtErro.append(SEPARADOR);
		}

		// Valida a sigla da moeda
		Moeda moeda = moedaService.findMoedaByAcronym(apropriacaoPlantao
				.getSiglaMoeda());
		apropriacaoPlantao.setMoeda(moeda);

		if (apropriacaoPlantao.getSiglaMoeda() == null || moeda == null) {
			srtErro.append(BundleUtil
					.getBundle(
							Constants.UPLOAD_PLANTAO_HE_MSG_ERRO_SIGLA_MOEDA,
							apropriacaoPlantao.getSiglaMoeda() != null ? apropriacaoPlantao
									.getSiglaMoeda() : "null"));
			srtErro.append(SEPARADOR);
		}

		// Valida valor
		if (apropriacaoPlantao.getValorPlantao() == null) {
			srtErro.append(BundleUtil
					.getBundle(Constants.UPLOAD_PLANTAO_HE_MSG_ERRO_FORMATO_VALOR));
			srtErro.append(SEPARADOR);
		}

		if (srtErro.length() > 0) {
			// Retira o ", " do ultimo registro
			return srtErro.toString().substring(0, srtErro.length() - 2);
		}

		return srtErro.toString();
	}

	/**
	 * Configura um objeto do tipo {@link UploadArquivo}.
	 * 
	 * @param uploadItem
	 *            {@link UploadItem}
	 * @param padraoArq
	 *            {@link PadraoArquivo}
	 * @param apropriacaoPlantaos
	 *            lista contendo os itens (ApropriacaoPlantao)
	 * 
	 * @return {@link UploadArquivo}
	 */
	private UploadArquivo buildUploadArquivo(final UploadItem uploadItem,
			final PadraoArquivo padraoArq,
			final List<ApropriacaoPlantao> apropriacaoPlantaos) {

		UploadArquivo uploadArquivo = new UploadArquivo();
		List<ApropriacaoPlantao> validApropriacaoPlantaos = new ArrayList<ApropriacaoPlantao>();
		Map<String, Date> mapLogins = new HashMap<String, Date>();

		int count = 1;

		for (ApropriacaoPlantao apropriacaoPlantao : apropriacaoPlantaos) {
			String error = this.validateApropriacaoPlantaoUploadedItem(
					apropriacaoPlantao, mapLogins);

			if (error.isEmpty()) {
				Date dataMes = DateUtils.truncate(
						apropriacaoPlantao.getDataMes(), Calendar.MONTH);

				mapLogins.put(apropriacaoPlantao.getLogin(), dataMes);

				// trunca a data pelo mês, pois o dia não deve ser considerado
				apropriacaoPlantao.setDataMes(dataMes);
				apropriacaoPlantao.setUploadArquivo(uploadArquivo);

				// relaciona com o arquivo de upload.
				validApropriacaoPlantaos.add(apropriacaoPlantao);
			} else {
				uploadArquivo.addErrors("Line " + count + ": " + error);
			}

			count++;
		}

		String fileName = uploadItem.getFileName();
		uploadArquivo.setNomeArquivo(fileName);
		uploadArquivo.setCodigoAutor(LoginUtil.getLoggedUsername());
		uploadArquivo.setDataDataHoraUpload(new Date());
		uploadArquivo.setValorBytes(String.valueOf(uploadItem.getFileSize()));
		uploadArquivo.setDataDataHoraUpload(new Date());
		uploadArquivo.setSiglaTabelaAlvo("APPL");
		uploadArquivo.setTextoExtArquivo(fileName.substring(
				fileName.lastIndexOf('.') + 1, fileName.length()));
		uploadArquivo.setPadraoArquivo(padraoArq);
		uploadArquivo.setApropriacaoPlantaos(validApropriacaoPlantaos);

		return uploadArquivo;
	}

	/**
	 * Salva o upload do arquivo de plantao e salva.
	 * 
	 * @param uploadItem
	 *            - arquivo que foi "upado"
	 * 
	 * @param padraoArq
	 *            - padrão do arquivo.
	 * 
	 * @throws IOException
	 *             lança exceção caso não seja possível salvar o arquivo de
	 *             upload
	 * 
	 * @return {@link UploadArquivo}
	 */
	@Override
	public UploadArquivo uploadApropriacaoPlantao(final UploadItem uploadItem,
			final PadraoArquivo padraoArq) {

		List<ApropriacaoPlantao> apropriacaoPlantaos = UploadUtil
				.csvFileToElementList(uploadItem, padraoArq,
						ApropriacaoPlantao.class);

		return this.buildUploadArquivo(uploadItem, padraoArq,
				apropriacaoPlantaos);
	}

	/**
	 * Checa se a entidade existe na base de dados.
	 * 
	 * @param apropriacaoPlantao
	 * @return true caso a entidade já exista na base de dados
	 */
	@Override
	public boolean existsApropriacaoPlantao(
			ApropriacaoPlantao apropriacaoPlantao) {
		return apropriacaoPlantaoDao.exists(apropriacaoPlantao);
	}

	/**
	 * Salva o arquivo que foi "uppado".
	 * 
	 * @param uploadArquivo
	 *            arquivo a ser salvo
	 * @param data
	 *            bytes
	 * @throws IOException
	 */
	@Override
	@Transactional
	public void saveUploadFile(UploadArquivo uploadArquivo, byte[] data)
			throws IOException {

		uploadArquivoService.createUploadArquivo(uploadArquivo);
		String fileName = uploadArquivo.getCodigoUploadArquivo() + "-"
				+ uploadArquivo.getNomeArquivo();

		String path = (String) systemProperties
				.get(Constants.UPLOAD_PLANTAO_POR_VALOR_DESTINATION_PAHT);

		uploadArquivo.setTextoCaminho(path);
		uploadArquivo.setNomeArquivo(fileName);

		uploadArquivoService.updateUploadArquivo(uploadArquivo);
		FileOutputStream fos = new FileOutputStream(path + fileName);
		fos.write(data);
		fos.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IApropriacaoPlantaoService#
	 * findApropriacaoPlantaoByData(java.util.Date)
	 */
	public List<ApropriacaoPlantao> findApropriacaoPlantaoByData(
			final Date dataFechamento) {
		return apropriacaoPlantaoDao
				.findApropriacaoPlantaoByData(dataFechamento);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IApropriacaoPlantaoService#
	 * findApropriacaoPlantaoByDataAndCdPessoa(java.util.Date,
	 * com.ciandt.pms.model.Pessoa)
	 */
	public List<ApropriacaoPlantao> findApropriacaoPlantaoByDataAndCdPessoa(
			final Date dataFechamento, final Pessoa pessoa) {
		return apropriacaoPlantaoDao.findApropriacaoPlantaoByDataAndCdPessoa(
				dataFechamento, pessoa);
	}
}