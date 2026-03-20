-- Users (20 records to support companies and buyers)
INSERT INTO users (email, password_hash, first_name, last_name, role, created_at, is_active) VALUES
('comp1@fenix.com', '$2a$10$xlv4R3h/X6yiX5f6YghZM..0lb0wGImD2GJID6x00ol0ZXsN8wLcG', 'Juan', 'Perez', 'EMPRESA', '2023-01-10 10:00:00', TRUE),
('comp2@fenix.com', '$2a$10$Yy8IgcOJFc6EuAvTDS0Ed.yWbMeW3yQ6wZsYM5j2iwp38TXSOuDt.', 'Maria', 'Gomez', 'EMPRESA', '2023-01-15 11:00:00', TRUE),
('comp3@fenix.com', '$2a$10$fnFtfeeyxag0wmZsE4LIGORvK9O.0vVuxeazfX7fQGg51Jj8KvdwC', 'Carlos', 'Ruiz', 'EMPRESA', '2023-02-01 09:30:00', TRUE),
('comp4@fenix.com', '$2a$10$HvA5PKnd2/T3hZyRlni7B.wzY5J3BWRjmQhxxhag0rqBOk13JArXu', 'Ana', 'Lopez', 'EMPRESA', '2023-02-20 14:00:00', TRUE),
('comp5@fenix.com', '$2a$10$iHiy/RIxQgCLe0ek3umPn.UBd4r1N79d67TU0VbwrPJGYtg7kSlSa', 'Luis', 'Torres', 'EMPRESA', '2023-03-05 16:20:00', TRUE),
('comp6@fenix.com', '$2a$10$mOzp04GCyPDY.EwlMByBk.5omEWjGFeCTETiIMlGUGIHM321mEyT2', 'Elena', 'Diaz', 'EMPRESA', '2023-03-15 08:45:00', TRUE),
('comp7@fenix.com', '$2a$10$5BL8aiBXhFJ.iW6rvjQSoO7YepdXC6a17FcN363Hwutys3lsozDT6', 'Pedro', 'Sanchez', 'EMPRESA', '2023-04-01 12:00:00', TRUE),
('comp8@fenix.com', '$2a$10$pa5zghEhwWVUFRWjj3NjOeT4EPYcfAGRaJHiEjP2ldlEUUGdQDXwu', 'Sofia', 'Martin', 'EMPRESA', '2023-04-10 10:15:00', TRUE),
('comp9@fenix.com', '$2a$10$M/Au1kTdgNxV5yRnBhtpteITwcnxKcou4xRtm9RKqRb/o15GE25E6', 'Miguel', 'Hernandez', 'EMPRESA', '2023-05-05 11:30:00', TRUE),
('comp10@fenix.com', '$2a$10$grR1ulh/XQfim4oFpNIwM.05unEvHAkvAl6tfCURV/OQFAScCQNrG', 'Lucia', 'Jimenez', 'EMPRESA', '2023-05-20 15:45:00', FALSE),
('comp11@fenix.com', '$2a$10$5UHKu2R3LA5yTN2cMDB4OOqqb.PUiweteeIbTTClN4A36/GqmqCXq', 'David', 'Alvarez', 'EMPRESA', '2023-06-01 09:00:00', TRUE),
('comp12@fenix.com', '$2a$10$qTjrRYDSpJeNwyS9I82Rh.vZfbYRu45xqhkmikERdWpq0YxDT6Wl6', 'Carmen', 'Moreno', 'EMPRESA', '2023-06-15 13:20:00', TRUE),
('comp13@fenix.com', '$2a$10$OJ3NfgK9157bczIrMCB7Ue8/DgIaRyAnHOZqBr/bqUVEtKtqIkPh.', 'Jorge', 'Munoz', 'EMPRESA', '2023-07-01 17:00:00', TRUE),
('comp14@fenix.com', '$2a$10$rBOsX.hkH7GwQg14pG8qrejvkArHEav9.F7r9pE3MQCQbPL/dwuc2', 'Raquel', 'Romero', 'EMPRESA', '2023-07-20 10:30:00', TRUE),
('comp15@fenix.com', '$2a$10$FryfaZ29Ad/SXD9MvbSJpOzMPF4gOKI3eKaBRmgRDDhmYpks2aHS6', 'Alberto', 'Navarro', 'EMPRESA', '2023-08-05 14:15:00', TRUE),
('user1@fenix.com', '$2a$10$6MyaSZhcLjmxWPrNnXgFBeNv8ZiP1Y3imZoLxFi4tE5wdVGZz16j.', 'Laura', 'Gil', 'PARTICULAR', '2023-08-15 16:00:00', TRUE),
('user2@fenix.com', '$2a$10$pxzsm37BrCgTsC05aQFIWuP09.jmsSzy2dFUMXL80X9tuxhCqctRu', 'Roberto', 'Serrano', 'PARTICULAR', '2023-09-01 09:45:00', TRUE),
('user3@fenix.com', '$2a$10$fBZ6ai8lEqNoZVUbogN/7.jCV8FmRPWg55BzxRcDJn3Xoc.xhbcWe', 'Isabel', 'Blanco', 'PARTICULAR', '2023-09-20 11:15:00', TRUE),
('user4@fenix.com', '$2a$10$pPImOFcZZOhZEborkEuW9e.zzvvn0I.oCinjlJWK6HTmEM1FEBgBm', 'Fernando', 'Molina', 'PARTICULAR', '2023-10-05 13:30:00', TRUE),
('admin@fenix.com', '$2a$10$3zwvbKLIsf7qYrLKvuWV0eL9zzIPbKKUQRuSmDuPgQeAo5By8LFka', 'Admin', 'System', 'ADMIN', '2023-01-01 00:00:00', TRUE);

