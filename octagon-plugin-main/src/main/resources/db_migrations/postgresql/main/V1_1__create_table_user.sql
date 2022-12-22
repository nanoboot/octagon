CREATE TABLE "USER"
(
    "ID"      UUID,
    "NICK"    VARCHAR(64) NOT NULL UNIQUE,
    "NAME"    TEXT        NULL,
    "SURNAME" TEXT        NULL,
    "E_MAIL"  TEXT        NOT NULL UNIQUE,
    "ACTIVE"  BOOLEAN     NOT NULL DEFAULT TRUE,
    "ROLE"    TEXT        NOT NULL DEFAULT 'READER'
);
