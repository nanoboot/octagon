CREATE TABLE "FAVORITE" (
 "ID" TEXT,
 "TYPE" TEXT NOT NULL,
 "OBJECT_ID" TEXT NOT NULL UNIQUE,
 "COMMENT" TEXT,
 "SORTKEY" INTEGER,
 "STAR_COUNT" INTEGER,
 "ULTRA_FAVORITE" INTEGER,
 "GROUP" TEXT,
 PRIMARY KEY("ID")
);
