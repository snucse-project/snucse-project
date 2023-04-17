const mongoose = require('mongoose');

const { Schema } = mongoose;
const articleSchema = new Schema({
    title: {
        type: String,
        required: true,
        unique: true,
    },
    ns: {
        type: Number,
        required: true,
    },
    id: {
        type: Number,
        required: true,
    },
    redirect: {
        type: {
            title: String,
            _id: false,
        },
    },
    revision: {
        type: {
            id: Number,
            parentid: Number,
            timestamp: Date,
            contributor: {
                type: {
                    username: String,
                    id: Number,
                    _id: false,
                }
            },
            comment: String,
            model: String,
            format: String,
            text: {
                type: {
                    bytes: Number,
                    content: String,
                    _id: false,
                }
            },
            _id: false,
        }
    } 
});

module.exports = mongoose.model('Article', articleSchema);