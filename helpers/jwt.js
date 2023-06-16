const {expressjwt:jwt} = require('express-jwt');

function authJwt() {
    const secret = process.env.SECRET;
    const api = process.env.API_URL;
    return jwt({
        secret,
        algorithms: ['HS256'],
        // isRevoked: isRevoked
    }).unless({
        path: [
            {url: /\/api\/recomenu\/products(.*)/ , methods: ['GET', 'OPTIONS'] },
            {url: /\/api\/recomenu\/categories(.*)/ , methods: ['GET', 'OPTIONS'] },
            {url: /\/api\/recomenu\/categories(.*)/ , methods: ['GET', 'OPTIONS'] },
            // {url: /api/recomenu/products , methods: ['GET', 'OPTIONS'] },
            // {url: /api/recomenu/categories , methods: ['GET', 'OPTIONS'] },
            // {url: /\/api\/recomenu\/users\/login(.*)/ , methods: ['POST', 'OPTIONS'] },
            `/api/recomenu/users/login`,
            `/api/recomenu/users/register`,
            `${api}/users/register`,
            `${api}/users/login`,
        ]
    })
}

// async function isRevoked(req, payload, done) {
//     if(!payload.isAdmin) {
//         done(null, true)
//     }

//     done();
// }



module.exports = authJwt