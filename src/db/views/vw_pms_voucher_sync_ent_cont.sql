CREATE OR REPLACE VIEW VW_PMS_VOUCHER_SYNC_ENT_CONT AS
SELECT v.vsec_cd_voucher_sync_ent_cont cd_entity,
       v.vsec_nm_entity nm_entity,
       to_char(SYSDATE,'dd/mm/yyyy hh24:mi:ss') dt_update
FROM voucher_sync_entity_control v