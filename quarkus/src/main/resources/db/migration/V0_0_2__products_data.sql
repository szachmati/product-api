DO $$DECLARE product_category_id numeric;
BEGIN
    INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'HOME');
    INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'ELECTRONICS');
    INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'FOOD');
    INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'MUSIC');
    INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'SPORT');
    INSERT INTO product_category(id, name) VALUES(nextval('product_category_seq'), 'HEALTH');

    --  HOME PRODUCTS
    product_category_id := (SELECT pc.id FROM product_category pc WHERE pc.name = 'HOME');
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Fork', 200.34, 'bfe937fc-b879-48fd-bf3b-c209327b6057', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Plate', 100.99, 'c1ff52cb-d662-4371-a4f4-a30579a739b6', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Spoon', 20.31, 'e2626d8c-1e17-40e9-ae1d-ece3d135dec7', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Kettle', 100.99, '136d656e-c7cf-4511-bcf2-9c5a58dcc10d', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Frying Pan', 200.45, '4c2295b7-a1ba-4987-a9d6-34fdbb96bab2', product_category_id);
    -- ELECTRONICS PRODUCTS
    product_category_id := (SELECT pc.id FROM product_category pc WHERE pc.name = 'ELECTRONICS');
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Xiaomi Redmi 8', 700, 'd9bedf43-fe33-4d19-9efd-551df42d5698', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Xiaomi Redmi 9', 800, 'dcbf3017-2944-4db1-816c-40531e1c67f1', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Iphone 9', 2000, '949e6893-6b68-4585-8197-d8c74e782c21', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Iphone 10', 3000, '36f9dc2d-8847-4716-8a1d-69478cf0da25', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Realme Note 10 Pro', 15000, 'e816e7f5-6852-483c-b399-0a6025176dea', product_category_id);
    -- FOOD PRODUCTS
    product_category_id := (SELECT pc.id FROM product_category pc WHERE pc.name = 'FOOD');
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Bread', 3, '9a764ac0-75d3-4b3e-93f9-e372a56a3a7c', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Roll', 0.6, '5b1c83fb-a22d-43a2-b9a2-7df851297974', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Butter', 7, '0320111f-a133-4f4f-bb7e-10a8156f58d8', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Milk', 3.5, 'ba1832c8-1bcc-4315-b38e-346ee5a5f0f7', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Joghurt', 2, '61395454-4925-4815-a825-1440d026ff15', product_category_id);
    -- MUSIC PRODUCTS
    product_category_id := (SELECT pc.id FROM product_category pc WHERE pc.name = 'MUSIC');
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Piano', 3000, '3c8d98e1-60b4-4246-838a-f8475b3d905b', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Cello', 300, '7069061e-7ab3-4731-ba51-af5e4d2291e9', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Acoustic Guitar', 999, '92b02fab-5328-498e-8de8-cef862712cef', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Bass', 1200, '22822927-d36c-4fbc-8131-8eee30071b31', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Electric Guitar', 2000, 'd36a36f1-817b-49c1-a5a8-88677e1f9e71', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Harp', 10000, '01e8130a-b3cd-490d-8849-692bbfd141a0', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Violin', 500, '66792a45-dda6-4b04-8704-5e7a8d755a02', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Drums', 15000, 'fca09277-65f8-4d3d-9e2f-a7b84879a340', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Amplifier', 10000, '623ad69b-c94a-40cf-8797-d288836849c0', product_category_id);
    -- SPORT PRODUCTS
    product_category_id := (SELECT pc.id FROM product_category pc WHERE pc.name = 'SPORT');
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Swimsuit', 120.83, '60bbd397-b855-461f-b09b-2c780d1a7393', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Skis', 3000, '71b26983-46a7-4ba7-aebb-2ffe98d5f984', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Bmx', 3000, '617b9598-5cff-4c0e-bb55-acaa28a5c56b', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Skateboard', 200, '6cccfb06-6358-46a5-83b2-ea4760437cda', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Football shoes', 500, 'd145c8bc-aafa-407e-a7ac-703dc31c0810', product_category_id);
    -- HEALTH PRODUCTS
    product_category_id := (SELECT pc.id FROM product_category pc WHERE pc.name = 'HEALTH');
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Vitamin A', 12, '66da8716-84f0-4c92-b219-def3390056c9', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Vitamin B', 5, '37b15e85-ecb0-4b1d-835f-eedaae96a6e7', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Vitamin C', 20, 'a179aa5b-e6e9-46ee-bd5c-5ab42acf151d', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Vitamin D', 112.34, 'a407ab20-6d74-47ec-9789-e4716080ac27', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Vitamin E', 500, '43d1c188-947b-4ba8-9648-09899aaf97e3', product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Rutinoscorbin', 0.98, '5a4ac12d-34ed-4aea-8989-7e3a33982c0c' , product_category_id);
    INSERT INTO product(id, name, price, uuid, category_id) VALUES(nextval('product_seq'), 'Strepsils', 283, '3ed56fa5-2b0a-49d5-82d5-fcfc10f746af', product_category_id);
END$$ language plpgsql;


