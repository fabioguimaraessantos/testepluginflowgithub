create or replace view vw_item_receita as
select
  p.pess_cd_pessoa,
  ap.alpe_pr_alocacao_periodo,
  pv.peve_cd_perfil_vendido,
  case when (
      select count(*) peve_vl_perfil_vendido
        from item_tabela_preco itp
        where itp.tapr_cd_tabela_preco = tp.tapr_cd_tabela_preco and
          itp.peve_cd_perfil_vendido = pv.peve_cd_perfil_vendido
  ) = 0 then 0 else (
       select itp.ittp_vl_item_tb_preco
         from item_tabela_preco itp
         where itp.tapr_cd_tabela_preco = tp.tapr_cd_tabela_preco and
               itp.peve_cd_perfil_vendido = pv.peve_cd_perfil_vendido
  ) end peve_vl_perfil_vendido,

  nvl((select itp.ittp_pr_despesa
         from item_tabela_preco itp
         where itp.tapr_cd_tabela_preco = tp.tapr_cd_tabela_preco and
               itp.peve_cd_perfil_vendido = pv.peve_cd_perfil_vendido), 0) itre_per_despesa,

  trunc(ap.alpe_dt_alocacao_periodo) alpe_dt_alocacao_periodo,
  ma.copr_cd_contrato_pratica,
  a.aloc_cd_alocacao
from alocacao a,
  alocacao_periodo ap,
  mapa_alocacao ma,
  contrato_pratica cp,
  msa msa,
  perfil_vendido pv,
  tabela_preco tp,
  pessoa p,
  recurso r
where ma.maal_in_versao = 'PB'
  and TRUNC(ap.alpe_dt_alocacao_periodo) >= TRUNC(tp.tapr_dt_inicio_vigencia)
  and (TRUNC(ap.alpe_dt_alocacao_periodo) <= TRUNC(tp.tapr_dt_fim_vigencia) or tp.tapr_dt_fim_vigencia is null)
  and a.aloc_cd_alocacao = ap.aloc_cd_alocacao
  and a.peve_cd_perfil_vendido = pv.peve_cd_perfil_vendido
  and a.maal_cd_mapa_alocacao = ma.maal_cd_mapa_alocacao

  AND pv.moed_cd_moeda = tp.moed_cd_moeda

  and cp.copr_cd_contrato_pratica = ma.copr_cd_contrato_pratica
  and cp.msaa_cd_msa = msa.msaa_cd_msa

  and tp.msaa_cd_msa = msa.msaa_cd_msa
  and p.recu_cd_recurso = r.recu_cd_recurso
  and r.recu_cd_recurso = a.recu_cd_recurso