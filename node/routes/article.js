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

// GET article by title (but temporary working as POST)
router.get('/:title', async (req, res, next) => {
  try{
    const title = req.params.title;
    const hashedTitle = hash.hashStringTo8ByteInt(title);
    const start = title.length;
    const end = hashedTitle.length;
    bptree.insert(title, hashedTitle, start, end);
    res.send('Title: ' + title + ', Hashed title: ' + hashedTitle + ', start: ' + start, ', end: ' + end);
  } catch (err) {
    console.error(err);
    next(err);
  }
});

// for test
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