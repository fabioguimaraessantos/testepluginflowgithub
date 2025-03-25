package com.ciandt.pms.business.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IReceitaService;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaCentroLucro;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.util.MailSenderUtil;

/**
 * A classe AbstractGestaoReajusteService proporciona as funcionalidades de
 * serviço referente a entidade ControleReajuste e DocumentoLegal.
 * 
 * @since 29/01/2014
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */

public class AbstractGestaoReajusteService {

	/** Arquivo de configuracoes. */
	@Autowired
	protected Properties systemProperties;

	/** Instancia do Servico da entidade {@link ReceitaService}. */
	@Autowired
	private IReceitaService receitaService;

	/** Instancia do Servico da entidade {@link PessoaService}. */
	@Autowired
	private IPessoaService pessoaService;

	/** Interface do Servico da entidade {@link MailSenderUtil}. */
	@Autowired
	protected MailSenderUtil mailSender;

	/** Separador de emails */
	public final String SEPARADOR_EMAIL = ", ";

	/** Pattern data */
	public final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

	/**
	 * Monta String com os gestores
	 * 
	 * @param Lista
	 *            com os nomes dos gestores (GN)
	 * @return Ex: Gestor 1, Gestor 2 and Gestor 3
	 */
	protected String montaNomesGestores(final List<String> gestores) {

		StringBuilder gestoresAsString = new StringBuilder("");
		for (String nome : gestores) {

			gestoresAsString.append(nome);
//			gestoresAsString.append(SEPARADOR_EMAIL);
		}

		return gestoresAsString.toString();
	}

	/**
	 * Calcula a data que será usada para iniciar o envio dos reminders.
	 * 
	 * @param qtMesesInicioLembretes
	 *            Numero em meses
	 * @return Data para inicio dos envios dos reminders.
	 */
	protected Date getDataInicioEnvio(final Integer qtMesesInicioLembretes) {
		// Pega a data atual e adiciona q quantidade de meses meses a
		// data atual
		Calendar calInicioEnvio = Calendar.getInstance();
		calInicioEnvio.add(Calendar.MONTH, qtMesesInicioLembretes);

		return calInicioEnvio.getTime();
	}

	/**
	 * Calcula a data do ultimo envio, para não enviar mais de um e-mail com
	 * intervalo menor que o configurado
	 * 
	 * @param qtDiasEntreLembretes
	 *            Numero em dias
	 * @return Data do último envio dos lembretes. Remove 7 dias da data atual
	 *         (qt dias é configurado no config.properties)
	 */
	protected Date getDataUltimoEnvio(final Integer qtDiasEntreLembretes) {

		// Pega a data atual e subtrai os dias da frequencia dos reminders
		Calendar calUltimoEnvio = new GregorianCalendar();
		calUltimoEnvio.set(Calendar.DAY_OF_MONTH,
				calUltimoEnvio.get(Calendar.DAY_OF_MONTH)
						- qtDiasEntreLembretes);
		return calUltimoEnvio.getTime();
	}

}