CREATE TABLE "REMINDER" (
 "ID" TEXT,
 "TYPE" TEXT,
 "OBJECT_ID" TEXT UNIQUE,
 "COMMENT" TEXT,
 "URGENCY" TEXT NOT NULL,
 "RUN_EVERY_X_DAYS" INTEGER,
 "LAST_RUN" TEXT,
 "ALARM_STATUS" TEXT,
 "RING_VOLUME" INTEGER,
 PRIMARY KEY("ID")
);
