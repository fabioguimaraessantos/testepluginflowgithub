create or replace view vw_pms_voucher_cgroup as
select gc.grcu_cd_grupo_custo cd_cgroup
       , gc.grcu_nm_grupo_custo nm_cgroup
       , to_char(SYSDATE,'dd/mm/yyyy hh24:mi:ss') dt_creation
from orc_despesa_gc odgc
     , orc_despesa od
     , grupo_custo gc
where od.orde_cd_orca_despesa = odgc.orde_cd_orca_despesa
      and gc.grcu_cd_grupo_custo = odgc.grcu_cd_grupo_custo
      and gc.grcu_in_ativo = 'A'
      and odgc.ordg_in_delete_logico = 'N'
      and od.orde_in_delete_logico = 'N'
      and od.orde_in_ativo = 'S'
group by gc.grcu_cd_grupo_custo, gc.grcu_nm_grupo_custo
