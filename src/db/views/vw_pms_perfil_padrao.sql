create or replace view vw_pms_perfil_padrao as
select cp.capm_nm_cargo_pms Cargo,
       cb.ciba_nm_cidade_base Base,
       m.moed_nm_moeda Moeda,
       p.pemg_nm_pmg PMG,
       ip.ittp_vl_item_tb_preco Valor,
       pp.pepa_cd_perfil_padrao perfil_padrao
  from perfil_padrao pp,
       cargo_pms     cp,
       pmg           p,
       cidade_base   cb,
       moeda         m,
       --perfil_vendido pv,
       contrato_pratica  cont,
       pessoa            pes,
       perfil_vendido    pv,
       tabela_preco      tp,
       item_tabela_preco ip
 where tp.tapr_cd_tabela_preco = ip.tapr_cd_tabela_preco
   and pv.peve_cd_perfil_vendido = ip.peve_cd_perfil_vendido
   and pp.capm_cd_cargo_pms = cp.capm_cd_cargo_pms
   and p.pemg_cd_pmg = pp.pemg_cd_pmg
   and cb.ciba_cd_cidade_base = pp.ciba_cd_cidade_base
   and m.moed_cd_moeda = cb.moed_cd_moeda
   and pv.pepa_cd_perfil_padrao = pp.pepa_cd_perfil_padrao
   and cont.msaa_cd_msa = pv.msaa_cd_msa
   and cont.pess_cd_pessoa_dn = pes.pess_cd_pessoa
   and pp.pepa_cd_perfil_padrao = 101