import { withIronSessionApiRoute } from 'iron-session/next';
import { NextApiRequest, NextApiResponse } from 'next';
import type { User } from 'pages/api/user';
import { sessionOptions } from '../../src/auth/session';

function logoutRoute(req: NextApiRequest, res: NextApiResponse<User>) {
  req.session.destroy();
  res.json({ isLoggedIn: false, login: '', avatarUrl: '' });
}

export default withIronSessionApiRoute(logoutRoute, sessionOptions);
