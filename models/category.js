const mongoose = require('mongoose');

const categorySchema = mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    foodType:{
        type: String,
        required: true
    },
})

exports.Category = mongoose.model('Category', categorySchema);
