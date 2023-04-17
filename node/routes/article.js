const express = require('express');
const hash = require('../structures/hash');
const router = express.Router();

// default
router.route('/')
  .get(async (req, res, next) => {
    try {
      console.log(bptree.visualize());
      res.send('search as article/:title');
    } catch (err) {
      console.error(err);
      next(err);
    }
  });

// GET article by title
router.get('/:title', async (req, res, next) => {
  try{
    const title = req.params.title;
    const hashedTitle = hash.hashStringTo8ByteInt(title);
    bptree.insert(title, hashedTitle);
    res.send('Title: ' + title + ', Hashed title: ' + hashedTitle);
  } catch (err) {
    console.error(err);
    next(err);
  }
});

router.get('/find/:title', async (req, res, next) => {
  try{
    const value = bptree.search(req.params.title);
    res.send('Found Hashed title: ' + hashedTitle);
  } catch (err) {
    console.error(err);
    next(err);
  }
});

module.exports = router;