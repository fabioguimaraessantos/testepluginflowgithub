package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICotacaoMoedaMediaService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.ExchangeRateException;
import com.ciandt.pms.model.CotacaoMoedaMedia;
import com.ciandt.pms.model.CotacaoMoedaMediaAud;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.persistence.dao.ICotacaoMoedaMediaAudDao;
import com.ciandt.pms.persistence.dao.ICotacaoMoedaMediaDao;
import com.ciandt.pms.persistence.dao.jpa.CotacaoMoedaMediaAudDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.NumberUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 
 * A classe CotacaoMoedaMediaService proporciona as funcionalidades de serviço para a
 * entidade CotacaoMoedaMedia.
 * 
 * @since 15/03/2010
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Service
public class CotacaoMoedaMediaService implements ICotacaoMoedaMediaService {

    @Autowired
    private ICotacaoMoedaMediaAudDao iCotacaoMoedaMediaAudDao;

    /** DAO da entidade CotacaoMoedaMedia. */
    @Autowired
    private ICotacaoMoedaMediaDao dao;
    
    /** DAO da entidade Moeda. */
    @Autowired
    private IMoedaService moedaService;

    @Autowired
    private IModuloService moduloService;

    /**
     * Cria a {@link CotacaoMoedaMedia}
     * 
     * @param cotacaoMoedaMedia
     */
    @Transactional
    private void create(final CotacaoMoedaMedia cotacaoMoedaMedia) {
    	dao.create(cotacaoMoedaMedia);
    }
    
