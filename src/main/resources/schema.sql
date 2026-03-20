
-- ------------------------------------------------------------------------------
-- MÓDULO 1: USUARIOS, PERFILES Y DIRECCIONES
-- ------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    -- ENUM: Evita crear tabla extra para roles, optimizando rendimiento
    role ENUM(
        'PARTICULAR',
        'EMPRESA',
        'ADMIN'
    ) DEFAULT 'PARTICULAR',
    user_img VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    description TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS companies (
    company_id INT PRIMARY KEY AUTO_INCREMENT,
    -- UNIQUE en user_id garantiza la relación 1 a 1
    user_id INT UNIQUE NOT NULL,
    company_name VARCHAR(150) NOT NULL,
    cif VARCHAR(50) NOT NULL,
    -- Desnormalización intencionada: Calculado desde Reviews y Donaciones
    reputation_score INT DEFAULT 0,
    -- JSON: Flexibilidad absoluta para métricas (CO2, e-waste, agua...) sin alterar tablas
    impact_metrics JSON,
    company_img VARCHAR(255) DEFAULT NULL,
    company_description TEXT DEFAULT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

-- Tabla para gestionar seguidores y seguidos
CREATE TABLE IF NOT EXISTS follows (
    follow_id INT AUTO_INCREMENT PRIMARY KEY,
    follower_id INT NOT NULL,
    following_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (follower_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES companies (company_id) ON DELETE CASCADE,
    UNIQUE (follower_id, following_id) 
);

CREATE TABLE IF NOT EXISTS addresses (
    address_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    region VARCHAR(100) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE INDEX idx_users_active ON users (is_active);

-- ------------------------------------------------------------------------------
-- MÓDULO 2: CATÁLOGO Y PRODUCTOS
-- ------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS subcategories (
    subcategory_id INT PRIMARY KEY AUTO_INCREMENT,
    category_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (category_id) REFERENCES categories (category_id)
);

CREATE TABLE IF NOT EXISTS products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    company_id INT NOT NULL,
    subcategory_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    condition_status ENUM(
        'NEW',
        'USED_GOOD',
        'USED_FAIR'
    ) NOT NULL,
    listing_type ENUM('SALE', 'DONATION') NOT NULL,
    price DECIMAL(10, 2) DEFAULT 0.00,
    stock_quantity INT DEFAULT 1,
    status ENUM(
        'ACTIVE',
        'SOLD_OUT',
        'HIDDEN'
    ) DEFAULT 'ACTIVE',
    street VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    region VARCHAR(100) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL,
    FOREIGN KEY (company_id) REFERENCES companies (company_id) ON DELETE CASCADE,
    FOREIGN KEY (subcategory_id) REFERENCES subcategories (subcategory_id)
);

CREATE TABLE IF NOT EXISTS products_img (
    image_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE
);

CREATE INDEX idx_products_status ON products (status);

-- ------------------------------------------------------------------------------
-- MÓDULO 3: TRANSACCIONES (CARRITO, PEDIDOS Y ENVÍOS)
-- ------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS cart_items (
    cart_item_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    buyer_user_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- Desnormalización intencionada: Total calculado para agilizar reportes
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM(
        'PENDING_PAYMENT',
        'PAID',
        'PROCESSING',
        'SHIPPED',
        'COMPLETED',
        'CANCELLED'
    ) DEFAULT 'PENDING_PAYMENT',
    requires_shipping BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (buyer_user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS order_details (
    order_detail_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    -- Desnormalización crítica: "Fotocopia" del precio en el momento de la compra
    unit_price_at_purchase DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);

CREATE TABLE IF NOT EXISTS shipping_carriers (
    carrier_id INT PRIMARY KEY AUTO_INCREMENT,
    carrier_name VARCHAR(100) NOT NULL UNIQUE, 
    base_price DECIMAL(10, 2) NOT NULL,
    estimated_days INT NOT NULL,
    carrier_logo TEXT,
    tracking_url TEXT, 
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS shipments (
    shipment_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT UNIQUE NOT NULL, 
    carrier_id INT NOT NULL,

    -- Desnormalización crítica para historial
    shipping_street VARCHAR(255) NOT NULL,
    shipping_city VARCHAR(100) NOT NULL,
    shipping_zip_code VARCHAR(20) NOT NULL,
    shipping_country VARCHAR(100) NOT NULL,

    tracking_number VARCHAR(100),
    shipment_status ENUM('PREPARING', 'IN_TRANSIT', 'DELIVERED') DEFAULT 'PREPARING',
    
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (carrier_id) REFERENCES shipping_carriers(carrier_id)
);
-- ------------------------------------------------------------------------------
-- MÓDULO 4: COMUNIDAD, RESEÑAS Y GAMIFICACIÓN
-- ------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS posts (
    post_id INT PRIMARY KEY AUTO_INCREMENT,
    author_user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    body TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS posts_img (
    image_id INT PRIMARY KEY AUTO_INCREMENT,
    post_id INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts (post_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
    comment_id INT PRIMARY KEY AUTO_INCREMENT,
    post_id INT NOT NULL,
    author_user_id INT NOT NULL,
    body TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (post_id) ON DELETE CASCADE,
    FOREIGN KEY (author_user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS proposals (
    proposal_id INT PRIMARY KEY AUTO_INCREMENT,
    requester_user_id INT NOT NULL,
    category_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    status ENUM('OPEN', 'FULFILLED') DEFAULT 'OPEN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (requester_user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories (category_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    reviewer_user_id INT NOT NULL,
    target_company_id INT NOT NULL,
    -- Restricción para asegurar que las estrellas estén entre 1 y 5
    rating INT CHECK (
        rating >= 1
        AND rating <= 5
    ),
    review_comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reviewer_user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (target_company_id) REFERENCES companies (company_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS badges (
    badge_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    icon_url VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS company_badges (
    company_id INT NOT NULL,
    badge_id INT NOT NULL,
    awarded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (company_id, badge_id),
    FOREIGN KEY (company_id) REFERENCES companies (company_id) ON DELETE CASCADE,
    FOREIGN KEY (badge_id) REFERENCES badges (badge_id)
);

CREATE TABLE IF NOT EXISTS contact(
    contact_id INT PRIMARY KEY AUTO_INCREMENT,
    category_id INT NOT NULL,
    user_id INT NOT NULL,
    title VARCHAR(250) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories (category_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

