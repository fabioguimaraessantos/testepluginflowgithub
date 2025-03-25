create or replace procedure usp_pms_charge_back_soft as
--10 de Setembro de 2012
--Carga para gravação do IT CHARGE BACK de SOFTWARE
--Essa carga é mensal e incremental. Não se trunca a tabela

       contador1 number := 0;
       contador2 number := 0;

       nome_carga            varchar2(50) := 'IT CHARGEBACK SOFTWARE';
       nome_proc             varchar2(200) := 'USP_PMS_CHARGE_BACK_SOFT';
       mensagem varchar2(3000) := '=== Início da Validacao ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') ||' ===';

begin

   send_mail('lnoboru@cit.com.br','[PMS][' || nome_carga || '] Inicio Validacao', mensagem);

   select count(*) into contador1 from vw_bi_pms_charge_back_soft;

   insert into tb_bi_pms_charge_back_soft
      (select trunc(sysdate),
              sysdate,
              categoria,
              pess_cd_pessoa,
              pess_cd_login,
              tire_cd_ti_recurso,
              chpe_dt_mes,
              chpe_nr_unidades,
              tire_nm_ti_recurso,
              moed_cd_moeda,
              valor_recurso,
              valor_custo_it_chbk_sw_curr,
              valor_custo_it_chbk_sw_brl,
              valor_custo_it_chbk_sw_usd
       from   vw_bi_pms_charge_back_soft);

       commit;

   select count(*) into contador2 from tb_bi_pms_charge_back_soft
   where dt_atualizacao >= trunc(sysdate);

   if contador1 = 0 then
       send_mail('lnoboru@cit.com.br', '[PMS][' || nome_carga || '] ERRO Validacao', 'Erro na view vw_bi_pms_charge_back_soft');
   elsif contador1 <> contador2 then
       send_mail('lnoboru@cit.com.br', '[PMS][' || nome_carga || '] ERRO Validacao', 'Divergência de valores da view ' || contador1 || ' com os valores gravados' || contador2);
  else
       send_mail('lnoboru@cit.com.br', '[PMS][' || nome_carga || '] Fim da  Validacao' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS'), mensagem);
  end if;

end usp_pms_charge_back_soft;