    /**
     * Atualiza a {@link CotacaoMoedaMedia}
     * 
     * @param cotacaoMoedaMedia
     */
    @Override
    @Transactional
    public void update(final CotacaoMoedaMedia cotacaoMoedaMedia) {
    	dao.update(cotacaoMoedaMedia);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public CotacaoMoedaMedia findCotacaoMoedaMediaById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Busca pela Moeda e Periodo.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * @param dataDiaBeg
     *            Data inicio referente ao dia da cotação.
     * @param dataDiaEnd
     *            Data final referente ao dia da cotação.
     * 
     * @return retorna uma lista de CotacaoMoedaMedia.
     */
    public List<CotacaoMoedaMedia> findByFilter(
            final CotacaoMoedaMedia filter, final Date dataDiaBeg,
            final Date dataDiaEnd) {
        return dao.findByFilter(filter, dataDiaBeg, dataDiaEnd);
    }

    /**
     * Busca a ultima cotação do mes de uma moeda.
     * 
     * @param moeda
     *            entidade do tipo Moeda.
     * @param dateMonth
     *            Data-mes referente a cotação.
     * 
     * @return retorna a ultimka cotação do mês, caso não exista nenhuma retorna
     *         null.
     */
    private CotacaoMoedaMedia findLastByMonth(final Moeda moeda, final Date dateMonth) {

        return dao.findLastByMonth(moeda, dateMonth);
    }

    /**
     * Pega a cotação do mes. É considerado a cotação do mês a última cotação
     * registrada no mês.
     * 
     * @param startDate
     *            - Moeda que se deseja saber a cotação
     * @param moedaDe
     *            data-mes que se deseja saber a cotação.
     * 
     * @return retorna uma CotacaoMoedaMedia do mes
     */
	public List<CotacaoMoedaMedia> findByDeAndParaAndPeriod(final Date startDate,
			final Date endDate, final Moeda moedaDe, final Moeda moedaPara) {
        return dao.findByDeAndParaAndPeriodAndNovaCotacao(startDate, endDate, moedaDe, moedaPara, null);
    }
	
	/**
	 * Pega a cotação do mes. É considerado a cotação do mês a última cotação
	 * registrada no mês.
	 * 
	 * @param moedaDe
	 *            - Moeda que se deseja saber a cotação
	 * @param startDate
	 *            data-mes que se deseja saber a cotação.
	 * 
	 * @return retorna uma CotacaoMoedaMedia do mes
	 */
	private List<CotacaoMoedaMedia> findByDeAndParaAndPeriodAndNovaCotacao(final Date startDate,
			final Date endDate, final Moeda moedaDe, final Moeda moedaPara, final Boolean isNovaCotacao) {
		return dao.findByDeAndParaAndPeriodAndNovaCotacao(startDate, endDate, moedaDe, moedaPara, isNovaCotacao);
	}

    /**
     * Busca a cotação moeda da data passada por parametro.
     * 
     * @param moeda
     *            - moeda em questão
     * @param date
     *            - data em questão
     * 
     * @return retorna a cotação.
     */
    public CotacaoMoedaMedia findCotacaoMoedaMediaByMoedaMes(final Moeda moeda,
            final Date date) {
        return dao.findByMoedaMesAndTipo(moeda, date,
                Constants.COTACAO_MOEDA_TIPO_REAL);
    }

    /**
     * Busca uma entidade cadastrada no ultimo dia do mes.
     * 
     * @param moeda
     *            - Moeda corrente.
     * @param dataMes
     *            - data do fechamento.
     * 
     * @return a entidade cadastrada no ultimo dia do mes.
     */
    public CotacaoMoedaMedia findCotMoedaByMoedaAndLastDayMonth(final Moeda moeda,
            final Date dataMes) {
        return dao.findByMoedaAndLastDayMonth(moeda, dataMes);
    }

    private String getCurrencyRatePage(Moeda moedaDe, Moeda moedaPara, Date date, HttpClient client) throws IOException {

    	SimpleDateFormat formatBr = new SimpleDateFormat("dd/MM/yyyy");
    	String responseAsString = null;

        try {
            HttpGet request = new HttpGet("https://ptax.bcb.gov.br/ptax_internet/conversaoDeMoedas.do?method=realizarConversaoMoedas&moedaX=" + moedaPara.getCodigoBancoCentral() + "&moedaY=" + moedaDe.getCodigoBancoCentral() + "&valorConverter=1,00&dataCotacao=" + formatBr.format(date));
            HttpResponse resp = client.execute(request);
            responseAsString = EntityUtils.toString(resp.getEntity());
        } catch (HttpHostConnectException e) {
            System.out.println(date + " - " + moedaDe.getSiglaMoeda() + "/" + moedaPara.getSiglaMoeda() + ": " + e.getMessage());

            try {
				Thread.sleep(4000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

            this.getCurrencyRatePage(moedaDe, moedaPara, date, client);
        } catch (ClientProtocolException e) {
			e.printStackTrace();
		}

        try {
            Thread.sleep(4000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        return responseAsString;
    }

    @Override
    @Transactional
    public void syncCotacaoMoedaMediaByDate(Moeda moedaDe, Moeda moedaPara, Date date, HttpClient client)
    		throws IOException, ParseException {

    	SimpleDateFormat formatBr = new SimpleDateFormat("dd/MM/yyyy");

        Date usedRateDate = null;
        CotacaoMoedaMedia ccm = null;
        Double value = 999.999;

        if (!moedaDe.equals(moedaPara)) {

            Pattern currencyLinePattern = Pattern.compile(moedaDe.getSiglaMoeda() + ".*" + moedaPara.getSiglaMoeda());
            Pattern currencyNumberPattern = Pattern.compile("\\d*\\,\\d+");
            Pattern datePattern = Pattern.compile("[0-9][0-9]\\/[0-9][0-9]\\/[0-9][0-9][0-9][0-9]");

            String responseAsString = this.getCurrencyRatePage(moedaDe, moedaPara, date, client);

	        String[] responseLines = responseAsString.split("\n");
	        for (int i = 0; i < responseLines.length; i++) {
	            if (usedRateDate == null) {
	                Matcher lineMatcher = datePattern.matcher(responseLines[i]);
	
	                if (lineMatcher.find()) {
	                    usedRateDate = formatBr.parse(lineMatcher.group());
	                }
	            }
	
	            Matcher lineMatcher = currencyLinePattern.matcher(responseLines[i]);
	            if (lineMatcher.find()) {
	                Matcher currencyMatcher = currencyNumberPattern.matcher(responseLines[i]);
	
	                if (currencyMatcher.find()) {
	                    NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);
	                    Number number = numberFormat.parse(currencyMatcher.group());
	                    value = number.doubleValue();
	                }
	            }
	        }

        } else {
            value = 1.0;
            usedRateDate = date;
        }

        if (value != null) {

            ccm = new CotacaoMoedaMedia();
            ccm.setDataCotacao(date);
            ccm.setMoedaDe(moedaDe);

            ccm.setMoedaPara(moedaPara);
            ccm.setValorCotacaoVenda(BigDecimal.valueOf(value));

            Boolean considerInAverage = true;
            String isNovaCotacao = "Y";
            if (!date.equals(usedRateDate)
                    && (moedaDe.getSiglaMoeda().equals(Constants.SIGLA_MOEDA_REAL)
                        || moedaPara.getSiglaMoeda().equals(Constants.SIGLA_MOEDA_REAL))) {
                considerInAverage = false;
                isNovaCotacao = "N";
            }
            Date startDateMonth = DateUtil.getDateFirstDayOfMonth(date);
            Date endDateMonth = DateUtil.sumDays(-1L, date);
            Double meanMonth = this.getMeanWithValue(value, startDateMonth, endDateMonth, moedaDe, moedaPara, considerInAverage);
            ccm.setValorMediaMes(BigDecimal.valueOf(meanMonth));

            Date startDateYear = DateUtil.getDateFirstDayOfYear(date);
            Date endDateYear = DateUtil.sumDays(-1L, date);
            Double meanYear = this.getMeanWithValue(value, startDateYear, endDateYear, moedaDe, moedaPara, considerInAverage);
            ccm.setValorMediaAno(BigDecimal.valueOf(meanYear));
            ccm.setIndicadorNovaCotacao(isNovaCotacao);

            List<CotacaoMoedaMedia> cotacaoMoedaMedias = this.findByFilter(ccm, date, date);
            if (cotacaoMoedaMedias != null) {
                CotacaoMoedaMedia ccmUpdate = cotacaoMoedaMedias.get(0);
                ccmUpdate.setValorCotacaoVenda(BigDecimal.valueOf(value));
                ccmUpdate.setValorMediaMes(BigDecimal.valueOf(meanMonth));
                ccmUpdate.setValorMediaAno(BigDecimal.valueOf(meanYear));
                ccmUpdate.setIndicadorNovaCotacao(isNovaCotacao);

                this.update(ccmUpdate);
            } else {

                this.create(ccm);
            }
        }
    }

	public Double getMeanWithValue(Double value, Date startDate, Date endDate,
			Moeda moedaDe, Moeda moedaPara, Boolean considerInAverage) {

        Double meanDouble = 0.0;

    	List<CotacaoMoedaMedia> cotacoes = this.findByDeAndParaAndPeriodAndNovaCotacao(startDate, endDate, moedaDe, moedaPara, true);
        if (considerInAverage) {
            CotacaoMoedaMedia cmm = new CotacaoMoedaMedia(value);
            cotacoes.add(cmm);
        }

    	for (CotacaoMoedaMedia cotacaoMoedaMedia : cotacoes) {
			meanDouble += cotacaoMoedaMedia.getValorCotacaoVenda().doubleValue();
		}

    	if (cotacoes.size() > 0) {
    		return NumberUtil.round(meanDouble / (cotacoes.size()), 7);
		}

    	return NumberUtil.round(meanDouble, 7);
    }

	public Double getMeanByCurrencyAndPeriod(Date startDate, Date endDate, Moeda moedaDe, Moeda moedaPara) {
        Double meanDouble = 0.0;
        List<CotacaoMoedaMedia> cotacoes = this.findByDeAndParaAndPeriodAndNovaCotacao(startDate, endDate, moedaDe, moedaPara, true);
        for (CotacaoMoedaMedia cotacaoMoedaMedia : cotacoes) {
            meanDouble += cotacaoMoedaMedia.getValorCotacaoVenda().doubleValue();
        }

        if (cotacoes.size() > 0) {
            return NumberUtil.round(meanDouble / (cotacoes.size()), 7);
        }

        return meanDouble;
    }

	public String getGenericResponseContent(final HttpResponse resp)
			throws IllegalStateException, IOException {

		BufferedReader rd = new BufferedReader(
				new InputStreamReader(resp.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		return result.toString();
	}

	public Date findLast() {
		return dao.findLast();
	}

	@Override
	public Date findLastByMoedaDeAndMoedaPara(final Moeda moedaDe, final Moeda moedaPara) {
		return dao.findLastByMoedaDeAndMoedaPara(moedaDe, moedaPara);
	}

    @Override
    public List<CotacaoMoedaMedia> findByDate(Date date) {
        return dao.findByDate(date);
    }

    @Override
    @Transactional
    public void replace(Date from, Date until, String password, String ticket) throws ExchangeRateException {

        if (from.equals(until)) {
            throw new ExchangeRateException("Cannot copy to the same date", Constants.MSG_EXCHANGE_COPY_SAME_DATE);
        }

        Boolean isMonthAfterClosing = moduloService.dateAfterHistoryLock(until);
        if (!isMonthAfterClosing) {
            throw new ExchangeRateException("Cannot copy to closed month. Please, verify closing date (History Lock).", Constants.MSG_EXCHANGE_COPY_CLOSED_MONTH);
        }

        List<CotacaoMoedaMedia> exchangesToCopy = this.findByDate(from);
        if (exchangesToCopy == null || exchangesToCopy.isEmpty()) {
            throw new ExchangeRateException(String.format("No exchanges found at %s", from),
                    Constants.MSG_NO_EXCHANGE_RATE_TO_COPY);
        }

        long differenceInDays = DateUtil.getDifferenceInDays(from, until);
        for (int i = 1; i <= differenceInDays; i++) {
            Date date = DateUtil.addDays(new Date(from.getTime()), i);
            this.deleteByDate(date);
            this.copyToDate(exchangesToCopy, date);
        }

        logAudit(Constants.LABEL_REPLACE, from, until, password, ticket);
    }

    @Transactional
    private void copyToDate(List<CotacaoMoedaMedia> exchangesToCopy, Date date) {
        List<CotacaoMoedaMedia> toInsert = new ArrayList<>();
        for (CotacaoMoedaMedia toCopy : exchangesToCopy) {
            toInsert.add(CotacaoMoedaMedia.copyToDate(date, toCopy));
        }
        dao.create(toInsert);
    }

    @Override
    @Transactional
    public void deleteByDate(Date date) {
        dao.deleteByDate(date);
    }

    /**
     * Log de auditoria.
     * @param operation
     * @param from
     * @param until
     * @param login
     * @param ticket
     */
    private void logAudit(String operation, Date from, Date until, String login, String ticket) {
        String userSession = getCurrentUser();
        CotacaoMoedaMediaAud auditLog = new CotacaoMoedaMediaAud(
                null,
                operation,
                new Date(),
                from,
                until,
                userSession,
                login,
                ticket
        );
        iCotacaoMoedaMediaAudDao.create(auditLog);
    }

    /**
     * Retorna o usuário logado.
     * @return
     */
    public String getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    @Override
    @Transactional
    public void delete(Date from, Date until, String password, String ticket)  {
        long differenceInDays = DateUtil.getDifferenceInDays(from, until);
        for (int i = 0; i <= differenceInDays; i++) {
            Date date = DateUtil.addDays(new Date(from.getTime()), i);
            this.deleteByDate(date);
        }
        logAudit(Constants.LABEL_DELETE, from, until, password, ticket);
    }
}