-- Companies (15 records)
INSERT INTO companies (user_id, company_name, cif, reputation_score, impact_metrics, company_img, is_active) VALUES
(1, 'Tech Solutions', 'B1111111A', 100, '{"environmental": {"totalCo2SavedKg": 50.5, "totalEwasteSavedKg": 200.5}, "social": {"itemsDonated": 10}}', 'uploads/Tech-Solutions.png', TRUE),
(2, 'Green Hardware', 'B2222222A', 200, '{"environmental": {"totalCo2SavedKg": 120.0, "totalEwasteSavedKg": 120.0}, "social": {"itemsDonated": 25}}', 'uploads/Green-Hardware.png', TRUE),
(3, 'Recycle IT', 'B3333333C', 150, '{"environmental": {"totalCo2SavedKg": 80.2, "totalEwasteSavedKg": 80.2}, "social": {"itemsDonated": 15}}', 'uploads/Recycle-It.png', TRUE),
(4, 'Second Life PC', 'B4444444C', 300, '{"environmental": {"totalCo2SavedKg": 200.5, "totalEwasteSavedKg": 150.0}, "social": {"itemsDonated": 40}}', 'uploads/Second-Life-PC.png', TRUE),
(5, 'EcoComp', 'B5555555F', 50, '{"environmental": {"totalCo2SavedKg": 20.0, "totalEwasteSavedKg": 20.0}, "social": {"itemsDonated": 5}}', 'uploads/EcoComp.png', TRUE),
(6, 'Future Tech', 'B6666666H', 120, '{"environmental": {"totalCo2SavedKg": 60.0, "totalEwasteSavedKg": 60.0}, "social": {"itemsDonated": 12}}', 'uploads/FutureTech.png', TRUE),
(7, 'Hardware Heroes', 'B7777777I', 180, '{"environmental": {"totalCo2SavedKg": 90.5, "totalEwasteSavedKg": 90.5}, "social": {"itemsDonated": 18}}', 'uploads/Hardware-Heroes.png', TRUE),
(8, 'Sustainable Systems', 'B8888888D', 250, '{"environmental": {"totalCo2SavedKg": 150.0, "totalEwasteSavedKg": 120.0}, "social": {"itemsDonated": 30}}', 'uploads/Suitanable-Service.jpg', TRUE),
(9, 'ReNew Devices', 'B9999999E', 80, '{"environmental": {"totalCo2SavedKg": 40.0, "totalEwasteSavedKg": 40.0}, "social": {"itemsDonated": 8}}', 'uploads/Renew-Devices.jpg', TRUE),
(10, 'Circular Electronics', 'B1010101A', 220, '{"environmental": {"totalCo2SavedKg": 130.5, "totalEwasteSavedKg": 130.5}, "social": {"itemsDonated": 28}}', 'uploads/Circular-Electronics.png', TRUE),
(11, 'Green Chip', 'B1212121F', 110, '{"environmental": {"totalCo2SavedKg": 55.0, "totalEwasteSavedKg": 55.0}, "social": {"itemsDonated": 11}}', 'uploads/Green-Chips.png', TRUE),
(12, 'Eco Parts', 'B1313131G', 160, '{"environmental": {"totalCo2SavedKg": 85.0, "totalEwasteSavedKg": 85.0}, "social": {"itemsDonated": 16}}', 'uploads/Eco-Parts.jpg', TRUE),
(13, 'Tech Cycle', 'B1414141I', 280, '{"environmental": {"totalCo2SavedKg": 180.0, "totalEwasteSavedKg": 180.0}, "social": {"itemsDonated": 35}}', 'uploads/Tech-Cycle.png', TRUE),
(14, 'Planet PC', 'B1515151C', 90, '{"environmental": {"totalCo2SavedKg": 45.0, "totalEwasteSavedKg": 45.0}, "social": {"itemsDonated": 9}}', 'uploads/Planet-PC.png', TRUE),
(15, 'BioBytes', 'B1616161A', 140, '{"environmental": {"totalCo2SavedKg": 70.0, "totalEwasteSavedKg": 70.0}, "social": {"itemsDonated": 14}}', 'uploads/BioBytes.png', TRUE);
-- Addresses (15 records)
INSERT INTO addresses (user_id, street, city, region, zip_code, country) VALUES
(1, 'Calle Mayor 1', 'Madrid', 'Madrid', '28001', 'Spain'),
(2, 'Av. Diagonal 10', 'Barcelona', 'Catalonia', '08001', 'Spain'),
(3, 'Calle Colon 5', 'Valencia', 'Valencia', '46001', 'Spain'),
(4, 'Calle Sierpes 2', 'Sevilla', 'Andalucia', '41001', 'Spain'),
(5, 'Gran Via 20', 'Bilbao', 'Basque Country', '48001', 'Spain'),
(6, 'Calle Larios 8', 'Malaga', 'Andalucia', '29001', 'Spain'),
(7, 'Paseo Independencia 15', 'Zaragoza', 'Aragon', '50001', 'Spain'),
(8, 'Calle Uría 3', 'Oviedo', 'Asturias', '33001', 'Spain'),
(9, 'Calle Real 12', 'A Coruña', 'Galicia', '15001', 'Spain'),
(10, 'Plaza Mayor 4', 'Salamanca', 'Castilla y Leon', '37001', 'Spain'),
(11, 'Calle San Miguel 7', 'Palma', 'Balearic Islands', '07001', 'Spain'),
(12, 'Calle Triana 9', 'Las Palmas', 'Canary Islands', '35001', 'Spain'),
(13, 'Calle Estafeta 6', 'Pamplona', 'Navarra', '31001', 'Spain'),
(14, 'Calle Laurel 1', 'Logroño', 'La Rioja', '26001', 'Spain'),
(15, 'Calle Tajo 22', 'Toledo', 'Castilla-La Mancha', '45001', 'Spain');

