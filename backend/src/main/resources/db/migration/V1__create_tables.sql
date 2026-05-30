CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS unaccent;
CREATE EXTENSION IF NOT EXISTS fuzzystrmatch;

CREATE TABLE "game_modes" (
    "id" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "name" VARCHAR(255) NOT NULL
);

CREATE TABLE "users" (
    "id" UUID NOT NULL PRIMARY KEY
);

CREATE TABLE "cards" (
    "id" INTEGER NOT NULL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "slug" VARCHAR(255) NOT NULL,
    "image" VARCHAR(255) NOT NULL,
    "mana_cost" INTEGER NOT NULL,
    "rarity_id" INTEGER NOT NULL,
    "set_id" INTEGER NOT NULL,
    "type_id" INTEGER NOT NULL
);

CREATE TABLE "card_rarities" (
    "id" INTEGER NOT NULL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "slug" VARCHAR(255) NOT NULL,
    "dust_value" INTEGER NOT NULL
);

CREATE TABLE "card_sets" (
    "id" INTEGER NOT NULL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "slug" VARCHAR(255) NOT NULL,
    "is_hyped" BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE "card_types" (
    "id" INTEGER NOT NULL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "slug" VARCHAR(255) NOT NULL
);

CREATE TABLE "card_classes" (
    "id" INTEGER NOT NULL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "slug" VARCHAR(255) NOT NULL
);

CREATE TABLE "card_keywords" (
    "id" INTEGER NOT NULL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "slug" VARCHAR(255) NOT NULL
);

CREATE TABLE "card_subtypes" (
    "id" UUID NOT NULL PRIMARY KEY,
    "api_id" INTEGER NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "slug" VARCHAR(255) NOT NULL,
    "subtype_type" VARCHAR(255) NOT NULL
);

CREATE TABLE "games" (
    "id" UUID NOT NULL PRIMARY KEY,
    "date" DATE NOT NULL,
    "card_id" INTEGER NOT NULL UNIQUE,
    "game_mode_id" UUID NOT NULL,
    CONSTRAINT "games_card_id_fkey" FOREIGN KEY ("card_id") REFERENCES "cards"("id"),
    CONSTRAINT "games_game_mode_id_fkey" FOREIGN KEY ("game_mode_id") REFERENCES "game_modes"("id")
);

CREATE TABLE "user_games" (
    "id" UUID NOT NULL PRIMARY KEY,
    "user_id" UUID NOT NULL,
    "game_id" UUID NOT NULL,
    CONSTRAINT "user_games_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "users"("id"),
    CONSTRAINT "user_games_game_id_fkey" FOREIGN KEY ("game_id") REFERENCES "games"("id")
);

CREATE TABLE "attempts" (
    "id" UUID NOT NULL PRIMARY KEY,
    "user_game_id" UUID NOT NULL,
    "card_id" INTEGER NOT NULL,
    "result" BOOLEAN NOT NULL,
    "attempt_number" INTEGER NOT NULL,
    CONSTRAINT "attempts_user_game_id_fkey" FOREIGN KEY ("user_game_id") REFERENCES "user_games"("id"),
    CONSTRAINT "attempts_card_id_fkey" FOREIGN KEY ("card_id") REFERENCES "cards"("id")
);

CREATE TABLE "class_junction" (
    "card_id" INTEGER NOT NULL,
    "card_class_id" INTEGER NOT NULL,
    PRIMARY KEY ("card_id", "card_class_id"),
    CONSTRAINT "class_junction_card_id_fkey" FOREIGN KEY ("card_id") REFERENCES "cards"("id"),
    CONSTRAINT "class_junction_card_class_id_fkey" FOREIGN KEY ("card_class_id") REFERENCES "card_classes"("id")
);

CREATE TABLE "keyword_junction" (
    "card_id" INTEGER NOT NULL,
    "card_keyword_id" INTEGER NOT NULL,
    PRIMARY KEY ("card_id", "card_keyword_id"),
    CONSTRAINT "keyword_junction_card_id_fkey" FOREIGN KEY ("card_id") REFERENCES "cards"("id"),
    CONSTRAINT "keyword_junction_card_keyword_id_fkey" FOREIGN KEY ("card_keyword_id") REFERENCES "card_keywords"("id")
);

CREATE TABLE "subtype_junction" (
    "card_id" INTEGER NOT NULL,
    "card_subtype_id" UUID NOT NULL,
    PRIMARY KEY ("card_id", "card_subtype_id"),
    CONSTRAINT "subtype_junction_card_id_fkey" FOREIGN KEY ("card_id") REFERENCES "cards"("id"),
    CONSTRAINT "subtype_junction_card_subtype_id_fkey" FOREIGN KEY ("card_subtype_id") REFERENCES "card_subtypes"("id")
);

ALTER TABLE "cards" ADD CONSTRAINT "cards_rarity_id_fkey" FOREIGN KEY ("rarity_id") REFERENCES "card_rarities"("id");
ALTER TABLE "cards" ADD CONSTRAINT "cards_set_id_fkey" FOREIGN KEY ("set_id") REFERENCES "card_sets"("id");
ALTER TABLE "cards" ADD CONSTRAINT "cards_type_id_fkey" FOREIGN KEY ("type_id") REFERENCES "card_types"("id");