const bcrypt = require('bcrypt');
const Student = require('../models/student');

exports.signup = async (req, res) => {
  const { Srn,email, password } = req.body;

  try {
    const existingStudent = await Student.findOne({ $or: [{ Srn }, { email }] });
    if (existingStudent) {
      return res.status(400).json({ message: 'Student already exists with this SRN or email' });
    }
    const hashedPassword = await bcrypt.hash(password, 10);
    const newStudent = new Student({ Srn, email, password: hashedPassword });
    await newStudent.save();

    res.status(201).json({ message: 'Student registered successfully' });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};
exports.login = async (req, res) => {
    const { Srn, password } = req.body;
  
    try {
    
      const student = await Student.findOne({ Srn });
      if (!student) {
        return res.status(404).json({ message: 'Student not found' });
      }
  
   
      const isPasswordMatch = await bcrypt.compare(password, student.password);
      if (!isPasswordMatch) {
        return res.status(401).json({ message: 'Invalid credentials' });
      }
  
    
      res.status(200).json({ message: 'Login successful' });
    } catch (error) {
      res.status(500).json({ message: error.message });
    }
  };