-- Categories (5 records)
INSERT INTO categories (name, description, is_active) VALUES 
('Ordenadores', 'Equipos completos, portátiles y estaciones de trabajo para todo tipo de uso', TRUE),
('Componentes', 'Hardware interno y piezas fundamentales para el montaje y actualización de equipos', TRUE),
('Periféricos', 'Dispositivos externos de entrada, salida y accesorios de interacción', TRUE),
('Smartphones', 'Dispositivos móviles inteligentes, tablets y accesorios de telefonía', TRUE),
('Servidores', 'Sistemas de alto rendimiento para almacenamiento, redes y gestión de datos', TRUE);

-- Subcategories (14 records) alineadas con el frontend
INSERT INTO subcategories (category_id, name, description, is_active) VALUES
(1, 'Gama Baja', 'Equipos básicos para tareas de ofimática, navegación web y estudio', TRUE), -- ID 1 (Ordenadores)
(1, 'Gama Media', 'Equipos equilibrados para multitarea, diseño ligero y entretenimiento', TRUE), -- ID 2 (Ordenadores)
(1, 'Gama Alta', 'Estaciones de trabajo y equipos de alto rendimiento para gaming y edición profesional', TRUE), -- ID 3 (Ordenadores)
(4, 'Gama Baja', 'Teléfonos móviles esenciales con funciones básicas y alta autonomía', TRUE), -- ID 4 (Smartphones)
(4, 'Gama Media', 'Smartphones con gran relación calidad-precio y cámaras versátiles', TRUE), -- ID 5 (Smartphones)
(4, 'Gama Alta', 'Dispositivos premium con máxima potencia, mejores cámaras y pantallas OLED', TRUE), -- ID 6 (Smartphones)
(3, 'Audio', 'Altavoces, auriculares y sistemas de sonido de alta fidelidad', TRUE), -- ID 7 (Periféricos)
(3, 'Teclados y Ratones', 'Periféricos de entrada: teclados mecánicos/membrana y ratones ópticos/láser', TRUE), -- ID 8 (Periféricos)
(3, 'Monitores', 'Pantallas de alta resolución, diversas tasas de refresco y tecnologías de panel', TRUE), -- ID 9 (Periféricos)
(2, 'Almacenamiento', 'Unidades de estado sólido (SSD) y discos duros rígidos (HDD)', TRUE), -- ID 10 (Componentes)
(2, 'Placas Base y RAM', 'Placas base de diversos chipsets y módulos de memoria RAM DDR4 y DDR5', TRUE), -- ID 11 (Componentes)
(2, 'Procesadores', 'Unidades centrales de procesamiento (CPU) de última generación Intel y AMD', TRUE), -- ID 12 (Componentes)
(5, 'Servidor Torre', 'Servidores con formato de torre para pequeñas y medianas empresas', TRUE), -- ID 13 (Servidores)
(5, 'Servidor Rack', 'Sistemas optimizados para montaje en armarios rack de centros de datos', TRUE); -- ID 14 (Servidores)

-- Badges (4 records)
INSERT INTO badges (name, icon_url, is_active) VALUES
('Eco-Friendly', '/icons/eco.png', TRUE),
('Top Seller', '/icons/top.png', TRUE),
('Fast Shipper', '/icons/fast.png', TRUE),
('Community Star', '/icons/star.png', FALSE);

