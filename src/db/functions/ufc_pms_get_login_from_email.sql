create or replace function ufc_pms_get_login_from_email(p_tx_email IN VARCHAR2) RETURN VARCHAR2 IS
BEGIN

   RETURN SUBSTR(p_tx_email,1,INSTR(p_tx_email,'@')-1);

END ufc_pms_get_login_from_email; 
/
