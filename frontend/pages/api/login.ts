import { withIronSessionApiRoute } from 'iron-session/next';
import { NextApiRequest, NextApiResponse } from 'next';
import { sessionOptions } from '../../src/auth/session';
import { logger } from '../../src/logger';

let url = 'http://localhost:3333/backend/auth/signin';

if(process.env.NODE_ENV === 'production') {
    url = 'http://localhost:8080/backend/auth/signin';
}

async function loginRoute(req: NextApiRequest, res: NextApiResponse) {
  try {
    const { email, password } = req.body as {
      email: string;
      password: string;
    };
    logger.log('loginRoute', email, password);
    const response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });
    logger.log(response, 'loginRoute', response.status);

    if (response.status === 200) {
      const data = await response.json();
      req.session.user = {
        roles: data.roles,
        token: data.token,
        username: data.username,
        isLoggedIn: true
      };
      await req.session.save();
      res.json({
        isLoggedIn: true,
        roles: data.roles,
        username: data.username,
        token: data.token
      });
    }
  } catch (error) {
    res.status(500).json({ message: (error as Error).message });
  }
}

export default withIronSessionApiRoute(loginRoute, sessionOptions);
