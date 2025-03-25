create or replace procedure usp_pms_voucher is

   log_data              date := trunc(sysdate);
   log_dt_carga_processo date;
   nome_carga            varchar2(50) := ' USP_PMS_VOUCHER ';
   sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := ' USP_PMS_VOUCHER ';
   sms_msg               varchar2(200) := 'Objeto: ';

   contador integer := 0;
   erro integer := 0;


   crlf      varchar2(2) := chr(13) || chr(10);
   assunto   varchar2(200) := '';
   mensagem  varchar2(4000) := '=== Inicio da Carga ' ||
                               to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') ||
                               '  ===';


----------------------------------------------------------------------------------------------------------------------------------------------


i integer := 0;  -- Para fazer o loop para gravar as parcelas

dt_mes date := null;
periodo_quebrado boolean := False;


numero_meses number := 0;
meses_inteiros number := 0;
--meses_quebrados number := 0;   ---???

dt_inicio date := null;
dt_fim date := null;
valor_orcado number := 0;

dia_inicio number := 0;
mes_ano_inicio varchar2(7) := null;
ultimo_dia_mes_inicio number := 0;

dia_fim number := 0;
mes_ano_fim varchar2(7) := null;
ultimo_dia_mes_fim number := 0;

-------------------------------------------------------------------------------------------------------------------------------------------


valor_parcelas number := 0;
valor_da_parcela number := 0;

valor_parcelas_incio number := 0;
valor_parcelas_fim number := 0;
soma_diferenca  number := 0;

pct_dt_inicio number := 0;
pct_dt_fim number := 0;


-------------------------------------------------------------------------------------------------------------------------------------------


      --CURSOR DE ORÇAMENTO DE DESPESAS
        cursor cursor_orc_despesa is
                              select orde_cd_orc_despesa,
                                     clie_cd_cliente,
                                     moed_cd_moeda,
                                     orde_dt_inicio,
                                     case
                                        when orde_dt_fim is null then
                                         para_dt_parametro
                                        else
                                         orde_dt_fim
                                     end orde_dt_fim,
                                     orde_tp_orcamento,
                                     orde_vl_orcado
                              from   orcamento_despesa,
                                     pms20.parametro
                              --where orde_cd_orc_despesa = 101      -- teste
                              order  by orde_dt_inicio;

   --VARIÁVEL PARA CURSOR RECEITA
   var_despesa cursor_orc_despesa%rowtype;
begin

     open cursor_orc_despesa;


     begin
          execute immediate 'TRUNCATE TABLE TB_BI_PMS_RATEIO_TRAVEL_BUDGET';
    exception when others then
                        mensagem := mensagem || crlf || crlf ||
                              '[ERRO] ##### PMS.TB_BI_PMS_RATEIO_TRAVEL_BUDGET  - (LIMPAR A  TABELA)  ##### - ' || crlf ||
                              '       SQL Erro: ' || sqlerrm || crlf ||
                              '       Data Execucao: ' ||
                              to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
    end;

   send_mail('lnoboru@ciandt.com', assunto, mensagem);
   send_mail('alexandrel@ciandt.com', assunto, mensagem);
   send_mail('tspadari@ciandt.com', assunto, mensagem);
   send_mail('llino@ciandt.com', assunto, mensagem);
   send_mail('andreiap@ciandt.com', assunto, mensagem);

------------------------------------------------------------------------------------------------------------------------------
-- COMEÇO DO LOOP
------------------------------------------------------------------------------------------------------------------------------
      loop

          fetch  cursor_orc_despesa into var_despesa;

          -- Se for o fim do CURSOR CAI FORA DO LOOP
          exit when cursor_orc_despesa%notfound;

