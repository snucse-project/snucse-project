const express = require('express');
const morgan = require('morgan');
// const cookieParser = require('cookie-parser');
// const session = require('express-session');
const dotenv = require('dotenv');
const path = require('path');
const nunjucks = require('nunjucks');
const connect = require('./schemas');

dotenv.config();

// modify here later, only for test
// change app.use statements below together
const indexRouter = require('./routes/index');
const articleRouter = require('./routes/article');

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
app.use('/article', articleRouter);

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
  