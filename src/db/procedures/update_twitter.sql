CREATE OR REPLACE PROCEDURE update_twitter(t_user IN VARCHAR2, t_pass IN VARCHAR2, t_update IN VARCHAR2) AS
http_req utl_http.req;
http_resp utl_http.resp;
h_name VARCHAR2(255);
h_value VARCHAR2(1023);
t_update_send VARCHAR2(4000);
res_value VARCHAR2(32767);
show_header NUMBER := 0;--0 False, 1 True
show_xml NUMBER := 1;--0 False, 1 True
BEGIN
t_update_send := 'status=' || SUBSTR(t_update, 1, 140) || '';
utl_http.set_proxy('proxycps.cit:3128'); --If you need to specify a proxy un comment this line.
http_req := utl_http.begin_request('http://twitter.com/statuses/update.xml', 'GET', utl_http.http_version_1_1);
utl_http.set_response_error_check(TRUE);
utl_http.set_detailed_excp_support(TRUE);
utl_http.set_body_charset(http_req, 'UTF-8');
utl_http.set_header(http_req, 'User-Agent', 'Mozilla/4.0');
utl_http.set_header(http_req, 'Content-Type', 'application/x-www-form-urlencoded');
utl_http.set_header(http_req, 'Content-Length', to_char(LENGTH(t_update_send)));
utl_http.set_transfer_timeout(to_char('60'));
utl_http.set_authentication(http_req, t_user, t_pass, 'Basic');
utl_http.write_text(http_req, t_update_send);
http_resp := utl_http.get_response(http_req);

DBMS_OUTPUT.PUT_LINE('status code: ' || http_resp.status_code);
DBMS_OUTPUT.PUT_LINE('reason phrase: ' || http_resp.reason_phrase);

IF show_header = 1 THEN
FOR i IN 1 .. utl_http.get_header_count(http_resp)
LOOP
utl_http.get_header(http_resp, i, h_name, h_value);
DBMS_OUTPUT.PUT_LINE(h_name || ': ' || h_value);
END LOOP;
END IF;

IF show_xml = 1 THEN
BEGIN
WHILE 1 = 1
LOOP
utl_http.read_line(http_resp, res_value, TRUE);
DBMS_OUTPUT.PUT_LINE(res_value);
END LOOP;

EXCEPTION
WHEN utl_http.end_of_body THEN
NULL;
END;
END IF;

utl_http.end_response(http_resp);

EXCEPTION
WHEN others THEN
DBMS_OUTPUT.PUT_LINE(sqlerrm);
--DBMS_OUTPUT.PUT_LINE(to_char(http_resp));
RAISE;

END update_twitter;