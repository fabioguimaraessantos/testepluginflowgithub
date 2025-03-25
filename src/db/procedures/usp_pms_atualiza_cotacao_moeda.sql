create or replace procedure usp_pms_atualiza_cotacao_moeda is

cursor c_cotacao_moeda is
   select c.ind_in_codigo, c.val_dt_indice, c.val_re_valor, c.val_ch_tipo
   from mgglo.glo_valor_financeiro@mega_link c
   -- pega somente as cotações verdadeiras (não pega as cotações estimadas)
   where val_ch_tipo = 'R' 
   -- não pega as cotações já importadas
   AND c.val_dt_indice not in
       (select cm.como_dt_dia 
        from cotacao_moeda cm, moeda m 
        where cm.moed_cd_moeda = m.moed_cd_moeda 
        and m.moed_cd_erp_ind_fin = c.ind_in_codigo)
   -- pegar as cotações a partir de jan/2009                     
   AND c.val_dt_indice > to_date('31/12/2008', 'dd/MM/yyyy') 
   -- pega as cotações das seguintes moedas: 14-Libras, 15-Dolar, 16-Euro
   AND (c.ind_in_codigo = 14 or c.ind_in_codigo = 15 or c.ind_in_codigo = 16); 

BEGIN


for r_cot_moeda in c_cotacao_moeda loop

  BEGIN
      -- insere as novas cotações
      insert into cotacao_moeda (como_cd_cotacao_moeda, moed_cd_moeda, como_dt_dia, como_vl_cotacao, como_in_tipo)
         values (SQ_COMO_CD_COTACAO_MOEDA.nextval, 
         (select m.moed_cd_moeda from moeda m where m.moed_cd_erp_ind_fin = r_cot_moeda.ind_in_codigo), 
         r_cot_moeda.val_dt_indice, 
         r_cot_moeda.val_re_valor, 
         r_cot_moeda.val_ch_tipo);
  EXCEPTION
  WHEN OTHERS THEN
   DBMS_OUTPUT.PUT_LINE(SQLERRM(SQLCODE)); 
  END;
         
end loop;

commit;

END usp_pms_atualiza_cotacao_moeda;
