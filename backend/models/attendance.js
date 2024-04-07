const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const studentAttendanceSchema = new Schema({
  srn: { type: String, ref: 'Student' },
  timestamp: { type: Date, default: Date.now }
});

const attendanceSchema = new Schema({
    faculty_id: { type: Schema.Types.ObjectId, ref: 'Faculty' },
    course_id: { type: Schema.Types.ObjectId, ref: 'Course' },
    faculty_name: { type: String }, // Added field
    course_name: { type: String }, // Added field
    students_attendance: [studentAttendanceSchema], // Array of student attendance objects
    qrcode_id: { type: Schema.Types.ObjectId, ref: 'QRCodeSession', required: true }
  });
  

module.exports = mongoose.model('Attendance', attendanceSchema);
