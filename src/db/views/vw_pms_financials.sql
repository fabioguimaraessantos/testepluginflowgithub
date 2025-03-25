create or replace view vw_pms_financials as
select sq1.copr_cd_contrato_pratica
      ,sq1.dt_mes
      ,sq1.moed_cd_moeda
      ,sq1.p_total_fte
      ,sq1.p_avg_ur
      ,round(nvl(sq2.p_total_receita,0),2) p_total_receita
      ,round(nvl(sq3.r_total_fte,0),2) r_total_fte
      ,round(nvl(sq3.r_total_receita,0), 2) r_total_receita
      ,to_number(nvl(decode(nvl(sq1.p_total_fte,0), 0, null, round(sq3.r_total_fte/sq1.p_total_fte,2)),0)) r_ur_medio
from
(select m.copr_cd_contrato_pratica copr_cd_contrato_pratica
       ,ap.alpe_dt_alocacao_periodo dt_mes
       ,mo.moed_cd_moeda moed_cd_moeda
       ,round(avg(a.aloc_vl_ur * fum.faum_pr_fator_ur),2) p_avg_ur
       ,sum(ap.alpe_pr_alocacao_periodo) p_total_fte
from mapa_alocacao m
    ,alocacao a
    ,perfil_vendido pv
    ,alocacao_periodo ap
    ,fator_ur_mes fum
    ,moeda mo
where m.maal_in_versao = 'PB'
  AND m.maal_cd_mapa_alocacao = a.maal_cd_mapa_alocacao
  AND a.peve_cd_perfil_vendido = pv.peve_cd_perfil_vendido
  and pv.moed_cd_moeda = mo.moed_cd_moeda
  and m.maal_cd_mapa_alocacao = a.maal_cd_mapa_alocacao
  and a.aloc_cd_alocacao = ap.aloc_cd_alocacao
  and m.maal_cd_mapa_alocacao = fum.maal_cd_mapa_alocacao
  and ap.alpe_dt_alocacao_periodo = fum.faum_dt_mes
  group by m.copr_cd_contrato_pratica
       ,ap.alpe_dt_alocacao_periodo
       ,mo.moed_cd_moeda) sq1, -- subquery que retorna ur_medio e total_fte baseado no mapa
(select m.copr_cd_contrato_pratica copr_cd_contrato_pratica
       ,ap.alpe_dt_alocacao_periodo dt_mes
       ,mo.moed_cd_moeda moed_cd_moeda
       ,sum(ap.alpe_pr_alocacao_periodo * 168 * pr.ittp_vl_item_tb_preco * a.aloc_vl_ur * fum.faum_pr_fator_ur)  p_total_receita
from mapa_alocacao m
    ,alocacao a
    ,perfil_vendido pv
    ,alocacao_periodo ap
    ,fator_ur_mes fum
    ,moeda mo
    ,(select tp.tapr_dt_inicio_vigencia, tp.tapr_dt_fim_vigencia, itp.peve_cd_perfil_vendido, itp.ittp_vl_item_tb_preco, tp.moed_cd_moeda
      from tabela_preco tp, item_tabela_preco itp
      where tp.tapr_cd_tabela_preco = itp.tapr_cd_tabela_preco) pr
where m.maal_in_versao = 'PB'
  AND m.maal_cd_mapa_alocacao = a.maal_cd_mapa_alocacao
  AND a.peve_cd_perfil_vendido = pv.peve_cd_perfil_vendido
  and pv.moed_cd_moeda = mo.moed_cd_moeda
  and m.maal_cd_mapa_alocacao = a.maal_cd_mapa_alocacao
  and a.aloc_cd_alocacao = ap.aloc_cd_alocacao
  and m.maal_cd_mapa_alocacao = fum.maal_cd_mapa_alocacao
  and ap.alpe_dt_alocacao_periodo = fum.faum_dt_mes
  and a.peve_cd_perfil_vendido = pr.peve_cd_perfil_vendido
  and ap.alpe_dt_alocacao_periodo between pr.tapr_dt_inicio_vigencia and nvl(pr.tapr_dt_fim_vigencia, last_day(add_months(sysdate,12)))
  group by m.copr_cd_contrato_pratica
       ,ap.alpe_dt_alocacao_periodo
       ,mo.moed_cd_moeda ) sq2,  -- subquery que traz a receita projetada pelo mapa
(select re.copr_cd_contrato_pratica copr_cd_contrato_pratica
       ,re.rece_dt_mes dt_mes
       ,rm.moed_cd_moeda moed_cd_moeda
       ,sum(ir.itre_nr_fte)r_total_fte
       ,sum(ir.itre_vl_total_item) r_total_receita
from receita re, receita_moeda rm, item_receita ir
where re.rece_cd_receita = rm.rece_cd_receita
and re.rece_in_versao in ('PB', 'IN')
and rm.moed_cd_moeda = rm.moed_cd_moeda
and rm.remo_cd_receita_moeda = ir.remo_cd_receita_moeda
group by re.copr_cd_contrato_pratica, re.rece_dt_mes, rm.moed_cd_moeda ) sq3 -- subquery que traz a receita publica/ou integrada por mês
where sq1.copr_cd_contrato_pratica = sq2.copr_cd_contrato_pratica (+)
and sq1.dt_mes = sq2.dt_mes (+)
and sq1.moed_cd_moeda = sq2.moed_cd_moeda (+)
and sq1.copr_cd_contrato_pratica = sq3.copr_cd_contrato_pratica (+)
and sq1.dt_mes = sq3.dt_mes (+)
and sq1.moed_cd_moeda = sq3.moed_cd_moeda (+)

