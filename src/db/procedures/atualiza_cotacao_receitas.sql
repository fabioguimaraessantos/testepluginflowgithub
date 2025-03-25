create or replace procedure atualiza_cotacao_receitas (month_date  IN VARCHAR2) is

cursor c_receitas is
select rm.remo_cd_receita_moeda
       ,r.rece_dt_mes
       ,cp.copr_cd_contrato_pratica
       ,rm.moed_cd_moeda
from receita_moeda rm, receita r, contrato_pratica cp
where r.rece_cd_receita = rm.remo_cd_receita_moeda
and cp.copr_cd_contrato_pratica = r.copr_cd_contrato_pratica
and rm.moed_cd_moeda <> 1
and trunc(r.rece_dt_mes,'MM') = trunc(to_date(month_date,'dd/MM/yyyy'),'MM');

v_como_cd_cotacao_moeda number;

begin

for r_receitas in c_receitas loop
    begin
        select cm.como_cd_cotacao_moeda
        into v_como_cd_cotacao_moeda
        from cotacao_moeda cm
        where cm.moed_cd_moeda = r_receitas.moed_cd_moeda
        and cm.como_in_tipo = 'R'
        and cm.como_dt_dia = (select max(cm2.como_dt_dia)
                              from cotacao_moeda cm2
                              where cm2.como_dt_dia <= last_day(r_receitas.rece_dt_mes)
                              and cm2.moed_cd_moeda = r_receitas.moed_cd_moeda
                              and cm2.como_in_tipo = 'R');
    exception
        when no_data_found then
             v_como_cd_cotacao_moeda := null;

    end;

    if (v_como_cd_cotacao_moeda is not null) then
      update receita_moeda rm
         set rm.como_cd_cotacao_moeda = v_como_cd_cotacao_moeda
       where rm.remo_cd_receita_moeda = r_receitas.remo_cd_receita_moeda;
    end if;

end loop;
commit;

end atualiza_cotacao_receitas;