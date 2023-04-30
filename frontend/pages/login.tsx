import React from 'react';
import useUser from '../src/auth/use-user';
import { ErrorMapCtx, z, ZodIssueOptionalMessage } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { TextFieldElement } from 'react-hook-form-mui';
import { Box, Button, Paper, Typography } from '@mui/material';
import { toast } from 'react-toastify';

export default function Login() {
  // here we just check if user is already logged in and redirect to profile
  const { mutateUser, user } = useUser({
    redirectTo: '/search',
    redirectIfFound: true
  });

  const formSchema = z.object({
    email: z.string().email(),
    password: z.string().min(1)
  });

  const customErrorMap = () => {
    return (issue: ZodIssueOptionalMessage, ctx: ErrorMapCtx) => {
      if (issue.code === z.ZodIssueCode.invalid_string) {
        if (issue.path.includes('email')) {
          return {
            message: `Bitte geben Sie eine korrekte Email ein!`
          };
        }
        if (issue.path.includes('password')) {
          return {
            message: `Bitte sie Ihr Passwort ein!`
          };
        }
      }
      return { message: ctx.defaultError };
    };
  };

  const { handleSubmit, control } = useForm({
    resolver: zodResolver(formSchema, {
      errorMap: customErrorMap()
    }),
    mode: 'onSubmit',
    defaultValues: {
      email: '',
      password: ''
    }
  });

  const handleFormSubmit = async (data: {
    email: string;
    password: string;
  }) => {
    const body = {
      email: data.email,
      password: data.password
    };

    if (body.email === 'test@mail.ch' && body.password === 'test') {
      await mutateUser({
        isLoggedIn: true,
        roles: ['ADMIN'],
        token: 'myuselesstoken',
        username: 'lokalTestUser'
      });
    }

    try {
      await fetch('/backend/auth/signin', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
      }).then(async (res: any) => {
        if (res.ok) {
          const data = await res.json();
          await mutateUser({
            isLoggedIn: true,
            roles: data.roles,
            token: data.token,
            username: data.username
          });
        }
      });
    } catch (error: any) {
      toast.error(error.message);
    }
  };

  return (
    <div>
      {user?.isLoggedIn ? (
        <Typography component="h1" align="center" variant="h5">
          Sie sind bereits eingeloggt
        </Typography>
      ) : (
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center'
          }}
        >
          <Typography component="h1" variant="h5">
            Willkommen bei Parkship!
          </Typography>
          <form
            style={{ display: 'grid', width: '25%', gap: '20px' }}
            onSubmit={handleSubmit((data) => handleFormSubmit(data))}
          >
            <Paper elevation={3}>
              <TextFieldElement
                required
                fullWidth
                id="email"
                placeholder="admin@parkship.ch"
                label="Email Addresse"
                name="email"
                autoComplete="email"
                autoFocus
                control={control}
                style={{ marginTop: '20px' }}
              />
              <TextFieldElement
                required
                fullWidth
                name="password"
                label="Passwort"
                type="password"
                id="password"
                autoComplete="current-password"
                control={control}
                autoFocus
                style={{ marginTop: '20px' }}
              />
              <Button
                type={'submit'}
                variant={'contained'}
                sx={{ width: '100%', marginTop: '20px' }}
              >
                Einloggen
              </Button>
            </Paper>
          </form>
        </Box>
      )}
    </div>
  );
}
