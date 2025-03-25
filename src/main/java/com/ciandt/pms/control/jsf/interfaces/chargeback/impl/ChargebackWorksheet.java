package com.ciandt.pms.control.jsf.interfaces.chargeback.impl;

import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.control.jsf.interfaces.chargeback.IChargebackWorksheet;
import com.ciandt.pms.control.jsf.pojo.LoginChargebackPojo;
import com.ciandt.pms.model.Pessoa;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.OutputStream;
import java.util.*;

public abstract class ChargebackWorksheet implements IChargebackWorksheet {

    /* Logger */
    private static Logger logger = LogManager.getLogger(ChargebackWorksheet.class.getName());

    private final String ERROR_LOGIN_DUPLICATED = "Duplicated";
    private final String ERROR_LOGIN_NOT_FOUND = "Not Found";

    @Autowired
    private IPessoaService pessoaService;

    @Override
    public void createErrorLoginsWorksheet(List<LoginChargebackPojo> logins, OutputStream output) {

        try{
            if(logins != null && !logins.isEmpty()){
                HSSFWorkbook workbook = new HSSFWorkbook ();
                HSSFSheet sheet = workbook.createSheet("Incorrect Logins");

                int rowNum = 0;
                for (LoginChargebackPojo login : logins) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(login.getLogin());
                    row.createCell(1).setCellValue(login.getError());
                }

                workbook.write(output);
            }

        }catch (Exception e){
            logger.error("Error when trying to export incorrect logins to worksheet.", e);
        }
    }

    /**
     *
     * @param logins
     * @return
     */
    protected List<LoginChargebackPojo> createListLoginsChargeback(List<String> logins){

        if(logins == null)
            return Collections.emptyList();

        List<LoginChargebackPojo> list = new ArrayList<LoginChargebackPojo>();
        Set<String> set = new HashSet<String>();

        Long id = 1l;
        for (String login: logins) {

            if(StringUtils.isEmpty(login))
                continue;

            LoginChargebackPojo loginChargeback = new LoginChargebackPojo(id++, login, Boolean.TRUE, null);

            if(set.contains(login)){
                setIncorrect(loginChargeback, ERROR_LOGIN_DUPLICATED);
            }else{
                validPessoaByLogin(loginChargeback, login);
                set.add(login);
            }

            list.add(loginChargeback);
        }

        return list;
    }

    /**
     *
     * @param rowIterator
     * @param logins
     */
    protected void readFromWorksheet(Iterator<Row> rowIterator, List<String> logins){
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {

                Cell cell = cellIterator.next();
                logins.add(cell.getStringCellValue());
            }
        }
    }

    /**
     *
     * @param loginChargeback
     * @param error
     */
    private void setIncorrect(LoginChargebackPojo loginChargeback, String error){
        loginChargeback.setCorrect(Boolean.FALSE);
        loginChargeback.setError(error);
    }

    /**
     *
     * @param loginChargeback
     * @param login
     */
    private void validPessoaByLogin(LoginChargebackPojo loginChargeback, String login){
        Pessoa pessoa = pessoaService.findPessoaByLogin(login);
        if(pessoa == null)
            setIncorrect(loginChargeback, ERROR_LOGIN_NOT_FOUND);
    }
}
