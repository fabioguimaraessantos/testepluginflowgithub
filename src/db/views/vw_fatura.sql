create or replace view vw_fatura as
select t.fatu_cd_fatura
      ,t.fatu_dt_previsao
      ,t.fatu_dt_vencimento
      ,t.fatu_cd_login_ae
from fatura t