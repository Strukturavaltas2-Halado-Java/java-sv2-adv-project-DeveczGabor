create TABLE pets
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    pet_name VARCHAR(255)          NOT NULL,
    race     VARCHAR(255)          NOT NULL,
    price    INT                   NULL,
    shop_id  BIGINT                NULL,
    CONSTRAINT pk_pets PRIMARY KEY (id)
);

alter table pets
    add CONSTRAINT FK_PETS_ON_SHOP FOREIGN KEY (shop_id) REFERENCES shops (id);