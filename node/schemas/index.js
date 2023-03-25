const mongoose = require('mongoose');

require('dotenv').config();

const connect = () => {
    if (process.env.NODE_ENV !== 'production') {
      mongoose.set('debug', true);
    }
    mongoose.connect(process.env.MONGO_URI, {
    dbName: 'enwiki_test',
    useNewUrlParser: true,
  }, (error) => {
    if (error) {
      console.log('MongoDB connection error', error);
    } else {
      console.log('MongoDB connection success');
    }
  });
};

mongoose.connection.on('error', (error) => {
  console.error('MongoDB connection error', error);
});
mongoose.connection.on('disconnected', () => {
  console.error('MongoDB disconnected. Retry connection.');
  connect();
});

module.exports = connect;