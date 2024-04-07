const express = require('express');
const router = express.Router();
const authController = require('../controllers/authController');

// Faculty signup route
router.post('/signup', authController.facultySignup);

// Faculty login route
router.post('/login', authController.facultyLogin);

module.exports = router;