--------------------------------------------------------------------------------------------------------------
-- INÍCIO DA COLETA DE DADOS
--------------------------------------------------------------------------------------------------------------

          dt_inicio := var_despesa.orde_dt_inicio;
          dt_fim := var_despesa.orde_dt_fim;
          valor_orcado := var_despesa.orde_vl_orcado;

          -- NÚMERO DE MESES ESTREA AS DATAS
          numero_meses := 1 +  MONTHS_BETWEEN( to_date('01/' || to_char(dt_fim, 'MM/YYYY'), 'DD/MM/YYYY') ,  to_date('01/' || to_char(dt_inicio, 'MM/YYYY'), 'DD/MM/YYYY') );
          if (numero_meses=0)
             then  numero_meses := 1;
          end if;   --- PARA IMPEDIR DIVISÃO POR ZERO

          dia_inicio := to_number(to_char(dt_inicio, 'dd'));
          mes_ano_inicio  := to_char(dt_inicio, 'mm/yyyy');
          ultimo_dia_mes_inicio:=  to_number(to_char(last_day(dt_inicio), 'dd'));
         if (ultimo_dia_mes_inicio=0)
            then ultimo_dia_mes_inicio := 1;
         end if;  --- PARA IMPEDIR DIVISÃO POR ZERO

          dia_fim := to_number(to_char(dt_fim, 'dd'));
          mes_ano_fim  := to_char(dt_fim, 'mm/yyyy');
          ultimo_dia_mes_fim:=  to_number(to_char(last_day(dt_fim), 'dd'));
          if ( ultimo_dia_mes_fim=0)
             then  ultimo_dia_mes_fim := 1;
          end if;   --- PARA IMPEDIR DIVISÃO POR ZERO

--------------------------------------------------------------------------------------------------------------
--  FIM DA COLETA DE DADOS
-------------------------------------------------------------------------------------------------------------

       meses_inteiros :=  numero_meses;
       valor_parcelas := valor_orcado / numero_meses;

        --verifica se é periodo quebrado
        periodo_quebrado := not ((dia_inicio = 01)  and   ( dia_fim = ultimo_dia_mes_fim));

-------------------------------------------------------------------------------------------------------------------------------------------------
         if periodo_quebrado then  --- 001
                   -- PORCENTAGEM DE INÍCIO E FIM
                   -- Só será usado quando o periodo for quebrado
                   --Exemplo: INICIO: 01-Out-2012  e FIM: 28-Fev-2013
                   --                 Ou seja, o DIA da DATA DE INICIO é o primeiro dia do mês
                   --                    E  o DIA da DATA FIM é o último dia do mês
                   -- Essas variáveis serão aplicadas na primeria e última parcela
                    pct_dt_inicio := 1 - (dia_inicio /  ultimo_dia_mes_inicio);
                    pct_dt_fim := 1 - (dia_fim / ultimo_dia_mes_fim);

                    valor_parcelas_incio := valor_parcelas * pct_dt_inicio;
                    valor_parcelas_fim := valor_parcelas *  pct_dt_fim;

                     if  numero_meses <= 2 then
                         soma_diferenca := (valor_parcelas - valor_parcelas_incio) + (valor_parcelas - valor_parcelas_fim);
                    else
                         soma_diferenca := ((valor_parcelas - case when valor_parcelas_incio = 0 then valor_parcelas else valor_parcelas_incio end) + (valor_parcelas -  case when valor_parcelas_fim = 0 then valor_parcelas else valor_parcelas_fim end));
                    end if;

                    --TOTA DE MESES MENOS - O INICIO E O FIM QUE SÃO QUEBRADOS
                    meses_inteiros  := meses_inteiros - 2;

                    --- ESSES DOIS IF SÃO PARA VERIFICAR SE O MÊS DE INÍCIO E FIM SÃO MESES FECHADOS
                    -- E FAZ UM AJUSTE NA QUANTIDADE CORRETA DE MESES INTEIROS
                    if  (dia_inicio = 01) and  ((to_date(to_char(ultimo_dia_mes_inicio,'99') || '/' || mes_ano_inicio,'dd/mm/yyyy'))   < dt_fim) then
                          meses_inteiros :=   meses_inteiros + 1;
                    end if;

                    if ((to_date(to_char(ultimo_dia_mes_inicio,'99') || '/' || mes_ano_inicio,'dd/mm/yyyy'))  <  (to_date('01/' || mes_ano_fim,'dd/mm/yyyy')))
                       and  (dt_fim =  (to_date(to_char(ultimo_dia_mes_fim,'99') || '/' || mes_ano_fim,'dd/mm/yyyy'))) then
                        meses_inteiros :=   meses_inteiros + 1;
                    end if;

                    ---SE TEM parecela entre o INÍCIO e o FIM
                    if  numero_meses > 2 then
                        valor_parcelas := valor_parcelas + (soma_diferenca/meses_inteiros);
                    else
                         valor_parcelas :=   (soma_diferenca/meses_inteiros);
                    end if;

           end if;  -- --- 001
