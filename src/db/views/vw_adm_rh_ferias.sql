create or replace view vw_adm_rh_ferias as
select hf.nr_hist_ferias
      ,hf.dt_saida
      ,hf.dt_retorno
      ,hf.login
      ,hf.tipo
from adm_rh.hist_ferias_func hf