-- Products (15 records)
INSERT INTO products (company_id, subcategory_id, title, description, condition_status, listing_type, price, stock_quantity, status, street, city, region, zip_code, country) VALUES
(1, 1, 'Mini PC Lenovo i3', '8GB RAM, 256GB SSD, ideal ofimática', 'USED_GOOD', 'SALE', 45.00, 5, 'ACTIVE', 'Calle Mayor 1', 'Valencia', 'Valencia', '46001', 'España'),
(2, 2, 'Laptop HP Pavilion Ryzen 5', '16GB RAM DDR4, 512GB SSD', 'USED_FAIR', 'SALE', 85.00, 2, 'ACTIVE', 'Gran Vía 12', 'Bilbao', 'Vizcaya', '48001', 'España'),
(3, 3, 'PC Gaming Predator i9', '32GB DDR5, 1TB SSD M.2', 'USED_GOOD', 'SALE', 150.00, 10, 'ACTIVE', 'Paseo Independencia 5', 'Zaragoza', 'Zaragoza', '50001', 'España'),
(4, 4, 'Samsung Galaxy A14', 'Batería nueva', 'USED_GOOD', 'SALE', 30.00, 8, 'ACTIVE','Avenida del Cid 20', 'Valencia', 'Valencia', '46014', 'España'),
(5, 5, 'Xiaomi Redmi Note 12', 'Como nuevo, con caja', 'NEW', 'SALE', 65.00, 20, 'ACTIVE','Plaza Circular 3', 'Murcia', 'Murcia', '30001', 'España'),
(6, 6, 'iPhone 13 Pro', 'Reacondicionado, 128GB', 'USED_FAIR', 'DONATION', 0.00, 5, 'ACTIVE', 'Calle Alcalá 50', 'Madrid', 'Madrid', '28001', 'España'),
(7, 7, 'Auriculares HyperX Cloud', 'Con micrófono, poco uso', 'NEW', 'SALE', 15.00, 15, 'ACTIVE', 'Calle Real 15', 'Santander', 'Cantabria', '39001', 'España'),
(8, 8, 'Teclado Mecánico Logitech', 'Switch Blue', 'USED_GOOD', 'SALE', 18.00, 3, 'ACTIVE', 'Calle Colón 10', 'Valencia', 'Valencia', '46004', 'España'),
(9, 9, 'Monitor Dell 24"', 'FHD 60Hz', 'USED_GOOD', 'SALE', 25.00, 1, 'ACTIVE', 'Carrer de Balmes 40', 'Barcelona', 'Barcelona', '08001', 'España'),
(10, 10, 'SSD Kingston 512GB', 'Disco sólido SATA3 512GB', 'NEW', 'SALE', 12.00, 50, 'ACTIVE', 'Calle Sierpes 5', 'Sevilla', 'Sevilla', '41004', 'España'),
(11, 11, 'Kit Memoria RAM Corsair 32GB', 'DDR4 ideal para placas ATX', 'USED_FAIR', 'SALE', 22.00, 4, 'ACTIVE',  'Calle de la Paz 8', 'Valencia', 'Valencia', '46003', 'España'),
(12, 12, 'Intel Core i5-10400F', '6 Núcleos, 12 Hilos, 80W TDP', 'USED_GOOD', 'DONATION', 0.00, 10, 'ACTIVE', 'Avinguda Diagonal 100', 'Barcelona', 'Barcelona', '08019', 'España'),
(13, 13, 'Servidor Torre Dell PowerEdge', 'Intel Xeon, 32GB ECC, 2x2TB HDD', 'USED_GOOD', 'SALE', 125.00, 6, 'ACTIVE','Paseo de la Castellana 120', 'Madrid', 'Madrid', '28046', 'España'),
(14, 14, 'Servidor Rack 1U HP ProLiant', 'Doble Xeon, 64GB ECC, 4x1TB SSD', 'USED_GOOD', 'SALE', 145.00, 7, 'ACTIVE', 'Calle Betis 30', 'Sevilla', 'Sevilla', '41010', 'España'),
(15, 8, 'Ratón Razer DeathAdder', 'Ratón gaming básico', 'NEW', 'DONATION', 0.00, 100, 'ACTIVE', 'Plaza Mayor 1', 'Valladolid', 'Valladolid', '47001', 'España');

-- Products Images (15 records) - ¡AÑADIDO! Relacionando cada producto con su imagen
INSERT INTO products_img (product_id, image_url) VALUES
(1, 'uploads/mini-pc-lenovo-i3.png'), (2, 'uploads/laptop-hp-pavilion-ryzen-5.png'), 
(3, 'uploads/pc-gaming-predator-i9.png'), (4, 'uploads/samsung-galaxy-a14.png'), 
(5, 'uploads/xiaomi-redmi-note-12.png'), (6, 'uploads/iphone-13-pro.png'), 
(7, 'uploads/auriculares-hyperx-cloud.png'), (8, 'uploads/teclado-mecanico-logitech.png'), 
(9, 'uploads/monitor-dell-24.png'), (10, 'uploads/ssd-kingston-512gb.png'), 
(11, 'uploads/kit-memoria-ram-corsair-32gb.png'), (12, 'uploads/intel-core-i5-10400f.png'), 
(13, 'uploads/servidor-torre-dell-poweredge.png'), (14, 'uploads/servidor-rack-1u-hp-proliant.png'), 
(15, 'uploads/raton-razer-deathadder.png');

-- Cart Items (15 records)
INSERT INTO cart_items (user_id, product_id, quantity) VALUES
(16, 1, 1), (16, 3, 2), (16, 5, 1),
(17, 2, 1), (17, 4, 1), (17, 6, 1),
(18, 7, 1), (18, 8, 2), (18, 9, 1),
(19, 10, 5), (19, 11, 1), (19, 12, 1),
(20, 13, 1), (20, 14, 1), (20, 15, 2);

