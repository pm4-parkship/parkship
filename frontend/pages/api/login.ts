import { withIronSessionApiRoute } from 'iron-session/next';
import { sessionOptions } from '../../src/auth/sessionconfig';
import { logger } from '../../src/logger';

export default withIronSessionApiRoute(async function loginRoute(req, res) {
  await fetch('http://localhost:8080/backend/auth/signin', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(req.body)
  }).then(async (response: any) => {
    if (response.ok) {
      const data = await response.json();
      // get user from database then:
      logger.log(data);
      req.session.user = {
        isLoggedIn: true,
        role: data.role,
        token: data.token,
        username: data.username,
      };
      await req.session.save();
      res.send({ user: req.session.user });
    }
  });
}, sessionOptions);
