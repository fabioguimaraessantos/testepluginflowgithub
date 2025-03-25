CREATE OR REPLACE VIEW VW_PMS_VOUCHER_TB_CL AS
SELECT cd_travel_budget,
       nm_travel_budget,
       vl_travel_budget,
       sg_currency,
       dt_begin,
       dt_end,
       tp_budget,
       nm_client,
       rtrim(xmlagg(xmlelement(e, cpod.copr_cd_contrato_pratica || ',')).extract('//text()'), ',') cd_clob,
       to_char(SYSDATE,'dd/mm/yyyy hh24:mi:ss') dt_creation,
       in_ativo,
       in_delete_logico,
       tp_orc_desp
FROM contrato_pratica_orc_desp_cl cpod,
     (
        select od.orde_cd_orca_despesa cd_travel_budget
               , od.orde_nm_orc_despesa nm_travel_budget
               , od.orde_vl_orcado vl_travel_budget
               , case when (od.moed_cd_moeda is not null)
                           then m.moed_sg_moeda
                           else 'BRL'
                 end sg_currency
               , to_char(trunc(od.orde_dt_inicio),'dd/mm/yyyy') dt_begin
               , to_char(trunc(od.orde_dt_fim),'dd/mm/yyyy') dt_end
               , odcl.ordc_in_reembolsavel tp_budget
               , c.clie_nm_cliente nm_client
               , od.orde_in_ativo in_ativo
               , od.orde_in_delete_logico in_delete_logico
               , odcl.ordc_cd_orca_desp_cl cd_travel_budget_cl
               , od.orde_tp_orc_desp tp_orc_desp
        from orc_despesa od
             , orc_despesa_cl odcl
             , moeda m
             , cliente c
        where od.orde_cd_orca_despesa = odcl.orde_cd_orca_despesa
              and od.moed_cd_moeda = m.moed_cd_moeda
              and odcl.clie_cd_cliente = c.clie_cd_cliente
     ) od
WHERE od.cd_travel_budget_cl = cpod.ordc_cd_orca_desp_cl
GROUP BY cd_travel_budget,
         nm_travel_budget,
         vl_travel_budget,
         sg_currency,
         dt_begin,
         dt_end,
         tp_budget,
         nm_client,
         in_ativo,
         in_delete_logico,
         tp_orc_desp