-- Orders (15 records)
INSERT INTO orders (buyer_user_id, order_date, total_amount, status, requires_shipping) VALUES
(16, '2024-01-05 10:00:00', 250.00, 'COMPLETED', TRUE),
(17, '2024-01-10 11:30:00', 300.00, 'SHIPPED', TRUE),
(18, '2024-01-15 09:15:00', 150.00, 'PROCESSING', TRUE),
(19, '2024-01-20 14:45:00', 120.00, 'PAID', TRUE),
(20, '2024-02-01 16:00:00', 80.00, 'PENDING_PAYMENT', TRUE),
(16, '2024-02-05 08:30:00', 100.00, 'COMPLETED', TRUE),
(17, '2024-02-10 12:20:00', 50.00, 'SHIPPED', TRUE),
(18, '2024-02-15 10:10:00', 110.00, 'COMPLETED', TRUE),
(19, '2024-02-20 15:50:00', 15.00, 'COMPLETED', TRUE),
(20, '2024-03-01 09:00:00', 350.00, 'PROCESSING', TRUE),
(16, '2024-03-05 13:40:00', 90.00, 'PAID', TRUE),
(17, '2024-03-10 17:15:00', 85.00, 'SHIPPED', TRUE),
(18, '2024-03-15 11:25:00', 250.00, 'COMPLETED', TRUE),
(19, '2024-03-20 14:05:00', 300.00, 'CANCELLED', FALSE),
(20, '2024-04-01 10:55:00', 150.00, 'COMPLETED', TRUE);

-- Order Details (15 records)
INSERT INTO order_details (order_id, product_id, quantity, unit_price_at_purchase) VALUES
(1, 1, 1, 250.00), (2, 2, 1, 300.00), (3, 3, 1, 150.00),
(4, 4, 1, 120.00), (5, 5, 1, 80.00), (6, 7, 1, 100.00),
(7, 8, 1, 50.00), (8, 9, 1, 110.00), (9, 10, 1, 15.00),
(10, 11, 1, 350.00), (11, 13, 1, 90.00), (12, 14, 1, 85.00),
(13, 1, 1, 250.00), (14, 2, 1, 300.00), (15, 3, 1, 150.00);

-- Carriers
INSERT INTO shipping_carriers (carrier_name, base_price, estimated_days, carrier_logo, tracking_url, is_active) VALUES
('DHL Express', 12.50, 2, 'url_logo_dhl.svg', 'https://www.dhl.com/es-es/home/tracking/tracking-main.html?tracking-id={}', TRUE),
('UPS', 11.00, 3, 'url_logo_ups.svg', 'https://www.ups.com/track?loc=es_ES&tracknum={}&adapter=Default', TRUE),
('Correos', 4.95, 5, 'url_logo_correos.svg', 'https://www.correos.es/es/es/herramientas/localizador/envios/detalle?numero={}', TRUE),
('SEUR', 8.90, 2, 'url_logo_seur.svg', 'https://www.seur.com/livetracking/pages/seguimiento-online-busqueda.do?id={}', TRUE),
('MRW', 7.50, 1, 'url_logo_mrw.svg', 'https://www.mrw.es/seguimiento-envios-mrw/?n_envio={}', TRUE);

-- Shipments (15 records)
INSERT INTO shipments (order_id, carrier_id, shipping_street, shipping_city, shipping_zip_code, shipping_country, tracking_number, shipment_status) VALUES
(1, 1, 'Calle A 1', 'Madrid', '28001', 'Spain', 'TRK001', 'DELIVERED'),
(2, 2, 'Calle B 2', 'Barcelona', '08001', 'Spain', 'TRK002', 'IN_TRANSIT'),
(3, 3, 'Calle C 3', 'Valencia', '46001', 'Spain', 'TRK003', 'PREPARING'),
(4, 4, 'Calle D 4', 'Sevilla', '41001', 'Spain', 'TRK004', 'PREPARING'),
(5, 5, 'Calle E 5', 'Bilbao', '48001', 'Spain', 'TRK005', 'PREPARING'),
(6, 1, 'Calle F 6', 'Malaga', '29001', 'Spain', 'TRK006', 'DELIVERED'),
(7, 2, 'Calle G 7', 'Zaragoza', '50001', 'Spain', 'TRK007', 'IN_TRANSIT'),
(8, 3, 'Calle H 8', 'Oviedo', '33001', 'Spain', 'TRK008', 'DELIVERED'),
(9, 4, 'Calle I 9', 'A Coruña', '15001', 'Spain', 'TRK009', 'DELIVERED'),
(10, 5, 'Calle J 10', 'Salamanca', '37001', 'Spain', 'TRK010', 'PREPARING'),
(11, 1, 'Calle K 11', 'Palma', '07001', 'Spain', 'TRK011', 'PREPARING'),
(12, 2, 'Calle L 12', 'Las Palmas', '35001', 'Spain', 'TRK012', 'IN_TRANSIT'),
(13, 3, 'Calle M 13', 'Pamplona', '31001', 'Spain', 'TRK013', 'DELIVERED'),
(14, 4, 'Calle N 14', 'Logroño', '26001', 'Spain', 'TRK014', 'PREPARING'),
(15, 5, 'Calle O 15', 'Toledo', '45001', 'Spain', 'TRK015', 'DELIVERED');



