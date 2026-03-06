-- Users (20 records to support companies and buyers)
INSERT INTO users (email, password_hash, first_name, last_name, role, created_at) VALUES
('comp1@fenix.com', 'hash1', 'Juan', 'Perez', 'EMPRESA', '2023-01-10 10:00:00'),
('comp2@fenix.com', 'hash2', 'Maria', 'Gomez', 'EMPRESA', '2023-01-15 11:00:00'),
('comp3@fenix.com', 'hash3', 'Carlos', 'Ruiz', 'EMPRESA', '2023-02-01 09:30:00'),
('comp4@fenix.com', 'hash4', 'Ana', 'Lopez', 'EMPRESA', '2023-02-20 14:00:00'),
('comp5@fenix.com', 'hash5', 'Luis', 'Torres', 'EMPRESA', '2023-03-05 16:20:00'),
('comp6@fenix.com', 'hash6', 'Elena', 'Diaz', 'EMPRESA', '2023-03-15 08:45:00'),
('comp7@fenix.com', 'hash7', 'Pedro', 'Sanchez', 'EMPRESA', '2023-04-01 12:00:00'),
('comp8@fenix.com', 'hash8', 'Sofia', 'Martin', 'EMPRESA', '2023-04-10 10:15:00'),
('comp9@fenix.com', 'hash9', 'Miguel', 'Hernandez', 'EMPRESA', '2023-05-05 11:30:00'),
('comp10@fenix.com', 'hash10', 'Lucia', 'Jimenez', 'EMPRESA', '2023-05-20 15:45:00'),
('comp11@fenix.com', 'hash11', 'David', 'Alvarez', 'EMPRESA', '2023-06-01 09:00:00'),
('comp12@fenix.com', 'hash12', 'Carmen', 'Moreno', 'EMPRESA', '2023-06-15 13:20:00'),
('comp13@fenix.com', 'hash13', 'Jorge', 'Munoz', 'EMPRESA', '2023-07-01 17:00:00'),
('comp14@fenix.com', 'hash14', 'Raquel', 'Romero', 'EMPRESA', '2023-07-20 10:30:00'),
('comp15@fenix.com', 'hash15', 'Alberto', 'Navarro', 'EMPRESA', '2023-08-05 14:15:00'),
('user1@fenix.com', 'hash16', 'Laura', 'Gil', 'PARTICULAR', '2023-08-15 16:00:00'),
('user2@fenix.com', 'hash17', 'Roberto', 'Serrano', 'PARTICULAR', '2023-09-01 09:45:00'),
('user3@fenix.com', 'hash18', 'Isabel', 'Blanco', 'PARTICULAR', '2023-09-20 11:15:00'),
('user4@fenix.com', 'hash19', 'Fernando', 'Molina', 'PARTICULAR', '2023-10-05 13:30:00'),
('admin@fenix.com', 'hash20', 'Admin', 'System', 'ADMIN', '2023-01-01 00:00:00');

-- Companies (15 records)
INSERT INTO companies (user_id, company_name, cif, reputation_score, impact_metrics) VALUES
(1, 'Tech Solutions', 'B11111111', 100, '{"environmental": {"totalCo2SavedKg": 50.5}, "social": {"itemsDonated": 10}}'),
(2, 'Green Hardware', 'B22222222', 200, '{"environmental": {"totalCo2SavedKg": 120.0}, "social": {"itemsDonated": 25}}'),
(3, 'Recycle IT', 'B33333333', 150, '{"environmental": {"totalCo2SavedKg": 80.2}, "social": {"itemsDonated": 15}}'),
(4, 'Second Life PC', 'B44444444', 300, '{"environmental": {"totalCo2SavedKg": 200.5}, "social": {"itemsDonated": 40}}'),
(5, 'EcoComp', 'B55555555', 50, '{"environmental": {"totalCo2SavedKg": 20.0}, "social": {"itemsDonated": 5}}'),
(6, 'Future Tech', 'B66666666', 120, '{"environmental": {"totalCo2SavedKg": 60.0}, "social": {"itemsDonated": 12}}'),
(7, 'Hardware Heroes', 'B77777777', 180, '{"environmental": {"totalCo2SavedKg": 90.5}, "social": {"itemsDonated": 18}}'),
(8, 'Sustainable Systems', 'B88888888', 250, '{"environmental": {"totalCo2SavedKg": 150.0}, "social": {"itemsDonated": 30}}'),
(9, 'ReNew Devices', 'B99999999', 80, '{"environmental": {"totalCo2SavedKg": 40.0}, "social": {"itemsDonated": 8}}'),
(10, 'Circular Electronics', 'B10101010', 220, '{"environmental": {"totalCo2SavedKg": 130.5}, "social": {"itemsDonated": 28}}'),
(11, 'Green Chip', 'B12121212', 110, '{"environmental": {"totalCo2SavedKg": 55.0}, "social": {"itemsDonated": 11}}'),
(12, 'Eco Parts', 'B13131313', 160, '{"environmental": {"totalCo2SavedKg": 85.0}, "social": {"itemsDonated": 16}}'),
(13, 'Tech Cycle', 'B14141414', 280, '{"environmental": {"totalCo2SavedKg": 180.0}, "social": {"itemsDonated": 35}}'),
(14, 'Planet PC', 'B15151515', 90, '{"environmental": {"totalCo2SavedKg": 45.0}, "social": {"itemsDonated": 9}}'),
(15, 'BioBytes', 'B16161616', 140, '{"environmental": {"totalCo2SavedKg": 70.0}, "social": {"itemsDonated": 14}}');

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
INSERT INTO categories (name, description) VALUES 
('Ordenadores', 'Equipos completos de sobremesa y portátiles'),
('Componentes', 'Piezas internas para PC y servidores'),
('Periféricos', 'Dispositivos externos de entrada y salida'),
('Redes', 'Equipos de conectividad y redes'),
('Impresión', 'Impresoras, escáneres y consumibles');

