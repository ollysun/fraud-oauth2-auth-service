INSERT INTO oauth_client_details
    (client_id,resource_ids, client_secret,
    scope, authorized_grant_types,web_server_redirect_uri, authorities, access_token_validity,
    refresh_token_validity, additional_information, autoapprove)
VALUES
    ('testClientId', 'test-manager','$2y$12$rwlLaZY3QthtdV5QPda5gup0junQnlK99bteMCgQMBCSt7f.7udYq',
    'read,write','password,authorization_code,refresh_token',null, 'USER', 360000,
    360000, null, true);

INSERT INTO OAUTH_CLIENT_DETAILS(CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET,
SCOPE, AUTHORIZED_GRANT_TYPES, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY)
VALUES ('clientId', 'fraud-engine','$2y$12$rwlLaZY3QthtdV5QPda5gup0junQnlK99bteMCgQMBCSt7f.7udYq',
'read', 'password,authorization_code,refresh_token,implicit', 'USER', 10800, 2592000);

INSERT INTO OAUTH_CLIENT_DETAILS(CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET,
SCOPE, AUTHORIZED_GRANT_TYPES, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY)
VALUES ('clientId2', 'fraud-eagle-eye-manager','$2y$12$rwlLaZY3QthtdV5QPda5gup0junQnlK99bteMCgQMBCSt7f.7udYq',
'read,write', 'password,authorization_code,refresh_token,implicit', 'USER', 10800, 2592000);


/* Insert Data into User Table  */
--password
INSERT INTO user(username, password, first_name, last_name, phone, email, status)
VALUES('user','$2y$12$edYVNJl3uCxhm/U2mMAfneNAcMOjxx24Me/lz7Pei287Kf2nhGhdu', 'moses',
       'olalere','08067648113','olalere.moses@etranzactng.com', 1);

--password1234
INSERT INTO user(username, password,  first_name, last_name, phone, email, status)
VALUES('admin','$2y$12$zMmgkvzilJolNzUPfuVlrufIdJoRlCqrbjRRPQvl0PmqquVVSJaG2', 'moses',
       'olalere','08067657890','olalere.moses@etranzactng.com', 1);

/* Insert Data into Role Table  */
insert into role(name, description, status) values('ROLE_USER', 'user role', 1);
insert into role(name, description, status) values('ROLE_ADMIN', 'admin role', 1);
insert into role(name, description, status) values('ROLE_API_USER', 'api role',1);
insert into role(name, description, status) values('ROLE_DBA', 'dba role', 1 );
insert into role(name, description, status) values('ROLE_SELLER', 'seller role', 1);
insert into role(name, description, status) values('ROLE_BUYER', 'buyer role', 1);


/* Insert Data into Privilege Table  */
insert into permission(name, status) values('READ_PERMISSION',1);
insert into permission(name, status) values('WRITE_PERMISSION',1);
insert into permission(name, status) values('DELETE_PERMISSION',1);

/* Insert Data into UserRole Table  */
INSERT INTO `user_role`(`user_id`,`role_id`) VALUES (1,1);
INSERT INTO `user_role`(`user_id`,`role_id`) VALUES (2,1);
INSERT INTO `user_role`(`user_id`,`role_id`) VALUES (2,2);

/* Insert Data into RolePermission Table  */
insert into role_permission(`role_id`,`permission_id`) values(2,1);
insert into role_permission(`user_id`,`permission_id`) values(2,2);
insert into role_permission(`user_id`,`permission_id`) values(2,3);
insert into role_permission(`user_id`,`permission_id`) values(1,1);

/* Insert Data into UserPermission Table  */
insert into user_permission(`user_id`,`permission_id`) values(1,1);
insert into user_permission(`user_id`,`permission_id`) values(2,2);