-- Posts (15 records)
INSERT INTO posts (author_user_id, title, body, created_at) VALUES
(1, 'Bienvenida', 'Hola a todos', '2023-11-01 10:00:00'),
(1, 'Nuevas llegadas de portátiles', 'Acaban de llegar varios modelos reacondicionados de Dell y Lenovo en perfecto estado. Aprovecha los precios.', '2024-02-01 09:00:00'),
(1, 'Consejo de mantenimiento', 'Limpiad el polvo de vuestros equipos al menos cada 6 meses. Os alargará la vida útil considerablemente.', '2024-02-15 10:30:00'),
(1, 'Descuento del 15% esta semana', 'Por el aniversario de Tech Solutions aplicamos un 15% de descuento en todos nuestros productos hasta el domingo.', '2024-03-01 08:00:00'),
(2, 'Oferta especial', 'Descuentos en portatiles', '2023-11-05 11:00:00'),
(3, 'Reciclaje', 'Importancia de reciclar', '2023-11-10 12:00:00'),
(4, 'Nuevos productos', 'Llegada de stock', '2023-11-15 13:00:00'),
(5, 'Evento', 'Feria tecnologica', '2023-11-20 14:00:00'),
(6, 'Guia de compra', 'Como elegir GPU', '2023-11-25 15:00:00'),
(7, 'Mantenimiento', 'Limpieza de PC', '2023-12-01 16:00:00'),
(8, 'Noticias', 'Avances en chips', '2023-12-05 17:00:00'),
(9, 'Sorteo', 'Participa y gana', '2023-12-10 18:00:00'),
(10, 'Opinion', 'Review de monitor', '2023-12-15 19:00:00'),
(11, 'Tutorial', 'Instalar RAM', '2023-12-20 20:00:00'),
(12, 'Pregunta', 'Duda sobre compatibilidad', '2023-12-25 21:00:00'),
(13, 'Comparativa', 'Intel vs AMD', '2024-01-01 09:00:00'),
(14, 'Historia', 'Evolucion de los PCs', '2024-01-05 10:00:00'),
(15, 'Agradecimiento', 'Gracias por el soporte', '2024-01-10 11:00:00');

-- Comments (15 records)
INSERT INTO comments (post_id, author_user_id, body, created_at) VALUES
(1, 16, 'Hola!', '2023-11-01 10:30:00'), (1, 17, 'Bienvenidos', '2023-11-01 11:00:00'),
(2, 18, 'Interesante', '2023-11-05 12:00:00'), (2, 19, 'Buenos precios', '2023-11-05 13:00:00'),
(3, 20, 'Muy cierto', '2023-11-10 14:00:00'), (3, 16, 'Yo reciclo siempre', '2023-11-10 15:00:00'),
(4, 17, 'Que modelos?', '2023-11-15 16:00:00'), (4, 18, 'Esperando stock', '2023-11-15 17:00:00'),
(5, 19, 'Donde es?', '2023-11-20 18:00:00'), (5, 20, 'Ire seguro', '2023-11-20 19:00:00'),
(6, 16, 'Gracias por la guia', '2023-11-25 20:00:00'), (6, 17, 'Muy util', '2023-11-25 21:00:00'),
(7, 18, 'Buen consejo', '2023-12-01 22:00:00'), (7, 19, 'Lo probare', '2023-12-01 23:00:00'),
(8, 20, 'Increible', '2023-12-05 09:00:00'),
(16, 17, 'Genial, mirare los modelos disponibles', '2024-02-01 10:00:00'),
(16, 18, 'Tienen Lenovo ThinkPad disponible?', '2024-02-01 11:30:00'),
(17, 19, 'Muy buen consejo, lo hare este fin de semana', '2024-02-15 12:00:00'),
(18, 20, 'Perfecto, justo lo que necesitaba', '2024-03-01 09:00:00'),
(18, 16, 'Aproveche el descuento ayer, trato excelente', '2024-03-02 10:00:00');

