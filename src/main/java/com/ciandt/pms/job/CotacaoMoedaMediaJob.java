package com.ciandt.pms.job;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICotacaoMoedaMediaService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.model.CotacaoMoedaMedia;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.JobUtil;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 
 * @author luizsj
 * 
 */
public class CotacaoMoedaMediaJob {

	private Logger logger = LoggerFactory.getLogger(CotacaoMoedaMediaJob.class);

	@Autowired
	private JobUtil jobUtil;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** Instancia do servico de documentoLegal. */
	@Autowired
	private ICotacaoMoedaMediaService cotacaoMoedaMediaService;
	
	/** Instancia do servico de moeda. */
	@Autowired
	private IMoedaService moedaService;


    public void syncCotacao() {

        if (!jobUtil.isJobActive(Constants.JOB_COTACAO_MOEDA_MEDIA_ACTIVE)) {
            logger.warn("JOB", "Job is not active on config.properties. Set the "
                    .concat(Constants.JOB_COTACAO_MOEDA_MEDIA_ACTIVE)
                    .concat(" property to 'true' for active it."));
            return;
        }

    	logger.info("JOB", "Executando Job CotacaoMoedaMediaJob.syncCotacao - " + new Date());

    	HttpClient client = HttpClientBuilder.create().build();

    	List<Moeda> allMoedas = moedaService.findMoedaAll();
    	for (Moeda moedaDe : allMoedas) {
    		List<Moeda> allMoedasPara = moedaService.findMoedaAll();

    		for (Moeda moedaPara : allMoedasPara) {

		    	Calendar calendar = DateUtil.getCalendar(1, 0, 2015);
		    	Date lastDate = cotacaoMoedaMediaService.findLastByMoedaDeAndMoedaPara(moedaDe, moedaPara);
		    	if (lastDate != null) {
		    		calendar.setTime(lastDate);
		    		calendar.add(Calendar.DAY_OF_MONTH, -2);
				}

		    	while (calendar.getTime().before(new Date())) {
		    		try {
						cotacaoMoedaMediaService.syncCotacaoMoedaMediaByDate(moedaDe, moedaPara, calendar.getTime(), client);
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					calendar.add(Calendar.DATE, 1);
    			}
    		}
		}
		logger.info("JOB", "Fim Job CotacaoMoedaMediaJob.syncCotacao - " + new Date());
    }

    /*
        This method assumes that all values are correct in the database and recalculates the averages for all averages.
     */
    public void calculateAllCurrencyAverages() {

        if (!jobUtil.isJobActive(Constants.JOB_CALCULATE_ALL_CURRENCY_AVERAGES_ACTIVE)) {
            logger.warn("JOB", "Job is not active on config.properties. Set the "
                    .concat(Constants.JOB_CALCULATE_ALL_CURRENCY_AVERAGES_ACTIVE)
                    .concat(" property to 'true' for active it."));
            return;
        }

        List<Moeda> allMoedasDe = moedaService.findMoedaAll();
        for (Moeda moedaDe : allMoedasDe) {
            List<Moeda> allMoedasPara = moedaService.findMoedaAll();
            for (Moeda moedaPara : allMoedasPara) {

                Calendar calendar = DateUtil.getCalendar(1, 0, 2015);
                Date lastDate = cotacaoMoedaMediaService.findLastByMoedaDeAndMoedaPara(moedaDe, moedaPara);

                while (calendar.getTime().before(lastDate)) {
                    CotacaoMoedaMedia filter = new CotacaoMoedaMedia();
                    filter.setMoedaDe(moedaDe);
                    filter.setMoedaPara(moedaPara);
                    CotacaoMoedaMedia cmm = null;
                    try {
                        cmm = cotacaoMoedaMediaService.findByFilter(filter, calendar.getTime(), calendar.getTime()).get(0);
                    } catch (Exception e) {
                        System.out.println("error: " + e.getMessage());
                    }

                    Date firstDayMonth = DateUtil.getDateFirstDayOfMonth(calendar.getTime());
                    Date FistDayYear = DateUtil.getDateFirstDayOfYear(calendar.getTime());
                    cmm.setValorMediaMes(BigDecimal.valueOf(cotacaoMoedaMediaService.getMeanByCurrencyAndPeriod(firstDayMonth, calendar.getTime(), moedaDe, moedaPara)));
                    cmm.setValorMediaAno(BigDecimal.valueOf(cotacaoMoedaMediaService.getMeanByCurrencyAndPeriod(FistDayYear, calendar.getTime(), moedaDe, moedaPara)));
                    cotacaoMoedaMediaService.update(cmm);

                    calendar.add(Calendar.DATE, 1);
                }
            }
        }
    }
}
