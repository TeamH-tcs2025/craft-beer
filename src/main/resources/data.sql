INSERT INTO beers (name, price, jancode, best_before, is_active, created_at)
SELECT 'アサヒスーパードライ', 350, 4901777123456, 180, true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM beers WHERE name = 'アサヒスーパードライ');
 
INSERT INTO beers (name, price, jancode, best_before, is_active, created_at)
SELECT 'サッポロ黒ラベル', 330, 4901234567890, 150, true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM beers WHERE name = 'サッポロ黒ラベル');
 
INSERT INTO beers (name, price, jancode, best_before, is_active, created_at)
SELECT 'キリン一番搾り', 350, 4903333123456, 120, true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM beers WHERE name = 'キリン一番搾り');
 
INSERT INTO beers (name, price, jancode, best_before, is_active, created_at)
SELECT 'よなよなエール', 400, 4904444123456, 90, true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM beers WHERE name = 'よなよなエール');
 
INSERT INTO beers (name, price, jancode, best_before, is_active, created_at)
SELECT '軽井沢高原ビール', 450, 4905555123456, 60, true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM beers WHERE name = '軽井沢高原ビール');