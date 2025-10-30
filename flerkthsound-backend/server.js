// server.js
const express = require('express');
const mysql = require('mysql2/promise');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const cors = require('cors');
const multer = require('multer');
const path = require('path');
const fs = require('fs');

const app = express();
const PORT = process.env.PORT || 3000;
const JWT_SECRET = 'flerkthsound-super-secret-key-2024';

// Middleware
app.use(cors());
app.use(express.json());
app.use('/uploads', express.static('uploads'));

// Создаем папки для загрузок, если их нет
const uploadsDir = './uploads';
const audioDir = './uploads/audio';
const imagesDir = './uploads/images';

if (!fs.existsSync(uploadsDir)) fs.mkdirSync(uploadsDir);
if (!fs.existsSync(audioDir)) fs.mkdirSync(audioDir);
if (!fs.existsSync(imagesDir)) fs.mkdirSync(imagesDir);

// Настройка multer для загрузки файлов
const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        if (file.mimetype.startsWith('audio/')) {
            cb(null, './uploads/audio/');
        } else if (file.mimetype.startsWith('image/')) {
            cb(null, './uploads/images/');
        } else {
            cb(new Error('Invalid file type'));
        }
    },
    filename: function (req, file, cb) {
        const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1E9);
        cb(null, file.fieldname + '-' + uniqueSuffix + path.extname(file.originalname));
    }
});

const upload = multer({
    storage: storage,
    limits: {
        fileSize: 50 * 1024 * 1024 // 50MB limit
    },
    fileFilter: function (req, file, cb) {
        if (file.mimetype.startsWith('audio/') || file.mimetype.startsWith('image/')) {
            cb(null, true);
        } else {
            cb(new Error('Only audio and image files are allowed!'), false);
        }
    }
});

// Подключение к MySQL
const dbConfig = {
    host: 'localhost',
    user: 'root', // замените на вашего пользователя MySQL
    password: '', // замените на ваш пароль MySQL
    database: 'FlerkthSound'
};


function formatUser(user) {
    return {
        id: user.id,
        email: user.email,
        name: user.name,
        role: user.role,
        avatarUrl: user.avatar_url,
        bio: user.bio,
        isVerified: Boolean(user.is_verified), // Преобразуем число в boolean
        createdAt: user.created_at
    };
}

// Создание подключения к БД
async function getConnection() {
    return await mysql.createConnection(dbConfig);
}

// Middleware для проверки JWT
const authenticateToken = (req, res, next) => {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];

    if (!token) {
        return res.status(401).json({ error: 'Access token required' });
    }

    jwt.verify(token, JWT_SECRET, (err, user) => {
        if (err) {
            return res.status(403).json({ error: 'Invalid or expired token' });
        }
        req.user = user;
        next();
    });
};

