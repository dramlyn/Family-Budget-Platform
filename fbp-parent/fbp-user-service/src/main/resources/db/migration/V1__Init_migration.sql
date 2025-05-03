create schema if not exists fbp;
alter schema fbp owner to fbp;


CREATE TABLE fbp.category (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fbp.family (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        description TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fbp."user" (
                        id BIGSERIAL PRIMARY KEY,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        first_name VARCHAR(255),
                        last_name VARCHAR(255),
                        family_id BIGINT REFERENCES family(id),
                        keycloak_id VARCHAR(255) NOT NULL,
                        role VARCHAR(50) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fbp.wallet (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        description TEXT,
                        balance integer DEFAULT 0,
                        family_id BIGINT REFERENCES family(id),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fbp.currency (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(50) NOT NULL,
                          description TEXT,
                          code VARCHAR(3) NOT NULL
);

CREATE TABLE fbp.goal (
                      id BIGSERIAL PRIMARY KEY,
                      cost integer NOT NULL,
                      name VARCHAR(255) NOT NULL,
                      description TEXT,
                      family_id BIGINT REFERENCES family(id),
                      user_id BIGINT REFERENCES "user"(id),
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fbp.family_category_plan (
                                      id BIGSERIAL PRIMARY KEY,
                                      category_id BIGINT REFERENCES category(id),
                                      family_id BIGINT REFERENCES family(id),
                                      "limit" integer NOT NULL,
                                      plan_start_date DATE NOT NULL,
                                      end_start_date DATE NOT NULL
);

CREATE TABLE fbp.mandatory_payment (
                                   id BIGSERIAL PRIMARY KEY,
                                   name VARCHAR(255) NOT NULL,
                                   description TEXT,
                                   amount integer NOT NULL,
                                   wallet_id BIGINT REFERENCES wallet(id),
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   "period" VARCHAR(50) NOT NULL
);

CREATE TABLE fbp.transaction (
                             id BIGSERIAL PRIMARY KEY,
                             type VARCHAR(50) NOT NULL,
                             amount integer NOT NULL,
                             wallet_id BIGINT REFERENCES wallet(id),
                             user_id BIGINT REFERENCES "user"(id),
                             category_id BIGINT REFERENCES category(id),
                             goal_id BIGINT REFERENCES goal(id),
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