-- Proposals (15 records)
INSERT INTO proposals (requester_user_id, category_id, title, description, status, created_at) VALUES
(16, 1, 'Solicitud PC Escuela', 'Necesitamos 5 PCs para el aula de informática', 'OPEN', '2024-01-01 10:00:00'),
(17, 1, 'Donacion ONG', 'Portátiles para trabajo social en campo', 'OPEN', '2024-01-02 11:00:00'),
(18, 4, 'Hardware para taller', 'Componentes varios para montaje de equipos', 'FULFILLED', '2024-01-03 12:00:00'),
(19, 3, 'Pantallas Centro Social', 'Monitores usados en buen estado', 'OPEN', '2024-01-04 13:00:00'),
(20, 3, 'Teclados Biblioteca', 'Periféricos de entrada para terminales públicos', 'FULFILLED', '2024-01-05 14:00:00'),
(16, 3, 'Ratones Aula', 'Ratones USB para sustitución de material dañado', 'OPEN', '2024-01-06 15:00:00'),
(17, 3, 'Impresora Asociacion', 'Impresora láser para tareas administrativas', 'OPEN', '2024-01-07 16:00:00'),
(18, 4, 'Cables Red', 'Cableado estructurado para nueva sede', 'FULFILLED', '2024-01-08 17:00:00'),
(19, 5, 'Servidor Web', 'Servidor rack para alojamiento de proyectos locales', 'OPEN', '2024-01-09 18:00:00'),
(20, 2, 'Tablets Educacion', 'Tablets android para alumnos con pocos recursos', 'OPEN', '2024-01-10 19:00:00'),
(16, 3, 'Proyector Sala', 'Proyector HDMI para conferencias', 'FULFILLED', '2024-01-11 20:00:00'),
(17, 3, 'Altavoces Eventos', 'Equipo de sonido básico para actos comunitarios', 'OPEN', '2024-01-12 21:00:00'),
(18, 3, 'Webcams Cursos', 'Cámaras USB para formación online', 'OPEN', '2024-01-13 09:00:00'),
(19, 4, 'Discos Duros Backup', 'HDD Externos para copia de seguridad de datos', 'FULFILLED', '2024-01-14 10:00:00'),
(20, 3, 'Memorias USB', 'Pendrives 16GB para entrega de material didáctico', 'OPEN', '2024-01-15 11:00:00');

-- Reviews (15 records)
INSERT INTO reviews (reviewer_user_id, target_company_id, rating, review_comment, created_at) VALUES
(16, 1, 5, 'Excelente servicio', '2024-02-01 10:00:00'),
(17, 2, 4, 'Buen producto', '2024-02-02 11:00:00'),
(18, 3, 3, 'Envio lento', '2024-02-03 12:00:00'),
(19, 4, 5, 'Muy recomendado', '2024-02-04 13:00:00'),
(20, 5, 2, 'Llego dañado', '2024-02-05 14:00:00'),
(16, 6, 4, 'Todo correcto', '2024-02-06 15:00:00'),
(17, 7, 5, 'Rapidisimo', '2024-02-07 16:00:00'),
(18, 8, 1, 'No contestan', '2024-02-08 17:00:00'),
(19, 9, 3, 'Regular', '2024-02-09 18:00:00'),
(20, 10, 5, 'Gran calidad', '2024-02-10 19:00:00'),
(16, 11, 4, 'Volvere a comprar', '2024-02-11 20:00:00'),
(17, 12, 5, 'Perfecto', '2024-02-12 21:00:00'),
(18, 13, 2, 'Mal embalaje', '2024-02-13 09:00:00'),
(19, 14, 4, 'Buen precio', '2024-02-14 10:00:00'),
(20, 15, 5, 'Genial', '2024-02-15 11:00:00');

-- Company Badges (15 records)
INSERT INTO company_badges (company_id, badge_id, awarded_at) VALUES
(1, 1, '2023-06-01 10:00:00'), (2, 2, '2023-06-02 11:00:00'), (3, 3, '2023-06-03 12:00:00'),
(4, 4, '2023-06-04 13:00:00'), (5, 1, '2023-06-05 14:00:00'), (6, 2, '2023-06-06 15:00:00'),
(7, 3, '2023-06-07 16:00:00'), (8, 4, '2023-06-08 17:00:00'), (9, 1, '2023-06-09 18:00:00'),
(10, 2, '2023-06-10 19:00:00'), (11, 3, '2023-06-11 20:00:00'), (12, 4, '2023-06-12 21:00:00'),
(13, 1, '2023-06-13 09:00:00'), (14, 2, '2023-06-14 10:00:00'), (15, 3, '2023-06-15 11:00:00');

-- Descriptions
UPDATE companies SET company_description = 'Especialistas en soluciones tecnológicas refurbishadas. Damos segunda vida a equipos de alto rendimiento para empresas y particulares con garantía certificada.' WHERE company_id = 1;
UPDATE companies SET company_description = 'Comprometidos con el medio ambiente. Recuperamos hardware usado y lo transformamos en productos sostenibles con el menor impacto ecológico posible.' WHERE company_id = 2;
UPDATE companies SET company_description = 'Líderes en reciclaje de componentes electrónicos. Nuestro proceso garantiza la correcta gestión de residuos tecnológicos y la reutilización de materiales.' WHERE company_id = 3;
UPDATE companies SET company_description = 'Vendemos PCs de segunda mano completamente revisados y actualizados. Cada equipo pasa por más de 50 pruebas antes de llegar a tus manos.' WHERE company_id = 4;
UPDATE companies SET company_description = 'Tu tienda de confianza para componentes ecológicos. Primamos la circularidad y el ahorro sin renunciar a la calidad.' WHERE company_id = 5;

UPDATE users SET description = 'Apasionado de la tecnología y el DIY. Siempre buscando la mejor relación calidad-precio en hardware.' WHERE user_id = 16;
UPDATE users SET description = 'Técnico informático con 10 años de experiencia. Me encanta restaurar equipos antiguos y darles una nueva vida.' WHERE user_id = 17;
UPDATE users SET description = 'Diseñadora gráfica freelance. Busco equipos refurbishados para mi estudio creativo sin arruinar el planeta.' WHERE user_id = 18;
UPDATE users SET description = 'Estudiante de ingeniería informática. Colecciono componentes raros y me gusta montar mis propios equipos.' WHERE user_id = 19;

