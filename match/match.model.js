const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const schema = new Schema({
    matchname: { type: String, unique: true, required: true },
    owner: { type: String, required: true },
    size: { type: String, required: true },
    players: { type: Array, required: true },
    stage: { type: String, default: "CREATED"},
    createdDate: { type: Date, default: Date.now },
    countries: {type: Object, default: {
            brazil: { owner: null, armies: 1 },
            colombia: { owner: null, armies: 1 },
            chile: { owner: null, armies: 1 },
            peru: { owner: null, armies: 1 },
            argentina: { owner: null, armies: 1 },
            uruguay: { owner: null, armies: 1 },
            egypt: { owner: null, armies: 1 },
            ethiopia: { owner: null, armies: 1 },
            zaire: { owner: null, armies: 1 },
            madagascar: { owner: null, armies: 1 },
            southafrica: { owner: null, armies: 1 },
            sahara: { owner: null, armies: 1 }
        }
    }
});

schema.set('toJSON', {
    virtuals: true,
    versionKey: false,
    transform: function (doc, ret) {
        delete ret._id;
        delete ret.hash;
    }
});

module.exports = mongoose.model('Match', schema);