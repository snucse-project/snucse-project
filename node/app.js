const express = require('express');
const morgan = require('morgan');
// const cookieParser = require('cookie-parser');
// const session = require('express-session');
const dotenv = require('dotenv');
const path = require('path');
const nunjucks = require('nunjucks');
// const bplustree = require('./structures/bplustree');
const { BTree } = require("node-btree"); // https://www.npmjs.com/package/i2bplustree
const spawn = require('child_process').spawn

dotenv.config();

 /* change degree later */
 function comparator(a, b) {
  if (a > b) {
    return 1;
  }
  else if (a < b) {
    return -1;
  }
  else {
    return 0;
  }
}

const bptree = new BTree(comparator); // use 100 : 199^3 ~ 8M
const indexRouter = require('./routes/index');
// const articleRouter = require('./routes/article')(bptree);
// const contributorRouter = require('./routes/contributor');

const app = express();
app.set('port', process.env.PORT || 7777);

/* view engine setup */
app.set('view engine', 'html'); // put layout files in ./views
nunjucks.configure('views', {
    express: app,
    watch: true,
})

app.use(morgan('dev'));
// app.use(express.static(path.join(__dirname, 'public'))); // put style files in ./public
app.use(express.json());
app.use(express.urlencoded({extended: false}));
// app.use(cookieParser());


app.use('/', indexRouter);
// app.use('/article', articleRouter);
// app.use('/contributor', contributorRouter);

/* not easy to use declared B+ tree in routers
   refactor this part later */
const hash = require('./structures/hash');

app.route('/article')
  .get(async (req, res, next) => {
    try {
      //const visual = bptree.visualize();
      //console.log(visual);
      res.send('search with article/:title');
    } catch (err) {
      console.error(err);
      next(err);
    }
  });

class Value{
  constructor(username, start, end){
    this.username = username;
    this.start = start;
    this.end = end;
  }
}

app.post('/init', function(req, res, next){
  try {
    const path = req.body.path;
    const childPython = spawn('python3', ['parse.py', path]);

    var result = "";
    childPython.stdout.on('data', (data) => {
      result += data.toString();
    });

    childPython.stdout.on('end', () => {
      const parsed_data = JSON.parse(result);
      for (item of parsed_data) {
        bptree.set(hash.hashStringTo8ByteInt(item.title), new Value(item.contributor, item.start, item.end));
      }
      res.send(""+bptree.size);
    });
  } catch (err) {
    console.error(err);
    next(err);
  }
});

app.get('/article/:title', async (req, res, next) => {
  try{
    const title = req.params.title;
    const hashedTitle = hash.hashStringTo8ByteInt(title);
    const start = title.length;
    const end = title.charAt(0);
    console.log(bptree.set(title, new Value(hashedTitle, start, end)));
    const value = bptree.get(req.params.title);
    res.send(title + ': [' + value.username + ', ' + value.start + ', ' + value.end + ']');
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
/* end of messy codes */

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