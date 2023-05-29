import { withIronSessionApiRoute } from 'iron-session/next';
import { NextApiRequest, NextApiResponse } from 'next';
import { sessionOptions } from '../../src/auth/sessionconfig';

export interface User {
  isLoggedIn: boolean;
  token: string;
  username: string;
  role: string;
  surname: string;
}

async function userRoute(req: NextApiRequest, res: NextApiResponse<User>) {
  if (req.session.user) {
    // in a real world application you might read the user id from the session and then do a database request
    // to get more information on the user if needed
    res.json({
      ...req.session.user,
      surname: req.session.user.surname,
      username: req.session.user.username,
      token: req.session.user.token,
      role: req.session.user.role,
      isLoggedIn: true
    });
  } else {
    res.status(401).json({
      isLoggedIn: false,
      username: '',
      token: '',
      role: '',
      surname: ''
    });
  }
}

export default withIronSessionApiRoute(userRoute, sessionOptions);
