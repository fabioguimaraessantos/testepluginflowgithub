create or replace view vw_adm_rh_cargos as
select c.cd_cargo
      ,c.nm_cargo
from adm_rh.cargos c