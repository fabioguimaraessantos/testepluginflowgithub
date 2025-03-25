CREATE OR REPLACE VIEW VW_PMS_VOUCHER_TRAVEL_BUDGET AS
SELECT cd_travel_budget,
       nm_travel_budget,
       vl_travel_budget,
       sg_currency,
       dt_begin,
       dt_end,
       tp_budget,
       nm_client,
       rtrim(xmlagg(xmlelement(e, cpod.copr_cd_contrato_pratica || ',')).extract('//text()'), ',') cd_clob,
       to_char(SYSDATE,'dd/mm/yyyy hh24:mi:ss') dt_creation
FROM contrato_pratica_orc_desp cpod,
     (
        select od.orde_cd_orc_despesa cd_travel_budget,
               od.orde_nm_orc_despesa nm_travel_budget,
               od.orde_vl_orcado vl_travel_budget,
               case when (od.moed_cd_moeda is not null)
                         then m.moed_sg_moeda
                         else 'BRL'
               end sg_currency,
               to_char(trunc(od.orde_dt_inicio),'dd/mm/yyyy') dt_begin,
               to_char(trunc(od.orde_dt_fim),'dd/mm/yyyy') dt_end,
               od.orde_tp_orcamento tp_budget,
               c.clie_nm_cliente nm_client
        from orcamento_despesa od,
             cliente c,
             moeda m
        where trunc(sysdate) between od.orde_dt_inicio and od.orde_dt_fim
          and od.orde_in_ativo = 'S'
          and (od.orde_in_sync is null or od.orde_in_sync = 'N')
          and od.clie_cd_cliente = c.clie_cd_cliente
          and od.moed_cd_moeda = m.moed_cd_moeda (+)
     ) od
WHERE od.cd_travel_budget = cpod.orde_cd_orc_despesa
GROUP BY cd_travel_budget,
         nm_travel_budget,
         vl_travel_budget,
         sg_currency,
         dt_begin,
         dt_end,
         tp_budget,
         nm_client