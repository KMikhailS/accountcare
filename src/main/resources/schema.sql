DROP DATABASE IF EXISTS accountcare;

CREATE DATABASE accountcare;

\connect accountcare

CREATE TABLE statuses
(status_id serial NOT NULL,
status varchar(10) NOT NULL,
PRIMARY KEY (status_id));

COMMENT ON TABLE statuses IS 'Statuses for accounts';

CREATE TABLE instruments
(instrument_id serial NOT NULL,
instrument varchar(255) NOT NULL,
PRIMARY KEY (instrument_id));

COMMENT ON TABLE instruments IS 'Types of measuring instruments';

CREATE TABLE accounts
(account_id serial NOT NULL,
status_id serial NOT NULL,
instrument_id serial NOT NULL,
account_number varchar(255) NOT NULL,
account_date date NOT NULL,
company varchar(255) NOT NULL,
service_type varchar(255),
amount decimal,
amount_with_nds decimal,
invoice_number varchar(255),
invoice_date date,
delivery_to_accounting_date date,
inspection_organization varchar(255),
notes text,
PRIMARY KEY (account_id),
FOREIGN KEY (status_id) REFERENCES statuses (status_id),
FOREIGN KEY (instrument_id) REFERENCES instruments (instrument_id));

COMMENT ON TABLE accounts IS 'Accounts';

CREATE TABLE accounts_instruments
(account_id serial NOT NULL,
instrument_id serial NOT NULL,
PRIMARY KEY (account_id, instrument_id),
FOREIGN KEY (instrument_id) REFERENCES instruments (instrument_id),
FOREIGN KEY (account_id) REFERENCES accounts (account_id)
ON DELETE CASCADE);

COMMENT ON TABLE accounts_instruments IS 'Many-to-many relation between accounts and instruments';
