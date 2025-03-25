create or replace view vw_adm_rh_tce_funcionario as
select c.login tcfu_login,
       to_date(c.mes, 'mm/yyyy') tcfu_dt_mes,
       tc.tico_sg_tipo_contrato tcfu_sg_tipo_contrato,
       tc.tico_cd_tipo_contrato,
       c.total_custo_colab tcfu_tce,
       case when c.moeda_custo is not null
       then c.moeda_custo
       else 'BRL'
       end as cod_moeda,
       case when c.valor_taxa_moeda_mes is not null
       then c.valor_taxa_moeda_mes
       else 1
       end as valor_taxa
from adm_rh.tb_bi_custo_func_mgr_aux c, tipo_contrato tc
where decode(c.tp_contrato, '27', 'PJF', '25', 'PJC', '29', 'EUA',
              40,'JPE',41,'JPF',
              42,'CNE',43,'CFW',
              44,'CNI',45,'ARE',46,'CRW',47,'CRE',48,'CFE'
,c.tp_contrato) = tc.tico_sg_tipo_contrato
and c.total_custo_colab > 0