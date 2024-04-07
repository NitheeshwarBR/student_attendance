const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const qrCodeSessionSchema = new Schema({
  facultyCourse: {
    type: Schema.Types.ObjectId,
    ref: 'FacultyCourse',
    required: true
  },
  faculty_name :{
    type: String
  },
  course_name:{
    type:String
  },
  qr_code_url: { type: String },
  timestamp: { type: Date, default: Date.now }
});

module.exports = mongoose.model('QRCodeSession', qrCodeSessionSchema);
