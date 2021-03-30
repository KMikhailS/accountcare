\connect accountcare

ALTER TABLE accounts ADD COLUMN invoice_file_path text;

ALTER TABLE accounts ADD COLUMN row_color integer;

ALTER TABLE accounts ADD COLUMN is_our boolean;