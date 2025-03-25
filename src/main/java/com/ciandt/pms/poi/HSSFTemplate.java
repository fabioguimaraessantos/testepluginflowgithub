package com.ciandt.pms.poi;

import com.ciandt.pms.model.LicencaSwProjetoParcela;
import com.ciandt.pms.model.VwPmsContratoProfitCenter;
import com.ciandt.pms.model.vo.LicencaSwProjetoCell;
import com.ciandt.pms.model.vo.LicencaSwUserCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe abstrata para geracao de arquivos do tipo workbook/sheet.
 * 
 * @since 17/12/2015
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Component
public class HSSFTemplate {

	private XSSFWorkbook wb;

	private CellStyle headerStyle;
	private CellStyle borderedCellStyle;

	public HSSFTemplate() {
		wb = new XSSFWorkbook();
	}
	
    private CellStyle getHeaderStyle() {

        headerStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerStyle = createBorderedStyle(wb);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setFont(headerFont);

        return headerStyle;
    }

    private CellStyle getBorderedCellStyle() {
			
        borderedCellStyle = wb.createCellStyle();
        borderedCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        borderedCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        borderedCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        borderedCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        borderedCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        borderedCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        borderedCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        borderedCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        return borderedCellStyle;
    }
    
    private CellStyle getBorderedCellStyle(String dataFormat) {
    	CellStyle cellStyle = wb.createCellStyle();
    	cellStyle.cloneStyleFrom(this.getBorderedCellStyle());

    	DataFormat df = wb.createDataFormat();
    	cellStyle.setDataFormat(df.getFormat(dataFormat));
    	
    	return cellStyle;
    }

    public void getRevenueForecastReport(List<VwPmsContratoProfitCenter> contratoProfitCenters) throws IOException  {
		Sheet sheet = wb.createSheet("Forecast");

		// Header
        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(12.75f);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(this.getHeaderStyle());
        }

        Row row;
        Cell cell;
        int rownum = 1;
        for (int i = 0; i < contratoProfitCenters.size(); i++, rownum++) {
            row = sheet.createRow(rownum);
            if(contratoProfitCenters.get(i) == null) continue;

                cell = row.createCell(0);
                cell.setCellValue(contratoProfitCenters.get(i).getCliente());
                cell.setCellStyle(this.getBorderedCellStyle());

                cell = row.createCell(1);
                cell.setCellValue(contratoProfitCenters.get(i).getNomeContratoPratica());
                cell.setCellStyle(this.getBorderedCellStyle());

                cell = row.createCell(2);
                cell.setCellValue(contratoProfitCenters.get(i).getNomePratica());
                cell.setCellStyle(this.getBorderedCellStyle());
                
                cell = row.createCell(3);
                cell.setCellValue(contratoProfitCenters.get(i).getNomeUmkt());
                cell.setCellStyle(this.getBorderedCellStyle());
                
                cell = row.createCell(4);
                cell.setCellValue(contratoProfitCenters.get(i).getNomeSso());
                cell.setCellStyle(this.getBorderedCellStyle());
                
                cell = row.createCell(5);
                cell.setCellValue(contratoProfitCenters.get(i).getLoginBusinessManager());
                cell.setCellStyle(this.getBorderedCellStyle());
                
                cell = row.createCell(6);
                cell.setCellValue(contratoProfitCenters.get(i).getDtValor());
                cell.setCellStyle(this.getBorderedCellStyle("mmm-yyyy"));
                
                cell = row.createCell(7);
                cell.setCellValue(contratoProfitCenters.get(i).getSiglaMoeda());
                cell.setCellStyle(this.getBorderedCellStyle());

                cell = row.createCell(8);
                cell.setCellValue(contratoProfitCenters.get(i).getValorMapa());
                cell.setCellStyle(this.getBorderedCellStyle("#,#0.00"));

                cell = row.createCell(9);
                cell.setCellValue(contratoProfitCenters.get(i).getValorReceita());
                cell.setCellStyle(this.getBorderedCellStyle("#,#0.00"));

                cell = row.createCell(10);
                cell.setCellValue(contratoProfitCenters.get(i).getTipoReceita());
                cell.setCellStyle(this.getBorderedCellStyle());
        }

