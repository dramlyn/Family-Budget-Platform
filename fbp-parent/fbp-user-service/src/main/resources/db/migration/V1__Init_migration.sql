create schema if not exists fbp;
alter schema fbp owner to fbp;


CREATE TABLE IF NOT EXISTS fbp.category
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS fbp.budget_period
(
    id      BIGSERIAL PRIMARY KEY,
    "year"  integer not null,
    "month" integer not null
);

CREATE TABLE IF NOT EXISTS fbp.family
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS fbp."user"(
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    first_name  VARCHAR(255),
    last_name   VARCHAR(255),
    family_id   BIGINT REFERENCES family(id),
    keycloak_id VARCHAR(255) NOT NULL,
    role        VARCHAR(50)  NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS fbp.goal
(
    id          BIGSERIAL PRIMARY KEY,
    balance     integer      NOT NULL,
    cost        integer      NOT NULL,
    is_paid     boolean,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    family_id   BIGINT REFERENCES family (id),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    check ( balance >= 0 AND cost > 0 )
);

CREATE TABLE IF NOT EXISTS fbp.mandatory_payment
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    is_paid    boolean,
    amount     integer      NOT NULL,
    family_id  BIGINT REFERENCES family(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    period_id  BIGINT REFERENCES budget_period(id),
    check ( amount > 0 )
);

CREATE TABLE IF NOT EXISTS fbp.transaction
(
    id          BIGSERIAL PRIMARY KEY,
    type        VARCHAR(10) NOT NULL,
    amount      integer     NOT NULL,
    user_id     BIGINT REFERENCES "user"(id),
    category_id BIGINT REFERENCES category(id),
    period_id   BIGINT REFERENCES budget_period(id),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS fbp.family_budget_plan
(
    id              BIGSERIAL PRIMARY KEY,
    family_id       BIGINT REFERENCES family(id),
    "limit"         integer   not null,
    period_id       BIGINT REFERENCES budget_period(id),
    unique (family_id, period_id)
);

CREATE TABLE IF NOT EXISTS fbp.user_category_plan(
    id              BIGSERIAL PRIMARY KEY,
    category_id     BIGINT REFERENCES category(id),
    user_id         BIGINT REFERENCES "user"(id),
    "limit"         integer   not null,
    period_id       BIGINT REFERENCES budget_period(id),
    check ("limit" > 0)
);

