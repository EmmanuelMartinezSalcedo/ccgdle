ALTER TABLE "game_modes" ADD COLUMN "slug" VARCHAR(255) NOT NULL;

INSERT INTO "game_modes" ("name", "slug") VALUES ('Standard', 'standard');
INSERT INTO "game_modes" ("name", "slug") VALUES ('Wild', 'wild');
INSERT INTO "game_modes" ("name", "slug") VALUES ('Legendaries Standard', 'legendaries-standard');
INSERT INTO "game_modes" ("name", "slug") VALUES ('Legendaries Wild', 'legendaries-wild');
INSERT INTO "game_modes" ("name", "slug") VALUES ('Only Known Faces Standard', 'only-known-faces-standard');