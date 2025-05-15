create table carts (
    id              binary(16) default (uuid_to_bin(uuid())) NOT NULL,
    date_created    DATE DEFAULT (curDate()) Not NULL,
    PRIMARY KEY (id)
);

create table cart_items (
    id          BIGINT AUTO_INCREMENT NOT NULL,
    quantity    INTEGER NOT NULL,
    product_id  BIGINT NOT NULL,
    cart_id     binary(16) NOT NULL,
    PRIMARY KEY (id),
    Constraint items_product_unique UNIQUE(cart_id, product_id),
    Constraint items_products_id_fk 
        FOREIGN Key (product_id) REFERENCES products(id) ON DELETE CASCADE,
    Constraint items_carts_id_fk 
        FOREIGN Key (cart_id) REFERENCES carts(id) ON DELETE CASCADE
);