import { withIronSessionApiRoute } from 'iron-session/next';
import { NextApiRequest, NextApiResponse } from 'next';
import type { User } from 'pages/api/user';
import { sessionOptions } from '../../src/auth/sessionconfig';

function logoutRoute(req: NextApiRequest, res: NextApiResponse<User>) {
  req.session.destroy();
  res.json({
    isLoggedIn: false,
    role: '',
    username: '',
    token: '',
    name: '',
    greeting: ''
  });
}

export default withIronSessionApiRoute(logoutRoute, sessionOptions);
