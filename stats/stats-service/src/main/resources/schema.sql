CREATE TABLE IF NOT EXISTS hit (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    app varchar(30),
    uri varchar(50),
    ip varchar(15),
    date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_hit PRIMARY KEY (id)
);