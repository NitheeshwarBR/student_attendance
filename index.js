const express = require('express');
const bodyParser = require('body-parser');
const mysql = require('mysql');

const app = express();
const port = 3000;

// MySQL database connection settings
const db = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: 'Nitheesh@3533',
  database: 'student_attendance'
});

// Connect to MySQL database
db.connect((err) => {
  if (err) {
    throw err;
  }
  console.log('Connected to the database');
});

// Middleware to parse request body
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

// Signup endpoint
app.post('/signup', async(req, res) => {
  const { email, password } = req.body;
  const sql = await `INSERT INTO users (email, password) VALUES ('${email}', '${password}')`;
  await db.query(sql, (err, result) => {
    if (err) {
      res.status(500).send('Error occurred while signing up');
    } else {
      res.status(200).send('Signed up successfully');
    }
  });
});
app.get('/',(req, res)=> {
  res.send({ "hello": "world" })
});
// Start the server
app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
