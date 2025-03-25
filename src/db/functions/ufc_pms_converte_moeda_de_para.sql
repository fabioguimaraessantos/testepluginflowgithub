/***************************************************************************
 * Funcao que retorna o valor convertido de uma moeda para outra moeda. 
 * Usa a view vw_pms_voucher_cotacao_moeda para pegar a cotacao das moedas.
 *  
 * Ex: Supondo que a cotacao das moedas sejam:
 *      Dolar (USD) = R$ 2,374
 *      Euro (EUR) = R$ 3,2184
 * 
 *      100,00 dolares retornará o valor de 73,763 euros
 *
***************************************************************************/
create or replace
function UFC_PMS_CONVERTE_MOEDA_DE_PARA (
  p_valor_a_converter IN vw_pms_cotacao_moeda.val_re_valor%type,
  p_moeda_de IN vw_pms_cotacao_moeda.moed_cd_moeda%type,
  p_moeda_para IN vw_pms_cotacao_moeda.moed_cd_moeda%type
  ) RETURN SYS_REFCURSOR 
IS
  refcursor SYS_REFCURSOR;
  v_valor_convertido vw_pms_cotacao_moeda.val_re_valor%type;
  v_valor_cotacao_de vw_pms_cotacao_moeda.val_re_valor%type;
  v_valor_cotacao_para vw_pms_cotacao_moeda.val_re_valor%type;

BEGIN
    ------------------------------------------------------------------------------------------
    -- Function que converte um determinado valor passado por parametro de uma moeda para 
    -- outra moeda. 
    --
    -- Parametros de entrada:
    -- p_valor_a_converter - Valor a ser convertido
    -- p_moeda_de - Código da Moeda na qual o valor se refere
    -- p_moeda_para - Código da Moeda para o qual quer converter o valor
    -- OBS: O código da moeda pode ser pego da tabela MOEDA
    --
    -- Retorno:
    -- Valor convertido na moeda (p_moeda_para)
    ------------------------------------------------------------------------------------------
  if p_moeda_de = 1 then
    -- Se a p_moeda_de igual a BLR seta v_valor_cotacao_de = 1
    v_valor_cotacao_de := 1;
  else  
    -- Pega o valor da moeda origem
    SELECT val_re_valor INTO v_valor_cotacao_de FROM VW_PMS_COTACAO_MOEDA t WHERE t.moed_cd_moeda = p_moeda_de;
  end if;
  
  if p_moeda_para = 1 then
    -- Se a p_moeda_para igual a BLR seta v_valor_cotacao_para = 1
    v_valor_cotacao_para := 1;
  else
    -- Pega o valor da moeda_destino
    SELECT val_re_valor INTO v_valor_cotacao_para FROM VW_PMS_COTACAO_MOEDA t WHERE t.moed_cd_moeda = p_moeda_para;
  end if;
  
  -- Se moeda for igual a BLR, apenas multiplica valores
  if p_moeda_de = 1 then 
  
    v_valor_convertido := p_valor_a_converter / v_valor_cotacao_para;
    
  -- Se moeda for diferente de BLR, converte
  else 
    v_valor_convertido := (p_valor_a_converter * v_valor_cotacao_de) / v_valor_cotacao_para;    
    
  end if;
  
  open refcursor for select v_valor_convertido from dual;
  return refcursor;

END UFC_PMS_CONVERTE_MOEDA_DE_PARA;