create or replace view vw_pms_perfil_vendido_preco as
select pv.peve_cd_perfil_vendido,
       pv.peve_nm_perfil_vendido,
       cp.copr_nm_contrato_pratica,
       cp.copr_cd_contrato_pratica,
       tp.tapr_dt_inicio_vigencia,
       tp.tapr_dt_fim_vigencia,
       ip.ittp_vl_item_tb_preco
  from perfil_vendido    pv,
       msa               m,
       contrato_pratica  cp,
       tabela_preco      tp,
       item_tabela_preco ip
 where m.msaa_cd_msa = pv.msaa_cd_msa
   and cp.msaa_cd_msa = m.msaa_cd_msa
   and tp.msaa_cd_msa = m.msaa_cd_msa
   and tp.tapr_cd_tabela_preco = ip.tapr_cd_tabela_preco
   and pv.peve_cd_perfil_vendido = ip.peve_cd_perfil_vendido