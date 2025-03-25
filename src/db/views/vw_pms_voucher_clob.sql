create or replace view vw_pms_voucher_clob as
select cp.copr_cd_contrato_pratica cd_clob,
       cp.copr_nm_contrato_pratica nm_clob,
       to_char(SYSDATE,'dd/mm/yyyy hh24:mi:ss') dt_creation
from orc_despesa_cl odcl
     , orc_despesa od
     , contrato_pratica_orc_desp_cl cpodcl
     , contrato_pratica cp
where od.orde_cd_orca_despesa = odcl.orde_cd_orca_despesa
      and cpodcl.ordc_cd_orca_desp_cl = odcl.ordc_cd_orca_desp_cl
      and cp.copr_cd_contrato_pratica = cpodcl.copr_cd_contrato_pratica
      and cp.copr_in_status = 'C'
      and odcl.ordc_in_delete_logico = 'N'
      and od.orde_in_delete_logico = 'N'
      and od.orde_in_ativo = 'S'
group by cp.copr_cd_contrato_pratica,
         cp.copr_nm_contrato_pratica