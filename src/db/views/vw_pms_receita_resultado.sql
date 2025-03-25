create or replace view vw_pms_receita_resultado as
select cp.copr_cd_contrato_pratica
      ,cp.copr_nm_contrato_pratica
      ,ap.alpe_dt_alocacao_periodo
      --,cp.moed_cd_moeda_padrao
      ,pv.moed_cd_moeda
      ,mo.moed_sg_moeda
      ,sum(ap.alpe_pr_alocacao_periodo) total_fte
      ,sum(ap.alpe_pr_alocacao_periodo * 168 * pr.ittp_vl_item_tb_preco * a.aloc_vl_ur * fum.faum_pr_fator_ur)  total_receita
from contrato_pratica cp
    ,mapa_alocacao m
    ,alocacao a
    ,alocacao_periodo ap
    ,fator_ur_mes fum
    ,moeda mo
    , perfil_vendido pv
    ,(select tp.tapr_dt_inicio_vigencia, tp.tapr_dt_fim_vigencia, itp.peve_cd_perfil_vendido, itp.ittp_vl_item_tb_preco
      from tabela_preco tp, item_tabela_preco itp
      where tp.tapr_cd_tabela_preco = itp.tapr_cd_tabela_preco ) pr
where m.maal_in_versao = 'PB'
-- and ap.alpe_dt_alocacao_periodo = p_mes
-- and cp.moed_cd_moeda_padrao = mo.moed_cd_moeda
and a.peve_cd_perfil_vendido = pv.peve_cd_perfil_vendido
and pv.moed_cd_moeda = mo.moed_cd_moeda
and cp.copr_cd_contrato_pratica = m.copr_cd_contrato_pratica
and m.maal_cd_mapa_alocacao = a.maal_cd_mapa_alocacao
and a.aloc_cd_alocacao = ap.aloc_cd_alocacao
and m.maal_cd_mapa_alocacao = fum.maal_cd_mapa_alocacao
and ap.alpe_dt_alocacao_periodo = fum.faum_dt_mes
and a.peve_cd_perfil_vendido = pr.peve_cd_perfil_vendido
and ap.alpe_dt_alocacao_periodo between pr.tapr_dt_inicio_vigencia and nvl(pr.tapr_dt_fim_vigencia, last_day(sysdate))
group by cp.copr_cd_contrato_pratica
        ,cp.copr_nm_contrato_pratica
        -- ,cp.moed_cd_moeda_padrao
        ,pv.moed_cd_moeda
        ,mo.moed_sg_moeda
        ,ap.alpe_dt_alocacao_periodo