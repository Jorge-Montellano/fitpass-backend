-- ==========================================================
-- FITPASS GYM NETWORK
-- DATABASE: PostgreSQL
-- VERSION: 1.0
-- ARCHITECTURE: MODULAR MONOLITH
-- ==========================================================

-- ==========================================================
-- RESET DATABASE
-- ==========================================================

DROP TABLE IF EXISTS check_ins CASCADE;
DROP TABLE IF EXISTS payments CASCADE;
DROP TABLE IF EXISTS user_memberships CASCADE;
DROP TABLE IF EXISTS memberships CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS gyms CASCADE;
DROP TABLE IF EXISTS roles CASCADE;


-- ==========================================================
-- ROLES
-- ==========================================================

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ==========================================================
-- GYMS
-- ==========================================================

CREATE TABLE gyms (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),

    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,

    phone VARCHAR(30),
    email VARCHAR(150),

    opening_time TIME,
    closing_time TIME,

    -- STATUS
    -- 0 = INACTIVE
    -- 1 = ACTIVE
    -- 2 = SUSPENDED

    status INT NOT NULL DEFAULT 1,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_gyms_status
        CHECK (status IN (0, 1, 2)),

    CONSTRAINT chk_gyms_schedule
        CHECK (
            closing_time IS NULL
            OR opening_time IS NULL
            OR closing_time > opening_time
        )
);


-- ==========================================================
-- USERS
-- ==========================================================

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,

    role_id BIGINT NOT NULL,
    gym_id BIGINT,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,

    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,

    phone VARCHAR(30),

    -- STATUS
    -- 0 = INACTIVE
    -- 1 = ACTIVE
    -- 2 = SUSPENDED
    -- 3 = DELETED

    status INT NOT NULL DEFAULT 1,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id),

    CONSTRAINT fk_users_gym
        FOREIGN KEY (gym_id)
        REFERENCES gyms(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_users_status
        CHECK (status IN (0, 1, 2, 3))
);


-- ==========================================================
-- MEMBERSHIPS / PLANS
-- ==========================================================

CREATE TABLE memberships (
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),

    price NUMERIC(10, 2) NOT NULL,
    duration_days INT NOT NULL,

    -- STATUS
    -- 0 = INACTIVE
    -- 1 = ACTIVE

    status INT NOT NULL DEFAULT 1,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_memberships_price
        CHECK (price >= 0),

    CONSTRAINT chk_memberships_duration
        CHECK (duration_days > 0),

    CONSTRAINT chk_memberships_status
        CHECK (status IN (0, 1))
);


-- ==========================================================
-- USER MEMBERSHIPS
-- ==========================================================

CREATE TABLE user_memberships (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,
    membership_id BIGINT NOT NULL,

    start_date DATE NOT NULL,
    end_date DATE NOT NULL,

    -- STATUS
    -- 0 = INACTIVE
    -- 1 = ACTIVE
    -- 2 = EXPIRED

    status INT NOT NULL DEFAULT 1,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_memberships_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_user_memberships_membership
        FOREIGN KEY (membership_id)
        REFERENCES memberships(id),

    CONSTRAINT chk_user_memberships_dates
        CHECK (end_date >= start_date),

    CONSTRAINT chk_user_memberships_status
        CHECK (status IN (0, 1, 2))
);


-- ==========================================================
-- PAYMENTS
-- ==========================================================

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,

    user_membership_id BIGINT NOT NULL,

    amount NUMERIC(10, 2) NOT NULL,

    payment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    payment_method VARCHAR(50) NOT NULL,

    -- PAYMENT STATUS
    -- 0 = PENDING
    -- 1 = PAID
    -- 2 = FAILED
    -- 3 = REFUNDED

    payment_status INT NOT NULL DEFAULT 0,

    transaction_reference VARCHAR(150),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payments_user_membership
        FOREIGN KEY (user_membership_id)
        REFERENCES user_memberships(id),

    CONSTRAINT chk_payments_amount
        CHECK (amount > 0),

    CONSTRAINT chk_payments_status
        CHECK (payment_status IN (0, 1, 2, 3))
);


-- ==========================================================
-- CHECK INS
-- ==========================================================

