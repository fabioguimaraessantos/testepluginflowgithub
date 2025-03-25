CREATE OR REPLACE VIEW VW_PMS_VOUCHER_WHITE_LIST AS
SELECT cd_resource,
       nm_resource,
       -- ajuste para os casos de logins "v." (vendors)
       CASE WHEN (cd_login = ufc_pms_get_login_from_email(tx_email))
                 THEN cd_login
                 ELSE ufc_pms_get_login_from_email(tx_email)
       END cd_login,
       cd_travel_budget,
       dt_creation
FROM (
        select p.pess_cd_pessoa cd_resource,
               p.pess_nm_pessoa nm_resource,
               p.pess_cd_login cd_login,
               p.pess_tx_email tx_email,
               rtrim(xmlagg(xmlelement(e, od.orde_cd_orca_despesa || ',')).extract('//text()'), ',') cd_travel_budget,
               to_char(SYSDATE,'dd/mm/yyyy hh24:mi:ss') dt_creation
        from pessoa p,
             orc_despesa od,
             orc_desp_white_list odwl
        where trunc(sysdate) between od.orde_dt_inicio and od.orde_dt_fim
          and p.pess_dt_rescisao is null
          and od.orde_in_ativo = 'S'
          and od.orde_in_delete_logico = 'N'
          and p.pess_cd_login = odwl.pess_cd_login
          and od.orde_cd_orca_despesa = odwl.orde_cd_orca_despesa
        group by p.pess_cd_pessoa,
                 p.pess_nm_pessoa,
                 p.pess_cd_login,
                 p.pess_tx_email
     )

