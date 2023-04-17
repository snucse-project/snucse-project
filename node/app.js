const express = require('express');
const morgan = require('morgan');
// const cookieParser = require('cookie-parser');
// const session = require('express-session');
const dotenv = require('dotenv');
const path = require('path');
const nunjucks = require('nunjucks');
const connect = require('./schemas');
const bplustree = require('./structures/bplustree');

dotenv.config();

// modify here later, only for test
// change app.use statements below together
const bptree = new bplustree.BPlusTree(3);
const indexRouter = require('./routes/index');
// const articleRouter = require('./routes/article')(bptree);
// const contributorRouter = require('./routes/contributor');

const app = express();
app.set('port', process.env.PORT || 7777);

// view engine setup
app.set('view engine', 'html'); // put layout files in ./views
nunjucks.configure('views', {
    express: app,
    watch: true,
})
connect();

app.use(morgan('dev'));
// app.use(express.static(path.join(__dirname, 'public'))); // put style files in ./public
app.use(express.json());
app.use(express.urlencoded({extended: false}));
// app.use(cookieParser());


// modify here later, only for test
app.use('/', indexRouter);
// app.use('/article', articleRouter);
// app.use('/contributor', contributorRouter);
const hash = require('./structures/hash');

// default
app.route('/article')
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
app.get('/article/:title', async (req, res, next) => {
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

app.get('/article/find/:title', async (req, res, next) => {
  try{
    const value = bptree.search(req.params.title);
    res.send('Found Hashed title: ' + value);
  } catch (err) {
    console.error(err);
    next(err);
  }
});

app.route('/contributor')
  .get(async (req, res, next) => {
    try {
      res.send('search as contributor/:username');
    } catch (err) {
      console.error(err);
      next(err);
    }
  });

// GET articles by contributor
app.get('/contributor/:username', async (req, res, next) => {
  try{
    const username = req.params.username;
    const hashedUsername = hash.hashStringTo8ByteInt(username);
    res.send('Contributor: ' + username + ', Hashed username: ' + hashedUsername);
  } catch (err) {
    console.error(err);
    next(err);
  }
});



app.use((req, res, next) => {
    const error =  new Error(`${req.method} ${req.url} router doesn't exist.`);
    error.status = 404;
    next(error);
});

app.use((err, req, res, next) => {
    res.locals.message = err.message;
    res.locals.error = process.env.NODE_ENV !== 'production' ? err : {};
    res.status(err.status || 500);
    res.render('error');
});

app.listen(app.get('port'), () => {
    console.log('Listening at port', app.get('port'));
});

module.exports = app;