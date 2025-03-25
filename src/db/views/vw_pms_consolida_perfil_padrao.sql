create or replace view vw_pms_consolida_perfil_padrao as
select rec.umkt,
       rec.sso,
       rec.pratica,
       rec.bm,
       rec.clie_cd_cliente,
       rec.nome_contrato_pratica,
       per.base,
       per.pmg,
       per.moeda,
       rec.dt_mes,
       rec.perfil_padrao
  from vw_consolida_receita_pms rec,
       vw_pms_perfil_padrao    per
 where rec.perfil_padrao = per.perfil_padrao