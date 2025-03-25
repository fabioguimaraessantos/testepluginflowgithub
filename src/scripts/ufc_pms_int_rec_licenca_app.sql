create or replace FUNCTION ufc_pms_int_rec_licenca_app (
    p_cd_receita_licenca IN NUMBER)
  RETURN SYS_REFCURSOR
IS

  /**
   * Integra receita de licenca do PMS no Mega.
   *
   * @author Marcos Vidolin - mvidolin@ciandt.com
   * @since Sep 11, 2014
   */

  refcursor SYS_REFCURSOR;
  p_result NUMBER;
  -------------------------
  -- Escopo de variáveis --
  -------------------------
  v_message               VARCHAR2(2000);
  v_error_code            NUMBER;
  v_error_num             NUMBER;
  v_error_text            VARCHAR2(200);
  v_ped_in_sequencia      NUMBER;
  v_reli_dt_mes           DATE;
  v_valor_receita_licenca NUMBER := 0;
  v_defi_cd_deal_fiscal   NUMBER;
  v_clie_cd_cliente       NUMBER;
  v_clie_in_tipo          VARCHAR2(5);
  v_empr_cd_empresa       NUMBER;
  v_nr_cgc                VARCHAR2(50);
  v_erp_cd_projeto        NUMBER;
  v_erp_cd_proj_ide       NUMBER;
  v_erp_cd_ccusto         NUMBER;
  v_erp_cd_cus_ide        NUMBER;
  v_erp_cd_filial         NUMBER;
  v_erp_ind_in_codigo     NUMBER;
  v_reli_in_status        VARCHAR2(1);
  v_count_int_receita_df  NUMBER;
  v_infp_cd_erp_item      NUMBER;
  v_infp_cd_erp_servico   NUMBER;
  v_tise_cd_tipo_servico  NUMBER;
  --------------------------
  -- Escopo de constantes --
  --------------------------
  con_cod_cgc             CONSTANT VARCHAR2(3)  := 'CGC';
  con_cod_avista          CONSTANT VARCHAR2(8)  := 'PMS';
  con_cod_usu_inclusao    CONSTANT NUMBER       := -1;
  con_cod_st_classe       CONSTANT VARCHAR2(10) := 'Vd Serv';
  con_st_descricao        CONSTANT VARCHAR(50)  := 'Item Receita';
  con_st_descricao_fatura CONSTANT VARCHAR(50)  := 'Item Fatura';
  -- Codigo da acao
  con_acao_in_codigo CONSTANT NUMBER := 573;
  -- Codigo da aplicacao
  con_apl_in_codigo CONSTANT NUMBER := 26;
  con_tpd_in_codigo CONSTANT NUMBER := 33;
  con_cos_in_codigo CONSTANT NUMBER := 1;
  con_fonte_receita NUMBER          := 1;
