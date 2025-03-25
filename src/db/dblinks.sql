-- Drop existing database link 
drop public database link ACDC_LINK;
-- Create database link 
create public database link ACDC_LINK
  connect to SOL_ACDC
  using 'SOL';

-- Drop existing database link 
drop public database link ACDC_MI_LINK;
-- Create database link 
create public database link ACDC_MI_LINK
  connect to SOL_ACDC_MI
  using 'SOL';

-- Drop existing database link 
drop public database link ACDC_MX_LINK;
-- Create database link 
create public database link ACDC_MX_LINK
  connect to SOL_ACDC_MX
  using 'SOL';

-- Drop existing database link 
drop public database link ADM_RH.LN_MGGLO;
-- Create database link 
create public database link ADM_RH.LN_MGGLO
  connect to MGGLO
  using 'ERP';

-- Drop existing database link 
drop public database link ADM_RH.LN_MGRH;
-- Create database link 
create public database link ADM_RH.LN_MGRH
  connect to MGRH
  using 'ERP';

-- Drop existing database link 
drop public database link ADM_RH.LN_MGWEB;
-- Create database link 
create public database link ADM_RH.LN_MGWEB
  connect to MGWEB
  using 'ERP';

-- Drop existing database link 
drop public database link ATLAS_LINK;
-- Create database link 
create public database link ATLAS_LINK
  connect to SOL_ATLAS_S
  using 'SOL';

-- Drop existing database link 
drop public database link BDEMQ_LINK;
-- Create database link 
create public database link BDEMQ_LINK
  connect to SOL_BDEMQ
  using 'SOL';

-- Drop existing database link 
drop public database link BIADM;
-- Create database link 
create public database link BIADM
  connect to BI_PMS
  using 'BIADM';

-- Drop existing database link 
drop public database link CADPES.NASH;
-- Create database link 
create public database link CADPES.NASH
  connect to DASSETS
  using 'NASH';

-- Drop existing database link 
drop public database link CHP.LN_MGWEB;
-- Create database link 
create public database link CHP.LN_MGWEB
  connect to MGWEB
  using 'ERP';

-- Drop existing database link 
drop public database link CITES.LN_MGWEB;
-- Create database link 
create public database link CITES.LN_MGWEB
  connect to MGWEB
  using 'ERP';

  -- Drop existing database link 
drop public database link CON_CRONOS_GOAL;
-- Create database link 
create public database link CON_CRONOS_GOAL
  connect to SLA_MONITOR
  using 'CRONOS.CIT';

-- Drop existing database link 
drop public database link CON_SLA;
-- Create database link 
create public database link CON_SLA
  connect to SLA_MONITOR
  using 'CRONOS.CIT';

-- Drop existing database link 
drop public database link HPAG_LINK;
-- Create database link 
create public database link HPAG_LINK
  connect to SOL_HP_AG
  using 'SOL';

-- Drop existing database link 
drop public database link HPBR_LINK;
-- Create database link 
create public database link HPBR_LINK
  connect to SOL_HP_BR
  using 'SOL';

-- Drop existing database link 
drop public database link HPCL_LINK;
-- Create database link 
create public database link HPCL_LINK
  connect to SOL_HP_CL
  using 'SOL';

-- Drop existing database link 
drop public database link HPMX_LINK;
-- Create database link 
create public database link HPMX_LINK
  connect to SOL_HP_MX
  using 'SOL';

-- Drop existing database link 
drop public database link INDNET_LINK;
-- Create database link 
create public database link INDNET_LINK
  connect to SOL_INDNET
  using 'SOL';

-- Drop existing database link 
drop public database link INTRA_RH.CHP;
-- Create database link 
create public database link INTRA_RH.CHP
  connect to CHP
  using 'ADM';

-- Drop existing database link 
drop public database link LN_ADM_TESTE_BI;
-- Create database link 
create public database link LN_ADM_TESTE_BI
  connect to ADM_RH
  using 'adm_teste_bi';

-- Drop existing database link 
drop public database link LN_CRONOS_GES_DEM.CIT;
-- Create database link 
create public database link LN_CRONOS_GES_DEM.CIT
  connect to DB_GESTAO_DEMANDA_TST
  using 'CRONOS.CIT';

