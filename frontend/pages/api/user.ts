import { withIronSessionApiRoute } from 'iron-session/next';
import { NextApiRequest, NextApiResponse } from 'next';
import { sessionOptions } from '../../src/auth/session';

export interface User {
  isLoggedIn: boolean;
  token: string;
  username: string;
  roles: string[];
}

async function userRoute(req: NextApiRequest, res: NextApiResponse<User>) {
  if (req.session.user) {
    // in a real world application you might read the user id from the session and then do a database request
    // to get more information on the user if needed
    res.json({
      ...req.session.user,
      username: req.session.user.username,
      token: req.session.user.token,
      roles: req.session.user.roles,
      isLoggedIn: true
    });
  } else {
    res.json({
      isLoggedIn: false,
      username: '',
      token: '',
      roles: []
    });
  }
}

export default withIronSessionApiRoute(userRoute, sessionOptions);
