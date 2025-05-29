CREATE Table orders (
    id              BIGINT AUTO_INCREMENT NOT NULL,
    customer_id     BIGINT NOT NULL,
    status          VARCHAR(20) NOT NULL,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    total_price     DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id),
    Foreign KEY (customer_id) REFERENCES users(id) 
);

CREATE Table order_items (
    id              BIGINT AUTO_INCREMENT NOT NULL,
    order_id        BIGINT NOT NULL,
    product_id      BIGINT NOT NULL,
    unit_price      DECIMAL(10,2) NOT NULL,
    quantity        INTEGER NOT NULL,
    total_price     DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id),
    Constraint order_items_order_id_fk
        Foreign KEY (order_id) REFERENCES orders(id),
    Constraint order_items_product_id_fk
        Foreign KEY (product_id) REFERENCES products(id) 
)

