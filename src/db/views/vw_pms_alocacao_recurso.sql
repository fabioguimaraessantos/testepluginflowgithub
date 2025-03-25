create or replace view vw_pms_alocacao_recurso as
select cl.clie_nm_cliente -- view para o people
      ,m.msaa_nm_msa
      ,cp.copr_nm_contrato_pratica
      ,ma.maal_tx_titulo
      ,re.recu_cd_mnemonico
      ,ap.alpe_dt_alocacao_periodo
      ,ap.alpe_pr_alocacao_periodo
from cliente cl
    ,msa m
    ,contrato_pratica cp
    ,mapa_alocacao ma
    ,alocacao al
    ,recurso re
    ,alocacao_periodo ap
where re.recu_in_tipo_recurso = 'PE'
and m.clie_cd_cliente = cl.clie_cd_cliente
and cp.msaa_cd_msa = m.msaa_cd_msa
and ma.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica
and al.maal_cd_mapa_alocacao = ma.maal_cd_mapa_alocacao
and re.recu_cd_recurso = al.recu_cd_recurso
and al.aloc_cd_alocacao = ap.aloc_cd_alocacao

