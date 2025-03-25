create or replace view vw_adm_rh_funcionarios as
select t.LOGIN
       ,t.NM_FUNC
       ,t.EMAIL
       ,t.dt_admissao
       ,t.LOGIN_MGR
       ,nvl(t.cidade_base, 'CPS')cidade_base
       ,t.organizacao, t.ST_ATIVO
       ,t.tp_contrato
       ,t.jornada_hrs_dia
       ,t.dt_saida
       ,t.mentor
       ,t.ds_papel
       ,t.cd_cargo
from adm_rh.chp_funcionarios t
order by t.NM_FUNC