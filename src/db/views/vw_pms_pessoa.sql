create or replace view vw_pms_pessoa as
select
pess_cd_pessoa,
recu_cd_recurso,
ciba_cd_cidade_base,
pess_cd_login,
pess_nm_pessoa,
pess_tx_email,
pess_dt_admissao,
pess_cd_login_mgr,
pess_dt_mes_validado,
tico_cd_tipo_contrato,
pess_pr_ur_alvo,
pess_dt_rescisao,
pess_cd_login_mentor
from pessoa t