import type { User } from './user';
import { withIronSessionApiRoute } from 'iron-session/next';
import { NextApiRequest, NextApiResponse } from 'next';
import { sessionOptions } from '../../src/auth/session';

async function loginRoute(req: NextApiRequest, res: NextApiResponse) {
  try {
    const { username, password } = req.body as {
      username: string;
      password: string;
    };
    const response = await fetch('/backend/auth/signin', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ username, password })
    });

    if (response.ok) {
      const user = (await response.json()) as User;
      req.session.user = user;
      await req.session.save();
      res.json(user);
    } else {
      const error = await response.json();
      res.status(response.status).json({ message: error.message });
    }
  } catch (error) {
    res.status(500).json({ message: (error as Error).message });
  }
}

export default withIronSessionApiRoute(loginRoute, sessionOptions);
