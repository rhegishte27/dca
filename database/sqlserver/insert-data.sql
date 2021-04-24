USE [DcaDb]
GO

----- Users Table -----
BEGIN TRANSACTION;
INSERT INTO Users (Role, Identifier, UserName, PwdHash, EmailAddress) VALUES (1, 'Admin', 'User Administrator', '$2a$10$bp64jc2ksegswQzAKSdGROUufn0wMkIfVfAE5V4Qn6WAF2yvzBO1i', 'user@admin.com');
COMMIT;
GO

----- UsersFeatures Table -----
BEGIN TRANSACTION;
INSERT INTO UsersFeatures (FK_Users, Feature) VALUES ((SELECT PK_Users FROM Users WHERE Identifier='Admin'), 1);
INSERT INTO UsersFeatures (FK_Users, Feature) VALUES ((SELECT PK_Users FROM Users WHERE Identifier='Admin'), 2);
INSERT INTO UsersFeatures (FK_Users, Feature) VALUES ((SELECT PK_Users FROM Users WHERE Identifier='Admin'), 3);
INSERT INTO UsersFeatures (FK_Users, Feature) VALUES ((SELECT PK_Users FROM Users WHERE Identifier='Admin'), 4);
INSERT INTO UsersFeatures (FK_Users, Feature) VALUES ((SELECT PK_Users FROM Users WHERE Identifier='Admin'), 5);
INSERT INTO UsersFeatures (FK_Users, Feature) VALUES ((SELECT PK_Users FROM Users WHERE Identifier='Admin'), 6);
INSERT INTO UsersFeatures (FK_Users, Feature) VALUES ((SELECT PK_Users FROM Users WHERE Identifier='Admin'), 7);
INSERT INTO UsersFeatures (FK_Users, Feature) VALUES ((SELECT PK_Users FROM Users WHERE Identifier='Admin'), 8);
INSERT INTO UsersFeatures (FK_Users, Feature) VALUES ((SELECT PK_Users FROM Users WHERE Identifier='Admin'), 9);
COMMIT;
GO
