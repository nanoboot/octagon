ALTER TABLE "USER_DETAIL"
    ADD CONSTRAINT "FK_USER_DETAIL_USER"
        FOREIGN KEY ("USER") REFERENCES "USER" ("ID") ON DELETE No Action ON UPDATE No Action;