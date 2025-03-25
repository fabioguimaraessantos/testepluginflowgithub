create trigger trg_bs_vepe_del_pms 
  before delete
  on ven_pedidovenda
  for each row
    
when (old.ped_in_seqimportacao is not null)

declare
  v_nr_pedido number;

begin
  v_nr_pedido := :old.ped_in_seqimportacao;     

  update mgweb.ven_pedidovenda p
  set p.ped_ch_situacao = 'N',
      p.ped_ch_statusimp = 'N' ,
      p.pe_ped_in_codigo = null,
      p.ser_st_codigo = null,
      p.pe_org_tab_in_codigo = null,
      p.pe_org_pad_in_codigo = null,
      p.pe_org_in_codigo = null,
      p.pe_org_tau_st_codigo = null
  where p.ped_in_sequencia = v_nr_pedido;
  
end;