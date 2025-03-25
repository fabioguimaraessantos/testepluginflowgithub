create or replace view vw_estratificacao_ur_resultado as
select m.copr_cd_contrato_pratica esur_cd_contrato_pratica
      ,m.dt_mes esur_dt_mes
      ,va.pess_cd_pessoa esur_cd_pessoa
      ,pes.pess_cd_login esur_cd_login
      ,nvl(va.total_alocado,0) esur_vl_total_alocado
      ,nvl(round(vr.total_receitado,2),0) esur_vl_total_receitado
      ,round(nvl(vr.total_receitado,0) - nvl(va.total_alocado,0), 2) esur_vl_diferenca_real
      ,e.iteu_vl_diferenca esur_vl_diferenca_review
      ,case
       when ( nvl(e.iteu_vl_diferenca,0) > round((nvl(vr.total_receitado,0) - nvl(va.total_alocado,0)),2) ) and ( round(nvl(vr.total_receitado,0) - nvl(va.total_alocado,0), 2) ) < 0 then 'S'
       else 'N' end esur_in_inconsistente
      ,case
       when nvl(vr.total_receitado,0) - nvl(va.total_alocado,0) < 0 then 'S'
       else 'N' end esur_in_diferenca_real_neg
      ,case
       when e.exde_cd_motivo_desvio = 5 then 'S'
       else 'N' end esur_in_nao_definido
      ,e.exde_cd_motivo_desvio esur_cd_motivo_desvio
      ,e.esur_cd_estratificacao_ur
      ,e.iteu_cd_item_estrat_ur esur_cd_item_estrat_ur

from pessoa pes,
-- seleção dos FTEs das pessoas receitadas
	(select re.copr_cd_contrato_pratica copr_cd_contrato_pratica
      , re.rece_dt_mes dt_mes
      , ir.pess_cd_pessoa pess_cd_pessoa
      , sum(ir.itre_nr_fte) total_receitado
	 from receita re, item_receita ir, receita_moeda rm
	 where re.rece_in_versao in ('PB', 'IN')
	 and re.rece_cd_receita = rm.rece_cd_receita
   AND rm.remo_cd_receita_moeda = ir.remo_cd_receita_moeda
	 group by re.copr_cd_contrato_pratica, re.rece_dt_mes, ir.pess_cd_pessoa ) vr,
-- seleção dos FTEs das pessoas alocadas
	(select ma.copr_cd_contrato_pratica copr_cd_contrato_pratica
          , ap.alpe_dt_alocacao_periodo dt_mes
          , p.pess_cd_pessoa pess_cd_pessoa
          , sum(ap.alpe_pr_alocacao_periodo) total_alocado
     from mapa_alocacao ma, alocacao a, alocacao_periodo ap, pessoa p
     where ma.maal_cd_mapa_alocacao = a.maal_cd_mapa_alocacao
     and a.aloc_cd_alocacao = ap.aloc_cd_alocacao
     and a.recu_cd_recurso = p.recu_cd_recurso
     and ma.maal_in_versao = 'PB'
     group by ma.copr_cd_contrato_pratica, ap.alpe_dt_alocacao_periodo, p.pess_cd_pessoa) va,
-- seleção da união das pessoas, mês, contrato-pratica do alocado e do receitado
	(select re.copr_cd_contrato_pratica copr_cd_contrato_pratica
           ,re.rece_dt_mes dt_mes
           ,ir.pess_cd_pessoa pess_cd_pessoa
           ,count(*) num_reg
     from receita re, item_receita ir, receita_moeda rm
     where re.rece_in_versao in ('PB', 'IN')
     and re.rece_cd_receita = rm.rece_cd_receita
     AND rm.remo_cd_receita_moeda = ir.remo_cd_receita_moeda
     group by re.copr_cd_contrato_pratica, re.rece_dt_mes, ir.pess_cd_pessoa
     union all
     select ma.copr_cd_contrato_pratica copr_cd_contrato_pratica
           ,ap.alpe_dt_alocacao_periodo dt_mes
           ,p.pess_cd_pessoa pess_cd_pessoa
           ,count(*) num_reg
     from mapa_alocacao ma, alocacao a, alocacao_periodo ap, pessoa p, receita_moeda rm1
     where ma.maal_cd_mapa_alocacao = a.maal_cd_mapa_alocacao
     and a.aloc_cd_alocacao = ap.aloc_cd_alocacao
     and a.recu_cd_recurso = p.recu_cd_recurso
     and ma.maal_in_versao = 'PB'
     and not exists (select null from receita re1, item_receita ir1
                     where re1.rece_cd_receita = rm1.rece_cd_receita
                     AND rm1.remo_cd_receita_moeda = ir1.remo_cd_receita_moeda
                     and re1.rece_dt_mes = ap.alpe_dt_alocacao_periodo
                     and re1.copr_cd_contrato_pratica = ma.copr_cd_contrato_pratica
                     and ir1.pess_cd_pessoa = p.pess_cd_pessoa)
     group by ma.copr_cd_contrato_pratica, ap.alpe_dt_alocacao_periodo, p.pess_cd_pessoa ) m,
-- seleção das estratificações
    (select m.copr_cd_contrato_pratica copr_cd_contrato_pratica
           ,eu.esur_dt_mes dt_mes
           ,ie.pess_cd_pessoa pess_cd_pessoa
           ,ie.iteu_vl_diferenca iteu_vl_diferenca
           ,ie.iteu_cd_item_estrat_ur iteu_cd_item_estrat_ur
           ,ie.exde_cd_motivo_desvio
           ,eu.esur_cd_estratificacao_ur
     from item_estratificacao_ur ie, estratificacao_ur eu, mapa_alocacao m
     where eu.maal_cd_mapa_alocacao = m.maal_cd_mapa_alocacao
     and eu.esur_cd_estratificacao_ur = ie.esur_cd_estratificacao_ur ) e
where m.pess_cd_pessoa = pes.pess_cd_pessoa
and m.dt_mes = va.dt_mes (+)
and m.copr_cd_contrato_pratica = va.copr_cd_contrato_pratica (+)
and m.pess_cd_pessoa = va.pess_cd_pessoa (+)
and m.dt_mes = vr.dt_mes (+)
and m.copr_cd_contrato_pratica = vr.copr_cd_contrato_pratica (+)
and m.pess_cd_pessoa = vr.pess_cd_pessoa (+)
and m.dt_mes = e.dt_mes (+)
and m.copr_cd_contrato_pratica = e.copr_cd_contrato_pratica (+)
and m.pess_cd_pessoa = e.pess_cd_pessoa (+)