// 📍 РЕГИСТРАЦИЯ
app.post('/api/auth/register', async (req, res) => {
    const { email, password, name, role = 'LISTENER' } = req.body;

    try {
        const connection = await getConnection();

        // Проверяем, существует ли пользователь
        const [existingUsers] = await connection.execute(
            'SELECT id FROM users WHERE email = ?',
            [email]
        );

        if (existingUsers.length > 0) {
            await connection.end();
            return res.status(400).json({ error: 'User already exists' });
        }

        // Хэшируем пароль
        const saltRounds = 10;
        const passwordHash = await bcrypt.hash(password, saltRounds);

        // Создаем пользователя
        const [result] = await connection.execute(
            'INSERT INTO users (id, email, password_hash, name, role) VALUES (UUID(), ?, ?, ?, ?)',
            [email, passwordHash, name, role]
        );

        // Получаем созданного пользователя
        // В функции login (примерно строка 120):
        const [newUsers] = await connection.execute(
            'SELECT id, email, name, bio, avatar_url, role, is_verified, created_at FROM users WHERE id = ?',
            [result.insertId]
        );

        const user = newUsers[0];
        res.json({
            success: true,
            token,
            user: formatUser(user)
        });
        // Преобразуем is_verified в boolean
        user.is_verified = Boolean(user.is_verified);

        // В ответе отправляем преобразованные данные
        res.json({
            success: true,
            token,
            user: {
                id: user.id,
                email: user.email,
                name: user.name,
                role: user.role,
                avatarUrl: user.avatar_url,
                bio: user.bio,
                isVerified: Boolean(user.is_verified) // Преобразуем в boolean
            }
        });


    } catch (error) {
        console.error('Registration error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// 📍 ЛОГИН
app.post('/api/auth/login', async (req, res) => {
    const { email, password } = req.body;

    try {
        const connection = await getConnection();

        // Ищем пользователя
        const [users] = await connection.execute(
            'SELECT * FROM users WHERE email = ?',
            [email]
        );

        if (users.length === 0) {
            await connection.end();
            return res.status(401).json({ error: 'Invalid credentials' });
        }

        const user = users[0];

        // Проверяем пароль
        const isPasswordValid = await bcrypt.compare(password, user.password_hash);
        if (!isPasswordValid) {
            await connection.end();
            return res.status(401).json({ error: 'Invalid credentials' });
        }

        // Проверяем блокировку
        if (user.is_blocked_until && new Date(user.is_blocked_until) > new Date()) {
            await connection.end();
            return res.status(403).json({ error: 'Account temporarily blocked' });
        }

        // Генерируем JWT токен
        const token = jwt.sign(
            { userId: user.id, email: user.email, role: user.role },
            JWT_SECRET,
            { expiresIn: '7d' }
        );

        await connection.end();

        res.json({
            success: true,
            user: formatUser(users[0])
        });

        res.json({
            success: true,
            token,
            user: {
                id: user.id,
                email: user.email,
                name: user.name,
                role: user.role,
                avatarUrl: user.avatar_url,
                bio: user.bio,
                isVerified: user.is_verified,
                followers: 0, // нужно будет доработать
                following: 0  // нужно будет доработать
            }
        });

    } catch (error) {
        console.error('Login error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// 📍 ПОЛУЧЕНИЕ ПРОФИЛЯ (защищенный route)
app.get('/api/users/profile', authenticateToken, async (req, res) => {
    try {
        const connection = await getConnection();
        const [users] = await connection.execute(
            'SELECT id, email, name, bio, avatar_url, role, is_verified, created_at FROM users WHERE id = ?',
            [req.user.userId]
        );

        await connection.end();

        if (users.length === 0) {
            return res.status(404).json({ error: 'User not found' });
        }

        res.json({ success: true, user: users[0] });
    } catch (error) {
        console.error('Profile error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// 📍 ЗАГРУЗКА ТРЕКА
app.post('/api/tracks/upload', authenticateToken, upload.fields([
    { name: 'audioFile', maxCount: 1 },
    { name: 'coverArt', maxCount: 1 }
]), async (req, res) => {
    try {
        const { title, description, genre } = req.body;

        if (!req.files || !req.files['audioFile']) {
            return res.status(400).json({ error: 'Audio file is required' });
        }

        const audioFile = req.files['audioFile'][0];
        const coverArt = req.files['coverArt'] ? req.files['coverArt'][0] : null;

        const connection = await getConnection();

        // Вставляем трек в базу данных
        const [result] = await connection.execute(
            `INSERT INTO tracks (id, artist_id, title, description, genre, audio_file_url, cover_art_url, duration) 
             VALUES (UUID(), ?, ?, ?, ?, ?, ?, ?)`,
            [
                req.user.userId,
                title,
                description || '',
                genre || 'Unknown',
                `/uploads/audio/${audioFile.filename}`,
                coverArt ? `/uploads/images/${coverArt.filename}` : null,
                0 // duration можно вычислить позже
            ]
        );

        // Получаем созданный трек
        const [tracks] = await connection.execute(
            `SELECT t.*, u.name as artist_name 
             FROM tracks t 
             JOIN users u ON t.artist_id = u.id 
             WHERE t.id = ?`,
            [result.insertId]
        );

        await connection.end();

        const track = tracks[0];
        const responseTrack = {
            id: track.id,
            title: track.title,
            artist: track.artist_name,
            artistId: track.artist_id,
            description: track.description,
            genre: track.genre,
            audioFileUrl: track.audio_file_url,
            coverArtUrl: track.cover_art_url,
            duration: track.duration,
            playsCount: track.plays_count,
            likes: 0, // нужно будет доработать
            comments: 0, // нужно будет доработать
            isPublic: track.is_public,
            createdAt: track.created_at
        };

        res.json({
            success: true,
            track: responseTrack
        });

    } catch (error) {
        console.error('Upload error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// 📍 ПОЛУЧЕНИЕ ЛЕНТЫ
app.get('/api/tracks/feed', authenticateToken, async (req, res) => {
    try {
        const connection = await getConnection();

        // Простой запрос для начала - последние треки
        const [tracks] = await connection.execute(
            `SELECT t.*, u.name as artist_name, 
                    (SELECT COUNT(*) FROM likes l WHERE l.track_id = t.id) as likes_count,
                    (SELECT COUNT(*) FROM comments c WHERE c.track_id = t.id) as comments_count
             FROM tracks t 
             JOIN users u ON t.artist_id = u.id 
             WHERE t.is_public = TRUE AND t.is_blocked = FALSE
             ORDER BY t.created_at DESC 
             LIMIT 20`
        );

        await connection.end();

        const formattedTracks = tracks.map(track => ({
            id: track.id,
            title: track.title,
            artist: track.artist_name,
            artistId: track.artist_id,
            description: track.description,
            genre: track.genre,
            audioFileUrl: track.audio_file_url,
            coverArtUrl: track.cover_art_url,
            duration: track.duration,
            playsCount: track.plays_count,
            likes: track.likes_count,
            comments: track.comments_count,
            isPublic: track.is_public,
            createdAt: track.created_at
        }));

        res.json({
            success: true,
            tracks: formattedTracks
        });

    } catch (error) {
        console.error('Feed error:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
});

// 📍 Базовый route для проверки
app.get('/api', (req, res) => {
    res.json({
        success: true,
        message: 'FlerkthSound API is running!',
        version: '1.0.0'
    });
});

// Обработка ошибок multer
app.use((error, req, res, next) => {
    if (error instanceof multer.MulterError) {
        if (error.code === 'LIMIT_FILE_SIZE') {
            return res.status(400).json({ error: 'File too large' });
        }
    }
    res.status(500).json({ error: error.message });
});

// Запуск сервера
app.listen(PORT, () => {
    console.log(`🎵 FlerkthSound Server running on port ${PORT}`);
    console.log(`📱 API available at: http://localhost:${PORT}/api`);
});