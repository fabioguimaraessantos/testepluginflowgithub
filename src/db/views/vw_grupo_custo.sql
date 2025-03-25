create or replace view vw_grupo_custo as
select a.area_cd_area,
       a.area_nm_area,
       a.area_in_ativo,
       gc.grcu_cd_grupo_custo,
       gc.grcu_nm_grupo_custo,
       gc.grcu_sg_grupo_custo,
       gc.grcu_in_ativo,
       gcp.grcp_cd_gc_periodo,
       gcp.grcp_dt_inicio,
       gcp.grcp_dt_fim,
       gccl.gccl_cd_grupo_custo_cl,
       cl.celu_cd_centro_lucro,
       cl.celu_nm_centro_lucro,
       ncl.nacl_cd_natureza,
       ncl.nacl_nm_natureza,
       ncl.nacl_in_ativo,
       ncl.nacl_in_tipo,
       ncl.nacl_tx_bundle
from   pms20.area                     a,
       pms20.grupo_custo              gc,
       pms20.grupo_custo_periodo      gcp,
       pms20.grupo_custo_centro_lucro gccl,
       pms20.centro_lucro             cl,
       pms20.natureza_centro_lucro    ncl

where  a.area_cd_area = gc.area_cd_area
       and gc.grcu_cd_grupo_custo = gcp.grcu_cd_grupo_custo
       and gcp.grcp_cd_gc_periodo = gccl.grcp_cd_gc_periodo
       and cl.celu_cd_centro_lucro = gccl.celu_cd_centro_lucro
       and cl.nacl_cd_natureza = ncl.nacl_cd_natureza