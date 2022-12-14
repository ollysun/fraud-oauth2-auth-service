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
'read,write', 'password,authorization_code,refresh_token', null,'ROLE_CLIENT,ROLE_TRUSTED_CLIENT', 360000,
 360000,null,true);

INSERT INTO oauth_client_details
      (client_id,resource_ids, client_secret,
       scope, authorized_grant_types,web_server_redirect_uri, authorities, access_token_validity,
       refresh_token_validity, additional_information, autoapprove)
VALUES ('eagle-eye-manager', 'fraud-eagle-eye-manager','$2a$10$vCXMWCn7fDZWOcLnIEhmK.74dvK1Eh8ae2WrWlhr2ETPLoxQctN4.',
'read,write', 'password,authorization_code,refresh_token', null,'ROLE_CLIENT,ROLE_TRUSTED_CLIENT', 360000,
360000, null, true);

--pass
INSERT INTO user(username, password,  first_name, last_name, phone, email, status, created_by, created_at)
VALUES('admin','$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', 'moses',
       'olalere','08067657890','olalere.moses2@etranzactng.com', 1, 'admin', sysdate());

INSERT INTO user(username, password,  first_name, last_name, phone, email, status, created_by, created_at)
VALUES('test','$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', 'moses',
       'olalere','08067657896','olalere3.moses2@etranzactng.com', 1, 'test', sysdate());

/* Insert Data into Role Table  */
insert into authorisation_service.role(name, description, status, created_by, created_at) values('ROLE_TEST', 'user test role', 1, 'admin', sysdate());
insert into authorisation_service.role(name, description, status, created_by, created_at) values('ROLE_ADMIN', 'admin role', 1, 'admin', sysdate());



/* Insert Data into Privilege Table  */
insert into permission(name, status) values("PRODUCT.CREATE" ,1);
insert into permission(name, status) values("PRODUCT.READ",1);
insert into permission(name, status) values("PRODUCT.UPDATE",1);
insert into permission(name, status) values("PRODUCT.DELETE",1);
insert into permission(name, status) values("PRODUCT.SERVICE.CREATE",1);
insert into permission(name, status) values("PRODUCT.SERVICE.READ",1);
insert into permission(name, status) values("PRODUCT.SERVICE.UPDATE",1);
insert into permission(name, status) values("PRODUCT.SERVICE.DELETE",1);
insert into permission(name, status) values("SERVICE.DATASET.CREATE",1);
insert into permission(name, status) values("SERVICE.DATASET.READ",1);
insert into permission(name, status) values("SERVICE.DATASET.UPDATE",1);
insert into permission(name, status) values("SERVICE.DATASET.DELETE",1);
insert into permission(name, status) values("SERVICE.DATASET.APPROVE",1);
insert into permission(name, status) values("CARD.READ",1);
insert into permission(name, status) values("CARD.UPDATE",1);
insert into permission(name, status) values("ACCOUNT.READ",1);
insert into permission(name, status) values("ACCOUNT.UPDATE",1);
insert into permission(name, status) values("PARAMETER.CREATE",1);
insert into permission(name, status) values("PARAMETER.READ",1);
insert into permission(name, status) values("PARAMETER.UPDATE",1);
insert into permission(name, status) values("PARAMETER.DELETE",1);
insert into permission(name, status) values("PARAMETER.APPROVE",1);
insert into permission(name, status) values("RULE.CREATE",1);
insert into permission(name, status) values("RULE.READ",1);
insert into permission(name, status) values("RULE.UPDATE",1);
insert into permission(name, status) values("RULE.DELETE",1);
insert into permission(name, status) values("RULE.APPROVE",1);
insert into permission(name, status) values("RULE.PRODUCT.MAP",1);
insert into permission(name, status) values("RULE.PRODUCT.UNMAP",1);
insert into permission(name, status) values("RULE.PRODUCT.APPROVE",1);
insert into permission(name, status) values("PERMISSION.READ",1);
insert into permission(name, status) values("PERMISSION.ASSIGN",1);
insert into permission(name, status) values("PERMISSION.CREATE",1);
insert into permission(name, status) values("PERMISSION.DELETE",1);
insert into permission(name, status) values("ROLE.CREATE",1);
insert into permission(name, status) values("ROLE.READ",1);
insert into permission(name, status) values("ROLE.UPDATE",1);
insert into permission(name, status) values("ROLE.DELETE",1);
insert into permission(name, status) values("ROLE.APPROVE",1);
insert into permission(name, status) values("USER.CREATE",1);
insert into permission(name, status) values("USER.READ",1);
insert into permission(name, status) values("USER.UPDATE",1);
insert into permission(name, status) values("USER.DELETE",1);
insert into permission(name, status) values("USER.APPROVE",1);
insert into permission(name, status) values("NOTIFICATION_GROUP.CREATE",1);
insert into permission(name, status) values("NOTIFICATION_GROUP.READ",1);
insert into permission(name, status) values("NOTIFICATION_GROUP.UPDATE",1);
insert into permission(name, status) values("NOTIFICATION_GROUP.DELETE",1);
insert into permission(name, status) values("NOTIFICATION_GROUP.APPROVE",1);
insert into permission(name, status) values("NOTIFICATION.READ",1);
insert into permission(name, status) values("OFAC.CREATE",1);
insert into permission(name, status) values("OFAC.READ",1);
insert into permission(name, status) values("OFAC.UPDATE",1);
insert into permission(name, status) values("OFAC.DELETE",1);
insert into permission(name, status) values("OFAC.APPROVE",1);
insert into permission(name, status) values("WATCHLIST_INTERNAL.CREATE",1);
insert into permission(name, status) values("WATCHLIST_INTERNAL.READ",1);
insert into permission(name, status) values("WATCHLIST_INTERNAL.UPDATE",1);
insert into permission(name, status) values("WATCHLIST_INTERNAL.DELETE",1);
insert into permission(name, status) values("WATCHLIST_INTERNAL.APPROVE",1);
insert into permission(name, status) values("REPORT.GET",1);
insert into permission(name, status) values("REPORT.SCHEDULE.CREATE",1);
insert into permission(name, status) values("REPORT.SCHEDULE.READ",1);
insert into permission(name, status) values("REPORT.SCHEDULE.UPDATE",1);
insert into permission(name, status) values("REPORT.SCHEDULE.DELETE",1);


