create or replace view vw_pms_voucher_tb_gc as
select od.orde_cd_orca_despesa cd_travel_budget
       , od.orde_nm_orc_despesa nm_travel_budget
       , od.orde_vl_orcado vl_travel_budget
       , case when (od.moed_cd_moeda is not null)
                   then m.moed_sg_moeda
                   else 'BRL'
         end sg_currency
       , to_char(trunc(od.orde_dt_inicio),'dd/mm/yyyy') dt_begin
       , to_char(trunc(od.orde_dt_fim),'dd/mm/yyyy') dt_end
       , gc.grcu_nm_grupo_custo nm_grupo_custo
       , gc.grcu_cd_grupo_custo cd_cgroup
       , to_char(SYSDATE,'dd/mm/yyyy hh24:mi:ss') dt_creation
       , od.orde_in_ativo in_ativo
       , od.orde_in_delete_logico in_delete_logico
       , od.orde_tp_orc_desp tp_orc_desp
from orc_despesa od
     , orc_despesa_gc odgc
     , moeda m
     , grupo_custo gc
where od.orde_cd_orca_despesa = odgc.orde_cd_orca_despesa
      and od.moed_cd_moeda = m.moed_cd_moeda
      and odgc.grcu_cd_grupo_custo = gc.grcu_cd_grupo_custo

