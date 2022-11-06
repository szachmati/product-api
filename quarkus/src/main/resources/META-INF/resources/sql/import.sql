INSERT INTO PRODUCT_CATEGORY(ID, NAME) VALUES(nextval('product_category_seq'), 'HOME');
INSERT INTO PRODUCT_CATEGORY(ID, NAME) VALUES(nextval('product_category_seq'), 'ELECTRONICS');
INSERT INTO PRODUCT_CATEGORY(ID, NAME) VALUES(nextval('product_category_seq'), 'TOOLS');
INSERT INTO PRODUCT_CATEGORY(ID, NAME) VALUES(nextval('product_category_seq'), 'CLOTHES');
INSERT INTO PRODUCT_CATEGORY(ID, NAME) VALUES(nextval('product_category_seq'), 'ENTERTAINMENT');
INSERT INTO PRODUCT_CATEGORY(ID, NAME) VALUES(nextval('product_category_seq'), 'HEALTH');

INSERT INTO PRODUCT(ID, UUID, PRODUCT_CATEGORY_ID, NAME, PRICE)
VALUES(nextval('product_seq'), '910a29f8-bab3-4138-a40d-b722671be58a', 1, 'Bed', 30.99);