import React from 'react';
import { withIronSessionSsr } from 'iron-session/next';
import { User } from 'pages/api/user';

import { InferGetServerSidePropsType } from 'next';
import { sessionOptions } from '../../src/auth/session';
import {logger} from "../../src/logger";

export default function SsrProfile({
  user
}: InferGetServerSidePropsType<typeof getServerSideProps>) {
  logger.log('user in the profile page', user);
  return (
<div>
  {user?.isLoggedIn && (
      <>
        <p style={{ fontStyle: 'italic' }}>
          Public data, from{' '}
          <a href={`https://github.com/${user.username}`}>
            https://github.com/{user.username}
          </a>
          , reduced to `login` and `avatar_url`.
        </p>
        <pre>{JSON.stringify(user, null, 2)}</pre>
      </>
  )}
    </div>
  );
}

export const getServerSideProps = withIronSessionSsr(async function ({
  req,
  res
}) {
  const user = req.session.user;

  if (user === undefined) {
    res.setHeader('location', '/login');
    res.statusCode = 302;
    res.end();
    return {
      props: {
        user: { isLoggedIn: false, token: '', username: '' } as User
      }
    };
  }

  return {
    props: { user: req.session.user }
  };
},
sessionOptions);
