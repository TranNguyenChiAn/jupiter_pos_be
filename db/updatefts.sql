-- Cập nhật dữ liệu ban đầu
UPDATE PUBLIC.ORDERS
SET FTS = TO_TSVECTOR(
        'simple',
        lower(UNACCENT(
                COALESCE(RECEIVER_NAME, '') || ' ' || COALESCE(RECEIVER_ADDRESS, '') || ' ' ||
                COALESCE(RECEIVER_PHONE, '') || ' ' || COALESCE(NOTE, '')
              ))
          );

-- Tạo lại hàm trigger
CREATE OR REPLACE FUNCTION UPDATE_ORDERS_FTS() RETURNS TRIGGER AS
$$
BEGIN
    NEW.fts := to_tsvector(
            'simple',
            lower(unaccent(
                    coalesce(NEW.receiver_name, '') || ' ' ||
                    coalesce(NEW.receiver_address, '') || ' ' ||
                    coalesce(NEW.receiver_phone, '') || ' ' ||
                    coalesce(NEW.note, '')
                  ))
               );
RETURN NEW;
END
$$ LANGUAGE PLPGSQL;

-- Tạo lại trigger
CREATE
OR REPLACE TRIGGER TRG_ORDERS_FTS
    BEFORE INSERT
        OR
UPDATE
    ON PUBLIC.ORDERS
    FOR EACH ROW
    EXECUTE FUNCTION UPDATE_ORDERS_FTS();

UPDATE PUBLIC.USERS
SET FTS = TO_TSVECTOR(
        'simple',
        lower(UNACCENT(
                COALESCE(FULLNAME, '') || ' ' || COALESCE(EMAIL, '') || ' ' || COALESCE(PHONE, '') || ' ' ||
                COALESCE(USERNAME, '')
              ))
          );

-- Tạo lại hàm trigger
CREATE OR REPLACE FUNCTION UPDATE_USERS_FTS() RETURNS TRIGGER AS
$$
BEGIN
    NEW.fts := to_tsvector(
            'simple',
            lower(unaccent(
                    coalesce(NEW.fullname, '') || ' ' ||
                    coalesce(NEW.email, '') || ' ' ||
                    coalesce(NEW.phone, '') || ' ' ||
                    coalesce(NEW.username, '')
                  ))
               );
RETURN NEW;
END
$$ LANGUAGE PLPGSQL;



UPDATE CUSTOMERS
SET FTS = TO_TSVECTOR(
        'simple',
        lower(UNACCENT(
                COALESCE(CUSTOMER_NAME, '') || ' ' || COALESCE(ADDRESS, '') || ' ' || COALESCE(PHONE, '')
              ))
          );

CREATE OR REPLACE FUNCTION UPDATE_CUSTOMERS_FTS() RETURNS TRIGGER AS
$$
BEGIN
    NEW.fts
:= to_tsvector('simple',
                       lower(unaccent(coalesce(NEW.customer_name, '') || ' ' || coalesce(NEW.address, '') || ' ' ||
                                      coalesce(NEW.phone, ''))));
RETURN NEW;
END
$$ LANGUAGE PLPGSQL;


UPDATE PRODUCTS
SET FTS = TO_TSVECTOR(
        'simple',
        lower(UNACCENT(
                COALESCE(PRODUCT_NAME, '') || ' ' || COALESCE(DESCRIPTION, '')
              ))
          );

-- Bảng product_variants
UPDATE PRODUCT_VARIANTS
SET FTS = TO_TSVECTOR(
        'simple',
        lower(UNACCENT(COALESCE(SKU, '') || ' ' || COALESCE(BARCODE, ''))
        ));



CREATE OR REPLACE FUNCTION PUBLIC.PRODUCTS_FTS_TRIGGER() RETURNS TRIGGER AS
$$
BEGIN
    NEW.fts
:=
            to_tsvector('simple',
                        lower(unaccent(coalesce(NEW.product_name, '') || ' ' || coalesce(NEW.description, ''))));
RETURN NEW;
END
$$ LANGUAGE PLPGSQL;

CREATE OR REPLACE FUNCTION PUBLIC.PRODUCT_VARIANTS_FTS_TRIGGER() RETURNS TRIGGER AS
$$
BEGIN
    NEW.fts
:=
            to_tsvector('simple', lower(unaccent(coalesce(NEW.sku, '') || ' ' || coalesce(NEW.barcode, ''))));
RETURN NEW;
END
$$ LANGUAGE PLPGSQL;

