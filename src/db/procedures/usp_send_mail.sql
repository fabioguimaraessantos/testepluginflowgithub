create or replace procedure usp_send_mail(p_recipient_mail in varchar2,
                                          p_subject        in varchar2,
                                          p_message        in varchar2) is
  ---------------
  -- CONSTANTES
  ---------------
  c_mailhost varchar2(20) := 'copernico.cit';
  c_domain   varchar2(12) := '@ciandt.com>';
  c_sender   varchar2(9) := '<pms';
  crlf       varchar2(2) := chr(13) || chr(10);

  ---------------
  -- VARIAVEIS
  ---------------
  mail_conn   utl_smtp.connection;
  mesg        varchar2(4000);
  v_recipient varchar2(50);
begin

  mail_conn := utl_smtp.open_connection(c_mailhost, 25);

  v_recipient := '<' || p_recipient_mail || '>';
  mesg        := 'From: ' || c_sender || c_domain || crlf || 'Subject: ' ||
                 p_subject || crlf || 'To: ' || p_recipient_mail || crlf || '' || crlf ||
                 p_message;

  utl_smtp.ehlo(mail_conn, c_mailhost);
  utl_smtp.mail(mail_conn, c_sender || c_domain);
  utl_smtp.rcpt(mail_conn, v_recipient);
  utl_smtp.open_data(mail_conn);
  utl_smtp.write_raw_data(mail_conn, utl_raw.cast_to_raw(mesg));
  utl_smtp.close_data(mail_conn);
  utl_smtp.quit(mail_conn);
end;