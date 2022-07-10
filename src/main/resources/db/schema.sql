CREATE TABLE IF NOT EXISTS accounts
(
    account  varchar(30) NOT NULL,
    name     varchar(30) NULL,
    epochSec bigint(20)  NULL
);
CREATE TABLE IF NOT EXISTS IC_INFO
(
    ID      INTEGER      NOT NULL AUTO_INCREMENT,
    IC_NAME VARCHAR(100) NOT NULL,
    IC_PATH VARCHAR(250) NOT NULL,
    IC_DESC VARCHAR(250)
);