CREATE TABLE "SPACE"
(
    "ID"          UUID,
    "ACCESS"      UUID NOT NULL,
    "NAME"        TEXT NOT NULL UNIQUE,
    "DESCRIPTION" TEXT,
    "DATABASE"    UUID NOT NULL

);