-- ビールデータの挿入
-- この方法ではテーブルが既に存在し、必要な列が定義されている必要があります

-- テーブルが存在することを確認
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_tables WHERE tablename = 'beers') THEN
        RAISE NOTICE 'テーブル beers が存在しません。このスクリプトの前にschema.sqlを実行してください。';
        RETURN;
    END IF;
END $$;

-- Insert beers with explicit IDs
INSERT INTO beers (id, name, price, jancode, best_before, is_active, created_at, updated_at) 
VALUES (1,'ホワイトビール', 900, 4901234567894, 10, true, NOW(), NOW());

INSERT INTO beers (id, name, price, jancode, best_before, is_active, created_at, updated_at) 
VALUES (2,'ラガー', 800, 4512345678907, 15, true, NOW(), NOW());

INSERT INTO beers (id, name, price, jancode, best_before, is_active, created_at, updated_at) 
VALUES(3, 'ペールエール', 1000, 4987654321097, 12, true, NOW(), NOW());

INSERT INTO beers (id, name, price, jancode, best_before, is_active, created_at, updated_at) 
VALUES(4, 'フルーツビール', 1000, 4545678901234, 5, true, NOW(), NOW());

INSERT INTO beers (id, name, price, jancode, best_before, is_active, created_at, updated_at) 
VALUES(5, '黒ビール', 1200, 4999999999996, 10, true, NOW(), NOW());

INSERT INTO beers (id, name, price, jancode, best_before, is_active, created_at, updated_at) 
VALUES (6, 'IPA', 900, 4571234567892, 150, true, NOW(), NOW());

-- ユーザーデータの挿入
-- テーブルが存在するか確認
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_tables WHERE tablename = 'users') THEN
        RAISE NOTICE 'テーブル users が存在しません。このスクリプトの前にschema.sqlを実行してください。';
        RETURN;
    END IF;
END $$;

-- ダミーユーザーデータ（パスワードハッシュは実際のシステムに合わせて変更してください）
INSERT INTO users (username, password_hash, created_at, updated_at) 
VALUES ('admin', 'bbadmin123hash', NOW(), NOW())
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password_hash, created_at, updated_at) 
VALUES ('manager', 'bbmanager456hash', NOW(), NOW())
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password_hash, created_at, updated_at) 
VALUES ('staff1', 'bbstaff1789hash', NOW(), NOW())
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password_hash, created_at, updated_at) 
VALUES ('staff2', 'bbstaff2012hash', NOW(), NOW())
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password_hash, created_at, updated_at) 
VALUES ('Miwachu', 'TeamH2025hash', NOW(), NOW())
ON CONFLICT (username) DO NOTHING;