create or replace view vw_mega_comp_tce_funcionario as
select ---------------------------------------------------------------
       -- registros TCE até mês 05/2011
       ---------------------------------------------------------------
       tce_adm.tcfu_cd_pessoa,
       tce_adm.tcfu_cd_login,
       tce_adm.tcfu_dt_mes,
       tce_adm.tcfu_sg_moeda,
       tce_adm.tcfu_vl_salario,
       tce_adm.tcfu_vl_beneficios,
       tce_adm.tcfu_nr_horas_jornada,
       tce_adm.tcfu_vl_tce,
       tce_adm.tcfu_cd_tipo_contrato,
       tce_adm.tcfu_vl_taxa
from (
        ---------------------------------------------------------------
        -- Considera composicao_tce para analisar se existe valores ou não. 
        -- Quando o usuário fizer a sincronização via aplicação, será
        -- feita a pergunta se deseja sobreescrever os registros Manuais.
        -- Caso sim, não haverá registros na composicao_tce porque a aplicação vai deletar.
        -- Caso não, a aplicação vai manter os registros aqui e assim o join desconsidera esses valores.
        ---------------------------------------------------------------
        select tce.tcfu_cd_pessoa,
               tce.tcfu_cd_login,
               tce.tcfu_dt_mes,
               tce.tcfu_sg_moeda,
               tce.tcfu_vl_salario,
               tce.tcfu_vl_beneficios,
               tce.tcfu_nr_horas_jornada,
               tce.tcfu_vl_tce,
               tce.tcfu_cd_tipo_contrato,
               tce.tcfu_vl_taxa
        from composicao_tce ct,
             (
                ---------------------------------------------------------------
                -- pega os registros com seus logins e respectivos valores
                ---------------------------------------------------------------
                select p.pess_cd_pessoa tcfu_cd_pessoa,
                       c.login tcfu_cd_login,
                       to_date(c.mes, 'mm/yyyy') tcfu_dt_mes,
                       c.data_atualizacao,
                       case when c.moeda_custo is not null
                                 then c.moeda_custo
                                 else 'BRL'
                       end as tcfu_sg_moeda,
                       c.valor_total_salario tcfu_vl_salario,
                       c.valor_total_beneficios tcfu_vl_beneficios,
                       220 tcfu_nr_horas_jornada,
                       c.total_custo_colab tcfu_vl_tce,
                       tc.tico_cd_tipo_contrato tcfu_cd_tipo_contrato,
                       case when c.valor_taxa_moeda_mes is not null
                                 then c.valor_taxa_moeda_mes
                                 else 1
                       end as tcfu_vl_taxa
                from adm_rh.tb_bi_custo_func_mgr_aux c,
                     tipo_contrato tc,
                     pessoa p
                where decode(c.tp_contrato, '27', 'PJF', c.tp_contrato) = tc.tico_sg_tipo_contrato
                  and c.total_custo_colab > 0
                  and c.login = p.pess_cd_login
             ) tce,
             (
                ---------------------------------------------------------------
                -- pega o registro com a data de atualizacao maxima 
                ---------------------------------------------------------------
                select login,
                       mes,
                       dt_atualizacao
                from (
                        select c.login,
                               to_date(c.mes, 'mm/yyyy') mes,
                               max(c.data_atualizacao) over(partition by c.nm_func, c.mes) dt_atualizacao,
                               row_number() over(partition by c.nm_func, c.mes
                                                 order by c.nm_func, c.mes) nlinha
                        from adm_rh.tb_bi_custo_func_mgr_aux c
                     )
                where nlinha = 1
             ) max_dt_atu
        where tce.tcfu_cd_pessoa = ct.pess_cd_pessoa (+)
          and tce.tcfu_dt_mes = ct.cote_dt_mes (+)
          and ct.cote_in_tipo is null
          and tce.tcfu_cd_login = max_dt_atu.login
          and tce.tcfu_dt_mes = max_dt_atu.mes
          and tce.data_atualizacao = max_dt_atu.dt_atualizacao
     ) tce_adm
where trunc(tce_adm.tcfu_dt_mes, 'mm') <= to_date('01/05/2011', 'dd/mm/yyyy')

union all

select ---------------------------------------------------------------
       -- registros TCE após mês 05/2011 (de 06/2011 pra frente)
       ---------------------------------------------------------------
       p.pess_cd_pessoa cd_pessoa,
       vw.login,
       vw.dt_mes,
       case when (vw.desc_moeda is not null)
                 then vw.desc_moeda
                 else 'BRL' 
       end as sg_moeda,
       vw.valor_salario,
       vw.valor_beneficios,
       220 nr_horas_jornada,
       null valor_tce,
       case when (vw.tp_contrato is not null)
                 then (select tc.tico_cd_tipo_contrato from tipo_contrato tc where tc.tico_sg_tipo_contrato = vw.tp_contrato)
                 else 1 
       end as cd_tipo_contrato,
       null valor_taxa
from bisi.VW_BI_CARGA_PMS_INFORM_TCE vw,
     pessoa p
where trunc(vw.dt_mes, 'mm') > to_date('01/05/2011', 'dd/mm/yyyy')
  and vw.login = p.pess_cd_login;