        for (int i = 0; i < titles.length; i++) {
        	sheet.autoSizeColumn(i);
        }

        String file = "RevenueForecast_" + new Date().getTime() + ".xlsx";
        
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + file);

        wb.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public XSSFWorkbook getProjectLicensesManagerNotificationReport(List<LicencaSwProjetoCell> licencaSwProjetoCellList) {
	    wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("ProjectLicenses");

        // Header
        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(12.75f);
        for (int i = 0; i < projectLicensesManagerNotificationTitles.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(projectLicensesManagerNotificationTitles[i]);
            cell.setCellStyle(this.getHeaderStyle());
        }

        Row row;
        Cell cell;
        int rownum = 1;
        for (int i = 0; i < licencaSwProjetoCellList.size(); i++, rownum++) {
            row = sheet.createRow(rownum);
            if(licencaSwProjetoCellList.get(i) == null) continue;

            // Column INVOICE NUMBER
            cell = row.createCell(0);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getNotaFiscal());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column VENDOR
            cell = row.createCell(1);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getDescricaoLicenca());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column RESOURCE NAME
            cell = row.createCell(2);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getNomeLicenca());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column INSTALLMENTS BALANCE
            cell = row.createCell(3);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getSaldo());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column INSTALLMENTS AMOUNT
            cell = row.createCell(4);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getValorParcela().toString());
            cell.setCellStyle(this.getBorderedCellStyle("#,#0.00"));

            // Column START DATE
            cell = row.createCell(5);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getDataInicio());
            cell.setCellStyle(this.getBorderedCellStyle("mmm/yyyy"));

            // Column ID COST CENTER PMS
            cell = row.createCell(6);
            cell.setCellValue(null != licencaSwProjetoCellList.get(i).getCodigoCentroCustoErp() ? licencaSwProjetoCellList.get(i).getCodigoCentroCustoErp().toString() : "");
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column NAME COST CENTER PMS
            cell = row.createCell(7);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getNomeCentroCusto());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column ID PROJECT MEGA
            cell = row.createCell(8);
            cell.setCellValue(null != licencaSwProjetoCellList.get(i).getCodigoProjetoErp() ? licencaSwProjetoCellList.get(i).getCodigoProjetoErp().toString() : "");
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column NAME PROJECT MEGA
            cell = row.createCell(9);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getNomeProjetoErp());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column LOGINS
            cell = row.createCell(10);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getLogins());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column BM
            cell = row.createCell(11);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getBusinessManager());
            cell.setCellStyle(this.getBorderedCellStyle("#,#0.00"));

            // Column PM
            cell = row.createCell(12);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getProjectManager());
            cell.setCellStyle(this.getBorderedCellStyle());

        }

        for (int i = 0; i < titles.length; i++) {
            sheet.autoSizeColumn(i);
        }
        return wb;
    }

    public void getProjectLicensesReport(List<LicencaSwProjetoCell> licencaSwProjetoCellList) throws IOException {
	    final Integer QTD_PARCELAS_CURTO_PRAZO = 12;
        Sheet sheet = wb.createSheet("ProjectLicenses");

        // Header
        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(12.75f);
        for (int i = 0; i < projectLicensesTitles.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(projectLicensesTitles[i]);
            cell.setCellStyle(this.getHeaderStyle());
        }

        Row row;
        Cell cell;
        int rownum = 1;
        for (int i = 0; i < licencaSwProjetoCellList.size(); i++, rownum++) {
            row = sheet.createRow(rownum);
            if(licencaSwProjetoCellList.get(i) == null) continue;

            // Column MONTH (Column A)
            cell = row.createCell(0);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getMonth());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column INVOICE NUMBER (Column B)
            cell = row.createCell(1);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getNotaFiscal());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column VENDOR (Column C)
            cell = row.createCell(2);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getDescricaoLicenca());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column RESOURCE NAME (Column D)
            cell = row.createCell(3);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getNomeLicenca());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column START DATE (Column E)
            cell = row.createCell(4);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getDataInicio());
            cell.setCellStyle(this.getBorderedCellStyle("mmm/yyyy"));

            // Column INSTALLMENTS (Column F)
            cell = row.createCell(5);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getQtdeParcelas());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column ID COST CENTER PMS (Column G)
            cell = row.createCell(6);
            cell.setCellValue(null != licencaSwProjetoCellList.get(i).getCodigoCentroCustoErp()
                    ? licencaSwProjetoCellList.get(i).getCodigoCentroCustoErp().toString()
                    : "");
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column NAME COST CENTER PMS (Column H)
            cell = row.createCell(7);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getNomeCentroCusto());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column ID PROJECT MEGA (Column I)
            cell = row.createCell(8);
            cell.setCellValue(null != licencaSwProjetoCellList.get(i).getCodigoProjetoErp()
                    ? licencaSwProjetoCellList.get(i).getCodigoProjetoErp().toString()
                    : "");
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column NAME PROJECT MEGA (Column J)
            cell = row.createCell(9);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getNomeProjetoErp());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column Logins
            cell = row.createCell(10);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getLogins());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column INSTALLMENTS AMOUNT (Column K)
            cell = row.createCell(11);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getValorParcela().toString());
            cell.setCellStyle(this.getBorderedCellStyle("#,#0.00"));

            // Column AMOUNT (Column L)
            cell = row.createCell(12);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getValorTotal().toString());
            cell.setCellStyle(this.getBorderedCellStyle("#,#0.00"));

            // Column INSTALMENT NUMBER (Column M)
            cell = row.createCell(13);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getParcelaApropriada());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column APPROPRIATE AMOUNT (Column N)
            cell = row.createCell(14);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getValorApropriacao().toString());
            cell.setCellStyle(this.getBorderedCellStyle("#,#0.00"));

            // Column INSTALLMENTS BALANCE TOTAL (Column O)
            cell = row.createCell(15);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getSaldo());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column BALANCE TOTAL (Column P)
            cell = row.createCell(16);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getSaldoParcelas().toString());
            cell.setCellStyle(this.getBorderedCellStyle("#,#0.00"));

            // Column INSTALLMENTS BALANCE CP (Column Q)
            Long totalParcelasRestantes = licencaSwProjetoCellList.get(i).getQtdeParcelas()
                                - licencaSwProjetoCellList.get(i).getParcelaApropriada();
            Long installmentsCP = totalParcelasRestantes > QTD_PARCELAS_CURTO_PRAZO
                    ? QTD_PARCELAS_CURTO_PRAZO
                    : totalParcelasRestantes;
            cell = row.createCell(17);
            cell.setCellValue(installmentsCP);
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column BALANCE CP (Column R)
            cell = row.createCell(18);
            cell.setCellValue(installmentsCP * licencaSwProjetoCellList.get(i).getValorParcela().doubleValue());
            cell.setCellStyle(this.getBorderedCellStyle("#,#0.00"));

            // Column INSTALLMENTS BALANCE LP (Column S)
            Long installmentsLP = totalParcelasRestantes - QTD_PARCELAS_CURTO_PRAZO;
            cell = row.createCell(19);
            cell.setCellValue(installmentsLP > 0 ? installmentsLP.toString() : "-");
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column BALANCE LP (Column T)
            cell = row.createCell(20);
            cell.setCellValue(installmentsLP > 0
                    ? String.valueOf(installmentsLP * licencaSwProjetoCellList.get(i).getValorParcela().doubleValue())
                    : "-");
            cell.setCellStyle(this.getBorderedCellStyle("#,#0.00"));

            // Column STATUS (Column U)
            cell = row.createCell(21);
            cell.setCellValue(LicencaSwProjetoParcela.translateStatus(licencaSwProjetoCellList.get(i).getStatus()));
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column BusinessManager
            cell = row.createCell(22);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getBusinessManager());
            cell.setCellStyle(this.getBorderedCellStyle());


            // Column ProjectManager
            cell = row.createCell(23);
            cell.setCellValue(licencaSwProjetoCellList.get(i).getProjectManager());
            cell.setCellStyle(this.getBorderedCellStyle());

        }

        for (int i = 0; i < titles.length; i++) {
            sheet.autoSizeColumn(i);
        }

        String file = "ProjectLicenses_" + new Date().getTime() + ".xlsx";

        HttpServletResponse response = (HttpServletResponse) FacesContext
                .getCurrentInstance().getExternalContext().getResponse();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + file);

        wb.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void getUserLicensesReport(List<LicencaSwUserCell> licencaSwUserCellList) throws IOException {
        Sheet sheet = wb.createSheet("UserLicenses");

        // Header
        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(12.75f);
        for (int i = 0; i < userLicensesTitles.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(userLicensesTitles[i]);
            cell.setCellStyle(this.getHeaderStyle());
        }

        Row row;
        Cell cell;
        int rownum = 1;
        for (int i = 0; i < licencaSwUserCellList.size(); i++, rownum++) {
            row = sheet.createRow(rownum);
            if(licencaSwUserCellList.get(i) == null) continue;

            // Column MONTH
            cell = row.createCell(0);
            cell.setCellValue(licencaSwUserCellList.get(i).getMonth());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column RESOURCE NAME
            cell = row.createCell(1);
            cell.setCellValue(licencaSwUserCellList.get(i).getItResourceName());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column TOTAL VALUE
            cell = row.createCell(2);
            cell.setCellValue(licencaSwUserCellList.get(i).getLicenseTotalValue());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column STATUS
            cell = row.createCell(3);
            cell.setCellValue(LicencaSwProjetoParcela.translateStatus(licencaSwUserCellList.get(i).getStatus()));
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column ID COST CENTER PMS
            cell = row.createCell(4);
            cell.setCellValue(null != licencaSwUserCellList.get(i).getCostCenterCode() ? licencaSwUserCellList.get(i).getCostCenterCode().toString() : "");
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column NAME COST CENTER PMS
            cell = row.createCell(5);
            cell.setCellValue(licencaSwUserCellList.get(i).getCostCenterName());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column ID PROJECT MEGA
            cell = row.createCell(6);
            cell.setCellValue(null != licencaSwUserCellList.get(i).getMegaProjectCode() ? licencaSwUserCellList.get(i).getMegaProjectCode().toString() : "");
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column NAME PROJECO MEGA
            cell = row.createCell(7);
            cell.setCellValue(licencaSwUserCellList.get(i).getMegaProjectName());
            cell.setCellStyle(this.getBorderedCellStyle());

            // Column AMOUNT BY COST CENTER AND PROJECT
            cell = row.createCell(8);
            cell.setCellValue(licencaSwUserCellList.get(i).getLicenseValue());
            cell.setCellStyle(this.getBorderedCellStyle());

            cell = row.createCell(9);
            cell.setCellValue(licencaSwUserCellList.get(i).getLicenseLogins());
            cell.setCellStyle(this.getBorderedCellStyle());
        }

        for (int i = 0; i < titles.length; i++) {
            sheet.autoSizeColumn(i);
        }

        String file = "UserLicenses_" + new Date().getTime() + ".xlsx";

        HttpServletResponse response = (HttpServletResponse) FacesContext
                .getCurrentInstance().getExternalContext().getResponse();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + file);

        wb.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * create a library of cell styles
     */
    private static Map<String, CellStyle> createStyles(Workbook wb){
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        DataFormat df = wb.createDataFormat();

        CellStyle style;
        Font headerFont = wb.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        styles.put("header", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("header_date", style);

        Font font1 = wb.createFont();
        font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font1);
        styles.put("cell_b", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font1);
        styles.put("cell_b_centered", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_b_date", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_g", style);

        Font font2 = wb.createFont();
        font2.setColor(IndexedColors.BLUE.getIndex());
        font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font2);
        styles.put("cell_bb", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_bg", style);

        Font font3 = wb.createFont();
        font3.setFontHeightInPoints((short)14);
        font3.setColor(IndexedColors.DARK_BLUE.getIndex());
        font3.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font3);
        style.setWrapText(true);
        styles.put("cell_h", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setWrapText(true);
        styles.put("cell_normal", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        styles.put("cell_normal_centered", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setWrapText(true);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_normal_date", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setIndention((short)1);
        style.setWrapText(true);
        styles.put("cell_indented", style);

        style = createBorderedStyle(wb);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("cell_blue", style);

        return styles;
    }

    private static CellStyle createBorderedStyle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }

	private static SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM");

    private static final String[] titles = {
            "Client", "Contract-LOB", "LOB", "UMKT", "SSO", "Business Manager", "Date", "Currency", "Allocation Map Value", "Revenue Value", "Revenue Type"};
    private static final String[] projectLicensesTitles = {
            "Month", "Invoice Number", "Vendor", "Resource Name", "Start Date", "Installments", "ID Cost Center", "Cost Center", "ID Project",
            "Project",  "Logins", "Installments Amount", "Amount", "Installment Number", "Appropriate Amount", "Installments Balance Total", "Balance Total",
            "Installments Balance CP", "Balance CP", "Installments Balance LP", "Balance LP", "Status", "BM", "PM"};
    private static final String[] projectLicensesManagerNotificationTitles = {
            "Invoice Number", "Vendor", "Resource Name", "Installments Balance", "Installments Amount", "Start Date", "ID Cost Center", "Cost Center", "ID Project",
            "Project", "Logins", "BM", "PM" };
    private static final String[] userLicensesTitles = {
            "Month", "Resource Name", "Total Value", "Status", "ID Cost Center", "Cost Center", "ID Project", "Project", "Amount by Cost Center and Project", "Users"};

    //sample data to fill the sheet.
    private static final String[][] data = {
            {"1.0", "Marketing Research Tactical Plan", "J. Dow", "70", "9-Jul", null,
                "x", "x", "x", "x", "x", "x", "x", "x", "x", "x", "x"},
            null,
            {"1.1", "Scope Definition Phase", "J. Dow", "10", "9-Jul", null,
                "x", "x", null, null,  null, null, null, null, null, null, null},
            {"1.1.1", "Define research objectives", "J. Dow", "3", "9-Jul", null,
                    "x", null, null, null,  null, null, null, null, null, null, null},
            {"1.1.2", "Define research requirements", "S. Jones", "7", "10-Jul", null,
                "x", "x", null, null,  null, null, null, null, null, null, null},
            {"1.1.3", "Determine in-house resource or hire vendor", "J. Dow", "2", "15-Jul", null,
                "x", "x", null, null,  null, null, null, null, null, null, null},
            null,
            {"1.2", "Vendor Selection Phase", "J. Dow", "19", "19-Jul", null,
                null, "x", "x", "x",  "x", null, null, null, null, null, null},
            {"1.2.1", "Define vendor selection criteria", "J. Dow", "3", "19-Jul", null,
                null, "x", null, null,  null, null, null, null, null, null, null},
            {"1.2.2", "Develop vendor selection questionnaire", "S. Jones, T. Wates", "2", "22-Jul", null,
                null, "x", "x", null,  null, null, null, null, null, null, null},
            {"1.2.3", "Develop Statement of Work", "S. Jones", "4", "26-Jul", null,
                null, null, "x", "x",  null, null, null, null, null, null, null},
            {"1.2.4", "Evaluate proposal", "J. Dow, S. Jones", "4", "2-Jul", null,
                null, null, null, "x",  "x", null, null, null, null, null, null},
            {"1.2.5", "Select vendor", "J. Dow", "1", "6-Jul", null,
                null, null, null, null,  "x", null, null, null, null, null, null},
            null,
            {"1.3", "Research Phase", "G. Lee", "47", "9-Jul", null,
                null, null, null, null,  "x", "x", "x", "x", "x", "x", "x"},
            {"1.3.1", "Develop market research information needs questionnaire", "G. Lee", "2", "9-Jul", null,
                null, null, null, null,  "x", null, null, null, null, null, null},
            {"1.3.2", "Interview marketing group for market research needs", "G. Lee", "2", "11-Jul", null,
                null, null, null, null,  "x", "x", null, null, null, null, null},
            {"1.3.3", "Document information needs", "G. Lee, S. Jones", "1", "13-Jul", null,
                null, null, null, null,  null, "x", null, null, null, null, null},
    };
}