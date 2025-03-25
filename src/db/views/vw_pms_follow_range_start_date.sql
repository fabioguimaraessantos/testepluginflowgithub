create or replace view vw_pms_follow_range_start_date as
select max(next_closing_date) max_date
from (
        --mês fechado (closingDate) + 1
        select add_months(m.modu_dt_fechamento, 1) next_closing_date
        from modulo m
        where m.modu_cd_modulo = 1

        union all

        --mês atual (sysdate) -1
        select add_months(trunc(sysdate,'MONTH'), -1) previous_current_date
        from dual
     )