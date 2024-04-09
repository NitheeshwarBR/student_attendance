const Course = require('../models/course');
const FacultyCourse = require('../models/facultyCourse');
const QRCode = require('qrcode');
const QRCodeSession = require('../models/qrCodeSession');
const Faculty = require('../models/faculty');

// Controller function to get all courses
exports.getAllCourses = async (req, res) => {
  try {
    const courses = await Course.find();
    res.status(200).json(courses);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

// Controller function to add a course for a specific faculty

exports.saveCourseByDetails = async (req, res) => {
    const { course_id, course_name } = req.body;
  
    try {
      // Check if the course with the same course_id already exists
      const existingCourse = await Course.findOne({ course_id });
      if (existingCourse) {
        return res.status(400).json({ message: 'Course with this course_id already exists' });
      }
  
      // Create a new Course entry
      const newCourse = new Course({ course_id, course_name });
      await newCourse.save();
  
      // Return success response with course details
      res.status(201).json({
        message: 'Course saved successfully',
        course: {
          _id: newCourse._id,
          course_id: newCourse.course_id,
          course_name: newCourse.course_name
        }
      });
    } catch (error) {
      res.status(500).json({ message: error.message });
    }
  };
 
  exports.addCourseForFaculty = async (req, res) => {
    const { facultyId } = req.params;
    const { course_id } = req.body; // Assuming course_id is provided in the request body
  
    try {
      // Find the faculty by ID
      const faculty = await Faculty.findById(facultyId);
      if (!faculty) {
        return res.status(404).json({ message: 'Faculty not found' });
      }
  
      // Find the course by ID
      const course = await Course.findById(course_id);
      if (!course) {
        return res.status(404).json({ message: 'Course not found' });
      }
  
      // Create a new FacultyCourse entry
      const newFacultyCourse = new FacultyCourse({
        faculty: faculty._id,
        course: course._id,
        faculty_name: faculty.name,
        course_name: course.course_name
      });
  
      // Save the new FacultyCourse entry
      await newFacultyCourse.save();
  
      // Return success response with details
      res.status(201).json({
        message: 'Course added successfully for the faculty',
        faculty: {
          _id: faculty._id,
          name: faculty.name,
          email: faculty.email
        },
        course: {
          _id: course._id,
          course_id: course.course_id,
          course_name: course.course_name
        }
      });
    } catch (error) {
      res.status(500).json({ message: error.message });
    }
  };
  exports.generateQrCode = async (req, res) => {
    const { facultyCourseId } = req.params;
  
    try {
      // Find the FacultyCourse by ID
      const facultyCourse = await FacultyCourse.findById(facultyCourseId);
      if (!facultyCourse) {
        return res.status(404).json({ message: 'Faculty and Course not found' });
      }
  
      // Create a new QRCodeSession document
      const newQrCode = new QRCodeSession({
        facultyCourse: facultyCourseId,
        faculty_name: facultyCourse.faculty_name,
        course_name: facultyCourse.course_name,
        timestamp: new Date() // Use current timestamp
      });
  
      // Save the new QRCodeSession document
      await newQrCode.save();
  
      // Update the qr_code_url with the _id of the newQrCode object
      newQrCode.qr_code_url = `https://quickchart.io/qr?text=${newQrCode._id}`;
  
      // Save the updated QRCodeSession document with the correct qr_code_url
      await newQrCode.save();
  
      // Respond with success message
      res.status(201).json({ message: 'QR Code generated successfully', qrCode: newQrCode.qr_code_url });
    } catch (error) {
      // Handle errors
      res.status(500).json({ message: error.message });
    }
  };