/* Insert Data into UserRole Table  */
INSERT INTO authorisation_service.`user_role`(`user_id`,`role_id`) VALUES (1,1);
INSERT INTO authorisation_service.`user_role`(`user_id`,`role_id`) VALUES (2,2);


/* Insert Data into UserPermission Table  */
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,1);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,2);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,3);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,4);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,5);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,6);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,7);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,8);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,9);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,10);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,11);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,12);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,13);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,14);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,15);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,16);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,17);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,18);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,19);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,20);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,21);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,22);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,23);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,24);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,25);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,26);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,27);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,28);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,29);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,30);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,31);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,32);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,33);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,34);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,35);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,36);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,37);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,38);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,39);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,40);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,41);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,42);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,43);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,44);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,45);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,46);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,47);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,48);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,49);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,50);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,51);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,52);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,53);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,54);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,55);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,56);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,57);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,58);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,59);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,60);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,61);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,62);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,63);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,64);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(1,65);

insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,1);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,2);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,3);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,4);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,5);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,6);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,7);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,8);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,9);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,10);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,11);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,12);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,13);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,14);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,15);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,16);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,17);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,18);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,19);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,20);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,21);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,22);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,23);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,24);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,25);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,26);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,27);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,28);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,29);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,30);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,31);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,32);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,33);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,34);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,35);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,36);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,37);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,38);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,39);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,40);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,41);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,42);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,43);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,44);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,45);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,46);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,47);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,48);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,49);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,50);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,51);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,52);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,53);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,54);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,55);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,56);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,57);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,58);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,59);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,60);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,61);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,62);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,63);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,64);
insert into authorisation_service.user_permission(`user_id`,`permission_id`) values(2,65);



/* Insert Data into RolePermission Table  */
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,1);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,2);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,3);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,4);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,5);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,6);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,7);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,8);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,9);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,10);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,11);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,12);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,13);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,14);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,15);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,16);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,17);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,18);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,19);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,20);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,21);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,22);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,23);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,24);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,25);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,26);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,27);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,28);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,29);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,30);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,31);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,32);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,33);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,34);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,35);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,36);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,37);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,38);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,39);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,40);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,41);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,42);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,43);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,44);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,45);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,46);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,47);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,48);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,49);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,50);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,51);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,52);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,53);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,54);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,55);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,56);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,57);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,58);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,59);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,60);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,61);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,62);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,63);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,64);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(1,65);


insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,1);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,2);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,3);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,4);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,5);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,6);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,7);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,8);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,9);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,10);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,11);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,12);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,13);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,14);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,15);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,16);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,17);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,18);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,19);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,20);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,21);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,22);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,23);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,24);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,25);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,26);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,27);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,28);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,29);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,30);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,31);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,32);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,33);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,34);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,35);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,36);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,37);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,38);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,39);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,40);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,41);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,42);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,43);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,44);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,45);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,46);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,47);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,48);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,49);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,50);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,51);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,52);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,53);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,54);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,55);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,56);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,57);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,58);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,59);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,60);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,61);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,62);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,63);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,64);
insert into authorisation_service.role_permission(`role_id`,`permission_id`) values(2,65);


