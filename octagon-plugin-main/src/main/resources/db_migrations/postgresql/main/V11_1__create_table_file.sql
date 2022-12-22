CREATE TABLE "FILE"
(
    "ID"          UUID,
    "NAME"        TEXT    NOT NULL,
    "DESCRIPTION" TEXT,
    "DOCUMENT"    UUID    NOT NULL,
    "SIZE"        INTEGER,
    "HASH"        TEXT
);
