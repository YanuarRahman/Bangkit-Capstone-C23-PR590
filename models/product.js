const mongoose = require('mongoose');

const productSchema = mongoose.Schema({
    name: {
        type: String,
        required: true,
    },

    ingredient: {
        type: String,
        required: true,
    },

    description : {
        type: String,
        required: true,
    },
    richDescription : {
        type: String,
        default: ''
    },
    image : {
        type: String,
        default: ''
    },
    images : [{
        type: String,
    }],
    price: {
        type: Number,
        default: 0
    },
    category: {
        type:mongoose.Schema.Types.ObjectId,
        ref: 'Category',
        required: true
    },
    countInStock: {
        type: Number,
        required: true
    },
    isFeatured: {
        type: Boolean,
        default: false
    },
    dateCreated: {
        type: Date,
        default: Date.now,
    },    

});

exports.Product = mongoose.model('Product', productSchema);
