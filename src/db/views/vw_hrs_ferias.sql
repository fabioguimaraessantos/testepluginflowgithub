create or replace view vw_hrs_ferias as
select vw.nr_hist_ferias,
       vw.dt_saida,
       vw.dt_retorno,
       vw.nr_dias_eq,
       p.pess_cd_login login,
       vw.nr_ano_eq,
       -- se o n�mero de dias de descanso for maior do que zero, 
       -- obviamente houve um per�odo de descanso, f�rias tiradas. 
       -- Caso contr�rio, quando � zero, s�o casos de (somente) vendas de 
       -- dias (geralmente dias vencidos)
       case when (vw.nr_dias_eq > 0) 
                 then 'T' --per�odo de f�rias tiradas, descanso
                 else 'V' --per�odo vendido
       end tipo
from hrs.vw_ferias vw,
     pessoa p
where vw.cd_pessoa = p.pess_cd_pessoa (+)