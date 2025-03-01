--username=example@email.com
--password=pass
insert into users(username, "password", enabled) values('example@email.com', '$2b$12$/f2bwFkp1Eqz8WFB.30M9O/r8.N5pLYKTQBtXtj8d1oe0XiFcZL12', true);
insert into authorities (username, authority) values('example@email.com', 'ROLE_USER');