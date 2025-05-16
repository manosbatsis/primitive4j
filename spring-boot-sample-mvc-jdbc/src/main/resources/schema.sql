CREATE TABLE customer
  (
     id   UUID NOT NULL,
     NAME VARCHAR(255),
     ref  VARCHAR(255) NOT NULL UNIQUE,
     PRIMARY KEY (id)
  )

CREATE TABLE purchase_order
  (
     id           UUID NOT NULL,
     comments     VARCHAR(255),
     customer_ref VARCHAR(255) NOT NULL,
     PRIMARY KEY (id)
  )

CREATE TABLE sample_entity
  (
     big_decimal_bean   DECIMAL(10, 1) NOT NULL,
     big_decimal_record DECIMAL(10, 1) NOT NULL,
     big_integer_bean   BIGINT NOT NULL,
     big_integer_record BIGINT NOT NULL,
     double_bean        DECIMAL(10, 1) NOT NULL,
     double_record      DECIMAL(10, 1) NOT NULL,
     float_bean         DECIMAL(10, 1) NOT NULL,
     float_record       DECIMAL(10, 1) NOT NULL,
     integer_bean       INT NOT NULL,
     integer_record     INT NOT NULL,
     short_bean         SMALLINT NOT NULL,
     short_record       SMALLINT NOT NULL,
     long_bean          BIGINT NOT NULL,
     long_record        BIGINT NOT NULL,
     id                 UUID NOT NULL,
     uuid_bean          VARCHAR(36) NOT NULL,
     uuid_record        VARCHAR(36) NOT NULL,
     string_bean        VARCHAR(255) NOT NULL,
     string_record      VARCHAR(255) NOT NULL,
     uri_bean           VARCHAR(500) NOT NULL,
     uri_record         VARCHAR(500) NOT NULL,
     url_bean           VARCHAR(500) NOT NULL,
     url_record         VARCHAR(500) NOT NULL,
     PRIMARY KEY (id)
  )
