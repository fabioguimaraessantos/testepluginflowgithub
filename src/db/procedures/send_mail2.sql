CREATE OR REPLACE PROCEDURE send_mail2( p_recipient_mail IN VARCHAR2,
p_subject IN VARCHAR2,
p_message IN VARCHAR2
) IS
---------------
-- CONSTANTES
---------------
C_mailhost varchar2(20) := 'copernico.cit';
C_domain varchar2(12) := '@cit.com.br>';
C_sender varchar2(9) := '<pms';
crlf varchar2(2) := CHR(13) || CHR(10);

---------------
-- VARIAVEIS
---------------
mail_conn UTL_SMTP.CONNECTION;
mesg VARCHAR2(4000);
v_recipient varchar2(50);
begin

mail_conn := UTL_SMTP.OPEN_CONNECTION( C_mailhost, 25 );

v_recipient := '<'||p_recipient_mail||'>';
mesg:= 'From: ' || C_sender || C_domain || crlf ||
       'Subject: ' || p_subject || crlf ||
       'To: ' || p_recipient_mail || crlf ||
       '' || crlf || p_message;

UTL_SMTP.EHLO( mail_conn, C_mailhost );
UTL_SMTP.MAIL( mail_conn, C_sender || C_domain );
UTL_SMTP.RCPT( mail_conn, v_recipient );
UTL_SMTP.OPEN_DATA(mail_conn);
UTL_SMTP.WRITE_RAW_DATA(mail_conn, UTL_RAW.CAST_TO_RAW(mesg));
UTL_SMTP.CLOSE_DATA(mail_conn);
UTL_SMTP.QUIT( mail_conn );
end;