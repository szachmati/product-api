CREATE SEQUENCE product_category_seq;
CREATE SEQUENCE product_seq;

CREATE TABLE product_category (
    ID   BIGINT NOT NULL PRIMARY KEY,
    NAME VARCHAR(255)
);

CREATE TABLE product (
    ID BIGINT NOT NULL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    PRICE NUMERIC(19, 2) NOT NULL,
    UUID UUID NOT NULL UNIQUE,
    CATEGORY_ID BIGINT NOT NULL
);

ALTER TABLE product
    ADD CONSTRAINT product_product_category_fk FOREIGN KEY(CATEGORY_ID) REFERENCES product_category(ID);

CREATE INDEX product_category_name_idx ON product_category(NAME);
CREATE INDEX product_uuid_idx ON product(UUID);