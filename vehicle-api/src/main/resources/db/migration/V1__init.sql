CREATE TABLE vehicle (
  id BIGSERIAL PRIMARY KEY,
  brand VARCHAR(255) NOT NULL,
  model VARCHAR(255) NOT NULL,
  manufacture_year INTEGER NOT NULL CHECK (manufacture_year >= 1900),
  color VARCHAR(255) NOT NULL,
  price NUMERIC(19,2) NOT NULL,
  status VARCHAR(20) NOT NULL CHECK (status IN ('AVAILABLE','SOLD')),
  created_at TIMESTAMPTZ DEFAULT now(),
  updated_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE buyer_profile (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  external_user_id VARCHAR(255) UNIQUE
);

CREATE TABLE sale (
  id BIGSERIAL PRIMARY KEY,
  vehicle_id BIGINT NOT NULL REFERENCES vehicle(id),
  buyer_id BIGINT NOT NULL REFERENCES buyer_profile(id),
  price_at_sale NUMERIC(19,2) NOT NULL,
  sold_at TIMESTAMPTZ DEFAULT now()
);
