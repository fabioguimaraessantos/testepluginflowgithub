package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IDealFiscalService;
import com.ciandt.pms.business.service.IPaymentConditionDealFiscalService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.PaymentConditionDealFiscal;
import com.ciandt.pms.persistence.dao.IPaymentConditionDealFiscalDao;
import com.ciandt.pms.util.LoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class PaymentConditionDealFiscalService implements IPaymentConditionDealFiscalService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IPaymentConditionDealFiscalDao dao;

    @Autowired
    private IDealFiscalService dealFiscalService;

    @Override
    public List<PaymentConditionDealFiscal> findByDealFiscal(final Long dealFiscalCode) {
        return dao.findByDealFiscal(dealFiscalCode);
    }

    @Override
    public PaymentConditionDealFiscal findActualPaymentCondition(final Long dealFiscalCode) {
        return dao.findActualPaymentCondition(dealFiscalCode);
    }

    @Override
    @Transactional
    public void saveByDealFiscal(final Long dealFiscalCode, String paymentConditionName) {
        if (paymentConditionName != "") {
            PaymentConditionDealFiscal paymentConditionDealFiscal = this.findActualPaymentCondition(dealFiscalCode);

            if (paymentConditionDealFiscal != null) {
                if (this.isSamePaymentCondition(paymentConditionName, paymentConditionDealFiscal.getPaymentConditionName())) {
                    Messages.showError("saveByDealFiscal",
                            Constants.DFCP_SAME_CONDITION);

                    return;
                } else {
                    if (this.isCurrentMonth(paymentConditionDealFiscal.getInitialDate())) {
                        paymentConditionDealFiscal.setFinalDate(this.getLastDayOfThisMonth());
                    }
                    else {
                        paymentConditionDealFiscal.setFinalDate(this.getLastDayOfLastMonth());
                    }
                    this.update(paymentConditionDealFiscal);
                    this.create(this.preparePaymentConditionDealFiscal(dealFiscalCode, paymentConditionName));
                }
            } else {
                this.create(this.preparePaymentConditionDealFiscal(dealFiscalCode, paymentConditionName));
            }
        }
    }

    @Override
    @Transactional
    public void create(final PaymentConditionDealFiscal paymentConditionDealFiscal) {
        dao.create(paymentConditionDealFiscal);
    }

    @Override
    @Transactional
    public void update(final PaymentConditionDealFiscal paymentConditionDealFiscal) {

        dao.update(paymentConditionDealFiscal);
    }

    private Boolean isSamePaymentCondition(String actualPaymentCondition, String savedPaymentCondition) {
        return actualPaymentCondition.equals(savedPaymentCondition);
    }

    private Boolean isCurrentMonth(Date dt) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(dt);
        int dtMonth = cal.get(Calendar.MONTH);
        cal.setTime(new Date());
        int actualMonth = cal.get(Calendar.MONTH);

        return dtMonth == actualMonth;

    }

    private Date getLastDayOfLastMonth(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date getLastDayOfThisMonth(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH,Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date getFirstDayOfThisMonth(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private PaymentConditionDealFiscal preparePaymentConditionDealFiscal(Long dealFiscalCode, String paymentConditionName){
        DealFiscal df = dealFiscalService.findDealFiscalById(dealFiscalCode);
        PaymentConditionDealFiscal paymentConditionDealFiscal = new PaymentConditionDealFiscal();
        paymentConditionDealFiscal.setDealFiscal(df);
        paymentConditionDealFiscal.setInitialDate(this.getFirstDayOfThisMonth());
        paymentConditionDealFiscal.setUpdatedAt(new Date());
        paymentConditionDealFiscal.setPaymentConditionName(paymentConditionName);
        paymentConditionDealFiscal.setLoginAuthor(LoginUtil.getLoggedUsername());

        return paymentConditionDealFiscal;
    }

    public PaymentConditionDealFiscal preparePaymentConditionDealFiscalToCreateDealFiscal(String paymentConditionName) {
        PaymentConditionDealFiscal paymentConditionDealFiscal = new PaymentConditionDealFiscal();
        paymentConditionDealFiscal.setDealFiscal(null);
        paymentConditionDealFiscal.setInitialDate(this.getFirstDayOfThisMonth());
        paymentConditionDealFiscal.setUpdatedAt(new Date());
        paymentConditionDealFiscal.setPaymentConditionName(paymentConditionName);
        paymentConditionDealFiscal.setLoginAuthor(LoginUtil.getLoggedUsername());
        return paymentConditionDealFiscal;
    }

}
