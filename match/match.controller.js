const express = require('express');
const router = express.Router();
const matchService = require('./match.service');

router.post('/create', create);
router.get('/', getAll);
router.get('/:id', getById);
router.put('/join/:id', join);
router.put('/leave/:id', leave);
router.delete('/:id', _delete);

module.exports = router;

function create(req, res, next) {
    matchService.create(req.body)
        .then(match => res.json(match))
        .catch(err => next(err));
}

function getAll(req, res, next) {
    matchService.getAll()
        .then(matchs => res.json(matchs))
        .catch(err => next(err));
}

function getById(req, res, next) {
    matchService.getById(req.params.id)
        .then(match => match ? res.json(match) : res.sendStatus(404))
        .catch(err => next(err));
}

function join(req, res, next) {
    matchService.join(req.params.id, req.body)
        .then(() => res.json({}))
        .catch(err => next(err));
}

function leave(req, res, next) {
    matchService.leave(req.params.id, req.body)
        .then(() => res.json({}))
        .catch(err => next(err));
}

function _delete(req, res, next) {
    matchService.delete(req.params.id)
        .then(() => res.json({}))
        .catch(err => next(err));
}