const Attendance = require('../models/attendance');
const QRCodeSession = require('../models/qrCodeSession');
const Student = require('../models/student');

exports.markAttendance = async (req, res) => {
    const { srn } = req.params;
    const { qrcodeId } = req.body;
  
    try {
      // Check if the student exists
      const student = await Student.findOne({ Srn: srn });
      if (!student) {
        return res.status(404).json({ message: 'Student not found' });
      }
  
      // Find the QRCodeSession, including faculty name and course name
      const qrCodeSession = await QRCodeSession.findById(qrcodeId).lean();
      if (!qrCodeSession) {
        return res.status(404).json({ message: 'QR Code session not found' });
      }
  
      // Define the query to find an existing attendance record by qrcodeId
      const query = { qrcode_id: qrcodeId };
  
      // Define the update operation to push the new student attendance if not already included
      // And set the faculty name and course name
      const updateOperation = {
        $addToSet: {
          students_attendance: {
            srn: student.Srn,
            timestamp: new Date()
          }
        },
        $set: {
          faculty_name: qrCodeSession.faculty_name, // Set faculty name
          course_name: qrCodeSession.course_name // Set course name
        }
      };
  
      // Set options for findOneAndUpdate to enable upsert and return the updated document
      const options = {
        new: true, // Return the updated document
        upsert: true // Create a new document if one doesn't exist
      };
  
      // Attempt to find and update an existing attendance record, or create a new one if it doesn't exist
      const updatedOrNewAttendance = await Attendance.findOneAndUpdate(query, updateOperation, options);
  
      res.status(201).json({ message: 'Attendance marked successfully', attendance: updatedOrNewAttendance });
    } catch (error) {
      res.status(500).json({ message: error.message });
    }
  };
  