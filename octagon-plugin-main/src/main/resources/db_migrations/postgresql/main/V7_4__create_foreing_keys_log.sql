ALTER TABLE "LOG"
    ADD CONSTRAINT "FK_LOG_USER"
        FOREIGN KEY ("USER") REFERENCES "USER" ("ID") ON DELETE No Action ON UPDATE No Action;