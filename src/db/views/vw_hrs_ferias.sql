create or replace view vw_hrs_ferias as
select vw.nr_hist_ferias,
       vw.dt_saida,
       vw.dt_retorno,
       vw.nr_dias_eq,
       p.pess_cd_login login,
       vw.nr_ano_eq,
       -- se o número de dias de descanso for maior do que zero, 
       -- obviamente houve um período de descanso, férias tiradas. 
       -- Caso contrário, quando é zero, são casos de (somente) vendas de 
       -- dias (geralmente dias vencidos)
       case when (vw.nr_dias_eq > 0) 
                 then 'T' --período de férias tiradas, descanso
                 else 'V' --período vendido
       end tipo
from hrs.vw_ferias vw,
     pessoa p
where vw.cd_pessoa = p.pess_cd_pessoa (+)