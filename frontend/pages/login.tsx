import React, { useState } from 'react';
import fetchJson, { FetchError } from '../src/auth/fetch-json';
import { logger } from '../src/logger';
import Form from '../src/auth/form';
import useUser from '../src/auth/use-user';

export default function Login() {
  // here we just check if user is already logged in and redirect to profile
  const { mutateUser } = useUser({
    redirectTo: '/profile/',
    redirectIfFound: true
  });

  const [errorMsg, setErrorMsg] = useState('');

  return (
    <div>
      <div className="login">
        <Form
          errorMessage={errorMsg}
          onSubmit={async function handleSubmit(event) {
            event.preventDefault();

            const body = {
              username: event.currentTarget.username.value
            };

            try {
              await mutateUser(
                await fetchJson('/api/login', {
                  method: 'POST',
                  headers: { 'Content-Type': 'application/json' },
                  body: JSON.stringify(body)
                })
              );
            } catch (error) {
              if (error instanceof FetchError) {
                setErrorMsg(error.data.message);
              } else {
                logger.error('An unexpected error happened:', error);
              }
            }
          }}
        />
      </div>
      <style jsx>{`
        .login {
          max-width: 21rem;
          margin: 0 auto;
          padding: 1rem;
          border: 1px solid #ccc;
          border-radius: 4px;
        }
      `}</style>
    </div>
  );
}