-- Follows (20 records — users 16-20 siguiendo empresas)
INSERT INTO follows (follower_id, following_id, created_at) VALUES
(16, 1, '2024-01-10 10:00:00'), (16, 2, '2024-01-10 11:00:00'),
(16, 3, '2024-01-11 09:00:00'), (16, 4, '2024-02-01 10:00:00'),
(17, 1, '2024-01-15 10:00:00'), (17, 5, '2024-01-16 11:00:00'),
(17, 6, '2024-01-17 12:00:00'), (17, 7, '2024-02-05 09:00:00'),
(18, 2, '2024-01-20 10:00:00'), (18, 8, '2024-01-21 11:00:00'),
(18, 9, '2024-02-10 09:00:00'), (18, 10, '2024-02-11 10:00:00'),
(19, 3, '2024-01-25 10:00:00'), (19, 11, '2024-01-26 11:00:00'),
(19, 12, '2024-02-15 09:00:00'), (19, 13, '2024-02-16 10:00:00'),
(20, 4, '2024-01-30 10:00:00'), (20, 14, '2024-02-01 11:00:00'),
(20, 15, '2024-02-20 09:00:00'), (20, 1, '2024-03-01 10:00:00');

-- Additional Comments (15 more records)
INSERT INTO comments (post_id, author_user_id, body, created_at) VALUES
(9, 16, 'Muy útil la guía, me ha ayudado a decidirme', '2023-11-26 09:00:00'),
(9, 20, 'Le echaré un vistazo al monitor LG 4K también', '2023-11-26 10:00:00'),
(10, 17, 'Imprescindible hacerlo cada vez que cambia el tiempo', '2023-12-02 08:00:00'),
(10, 18, 'Yo uso aire comprimido y funciona genial', '2023-12-02 09:30:00'),
(11, 19, 'Los chips de nueva generación van a cambiar todo', '2023-12-06 11:00:00'),
(12, 16, 'Ya estoy participando, mucha suerte a todos', '2023-12-11 09:00:00'),
(13, 17, 'Gracias por la review, me ha sido muy útil', '2023-12-16 10:00:00'),
(14, 18, 'Tutorial muy claro, lo seguí paso a paso sin problemas', '2023-12-21 11:00:00'),
(15, 19, 'Yo tuve ese mismo problema con una placa MSI', '2023-12-26 12:00:00'),
(15, 20, 'Te recomiendo revisar la lista de compatibilidad oficial', '2023-12-26 13:00:00'),
(16, 16, 'AMD sigue mejorando mucho en relación calidad-precio', '2024-01-02 10:00:00'),
(17, 17, 'Increíble cómo han evolucionado los procesadores en 20 años', '2024-01-06 11:00:00'),
(18, 18, 'De nada, es un placer ayudar a la comunidad', '2024-01-11 12:00:00'),
(2, 20, 'Perfecto, justo necesitaba un portátil para trabajar en casa', '2024-02-03 14:00:00'),
(4, 16, 'Aproveché el descuento, excelente trato como siempre', '2024-03-02 09:30:00');

-- Additional Reviews (15 more records)
INSERT INTO reviews (reviewer_user_id, target_company_id, rating, review_comment, created_at) VALUES
(17, 1, 4, 'Muy buena atención al cliente', '2024-03-01 10:00:00'),
(18, 2, 5, 'Productos en perfecto estado, muy recomendados', '2024-03-02 11:00:00'),
(19, 3, 4, 'Reciclaje responsable y rápido', '2024-03-03 12:00:00'),
(20, 4, 5, 'El equipo llegó impecable y completamente revisado', '2024-03-04 13:00:00'),
(16, 5, 3, 'Correctos pero algo lentos en responder', '2024-03-05 14:00:00'),
(17, 8, 4, 'Sistemas bien configurados de fábrica', '2024-03-06 15:00:00'),
(18, 9, 5, 'Servicio excelente, totalmente recomendados', '2024-03-07 16:00:00'),
(19, 10, 4, 'Buena calidad de componentes y buen precio', '2024-03-08 17:00:00'),
(20, 11, 3, 'Precio justo, la presentación es mejorable', '2024-03-09 18:00:00'),
(16, 12, 5, 'Las piezas llegaron perfectamente embaladas', '2024-03-10 19:00:00'),
(17, 13, 4, 'Ciclo de vida del producto muy bien gestionado', '2024-03-11 20:00:00'),
(18, 14, 5, 'Servidores de gran calidad a buen precio', '2024-03-12 09:00:00'),
(19, 15, 4, 'BioBytes cumple con sus promesas ecológicas', '2024-03-13 10:00:00'),
(20, 6, 4, 'Future Tech tiene productos realmente innovadores', '2024-03-14 11:00:00'),
(16, 7, 5, 'Hardware Heroes, el nombre lo dice todo', '2024-03-15 12:00:00');
