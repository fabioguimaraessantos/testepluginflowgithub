CREATE OR REPLACE VIEW "PMS20"."VW_FISCAL_BALANCE_ACUMULADO" AS 
  SELECT 
  rownum cd_dfiscal_acumulado
  , rec.CD_FISCAL_DEAL
  , rec.NM_FISCAL_DEAL
  , rec.CD_REVENUE_FISCAL_DEAL
  , rec.dt_mes
  , ROUND(nvl(rec.vl_total_revenue, 0), 2) vl_total_revenue
  , ROUND(nvl(rec.vl_total_revenue_ass, 0), 2) vl_total_revenue_ass
  , ROUND(nvl(rec.vl_total_invoiced, 0), 2) vl_total_invoiced
  , ROUND(nvl(rec.vl_total_invoiced_ass, 0), 2) vl_total_invoiced_ass
FROM
  (SELECT r.CD_FISCAL_DEAL,
    r.NM_FISCAL_DEAL,
    r.CD_REVENUE_FISCAL_DEAL,
    r.month dt_mes,
    SUM(r.REVENUE_VALUE) vl_total_revenue,
    SUM(r.VL_REVENUE_ASSOCIATED) vl_total_revenue_ass,
    SUM(r.VL_INVOICE) vl_total_invoiced,
    SUM(r.VL_INVOICE_ASSOCIATED) vl_total_invoiced_ass
  FROM VW_FISCAL_BALANCE r
  WHERE r.fonte in ('REC','FAT')
  GROUP BY r.CD_FISCAL_DEAL,
    r.NM_FISCAL_DEAL,
    r.CD_REVENUE_FISCAL_DEAL,
    r.month
  ) REC
ORDER BY rec.dt_mes;