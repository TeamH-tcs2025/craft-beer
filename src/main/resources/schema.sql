CREATE TABLE IF NOT EXISTS beers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price INTEGER NOT NULL,
    jancode INTEGER,
    best_before INTEGER,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sales_records (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    beer_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    created_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    FOREIGN KEY (beer_id) REFERENCES beers(id)
);