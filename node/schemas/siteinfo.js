const mongoose = require('mongoose');

const { Schema } = mongoose;
const siteinfoSchema = new Schema({
    sitename: {
        type: String,
        required: true,
    },
    dbname: {
        type: String,
        required: true,
    },
    base: {
        type: String,
    },
    generator: {
        type: String,
    },
    case: {
        type: String,
    },
    namespaces: {
        type: {
            id: Array,
            default: [],
        } 
    }
});

module.exports = mongoose.model('Siteinfo', siteinfoSchema);