CREATE TABLE "LOG"
(
    "ID"        UUID,
    "TYPE"      VARCHAR(50)                 NOT NULL,
    "TIMESTAMP" timestamp without time zone NOT NULL,
    "USER"      UUID                        NOT NULL,
    "DATA"      jsonb                       NOT NULL
);