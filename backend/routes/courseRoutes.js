const express = require('express');
const router = express.Router();
const courseController = require('../controllers/courseController');


// Route to get all courses
router.get('/courses', courseController.getAllCourses);
router.post('/courses', courseController.saveCourseByDetails);
// Route to add a course for a specific faculty
    router.post('/courses/:facultyId', courseController.addCourseForFaculty);
router.post('/courses/generate-qrcode/:facultyCourseId',courseController.generateQrCode)
module.exports = router;
