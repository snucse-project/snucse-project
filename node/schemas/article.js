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
        },
    },
    revision: {
        type: {
            id: Number,
            parentid: Number,
            timestamp: Date,
            Contributor: {
                type: {
                    username: String,
                    id: Number,
                }
            },
            comment: String,
            model: String,
            format: String,
            text: {
                type: {
                    bytes: Number,
                    content: String,
                }
            }
        }
    } 
});

module.exports = mongoose.model('Article', articleSchema);