CREATE TABLE check_ins (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,
    gym_id BIGINT NOT NULL,

    check_in_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    check_out_at TIMESTAMP,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_check_ins_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_check_ins_gym
        FOREIGN KEY (gym_id)
        REFERENCES gyms(id),

    CONSTRAINT chk_check_ins_dates
        CHECK (
            check_out_at IS NULL
            OR check_out_at >= check_in_at
        )
);


-- ==========================================================
-- INDEXES
-- ==========================================================

-- USERS

CREATE INDEX idx_users_role_id
    ON users(role_id);

CREATE INDEX idx_users_gym_id
    ON users(gym_id);

CREATE INDEX idx_users_status
    ON users(status);


-- GYMS

CREATE INDEX idx_gyms_status
    ON gyms(status);

CREATE INDEX idx_gyms_city
    ON gyms(city);


-- USER MEMBERSHIPS

CREATE INDEX idx_user_memberships_user_id
    ON user_memberships(user_id);

CREATE INDEX idx_user_memberships_membership_id
    ON user_memberships(membership_id);

CREATE INDEX idx_user_memberships_status
    ON user_memberships(status);

CREATE INDEX idx_user_memberships_dates
    ON user_memberships(start_date, end_date);


-- PAYMENTS

CREATE INDEX idx_payments_user_membership_id
    ON payments(user_membership_id);

CREATE INDEX idx_payments_status
    ON payments(payment_status);

CREATE INDEX idx_payments_date
    ON payments(payment_date);


-- CHECK INS

CREATE INDEX idx_check_ins_user_id
    ON check_ins(user_id);

CREATE INDEX idx_check_ins_gym_id
    ON check_ins(gym_id);

CREATE INDEX idx_check_ins_date
    ON check_ins(check_in_at);


-- ==========================================================
-- INITIAL DATA
-- ==========================================================


-- ==========================================================
-- ROLES
-- ==========================================================

INSERT INTO roles (
    name,
    description
)
VALUES
    (
        'ADMIN',
        'System administrator'
    ),
    (
        'USER',
        'FitPass customer'
    ),
    (
        'GYM_ADMIN',
        'Gym administrator'
    );


-- ==========================================================
-- MEMBERSHIPS
-- ==========================================================

INSERT INTO memberships (
    name,
    description,
    price,
    duration_days,
    status
)
VALUES
    (
        'BASIC',
        'Basic FitPass membership',
        99.00,
        30,
        1
    ),
    (
        'PREMIUM',
        'Premium FitPass membership',
        149.00,
        30,
        1
    ),
    (
        'ANNUAL',
        'Annual FitPass membership',
        1499.00,
        365,
        1
    );


-- ==========================================================
-- GYMS
-- ==========================================================

INSERT INTO gyms (
    name,
    description,
    address,
    city,
    phone,
    email,
    opening_time,
    closing_time,
    status
)
VALUES
    (
        'FitPass Central Gym',
        'Main FitPass gym',
        'Av. Principal #100',
        'La Paz',
        '70000000',
        'central@fitpass.com',
        '06:00:00',
        '22:00:00',
        1
    ),
    (
        'FitPass North Gym',
        'North branch',
        'Av. Norte #200',
        'Santa Cruz',
        '70000001',
        'north@fitpass.com',
        '06:00:00',
        '22:00:00',
        1
    );


-- ==========================================================
-- ADMIN USER
-- ==========================================================
-- IMPORTANT:
-- Replace the password with a real BCrypt hash
-- generated by the Spring Boot application.
--
-- Example placeholder:
-- $2a$10$REPLACE_WITH_BCRYPT_PASSWORD
-- ==========================================================

INSERT INTO users (
    role_id,
    gym_id,
    first_name,
    last_name,
    email,
    password,
    phone,
    status
)
VALUES (
    (
        SELECT id
        FROM roles
        WHERE name = 'ADMIN'
    ),
    NULL,
    'System',
    'Administrator',
    'admin@fitpass.com',
    '$2a$10$REPLACE_WITH_BCRYPT_PASSWORD',
    '70000000',
    1
);


-- ==========================================================
-- VERIFICATION
-- ==========================================================

SELECT * FROM roles;

SELECT * FROM gyms;

SELECT * FROM users;

SELECT * FROM memberships;

SELECT * FROM user_memberships;

SELECT * FROM payments;

SELECT * FROM check_ins;