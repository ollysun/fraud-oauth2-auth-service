--secret
INSERT INTO oauth_client_details
    (client_id,resource_ids, client_secret,
    scope, authorized_grant_types,web_server_redirect_uri, authorities, access_token_validity,
    refresh_token_validity, additional_information, autoapprove)
VALUES
    ('testClientId', 'test-manager','$2a$10$vCXMWCn7fDZWOcLnIEhmK.74dvK1Eh8ae2WrWlhr2ETPLoxQctN4.',
    'read,write','password,authorization_code,refresh_token',null, 'ROLE_CLIENT,ROLE_TRUSTED_CLIENT', 360000,
    360000, null, true);

INSERT INTO oauth_client_details
     (client_id,resource_ids, client_secret,
      scope, authorized_grant_types,web_server_redirect_uri, authorities, access_token_validity,
      refresh_token_validity, additional_information, autoapprove)
VALUES ('clientId', 'fraud-engine','$2a$10$vCXMWCn7fDZWOcLnIEhmK.74dvK1Eh8ae2WrWlhr2ETPLoxQctN4.',
'read', 'password,authorization_code,refresh_token', null,'ROLE_CLIENT,ROLE_TRUSTED_CLIENT', 360000,
 360000,null,true);

INSERT INTO oauth_client_details
      (client_id,resource_ids, client_secret,
       scope, authorized_grant_types,web_server_redirect_uri, authorities, access_token_validity,
       refresh_token_validity, additional_information, autoapprove)
VALUES ('eagle-eye-manager', 'fraud-eagle-eye-manager','$2a$10$vCXMWCn7fDZWOcLnIEhmK.74dvK1Eh8ae2WrWlhr2ETPLoxQctN4.',
'read,write', 'password,authorization_code,refresh_token', null,'ROLE_CLIENT,ROLE_TRUSTED_CLIENT', 360000,
360000, null, true);

--pass
INSERT INTO user(username, password, first_name, last_name, phone, email, status)
VALUES('user','$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', 'moses',
       'olalere','08067648113','olalere.moses@etranzactng.com', 1);

INSERT INTO user(username, password,  first_name, last_name, phone, email, status)
VALUES('admin','$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', 'moses',
       'olalere','08067657890','olalere.moses2@etranzactng.com', 1);

/* Insert Data into Role Table  */
insert into authorisation_service.role(name, description, status) values('ROLE_USER', 'user role', 1);
insert into authorisation_service.role(name, description, status) values('ROLE_ADMIN', 'admin role', 1);
insert into authorisation_service.role(name, description, status) values('ROLE_DBA', 'dba role', 1 );
insert into authorisation_service.role(name, description, status) values('ROLE_SELLER', 'seller role', 1);
insert into authorisation_service.role(name, description, status) values('ROLE_BUYER', 'buyer role', 1);



/* Insert Data into Privilege Table  */
insert into permission(name, status) values('READ_PERMISSION',1);
insert into permission(name, status) values('WRITE_PERMISSION',1);
insert into permission(name, status) values('DELETE_PERMISSION',1);


/* Insert Data into UserRole Table  */
INSERT INTO authorisation_service.`user_role`(`user_id`,`role_id`) VALUES (1,1);
INSERT INTO authorisation_service.`user_role`(`user_id`,`role_id`) VALUES (2,1);
INSERT INTO authorisation_service.`user_role`(`user_id`,`role_id`) VALUES (2,2);


/* Insert Data into UserPermission Table  */
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,1);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,2);

/* Insert Data into RolePermission Table  */
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,1);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,2);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,3);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,2);



