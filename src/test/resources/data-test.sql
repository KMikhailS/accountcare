INSERT INTO companies (company)
VALUES
('СКБ'),
('КТБ'),
('С-1');

INSERT INTO inspection_organizations (inspection_organization)
VALUES
('ЧЦСМ'),
('УНИИМ'),
('ТЕРМЕКС'),
('УРАЛТЕСТ'),
('ВНИИМС'),
('ВНИИМ'),
('СНИИМ'),
('ТЕСТ ИНТЕХ'),
('ВНИИОФИ'),
('ИМЦ МИКРО'),
('ФИЗПРИБОР');

INSERT INTO table_types (table_type)
VALUES
('ЧЦСМ'),
('УНИИМ'),
('другие'),
('прочие услуги');

INSERT INTO accounts (account_number, account_date, company_id, service_type, amount, amount_with_nds, instruments,
invoice_number, invoice_date, delivery_to_accounting_date, inspection_organization_id, notes, account_file_path, table_type_id)
VALUES
('1111', '2021-02-12', 1, 'услуга', '100', '120', 'приборы', '101010', '2021-02-13', '2021-02-14', 1, 'заметки', 'file_path1', 1),
('2222', '2021-02-12', 2, 'услуга', '200', '120', 'приборы', '202020', '2021-02-13', '2021-02-14', 2, 'заметки', 'file_path2', 2),
('3333', '2021-02-12', 3, 'услуга', '300', '120', 'приборы', '303030', '2021-02-13', '2021-02-14', 3, 'заметки', 'file_path3', 3);
