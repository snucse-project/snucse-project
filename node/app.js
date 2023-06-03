const express = require('express');
const morgan = require('morgan');
const dotenv = require('dotenv');
const path = require('path');
const nunjucks = require('nunjucks');
const { BTree } = require("node-btree"); // https://www.npmjs.com/package/i2bplustree
const hash = require('./structures/hash');
const cache = require('./structures/cache');
const spawn = require('child_process').spawn;
const fs = require('fs');
const JSONStream = require('JSONStream');

var filepath;

dotenv.config();

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

const bptree = new BTree(comparator);
/* select cache type */
// const titleCache = new cache.CacheLRU(1000000);  // @param memory limit
const titleCache = new cache.CacheClock(1000000);  // @param memory limit
const indexRouter = require('./routes/index');

module.exports.bptreeInstance = bptree;
module.exports.cacheInstance = titleCache;
const prefetch = require('./prefetch');

const app = express();
app.set('port', process.env.PORT || 7777);

/* view engine setup */
app.set('view engine', 'html'); // put layout files in ./views
nunjucks.configure('views', {
    express: app,
    watch: true,
})

app.use(morgan('dev'));
app.use(express.json());
app.use(express.urlencoded({extended: false}));

app.use('/', indexRouter);

app.route('/article')
  .get(async (req, res, next) => {
    try {
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
    filepath = req.body.path;
    const childPython = spawn('python3', ['parse.py', filepath]);

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
    const cachedArticle = titleCache.find(hashedTitle);
    if (cachedArticle === null) { // Cache miss or Not in dump file
      if (!bptree.has(hashedTitle)) {
        res.status(404).send(`Article "${title}" doesn't exist.`);
      }
      else {
        const value = bptree.get(hashedTitle);
        res.set('Content-Type', 'text/html');
        res.write(`title: ${title}<br/>`);
        res.write(`contributor: ${value.username}<br/>`);
        res.write(`start, end: (${value.start}, ${value.end})<br/><br/>`);
  
        // Using Byte Offset
        // To compare performance, comment out following lines
        // and uncomment 'Without Using Byte Offset' part below.
        let readArticle = '';
        const stream = fs.createReadStream(filepath, {
          encoding: 'utf8',
          start: value.start,
          end: value.end
        });
        stream
          .on('data', (data) => {
            res.write(data.toString());
            readArticle += data;
          })
          .on('end', () => {
            titleCache.insert(hashedTitle, readArticle, value.end - value.start);
            res.end();
            // prefetch.prefetch(readArticle, filepath);
          });
  
        // Without Using Byte Offset
        /*
        const stream = fs.createReadStream(filepath, {encoding: 'utf8'});
        stream.pipe(JSONStream.parse('*'))
          .on('data', (page) => {
            if (page.title===title) {
              res.write(JSON.stringify(page.revision.text["#text"]));
              return res.end();
            }
          });
        */
      }
    }
    else { // Cache hit
      res.set('Content-Type', 'text/html');
      res.write(`title: ${title}<br/>`);
      res.write(`Cache Hit!<br/><br/>`);
      res.write(cachedArticle);
      res.end();
      // prefetch.prefetch(cachedArticle, filepath);
    }
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