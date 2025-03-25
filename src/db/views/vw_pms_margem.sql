create or replace view vw_pms_margem as
select cd_contrato_pratica
, dt_mes
, total_receita
, receita_liquida
, lucro_direto_gda
, lucro_contrib
, case
  when total_receita = 0 then 0
  else (lucro_direto_gda/total_receita)
  end margem_direta_gda
, case
  when receita_liquida = 0 then 0
  else (lucro_contrib/receita_liquida)
  end margem_contrib
, case
  when total_receita = 0 or receita_liquida = 0 then 0
  else(lucro_direto_gda/total_receita - lucro_contrib/receita_liquida)
  end delta
from (
select cd_contrato_pratica, dt_mes
,total_receita
, total_receita- total_imposto receita_liquida
, (total_receita + total_custo_direto + total_custo_dir_infra) lucro_direto_gda
, (total_receita - total_imposto + total_custo_direto + total_custo_dir_infra
   + total_custo_ind_tce + total_custo_ind_infra + total_despesas
    + total_hora_extra + total_plantao - total_custo_item)    lucro_contrib
from (select c.cd_contrato_pratica cd_contrato_pratica
      ,c.lob_contrato_pratica contrato
      ,c.dt_mes dt_mes
      ,sum(c.valor_receita_brl) total_receita
      ,sum(c.valor_imposto_convertido) total_imposto
      ,sum(c.valor_custo_dir_tce_brl) total_custo_direto
      ,sum(c.valor_custo_dir_infra_brl) total_custo_dir_infra
      ,sum(c.valor_custo_ind_tce_brl) total_custo_ind_tce
      ,sum(c.valor_custo_ind_infra_brl) total_custo_ind_infra
      ,sum(c.valor_desp_rel_brl) total_despesas
      ,sum(c.valor_hora_extra) total_hora_extra
      ,sum(c.valor_plantao) total_plantao
      ,sum(c.valor_custo_item) total_custo_item
from pms.tb_bi_pms_consolida_dres c
where c.cd_contrato_pratica <> 0
      and c.cd_contrato_pratica is not null
group by c.cd_contrato_pratica ,c.dt_mes
))
order by cd_contrato_pratica, dt_mes