-- Subcategories (10 records)
INSERT INTO subcategories (category_id, name, description) VALUES
(1, 'Portátiles', 'Ordenadores portátiles de todo tipo'),
(1, 'Sobremesa', 'Torres y equipos all-in-one'),
(2, 'Tarjetas Gráficas', 'GPUs para gaming y diseño'),
(2, 'Procesadores', 'CPUs Intel y AMD'),
(3, 'Teclados', 'Teclados mecánicos y de membrana'),
(3, 'Ratones', 'Ratones ópticos y láser'),
(4, 'Routers', 'Routers WiFi y neutros'),
(4, 'Switches', 'Switches gestionables y no gestionables'),
(5, 'Impresoras 3D', 'Impresoras de filamento y resina'),
(5, 'Cartuchos', 'Tinta y tóner');

-- Badges (4 records)
INSERT INTO badges (name, icon_url, is_active) VALUES
('Eco-Friendly', '/icons/eco.png', TRUE),
('Top Seller', '/icons/top.png', TRUE),
('Fast Shipper', '/icons/fast.png', TRUE),
('Community Star', '/icons/star.png', FALSE);

-- Products (15 records)
INSERT INTO products (company_id, subcategory_id, title, description, image_url, condition_status, listing_type, price, stock_quantity, status) VALUES
(1, 1, 'Laptop Dell Latitude', 'Buen estado, i5 8th gen', 'url1', 'USED_GOOD', 'SALE', 250.00, 5, 'ACTIVE'),
(2, 2, 'PC Gaming Entry', 'GTX 1050, i3', 'url2', 'USED_FAIR', 'SALE', 300.00, 2, 'ACTIVE'),
(3, 3, 'Nvidia GTX 1080', 'Usada para diseño', 'url3', 'USED_GOOD', 'SALE', 150.00, 10, 'ACTIVE'),
(4, 4, 'Intel i7 7700K', 'Sin caja original', 'url4', 'USED_GOOD', 'SALE', 120.00, 8, 'ACTIVE'),
(5, 5, 'Teclado Mecánico Logitech', 'Switch Blue', 'url5', 'NEW', 'SALE', 80.00, 20, 'ACTIVE'),
(6, 6, 'Raton Razer', 'Modelo antiguo', 'url6', 'USED_FAIR', 'DONATION', 0.00, 5, 'ACTIVE'),
(7, 7, 'Router Asus', 'WiFi 6', 'url7', 'NEW', 'SALE', 100.00, 15, 'ACTIVE'),
(8, 8, 'Switch Cisco 24p', 'Reacondicionado', 'url8', 'USED_GOOD', 'SALE', 50.00, 3, 'ACTIVE'),
(9, 9, 'Ender 3 Pro', 'Con mejoras', 'url9', 'USED_GOOD', 'SALE', 110.00, 1, 'ACTIVE'),
(10, 10, 'Toner HP 85A', 'Compatible nuevo', 'url10', 'NEW', 'SALE', 15.00, 50, 'ACTIVE'),
(11, 1, 'MacBook Air 2015', 'Bateria nueva', 'url11', 'USED_FAIR', 'SALE', 350.00, 4, 'ACTIVE'),
(12, 2, 'HP EliteDesk', 'Mini PC oficina', 'url12', 'USED_GOOD', 'DONATION', 0.00, 10, 'ACTIVE'),
(13, 3, 'AMD RX 580', '8GB VRAM', 'url13', 'USED_GOOD', 'SALE', 90.00, 6, 'ACTIVE'),
(14, 4, 'Ryzen 5 3600', 'Con disipador', 'url14', 'USED_GOOD', 'SALE', 85.00, 7, 'ACTIVE'),
(15, 5, 'Teclado Membrana HP', 'Basico', 'url15', 'NEW', 'DONATION', 0.00, 100, 'ACTIVE');

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