-- Drop existing database link 
drop public database link LN_GDA_UAT.CIT;
-- Create database link 
create public database link LN_GDA_UAT.CIT
  connect to GDA_UAT
  using 'oneiros.cit';

-- Drop existing database link 
drop public database link LN_MGGLO;
-- Create database link 
create public database link LN_MGGLO
  connect to MGGLO
  using 'ERP';

-- Drop existing database link 
drop public database link LN_MGRH;
-- Create database link 
create public database link LN_MGRH
  connect to MGRH
  using 'ERP';

-- Drop existing database link 
drop public database link LN_MGRH.CIT;
-- Create database link 
create public database link LN_MGRH.CIT
  connect to MGRH
  using 'ERP';

-- Drop existing database link 
drop public database link LN_MGWEB;
-- Create database link 
create public database link LN_MGWEB
  connect to MGWEB
  using 'ERP';

-- Drop existing database link 
drop public database link LN_MGWEB.CIT;
-- Create database link 
create public database link LN_MGWEB.CIT
  connect to MGWEB
  using 'ERP';

-- Drop existing database link 
drop public database link LN_MGWEBTST;
-- Create database link 
create public database link LN_MGWEBTST
  connect to MGWEB
  using 'ERPTESTE';

-- Drop existing database link 
drop public database link LN_PMS;
-- Create database link 
create public database link LN_PMS
  connect to PMS
  using 'ONEIROS';

-- Drop existing database link 
drop public database link LN_SOL_BDEMQ;
-- Create database link 
create public database link LN_SOL_BDEMQ
  connect to SOL_BDEMQ
  using 'SOL';

-- Drop existing database link 
drop public database link LN_SOL_INQUIRER;
-- Create database link 
create public database link LN_SOL_INQUIRER
  connect to INQUIRER
  using 'SOL';

-- Drop existing database link 
drop public database link LN_VIENA_SQA;
-- Create database link 
create public database link LN_VIENA_SQA
  connect to SQASTAT
  using 'VIENA';

-- Drop existing database link 
drop public database link MEGA_LINK;
-- Create database link 
create public database link MEGA_LINK
  connect to MEGA
  using 'ERP';

-- Drop existing database link 
drop public database link NASH_LK;
-- Create database link 
create public database link NASH_LK
  connect to DASSETS_TO_ADM
  using 'NASH';

-- Drop existing database link 
drop public database link OCSWEB;
-- Create database link 
create public database link OCSWEB
  connect to root
  using 'ocsweb';

-- Drop existing database link 
drop public database link PROJ.LN_MGWEB;
-- Create database link 
create public database link PROJ.LN_MGWEB
  connect to MGWEB
  using 'ERP';

-- Drop existing database link 
drop public database link RH.LN_MGRH;
-- Create database link 
create public database link RH.LN_MGRH
  connect to MGRH
  using 'ERP';

-- Drop existing database link 
drop public database link RODONITA;
-- Create database link 
create public database link RODONITA
  connect to PMS
  using '172.16.22.89/adm';

-- Drop existing database link 
drop public database link SOL.LN_MGWEB;
-- Create database link 
create public database link SOL.LN_MGWEB
  connect to MGWEB
  using 'ERP';

-- Drop existing database link 
drop public database link SOLSKY_LINK;
-- Create database link 
create public database link SOLSKY_LINK
  connect to SOL_SKY
  using 'SOL';

-- Drop existing database link 
drop public database link UNION_LINK;
-- Create database link 
create public database link UNION_LINK
  connect to SOL_UNION
  using 'SOL';

-- Drop existing database link 
drop public database link WEBREQ_ATD_LINK;
-- Create database link 
create public database link WEBREQ_ATD_LINK
  connect to WEBREQ_ATD
  using 'SOL';

-- Drop existing database link 
drop public database link WEBREQ_PEND_LINK;
-- Create database link 
create public database link WEBREQ_PEND_LINK
  connect to WEBREQ_PEND
  using 'SOL';
