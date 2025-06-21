-- DROP TABLE IF EXISTS sales_records;
-- DROP TABLE IF EXISTS beers;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255),
    password_hash VARCHAR(255) NOT NULL,
    role BOOLEAN NOT NULL DEFAULT FALSE, 
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE beers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    jancode BIGINT UNIQUE,
    best_before INT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sales_records (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    beer_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    created_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT fk_beer FOREIGN KEY (beer_id) REFERENCES beers(id)
);

-- ビールテーブルにタイプカラムを追加
ALTER TABLE beers ADD COLUMN IF NOT EXISTS type VARCHAR(50);

-- 既存のビールに対してタイプを設定
UPDATE beers SET type = 'ペールエール' WHERE name LIKE '%ペール%' OR name LIKE '%エール%';
UPDATE beers SET type = 'ラガー' WHERE name LIKE '%ラガー%';
UPDATE beers SET type = 'IPA' WHERE name LIKE '%IPA%';
UPDATE beers SET type = 'ホワイトビール' WHERE name LIKE '%ホワイト%' OR name LIKE '%白%';
UPDATE beers SET type = '黒ビール' WHERE name LIKE '%黒%' OR name LIKE '%スタウト%';
UPDATE beers SET type = 'フルーツビール' WHERE name LIKE '%フルーツ%' OR name LIKE '%果実%';
-- デフォルト値を設定
UPDATE beers SET type = '未分類' WHERE type IS NULL;