-- Shipments (15 records)
INSERT INTO shipments (order_id, shipping_street, shipping_city, shipping_zip_code, shipping_country, tracking_number, carrier_name, shipment_status) VALUES
(1, 'Calle A 1', 'Madrid', '28001', 'Spain', 'TRK001', 'DHL', 'DELIVERED'),
(2, 'Calle B 2', 'Barcelona', '08001', 'Spain', 'TRK002', 'UPS', 'IN_TRANSIT'),
(3, 'Calle C 3', 'Valencia', '46001', 'Spain', 'TRK003', 'Correos', 'PREPARING'),
(4, 'Calle D 4', 'Sevilla', '41001', 'Spain', 'TRK004', 'SEUR', 'PREPARING'),
(5, 'Calle E 5', 'Bilbao', '48001', 'Spain', 'TRK005', 'MRW', 'PREPARING'),
(6, 'Calle F 6', 'Malaga', '29001', 'Spain', 'TRK006', 'DHL', 'DELIVERED'),
(7, 'Calle G 7', 'Zaragoza', '50001', 'Spain', 'TRK007', 'UPS', 'IN_TRANSIT'),
(8, 'Calle H 8', 'Oviedo', '33001', 'Spain', 'TRK008', 'Correos', 'DELIVERED'),
(9, 'Calle I 9', 'A Coruña', '15001', 'Spain', 'TRK009', 'SEUR', 'DELIVERED'),
(10, 'Calle J 10', 'Salamanca', '37001', 'Spain', 'TRK010', 'MRW', 'PREPARING'),
(11, 'Calle K 11', 'Palma', '07001', 'Spain', 'TRK011', 'DHL', 'PREPARING'),
(12, 'Calle L 12', 'Las Palmas', '35001', 'Spain', 'TRK012', 'UPS', 'IN_TRANSIT'),
(13, 'Calle M 13', 'Pamplona', '31001', 'Spain', 'TRK013', 'Correos', 'DELIVERED'),
(14, 'Calle N 14', 'Logroño', '26001', 'Spain', 'TRK014', 'SEUR', 'PREPARING'),
(15, 'Calle O 15', 'Toledo', '45001', 'Spain', 'TRK015', 'MRW', 'DELIVERED');

-- Posts (15 records)
INSERT INTO posts (author_user_id, title, body, created_at) VALUES
(1, 'Bienvenida', 'Hola a todos', '2023-11-01 10:00:00'),
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
(8, 20, 'Increible', '2023-12-05 09:00:00');

-- Proposals (15 records)
INSERT INTO proposals (requester_user_id, title, description, status, created_at) VALUES
(16, 'Solicitud PC Escuela', 'Necesitamos 5 PCs', 'OPEN', '2024-01-01 10:00:00'),
(17, 'Donacion ONG', 'Portatiles para trabajo', 'OPEN', '2024-01-02 11:00:00'),
(18, 'Hardware para taller', 'Componentes varios', 'FULFILLED', '2024-01-03 12:00:00'),
(19, 'Pantallas Centro Social', 'Monitores usados', 'OPEN', '2024-01-04 13:00:00'),
(20, 'Teclados Biblioteca', 'Perifericos entrada', 'FULFILLED', '2024-01-05 14:00:00'),
(16, 'Ratones Aula', 'Ratones USB', 'OPEN', '2024-01-06 15:00:00'),
(17, 'Impresora Asociacion', 'Impresora laser', 'OPEN', '2024-01-07 16:00:00'),
(18, 'Cables Red', 'Cableado estructurado', 'FULFILLED', '2024-01-08 17:00:00'),
(19, 'Servidor Web', 'Servidor rack', 'OPEN', '2024-01-09 18:00:00'),
(20, 'Tablets Educacion', 'Tablets android', 'OPEN', '2024-01-10 19:00:00'),
(16, 'Proyector Sala', 'Proyector HDMI', 'FULFILLED', '2024-01-11 20:00:00'),
(17, 'Altavoces Eventos', 'Equipo sonido', 'OPEN', '2024-01-12 21:00:00'),
(18, 'Webcams Cursos', 'Camaras USB', 'OPEN', '2024-01-13 09:00:00'),
(19, 'Discos Duros Backup', 'HDD Externos', 'FULFILLED', '2024-01-14 10:00:00'),
(20, 'Memorias USB', 'Pendrives 16GB', 'OPEN', '2024-01-15 11:00:00');

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