const express = require('express');
const Article = require('../schemas/article');

const router = express.Router();

// GET all articles
router.route('/')
  .get(async (req, res, next) => {
    try {
      const articles = await Article.find({}, {_id: false});
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
    const article = await Article.findOne({title: req.params.title});
    if(article == null){
     return res.status(404).send('article is not found with the given title.');
    }
    console.log(article);
    res.json(article);
  } catch (err) {
    console.error(err);
    next(err);
  }
});

// GET informations of article by title
router.get('/info/:title', async (req, res, next) => {
  try{
    const article = await Article.find({title: req.params.title}, {_id: false, revision: false});
    if(typeof article != "undefined"){
      return res.status(404).send('article information is not found with the given title.');
     }
    console.log(article);
    res.json(article);
  } catch (err) {
    console.error(err);
    next(err);
  }
});

// GET content of article by title
router.get('/content/:title', async (req, res, next) => {
  try{
    const article = await Article.find({title: req.params.title}, { _id: false, revision: true});
    if(typeof article != "undefined"){
      return res.status(404).send('article information is not found with the given title.');
     }
    console.log(article);
    res.json(article);
  } catch (err) {
    console.error(err);
    next(err);
  }
});

// GET articles by contributor username
router.get('/contributor/:username', async (req, res, next) => {
  try{
    const article = await Article.find({"revision.contributor.username": req.params.username}, {_id: false});
    if(typeof article != "undefined"){
      return res.status(404).send('article information is not found with the given contributor.');
     }
    console.log(article);
    res.json(article);
  } catch (err) {
    console.error(err);
    next(err);
  }
});

module.exports = router;