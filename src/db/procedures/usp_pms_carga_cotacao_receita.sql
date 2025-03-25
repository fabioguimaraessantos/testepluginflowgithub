create or replace procedure usp_pms_carga_cotacao_receita is

cursor c_receita is
       select r.rece_cd_receita cod_receita
              , r.rece_dt_mes data_mes
              , rm.moed_cd_moeda cod_moeda
       from receita_moeda rm, receita r, contrato_pratica cp
       where rm.rece_cd_receita = r.rece_cd_receita
       and r.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica
       -- não pega as receitas na moeda REAL, pois não é necessário setar a cotação
       and rm.moed_cd_moeda <> 1;
v_cod_cotacao number;

BEGIN

for item in c_receita loop

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
                   AND TO_CHAR(c.como_dt_dia,'MM/yyyy') = TO_CHAR(item.data_mes,'MM/yyyy')
                   AND c.como_in_tipo = 'R'
           );
        -- seta a cotação na receita
        update receita_moeda rm set rm.como_cd_cotacao_moeda = v_cod_cotacao
               where rm.rece_cd_receita = item.cod_moeda;

  EXCEPTION
  WHEN OTHERS THEN
   DBMS_OUTPUT.PUT_LINE(SQLERRM(SQLCODE));
  END;

end loop;

--commit;

END usp_pms_carga_cotacao_receita;
