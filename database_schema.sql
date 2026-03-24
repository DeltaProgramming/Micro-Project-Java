-- GECT Connect Comprehensive Database Schema
CREATE DATABASE IF NOT EXISTS gect_connect;
USE gect_connect;

-- Team 1 & 2: User Registration, Auth, and Profile Management
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department VARCHAR(50),
    role ENUM('Student', 'Staff') NOT NULL,
    roll_no_emp_id VARCHAR(20) UNIQUE NOT NULL,
    mobile VARCHAR(15),
    password VARCHAR(255) NOT NULL,
    bio TEXT,
    profile_pic_path VARCHAR(255),
    last_seen TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_online BOOLEAN DEFAULT FALSE,
    join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Team 3: Contact Management
CREATE TABLE IF NOT EXISTS contacts (
    contact_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    friend_id INT,
    status ENUM('pending', 'accepted', 'blocked') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE(user_id, friend_id)
);

-- Team 4: Individual Chat Module
CREATE TABLE IF NOT EXISTS messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT,
    receiver_id INT,
    content TEXT,
    message_type ENUM('text', 'emoji', 'media') DEFAULT 'text',
    status ENUM('sent', 'delivered', 'read') DEFAULT 'sent',
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Team 5: Group Chat Module
CREATE TABLE IF NOT EXISTS chat_groups (
    group_id INT AUTO_INCREMENT PRIMARY KEY,
    group_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    group_type ENUM('Departmental', 'Interest') DEFAULT 'Interest',
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS group_members (
    group_id INT,
    user_id INT,
    role ENUM('Admin', 'Member') DEFAULT 'Member',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (group_id, user_id),
    FOREIGN KEY (group_id) REFERENCES chat_groups(group_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS group_messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    group_id INT,
    sender_id INT,
    message_content TEXT NOT NULL,
    message_type ENUM('text', 'emoji', 'media') DEFAULT 'text',
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (group_id) REFERENCES chat_groups(group_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Team 6: Academic Notifications and Media Sharing
CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    sender_id INT,
    target_department VARCHAR(50), -- NULL means all
    file_path VARCHAR(255),
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Team 7: Notifications, Settings and Campus Feed
CREATE TABLE IF NOT EXISTS campus_events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    event_date TIMESTAMP,
    location VARCHAR(255),
    image_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_settings (
    user_id INT PRIMARY KEY,
    theme ENUM('Light', 'Dark') DEFAULT 'Light',
    notification_enabled BOOLEAN DEFAULT TRUE,
    privacy_last_seen ENUM('Everyone', 'Contacts', 'Nobody') DEFAULT 'Everyone',
    privacy_profile_photo ENUM('Everyone', 'Contacts', 'Nobody') DEFAULT 'Everyone',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
