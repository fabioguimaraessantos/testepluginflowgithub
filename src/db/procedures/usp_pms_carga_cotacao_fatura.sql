create or replace procedure usp_pms_carga_cotacao_fatura is

cursor c_fatura is
       select f.fatu_cd_fatura cod_fatura, f.fatu_dt_previsao data_mes, cp.moed_cd_moeda_padrao cod_moeda
       from fatura f, contrato_pratica cp, deal_fiscal df
       where df.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica
       and df.defi_cd_deal_fiscal = f.defi_cd_deal_fiscal
       -- não pega as faturas na moeda REAL, pois não é necessário setar a cotação
       and cp.moed_cd_moeda_padrao <> 1;
v_cod_cotacao number;

BEGIN

for item in c_fatura loop

  BEGIN
       -- pega a ultima cotação do mês, de uma determinada moeda
       SELECT cotacao.como_cd_cotacao_moeda
       into v_cod_cotacao
       FROM cotacao_moeda cotacao
       WHERE cotacao.moed_cd_moeda = item.cod_moeda
        AND cotacao.como_in_tipo = 'R'
        AND cotacao.como_dt_dia =
           ( SELECT MAX(c.como_dt_dia)
             FROM cotacao_moeda c
             WHERE c.moed_cd_moeda = item.cod_moeda
                   AND c.como_dt_dia <= last_day(item.data_mes)
                   AND c.como_in_tipo = 'R'
           );
        -- seta a cotação na fatura
        update fatura f set f.como_cd_cotacao_moeda = v_cod_cotacao
               where f.fatu_cd_fatura = item.cod_fatura;

  EXCEPTION
  WHEN OTHERS THEN
   DBMS_OUTPUT.PUT_LINE(SQLERRM(SQLCODE));
  END;

end loop;

commit;

END usp_pms_carga_cotacao_fatura;
