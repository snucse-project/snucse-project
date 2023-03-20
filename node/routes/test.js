const express = require('express');

const router = express.Router();

// GET /test router
router.get('/', (req, res) => {
  res.send('Test page');
});

module.exports = router;