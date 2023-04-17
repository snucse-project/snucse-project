const express = require('express');
const hash = require('../structures/hash');

const router = express.Router();

// default
router.route('/')
  .get(async (req, res, next) => {
    try {
      res.send('search as contributor/:username');
    } catch (err) {
      console.error(err);
      next(err);
    }
  });

// GET articles by contributor
router.get('/:username', async (req, res, next) => {
  try{
    const username = req.params.username;
    const hashedUsername = hash.hashStringTo8ByteInt(username);
    res.send('Contributor: ' + username + ', Hashed username: ' + hashedUsername);
  } catch (err) {
    console.error(err);
    next(err);
  }
});

module.exports = router;