BEGIN
  v_message := '';
  -- Sequencial + 1
  SELECT NVL(MAX(PED_IN_SEQUENCIA),0)+1
  INTO v_ped_in_sequencia
  FROM VEN_PEDIDOVENDA;
  v_error_num := 0;
  BEGIN

    -- Busca o código do Deal Fiscal, Indice Financeiro, Valor da Receita Licenca, Status, Mes da Receita
    SELECT rl.DEFI_CD_DEAL_FISCAL, m.MOED_CD_ERP_IND_FIN, rl.RELI_VL_RECEITA, rl.RELI_IN_STATUS, rl.RELI_DT_MES
    INTO v_defi_cd_deal_fiscal, v_erp_ind_in_codigo, v_valor_receita_licenca, v_reli_in_status, v_reli_dt_mes
    FROM RECEITA_LICENCA rl, moeda m
    WHERE m.MOED_CD_MOEDA          = rl.MOED_CD_MOEDA
    AND rl.RELI_CD_RECEITA_LICENCA = p_cd_receita_licenca;
    v_error_num                   := v_error_num + 1;

    IF (v_reli_in_status IS NULL OR v_reli_in_status = 'W') THEN

      -- Codigo da Empresa Cliente e CiT
      SELECT df.clie_cd_cliente, df.empr_cd_empresa
      INTO v_clie_cd_cliente, v_empr_cd_empresa
      FROM deal_fiscal df
      WHERE df.defi_cd_deal_fiscal = v_defi_cd_deal_fiscal;
      v_error_num := v_error_num + 1;

      -- Numero do CNPJ
      SELECT c.clie_cd_cnpj, c.clie_in_tipo
      INTO v_nr_cgc, v_clie_in_tipo
      FROM cliente c
      WHERE c.clie_cd_cliente = v_clie_cd_cliente;
      v_error_num := v_error_num + 1;


      IF (v_nr_cgc IS NOT NULL) THEN
      
        -- TODO: Alterar para pegar o projeto do c-lob e nao da empresa (Convergencia)
        -- Dados da Filial CiT
        SELECT e.empr_cd_erp_ccusto, e.empr_cd_erp_cus_ide, e.empr_cd_erp_projeto, e.empr_cd_erp_proj_ide, e.empr_cd_erp_filial
        INTO v_erp_cd_ccusto, v_erp_cd_cus_ide, v_erp_cd_projeto, v_erp_cd_proj_ide, v_erp_cd_filial
        FROM empresa e
        WHERE e.empr_cd_empresa = v_empr_cd_empresa;
        v_error_num := v_error_num + 1;
        
        IF (v_erp_cd_ccusto IS NOT NULL 
            AND v_erp_cd_cus_ide IS NOT NULL 
            AND v_erp_cd_projeto IS NOT NULL 
            AND v_erp_cd_proj_ide IS NOT NULL 
            AND v_erp_cd_filial IS NOT NULL) THEN

          INSERT INTO VEN_PEDIDOVENDA
            (PED_IN_SEQUENCIA ,
              CLI_ST_TIPOCODIGO ,
              FIL_IN_CODIGO ,
              PED_IN_CODIGO ,
              TPD_IN_CODIGO ,
              CLI_ST_CODIGO ,
              CLI_TAU_ST_CODIGO ,
              COND_ST_CODIGO ,
              REP_IN_CODIGO ,
              TRA_IN_CODIGO ,
              ACAO_IN_CODIGO ,
              PED_DT_EMISSAO ,
              PED_CH_SITUACAO ,
              PED_CH_STATUSIMP ,
              ENA_IN_CODIGO ,
              IND_IN_CODIGO ,
              EQU_IN_CODIGO ,
              UIN_IN_CODIGO)
            VALUES
            (v_ped_in_sequencia ,
              con_cod_cgc ,
              v_erp_cd_filial ,
              v_ped_in_sequencia ,
              con_tpd_in_codigo ,
              v_nr_cgc ,
              'C' ,
              con_cod_avista ,
              NULL ,
              NULL ,
              con_acao_in_codigo ,
              last_day(v_reli_dt_mes) ,
              'N' ,
              'N' ,
              NULL ,
              v_erp_ind_in_codigo ,
              NULL ,
              con_cod_usu_inclusao);
          v_error_num := v_error_num + 1;

          -- seleciona tipo de servico
          SELECT tsdf.tise_cd_tipo_servico
          INTO v_tise_cd_tipo_servico
          FROM tipo_servico_deal_fiscal tsdf
          WHERE tsdf.defi_cd_deal_fiscal = v_defi_cd_deal_fiscal;

          -- seleciona pro_in_codigo e cos_in_codigo (respectivamente)
          SELECT infp_cd_erp_item, infp_cd_erp_servico
          INTO v_infp_cd_erp_item, v_infp_cd_erp_servico
          FROM integ_fatura_parametro i
          WHERE i.tise_cd_tipo_servico = v_tise_cd_tipo_servico
          AND i.fore_cd_fonte_receita  = con_fonte_receita --con_fonte_receita = 1 (constante)
          AND i.empr_cd_empresa        = v_empr_cd_empresa;


          INSERT INTO VEN_ITEMPEDIDOVENDA
            (PED_IN_SEQUENCIA ,
              ITP_IN_SEQUENCIA ,
              APL_IN_CODIGO ,
              PROJ_IDE_ST_CODIGO ,
              PROJ_IN_REDUZIDO ,
              TPR_IN_CODIGO ,
              TPP_IN_CODIGO ,
              CUS_IDE_ST_CODIGO ,
              CUS_IN_REDUZIDO ,
              ITP_CH_FRETEPCONTA ,
              PRO_IN_CODIGO ,
              ITP_ST_DESCRICAO ,
              ITP_RE_QUANTIDADE ,
              itp_re_valorunitario ,
              ITP_RE_VALORTOTAL ,
              TPC_ST_CLASSE ,
              ITP_CH_STATUSIMP ,
              UIN_IN_CODIGO ,
              COS_IN_CODIGO)
            VALUES
            (v_ped_in_sequencia ,
              1 ,
              con_apl_in_codigo ,
              TO_CHAR(v_erp_cd_proj_ide) ,
              v_erp_cd_projeto ,
              NULL ,
              NULL ,
              TO_CHAR(v_erp_cd_cus_ide) ,
              v_erp_cd_ccusto ,
              1 ,
              v_infp_cd_erp_item -- código do item/produto
              ,
              con_st_descricao ,
              1 ,
              v_valor_receita_licenca ,
              v_valor_receita_licenca ,
              con_cod_st_classe ,
              'S' ,
              con_cod_usu_inclusao ,
              v_infp_cd_erp_servico);
          
          -- Receita Integrada
          UPDATE receita_licenca r
          SET r.RELI_IN_STATUS = 'I', r.reli_cd_erp_pedido = v_ped_in_sequencia
          WHERE r.reli_cd_receita_licenca = p_cd_receita_licenca;
          COMMIT;

          v_error_num := v_error_num + 1;

          COMMIT;
          p_result := 1;
        ELSE
          v_message := 'Empresa Id: ' || v_empr_cd_empresa || ' possui parâmetros inválidos';
          UPDATE receita_licenca rl
          SET rl.RELI_IN_STATUS            = 'E',
            rl.reli_tx_error               = v_message
          WHERE rl.RELI_CD_RECEITA_LICENCA = p_cd_receita_licenca;
          COMMIT;
          p_result := 0;
        END IF;
      ELSE
        v_message := 'Cliente id: '|| v_clie_cd_cliente || ' com cnpj nulo';
        UPDATE receita_licenca rl
        SET rl.RELI_IN_STATUS            = 'E',
          rl.reli_tx_error               = v_message
        WHERE rl.RELI_CD_RECEITA_LICENCA = p_cd_receita_licenca;
        COMMIT;
        p_result := 0;
      END IF;
    ELSE
      p_result := 0;
    END IF;
  EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    v_error_code := SQLCODE;
    v_error_text := SUBSTR(SQLERRM, 1,200);
    v_message    := 'Error('|| v_error_num|| ')'||' ['||v_error_code||']'|| ' - ' || v_error_text;
    UPDATE receita_licenca rl
    SET rl.RELI_IN_STATUS            = 'E',
      rl.reli_tx_error               = v_message
    WHERE rl.RELI_CD_RECEITA_LICENCA = p_cd_receita_licenca;
    COMMIT;
    p_result := 0;
  END;
  OPEN refcursor FOR SELECT p_result FROM dual;
  RETURN refcursor;
END ufc_pms_int_rec_licenca_app;
