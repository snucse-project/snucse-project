const express = require('express');
const Article = require('../schemas/article');

const router = express.Router();

// GET all articles
router.route('/')
  .get(async (req, res, next) => {
    try {
      const articles = await Article.find({});
      console.log(articles);
      res.json(articles);
    } catch (err) {
      console.error(err);
      next(err);
    }
  });
  
// GET article by title
router.get('/:title', async (req, res, next) => {
  try{
    const article = await Article.find({title: req.params.title});
    console.log(article);
    res.json(article);
  } catch (err) {
    console.error(err);
    next(err);
  }
});

module.exports = router;