-------------------------------------------------------------------------------------------------------------------------------------------------


          i := 0;
          while (i < numero_meses) loop
                --- Pega o mês de inicio é vai acrescentando o mês conforme o número de parcelas
                --- a variável i representa o número de meses a serem somados.
                -- Começa com zero, ou seja, a primeira vez é o mês de início e em cada loop
                -- vai somando +1
                dt_mes :=  add_months( to_date('01' || to_char(dt_inicio,'/mm/yyyy'),'dd/mm/yyyy'),i);

                    if (periodo_quebrado and  (dt_mes =   (to_date('01' ||  mes_ano_inicio,'dd/mm/yyyy')) )) then
                          valor_da_parcela :=  valor_parcelas_incio;
                    elsif  (periodo_quebrado and  (dt_mes =   (to_date('01' ||  mes_ano_fim,'dd/mm/yyyy')) )) then
                          valor_da_parcela :=  valor_parcelas_fim;
                    else
                         valor_da_parcela := valor_parcelas;
                    end if;

                    if  valor_da_parcela = 0 then valor_da_parcela  :=  valor_parcelas; end if;

                    if (numero_meses = 1) and (mes_ano_inicio = mes_ano_fim)  then
                        valor_da_parcela := var_despesa.orde_vl_orcado;

                    end if;


               begin --- INÍCIO DO TRY
                          insert into tb_bi_pms_rateio_travel_budget
                             (
                                   cd_orcamento,
                                   clie_cd_cliente,
                                   moed_cd_moeda,
                                   valor_total_orcado,
                                   dt_inicio,
                                   dt_fim,
                                   mes,
                                   percentual_aplicado,
                                   valor_parcela
                              )
                          values
                             (
                                   var_despesa.orde_cd_orc_despesa,
                                   var_despesa.clie_cd_cliente,
                                   var_despesa.moed_cd_moeda,
                                   var_despesa.orde_vl_orcado,
                                   var_despesa.orde_dt_inicio,
                                   var_despesa.orde_dt_fim,
                                   dt_mes,
                                  (valor_da_parcela / var_despesa.orde_vl_orcado),
                                  valor_da_parcela
                             );
                             commit;
                   exception when others then
                                  mensagem := mensagem || crlf || crlf ||
                                        '[ERRO] ##### PMS.TB_BI_PMS_RATEIO_TRAVEL_BUDGET  - (INSERT)  ##### - ' || crlf ||
                                        '       SQL Erro: ' || sqlerrm || crlf ||
                                        '       Data Execucao: ' ||
                                        to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  end;  -- FIM DO TRY

                i := i + 1;

          end loop;


      end loop;
------------------------------------------------------------------------------------------------------------------------------
-- FIM DO LOOP
------------------------------------------------------------------------------------------------------------------------------

     close cursor_orc_despesa;


   send_mail('lnoboru@ciandt.com', assunto, mensagem);
   send_mail('alexandrel@ciandt.com', assunto, mensagem);
   send_mail('tspadari@ciandt.com', assunto, mensagem);
   send_mail('llino@ciandt.com', assunto, mensagem);
   send_mail('andreiap@ciandt.com', assunto, mensagem);


end usp_pms_voucher;
