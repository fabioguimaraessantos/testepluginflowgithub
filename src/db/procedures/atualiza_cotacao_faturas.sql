create or replace procedure atualiza_cotacao_faturas (month_date  IN VARCHAR2) is

cursor c_faturas is

select f.fatu_cd_fatura
       ,f.fatu_dt_previsao
       ,cp.copr_cd_contrato_pratica
       ,f.moed_cd_moeda
from fatura f, deal_fiscal df, msa m, contrato_pratica cp
where f.defi_cd_deal_fiscal = df.defi_cd_deal_fiscal
and m.msaa_cd_msa = df.msaa_cd_msa
and cp.msaa_cd_msa = m.msaa_cd_msa
and f.moed_cd_moeda <> 1
and trunc(f.fatu_dt_previsao,'MM') = trunc(to_date(month_date,'dd/MM/yyyy'),'MM');

v_como_cd_cotacao_moeda number;

begin

for r_faturas in c_faturas loop
    begin
        select cm.como_cd_cotacao_moeda
        into v_como_cd_cotacao_moeda
        from cotacao_moeda cm
        where cm.moed_cd_moeda = r_faturas.moed_cd_moeda
        and cm.como_in_tipo = 'R'
        and cm.como_dt_dia = (select max(cm2.como_dt_dia)
                              from cotacao_moeda cm2
                              where cm2.como_dt_dia <= last_day(r_faturas.fatu_dt_previsao)
                              and cm2.moed_cd_moeda = r_faturas.moed_cd_moeda
                              and cm2.como_in_tipo = 'R');
    exception
        when no_data_found then
             v_como_cd_cotacao_moeda := null;

    end;

    update fatura f
    set f.como_cd_cotacao_moeda = v_como_cd_cotacao_moeda
    where f.fatu_cd_fatura = r_faturas.fatu_cd_fatura;

end loop;
commit;

end atualiza_cotacao_faturas;
