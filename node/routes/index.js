const express = require('express');

const router = express.Router();

// GET homepage
router.get('/', (req, res) => {
    res.render('index', { title: 'CID phase1' });
});

module.exports = router;
