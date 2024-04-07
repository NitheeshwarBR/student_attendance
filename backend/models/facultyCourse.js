const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const facultyCourseSchema = new Schema({
    faculty: {
      type: Schema.Types.ObjectId,
      ref: 'Faculty',
      required: true
    },
    course: {
      type: Schema.Types.ObjectId,
      ref: 'Course',
      required: true
    },
    faculty_name: {
      type: String,
      required: true
    },
    course_name: {
      type: String,
      required: true
    }
  });
module.exports = mongoose.model('FacultyCourse', facultyCourseSchema);
