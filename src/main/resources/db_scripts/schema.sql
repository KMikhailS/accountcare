DROP DATABASE IF EXISTS accountcare;

CREATE DATABASE accountcare;

\connect accountcare

--CREATE TABLE statuses
--(status_id serial NOT NULL,
--status varchar(10) NOT NULL,
--PRIMARY KEY (status_id));
--
--COMMENT ON TABLE statuses IS 'Statuses for accounts';

DROP TABLE IF EXISTS accounts, companies, inspection_organizations, table_types;

CREATE TABLE companies
(company_id serial NOT NULL,
company varchar(255) NOT NULL,
PRIMARY KEY (company_id));

COMMENT ON TABLE companies IS 'Companies of SKB';

CREATE TABLE inspection_organizations
(inspection_organization_id serial NOT NULL,
inspection_organization varchar(255) NOT NULL,
PRIMARY KEY (inspection_organization_id));

COMMENT ON TABLE inspection_organizations IS 'Inspection organizations';

CREATE TABLE table_types
(table_type_id serial NOT NULL,
table_type varchar(255) NOT NULL,
PRIMARY KEY (table_type_id));

COMMENT ON TABLE table_types IS 'Types of table models';

CREATE TABLE accounts
(account_id serial NOT NULL,
status varchar(10) DEFAULT 'NEW',
instruments text,
account_number varchar(255) NOT NULL,
account_date date NOT NULL,
company_id integer,
service_type varchar(255),
amount varchar(255),
amount_with_nds varchar(255),
invoice_number varchar(255),
invoice_date date,
delivery_to_accounting_date date,
inspection_organization_id integer,
notes text,
account_file_path text,
table_type_id integer NOT NULL,
PRIMARY KEY (account_id),
FOREIGN KEY (company_id) REFERENCES companies (company_id),
FOREIGN KEY (table_type_id) REFERENCES table_types (table_type_id),
FOREIGN KEY (inspection_organization_id) REFERENCES inspection_organizations (inspection_organization_id)
ON DELETE CASCADE);

COMMENT ON TABLE accounts IS 'Accounts';
