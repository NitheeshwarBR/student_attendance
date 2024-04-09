const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const Faculty = require('../models/faculty');
const config = require('../config/config');

exports.facultySignup = async (req, res) => {
  try {
    const { name, email, password } = req.body;
    const hashedPassword = await bcrypt.hash(password, 10);
    const faculty = new Faculty({ name, email, password: hashedPassword });
    await faculty.save();
    res.status(201).json({ message: 'Faculty registered successfully' });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

exports.facultyLogin = async (req, res) => {
  try {
    const { email, password } = req.body;
    const faculty = await Faculty.findOne({ email });
    if (!faculty) {
      return res.status(404).json({ message: 'Faculty not found' });
    }
    const isMatch = await bcrypt.compare(password, faculty.password);
    if (!isMatch) {
      return res.status(401).json({ message: 'Invalid credentials' });
    }
    const token = jwt.sign({ facultyId: faculty._id }, config.secretKey, { expiresIn: '1h' });
    res.status(200).json({ facultyId:faculty._id });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};
