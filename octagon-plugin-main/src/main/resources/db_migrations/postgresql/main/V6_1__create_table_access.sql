CREATE TABLE "ACCESS"
(
    "ID"                 UUID    NOT NULL,
    "USER"               UUID    NOT NULL,
    "GROUP"              UUID    NOT NULL,
    "USER_ACCESS_RIGHT"  UUID    NOT NULL,
    "GROUP_ACCESS_RIGHT" UUID    NOT NULL,
    "OTHER_ACCESS_RIGHT" UUID    NOT NULL,
    "SYSTEM"             BOOLEAN NOT NULL DEFAULT FALSE,
    "READ_ONLY" BOOLEAN NOT NULL DEFAULT FALSE,
    "HIDDEN"             BOOLEAN NOT NULL DEFAULT FALSE,
    "DENY_ANONYMOUS"     BOOLEAN NOT NULL DEFAULT TRUE,
    "UNDELETABLE"     BOOLEAN NOT NULL DEFAULT FALSE,
    "DELETED"     BOOLEAN NOT NULL DEFAULT FALSE,
    "TYPE"     TEXT NOT NULL,
    "ICON"     BYTEA,
    "ACCESS_COUNT"     INTEGER NOT NULL DEFAULT 0,
    "CREATED"     TIMESTAMP NOT NULL,
    "MODIFIED"     TIMESTAMP NOT NULL,
    "ACCESSED"     TIMESTAMP NOT NULL,
    "READ_PASSWORD"     TEXT,
    "EDIT_PASSWORD"     TEXT,
    "ENCRYPTED"     BOOLEAN
);
