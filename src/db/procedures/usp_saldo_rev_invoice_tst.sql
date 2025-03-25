create or replace procedure usp_saldo_rev_invoice_tst is
valor_associado number := 0;
ant_revenue number := 0;
ant_invoice number := 0;

saldo_revenue number := 0;
saldo_invoice number := 0;

var_cod_fatura number := 0;
var_total_fautra number := 0;
invoide_date date := null;

qtd_loop integer := 0;

fiscal_deal number := 0;

invoice_not_found  boolean := False;

--VARIAVEL PARA QUANDO ACABOU FATURA MAS RESTOU RECEITA.
change_revenue boolean := False;


   ----------------------------------------------------------------------------------------------------------------------------------------------------------------
   -- DECLARANDO CURSOR DA RECEITA
   cursor cursor_receita is
          select * from 
          (
                         select rec.cd_deal_fiscal,
                                rec.nome_deal_fiscal,
                                rec.dt_mes,
                                rec.rece_vl_total_receita,
                                rec.redf_vl_receita,
                                rec.valor_ajuste,
                                
                                case when (aux.valor_saldo_revenue  is not null) then aux.valor_saldo_revenue
                                        else  rec.valor_total_receita
                               end valor_total_receita,
                               
                               case when aux.valor_saldo_revenue is null then 0 else aux.valor_saldo_revenue end saldo,          
                               
                                rec.cd_receita_dfiscal,
                                rec.cd_receita
                         from   (select rdf.defi_cd_deal_fiscal cd_deal_fiscal,
                                        df.defi_nm_deal_fiscal nome_deal_fiscal,
                                        rec.rece_dt_mes dt_mes,
                                        rec.rece_vl_total_receita, -- original 
                                        rdf.redf_vl_receita, -- boica
                                        ajuste.vl_ajuste_total valor_ajuste,
                                        round(rdf.redf_vl_receita + nvl(ajuste.vl_ajuste_total, 0), 2) valor_total_receita, -- USADO PELO BOICA
                                        --round(rec.rece_vl_total_receita + nvl( ajuste.vl_ajuste_total,0),2)  valor_total_receita,
                                        rdf.redf_cd_receita_dfiscal cd_receita_dfiscal,
                                        rec.rece_cd_receita         cd_receita
                                 from   receita_deal_fiscal rdf,
                                        receita rec,
                                        deal_fiscal df,
                                        (select aj.redf_cd_receita_dfiscal,
                                                sum(aj.ajre_vl_ajuste) vl_ajuste_total
                                         from   pms.ajuste_receita aj
                                         group  by aj.redf_cd_receita_dfiscal) ajuste
                                 where  rdf.defi_cd_deal_fiscal = df.defi_cd_deal_fiscal
                                        and rdf.rece_cd_receita = rec.rece_cd_receita
                                        and
                                        ajuste.redf_cd_receita_dfiscal(+) = rdf.redf_cd_receita_dfiscal
                                       --and rdf.defi_cd_deal_fiscal in (386) --=====> PARA TESTE
                                       --and round(rec.rece_vl_total_receita + nvl( ajuste.vl_ajuste_total,0),2)  >= 0  -- ORIGIANL
                                       --and  round(rdf.redf_vl_receita + nvl( ajuste.vl_ajuste_total,0),2) >= 0 -- BOICA
                                        and rec.rece_cd_receita not in
                                        ( ---SÃO RECEITAS QUE NÃO TEM MAIS SALDO
                                         (select cd_receita
                                              from   (select cd_fiscal_deal,
                                                             cd_receita,
                                                             row_number() over(partition by cd_receita, cd_fiscal_deal order by cd_receita, cd_fiscal_deal) linha
                                                      from   tb_teste_saldo_rev_invoice s
                                                      where  s.valor_saldo_revenue <= 0)
                                              where  linha = 1))
                                 order  by df.defi_cd_deal_fiscal,
                                           rec.rece_dt_mes) rec,
                                vw_teste_aux_fiscal_balance aux
                         where  rec.cd_deal_fiscal = aux.cd_fiscal_deal(+)
                                and rec.cd_receita = aux.cd_receita(+)
           )
           where valor_total_receita > 0;                     

   --DECLARANDO VARIAVEL PARA O CURSOSR RECEITA
   var_receita cursor_receita%rowtype;


