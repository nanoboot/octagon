ALTER TABLE "GROUP"
    ADD CONSTRAINT "FK_GROUP_OWNER"
        FOREIGN KEY ("OWNER") REFERENCES "USER" ("ID") ON DELETE No Action ON UPDATE No Action;
