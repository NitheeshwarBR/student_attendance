const express = require('express');
const router = express.Router();
const attendanceController = require('../controllers/attendanceController');
router.post('/mark-attendance/:srn', attendanceController.markAttendance);

module.exports = router;