begin ---usp_saldo_rev_invoice3;

   send_mail('lnoboru@ciandt.com','FISCAL BALANCE', 'Começo do Teste   '     || to_char(sysdate));     

   open cursor_receita; --001

   loop --- LOOP PARA PERCORRER A RECEITA (REVENUE).
    
            
       
    
        --if i > 10002 then -- teste apenas, é para forçar o fim em caso de entrar em loop infinito
       --     exit;
        --end if;

             if (saldo_revenue <= 0)  then
                  --Lendo registro do cursor
                  fetch cursor_receita into var_receita;
                  change_revenue := False;
             elsif ( (saldo_revenue >0) and (invoice_not_found) and (qtd_loop  > 2 )) then
                  fetch cursor_receita into var_receita;
                  change_revenue := True;
             else
                 change_revenue := False;
             end if;
            

            -- Verifica se chegou ao fim. Se sim, cai fora
            exit when cursor_receita%notfound;
            
            if (fiscal_deal =  0) then
                fiscal_deal := var_receita.cd_deal_fiscal;  
                
                --execute immediate  'truncate table  tb_bi_pms_saldo_rev_invoice';
                --execute immediate 'TRUNCATE TABLE fatura_receita';                                               
           elsif fiscal_deal <>  var_receita.cd_deal_fiscal then
                fiscal_deal := var_receita.cd_deal_fiscal;

                    valor_associado  := 0;
                    ant_revenue  := 0;
                    ant_invoice  := 0;
                    
                    saldo_revenue  := 0;
                    saldo_invoice  := 0;
                    
                    var_cod_fatura  := 0;
                    var_total_fautra  := 0;
                    invoide_date  := null;
                    
                    qtd_loop  := 0;
                    
                    invoice_not_found  := False;
                    
                    change_revenue  := False;                
           end if;   
             
            if (saldo_revenue = 0) and (saldo_invoice > 0) then
                                           
                                         saldo_revenue := case when saldo_revenue < 0 then 0 else saldo_revenue end;
                                         saldo_invoice := case when saldo_invoice < 0 then 0 else saldo_invoice end;
                                           
                                          valor_associado :=   Least(var_receita.valor_total_receita, saldo_invoice);

                                         saldo_revenue := var_receita.valor_total_receita - saldo_invoice;

                                         saldo_invoice := saldo_invoice - var_receita.valor_total_receita;
                                         
                                         saldo_revenue := case when saldo_revenue < 0 then 0 else saldo_revenue end;
                                         saldo_invoice := case when saldo_invoice < 0 then 0 else saldo_invoice end;
                                         
                                         insert into tb_teste_saldo_rev_invoice
                                         (
                                                cd_sequencial,
                                                 mes_processamento,
                                                 cd_fiscal_deal,
                                                 cd_receita,
                                                 cd_fatura,
                                                 valor_saldo_revenue,
                                                 valor_saldo_invoice,
                                                 valor_revenue,
                                                 valor_invoice,
                                                 VALOR_ASSOCIADO,
                                                 VALOR_SALDO_ANT_INV,
                                                 VALOR_SALDO_ANTERIOR_REV,
                                                 DATA_REVENUE,
                                                 DATA_INVOICE
                                         )
                                         values
                                        (
                                                (select nvl(max(cd_sequencial), 0) + 1 from tb_teste_saldo_rev_invoice),
                                                 to_date('01'|| to_char(sysdate,'/MM/YYYY')),
                                                 var_receita.cd_deal_fiscal,
                                                 var_receita.cd_receita,
                                                 var_cod_fatura,
                                                 case when saldo_revenue < 0 then 0 else saldo_revenue end,
                                                 case when saldo_invoice < 0 then 0 else saldo_invoice end,
                                                 var_receita.valor_total_receita,
                                                 var_total_fautra,
                                                 valor_associado,
                                                 ant_invoice,
                                                 ant_revenue,
                                                 var_receita.dt_mes,
                                                 invoide_date
                                         );



                                        insert into fatura_receita_tst
                                           (
                                           fare_cd_fatura_receita,
                                            fatu_cd_fatura,
                                            redf_cd_receita_dfiscal,
                                            fare_vl_receita_associada
                                            )
                                           values
                                           (
                                               --var_receita. cd_receita,
                                              (select nvl(max(fare_cd_fatura_receita), 0) + 1 from  fatura_receita_tst) ,
                                               var_cod_fatura,
                                               --var_receita.cd_deal_fiscal,
                                               var_receita.cd_receita_dfiscal,
                                               valor_associado
                                           );

                                         var_total_fautra  :=  var_total_fautra;
                                         var_cod_fatura := var_cod_fatura;

                                         invoide_date :=  invoide_date;
                                
                                         commit;

                                       ant_revenue  := saldo_revenue;
                                       ant_invoice  := saldo_invoice;
            else
            ----------------------------------------------------------------------------------------------------------------------------------------
            --- inicio do ELSE
             ---------------------------------------------------------------------------------------------------------------------------------------------------------------
                           --DECLARANDO CURSOR DE INVOICE
                           declare
                           cursor cursor_invoice is
                                        select fat.defi_cd_deal_fiscal,
                                                  df_inv.defi_nm_deal_fiscal nome_deal_fiscal,
                                                  fat.fatu_dt_previsao dt_mes_fatura,
                                                  ifat.valor_item_fatura,
                                                  ifat.fatu_cd_fatura
                                        from   fatura fat,
                                                  (select fatu_cd_fatura,
                                                          valor_item_fatura
                                                   from   (select fatu_cd_fatura,
                                                                  sum(itfa_vl_item) over(partition by fatu_cd_fatura order by fatu_cd_fatura) valor_item_fatura,
                                                                  row_number() over(partition by fatu_cd_fatura order by fatu_cd_fatura) nlinha
                                                           from   item_fatura i
                                                           where i.fore_cd_fonte_receita = 1
                                                           )
                                                   where  nlinha = 1) ifat,
                                                  deal_fiscal df_inv
                                         where  fat.fatu_cd_fatura = ifat.fatu_cd_fatura
                                                    and
                                                    fat.fatu_in_status = 'SB'
                                                    and
                                                     fat.defi_cd_deal_fiscal = df_inv.defi_cd_deal_fiscal
                                                    and
                                                    fat.defi_cd_deal_fiscal = var_receita.cd_deal_fiscal
                                                     --and  fat.fatu_cd_fatura NOT in (20379,19343,20612) -- TESTE APENAS
                                                    and ifat.fatu_cd_fatura not in
                                                                                        (select cd_fatura
                                                                                          from   (select cd_fatura,
                                                                                                                 cd_fiscal_deal,
                                                                                                                 row_number() over(partition by cd_fatura, cd_fiscal_deal order by cd_fatura, cd_fiscal_deal) linha
                                                                                                       from   tb_teste_saldo_rev_invoice
                                                                                                       where  valor_saldo_invoice <= 0
                                                                                                 )
                                                                                          where  linha = 1
                                                                                        )
                                         order  by 1,3;


                              --DECLARANDO VARIAVEL PARA O CURSOSR INVOICE
                              var_invoice cursor_invoice%rowtype;
                             
 
                               begin
                               -------------------------------------------------------------------------------------------------------------------------------------------
                                   open cursor_invoice;

                                   loop ---LOOP DA FATURA (INVOICE)
                                           --change_revenue := False;
                                           
                                                    


                                           if saldo_invoice <= 0 then
                                              --Lendo registro do cursor
                                              fetch cursor_invoice
                                              into var_invoice;
                                           end if;

                                              -- Verifica se chegou ao fim. Se sim, cai fora
                                              --exit when cursor_invoice%notfound;
                                              if  cursor_invoice%notfound then
                                                   invoice_not_found := True;
                                                    qtd_loop := qtd_loop + 1;
                                                  exit;
                                              else 
                                                   invoice_not_found  := False;
                                                   qtd_loop := 0;
                                              end if;      

                                           saldo_revenue := case when saldo_revenue < 0 then 0 else saldo_revenue end;
                                           saldo_invoice := case when saldo_invoice < 0 then 0 else saldo_invoice end;

                                          if  (saldo_revenue = 0) and (saldo_invoice = 0) then
                                                 valor_associado := Least(var_invoice.valor_item_fatura,  var_receita.valor_total_receita);
                                                 saldo_revenue := var_receita.valor_total_receita - valor_associado;
                                                 saldo_invoice := var_invoice.valor_item_fatura - valor_associado;
                                           elsif  (saldo_revenue > 0) and (saldo_invoice > 0) then
                                                 valor_associado := Least(saldo_revenue, saldo_invoice);
                                                 saldo_revenue := saldo_revenue - valor_associado;
                                                 saldo_invoice := saldo_invoice - valor_associado;
                                                 
                                           elsif  ((saldo_revenue > 0) and (saldo_invoice <= 0) and (change_revenue)) then -- CASO ABACA A FATURA MAS SOBRE A RECEITA.
                                                  saldo_revenue := saldo_revenue + var_receita.valor_total_receita;
                                                  valor_associado :=  Least(var_invoice.valor_item_fatura,  var_receita.valor_total_receita, valor_associado);
                                                  
                                                  saldo_revenue := saldo_revenue - valor_associado;
                                                  saldo_invoice := var_invoice.valor_item_fatura - valor_associado;                                                  
                                           else
                                                 valor_associado :=  saldo_revenue + saldo_invoice;
                                                 valor_associado :=  Least(var_invoice.valor_item_fatura,  var_receita.valor_total_receita, valor_associado);

                                                 if   (saldo_revenue > 0)  then
                                                       saldo_revenue := saldo_revenue - valor_associado;
                                                        saldo_invoice := var_invoice.valor_item_fatura - valor_associado;
                                                  else
                                                        saldo_revenue := var_receita.valor_total_receita - valor_associado;
                                                        saldo_invoice := saldo_invoice - valor_associado;
                                                 end if;
                                           end if;


                                           saldo_revenue := case when saldo_revenue < 0 then 0 else saldo_revenue end;
                                           saldo_invoice := case when saldo_invoice < 0 then 0 else saldo_invoice end;


                                         var_total_fautra  :=  var_invoice.valor_item_fatura;
                                         var_cod_fatura := var_invoice.fatu_cd_fatura;

                                         invoide_date := var_invoice.dt_mes_fatura;

                                           insert into tb_teste_saldo_rev_invoice
                                              (cd_sequencial,
                                               mes_processamento,
                                               cd_fiscal_deal,
                                               cd_receita,
                                               cd_fatura,
                                               valor_saldo_revenue,
                                               valor_saldo_invoice,
                                               valor_revenue,
                                               valor_invoice,
                                               valor_associado,
                                               valor_saldo_ant_inv,
                                               valor_saldo_anterior_rev,
                                               data_revenue,
                                               data_invoice)
                                           values
                                              ((select nvl(max(cd_sequencial), 0) + 1
                                               from   tb_teste_saldo_rev_invoice),
                                               to_date('01' || to_char(sysdate, '/MM/YYYY')),
                                               var_receita.cd_deal_fiscal,
                                               var_receita. cd_receita,
                                               var_invoice.fatu_cd_fatura,

                                               saldo_revenue,
                                               saldo_invoice,

                                               var_receita.valor_total_receita,
                                               var_invoice.valor_item_fatura,
                                               valor_associado,
                                               ant_invoice,
                                               ant_revenue,
                                               var_receita.dt_mes,
                                               var_invoice.dt_mes_fatura);

                                        insert into fatura_receita_tst
                                           (
                                             fare_cd_fatura_receita,
                                            fatu_cd_fatura,
                                            redf_cd_receita_dfiscal,
                                            fare_vl_receita_associada
                                            )
                                           values
                                           (
                                               --var_receita.cd_receita,
                                               (select nvl(max(fare_cd_fatura_receita), 0) + 1 from  fatura_receita_tst),
                                                var_invoice.fatu_cd_fatura,
                                               --var_receita.cd_deal_fiscal,
                                               var_receita.cd_receita_dfiscal,
                                               valor_associado
                                           );
                                               
                                           commit;

                                       ant_revenue  := saldo_revenue;
                                       ant_invoice  := saldo_invoice;


                                        if  (saldo_revenue <=  0)  then
                                              --fetch cursor_receita into var_receita;
                                              --change_revenue := True;
                                              exit;
                                        end if;
                              end loop; -- FIM DO LOOP DA FATURA
                               -------------------------------------------------------------------------------------------------------------------------------------------


                              close cursor_invoice;
                              end;



            -- fim do ELSE
            ----------------------------------------------------------------------------------------------------------------------------------------
            end if;




   end loop; -- FIM DO LOOP DA RECEITA


   close cursor_receita; -- Fecha o Cursor. --001
   
   send_mail('lnoboru@ciandt.com','FISCAL BALANCE', 'Fim Teste   '    ||  to_char(sysdate));      

end usp_saldo_rev_invoice_tst;