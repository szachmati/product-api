DO $$DECLARE product_category_id numeric;
    BEGIN
        INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'HOME');
        INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'ELECTRONICS');
        INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'CARS');
        INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'FOOD');
        INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'FURNITURE');
        INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'MOBILE PHONES');
        INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'FASHION');
        INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'MUSIC');
        INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'SPORT');
        INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'CHILD');
        INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'HEALTH');

        product_category_id := (SELECT pc.id FROM product_category pc WHERE pc.name = 'HOME');
        INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Fork', 200.34, 'bfe937fc-b879-48fd-bf3b-c209327b6057', product_category_id);
        INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Plate', 100.99, 'c1ff52cb-d662-4371-a4f4-a30579a739b6', product_category_id);
        product_category_id := (SELECT pc.id FROM product_category pc WHERE pc.name = 'HEALTH');
        INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Vitamin C', 12, '66da8716-84f0-4c92-b219-def3390056c9', product_category_id);
        INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Vitamin D', 1234, 'a196b847-8f36-4ffd-bf51-022ba2048c72', product_category_id);
    END$$ language plpgsql;


