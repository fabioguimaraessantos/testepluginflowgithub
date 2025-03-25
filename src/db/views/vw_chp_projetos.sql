create or replace view vw_chp_projetos as
select p.codigo
      ,p.nome
      ,p.cd_empresa
      ,p.copr_cd_contrato_pratica
      ,p.grcu_cd_grupo_custo
      ,p.cd_mega
from chp.projetos p